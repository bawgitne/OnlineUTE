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
                em.persist(schedule);
            } else {
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
}
