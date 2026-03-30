package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.PasswordUtil;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.AccountService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;

    public AccountServiceImpl(AccountDAO accountDAO, StudentDAO studentDAO, LecturerDAO lecturerDAO) {
        this.accountDAO = accountDAO;
        this.studentDAO = studentDAO;
        this.lecturerDAO = lecturerDAO;
    }

    @Override
    public Account createStudentAccount(Account account, Student student) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(),salt));
        student.setAccount(account);
        studentDAO.save(student);
        return account;
    }

    @Override
    public Account createLecturerAccount(Account account, Lecturer lecturer) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = PasswordUtil.generateSalt();
        account.setSalt(salt);
        account.setPasswordHash(PasswordUtil.hashPassword(account.getPasswordHash(),salt));
        lecturer.setAccount(account);
        lecturerDAO.save(lecturer);
        return account;
    }
}
