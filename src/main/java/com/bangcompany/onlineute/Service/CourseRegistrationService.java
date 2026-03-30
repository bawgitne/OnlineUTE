package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationService {
    CourseRegistration registerToSection(CourseRegistration registration);

    CourseRegistration registerStudentToSection(Long studentId, Long sectionId);
    
    void cancelRegistration(CourseRegistration registration);
    
    Optional<CourseRegistration> getRegistrationById(Long id);
    
    List<CourseRegistration> getRegistrationsByStudent(Long studentId);
    
    List<CourseRegistration> getRegistrationsBySection(Long sectionId);
    
    boolean isStudentRegistered(Long studentId, Long sectionId);
}
