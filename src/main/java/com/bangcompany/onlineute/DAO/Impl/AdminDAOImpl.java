package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.Model.Entity.Admin;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin save(Admin admin) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (admin.getId() == null) {
                em.persist(admin);
            } else {
                admin = em.merge(admin);
            }
            em.getTransaction().commit();
            return admin;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Admin> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Admin admin = em.createQuery("SELECT a FROM Admin a WHERE a.account.id = :accountId", Admin.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(admin);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
