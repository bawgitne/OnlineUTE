package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.FacultyDAO;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Service.FacultyService;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {
    private final FacultyDAO facultyDAO;

    public FacultyServiceImpl(FacultyDAO facultyDAO) {
        this.facultyDAO = facultyDAO;
    }

    // lấy toàn bộ danh sách các khoa
    @Override
    public List<Faculty> getAllFaculties() {
        return facultyDAO.findAll();
    }

    // tạo mới một khoa vào hệ thống
    @Override
    public Faculty createFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Dữ liệu khoa không được để trống.");
        }
        return facultyDAO.save(faculty);
    }

    // cập nhật thông tin cho một khoa đã tồn tại
    @Override
    public Faculty updateFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Dữ liệu khoa không được để trống.");
        }
        return facultyDAO.save(faculty);
    }

    // xóa khoa ra khỏi hệ thống dựa trên ID
    @Override
    public void deleteFaculty(Long id) {
        facultyDAO.deleteById(id);
    }
}
