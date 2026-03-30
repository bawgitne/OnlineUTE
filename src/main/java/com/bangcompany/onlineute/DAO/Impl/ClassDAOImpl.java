package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.Entity.Class;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClassDAOImpl implements ClassDAO {
    @Override
    public List<Class> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Class c", Class.class).getResultList();
        } finally {
            em.close();
        }
    }
}
