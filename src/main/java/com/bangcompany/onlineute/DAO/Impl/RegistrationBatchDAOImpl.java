package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.RegistrationBatchDAO;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RegistrationBatchDAOImpl extends AbstractDAO<RegistrationBatch> implements RegistrationBatchDAO {
    public RegistrationBatchDAOImpl() {
        super(RegistrationBatch.class);
    }

    @Override
    public RegistrationBatch save(RegistrationBatch registrationBatch) {
        return saveEntity(registrationBatch);
    }

    @Override
    public Optional<RegistrationBatch> findById(Long id) {
        return executeRead(em -> {
            List<RegistrationBatch> result = em.createQuery(
                            "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term WHERE rb.id = :id",
                            RegistrationBatch.class
                    )
                    .setParameter("id", id)
                    .getResultList();
            return result.stream().findFirst();
        });
    }

    @Override
    public List<RegistrationBatch> findByTermId(Long termId) {
        return executeRead(em -> em.createQuery(
                        "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term t WHERE t.id = :termId ORDER BY rb.createdAt DESC",
                        RegistrationBatch.class
                )
                .setParameter("termId", termId)
                .getResultList());
    }

    @Override
    public List<RegistrationBatch> findAll() {
        return executeRead(em -> em.createQuery(
                        "SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term ORDER BY rb.createdAt DESC",
                        RegistrationBatch.class
                ).getResultList());
    }

    @Override
    public List<RegistrationBatch> findOpenBatches(LocalDateTime currentTime) {
        return executeRead(em -> em.createQuery(
                        "SELECT rb FROM RegistrationBatch rb " +
                                "JOIN FETCH rb.term " +
                                "WHERE rb.openAt <= :currentTime AND rb.closeAt >= :currentTime " +
                                "ORDER BY rb.openAt ASC",
                        RegistrationBatch.class
                )
                .setParameter("currentTime", currentTime)
                .getResultList());
    }
}