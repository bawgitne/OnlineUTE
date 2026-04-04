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

    @Override
    public List<Class> getAllClasses() {
        return classDAO.findAll();
    }

    @Override
    public List<Class> getClassesByFaculty(Long facultyId) {
        if (facultyId == null) {
            return List.of();
        }
        return classDAO.findByFacultyId(facultyId);
    }

    @Override
    public PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return classDAO.search(keyword.trim(), pageRequest);
    }

    @Override
    public PagedResult<Class> searchClasses(String keyword, int page, int pageSize) {
        return searchClasses(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public Class createClass(Class classEntity) {
        if (classEntity == null) {
            throw new IllegalArgumentException("Class is required.");
        }
        return classDAO.save(classEntity);
    }
}
