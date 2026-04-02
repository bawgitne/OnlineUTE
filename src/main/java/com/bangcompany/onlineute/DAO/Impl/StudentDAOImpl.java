package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.StudentDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public Student save(Student student) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (student.getId() == null) {
                em.persist(student);
            } else {
                student = em.merge(student);
            }
            em.getTransaction().commit();
            return student;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Student student = em.createQuery(
                            "SELECT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.faculty " +
                                    "WHERE s.id = :id",
                            Student.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(student);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Student> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Student student = em.createQuery(
                            "SELECT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.faculty " +
                                    "WHERE s.account.id = :accountId",
                            Student.class
                    )
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(student);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Student> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.faculty " +
                                    "ORDER BY s.fullName, s.code",
                            Student.class
                    )
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Student> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Student> items = em.createQuery(
                            "SELECT DISTINCT s FROM Student s " +
                                    "LEFT JOIN FETCH s.classEntity c " +
                                    "LEFT JOIN FETCH c.faculty f " +
                                    "WHERE LOWER(s.code) LIKE :keyword " +
                                    "OR LOWER(s.fullName) LIKE :keyword " +
                                    "OR LOWER(s.email) LIKE :keyword " +
                                    "OR LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword " +
                                    "ORDER BY s.fullName, s.code",
                            Student.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(DISTINCT s.id) FROM Student s " +
                                    "LEFT JOIN s.classEntity c " +
                                    "LEFT JOIN c.faculty f " +
                                    "WHERE LOWER(s.code) LIKE :keyword " +
                                    "OR LOWER(s.fullName) LIKE :keyword " +
                                    "OR LOWER(s.email) LIKE :keyword " +
                                    "OR LOWER(c.className) LIKE :keyword " +
                                    "OR LOWER(f.fullName) LIKE :keyword",
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
    public long countAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(s) FROM Student s", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByCodePrefix(String codePrefix) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(s) FROM Student s WHERE s.code LIKE :codePrefix",
                            Long.class
                    )
                    .setParameter("codePrefix", codePrefix + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
