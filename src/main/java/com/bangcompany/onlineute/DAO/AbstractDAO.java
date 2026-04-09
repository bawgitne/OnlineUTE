package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Config.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractDAO<T> {
    private final Class<T> entityClass;

    protected AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected <R> R executeRead(Function<EntityManager, R> action) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return action.apply(em);
        } finally {
            if (!JpaUtil.isManaged(em)) {
                em.close();
            }
        }
    }

    protected <R> R executeWrite(Function<EntityManager, R> action) {
        EntityManager em = JpaUtil.getEntityManager();
        boolean managed = JpaUtil.isManaged(em);
        EntityTransaction tx = em.getTransaction();

        try {
            if (!managed) {
                tx.begin();
            }
            R result = action.apply(em);
            if (!managed) {
                tx.commit();
            }
            return result;
        } catch (RuntimeException e) {
            if (!managed && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (!managed) {
                em.close();
            }
        }
    }

    protected T saveEntity(T entity) {
        return executeWrite(em -> {
            Long id = getEntityId(entity);
            if (id == null) {
                em.persist(entity);
                return entity;
            }
            return em.merge(entity);
        });
    }

    protected void deleteEntityById(Long id) {
        if (id == null) {
            return;
        }
        executeWrite(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }

    protected T findEntityById(Long id) {
        return executeRead(em -> em.find(entityClass, id));
    }

    protected List<T> findAllEntities() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return executeRead(em -> em.createQuery(jpql, entityClass).getResultList());
    }

    private Long getEntityId(T entity) {
        if (entity == null) {
            return null;
        }
        try {
            Method method = entityClass.getMethod("getId");
            Object value = method.invoke(entity);
            return (Long) value;
        } catch (Exception ex) {
            return null;
        }
    }
}