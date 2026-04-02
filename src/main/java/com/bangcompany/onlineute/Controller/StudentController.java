package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.StudentService;

import java.util.List;

public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public void createStudent(Student student, Account account) {
        studentService.createStudent(student, account);
    }

    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    public PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest) {
        return studentService.searchStudents(keyword, pageRequest);
    }

    public PagedResult<Student> searchStudents(String keyword, int page, int pageSize) {
        return studentService.searchStudents(keyword, page, pageSize);
    }

    public long countAllStudents() {
        return studentService.countAllStudents();
    }
}
