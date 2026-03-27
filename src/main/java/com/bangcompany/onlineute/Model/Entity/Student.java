package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", unique = true)
    private Integer studentCode;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "birth_of_date")
    private String birthOfDate;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_term_id")
    private Term term;

    @OneToOne(mappedBy = "student")
    private Account account;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<CourseRegistration> courseRegistrations;

    public Student() {}

    public Student(Long id, Integer studentCode, String fullName, String birthOfDate, String email, String avatarUrl) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.birthOfDate = birthOfDate;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getStudentCode() { return studentCode; }
    public void setStudentCode(Integer studentCode) { this.studentCode = studentCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBirthOfDate() { return birthOfDate; }
    public void setBirthOfDate(String birthOfDate) { this.birthOfDate = birthOfDate; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Class getClassEntity() { return classEntity; }
    public void setClassEntity(Class classEntity) { this.classEntity = classEntity; }
    public StudyProgram getStudyProgram() { return studyProgram; }
    public void setStudyProgram(StudyProgram studyProgram) { this.studyProgram = studyProgram; }
    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public List<CourseRegistration> getCourseRegistrations() { return courseRegistrations; }
    public void setCourseRegistrations(List<CourseRegistration> courseRegistrations) { this.courseRegistrations = courseRegistrations; }
}
