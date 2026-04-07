/**
 * Giao diện định nghĩa các thao tác dữ liệu cho ngành học
 */
package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Major;

import java.util.List;

public interface MajorDAO {
    List<Major> findAll();
    List<Major> findByFacultyId(Long facultyId);
    Major save(Major major);
    void deleteById(Long id);
}
