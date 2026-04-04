package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Service.LecturerService;

public class LecturerServiceImpl implements LecturerService {
    private final LecturerDAO lecturerDAO;

    public LecturerServiceImpl(LecturerDAO lecturerDAO) {
        this.lecturerDAO = lecturerDAO;
    }

    @Override
    public PagedResult<Lecturer> searchLecturers(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return lecturerDAO.search(keyword.trim(), pageRequest);
    }

    @Override
    public PagedResult<Lecturer> searchLecturers(String keyword, int page, int pageSize) {
        return searchLecturers(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public long countAllLecturers() {
        return lecturerDAO.countAll();
    }
}
