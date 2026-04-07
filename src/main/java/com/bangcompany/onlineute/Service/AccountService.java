package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AccountService {
    // tạo tài khoản sinh viên
    Account createStudentAccount(Account account, Student student);

    // tạo tài khoản giảng viên
    Account createLecturerAccount(Account account, Lecturer lecturer);
    
    // tạo tài khoản quản trị viên
    Account createAdminAccount(Account account, Admin admin);

    // đổi mật khẩu
    boolean changePassword(Long accountId, String oldPassword, String newPassword);
}
