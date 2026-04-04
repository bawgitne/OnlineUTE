package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.MajorDAO;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Service.MajorService;

import java.util.List;

public class MajorServiceImpl implements MajorService {
    private final MajorDAO majorDAO;

    public MajorServiceImpl(MajorDAO majorDAO) {
        this.majorDAO = majorDAO;
    }

    @Override
    public List<Major> getAllMajors() {
        return majorDAO.findAll();
    }

    @Override
    public List<Major> getMajorsByFaculty(Long facultyId) {
        if (facultyId == null) {
            return List.of();
        }
        return majorDAO.findByFacultyId(facultyId);
    }

    @Override
    public Major createMajor(Major major) {
        if (major == null) {
            throw new IllegalArgumentException("Major is required.");
        }
        return majorDAO.save(major);
    }
}
