package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO;

    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void createStudent(Student student, Account account) {
        student.setAccount(account);
        studentDAO.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return studentDAO.search(keyword.trim(), pageRequest);
    }

    @Override
    public PagedResult<Student> searchStudents(String keyword, int page, int pageSize) {
        return searchStudents(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public long countAllStudents() {
        return studentDAO.countAll();
    }

    @Override
    public long countStudentsByCodePrefix(String codePrefix) {
        return studentDAO.countByCodePrefix(codePrefix);
    }
}
