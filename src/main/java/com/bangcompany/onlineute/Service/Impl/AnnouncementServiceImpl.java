package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.DAO.AnnouncementDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import com.bangcompany.onlineute.Service.AnnouncementService;
import java.util.List;
import java.util.ArrayList;

public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementDAO announcementDAO;

    public AnnouncementServiceImpl(AnnouncementDAO announcementDAO) {
        this.announcementDAO = announcementDAO;
    }

    @Override
    public void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName) {
        Announcement announcement = new Announcement(title, content, targetType, courseSectionId, senderName);
        announcementDAO.create(announcement);
    }

    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return announcementDAO.search(keyword.trim(), pageRequest);
    }

    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, int page, int pageSize) {
        return searchAnnouncements(keyword, PaginationSupport.normalize(page, pageSize));
    }

    @Override
    public List<Announcement> getAnnouncementsForCurrentUser() {
        String role = SessionManager.getRole();
        if (role == null) return new ArrayList<>();

        // If admin, they see all announcements that Admins send or maybe just all?
        // Let's return all, so Admin can review them.
        if (role.equals("ADMIN")) {
            return announcementDAO.findAll();
        } 
        else if (role.equals("LECTURER")) {
            // Very simplified: return ALL_LECTURERS or those created by lecturer (if we added sender match later)
            // For now just return ALL_LECTURERS and ALL
            List<Announcement> res = new ArrayList<>();
            res.addAll(announcementDAO.findByTargetType("ALL_LECTURERS"));
            res.addAll(announcementDAO.findByTargetType("ALL"));
            return res;
        } 
        else if (role.equals("STUDENT")) {
            var student = SessionManager.getCurrentStudent();
            if (student == null) return new ArrayList<>();
            return announcementDAO.findAnnouncementsForStudent(student.getId());
        }

        return new ArrayList<>();
    }
}
