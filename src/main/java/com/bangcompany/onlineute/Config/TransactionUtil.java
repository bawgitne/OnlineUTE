package com.bangcompany.onlineute.Config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Function;

public class TransactionUtil {

    public static <T> T execute(Function<EntityManager, T> action) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;

        } finally {
            em.close();
        }
    }
}