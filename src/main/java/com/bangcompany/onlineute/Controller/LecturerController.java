package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Service.LecturerService;

public class LecturerController {
    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    public PagedResult<Lecturer> searchLecturers(String keyword, PageRequest pageRequest) {
        return lecturerService.searchLecturers(keyword, pageRequest);
    }

    public PagedResult<Lecturer> searchLecturers(String keyword, int page, int pageSize) {
        return lecturerService.searchLecturers(keyword, page, pageSize);
    }

    public long countAllLecturers() {
        return lecturerService.countAllLecturers();
    }
}
