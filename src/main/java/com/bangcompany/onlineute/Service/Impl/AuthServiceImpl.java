package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Service.AuthService;
import com.bangcompany.onlineute.Config.SessionManager;
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
        
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getPasswordHash().equals(password)) {
                linkProfileToSession(account);
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    private void linkProfileToSession(Account account) {
        SessionManager.login(account);
        
        if (account.getRole() == com.bangcompany.onlineute.Model.EnumType.Role.STUDENT) {
            studentDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentStudent);
        } else if (account.getRole() == com.bangcompany.onlineute.Model.EnumType.Role.LECTURER) {
            lecturerDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentLecturer);
        } else if (account.getRole() == com.bangcompany.onlineute.Model.EnumType.Role.ADMIN) {
            adminDAO.findByAccountId(account.getId()).ifPresent(SessionManager::setCurrentAdmin);
        }
    }

    @Override
    public void logout() {
        SessionManager.logout();
    }

    @Override
    public Account registerStudent(Account account, Student student) {
        student.setAccount(account); // Linking
        return studentDAO.save(student).getAccount();
    }

    @Override
    public Account registerLecturer(Account account, Lecturer lecturer) {
        lecturer.setAccount(account); // Linking
        return lecturerDAO.save(lecturer).getAccount();
    }

    @Override
    public Account registerAdmin(Account account, Admin admin) {
        admin.setAccount(account); // Linking
        return adminDAO.save(admin).getAccount();
    }
}
