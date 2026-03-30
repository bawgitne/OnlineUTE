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

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleDAO.save(schedule);
    }

    @Override
    public void regenerateSectionSchedules(Long courseSectionId, List<Schedule> schedules) {
        scheduleDAO.deleteByCourseSectionId(courseSectionId);
        for (Schedule schedule : schedules) {
            scheduleDAO.save(schedule);
        }
    }

    @Override
    public List<Schedule> getSectionSchedules(Long courseSectionId) {
        return scheduleDAO.findByCourseSectionId(courseSectionId);
    }

    @Override
    public List<Schedule> getStudentSchedule(Long studentId) {
        return scheduleDAO.findByStudentId(studentId);
    }

    @Override
    public List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleDAO.findByStudentIdAndDateRange(studentId, weekStart, weekEnd);
    }
}
