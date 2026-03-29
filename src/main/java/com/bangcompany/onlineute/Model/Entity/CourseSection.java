package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
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
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

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
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }
    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }
    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }
    public List<ExamSchedule> getExamSchedules() { return examSchedules; }
    public void setExamSchedules(List<ExamSchedule> examSchedules) { this.examSchedules = examSchedules; }
    public List<CourseRegistration> getCourseRegistrations() { return courseRegistrations; }
    public void setCourseRegistrations(List<CourseRegistration> courseRegistrations) { this.courseRegistrations = courseRegistrations; }
}
