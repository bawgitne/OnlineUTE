package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.StudentService;

public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO;

    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void createStudent(Student student, Account account) {
        // Uni-directional linking from Student back to Account
        // Since Student owns the relationship now, setting it on Student is mandatory
        student.setAccount(account);
        
        // Save the Student, and because of CascadeType.ALL, the Account will be saved too.
        studentDAO.save(student);
    }
}
