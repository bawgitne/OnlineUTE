package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AccountService {
    Account createStudentAccount(Account account, Student student);

    Account createLecturerAccount(Account account, Lecturer lecturer);
    Account createAdminAccount(Account account, Admin admin);
}
