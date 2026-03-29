package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.CourseSection;
import java.util.List;
import java.util.Optional;

public interface CourseSectionDAO {
    CourseSection save(CourseSection courseSection);

    CourseSection update(CourseSection courseSection);

    void delete(CourseSection courseSection);

    Optional<CourseSection> findById(Long id);

    List<CourseSection> findByTermId(Long termId);
    
    List<CourseSection> findAll();
}
