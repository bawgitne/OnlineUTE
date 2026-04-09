package com.bangcompany.onlineute.DAO.Impl;

import com.bangcompany.onlineute.DAO.AbstractDAO;
import com.bangcompany.onlineute.DAO.ScheduleDAO;
import com.bangcompany.onlineute.Model.Entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public class ScheduleDAOImpl extends AbstractDAO<Schedule> implements ScheduleDAO {
    public ScheduleDAOImpl() {
        super(Schedule.class);
    }

    // lưu 1 buổi học vào db
    @Override
    public Schedule save(Schedule schedule) {
        return saveEntity(schedule);
    }

    // xóa sạch lịch cũ của 1 lớp để vẽ lại lịch mới
    @Override
    public void deleteByCourseSectionId(Long courseSectionId) {
        executeWrite(em -> {
            em.createQuery("DELETE FROM Schedule s WHERE s.courseSection.id = :courseSectionId")
                    .setParameter("courseSectionId", courseSectionId)
                    .executeUpdate();
            return null;
        });
    }

    // tìm xem lớp này có những buổi học nào
    @Override
    public List<Schedule> findByCourseSectionId(Long courseSectionId) {
        return executeRead(em -> em.createQuery(
                        "SELECT s FROM Schedule s WHERE s.courseSection.id = :courseSectionId ORDER BY s.weekNumber",
                        Schedule.class
                )
                .setParameter("courseSectionId", courseSectionId)
                .getResultList());
    }

    // lấy toàn bộ lịch học của sv (fetch luôn môn học cho đỡ query lẻ)
    @Override
    public List<Schedule> findByStudentId(Long studentId) {
        return executeRead(em -> em.createQuery(
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
                .getResultList());
    }

    // lấy lịch học của sv trong 1 khoảng thời gian (dùng cho xem tkb theo tuần)
    @Override
    public List<Schedule> findByStudentIdAndDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        return executeRead(em -> em.createQuery(
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
                .getResultList());
    }

    // lấy lịch dạy của gv theo tuần
    @Override
    public List<Schedule> findByLecturerIdAndDateRange(Long lecturerId, LocalDate startDate, LocalDate endDate) {
        return executeRead(em -> em.createQuery(
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
                .getResultList());
    }
}