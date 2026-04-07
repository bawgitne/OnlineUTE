package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationService {
    // đăng ký một học phần
    CourseRegistration registerToSection(CourseRegistration registration);

    // đăng ký lớp cho sinh viên dựa theo ID
    CourseRegistration registerStudentToSection(Long studentId, Long sectionId);
    
    // hủy đăng ký học phần
    void cancelRegistration(CourseRegistration registration);
    
    // lấy đơn đăng ký theo ID
    Optional<CourseRegistration> getRegistrationById(Long id);
    
    // lấy danh sách đã đăng ký của sinh viên
    List<CourseRegistration> getRegistrationsByStudent(Long studentId);
    
    // lấy danh sách sinh viên đăng ký của lớp
    List<CourseRegistration> getRegistrationsBySection(Long sectionId);
    
    // kiểm tra sinh viên đã đăng ký lớp học phần hay chưa
    boolean isStudentRegistered(Long studentId, Long sectionId);
}
