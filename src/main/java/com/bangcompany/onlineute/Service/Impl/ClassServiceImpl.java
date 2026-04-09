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

    // lấy hết list lớp
    @Override
    public List<Class> getAllClasses() {
        return classDAO.findAll();
    }

    // lấy các lớp của 1 ngành học
    @Override
    public List<Class> getClassesByMajor(Long majorId) {
        if (majorId == null) {
            return List.of();
        }
        return classDAO.findByMajorId(majorId);
    }

    // tìm lớp có phân trang
    @Override
    public PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        String trimmedKeyword = keyword.trim();
        String effectiveKeyword = "all".equalsIgnoreCase(trimmedKeyword) ? "" : trimmedKeyword;
        return classDAO.search(effectiveKeyword, pageRequest);
    }

    // tìm lớp hỗ trợ truyền số trang trực tiếp
    @Override
    public PagedResult<Class> searchClasses(String keyword, int page, int pageSize) {
        return searchClasses(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public long countAllClasses() {
        return classDAO.countAll();
    }

    // tạo lớp mới
    @Override
    public Class createClass(Class classEntity) {
        if (classEntity == null) {
            throw new IllegalArgumentException("Dữ liệu lớp học là bắt buộc.");
        }
        return classDAO.save(classEntity);
    }

    // lưu thay đổi cho lớp
    @Override
    public Class updateClass(Class classEntity) {
        if (classEntity == null) {
            throw new IllegalArgumentException("Dữ liệu lớp học là bắt buộc.");
        }
        return classDAO.save(classEntity);
    }

    // xóa lớp khỏi db theo id
    @Override
    public void deleteClass(Long id) {
        classDAO.deleteById(id);
    }
}
