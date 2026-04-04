package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Class;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClassDAOImpl implements ClassDAO {
    @Override
    public List<Class> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Class c JOIN FETCH c.faculty ORDER BY c.className",
                    Class.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Class> findByFacultyId(Long facultyId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Class c JOIN FETCH c.faculty f WHERE f.id = :facultyId ORDER BY c.className",
                    Class.class
            ).setParameter("facultyId", facultyId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Class> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Class> items = em.createQuery(
                            "SELECT c FROM Class c JOIN FETCH c.faculty f " +
                                    "WHERE LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword " +
                                    "ORDER BY c.className",
                            Class.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(c) FROM Class c JOIN c.faculty f " +
                                    "WHERE LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "OR LOWER(f.facultyCode) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        } finally {
            em.close();
        }
    }

    @Override
    public Class save(Class classEntity) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(classEntity);
            em.getTransaction().commit();
            return classEntity;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
