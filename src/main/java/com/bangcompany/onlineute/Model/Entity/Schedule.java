package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_slot", nullable = false)
    private Integer startSlot;

    @Column(name = "end_slot", nullable = false)
    private Integer endSlot;

    @Column(name = "room", nullable = false)
    private String room;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    public Schedule() {}

    public Schedule(Long id, Integer dayOfWeek, Integer startSlot, Integer endSlot, String room, Integer weekNumber) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
        this.room = room;
        this.weekNumber = weekNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) { this.courseSection = courseSection; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getStartSlot() { return startSlot; }
    public void setStartSlot(Integer startSlot) { this.startSlot = startSlot; }
    public Integer getEndSlot() { return endSlot; }
    public void setEndSlot(Integer endSlot) { this.endSlot = endSlot; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }
    public LocalDate getStudyDate() { return studyDate; }
    public void setStudyDate(LocalDate studyDate) { this.studyDate = studyDate; }
}
