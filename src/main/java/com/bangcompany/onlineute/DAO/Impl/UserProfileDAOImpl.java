package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.UserProfileDAO;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class UserProfileDAOImpl implements UserProfileDAO {
    @Override
    public UserProfile save(UserProfile userProfile) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (userProfile.getId() == null) {
                em.persist(userProfile);
            } else {
                userProfile = em.merge(userProfile);
            }
            em.getTransaction().commit();
            return userProfile;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<UserProfile> findByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            UserProfile userProfile = em.createQuery(
                            "SELECT up FROM UserProfile up WHERE up.account.id = :accountId",
                            UserProfile.class
                    )
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return Optional.of(userProfile);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
