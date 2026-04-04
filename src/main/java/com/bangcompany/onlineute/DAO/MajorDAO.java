package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public interface MajorDAO {
    List<Major> findAll();
    List<Major> findByFacultyId(Long facultyId);
    Major save(Major major);
}
