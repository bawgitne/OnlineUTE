package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.AnnouncementDAO;
import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.DTO.PaginationSupport;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;

public class AnnouncementDAOImpl extends AbstractDAO<Announcement> implements AnnouncementDAO {
    public AnnouncementDAOImpl() {
        super(Announcement.class);
    }

    // lưu tin mới vào db
    @Override
    public void create(Announcement announcement) {
        saveEntity(announcement);
    }

    // lấy hết thông báo, tin mới nhất lên đầu
    @Override
    public List<Announcement> findAll() {
        return executeRead(em -> em.createQuery("SELECT a FROM Announcement a ORDER BY a.createdAt DESC", Announcement.class)
                .getResultList());
    }

    // search tin theo tiêu đề, nội dung, người gửi... có phân trang
    @Override
    public PagedResult<Announcement> search(String keyword, PageRequest pageRequest) {
        return executeRead(em -> {
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
        });
    }

    // lấy tin theo đối tượng (tất cả, sv, gv)
    @Override
    public List<Announcement> findByTargetType(String targetType) {
        return executeRead(em -> {
            TypedQuery<Announcement> query = em.createQuery(
                    "SELECT a FROM Announcement a WHERE a.targetType = :tt ORDER BY a.createdAt DESC",
                    Announcement.class);
            query.setParameter("tt", targetType);
            return query.getResultList();
        });
    }

    // lấy những tin mà sv này được xem (tin chung + tin của lớp sv đang học)
    @Override
    public List<Announcement> findAnnouncementsForStudent(Long studentId) {
        if (studentId == null) return new ArrayList<>();

        return executeRead(em -> em.createQuery("""
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
                .getResultList());
    }

    // lấy những tin mà gv này được xem (tin chung + tin của lớp gv đang dạy)
    @Override
    public List<Announcement> findAnnouncementsForLecturer(Long lecturerId) {
        if (lecturerId == null) return new ArrayList<>();

        return executeRead(em -> em.createQuery("""
                    SELECT a
                    FROM Announcement a
                    WHERE a.targetType = 'ALL'
                       OR a.targetType = 'ALL_LECTURERS'
                       OR (
                            a.targetType = 'COURSE_SECTION'
                            AND a.courseSectionId IN (
                                SELECT cs.id
                                FROM CourseSection cs
                                WHERE cs.lecturer.id = :lecturerId
                            )
                          )
                    ORDER BY a.createdAt DESC
                    """, Announcement.class)
                .setParameter("lecturerId", lecturerId)
                .getResultList());
    }
}