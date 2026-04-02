package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    Student save(Student student);
    Optional<Student> findById(Long id);
    Optional<Student> findByAccountId(Long accountId);
    List<Student> findAll();
    PagedResult<Student> search(String keyword, PageRequest pageRequest);
    long countAll();
    long countByCodePrefix(String codePrefix);
}
