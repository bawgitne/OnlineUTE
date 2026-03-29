package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Admin;
import java.util.Optional;

public interface AdminDAO {
    Admin save(Admin admin);
    Optional<Admin> findByAccountId(Long accountId);
}
