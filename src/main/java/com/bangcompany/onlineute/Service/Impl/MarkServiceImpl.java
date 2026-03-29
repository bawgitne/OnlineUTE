package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.MarkDAO;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Service.MarkService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class MarkServiceImpl implements MarkService {
    private final MarkDAO markDAO;

    public MarkServiceImpl(MarkDAO markDAO) {
        this.markDAO = markDAO;
    }

    @Override
    public Mark saveMark(Mark mark) {
        calculateGrade(mark);
        return markDAO.save(mark);
    }

    @Override
    public void deleteMark(Mark mark) {
        markDAO.delete(mark);
    }

    @Override
    public Optional<Mark> getMarkById(Long id) {
        return markDAO.findById(id);
    }

    @Override
    public Optional<Mark> getMarkByRegistration(Long registrationId) {
        return markDAO.findByRegistrationId(registrationId);
    }

    @Override
    public void calculateGrade(Mark mark) {
        BigDecimal process = mark.getProcessScore() != null ? mark.getProcessScore() : BigDecimal.ZERO;
        BigDecimal test = mark.getTestScore() != null ? mark.getTestScore() : BigDecimal.ZERO;
        
        // Final Score = 30% Process + 70% Test (Standard Academic Formula)
        BigDecimal finalScore = process.multiply(new BigDecimal("0.3"))
                .add(test.multiply(new BigDecimal("0.7")))
                .setScale(2, RoundingMode.HALF_UP);
        
        mark.setFinalScore(finalScore);
        
        // Grade Letter Mapping (Vietnamese standard usually)
        if (finalScore.compareTo(new BigDecimal("8.5")) >= 0) mark.setGradeChar("A");
        else if (finalScore.compareTo(new BigDecimal("7.0")) >= 0) mark.setGradeChar("B");
        else if (finalScore.compareTo(new BigDecimal("5.5")) >= 0) mark.setGradeChar("C");
        else if (finalScore.compareTo(new BigDecimal("4.0")) >= 0) mark.setGradeChar("D");
        else mark.setGradeChar("F");
    }
}
