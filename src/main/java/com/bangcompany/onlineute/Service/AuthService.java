package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import java.util.Optional;

public interface AuthService {
    // đăng nhập vào hệ thống
    Optional<Account> login(String loginCode, String password);
    // đăng xuất ra khỏi hệ thống
    void logout();
}
