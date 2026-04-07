package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Service.CourseService;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;
    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    // tạo mới môn học vào hệ thống
    @Override
    public Course createCourse(Course course) {
        return courseDAO.save(course);
    }

    // cập nhật thông tin môn học đã tồn tại
    @Override
    public Course updateCourse(Course course) {
        return courseDAO.update(course);
    }

    // xóa môn học ra khỏi hệ thống
    @Override
    public Course deleteCourse(Course course) {
        return courseDAO.delete(course);
    }

    // tìm môn học theo mã định danh (ID)
    @Override
    public Course findById(Long id) {
        return courseDAO.findById(id);
    }

    // lấy toàn bộ danh sách môn học
    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    // tìm kiếm môn học dựa trên từ khóa (mã hoặc tên) có phân trang
    @Override
    public PagedResult<Course> searchCourses(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return courseDAO.search(keyword.trim(), pageRequest);
    }

    // tìm kiếm môn học dựa trên từ khóa (số trang và kích thước trang)
    @Override
    public PagedResult<Course> searchCourses(String keyword, int page, int pageSize) {
        return searchCourses(keyword, PaginationSupport.normalize(page, pageSize));
    }
}
