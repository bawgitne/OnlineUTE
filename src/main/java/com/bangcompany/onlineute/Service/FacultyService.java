package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Faculty;

import java.util.List;

public interface FacultyService {
    List<Faculty> getAllFaculties();
    PagedResult<Faculty> searchFaculties(String keyword, PageRequest pageRequest);
    PagedResult<Faculty> searchFaculties(String keyword, int page, int pageSize);
    long countAllFaculties();
    Faculty createFaculty(Faculty faculty);
    Faculty updateFaculty(Faculty faculty);
    void deleteFaculty(Long id);
}
