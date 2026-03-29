package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Config.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Function;

public abstract class AbstractDAO {

    protected <T> T execute(Function<EntityManager, T> action) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            T result = action.apply(em);

            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }
}