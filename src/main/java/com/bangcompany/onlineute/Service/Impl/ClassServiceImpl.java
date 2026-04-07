package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Service.ClassService;
import java.util.List;

public class ClassServiceImpl implements ClassService {
    private final ClassDAO classDAO;

    public ClassServiceImpl(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    // lấy toàn bộ danh sách các lớp học
    @Override
    public List<Class> getAllClasses() {
        return classDAO.findAll();
    }

    // tìm danh sách các lớp thuộc về một ngành học cụ thể
    @Override
    public List<Class> getClassesByMajor(Long majorId) {
        if (majorId == null) {
            return List.of();
        }
        return classDAO.findByMajorId(majorId);
    }

    // tìm kiếm lớp học (hỗ trợ phân trang)
    @Override
    public PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return classDAO.search(keyword.trim(), pageRequest);
    }

    // tìm kiếm lớp học (hỗ trợ thông tin trực tiếp về số trang và kích thước trang)
    @Override
    public PagedResult<Class> searchClasses(String keyword, int page, int pageSize) {
        return searchClasses(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // tạo một lớp mới
    @Override
    public Class createClass(Class classEntity) {
        if (classEntity == null) {
            throw new IllegalArgumentException("Dữ liệu lớp học là bắt buộc.");
        }
        return classDAO.save(classEntity);
    }

    // cập nhật thông tin lớp học đã có
    @Override
    public Class updateClass(Class classEntity) {
        if (classEntity == null) {
            throw new IllegalArgumentException("Dữ liệu lớp học là bắt buộc.");
        }
        return classDAO.save(classEntity);
    }

    // xóa lớp học khỏi hệ thống
    @Override
    public void deleteClass(Long id) {
        classDAO.deleteById(id);
    }
}
