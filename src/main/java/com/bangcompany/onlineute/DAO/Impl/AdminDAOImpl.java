package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.AdminDAO;
import com.bangcompany.onlineute.Model.Entity.Admin;
import java.util.Optional;

public class AdminDAOImpl extends AbstractDAO<Admin> implements AdminDAO {
    public AdminDAOImpl() {
        super(Admin.class);
    }

    @Override
    public Admin save(Admin admin) {
        return saveEntity(admin);
    }

    @Override
    public Optional<Admin> findByAccountId(Long accountId) {
        return executeRead(em -> {
            Admin admin = em.createQuery("SELECT a FROM Admin a WHERE a.account.id = :accountId", Admin.class)
                    .setParameter("accountId", accountId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(admin);
        });
    }
}