package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.Config.PasswordUtil;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;
    private final AdminDAO adminDAO;

    public AccountServiceImpl(AccountDAO accountDAO, StudentDAO studentDAO, LecturerDAO lecturerDAO, AdminDAO adminDAO) {
        this.accountDAO = accountDAO;
        this.studentDAO = studentDAO;
        this.lecturerDAO = lecturerDAO;
        this.adminDAO = adminDAO;
    }

    // tạo account sv, tự hash pass và gán username = mã sv
    @Override
    public Account createStudentAccount(Account account, Student student) {
        return JpaUtil.doInTransaction(() -> {
            byte[] salt = PasswordUtil.generateSalt();
            account.setSalt(salt);
            account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(), salt));
            account.setUsername(student.getCode());
            student.setAccount(account);
            return studentDAO.save(student).getAccount();
        });
    }

    // tạo account gv, tự hash pass và gán username = mã gv
    @Override
    public Account createLecturerAccount(Account account, Lecturer lecturer) {
        return JpaUtil.doInTransaction(() -> {
            byte[] salt = PasswordUtil.generateSalt();
            account.setSalt(salt);
            account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(), salt));
            account.setUsername(lecturer.getCode());
            lecturer.setAccount(account);
            return lecturerDAO.save(lecturer).getAccount();
        });
    }

    // tạo account admin, tự hash pass và gán username = mã admin
    @Override
    public Account createAdminAccount(Account account, Admin admin) {
        return JpaUtil.doInTransaction(() -> {
            byte[] salt = PasswordUtil.generateSalt();
            account.setSalt(salt);
            account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(), salt));
            account.setUsername(admin.getCode());
            admin.setAccount(account);
            return adminDAO.save(admin).getAccount();
        });
    }

    // đổi mật khẩu: check pass cũ rồi mới cho hash pass mới
    @Override
    public boolean changePassword(Long accountId, String oldPassword, String newPassword) {
        if (accountId == null || oldPassword == null || newPassword == null) {
            return false;
        }

        return JpaUtil.doInTransaction(() -> {
            Account account = accountDAO.findById(accountId).orElse(null);
            if (account == null) {
                return false;
            }

            if (!verifyOldPassword(account, oldPassword)) {
                return false;
            }

            byte[] salt = PasswordUtil.generateSalt();
            account.setSalt(salt);
            account.setPasswordHash(PasswordUtil.hashPassword(newPassword, salt));
            accountDAO.save(account);
            return true;
        });
    }

    // check pass cũ có khớp với hash trong db ko
    private boolean verifyOldPassword(Account account, String oldPassword) {
        byte[] salt = account.getSalt();
        String storedHash = account.getPasswordHash();
        if (storedHash == null) {
            return false;
        }

        if (salt == null || salt.length == 0) {
            return storedHash.equals(oldPassword);
        }

        return PasswordUtil.hashPassword(oldPassword, salt).equals(storedHash);
    }
}