package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Service.MajorService;

import java.util.List;

public class MajorController {
    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    public List<Major> getAllMajors() {
        return majorService.getAllMajors();
    }

    public List<Major> getMajorsByFaculty(Long facultyId) {
        return majorService.getMajorsByFaculty(facultyId);
    }

    public PagedResult<Major> searchMajors(String keyword, PageRequest pageRequest) {
        return majorService.searchMajors(keyword, pageRequest);
    }

    public PagedResult<Major> searchMajors(String keyword, int page, int pageSize) {
        return majorService.searchMajors(keyword, page, pageSize);
    }

    public long countAllMajors() {
        return majorService.countAllMajors();
    }

    public Major createMajor(Major major) {
        return majorService.createMajor(major);
    }

    public Major updateMajor(Major major) {
        return majorService.updateMajor(major);
    }

    public void deleteMajor(Long id) {
        majorService.deleteMajor(id);
    }
}
