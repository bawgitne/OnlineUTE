package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;

import java.util.List;
import java.util.Optional;

public interface LecturerDAO {
    Lecturer save(Lecturer lecturer);
    List<Lecturer> findAll();
    Optional<Lecturer> findByAccountId(Long accountId);
    PagedResult<Lecturer> search(String keyword, PageRequest pageRequest);
    long countAll();
}
