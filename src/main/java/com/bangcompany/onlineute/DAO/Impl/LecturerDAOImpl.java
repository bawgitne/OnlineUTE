package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class LecturerDAOImpl implements LecturerDAO {

    @Override
    public Lecturer save(Lecturer lecturer) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (lecturer.getId() == null) {
                em.persist(lecturer);
            } else {
                lecturer = em.merge(lecturer);
            }
            em.getTransaction().commit();
            return lecturer;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Lecturer> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Lecturer lecturer = em.createQuery("SELECT l FROM Lecturer l WHERE l.account.id = :accountId", Lecturer.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(lecturer);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
