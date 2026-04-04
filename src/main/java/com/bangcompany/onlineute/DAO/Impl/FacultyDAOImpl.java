package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.FacultyDAO;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FacultyDAOImpl implements FacultyDAO {
    @Override
    public List<Faculty> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Faculty f ORDER BY f.fullName", Faculty.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Faculty save(Faculty faculty) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(faculty);
            em.getTransaction().commit();
            return faculty;
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
