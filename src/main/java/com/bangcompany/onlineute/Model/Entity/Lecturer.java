package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "lecturer", indexes = {
    @Index(name = "idx_lecturer_fullname", columnList = "fullname")
})
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    private List<CourseSection> courseSections;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Lecturer() {}

    public Lecturer(Long id, String code, String fullName) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public List<CourseSection> getCourseSections() { return courseSections; }
    public void setCourseSections(List<CourseSection> courseSections) { this.courseSections = courseSections; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
