
package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.ScheduleDAO;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class ScheduleDAOImpl implements ScheduleDAO {
    @Override
    public Schedule save(Schedule schedule) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (schedule.getId() == null) {
                // Thêm mới buổi học
                em.persist(schedule);
            } else {
                // Cập nhật buổi học
                schedule = em.merge(schedule);
            }
            em.getTransaction().commit();
            return schedule;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteByCourseSectionId(Long courseSectionId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Xóa tất cả các buổi học thuộc một lớp học phần cụ thể
            em.createQuery("DELETE FROM Schedule s WHERE s.courseSection.id = :courseSectionId")
                    .setParameter("courseSectionId", courseSectionId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Schedule> findByCourseSectionId(Long courseSectionId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy danh sách các buổi học của một lớp học phần, sắp xếp theo tuần
            return em.createQuery(
                            "SELECT s FROM Schedule s WHERE s.courseSection.id = :courseSectionId ORDER BY s.weekNumber",
                            Schedule.class
                    )
                    .setParameter("courseSectionId", courseSectionId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Schedule> findByStudentId(Long studentId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy toàn bộ lịch học của một sinh viên dựa trên các môn đã đăng ký thành công
            return em.createQuery(
                            "SELECT s FROM Schedule s " +
                                    "JOIN FETCH s.courseSection cs " +
                                    "JOIN FETCH cs.course " +
                                    "JOIN FETCH cs.lecturer " +
                                    "JOIN cs.courseRegistrations cr " +
                                    "WHERE cr.student.id = :studentId " +
                                    "ORDER BY s.studyDate, s.startSlot",
                            Schedule.class
                    )
                    .setParameter("studentId", studentId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Schedule> findByStudentIdAndDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy lịch học của sinh viên trong một khoảng thời gian cụ thể (ví dụ trong 1 tuần)
            return em.createQuery(
                            "SELECT s FROM Schedule s " +
                                    "JOIN FETCH s.courseSection cs " +
                                    "JOIN FETCH cs.course " +
                                    "JOIN FETCH cs.lecturer " +
                                    "JOIN cs.courseRegistrations cr " +
                                    "WHERE cr.student.id = :studentId " +
                                    "AND s.studyDate BETWEEN :startDate AND :endDate " +
                                    "ORDER BY s.studyDate, s.startSlot",
                            Schedule.class
                    )
                    .setParameter("studentId", studentId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Schedule> findByLecturerIdAndDateRange(Long lecturerId, LocalDate startDate, LocalDate endDate) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Lấy lịch dạy của giảng viên trong một khoảng thời gian cụ thể
            return em.createQuery(
                            "SELECT s FROM Schedule s " +
                                    "JOIN FETCH s.courseSection cs " +
                                    "JOIN FETCH cs.course " +
                                    "JOIN FETCH cs.lecturer " +
                                    "WHERE cs.lecturer.id = :lecturerId " +
                                    "AND s.studyDate BETWEEN :startDate AND :endDate " +
                                    "ORDER BY s.studyDate, s.startSlot",
                            Schedule.class
                    )
                    .setParameter("lecturerId", lecturerId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }
}
