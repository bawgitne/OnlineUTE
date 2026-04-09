package com.bangcompany.onlineute.Service.Impl;


import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Service.LecturerService;

public class LecturerServiceImpl implements LecturerService {
    private final LecturerDAO lecturerDAO;

    public LecturerServiceImpl(LecturerDAO lecturerDAO) {
        this.lecturerDAO = lecturerDAO;
    }

    // tìm kiếm giảng viên theo từ khóa (có phân trang)
    @Override
    public PagedResult<Lecturer> searchLecturers(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        String trimmedKeyword = keyword.trim();
        String effectiveKeyword = "all".equalsIgnoreCase(trimmedKeyword) ? "" : trimmedKeyword;
        return lecturerDAO.search(effectiveKeyword, pageRequest);
    }

    // tìm kiếm giảng viên (hỗ trợ trực tiếp thông số trang)
    @Override
    public PagedResult<Lecturer> searchLecturers(String keyword, int page, int pageSize) {
        return searchLecturers(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // cập nhật thông tin giảng viên
    @Override
    public Lecturer updateLecturer(Lecturer lecturer) {
        if (lecturer == null) {
            throw new IllegalArgumentException("Dữ liệu giảng viên không được để trống.");
        }
        return lecturerDAO.save(lecturer);
    }

    // xóa giảng viên ra khọi hệ thống theo ID
    @Override
    public void deleteLecturer(Long id) {
        lecturerDAO.deleteById(id);
    }

    // lấy toàn bộ danh sách giảng viên
    @Override
    public java.util.List<Lecturer> getAllLecturers() {
        return lecturerDAO.findAll();
    }

    // đếm tổng số lượng giảng viên trong hệ thống
    @Override
    public long countAllLecturers() {
        return lecturerDAO.countAll();
    }
}
