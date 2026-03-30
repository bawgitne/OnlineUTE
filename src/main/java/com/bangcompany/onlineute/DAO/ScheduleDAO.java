package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDAO {
    Schedule save(Schedule schedule);
    void deleteByCourseSectionId(Long courseSectionId);
    List<Schedule> findByCourseSectionId(Long courseSectionId);
    List<Schedule> findByStudentId(Long studentId);
    List<Schedule> findByStudentIdAndDateRange(Long studentId, LocalDate startDate, LocalDate endDate);
}
