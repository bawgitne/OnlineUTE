package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.DAO.UserProfileDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.Service.UserProfileService;

import java.util.Optional;

public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileDAO userProfileDAO;

    public UserProfileServiceImpl(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    @Override
    public Optional<UserProfile> findByAccountId(Long accountId) {
        return userProfileDAO.findByAccountId(accountId);
    }

    @Override
    public UserProfile getCurrentUserProfile() {
        Account currentAccount = SessionManager.getCurrentAccount();
        if (currentAccount == null || currentAccount.getId() == null) {
            return buildFallbackProfile(null);
        }

        return userProfileDAO.findByAccountId(currentAccount.getId())
                .orElseGet(() -> buildFallbackProfile(currentAccount));
    }

    private UserProfile buildFallbackProfile(Account account) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setDisplayName(SessionManager.getProfileFullName());
        profile.setProfileCode(SessionManager.getProfileCode());
        profile.setRoleTitle(resolveRoleTitle(account));

        Student student = SessionManager.getCurrentStudent();
        if (student != null) {
            profile.setEmail(student.getEmail());
            profile.setBirthDate(student.getBirthOfDate());
            profile.setAvatarUrl(student.getAvatarUrl());
            return profile;
        }

        Lecturer lecturer = SessionManager.getCurrentLecturer();
        if (lecturer != null) {
            profile.setDisplayName(lecturer.getFullName());
            profile.setProfileCode(lecturer.getCode());
            return profile;
        }

        Admin admin = SessionManager.getCurrentAdmin();
        if (admin != null) {
            profile.setDisplayName(admin.getFullName());
            profile.setProfileCode(admin.getCode());
        }

        return profile;
    }

    private String resolveRoleTitle(Account account) {
        if (account == null || account.getRole() == null) {
            return "Người dùng";
        }

        Role role = account.getRole();
        return switch (role) {
            case STUDENT -> "Sinh viên";
            case LECTURER -> "Giảng viên";
            case ADMIN -> "Quản trị viên";
        };
    }
}
