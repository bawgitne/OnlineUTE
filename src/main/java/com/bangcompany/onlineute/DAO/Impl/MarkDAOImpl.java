package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.MarkDAO;
import com.bangcompany.onlineute.Model.Entity.Mark;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class MarkDAOImpl implements MarkDAO {

    @Override
    public Mark save(Mark mark) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (mark.getId() == null) {
                em.persist(mark);
            } else {
                mark = em.merge(mark);
            }
            em.getTransaction().commit();
            return mark;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Mark update(Mark mark) {
        return save(mark);
    }

    @Override
    public void delete(Mark mark) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            mark = em.merge(mark);
            em.remove(mark);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Mark> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Mark.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Mark> findByRegistrationId(Long registrationId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mark m WHERE m.courseRegistration.id = :registrationId", Mark.class)
                    .setParameter("registrationId", registrationId)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }
}
