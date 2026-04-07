/**
 * Giao diện định nghĩa các thao tác dữ liệu cho Admin
 */
package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Admin;
import java.util.Optional;

public interface AdminDAO {
    Admin save(Admin admin);
    Optional<Admin> findByAccountId(Long accountId);
}
