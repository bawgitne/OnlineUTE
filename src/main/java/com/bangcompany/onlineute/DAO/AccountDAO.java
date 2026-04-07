/**
 * Giao diện định nghĩa các thao tác dữ liệu cho tài khoản
 */
package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Account;
import java.util.Optional;

public interface AccountDAO {
    Account save(Account account);
    Optional<Account> findByLoginCode(String loginCode);
    Optional<Account> findById(Long id);
}
