package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.ScheduleDAO;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.Service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleDAO scheduleDAO;

    public ScheduleServiceImpl(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    // lưu thông tin một buổi học cụ thể
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleDAO.save(schedule);
    }

    // xóa lịch cũ và tạo lại toàn bộ lịch học cho một lớp học phần
    @Override
    public void regenerateSectionSchedules(Long courseSectionId, List<Schedule> schedules) {
        // Xóa tất cả các buổi học hiện có của lớp học phần này
        scheduleDAO.deleteByCourseSectionId(courseSectionId);
        // Lưu lại danh sách các buổi học mới
        for (Schedule schedule : schedules) {
            scheduleDAO.save(schedule);
        }
    }

    // lấy danh sách tất cả các buổi học của một lớp học phần
    @Override
    public List<Schedule> getSectionSchedules(Long courseSectionId) {
        return scheduleDAO.findByCourseSectionId(courseSectionId);
    }

    // lấy toàn bộ lịch học của một sinh viên
    @Override
    public List<Schedule> getStudentSchedule(Long studentId) {
        return scheduleDAO.findByStudentId(studentId);
    }

    // lấy lịch học của sinh viên trong một tuần cụ thể
    @Override
    public List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleDAO.findByStudentIdAndDateRange(studentId, weekStart, weekEnd);
    }

    // lấy lịch dạy của giảng viên trong một tuần cụ thể
    @Override
    public List<Schedule> getLecturerScheduleByWeek(Long lecturerId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleDAO.findByLecturerIdAndDateRange(lecturerId, weekStart, weekEnd);
    }
}
