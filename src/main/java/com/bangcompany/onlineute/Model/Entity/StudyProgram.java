package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "study_program")
public class StudyProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_code", unique = true, nullable = false)
    private String programCode;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "total_credit", nullable = false)
    private Integer totalCredit;

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.LAZY)
    private List<Student> students;

    public StudyProgram() {}

    public StudyProgram(Long id, String programCode, String fullName, Integer totalCredit) {
        this.id = id;
        this.programCode = programCode;
        this.fullName = fullName;
        this.totalCredit = totalCredit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProgramCode() { return programCode; }
    public void setProgramCode(String programCode) { this.programCode = programCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getTotalCredit() { return totalCredit; }
    public void setTotalCredit(Integer totalCredit) { this.totalCredit = totalCredit; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    @Override
    public String toString() {
        return programCode + " - " + fullName;
    }
}
