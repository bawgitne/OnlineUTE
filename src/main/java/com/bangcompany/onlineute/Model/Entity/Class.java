package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    public Class() {}

    public Class(Long id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    @Override
    public String toString() {
        return className;
    }
}