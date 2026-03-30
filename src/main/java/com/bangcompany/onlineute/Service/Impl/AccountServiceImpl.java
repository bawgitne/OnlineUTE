package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;
    private final AdminDAO adminDAO;

    public AccountServiceImpl(StudentDAO studentDAO, LecturerDAO lecturerDAO, AdminDAO adminDAO) {
        this.studentDAO = studentDAO;
        this.lecturerDAO = lecturerDAO;
        this.adminDAO = adminDAO;
    }

    @Override
    public Account createStudentAccount(Account account, Student student) {
        student.setAccount(account);
        return studentDAO.save(student).getAccount();
    }

    @Override
    public Account createLecturerAccount(Account account, Lecturer lecturer) {
        lecturer.setAccount(account);
        return lecturerDAO.save(lecturer).getAccount();
    }

    @Override
    public Account createAdminAccount(Account account, Admin admin) {
        admin.setAccount(account);
        return adminDAO.save(admin).getAccount();
    }
}
