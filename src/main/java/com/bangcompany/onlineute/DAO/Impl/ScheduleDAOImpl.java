package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.DAO.ScheduleDAO;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ScheduleDAOImpl implements ScheduleDAO {
    
    @Override
    public List<Schedule> findByStudentId(Long studentId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Updated Query with JOIN FETCH to prevent LazyInitializationException in the UI.
            // We fetch the CourseSection, its Course, and its Lecturer in one go.
            return em.createQuery(
                "SELECT s FROM Schedule s " +
                "JOIN FETCH s.courseSection cs " +
                "JOIN FETCH cs.course c " +
                "JOIN FETCH cs.lecturer l " +
                "JOIN cs.courseRegistrations cr " +
                "WHERE cr.student.id = :studentId", Schedule.class)
                .setParameter("studentId", studentId)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }
}
