package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class CourseSectionDAOImpl implements CourseSectionDAO {

    @Override
    public CourseSection save(CourseSection courseSection) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (courseSection.getId() == null) {
                em.persist(courseSection);
            } else {
                courseSection = em.merge(courseSection);
            }
            em.getTransaction().commit();
            return courseSection;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public CourseSection update(CourseSection courseSection) {
        return save(courseSection);
    }

    @Override
    public void delete(CourseSection courseSection) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            courseSection = em.merge(courseSection);
            em.remove(courseSection);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<CourseSection> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(CourseSection.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<CourseSection> findByTermId(Long termId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT cs FROM CourseSection cs WHERE cs.term.id = :termId", CourseSection.class)
                    .setParameter("termId", termId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CourseSection> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT cs FROM CourseSection cs JOIN FETCH cs.course LEFT JOIN FETCH cs.lecturer", CourseSection.class).getResultList();
        } finally {
            em.close();
        }
    }
}
