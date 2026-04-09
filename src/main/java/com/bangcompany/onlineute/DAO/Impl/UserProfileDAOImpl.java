package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.UserProfileDAO;
import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public class UserProfileDAOImpl extends AbstractDAO<UserProfile> implements UserProfileDAO {
    public UserProfileDAOImpl() {
        super(UserProfile.class);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return saveEntity(userProfile);
    }

    @Override
    public Optional<UserProfile> findByAccountId(Long accountId) {
        return executeRead(em -> {
            UserProfile userProfile = em.createQuery(
                            "SELECT up FROM UserProfile up WHERE up.account.id = :accountId",
                            UserProfile.class
                    )
                    .setParameter("accountId", accountId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(userProfile);
        });
    }
}