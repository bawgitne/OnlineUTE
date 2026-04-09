package com.bangcompany.onlineute.Controller;

import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.Service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleService.getStudentScheduleByWeek(studentId, weekStart, weekEnd);
    }

    public List<Schedule> getLecturerScheduleByWeek(Long lecturerId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleService.getLecturerScheduleByWeek(lecturerId, weekStart, weekEnd);
    }
}