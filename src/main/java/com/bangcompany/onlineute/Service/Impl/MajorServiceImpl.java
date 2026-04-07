package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.MajorDAO;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Service.MajorService;

import java.util.List;

public class MajorServiceImpl implements MajorService {
    private final MajorDAO majorDAO;

    public MajorServiceImpl(MajorDAO majorDAO) {
        this.majorDAO = majorDAO;
    }

    // lấy toàn bộ danh sách các ngành học
    @Override
    public List<Major> getAllMajors() {
        return majorDAO.findAll();
    }

    // lấy danh sách các ngành học thuộc về một khoa cụ thể
    @Override
    public List<Major> getMajorsByFaculty(Long facultyId) {
        if (facultyId == null) {
            return List.of();
        }
        return majorDAO.findByFacultyId(facultyId);
    }

    // tạo một ngành học mới
    @Override
    public Major createMajor(Major major) {
        if (major == null) {
            throw new IllegalArgumentException("Dữ liệu ngành học không được để trống.");
        }
        return majorDAO.save(major);
    }

    // cập nhật thông tin ngành học đã có
    @Override
    public Major updateMajor(Major major) {
        if (major == null) {
            throw new IllegalArgumentException("Dữ liệu ngành học không được để trống.");
        }
        return majorDAO.save(major);
    }

    // xóa ngành học ra khỏi hệ thống dựa theo ID
    @Override
    public void deleteMajor(Long id) {
        majorDAO.deleteById(id);
    }
}
