package com.bangcompany.onlineute.Config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.function.Supplier;

/**
 * JpaUtil - Singleton utility to manage the EntityManagerFactory and provide EntityManagers.
 * Modeled after the Example project's manual database management.
 */
public final class JpaUtil {
    static {
        // Tắt log khởi động loằng ngoằng của Hibernate cho sạch console
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
    }

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("OnlineUtePU");
    private static final ThreadLocal<EntityManager> CONTEXT_EM = new ThreadLocal<>();

    private JpaUtil() {
    }

    public static EntityManager getEntityManager() {
        EntityManager em = CONTEXT_EM.get();
        if (em != null && em.isOpen()) {
            return em;
        }
        return EMF.createEntityManager();
    }

    public static boolean isManaged(EntityManager em) {
        return em != null && em == CONTEXT_EM.get();
    }

    public static <T> T doInTransaction(Supplier<T> work) {
        EntityManager existing = CONTEXT_EM.get();
        if (existing != null) {
            return work.get();
        }
        EntityManager em = EMF.createEntityManager();
        CONTEXT_EM.set(em);
        var tx = em.getTransaction();
        tx.begin();
        try {
            T result = work.get();
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            CONTEXT_EM.remove();
            em.close();
        }
    }

    public static void doInTransaction(Runnable work) {
        doInTransaction(() -> {
            work.run();
            return null;
        });
    }

    public static void shutdown() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
