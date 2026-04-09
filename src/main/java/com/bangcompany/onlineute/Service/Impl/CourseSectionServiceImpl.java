package com.bangcompany.onlineute.Service.Impl;


import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.DAO.CourseRegistrationDAO;
import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Service.AnnouncementService;
import com.bangcompany.onlineute.Service.CourseSectionService;
import com.bangcompany.onlineute.Service.ScheduleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseSectionServiceImpl implements CourseSectionService {
    private final CourseSectionDAO courseSectionDAO;
    private final ScheduleService scheduleService;
    private final CourseRegistrationDAO registrationDAO;
    private final AnnouncementService announcementService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CourseSectionServiceImpl(CourseSectionDAO courseSectionDAO, ScheduleService scheduleService, CourseRegistrationDAO registrationDAO, AnnouncementService announcementService) {
        this.courseSectionDAO = courseSectionDAO;
        this.scheduleService = scheduleService;
        this.registrationDAO = registrationDAO;
        this.announcementService = announcementService;
    }

    // tạo lớp học phần mới, tính ngày học và xếp lịch tự động
    @Override
    public CourseSection createSection(CourseSection section) {
        return JpaUtil.doInTransaction(() -> {
            validateSection(section);
            applyDerivedDates(section);
            CourseSection savedSection = courseSectionDAO.save(section);
            scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
            notifyLecturer(savedSection);
            return savedSection;
        });
    }

    @Override
    public CourseSection createSectionForBatch(RegistrationBatch registrationBatch, CourseSection section) {
        if (registrationBatch == null) {
            throw new IllegalArgumentException("Đợt đăng ký không được để trống.");
        }
        section.setRegistrationBatch(registrationBatch);
        section.setTerm(registrationBatch.getTerm());
        return createSection(section);
    }

    // cập nhật thông tin lớp và vẽ lại lịch học
    @Override
    public CourseSection updateSection(CourseSection section) {
        return JpaUtil.doInTransaction(() -> {
            validateSection(section);
            applyDerivedDates(section);
            CourseSection savedSection = courseSectionDAO.update(section);
            scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
            return savedSection;
        });
    }

    // xóa lớp và các dữ liệu liên quan (lịch, đăng ký)
    @Override
    public void deleteSection(CourseSection section) {
        if (section == null || section.getId() == null) {
            return;
        }
        JpaUtil.doInTransaction(() -> {
            scheduleService.regenerateSectionSchedules(section.getId(), List.of());
            List<CourseRegistration> registrations = registrationDAO.findByCourseSectionId(section.getId());
            for (CourseRegistration registration : registrations) {
                registrationDAO.delete(registration);
            }
            courseSectionDAO.delete(section);
        });
    }

    @Override
    public Optional<CourseSection> getSectionById(Long id) {
        return courseSectionDAO.findById(id);
    }

    @Override
    public List<CourseSection> getSectionsByTerm(Long termId) {
        return courseSectionDAO.findByTermId(termId);
    }

    @Override
    public List<CourseSection> getSectionsByBatch(Long registrationBatchId) {
        return courseSectionDAO.findByRegistrationBatchId(registrationBatchId);
    }

    @Override
    public List<CourseSection> getAllSections() {
        return courseSectionDAO.findAll();
    }

    // check các trường bắt buộc ko được để trống
    private void validateSection(CourseSection section) {
        if (section == null) {
            throw new IllegalArgumentException("Lớp học phần không được để trống.");
        }
        if (section.getRegistrationBatch() == null) {
            throw new IllegalArgumentException("Lớp học phần phải thuộc một đợt đăng ký.");
        }
        if (section.getCourse() == null) {
            throw new IllegalArgumentException("Môn học không được để trống.");
        }
        if (section.getLecturer() == null) {
            throw new IllegalArgumentException("Giảng viên không được để trống.");
        }
        if (section.getTerm() == null) {
            throw new IllegalArgumentException("Học kỳ không được để trống.");
        }
        if (section.getRoom() == null || section.getRoom().isBlank()) {
            throw new IllegalArgumentException("Phòng học không được để trống.");
        }
        if (section.getMaxCapacity() == null || section.getMaxCapacity() <= 0) {
            throw new IllegalArgumentException("Số lượng tối đa phải lớn hơn 0.");
        }
        if (section.getDayOfWeek() == null || section.getDayOfWeek() < 1 || section.getDayOfWeek() > 7) {
            throw new IllegalArgumentException("Thứ học phải nằm trong khoảng 1 đến 7.");
        }
        if (section.getStartSlot() == null || section.getEndSlot() == null || section.getStartSlot() > section.getEndSlot()) {
            throw new IllegalArgumentException("Tiết học không hợp lệ.");
        }
        if (section.getTotalWeeks() == null || section.getTotalWeeks() <= 0) {
            throw new IllegalArgumentException("Số tuần học phải lớn hơn 0.");
        }
        if (section.getCurrentCapacity() == null) {
            section.setCurrentCapacity(0);
        }
        validateScheduleConflicts(section);
    }

    // check xem gv hoặc phòng có bị dính lịch lớp khác ko
    private void validateScheduleConflicts(CourseSection section) {
        List<CourseSection> conflictingSections = courseSectionDAO.findConflictingSections(
                section.getTerm().getId(),
                section.getDayOfWeek(),
                section.getStartSlot(),
                section.getEndSlot()
        );

        for (CourseSection conflictingSection : conflictingSections) {
            if (section.getId() != null && section.getId().equals(conflictingSection.getId())) {
                continue;
            }

            if (hasSameLecturer(section, conflictingSection)) {
                throw new IllegalArgumentException("Giảng viên bị trùng lịch với lớp học phần " + conflictingSection.getSectionCode() + ".");
            }

            if (hasSameRoom(section, conflictingSection)) {
                throw new IllegalArgumentException("Phòng học bị trùng lịch với lớp học phần " + conflictingSection.getSectionCode() + ".");
            }
        }
    }

    private boolean hasSameLecturer(CourseSection left, CourseSection right) {
        return left.getLecturer() != null
                && right.getLecturer() != null
                && left.getLecturer().getId() != null
                && left.getLecturer().getId().equals(right.getLecturer().getId());
    }

    private boolean hasSameRoom(CourseSection left, CourseSection right) {
        return left.getRoom() != null
                && right.getRoom() != null
                && left.getRoom().equalsIgnoreCase(right.getRoom());
    }

    // tính ngày bắt đầu và kết thúc dựa trên đợt đăng ký và số tuần học
    private void applyDerivedDates(CourseSection section) {
        LocalDate commonStartDate = section.getRegistrationBatch().getCommonStartDate();
        LocalDate firstStudyDate = calculateFirstStudyDate(commonStartDate, section.getDayOfWeek());
        LocalDate lastStudyDate = firstStudyDate.plusWeeks(section.getTotalWeeks() - 1L);
        section.setFirstStudyDate(firstStudyDate);
        section.setLastStudyDate(lastStudyDate);
    }

    // gửi thông báo cho gv khi được phân công dạy lớp này
    private void notifyLecturer(CourseSection section) {
        if (announcementService == null || section == null || section.getLecturer() == null || section.getId() == null) {
            return;
        }

        String senderName = "Admin";
        Admin admin = SessionManager.getCurrentAdmin();
        if (admin != null && admin.getFullName() != null && !admin.getFullName().isBlank()) {
            senderName = admin.getFullName();
        } else {
            Account account = SessionManager.getCurrentAccount();
            if (account != null && account.getUsername() != null && !account.getUsername().isBlank()) {
                senderName = account.getUsername();
            }
        }

        String courseName = section.getCourse() == null ? "" : section.getCourse().getFullName();
        String sectionCode = section.getSectionCode() == null ? "" : section.getSectionCode();
        String room = section.getRoom() == null ? "" : section.getRoom();
        String startDate = section.getFirstStudyDate() == null ? "" : DATE_FORMATTER.format(section.getFirstStudyDate());
        String endDate = section.getLastStudyDate() == null ? "" : DATE_FORMATTER.format(section.getLastStudyDate());

        String title = "Phân công giảng dạy";
        String content = "Tiết " + section.getStartSlot() + " - " + section.getEndSlot() +
                " đã được xếp để dạy môn " + courseName +
                " (Mã lớp: " + sectionCode + "), vào Thứ " + section.getDayOfWeek() +
                ", phòng " + room + ", từ " + startDate + " đến " + endDate + ".";

        announcementService.createAnnouncement(title, content, "COURSE_SECTION", section.getId(), senderName);
    }

    // tạo danh sách các buổi học cho từng tuần
    private List<Schedule> buildSchedules(CourseSection section) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDate firstStudyDate = section.getFirstStudyDate();

        for (int week = 1; week <= section.getTotalWeeks(); week++) {
            Schedule schedule = new Schedule();
            schedule.setCourseSection(section);
            schedule.setDayOfWeek(section.getDayOfWeek());
            schedule.setStartSlot(section.getStartSlot());
            schedule.setEndSlot(section.getEndSlot());
            schedule.setRoom(section.getRoom());
            schedule.setWeekNumber(week);
            schedule.setStudyDate(firstStudyDate.plusWeeks(week - 1L));
            schedules.add(schedule);
        }

        return schedules;
    }

    // tìm ngày đầu tiên học dựa trên thứ học trong tuần
    private LocalDate calculateFirstStudyDate(LocalDate commonStartDate, int targetDayOfWeek) {
        int currentDay = commonStartDate.getDayOfWeek().getValue();
        int offset = targetDayOfWeek - currentDay;
        if (offset < 0) {
            offset += 7;
        }
        return commonStartDate.plusDays(offset);
    }
}
