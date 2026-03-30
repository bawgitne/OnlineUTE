package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Service.CourseRegistrationService;
import java.util.List;
import java.util.Optional;

public class CourseRegistrationController {
    private final CourseRegistrationService registrationService;

    public CourseRegistrationController(CourseRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public CourseRegistration register(CourseRegistration registration) {
        return registrationService.registerToSection(registration);
    }

    public CourseRegistration registerStudentToSection(Long studentId, Long sectionId) {
        return registrationService.registerStudentToSection(studentId, sectionId);
    }

    public void cancel(CourseRegistration registration) {
        registrationService.cancelRegistration(registration);
    }

    public List<CourseRegistration> getStudentRegistrations(Long studentId) {
        return registrationService.getRegistrationsByStudent(studentId);
    }

    public boolean isStudentInCourse(Long studentId, Long sectionId) {
        return registrationService.isStudentRegistered(studentId, sectionId);
    }
}
