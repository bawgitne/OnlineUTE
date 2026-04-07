package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import java.util.List;

public interface AnnouncementService {
    // tạo thông báo
    void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName);
    // tìm kiếm thông báo (phân trang)
    PagedResult<Announcement> searchAnnouncements(String keyword, PageRequest pageRequest);
    // tìm kiếm thông báo (số trang và kích thước trang)
    PagedResult<Announcement> searchAnnouncements(String keyword, int page, int pageSize);
    // lấy thông báo cho người dùng hiện tại
    List<Announcement> getAnnouncementsForCurrentUser();
}
