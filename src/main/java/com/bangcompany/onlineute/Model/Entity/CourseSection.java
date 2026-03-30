package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "course_section", indexes = {
    @Index(name = "idx_section_code", columnList = "section_code")
})
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_code", nullable = false)
    private String sectionCode; // Changed to String to match VARCHAR in DB

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_batch_id")
    private RegistrationBatch registrationBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    @Column(name = "room", nullable = false)
    private String room;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "current_capacity", nullable = false)
    private Integer currentCapacity = 0;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_slot", nullable = false)
    private Integer startSlot;

    @Column(name = "end_slot", nullable = false)
    private Integer endSlot;

    @Column(name = "total_weeks", nullable = false)
    private Integer totalWeeks;

    @Column(name = "first_study_date")
    private LocalDate firstStudyDate;

    @Column(name = "last_study_date")
    private LocalDate lastStudyDate;

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
    private List<ExamSchedule> examSchedules;

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
    private List<CourseRegistration> courseRegistrations;

    public CourseSection() {}

    public CourseSection(Long id, String sectionCode) {
        this.id = id;
        this.sectionCode = sectionCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSectionCode() { return sectionCode; }
    public void setSectionCode(String sectionCode) { this.sectionCode = sectionCode; }
    public RegistrationBatch getRegistrationBatch() { return registrationBatch; }
    public void setRegistrationBatch(RegistrationBatch registrationBatch) { this.registrationBatch = registrationBatch; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }
    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    public Integer getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(Integer currentCapacity) { this.currentCapacity = currentCapacity; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getStartSlot() { return startSlot; }
    public void setStartSlot(Integer startSlot) { this.startSlot = startSlot; }
    public Integer getEndSlot() { return endSlot; }
    public void setEndSlot(Integer endSlot) { this.endSlot = endSlot; }
    public Integer getTotalWeeks() { return totalWeeks; }
    public void setTotalWeeks(Integer totalWeeks) { this.totalWeeks = totalWeeks; }
    public LocalDate getFirstStudyDate() { return firstStudyDate; }
    public void setFirstStudyDate(LocalDate firstStudyDate) { this.firstStudyDate = firstStudyDate; }
    public LocalDate getLastStudyDate() { return lastStudyDate; }
    public void setLastStudyDate(LocalDate lastStudyDate) { this.lastStudyDate = lastStudyDate; }
    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }
    public List<ExamSchedule> getExamSchedules() { return examSchedules; }
    public void setExamSchedules(List<ExamSchedule> examSchedules) { this.examSchedules = examSchedules; }
    public List<CourseRegistration> getCourseRegistrations() { return courseRegistrations; }
    public void setCourseRegistrations(List<CourseRegistration> courseRegistrations) { this.courseRegistrations = courseRegistrations; }
}
