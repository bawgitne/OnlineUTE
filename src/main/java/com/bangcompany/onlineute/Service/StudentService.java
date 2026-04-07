package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.util.List;

public interface StudentService {
    // tạo sinh viên mới
    void createStudent(Student student, Account account);
    // cập nhật thông tin sinh viên
    Student updateStudent(Student student);
    // xóa sinh viên
    void deleteStudent(Long id);
    // lấy toàn bộ sinh viên
    List<Student> getAllStudents();
    
    // tìm kiếm sinh viên (phân trang)
    PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest);
    
    // tìm kiếm sinh viên (số trang và kích thước trang)
    PagedResult<Student> searchStudents(String keyword, int page, int pageSize);
    
    // đếm tổng số sinh viên
    long countAllStudents();
    
    // đếm sinh viên theo tiền tố mã
    long countStudentsByCodePrefix(String codePrefix);
}
