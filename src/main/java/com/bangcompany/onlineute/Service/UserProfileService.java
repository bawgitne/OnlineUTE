package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findByAccountId(Long accountId);
    UserProfile getCurrentUserProfile();
}
