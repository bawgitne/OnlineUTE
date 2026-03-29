package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Lecturer;
import java.util.Optional;

public interface LecturerDAO {
    Lecturer save(Lecturer lecturer);
    Optional<Lecturer> findByAccountId(Long accountId);
}
