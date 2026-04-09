package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.LecturerDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Lecturer;

import java.util.List;
import java.util.Optional;

public class LecturerDAOImpl extends AbstractDAO<Lecturer> implements LecturerDAO {
    public LecturerDAOImpl() {
        super(Lecturer.class);
    }

    @Override
    public Lecturer save(Lecturer lecturer) {
        return saveEntity(lecturer);
    }

    @Override
    public List<Lecturer> findAll() {
        return executeRead(em -> em.createQuery("SELECT l FROM Lecturer l ORDER BY l.fullName", Lecturer.class)
                .getResultList());
    }

    @Override
    public Optional<Lecturer> findByAccountId(Long accountId) {
        return executeRead(em -> {
            Lecturer lecturer = em.createQuery("SELECT l FROM Lecturer l WHERE l.account.id = :accountId", Lecturer.class)
                    .setParameter("accountId", accountId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(lecturer);
        });
    }

    @Override
    public PagedResult<Lecturer> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Lecturer> items = em.createQuery(
                            "SELECT DISTINCT l FROM Lecturer l " +
                                    "WHERE LOWER(l.code) LIKE :keyword " +
                                    "OR LOWER(l.fullName) LIKE :keyword " +
                                    "ORDER BY l.fullName, l.code",
                            Lecturer.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(DISTINCT l.id) FROM Lecturer l " +
                                    "WHERE LOWER(l.code) LIKE :keyword " +
                                    "OR LOWER(l.fullName) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        });
    }

    @Override
    public long countAll() {
        return executeRead(em -> em.createQuery("SELECT COUNT(l) FROM Lecturer l", Long.class)
                .getSingleResult());
    }

    @Override
    public void deleteById(Long id) {
        deleteEntityById(id);
    }
}