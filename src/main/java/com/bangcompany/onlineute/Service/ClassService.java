package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Class;
import java.util.List;

public interface ClassService {
    List<Class> getAllClasses();
    List<Class> getClassesByFaculty(Long facultyId);
    PagedResult<Class> searchClasses(String keyword, PageRequest pageRequest);
    PagedResult<Class> searchClasses(String keyword, int page, int pageSize);
    Class createClass(Class classEntity);
}
