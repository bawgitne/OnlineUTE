package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Service.ClassService;

import java.util.List;

public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    public List<Class> getAllClasses() {
        return classService.getAllClasses();
    }

    public List<Class> getClassesByMajor(Long majorId) {
        return classService.getClassesByMajor(majorId);
    }

    public PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest) {
        return classService.searchClasses(keyword, pageRequest);
    }

    public PagedResult<Class> searchClasses(String keyword, int page, int pageSize) {
        return classService.searchClasses(keyword, page, pageSize);
    }

    public long countAllClasses() {
        return classService.countAllClasses();
    }

    public Class createClass(Class classEntity) {
        return classService.createClass(classEntity);
    }

    public Class updateClass(Class classEntity) {
        return classService.updateClass(classEntity);
    }

    public void deleteClass(Long id) {
        classService.deleteClass(id);
    }
}
