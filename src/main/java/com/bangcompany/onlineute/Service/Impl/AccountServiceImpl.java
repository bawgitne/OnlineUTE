package com.bangcompany.onlineute.Service.Impl;

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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

    // tạo tài khoản cho sinh viên
    @Override
    public Account createStudentAccount(Account account, Student student) {
        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        // Băm mật khẩu với muối để bảo mật
        account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(),salt));
        account.setUsername(student.getCode());
        student.setAccount(account);
        return studentDAO.save(student).getAccount();
    }

    // tạo tài khoản cho giảng viên
    @Override
    public Account createLecturerAccount(Account account, Lecturer lecturer) {
        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(), salt));
        account.setUsername(lecturer.getCode());
        lecturer.setAccount(account);
        return lecturerDAO.save(lecturer).getAccount();
    }

    // tạo tài khoản cho quản trị viên
    @Override
    public Account createAdminAccount(Account account, Admin admin) {
        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(), salt));
        account.setUsername(admin.getCode());
        admin.setAccount(account);
        return adminDAO.save(admin).getAccount();
    }

    // đổi mật khẩu người dùng
    @Override
    public boolean changePassword(Long accountId, String oldPassword, String newPassword) {
        if (accountId == null || oldPassword == null || newPassword == null) {
            return false;
        }

        Account account = accountDAO.findById(accountId).orElse(null);
        if (account == null) {
            return false;
        }

        // Xác thực mật khẩu cũ trước khi cho phép đổi
        if (!verifyOldPassword(account, oldPassword)) {
            return false;
        }

        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        account.setPasswordHash(PasswordUtil.hashPassword(newPassword, salt));
        accountDAO.save(account);
        return true;
    }

    // kiểm tra mật khẩu cũ có khớp không
    private boolean verifyOldPassword(Account account, String oldPassword) {
        byte[] salt = account.getSalt();
        String storedHash = account.getPasswordHash();
        if (storedHash == null) {
            return false;
        }

        // Nếu tài khoản cũ chưa có muối (dữ liệu cũ), so sánh trực tiếp
        if (salt == null || salt.length == 0) {
            return storedHash.equals(oldPassword);
        }

        // So sánh mã băm của mật khẩu nhập vào với mật khẩu đã lưu
        return PasswordUtil.hashPassword(oldPassword, salt).equals(storedHash);
    }
}
