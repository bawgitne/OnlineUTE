package com.bangcompany.onlineute.Config;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.EnumType.Role;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * SessionManager - Manages in-memory session and persists it to config/session.properties.
 */
public final class SessionManager {
    private static final Path SESSION_FILE = Paths.get("config", "session.properties");

    private static Account currentAccount;
    private static Student currentStudent;
    private static Lecturer currentLecturer;
    private static Admin currentAdmin;

    static {
        loadFromFile();
    }

    private SessionManager() {}

    public static void login(Account account) {
        currentAccount = account;
        persistToFile();
    }

    public static void logout() {
        currentAccount = null;
        currentStudent = null;
        currentLecturer = null;
        currentAdmin = null;
        persistToFile();
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void setCurrentStudent(Student student) {
        currentStudent = student;
        persistToFile();
    }

    public static Lecturer getCurrentLecturer() {
        return currentLecturer;
    }

    public static void setCurrentLecturer(Lecturer lecturer) {
        currentLecturer = lecturer;
        persistToFile();
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
        persistToFile();
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

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
        if (currentAccount != null && currentAccount.getId() != null) return "ACC-" + currentAccount.getId();
        return "N/A";
    }

    public static String getRole() {
        if (currentAccount != null && currentAccount.getRole() != null) {
            return currentAccount.getRole().name();
        }
        return null;
    }

    public static String getRoleDisplayName() {
        if (currentAccount != null && currentAccount.getRole() != null) {
            return currentAccount.getRole().toString();
        }
        return "Nguoi dung";
    }

    private static void loadFromFile() {
        if (!Files.exists(SESSION_FILE)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(SESSION_FILE)) {
            properties.load(inputStream);
        } catch (IOException ignored) {
            return;
        }

        if (!Boolean.parseBoolean(properties.getProperty("session.loggedIn", "false"))) {
            return;
        }

        String username = properties.getProperty("account.username");
        String roleText = properties.getProperty("account.role");
        Long accountId = parseLong(properties.getProperty("account.id"));
        Role role = parseRole(roleText);

        if (username == null || role == null) {
            return;
        }

        Account account = new Account();
        account.setId(accountId);
        account.setUsername(username);
        account.setRole(role);
        currentAccount = account;

        String profileType = properties.getProperty("profile.type");
        if ("STUDENT".equals(profileType)) {
            Student student = new Student();
            student.setId(parseLong(properties.getProperty("profile.id")));
            student.setCode(properties.getProperty("profile.code"));
            student.setFullName(properties.getProperty("profile.fullName"));
            currentStudent = student;
        } else if ("LECTURER".equals(profileType)) {
            Lecturer lecturer = new Lecturer();
            lecturer.setId(parseLong(properties.getProperty("profile.id")));
            lecturer.setCode(properties.getProperty("profile.code"));
            lecturer.setFullName(properties.getProperty("profile.fullName"));
            currentLecturer = lecturer;
        } else if ("ADMIN".equals(profileType)) {
            Admin admin = new Admin();
            admin.setId(parseLong(properties.getProperty("profile.id")));
            admin.setCode(properties.getProperty("profile.code"));
            admin.setFullName(properties.getProperty("profile.fullName"));
            currentAdmin = admin;
        }
    }

    private static void persistToFile() {
        Properties properties = new Properties();
        properties.setProperty("session.loggedIn", Boolean.toString(currentAccount != null));

        if (currentAccount != null) {
            setIfNotNull(properties, "account.id", currentAccount.getId());
            setIfNotNull(properties, "account.username", currentAccount.getUsername());
            if (currentAccount.getRole() != null) {
                properties.setProperty("account.role", currentAccount.getRole().name());
            }
        }

        if (currentStudent != null) {
            properties.setProperty("profile.type", "STUDENT");
            setIfNotNull(properties, "profile.id", currentStudent.getId());
            setIfNotNull(properties, "profile.code", currentStudent.getCode());
            setIfNotNull(properties, "profile.fullName", currentStudent.getFullName());
        } else if (currentLecturer != null) {
            properties.setProperty("profile.type", "LECTURER");
            setIfNotNull(properties, "profile.id", currentLecturer.getId());
            setIfNotNull(properties, "profile.code", currentLecturer.getCode());
            setIfNotNull(properties, "profile.fullName", currentLecturer.getFullName());
        } else if (currentAdmin != null) {
            properties.setProperty("profile.type", "ADMIN");
            setIfNotNull(properties, "profile.id", currentAdmin.getId());
            setIfNotNull(properties, "profile.code", currentAdmin.getCode());
            setIfNotNull(properties, "profile.fullName", currentAdmin.getFullName());
        }

        try {
            Path parent = SESSION_FILE.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (OutputStream outputStream = Files.newOutputStream(SESSION_FILE)) {
                properties.store(outputStream, "OnlineUTE Session Config");
            }
        } catch (IOException ignored) {
            // Session persistence is best-effort; app should still run if file I/O fails.
        }
    }

    private static void setIfNotNull(Properties properties, String key, Object value) {
        if (value != null) {
            properties.setProperty(key, String.valueOf(value));
        }
    }

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Role parseRole(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Role.valueOf(value.trim());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
