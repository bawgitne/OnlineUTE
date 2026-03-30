package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseRegistrationDAO;
import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.EnumType.RegistrationStatus;
import com.bangcompany.onlineute.Service.CourseRegistrationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CourseRegistrationServiceImpl implements CourseRegistrationService {
    private final CourseRegistrationDAO registrationDAO;
    private final StudentDAO studentDAO;
    private final CourseSectionDAO courseSectionDAO;

    public CourseRegistrationServiceImpl(CourseRegistrationDAO registrationDAO, StudentDAO studentDAO, CourseSectionDAO courseSectionDAO) {
        this.registrationDAO = registrationDAO;
        this.studentDAO = studentDAO;
        this.courseSectionDAO = courseSectionDAO;
    }

    @Override
    public CourseRegistration registerToSection(CourseRegistration registration) {
        return registrationDAO.save(registration);
    }

    @Override
    public CourseRegistration registerStudentToSection(Long studentId, Long sectionId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên."));
        CourseSection section = courseSectionDAO.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lớp học phần."));

        RegistrationBatch batch = section.getRegistrationBatch();
        if (batch == null) {
            throw new IllegalArgumentException("Lớp học phần này chưa thuộc đợt đăng ký nào.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(batch.getOpenAt()) || now.isAfter(batch.getCloseAt())) {
            throw new IllegalArgumentException("Đợt đăng ký này hiện không mở.");
        }

        if (registrationDAO.isRegistered(studentId, sectionId)) {
            throw new IllegalArgumentException("Sinh viên đã đăng ký lớp học phần này.");
        }

        Integer currentCapacity = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
        Integer maxCapacity = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
        if (currentCapacity >= maxCapacity) {
            throw new IllegalArgumentException("Lớp học phần đã đủ số lượng.");
        }

        validateStudentScheduleConflict(studentId, section);

        CourseRegistration registration = new CourseRegistration();
        registration.setStudent(student);
        registration.setCourseSection(section);
        registration.setStatus(RegistrationStatus.APPROVED);
        registration.setRegDate(LocalDate.now());

        CourseRegistration savedRegistration = registrationDAO.save(registration);
        section.setCurrentCapacity(currentCapacity + 1);
        courseSectionDAO.save(section);

        return savedRegistration;
    }

    @Override
    public void cancelRegistration(CourseRegistration registration) {
        registrationDAO.delete(registration);
    }

    @Override
    public Optional<CourseRegistration> getRegistrationById(Long id) {
        return registrationDAO.findById(id);
    }

    @Override
    public List<CourseRegistration> getRegistrationsByStudent(Long studentId) {
        return registrationDAO.findByStudentId(studentId);
    }

    @Override
    public List<CourseRegistration> getRegistrationsBySection(Long sectionId) {
        return registrationDAO.findByCourseSectionId(sectionId);
    }

    @Override
    public boolean isStudentRegistered(Long studentId, Long sectionId) {
        return registrationDAO.isRegistered(studentId, sectionId);
    }

    private void validateStudentScheduleConflict(Long studentId, CourseSection targetSection) {
        List<CourseRegistration> existingRegistrations = registrationDAO.findByStudentId(studentId);
        for (CourseRegistration existingRegistration : existingRegistrations) {
            CourseSection existingSection = existingRegistration.getCourseSection();
            if (existingSection == null || existingSection.getId() == null) {
                continue;
            }
            if (existingSection.getId().equals(targetSection.getId())) {
                continue;
            }
            if (isOverlapping(existingSection, targetSection)) {
                throw new IllegalArgumentException("Lớp học phần này bị trùng lịch với lớp " + existingSection.getSectionCode() + ".");
            }
        }
    }

    private boolean isOverlapping(CourseSection left, CourseSection right) {
        if (left.getDayOfWeek() == null || right.getDayOfWeek() == null) {
            return false;
        }
        if (!left.getDayOfWeek().equals(right.getDayOfWeek())) {
            return false;
        }
        if (left.getStartSlot() == null || left.getEndSlot() == null || right.getStartSlot() == null || right.getEndSlot() == null) {
            return false;
        }
        return left.getStartSlot() <= right.getEndSlot() && left.getEndSlot() >= right.getStartSlot();
    }
}
