package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Course;
import java.util.List;

public interface CourseDAO {
    Course save(Course course);

    Course update(Course course);

    Course delete(Course course);

    Course findById(Long id);

    List<Course> findAll();
    PagedResult<Course> search(String keyword, PageRequest pageRequest);
}
