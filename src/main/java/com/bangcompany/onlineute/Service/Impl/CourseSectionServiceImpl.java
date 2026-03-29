package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Service.CourseSectionService;
import java.util.List;
import java.util.Optional;

public class CourseSectionServiceImpl implements CourseSectionService {
    private final CourseSectionDAO courseSectionDAO;

    public CourseSectionServiceImpl(CourseSectionDAO courseSectionDAO) {
        this.courseSectionDAO = courseSectionDAO;
    }

    @Override
    public CourseSection createSection(CourseSection section) {
        return courseSectionDAO.save(section);
    }

    @Override
    public CourseSection updateSection(CourseSection section) {
        return courseSectionDAO.update(section);
    }

    @Override
    public void deleteSection(CourseSection section) {
        courseSectionDAO.delete(section);
    }

    @Override
    public Optional<CourseSection> getSectionById(Long id) {
        return courseSectionDAO.findById(id);
    }

    @Override
    public List<CourseSection> getSectionsByTerm(Long termId) {
        return courseSectionDAO.findByTermId(termId);
    }

    @Override
    public List<CourseSection> getAllSections() {
        return courseSectionDAO.findAll();
    }
}
