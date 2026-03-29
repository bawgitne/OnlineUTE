package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "birth_of_date")
    private LocalDate birthOfDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_program_id", nullable = false)
    private StudyProgram studyProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_term_id", nullable = false)
    private Term term;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<CourseRegistration> courseRegistrations;

    public Student() {}

    public Student(Long id, String code, String fullName, LocalDate birthOfDate, String email, String avatarUrl, Account account) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.birthOfDate = birthOfDate;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public LocalDate getBirthOfDate() { return birthOfDate; }
    public void setBirthOfDate(LocalDate birthOfDate) { this.birthOfDate = birthOfDate; }
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

