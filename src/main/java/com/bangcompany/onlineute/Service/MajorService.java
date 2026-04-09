package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> getAllMajors();
    List<Major> getMajorsByFaculty(Long facultyId);
    PagedResult<Major> searchMajors(String keyword, PageRequest pageRequest);
    PagedResult<Major> searchMajors(String keyword, int page, int pageSize);
    long countAllMajors();
    Major createMajor(Major major);
    Major updateMajor(Major major);
    void deleteMajor(Long id);
}
