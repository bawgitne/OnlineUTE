package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import java.util.Optional;

public interface AuthService {
    Optional<Account> login(String username, String password);
    void logout();
}
