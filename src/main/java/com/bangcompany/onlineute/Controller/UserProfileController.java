package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.Service.UserProfileService;

public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public UserProfile save(UserProfile userProfile) {
        return userProfileService.save(userProfile);
    }

    public UserProfile getCurrentUserProfile() {
        return userProfileService.getCurrentUserProfile();
    }
}
