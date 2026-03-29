package com.bangcompany.onlineute.Config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * JpaUtil - Singleton utility to manage the EntityManagerFactory and provide EntityManagers.
 * Modeled after the Example project's manual database management.
 */
public final class JpaUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("OnlineUtePU");

    private JpaUtil() {
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void shutdown() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
