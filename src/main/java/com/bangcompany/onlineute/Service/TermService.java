package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Term;
import java.util.List;
import java.util.Optional;

public interface TermService {
    List<Term> getAllTerms();
    
    Optional<Term> getTermById(Long id);
    
    Optional<Term> getCurrentTerm();
    
    Term createTerm(Term term);

    Term updateTerm(Term term);

    void deleteTerm(Term term);
}
