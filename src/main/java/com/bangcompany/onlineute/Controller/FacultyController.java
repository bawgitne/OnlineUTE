package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Service.FacultyService;

import java.util.List;

public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }
}
