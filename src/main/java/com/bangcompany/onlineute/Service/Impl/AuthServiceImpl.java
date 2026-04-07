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

    // xử lý đăng nhập người dùng
    @Override
    public Optional<Account> login(String loginCode, String password) {
        // Tìm tài khoản theo mã đăng nhập (username, mã SV, mã GV,...)
        Optional<Account> accountOpt = accountDAO.findByLoginCode(loginCode);
        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }
        Account account = accountOpt.get();
        byte[] salt = account.getSalt();

        // Kiểm tra mật khẩu (hỗ trợ cả mật khẩu chưa băm cũ và mật khẩu đã băm mới có muối)
        if (salt == null || salt.length == 0) {
            if (!password.equals(account.getPasswordHash())) {
                return Optional.empty();
            }
        } else {
            String hashed = PasswordUtil.hashPassword(password, salt);
            if (!hashed.equals(account.getPasswordHash())) {
                return Optional.empty();
            }
        }

        if (account.getPasswordHash() == null) {
            return Optional.empty();
        }

        // Tải thông tin phiên làm việc nếu đăng nhập thành công
        loadSession(account);
        return Optional.of(account);
    }

    // đăng xuất ra khỏi hệ thống
    @Override
    public void logout() {
        SessionManager.logout();
    }

    // tải dữ liệu người dùng vào SessionManager dựa trên vai trò
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
