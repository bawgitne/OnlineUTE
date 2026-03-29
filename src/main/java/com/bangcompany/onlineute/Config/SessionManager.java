package com.bangcompany.onlineute.Config;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Admin;

/**
 * SessionManager - Manages the current logged-in user session and their profile.
 */
public final class SessionManager {
    private static Account currentAccount;
    private static Student currentStudent;
    private static Lecturer currentLecturer;
    private static Admin currentAdmin;

    private SessionManager() {}

    public static void login(Account account) {
        currentAccount = account;
    }

    public static void logout() {
        currentAccount = null;
        currentStudent = null;
        currentLecturer = null;
        currentAdmin = null;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void setCurrentStudent(Student student) {
        currentStudent = student;
    }

    public static Lecturer getCurrentLecturer() {
        return currentLecturer;
    }

    public static void setCurrentLecturer(Lecturer lecturer) {
        currentLecturer = lecturer;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    // Helper methods for Dashboard
    public static String getProfileFullName() {
        if (currentStudent != null) return currentStudent.getFullName();
        if (currentLecturer != null) return currentLecturer.getFullName();
        if (currentAdmin != null) return currentAdmin.getFullName();
        if (currentAccount != null) return currentAccount.getUsername();
        return "Unknown User";
    }

    public static String getProfileCode() {
        if (currentStudent != null) return currentStudent.getCode();
        if (currentLecturer != null) return currentLecturer.getCode();
        if (currentAdmin != null) return currentAdmin.getCode();
        if (currentAccount != null) return "ACC-" + currentAccount.getId();
        return "N/A";
    }
}
