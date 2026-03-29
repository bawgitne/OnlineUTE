package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.ScheduleDAO;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.Service.ScheduleService;
import java.util.List;

public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleDAO scheduleDAO;

    public ScheduleServiceImpl(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public List<Schedule> getStudentSchedule(Long studentId) {
        return scheduleDAO.findByStudentId(studentId);
    }
}
