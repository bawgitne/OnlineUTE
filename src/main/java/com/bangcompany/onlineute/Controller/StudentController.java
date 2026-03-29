package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.StudentService;

public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public void createStudent(Student student, Account account) {
        studentService.createStudent(student, account);
    }

    // Other methods can be implemented as needed
}
