package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Student;
import java.util.Optional;

public interface StudentDAO {
    Student save(Student student);
    Optional<Student> findByAccountId(Long accountId);
}
