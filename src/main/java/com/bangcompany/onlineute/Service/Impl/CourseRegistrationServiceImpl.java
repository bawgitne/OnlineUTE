package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.CourseRegistrationDAO;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Service.CourseRegistrationService;
import java.util.List;
import java.util.Optional;

public class CourseRegistrationServiceImpl implements CourseRegistrationService {
    private final CourseRegistrationDAO registrationDAO;

    public CourseRegistrationServiceImpl(CourseRegistrationDAO registrationDAO) {
        this.registrationDAO = registrationDAO;
    }

    @Override
    public CourseRegistration registerToSection(CourseRegistration registration) {
        return registrationDAO.save(registration);
    }

    @Override
    public void cancelRegistration(CourseRegistration registration) {
        registrationDAO.delete(registration);
    }

    @Override
    public Optional<CourseRegistration> getRegistrationById(Long id) {
        return registrationDAO.findById(id);
    }

    @Override
    public List<CourseRegistration> getRegistrationsByStudent(Long studentId) {
        return registrationDAO.findByStudentId(studentId);
    }

    @Override
    public List<CourseRegistration> getRegistrationsBySection(Long sectionId) {
        return registrationDAO.findByCourseSectionId(sectionId);
    }

    @Override
    public boolean isStudentRegistered(Long studentId, Long sectionId) {
        return registrationDAO.isRegistered(studentId, sectionId);
    }
}
