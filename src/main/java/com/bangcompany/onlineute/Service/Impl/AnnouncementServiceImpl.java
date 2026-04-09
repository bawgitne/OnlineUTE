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

    // tạo tin mới
    @Override
    public void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName) {
        Announcement announcement = new Announcement(title, content, targetType, courseSectionId, senderName);
        announcementDAO.create(announcement);
    }

    // tìm tin có phân trang
    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return announcementDAO.search(keyword.trim(), pageRequest);
    }

    // tìm tin nâng cao
    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, int page, int pageSize) {
        return searchAnnouncements(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // lấy tin đúng role của user đang login
    @Override
    public List<Announcement> getAnnouncementsForCurrentUser() {
        String role = SessionManager.getRole();
        if (role == null) return new ArrayList<>();

        // admin xem đc hết
        if (role.equals("ADMIN")) {
            return announcementDAO.findAll();
        } 
        // gv thấy tin chung với tin cho gv
        else if (role.equals("LECTURER")) {
            var lecturer = SessionManager.getCurrentLecturer();
            if (lecturer == null) return new ArrayList<>();
            return announcementDAO.findAnnouncementsForLecturer(lecturer.getId());
        } 
        // sv thấy tin chung và tin của lớp mình học
        else if (role.equals("STUDENT")) {
            var student = SessionManager.getCurrentStudent();
            if (student == null) return new ArrayList<>();
            return announcementDAO.findAnnouncementsForStudent(student.getId());
        }

        return new ArrayList<>();
    }
}
