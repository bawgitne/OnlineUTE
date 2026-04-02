package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.util.List;

public interface StudentService {
    void createStudent(Student student, Account account);
    List<Student> getAllStudents();
    PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest);
    PagedResult<Student> searchStudents(String keyword, int page, int pageSize);
    long countAllStudents();
    long countStudentsByCodePrefix(String codePrefix);
}
