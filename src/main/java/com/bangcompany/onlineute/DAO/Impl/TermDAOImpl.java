package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.TermDAO;
import com.bangcompany.onlineute.Model.Entity.Term;
import java.util.List;
import java.util.Optional;

public class TermDAOImpl extends AbstractDAO<Term> implements TermDAO {
    public TermDAOImpl() {
        super(Term.class);
    }

    @Override
    public List<Term> findAll() {
        return findAllEntities();
    }

    @Override
    public Optional<Term> findById(Long id) {
        return Optional.ofNullable(findEntityById(id));
    }

    @Override
    public Optional<Term> findCurrentTerm() {
        return executeRead(em -> em.createQuery("SELECT t FROM Term t WHERE t.isCurrent = true", Term.class)
                .getResultStream()
                .findFirst());
    }

    @Override
    public Term save(Term term) {
        return saveEntity(term);
    }

    @Override
    public Term update(Term term) {
        return saveEntity(term);
    }

    @Override
    public void delete(Term term) {
        if (term == null) {
            return;
        }
        deleteEntityById(term.getId());
    }
}