/**
 * Giao diện định nghĩa các thao tác dữ liệu cho lớp học
 */
package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Class;
import java.util.List;

public interface ClassDAO {
    List<Class> findAll();
    List<Class> findByMajorId(Long majorId);
    PagedResult<Class> search(String keyword, PageRequest pageRequest);
    Class save(Class classEntity);
    void deleteById(Long id);
}
