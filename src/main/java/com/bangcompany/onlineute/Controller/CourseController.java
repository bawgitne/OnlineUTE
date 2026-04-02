
package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Service.CourseService;

public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    public Course createCourse(Course course) {
        return courseService.createCourse(course);
    }

    public Course updateCourse(Course course) {
        return courseService.updateCourse(course);
    }

    public Course deleteCourse(Course course) {
        return courseService.deleteCourse(course);
    }

    public Course findById(Long id) {
        return courseService.findById(id);
    }

    public PagedResult<Course> searchCourses(String keyword, PageRequest pageRequest) {
        return courseService.searchCourses(keyword, pageRequest);
    }

    public PagedResult<Course> searchCourses(String keyword, int page, int pageSize) {
        return courseService.searchCourses(keyword, page, pageSize);
    }
}
