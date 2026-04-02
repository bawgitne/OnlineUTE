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

    @Override
    public Course createCourse(Course course) {
        return courseDAO.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseDAO.update(course);
    }

    @Override
    public Course deleteCourse(Course course) {
        return courseDAO.delete(course);
    }

    @Override
    public Course findById(Long id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public PagedResult<Course> searchCourses(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return courseDAO.search(keyword.trim(), pageRequest);
    }

    @Override
    public PagedResult<Course> searchCourses(String keyword, int page, int pageSize) {
        return searchCourses(keyword, PaginationSupport.normalize(page, pageSize));
    }
}
