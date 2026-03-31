package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileDAO {
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findByAccountId(Long accountId);
}
