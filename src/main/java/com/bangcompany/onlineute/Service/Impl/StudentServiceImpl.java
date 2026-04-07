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

    // tạo mới một sinh viên và gán tài khoản cho sinh viên đó
    @Override
    public void createStudent(Student student, Account account) {
        student.setAccount(account);
        studentDAO.save(student);
    }

    // cập nhật thông tin cho sinh viên
    @Override
    public Student updateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Dữ liệu sinh viên không được để trống.");
        }
        return studentDAO.save(student);
    }

    // xóa sinh viên ra khỏi hệ thống dựa theo ID
    @Override
    public void deleteStudent(Long id) {
        studentDAO.deleteById(id);
    }

    // lấy toàn bộ danh sách sinh viên
    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    // tìm kiếm sinh viên theo từ khóa (có phân trang)
    @Override
    public PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return studentDAO.search(keyword.trim(), pageRequest);
    }

    // tìm kiếm sinh viên (hỗ trợ trực tiếp thông số trang)
    @Override
    public PagedResult<Student> searchStudents(String keyword, int page, int pageSize) {
        return searchStudents(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // đếm tổng số lượng sinh viên trong hệ thống
    @Override
    public long countAllStudents() {
        return studentDAO.countAll();
    }

    // đếm số lượng sinh viên theo tiền tố mã (dùng cho việc sinh mã tự động)
    @Override
    public long countStudentsByCodePrefix(String codePrefix) {
        return studentDAO.countByCodePrefix(codePrefix);
    }
}
