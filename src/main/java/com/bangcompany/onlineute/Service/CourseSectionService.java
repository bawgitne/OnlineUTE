package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.CourseSection;
import java.util.List;
import java.util.Optional;

public interface CourseSectionService {
    CourseSection createSection(CourseSection section);
    
    CourseSection updateSection(CourseSection section);
    
    void deleteSection(CourseSection section);
    
    Optional<CourseSection> getSectionById(Long id);
    
    List<CourseSection> getSectionsByTerm(Long termId);
    
    List<CourseSection> getAllSections();
}
