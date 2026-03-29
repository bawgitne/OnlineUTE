package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseDAO;
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
}
