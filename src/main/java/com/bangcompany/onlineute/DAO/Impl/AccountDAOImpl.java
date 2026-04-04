package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Admin;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
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
    public Optional<Account> findByLoginCode(String loginCode) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedCode = loginCode == null ? "" : loginCode.trim().toLowerCase();
            Account account = em.createQuery(
                            "SELECT a FROM Account a " +
                                    "WHERE LOWER(a.username) = :loginCode " +
                                    "OR EXISTS (SELECT s.id FROM Student s WHERE s.account.id = a.id AND LOWER(s.code) = :loginCode) " +
                                    "OR EXISTS (SELECT l.id FROM Lecturer l WHERE l.account.id = a.id AND LOWER(l.code) = :loginCode) " +
                                    "OR EXISTS (SELECT ad.id FROM Admin ad WHERE ad.account.id = a.id AND LOWER(ad.code) = :loginCode)",
                            Account.class
                    )
                    .setParameter("loginCode", normalizedCode)
                    .getSingleResult();
            return Optional.of(account);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
