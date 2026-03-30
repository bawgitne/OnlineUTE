package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * AccountService - Handles Account administration (Admin only functionality).
 */
public interface AccountService {
    Account createStudentAccount(Account account, Student student) throws NoSuchAlgorithmException, InvalidKeySpecException;
    Account createLecturerAccount(Account account, Lecturer lecturer) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
