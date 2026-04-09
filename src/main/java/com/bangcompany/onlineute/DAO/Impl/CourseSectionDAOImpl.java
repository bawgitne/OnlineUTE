package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.CourseSectionDAO;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import java.util.List;
import java.util.Optional;

public class CourseSectionDAOImpl extends AbstractDAO<CourseSection> implements CourseSectionDAO {
    public CourseSectionDAOImpl() {
        super(CourseSection.class);
    }

    // lưu lớp học phần mới
    @Override
    public CourseSection save(CourseSection courseSection) {
        return saveEntity(courseSection);
    }

    // cập nhật lớp học phần đã có
    @Override
    public CourseSection update(CourseSection courseSection) {
        return saveEntity(courseSection);
    }

    // xóa lớp học phần
    @Override
    public void delete(CourseSection courseSection) {
        if (courseSection == null) {
            return;
        }
        deleteEntityById(courseSection.getId());
    }

    // tìm lớp theo id, fetch luôn môn học và giảng viên cho nhanh
    @Override
    public Optional<CourseSection> findById(Long id) {
        return executeRead(em -> {
            List<CourseSection> result = em.createQuery(
                            "SELECT cs FROM CourseSection cs " +
                                    "JOIN FETCH cs.course " +
                                    "LEFT JOIN FETCH cs.lecturer " +
                                    "LEFT JOIN FETCH cs.registrationBatch rb " +
                                    "LEFT JOIN FETCH rb.term " +
                                    "WHERE cs.id = :id",
                            CourseSection.class
                    )
                    .setParameter("id", id)
                    .getResultList();
            return result.stream().findFirst();
        });
    }

    // tìm các lớp của học kỳ nào đó
    @Override
    public List<CourseSection> findByTermId(Long termId) {
        return executeRead(em -> em.createQuery(
                        "SELECT cs FROM CourseSection cs WHERE cs.term.id = :termId",
                        CourseSection.class
                )
                .setParameter("termId", termId)
                .getResultList());
    }

    // tìm các lớp thuộc đợt đăng ký môn cụ thể
    @Override
    public List<CourseSection> findByRegistrationBatchId(Long registrationBatchId) {
        return executeRead(em -> em.createQuery(
                        "SELECT cs FROM CourseSection cs " +
                                "JOIN FETCH cs.course " +
                                "LEFT JOIN FETCH cs.lecturer " +
                                "WHERE cs.registrationBatch.id = :registrationBatchId",
                        CourseSection.class
                )
                .setParameter("registrationBatchId", registrationBatchId)
                .getResultList());
    }

    // tìm các lớp bị trùng giờ học trong cùng học kỳ (để check conflict)
    @Override
    public List<CourseSection> findConflictingSections(Long termId, Integer dayOfWeek, Integer startSlot, Integer endSlot) {
        return executeRead(em -> em.createQuery(
                        "SELECT cs FROM CourseSection cs " +
                                "JOIN FETCH cs.course " +
                                "LEFT JOIN FETCH cs.lecturer " +
                                "WHERE cs.term.id = :termId " +
                                "AND cs.dayOfWeek = :dayOfWeek " +
                                "AND cs.startSlot <= :endSlot " +
                                "AND cs.endSlot >= :startSlot",
                        CourseSection.class
                )
                .setParameter("termId", termId)
                .setParameter("dayOfWeek", dayOfWeek)
                .setParameter("startSlot", startSlot)
                .setParameter("endSlot", endSlot)
                .getResultList());
    }

    @Override
    public List<CourseSection> findAll() {
        return executeRead(em -> em.createQuery(
                        "SELECT cs FROM CourseSection cs JOIN FETCH cs.course LEFT JOIN FETCH cs.lecturer",
                        CourseSection.class
                ).getResultList());
    }
}