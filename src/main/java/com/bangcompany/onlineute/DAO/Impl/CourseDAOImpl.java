package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.CourseDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {
    @Override
    public Course save(Course course) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
            return course;
        } finally {
            em.close();
        }
    }


    @Override
    public Course update(Course course) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(course);
            em.getTransaction().commit();
            return course;
        } finally {
            em.close();
        }
    }


    @Override
    public Course delete(Course course) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(course);
            em.getTransaction().commit();
            return course;
        } finally {
            em.close();
        }
    }

    @Override
    public Course findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Course.class, id)).orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Course> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Course c ORDER BY c.courseCode", Course.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Course> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Course> items = em.createQuery(
                            "SELECT c FROM Course c " +
                                    "WHERE LOWER(c.courseCode) LIKE :keyword " +
                                    "OR LOWER(c.fullName) LIKE :keyword " +
                                    "ORDER BY c.courseCode",
                            Course.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(c) FROM Course c " +
                                    "WHERE LOWER(c.courseCode) LIKE :keyword " +
                                    "OR LOWER(c.fullName) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        } finally {
            em.close();
        }
    }
}
