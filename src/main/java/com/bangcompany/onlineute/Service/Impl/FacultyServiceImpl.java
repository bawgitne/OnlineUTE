package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.FacultyDAO;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Service.FacultyService;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {
    private final FacultyDAO facultyDAO;

    public FacultyServiceImpl(FacultyDAO facultyDAO) {
        this.facultyDAO = facultyDAO;
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyDAO.findAll();
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty is required.");
        }
        return facultyDAO.save(faculty);
    }
}
