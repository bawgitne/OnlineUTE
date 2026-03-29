package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Term;
import java.util.List;
import java.util.Optional;

public interface TermDAO {
    List<Term> findAll();
    
    Optional<Term> findById(Long id);
    
    Optional<Term> findCurrentTerm();
    
    Term save(Term term);

    Term update(Term term);

    void delete(Term term);
}
