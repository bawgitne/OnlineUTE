package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;

/**
 * AccountService - Handles Account administration (Admin only functionality).
 */
public interface AccountService {
    Account createStudentAccount(Account account, Student student);
    Account createLecturerAccount(Account account, Lecturer lecturer);
}
