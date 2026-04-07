package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.TermDAO;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.Service.TermService;
import java.util.List;
import java.util.Optional;

public class TermServiceImpl implements TermService {
    private final TermDAO termDAO;

    public TermServiceImpl(TermDAO termDAO) {
        this.termDAO = termDAO;
    }

    // lấy toàn bộ danh sách các học kỳ
    @Override
    public List<Term> getAllTerms() {
        return termDAO.findAll();
    }

    // lấy thông tin học kỳ theo ID
    @Override
    public Optional<Term> getTermById(Long id) {
        return termDAO.findById(id);
    }

    // lấy thông tin học kỳ hiện tại đang diễn ra
    @Override
    public Optional<Term> getCurrentTerm() {
        return termDAO.findCurrentTerm();
    }

    // tạo học kỳ mới
    @Override
    public Term createTerm(Term term) {
        return termDAO.save(term);
    }

    // cập nhật thông tin học kỳ
    @Override
    public Term updateTerm(Term term) {
        return termDAO.update(term);
    }

    // xóa học kỳ khỏi hệ thống
    @Override
    public void deleteTerm(Term term) {
        termDAO.delete(term);
    }
}
