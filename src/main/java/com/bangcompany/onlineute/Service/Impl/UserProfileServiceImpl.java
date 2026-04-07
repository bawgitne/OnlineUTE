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

    // lưu thông tin hồ sơ người dùng
    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileDAO.save(userProfile);
    }

    // tìm hồ sơ theo ID tài khoản
    @Override
    public Optional<UserProfile> findByAccountId(Long accountId) {
        return userProfileDAO.findByAccountId(accountId);
    }

    // lấy thông tin hồ sơ của người dùng hiện đang đăng nhập
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

    // xây dựng hồ sơ dự phòng từ dữ liệu trong Session nêú DB chưa có Profile record
    private UserProfile buildFallbackProfile(Account account) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setDisplayName(SessionManager.getProfileFullName());
        profile.setProfileCode(SessionManager.getProfileCode());
        profile.setRoleTitle(resolveRoleTitle(account));

        // Nếu là sinh viên, bổ sung thêm các thông tin về lớp và khoa
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

    // xác định tên hiển thị của vai trò người dùng
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
