package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import java.util.List;
import java.util.Optional;

public interface CourseSectionService {
    // tạo lớp học phần mới
    CourseSection createSection(CourseSection section);
    
    // tạo lớp học phần cho một đợt đăng ký cụ thể
    CourseSection createSectionForBatch(RegistrationBatch registrationBatch, CourseSection section);
    
    // cập nhật thông tin lớp học phần
    CourseSection updateSection(CourseSection section);
    
    // xóa lớp học phần
    void deleteSection(CourseSection section);
    
    // lấy lớp học phần theo ID
    Optional<CourseSection> getSectionById(Long id);
    
    // lấy danh sách lớp học phần theo học kỳ
    List<CourseSection> getSectionsByTerm(Long termId);

    // lấy danh sách lớp học phần theo đợt đăng ký
    List<CourseSection> getSectionsByBatch(Long registrationBatchId);
    
    // lấy toàn bộ lớp học phần
    List<CourseSection> getAllSections();
}
