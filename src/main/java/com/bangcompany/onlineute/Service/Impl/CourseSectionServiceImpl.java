package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.Service.CourseSectionService;
import com.bangcompany.onlineute.Service.ScheduleService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseSectionServiceImpl implements CourseSectionService {
    private final CourseSectionDAO courseSectionDAO;
    private final ScheduleService scheduleService;

    public CourseSectionServiceImpl(CourseSectionDAO courseSectionDAO, ScheduleService scheduleService) {
        this.courseSectionDAO = courseSectionDAO;
        this.scheduleService = scheduleService;
    }

    // tạo mới lớp học phần và tự động sinh lịch học chi tiết cho từng tuần
    @Override
    public CourseSection createSection(CourseSection section) {
        validateSection(section);
        applyDerivedDates(section);
        CourseSection savedSection = courseSectionDAO.save(section);
        // Sau khi lưu lớp học phần, tiến hành sinh lịch học cho các tuần
        scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
        return savedSection;
    }

    // tạo lớp học phần thuộc về một đợt đăng ký cụ thể
    @Override
    public CourseSection createSectionForBatch(RegistrationBatch registrationBatch, CourseSection section) {
        if (registrationBatch == null) {
            throw new IllegalArgumentException("Đợt đăng ký không được để trống.");
        }
        section.setRegistrationBatch(registrationBatch);
        section.setTerm(registrationBatch.getTerm());
        return createSection(section);
    }

    // cập nhật thông tin lớp học phần và cập nhật lại toàn bộ lịch học liên quan
    @Override
    public CourseSection updateSection(CourseSection section) {
        validateSection(section);
        applyDerivedDates(section);
        CourseSection savedSection = courseSectionDAO.update(section);
        // Cập nhật lại lịch học tuần vì thời gian hoặc phòng học có thể đã thay đổi
        scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
        return savedSection;
    }

    // xóa lớp học phần cùng toàn bộ lịch học của nó
    @Override
    public void deleteSection(CourseSection section) {
        if (section != null && section.getId() != null) {
            scheduleService.regenerateSectionSchedules(section.getId(), List.of());
        }
        courseSectionDAO.delete(section);
    }

    // lấy thông tin lớp học phần theo ID
    @Override
    public Optional<CourseSection> getSectionById(Long id) {
        return courseSectionDAO.findById(id);
    }

    // lấy danh sách các lớp học phần trong một học kỳ cụ thể
    @Override
    public List<CourseSection> getSectionsByTerm(Long termId) {
        return courseSectionDAO.findByTermId(termId);
    }

    // lấy danh sách các lớp học phần thuộc một đợt đăng ký
    @Override
    public List<CourseSection> getSectionsByBatch(Long registrationBatchId) {
        return courseSectionDAO.findByRegistrationBatchId(registrationBatchId);
    }

    // lấy toàn bộ danh sách lớp học phần trong hệ thống
    @Override
    public List<CourseSection> getAllSections() {
        return courseSectionDAO.findAll();
    }

    // kiểm tra tính hợp lệ của dữ liệu lớp học phần
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
        // Kiểm tra xem giảng viên hoặc phòng có bị trùng lịch với lớp khác không
        validateScheduleConflicts(section);
    }

    // kiểm tra các xung đột lịch học (trùng giảng viên hoặc trùng phòng học)
    private void validateScheduleConflicts(CourseSection section) {
        List<CourseSection> conflictingSections = courseSectionDAO.findConflictingSections(
                section.getTerm().getId(),
                section.getDayOfWeek(),
                section.getStartSlot(),
                section.getEndSlot()
        );

        for (CourseSection conflictingSection : conflictingSections) {
            // Bỏ qua nếu chính là lớp học phần đang xét
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

    // kiểm tra hai lớp học phần có chung giảng viên không
    private boolean hasSameLecturer(CourseSection left, CourseSection right) {
        return left.getLecturer() != null
                && right.getLecturer() != null
                && left.getLecturer().getId() != null
                && left.getLecturer().getId().equals(right.getLecturer().getId());
    }

    // kiểm tra hai lớp học phần có dùng chung phòng không
    private boolean hasSameRoom(CourseSection left, CourseSection right) {
        return left.getRoom() != null
                && right.getRoom() != null
                && left.getRoom().equalsIgnoreCase(right.getRoom());
    }

    // tính ngày bắt đầu kết thúc bằng tuần học và ngày bắt đầu của đợt đăng ký
    private void applyDerivedDates(CourseSection section) {
        LocalDate commonStartDate = section.getRegistrationBatch().getCommonStartDate();
        LocalDate firstStudyDate = calculateFirstStudyDate(commonStartDate, section.getDayOfWeek());
        LocalDate lastStudyDate = firstStudyDate.plusWeeks(section.getTotalWeeks() - 1L);
        section.setFirstStudyDate(firstStudyDate);
        section.setLastStudyDate(lastStudyDate);
    }

    // xây dựng danh sách các buổi học chi tiết cho từng tuần dựa trên các thông tin của lớp học phần
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

    // tính toán ngày học đầu tiên dựa trên ngày bắt đầu chung của kỳ và thứ trong tuần mà lớp học đó diễn ra
    private LocalDate calculateFirstStudyDate(LocalDate commonStartDate, int targetDayOfWeek) {
        int currentDay = commonStartDate.getDayOfWeek().getValue();
        int offset = targetDayOfWeek - currentDay;
        // Nếu ngày học trong tuần nằm trước ngày bắt đầu chung, lịch sẽ lùi sang tuần kế tiếp
        if (offset < 0) {
            offset += 7;
        }
        return commonStartDate.plusDays(offset);
    }
}
