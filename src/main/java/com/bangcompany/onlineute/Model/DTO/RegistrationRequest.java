package com.bangcompany.onlineute.Model.DTO;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;

public class RegistrationRequest {
    private Account account;
    private Student student;
    private Lecturer lecturer;

    public RegistrationRequest() {}

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }
}
