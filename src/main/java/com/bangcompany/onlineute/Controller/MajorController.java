package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Service.MajorService;

import java.util.List;

public class MajorController {
    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    public List<Major> getAllMajors() {
        return majorService.getAllMajors();
    }

    public List<Major> getMajorsByFaculty(Long facultyId) {
        return majorService.getMajorsByFaculty(facultyId);
    }

    public Major createMajor(Major major) {
        return majorService.createMajor(major);
    }
}
