package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.CourseDAO;
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
}
