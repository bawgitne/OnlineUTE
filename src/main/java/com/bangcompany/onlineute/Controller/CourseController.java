
package com.bangcompany.onlineute.Controller;

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
}
