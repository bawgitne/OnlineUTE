package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "course_registration", indexes = {
    @Index(name = "idx_student_section", columnList = "student_id, section_id", unique = true)
})
public class CourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private CourseSection courseSection;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;

    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

    @OneToOne(mappedBy = "courseRegistration")
    private Mark mark;

    public CourseRegistration() {}

    public CourseRegistration(Long id, RegistrationStatus status, LocalDate regDate) {
        this.id = id;
        this.status = status;
        this.regDate = regDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public CourseSection getCourseSection() { return courseSection; }
    public void setCourseSection(CourseSection courseSection) { this.courseSection = courseSection; }
    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }
    public LocalDate getRegDate() { return regDate; }
    public void setRegDate(LocalDate regDate) { this.regDate = regDate; }
    public Mark getMark() { return mark; }
    public void setMark(Mark mark) { this.mark = mark; }
}

