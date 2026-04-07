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

    // tạo mới thông báo
    @Override
    public void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName) {
        Announcement announcement = new Announcement(title, content, targetType, courseSectionId, senderName);
        announcementDAO.create(announcement);
    }

    // tìm kiếm thông báo có phân trang
    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, PageRequest pageRequest) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PaginationSupport.empty(pageRequest);
        }
        return announcementDAO.search(keyword.trim(), pageRequest);
    }

    // tìm kiếm thông báo (hỗ trợ số trang và kích thước trang)
    @Override
    public PagedResult<Announcement> searchAnnouncements(String keyword, int page, int pageSize) {
        return searchAnnouncements(keyword, PaginationSupport.normalize(page, pageSize));
    }

    // lấy danh sách thông báo phù hợp với người dùng hiện tại
    @Override
    public List<Announcement> getAnnouncementsForCurrentUser() {
        String role = SessionManager.getRole();
        if (role == null) return new ArrayList<>();

        // Admin có thể thấy toàn bộ thông báo trong hệ thống
        if (role.equals("ADMIN")) {
            return announcementDAO.findAll();
        } 
        // Giảng viên thấy thông báo chung và thông báo dành riêng cho giảng viên
        else if (role.equals("LECTURER")) {
            List<Announcement> res = new ArrayList<>();
            res.addAll(announcementDAO.findByTargetType("ALL_LECTURERS"));
            res.addAll(announcementDAO.findByTargetType("ALL"));
            return res;
        } 
        // Sinh viên thấy thông báo chung và thông báo từ các lớp học phần đang tham gia
        else if (role.equals("STUDENT")) {
            var student = SessionManager.getCurrentStudent();
            if (student == null) return new ArrayList<>();
            return announcementDAO.findAnnouncementsForStudent(student.getId());
        }

        return new ArrayList<>();
    }
}
