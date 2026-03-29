package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Announcement;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Service.AnnouncementService;
import com.bangcompany.onlineute.Service.CourseSectionService;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationController {
    private final AnnouncementService announcementService;
    private final CourseSectionService courseSectionService;

    public NotificationController(AnnouncementService announcementService, CourseSectionService courseSectionService) {
        this.announcementService = announcementService;
        this.courseSectionService = courseSectionService;
    }

    public void createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName) {
        announcementService.createAnnouncement(title, content, targetType, courseSectionId, senderName);
    }

    public List<Announcement> getAnnouncementsForCurrentUser() {
        return announcementService.getAnnouncementsForCurrentUser();
    }

    public List<CourseSection> getCourseSectionsByLecturerId(Long lecturerId) {
        if (lecturerId == null) return List.of();
        return courseSectionService.getAllSections().stream()
                .filter(sec -> sec.getLecturer() != null && lecturerId.equals(sec.getLecturer().getId()))
                .collect(Collectors.toList());
    }
}
