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

    // tạo sv mới và link với account
    @Override
    public void createStudent(Student student, Account account) {
        student.setAccount(account);
        studentDAO.save(student);
    }

    // lưu thông tin sv thay đổi
    @Override
    public Student updateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Dữ liệu sinh viên không được để trống.");
        }
        return studentDAO.save(student);
    }

    // xóa sv khỏi db theo id
    @Override
    public void deleteStudent(Long id) {
        studentDAO.deleteById(id);
    }

    // lấy hết list sv
    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    // tìm sv theo tên/mã có phân trang
    @Override
    public PagedResult<Student> searchStudents(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        String trimmedKeyword = keyword.trim();
        String effectiveKeyword = "all".equalsIgnoreCase(trimmedKeyword) ? "" : trimmedKeyword;
        return studentDAO.search(effectiveKeyword, pageRequest);
    }

    // tìm sv hỗ trợ truyền số trang trực tiếp
    @Override
    public PagedResult<Student> searchStudents(String keyword, int page, int pageSize) {
        return searchStudents(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // đếm tổng sv đang có
    @Override
    public long countAllStudents() {
        return studentDAO.countAll();
    }

    // đếm sv theo tiền tố mã để sinh mã tự động
    @Override
    public long countStudentsByCodePrefix(String codePrefix) {
        return studentDAO.countByCodePrefix(codePrefix);
    }
}
