package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exam_schedule")
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection courseSection;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "room_code", nullable = false)
    private String roomCode;

    @Column(name = "seat_no", nullable = false)
    private Integer seatNo;

    public ExamSchedule() {}

    public ExamSchedule(Long id, LocalDate examDate, LocalTime startTime, String roomCode, Integer seatNo) {
        this.id = id;
        this.examDate = examDate;
        this.startTime = startTime;
        this.roomCode = roomCode;
        this.seatNo = seatNo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) { this.courseSection = courseSection; }
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
    public Integer getSeatNo() { return seatNo; }
    public void setSeatNo(Integer seatNo) { this.seatNo = seatNo; }
}

