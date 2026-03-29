package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;

public interface StudentService {
    void createStudent(Student student, Account account);
}
