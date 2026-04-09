/**
 * Giao diện định nghĩa các thao tác dữ liệu cho ngành học
 */
package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public interface MajorDAO {
    List<Major> findAll();
    List<Major> findByFacultyId(Long facultyId);
    PagedResult<Major> search(String keyword, PageRequest pageRequest);
    Major save(Major major);
    void deleteById(Long id);
    long countAll();
}
