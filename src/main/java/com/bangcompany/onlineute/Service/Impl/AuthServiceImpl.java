package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.PasswordUtil;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.Service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final AccountDAO accountDAO;
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;
    private final AdminDAO adminDAO;

    public AuthServiceImpl(AccountDAO accountDAO, StudentDAO studentDAO, LecturerDAO lecturerDAO, AdminDAO adminDAO) {
        this.accountDAO = accountDAO;
        this.studentDAO = studentDAO;
        this.lecturerDAO = lecturerDAO;
        this.adminDAO = adminDAO;
    }

    @Override
    public Optional<Account> login(String username, String password) {
        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }
        password = PasswordUtil.hashPassword(password, accountOpt.get().getSalt());
        Account account = accountOpt.get();
        if (!password.equals(account.getPasswordHash())) {
            return Optional.empty();
        }

        loadSession(account);
        return Optional.of(account);
    }

    @Override
    public void logout() {
        SessionManager.logout();
    }

    private void loadSession(Account account) {
        SessionManager.login(account);

        Role role = account.getRole();
        if (role == Role.STUDENT) {
            studentDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentStudent);
            return;
        }

        if (role == Role.LECTURER) {
            lecturerDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentLecturer);
            return;
        }

        if (role == Role.ADMIN) {
            adminDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentAdmin);
        }
    }
}
