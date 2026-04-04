package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> getAllMajors();
    List<Major> getMajorsByFaculty(Long facultyId);
    Major createMajor(Major major);
}
