package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Course;
import java.util.List;

public interface CourseService {
    // tạo môn học mới
    Course createCourse(Course course);

    // cập nhật môn học
    Course updateCourse(Course course);

    // xóa môn học
    Course deleteCourse(Course course);

    // tìm môn học theo ID
    Course findById(Long id);

    // lấy toàn bộ môn học
    List<Course> getAllCourses();
    
    // tìm kiếm môn học (phân trang)
    PagedResult<Course> searchCourses(String keyword, PageRequest pageRequest);
    
    // tìm kiếm môn học (số trang và kích thước trang)
    PagedResult<Course> searchCourses(String keyword, int page, int pageSize);
    long countAllCourses();
}
