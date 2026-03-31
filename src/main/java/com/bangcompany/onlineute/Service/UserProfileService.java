package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> findByAccountId(Long accountId);
    UserProfile getCurrentUserProfile();
}
