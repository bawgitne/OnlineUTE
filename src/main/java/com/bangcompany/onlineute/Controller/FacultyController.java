package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
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

    public PagedResult<Faculty> searchFaculties(String keyword, PageRequest pageRequest) {
        return facultyService.searchFaculties(keyword, pageRequest);
    }

    public PagedResult<Faculty> searchFaculties(String keyword, int page, int pageSize) {
        return facultyService.searchFaculties(keyword, page, pageSize);
    }

    public long countAllFaculties() {
        return facultyService.countAllFaculties();
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyService.deleteFaculty(id);
    }
}
