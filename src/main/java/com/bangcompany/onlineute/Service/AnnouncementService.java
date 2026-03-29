package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Announcement;
import java.util.List;

public interface AnnouncementService {
    void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName);
    
    /**
     * Gets relevant announcements based on user role.
     */
    List<Announcement> getAnnouncementsForCurrentUser();
}
