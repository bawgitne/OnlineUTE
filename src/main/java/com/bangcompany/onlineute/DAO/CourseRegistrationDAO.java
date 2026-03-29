package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationDAO {
    CourseRegistration save(CourseRegistration registration);
    
    CourseRegistration update(CourseRegistration registration);
    
    void delete(CourseRegistration registration);
    
    Optional<CourseRegistration> findById(Long id);
    
    List<CourseRegistration> findByStudentId(Long studentId);
    
    List<CourseRegistration> findByCourseSectionId(Long sectionId);
    
    boolean isRegistered(Long studentId, Long sectionId);
}
