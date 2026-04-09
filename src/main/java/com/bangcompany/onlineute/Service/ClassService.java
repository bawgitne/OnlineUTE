package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Class;
import java.util.List;

public interface ClassService {
    List<Class> getAllClasses();
    List<Class> getClassesByMajor(Long majorId);
    PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest);
    PagedResult<Class> searchClasses(String keyword, int page, int pageSize);
    long countAllClasses();
    Class createClass(Class classEntity);
    Class updateClass(Class classEntity);
    void deleteClass(Long id);
}
