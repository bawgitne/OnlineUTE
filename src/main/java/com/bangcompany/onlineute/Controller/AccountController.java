package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Service.AccountService;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account createStudentAccount(Account account, Student student) {
        return accountService.createStudentAccount(account, student);
    }

    public Account createLecturerAccount(Account account, Lecturer lecturer) {
        return accountService.createLecturerAccount(account, lecturer);
    }

    public Account createAdminAccount(Account account, Admin admin) {
        return accountService.createAdminAccount(account, admin);
    }
}
