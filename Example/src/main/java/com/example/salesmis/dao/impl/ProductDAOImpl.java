package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    ORDER BY p.id
                    """, Product.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Product.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Product> list = em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE LOWER(p.sku) = LOWER(:sku)
                    """, Product.class)
                    .setParameter("sku", sku)
                    .getResultList();

            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE LOWER(p.sku) LIKE LOWER(:kw)
                       OR LOWER(p.productName) LIKE LOWER(:kw)
                       OR LOWER(COALESCE(p.category, '')) LIKE LOWER(:kw)
                    ORDER BY p.productName
                    """, Product.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Product save(Product product) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(product);
            tx.commit();
            return product;
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
    public Product update(Product product) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Product merged = em.merge(product);
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
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
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
    public boolean existsBySku(String sku) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(p)
                    FROM Product p
                    WHERE LOWER(p.sku) = LOWER(:sku)
                    """, Long.class)
                    .setParameter("sku", sku)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}
