package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faculty")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty_code", unique = true, nullable = false)
    private String facultyCode;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<Major> majors = new ArrayList<>();

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<Class> classes = new ArrayList<>();

    public Faculty() {
    }

    public Faculty(Long id, String facultyCode, String fullName) {
        this.id = id;
        this.facultyCode = facultyCode;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Major> getMajors() {
        return majors;
    }

    public void setMajors(List<Major> majors) {
        this.majors = majors;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return facultyCode + " - " + fullName;
    }
}
