package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", unique = true, nullable = false)
    private String courseCode;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "credit", nullable = false)
    private Integer credit;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseSection> courseSections;

    public Course() {}

    public Course(Long id, String courseCode, String fullName, Integer credit) {
        this.id = id;
        this.courseCode = courseCode;
        this.fullName = fullName;
        this.credit = credit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }
    public List<CourseSection> getCourseSections() { return courseSections; }
    public void setCourseSections(List<CourseSection> courseSections) { this.courseSections = courseSections; }
}
