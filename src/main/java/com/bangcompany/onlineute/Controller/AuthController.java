package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Service.AuthService;

import java.util.Optional;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Optional<Account> Login(String username, String password) {
        return authService.login(username, password);
    }

    public void Logout() {
        authService.logout();
    }
}
