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

    // lưu profile vào db
    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileDAO.save(userProfile);
    }

    // tìm profile theo id của account link với nó
    @Override
    public Optional<UserProfile> findByAccountId(Long accountId) {
        return userProfileDAO.findByAccountId(accountId);
    }

    // lấy profile của người đang login, ưu tiên lấy từ db ko có thì chế tạm từ session
    @Override
    public UserProfile getCurrentUserProfile() {
        Account currentAccount = SessionManager.getCurrentAccount();
        if (currentAccount == null || currentAccount.getId() == null) {
            return buildFallbackProfile(null);
        }

        // Ưu tiên lấy từ DB, nếu không có thì sinh hồ sơ dự phòng từ Session
        return userProfileDAO.findByAccountId(currentAccount.getId())
                .orElseGet(() -> buildFallbackProfile(currentAccount));
    }

    // tự chế profile từ data trong session (dùng khi db chưa có record cho account này)
    private UserProfile buildFallbackProfile(Account account) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setDisplayName(SessionManager.getProfileFullName());
        profile.setProfileCode(SessionManager.getProfileCode());
        profile.setRoleTitle(resolveRoleTitle(account));

        // lấy thêm lớp, khoa... nếu là sv
        Student student = SessionManager.getCurrentStudent();
        if (student != null) {
            profile.setEmail(student.getEmail());
            profile.setBirthDate(student.getBirthOfDate());
            profile.setAvatarUrl(student.getAvatarUrl());
            if (student.getClassEntity() != null) {
                profile.setClassName(student.getClassEntity().getClassName());
                if (student.getClassEntity().getMajor() != null && student.getClassEntity().getMajor().getFaculty() != null) {
                    profile.setFacultyName(student.getClassEntity().getMajor().getFaculty().getFullName());
                }
            }
            return profile;
        }

        // Nếu là giảng viên
        Lecturer lecturer = SessionManager.getCurrentLecturer();
        if (lecturer != null) {
            profile.setDisplayName(lecturer.getFullName());
            profile.setProfileCode(lecturer.getCode());
            return profile;
        }

        // Nếu là quản trị viên
        Admin admin = SessionManager.getCurrentAdmin();
        if (admin != null) {
            profile.setDisplayName(admin.getFullName());
            profile.setProfileCode(admin.getCode());
        }

        return profile;
    }

    // đọc xem role là gì để hiện chữ sv/gv/admin cho đẹp
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
