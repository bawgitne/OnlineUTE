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

    // lưu thông tin điểm số sau khi đã tính toán điểm tổng kết
    @Override
    public Mark saveMark(Mark mark) {
        calculateGrade(mark);
        return markDAO.save(mark);
    }

    // xóa điểm số khỏi hệ thống
    @Override
    public void deleteMark(Mark mark) {
        markDAO.delete(mark);
    }

    // lấy thông tin điểm theo ID
    @Override
    public Optional<Mark> getMarkById(Long id) {
        return markDAO.findById(id);
    }

    // lấy thông tin điểm dựa trên bản ghi đăng ký học phần
    @Override
    public Optional<Mark> getMarkByRegistration(Long registrationId) {
        return markDAO.findByRegistrationId(registrationId);
    }

    // thực hiện tính toán điểm tổng kết và quy đổi sang điểm chữ
    @Override
    public void calculateGrade(Mark mark) {
        BigDecimal process = mark.getProcessScore() != null ? mark.getProcessScore() : BigDecimal.ZERO;
        BigDecimal test = mark.getTestScore() != null ? mark.getTestScore() : BigDecimal.ZERO;
        
        // Điểm tổng kết = 30% Điểm quá trình + 70% Điểm thi (Quy định chuẩn học thuật)
        BigDecimal finalScore = process.multiply(new BigDecimal("0.3"))
                .add(test.multiply(new BigDecimal("0.7")))
                .setScale(2, RoundingMode.HALF_UP);
        
        mark.setFinalScore(finalScore);
        
        // Quy đổi điểm hệ 10 sang điểm chữ (A, B, C, D, F) theo chuẩn Việt Nam
        if (finalScore.compareTo(new BigDecimal("8.5")) >= 0) mark.setGradeChar("A");
        else if (finalScore.compareTo(new BigDecimal("7.0")) >= 0) mark.setGradeChar("B");
        else if (finalScore.compareTo(new BigDecimal("5.5")) >= 0) mark.setGradeChar("C");
        else if (finalScore.compareTo(new BigDecimal("4.0")) >= 0) mark.setGradeChar("D");
        else mark.setGradeChar("F");
    }
}
