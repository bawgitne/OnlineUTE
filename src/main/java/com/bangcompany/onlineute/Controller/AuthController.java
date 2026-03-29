package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.AuthService;
import java.util.Optional;

/**
 * AuthController - Thin wrapper around AuthService.
 * Receives the managed AuthService instance via Constructor Injection.
 */
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

    public Account RegisterStudent(Account account, Student student) {
        try {
            return authService.registerStudent(account, student);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account RegisterLecturer(Account account, Lecturer lecturer) {
        try {
            return authService.registerLecturer(account, lecturer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account RegisterAdmin(Account account, Admin admin) {
        try {
            return authService.registerAdmin(account, admin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}