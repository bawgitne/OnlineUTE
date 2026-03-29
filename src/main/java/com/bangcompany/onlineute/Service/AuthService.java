package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Admin;
import java.util.Optional;

public interface AuthService {
    Optional<Account> login(String username, String password);
    void logout();
    
    // Registration methods
    Account registerStudent(Account account, Student student);
    Account registerLecturer(Account account, Lecturer lecturer);
    Account registerAdmin(Account account, Admin admin);
}
