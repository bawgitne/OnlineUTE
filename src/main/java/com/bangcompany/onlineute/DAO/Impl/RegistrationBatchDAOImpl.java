package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.RegistrationBatchDAO;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RegistrationBatchDAOImpl implements RegistrationBatchDAO {
    @Override
    public RegistrationBatch save(RegistrationBatch registrationBatch) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (registrationBatch.getId() == null) {
                em.persist(registrationBatch);
            } else {
                registrationBatch = em.merge(registrationBatch);
            }
            em.getTransaction().commit();
            return registrationBatch;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<RegistrationBatch> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<RegistrationBatch> result = em.createQuery(
                            "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term WHERE rb.id = :id",
                            RegistrationBatch.class
                    )
                    .setParameter("id", id)
                    .getResultList();
            return result.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<RegistrationBatch> findByTermId(Long termId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term t WHERE t.id = :termId ORDER BY rb.createdAt DESC",
                            RegistrationBatch.class
                    )
                    .setParameter("termId", termId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<RegistrationBatch> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term ORDER BY rb.createdAt DESC",
                    RegistrationBatch.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<RegistrationBatch> findOpenBatches(LocalDateTime currentTime) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT rb FROM RegistrationBatch rb " +
                                    "JOIN FETCH rb.term " +
                                    "WHERE rb.openAt <= :currentTime AND rb.closeAt >= :currentTime " +
                                    "ORDER BY rb.openAt ASC",
                            RegistrationBatch.class
                    )
                    .setParameter("currentTime", currentTime)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
