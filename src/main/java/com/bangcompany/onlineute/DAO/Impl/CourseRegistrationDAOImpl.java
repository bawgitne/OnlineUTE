package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.CourseRegistrationDAO;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class CourseRegistrationDAOImpl implements CourseRegistrationDAO {

    @Override
    public CourseRegistration save(CourseRegistration registration) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (registration.getId() == null) {
                em.persist(registration);
            } else {
                registration = em.merge(registration);
            }
            em.getTransaction().commit();
            return registration;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public CourseRegistration update(CourseRegistration registration) {
        return save(registration);
    }

    @Override
    public void delete(CourseRegistration registration) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            registration = em.merge(registration);
            em.remove(registration);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<CourseRegistration> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(CourseRegistration.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<CourseRegistration> findByStudentId(Long studentId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT cr FROM CourseRegistration cr JOIN FETCH cr.courseSection cs JOIN FETCH cs.course LEFT JOIN FETCH cr.mark WHERE cr.student.id = :studentId", CourseRegistration.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CourseRegistration> findByCourseSectionId(Long sectionId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT cr FROM CourseRegistration cr JOIN FETCH cr.student LEFT JOIN FETCH cr.mark WHERE cr.courseSection.id = :sectionId", CourseRegistration.class)
                    .setParameter("sectionId", sectionId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean isRegistered(Long studentId, Long sectionId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(cr) FROM CourseRegistration cr WHERE cr.student.id = :studentId AND cr.courseSection.id = :sectionId", Long.class)
                .setParameter("studentId", studentId)
                .setParameter("sectionId", sectionId)
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}
