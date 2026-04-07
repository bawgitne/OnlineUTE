package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    // lưu hồ sơ người dùng
    UserProfile save(UserProfile userProfile);
    // tìm hồ sơ qua ID tài khoản
    Optional<UserProfile> findByAccountId(Long accountId);
    // lấy hồ sơ hiện tại
    UserProfile getCurrentUserProfile();
}
