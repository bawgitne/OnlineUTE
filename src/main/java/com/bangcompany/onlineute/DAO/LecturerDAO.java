package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Lecturer;

import java.util.List;
import java.util.Optional;

public interface LecturerDAO {
    Lecturer save(Lecturer lecturer);
    List<Lecturer> findAll();
    Optional<Lecturer> findByAccountId(Long accountId);
}
