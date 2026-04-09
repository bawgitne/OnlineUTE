package com.bangcompany.onlineute.Service.Impl;


import com.bangcompany.onlineute.DAO.MajorDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Service.MajorService;

import java.util.List;

public class MajorServiceImpl implements MajorService {
    private final MajorDAO majorDAO;

    public MajorServiceImpl(MajorDAO majorDAO) {
        this.majorDAO = majorDAO;
    }

    // lấy toàn bộ danh sách các ngành học
    @Override
    public List<Major> getAllMajors() {
        return majorDAO.findAll();
    }

    // lấy danh sách các ngành học thuộc vọ một khoa cụ thể
    @Override
    public List<Major> getMajorsByFaculty(Long facultyId) {
        if (facultyId == null) {
            return List.of();
        }
        return majorDAO.findByFacultyId(facultyId);
    }

    @Override
    public PagedResult<Major> searchMajors(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        String trimmedKeyword = keyword.trim();
        String effectiveKeyword = "all".equalsIgnoreCase(trimmedKeyword) ? "" : trimmedKeyword;
        return majorDAO.search(effectiveKeyword, pageRequest);
    }

    @Override
    public PagedResult<Major> searchMajors(String keyword, int page, int pageSize) {
        return searchMajors(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public long countAllMajors() {
        return majorDAO.countAll();
    }

    // tạo một ngành học mớii
    @Override
    public Major createMajor(Major major) {
        if (major == null) {
            throw new IllegalArgumentException("Dữ liệu ngành học không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        return majorDAO.save(major);
    }

    // cập nhật thông tin ngành học đã có
    @Override
    public Major updateMajor(Major major) {
        if (major == null) {
            throw new IllegalArgumentException("Dữ liệu ngành học không đÃ†Â°ợc đáÂ»Æ’ trệ˜ng.");
        }
        return majorDAO.save(major);
    }

    // xóa ngành học ra khọi hệ thống dựa theo ID
    @Override
    public void deleteMajor(Long id) {
        majorDAO.deleteById(id);
    }
}
