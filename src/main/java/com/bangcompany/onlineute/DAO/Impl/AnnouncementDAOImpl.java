package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.AnnouncementDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;

public class AnnouncementDAOImpl implements AnnouncementDAO {
    @Override
    public void create(Announcement announcement) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(announcement);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Announcement> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Announcement a ORDER BY a.createdAt DESC", Announcement.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PagedResult<Announcement> search(String keyword, PageRequest pageRequest) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String normalizedKeyword = "%" + keyword.toLowerCase() + "%";
            List<Announcement> items = em.createQuery(
                            "SELECT a FROM Announcement a " +
                                    "WHERE LOWER(a.title) LIKE :keyword " +
                                    "OR LOWER(a.content) LIKE :keyword " +
                                    "OR LOWER(a.senderName) LIKE :keyword " +
                                    "OR LOWER(a.targetType) LIKE :keyword " +
                                    "ORDER BY a.createdAt DESC",
                            Announcement.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .setFirstResult(PaginationSupport.offset(pageRequest))
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();

            Long totalItems = em.createQuery(
                            "SELECT COUNT(a) FROM Announcement a " +
                                    "WHERE LOWER(a.title) LIKE :keyword " +
                                    "OR LOWER(a.content) LIKE :keyword " +
                                    "OR LOWER(a.senderName) LIKE :keyword " +
                                    "OR LOWER(a.targetType) LIKE :keyword",
                            Long.class
                    )
                    .setParameter("keyword", normalizedKeyword)
                    .getSingleResult();

            return PaginationSupport.of(items, pageRequest, totalItems);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Announcement> findByTargetType(String targetType) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Announcement> query = em.createQuery(
                    "SELECT a FROM Announcement a WHERE a.targetType = :tt ORDER BY a.createdAt DESC"
                    , Announcement.class);
            query.setParameter("tt", targetType);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Announcement> findAnnouncementsForStudent(Long studentId) {
        if (studentId == null) return new ArrayList<>();

        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT a
                    FROM Announcement a
                    WHERE a.targetType = 'ALL'
                       OR a.targetType = 'ALL_STUDENTS'
                       OR (
                            a.targetType = 'COURSE_SECTION'
                            AND a.courseSectionId IN (
                                SELECT cr.courseSection.id
                                FROM CourseRegistration cr
                                WHERE cr.student.id = :studentId
                            )
                          )
                    ORDER BY a.createdAt DESC
                    """, Announcement.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
