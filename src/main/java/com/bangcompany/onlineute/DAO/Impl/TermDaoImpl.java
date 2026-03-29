package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.TermDAO;
import com.bangcompany.onlineute.Model.Entity.Term;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class TermDAOImpl implements TermDAO {

    @Override
    public List<Term> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Term t", Term.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Term> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Term.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Term> findCurrentTerm() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Term t WHERE t.isCurrent = true", Term.class)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Term save(Term term) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (term.getId() == null) {
                em.persist(term);
            } else {
                term = em.merge(term);
            }
            em.getTransaction().commit();
            return term;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Term update(Term term) {
        return save(term); // save/merge is used for updates in Hibernate/JPA if logic is same
    }

    @Override
    public void delete(Term term) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            term = em.merge(term); // Ensure it's attached
            em.remove(term);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
