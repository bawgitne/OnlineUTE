package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;

public interface LecturerService {
    PagedResult<Lecturer> searchLecturers(String keyword, PageRequest pageRequest);
    PagedResult<Lecturer> searchLecturers(String keyword, int page, int pageSize);
    long countAllLecturers();
}
