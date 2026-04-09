package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.AccountDAO;
import com.bangcompany.onlineute.Model.Entity.Account;
import java.util.Optional;

public class AccountDAOImpl extends AbstractDAO<Account> implements AccountDAO {
    public AccountDAOImpl() {
        super(Account.class);
    }

    // lưu account (tạo mới hoặc update)
    @Override
    public Account save(Account account) {
        return saveEntity(account);
    }

    // tìm account bằng username hoặc mã sv/gv/admin (không phân biệt hoa thường)
    @Override
    public Optional<Account> findByLoginCode(String loginCode) {
        return executeRead(em -> {
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
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(account);
        });
    }

    // tìm theo id chính của bảng account
    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(findEntityById(id));
    }
}