package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Course;
import java.util.List;

public interface CourseService {
    Course createCourse(Course course);

    Course updateCourse(Course course);

    Course deleteCourse(Course course);

    Course findById(Long id);

    List<Course> getAllCourses();
    PagedResult<Course> searchCourses(String keyword, PageRequest pageRequest);
    PagedResult<Course> searchCourses(String keyword, int page, int pageSize);
}
