package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public Account save(Account account) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (account.getId() == null) {
                em.persist(account);
            } else {
                account = em.merge(account);
            }
            em.getTransaction().commit();
            return account;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Account account = em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(account);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
