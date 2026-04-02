package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "term")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_name", nullable = false)
    private String yearName;

    @Column(name = "term_name", nullable = false)
    private String termName;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent;


    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    private List<CourseSection> courseSections;

    public Term() {}

    public Term(Long id, String yearName, String termName, Boolean isCurrent) {
        this.id = id;
        this.yearName = yearName;
        this.termName = termName;
        this.isCurrent = isCurrent;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getYearName() { return yearName; }
    public void setYearName(String yearName) { this.yearName = yearName; }
    public String getTermName() { return termName; }
    public void setTermName(String termName) { this.termName = termName; }
    public Boolean getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
    public List<CourseSection> getCourseSections() { return courseSections; }
    public void setCourseSections(List<CourseSection> courseSections) { this.courseSections = courseSections; }

    @Override
    public String toString() {
        return termName + " - " + yearName;
    }
}
