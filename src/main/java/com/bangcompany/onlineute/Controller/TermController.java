package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.Service.TermService;
import java.util.List;
import java.util.Optional;

public class TermController {
    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    public List<Term> getAllTerms() {
        return termService.getAllTerms();
    }

    public Optional<Term> getTermById(Long id) {
        return termService.getTermById(id);
    }

    public Optional<Term> getCurrentTerm() {
        return termService.getCurrentTerm();
    }

    public Term createTerm(Term term){
        return termService.createTerm(term);
    }

    public Term updateTerm(Term term){
        return termService.updateTerm(term);
    }

    public void deleteTerm(Term term){
        termService.deleteTerm(term);
    }
}
