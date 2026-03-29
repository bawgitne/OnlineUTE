package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Course;

public interface CourseDAO {
    Course save(Course course);

    Course update(Course course);

    Course delete(Course course);

    Course findById(Long id);
}
