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

    // lưu thông tin đăng ký học phần trực tiếp
    @Override
    public CourseRegistration registerToSection(CourseRegistration registration) {
        return registrationDAO.save(registration);
    }

    // thực hiện việc sinh viên đăng ký vào một lớp học phần
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

        // Kiểm tra xem thời gian hiện tại có nằm trong thời gian mở cửa của đợt đăng ký không
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(batch.getOpenAt()) || now.isAfter(batch.getCloseAt())) {
            throw new IllegalArgumentException("Đợt đăng ký này hiện không mở.");
        }

        // Kiểm tra xem sinh viên đã đăng ký lớp này trước đó chưa
        if (registrationDAO.isRegistered(studentId, sectionId)) {
            throw new IllegalArgumentException("Sinh viên đã đăng ký lớp học phần này.");
        }

        // Kiểm tra sĩ số lớp học phần
        Integer currentCapacity = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
        Integer maxCapacity = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
        if (currentCapacity >= maxCapacity) {
            throw new IllegalArgumentException("Lớp học phần đã đủ số lượng.");
        }

        // Kiểm tra trùng lịch học của sinh viên
        validateStudentScheduleConflict(studentId, section);

        // Tạo bản ghi đăng ký mới
        CourseRegistration registration = new CourseRegistration();
        registration.setStudent(student);
        registration.setCourseSection(section);
        registration.setStatus(RegistrationStatus.APPROVED);
        registration.setRegDate(LocalDate.now());

        CourseRegistration savedRegistration = registrationDAO.save(registration);

        // Cập nhật lại sĩ số hiện tại của lớp học phần
        section.setCurrentCapacity(currentCapacity + 1);
        courseSectionDAO.save(section);

        return savedRegistration;
    }

    // hủy đăng ký một học phần
    @Override
    public void cancelRegistration(CourseRegistration registration) {
        registrationDAO.delete(registration);
    }

    // lấy thông tin đăng ký học phần theo ID
    @Override
    public Optional<CourseRegistration> getRegistrationById(Long id) {
        return registrationDAO.findById(id);
    }

    // lấy toàn bộ các môn học đã đăng ký của một sinh viên
    @Override
    public List<CourseRegistration> getRegistrationsByStudent(Long studentId) {
        return registrationDAO.findByStudentId(studentId);
    }

    // lấy toàn bộ danh sách sinh viên đăng ký của một lớp học phần
    @Override
    public List<CourseRegistration> getRegistrationsBySection(Long sectionId) {
        return registrationDAO.findByCourseSectionId(sectionId);
    }

    // kiểm tra sinh viên đã đăng ký một lớp học phần cụ thể hay chưa
    @Override
    public boolean isStudentRegistered(Long studentId, Long sectionId) {
        return registrationDAO.isRegistered(studentId, sectionId);
    }

    // kiểm tra chồng chéo lịch học giữa lớp đang định đăng ký và các lớp đã đăng ký
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
            // Nếu phát hiện trùng lịch (cùng thứ và khoảng tiết học giao nhau)
            if (isOverlapping(existingSection, targetSection)) {
                throw new IllegalArgumentException("Lớp học phần này bị trùng lịch với lớp " + existingSection.getSectionCode() + ".");
            }
        }
    }

    // kiểm tra xem hai lớp học có bị chồng chéo thời gian học hay không
    private boolean isOverlapping(CourseSection left, CourseSection right) {
        if (left.getDayOfWeek() == null || right.getDayOfWeek() == null) {
            return false;
        }
        // Khác thứ thì không bao giờ trùng lịch
        if (!left.getDayOfWeek().equals(right.getDayOfWeek())) {
            return false;
        }
        if (left.getStartSlot() == null || left.getEndSlot() == null || right.getStartSlot() == null || right.getEndSlot() == null) {
            return false;
        }
        // Kiểm tra sự giao nhau của hai khoảng tiết học
        return left.getStartSlot() <= right.getEndSlot() && left.getEndSlot() >= right.getStartSlot();
    }
}
