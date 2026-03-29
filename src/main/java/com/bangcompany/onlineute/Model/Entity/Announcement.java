package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "target_type", nullable = false)
    private String targetType; // 'ALL_STUDENTS', 'ALL_LECTURERS', 'COURSE_SECTION', 'ALL'

    @Column(name = "course_section_id")
    private Long courseSectionId;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Announcement() {}

    public Announcement(String title, String content, String targetType, Long courseSectionId, String senderName) {
        this.title = title;
        this.content = content;
        this.targetType = targetType;
        this.courseSectionId = courseSectionId;
        this.senderName = senderName;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTargetType() { return targetType; }
    public Long getCourseSectionId() { return courseSectionId; }
    public String getSenderName() { return senderName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
