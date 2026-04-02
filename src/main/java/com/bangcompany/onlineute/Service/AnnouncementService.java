package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import java.util.List;

public interface AnnouncementService {
    void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName);
    PagedResult<Announcement> searchAnnouncements(String keyword, PageRequest pageRequest);
    PagedResult<Announcement> searchAnnouncements(String keyword, int page, int pageSize);
    
    /**
     * Gets relevant announcements based on user role.
     */
    List<Announcement> getAnnouncementsForCurrentUser();
}
