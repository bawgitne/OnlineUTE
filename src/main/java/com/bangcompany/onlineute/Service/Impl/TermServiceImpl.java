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

    @Override
    public List<Term> getAllTerms() {
        return termDAO.findAll();
    }

    @Override
    public Optional<Term> getTermById(Long id) {
        return termDAO.findById(id);
    }

    @Override
    public Optional<Term> getCurrentTerm() {
        return termDAO.findCurrentTerm();
    }

    @Override
    public Term createTerm(Term term) {
        return termDAO.save(term);
    }

    @Override
    public Term updateTerm(Term term) {
        return termDAO.update(term);
    }

    @Override
    public void deleteTerm(Term term) {
        termDAO.delete(term);
    }
}
