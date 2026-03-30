package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    Schedule saveSchedule(Schedule schedule);
    void regenerateSectionSchedules(Long courseSectionId, List<Schedule> schedules);
    List<Schedule> getSectionSchedules(Long courseSectionId);
    List<Schedule> getStudentSchedule(Long studentId);
    List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd);
}
