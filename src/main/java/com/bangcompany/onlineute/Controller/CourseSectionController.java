package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Service.CourseSectionService;
import java.util.List;
import java.util.Optional;

public class CourseSectionController {
    private final CourseSectionService sectionService;

    public CourseSectionController(CourseSectionService sectionService) {
        this.sectionService = sectionService;
    }

    public List<CourseSection> getSectionsByTerm(Long termId) {
        return sectionService.getSectionsByTerm(termId);
    }

    public List<CourseSection> getAllSections() {
        return sectionService.getAllSections();
    }

    public Optional<CourseSection> getSectionById(Long id) {
        return sectionService.getSectionById(id);
    }
    
    public CourseSection createSection(CourseSection section) {
        return sectionService.createSection(section);
    }
}
