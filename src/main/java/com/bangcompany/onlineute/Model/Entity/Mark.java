package com.bangcompany.onlineute.Model.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private CourseRegistration courseRegistration;

    @Column(name = "process_score", precision = 5, scale = 2)
    private BigDecimal processScore; // Use BigDecimal for DECIMAL(5,2) alignment

    @Column(name = "test_score", precision = 5, scale = 2)
    private BigDecimal testScore;

    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;

    @Column(name = "grade_char")
    private String gradeChar;

    public Mark() {}

    public Mark(Long id, BigDecimal processScore, BigDecimal testScore, BigDecimal finalScore, String gradeChar) {
        this.id = id;
        this.processScore = processScore;
        this.testScore = testScore;
        this.finalScore = finalScore;
        this.gradeChar = gradeChar;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CourseRegistration getCourseRegistration() { return courseRegistration; }
    public void setCourseRegistration(CourseRegistration courseRegistration) { this.courseRegistration = courseRegistration; }
    public BigDecimal getProcessScore() { return processScore; }
    public void setProcessScore(BigDecimal processScore) { this.processScore = processScore; }
    public BigDecimal getTestScore() { return testScore; }
    public void setTestScore(BigDecimal testScore) { this.testScore = testScore; }
    public BigDecimal getFinalScore() { return finalScore; }
    public void setFinalScore(BigDecimal finalScore) { this.finalScore = finalScore; }
    public String getGradeChar() { return gradeChar; }
    public void setGradeChar(String gradeChar) { this.gradeChar = gradeChar; }
}
