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

    @Override
    public CourseSection createSection(CourseSection section) {
        validateSection(section);
        applyDerivedDates(section);
        CourseSection savedSection = courseSectionDAO.save(section);
        scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
        return savedSection;
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

    @Override
    public CourseSection updateSection(CourseSection section) {
        validateSection(section);
        applyDerivedDates(section);
        CourseSection savedSection = courseSectionDAO.update(section);
        scheduleService.regenerateSectionSchedules(savedSection.getId(), buildSchedules(savedSection));
        return savedSection;
    }

    @Override
    public void deleteSection(CourseSection section) {
        if (section != null && section.getId() != null) {
            scheduleService.regenerateSectionSchedules(section.getId(), List.of());
        }
        courseSectionDAO.delete(section);
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

    private void applyDerivedDates(CourseSection section) {
        LocalDate commonStartDate = section.getRegistrationBatch().getCommonStartDate();
        LocalDate firstStudyDate = calculateFirstStudyDate(commonStartDate, section.getDayOfWeek());
        LocalDate lastStudyDate = firstStudyDate.plusWeeks(section.getTotalWeeks() - 1L);
        section.setFirstStudyDate(firstStudyDate);
        section.setLastStudyDate(lastStudyDate);
    }

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

    private LocalDate calculateFirstStudyDate(LocalDate commonStartDate, int targetDayOfWeek) {
        int currentDay = commonStartDate.getDayOfWeek().getValue();
        int offset = targetDayOfWeek - currentDay;
        if (offset < 0) {
            offset += 7;
        }
        return commonStartDate.plusDays(offset);
    }
}
