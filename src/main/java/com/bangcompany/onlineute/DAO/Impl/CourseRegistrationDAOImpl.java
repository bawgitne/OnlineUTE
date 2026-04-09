package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.CourseRegistrationDAO;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import java.util.List;
import java.util.Optional;

public class CourseRegistrationDAOImpl extends AbstractDAO<CourseRegistration> implements CourseRegistrationDAO {
    public CourseRegistrationDAOImpl() {
        super(CourseRegistration.class);
    }

    // lưu đăng ký môn mới
    @Override
    public CourseRegistration save(CourseRegistration registration) {
        return saveEntity(registration);
    }

    @Override
    public CourseRegistration update(CourseRegistration registration) {
        return saveEntity(registration);
    }

    // hủy đăng ký môn (xóa khỏi db)
    @Override
    public void delete(CourseRegistration registration) {
        if (registration == null) {
            return;
        }
        deleteEntityById(registration.getId());
    }

    @Override
    public Optional<CourseRegistration> findById(Long id) {
        return Optional.ofNullable(findEntityById(id));
    }

    // lấy hết các môn sv này đã đăng ký, kèm theo điểm nếu có
    @Override
    public List<CourseRegistration> findByStudentId(Long studentId) {
        return executeRead(em -> em.createQuery(
                        "SELECT cr FROM CourseRegistration cr JOIN FETCH cr.courseSection cs JOIN FETCH cs.course LEFT JOIN FETCH cs.lecturer LEFT JOIN FETCH cr.mark WHERE cr.student.id = :studentId",
                        CourseRegistration.class
                )
                .setParameter("studentId", studentId)
                .getResultList());
    }

    // lấy danh sách sv đăng ký 1 lớp học phần nào đó
    @Override
    public List<CourseRegistration> findByCourseSectionId(Long sectionId) {
        return executeRead(em -> em.createQuery(
                        "SELECT cr FROM CourseRegistration cr JOIN FETCH cr.student LEFT JOIN FETCH cr.mark WHERE cr.courseSection.id = :sectionId",
                        CourseRegistration.class
                )
                .setParameter("sectionId", sectionId)
                .getResultList());
    }

    // tìm 1 dòng đăng ký cụ thể của sv trong 1 lớp
    @Override
    public Optional<CourseRegistration> findByStudentAndSection(Long studentId, Long sectionId) {
        return executeRead(em -> em.createQuery(
                        "SELECT cr FROM CourseRegistration cr LEFT JOIN FETCH cr.mark WHERE cr.student.id = :studentId AND cr.courseSection.id = :sectionId",
                        CourseRegistration.class)
                .setParameter("studentId", studentId)
                .setParameter("sectionId", sectionId)
                .getResultStream()
                .findFirst());
    }

    @Override
    public void deleteByStudentAndSection(Long studentId, Long sectionId) {
        executeWrite(em -> {
            em.createQuery("DELETE FROM CourseRegistration cr WHERE cr.student.id = :studentId AND cr.courseSection.id = :sectionId")
                    .setParameter("studentId", studentId)
                    .setParameter("sectionId", sectionId)
                    .executeUpdate();
            return null;
        });
    }

    // check xem sv này đã đk lớp này chưa
    @Override
    public boolean isRegistered(Long studentId, Long sectionId) {
        return executeRead(em -> {
            Long count = em.createQuery(
                            "SELECT COUNT(cr) FROM CourseRegistration cr WHERE cr.student.id = :studentId AND cr.courseSection.id = :sectionId",
                            Long.class)
                    .setParameter("studentId", studentId)
                    .setParameter("sectionId", sectionId)
                    .getSingleResult();
            return count > 0;
        });
    }
}