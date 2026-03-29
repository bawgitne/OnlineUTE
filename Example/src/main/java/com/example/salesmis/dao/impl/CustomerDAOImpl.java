package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    ORDER BY c.id
                    """, Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Customer.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findByCode(String customerCode) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Customer> list = em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.customerCode) = LOWER(:customerCode)
                    """, Customer.class)
                    .setParameter("customerCode", customerCode)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Customer> list = em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.email) = LOWER(:email)
                    """, Customer.class)
                    .setParameter("email", email)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE LOWER(c.customerCode) LIKE LOWER(:kw)
                       OR LOWER(c.fullName) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(c.phone, '')) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(c.email, '')) LIKE LOWER(:kw)
                    ORDER BY c.fullName
                    """, Customer.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Customer save(Customer customer) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(customer);
            tx.commit();
            return customer;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Customer update(Customer customer) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Customer merged = em.merge(customer);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByCode(String customerCode) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(c)
                    FROM Customer c
                    WHERE LOWER(c.customerCode) = LOWER(:customerCode)
                    """, Long.class)
                    .setParameter("customerCode", customerCode)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(c)
                    FROM Customer c
                    WHERE LOWER(c.email) = LOWER(:email)
                    """, Long.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}
