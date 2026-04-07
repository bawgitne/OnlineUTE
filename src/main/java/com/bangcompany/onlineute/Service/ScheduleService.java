package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    // lưu buổi học
    Schedule saveSchedule(Schedule schedule);
    // cấp lại toàn bộ lịch cho một lớp học phần
    void regenerateSectionSchedules(Long courseSectionId, List<Schedule> schedules);
    // lấy danh sách lịch của một lớp học phần
    List<Schedule> getSectionSchedules(Long courseSectionId);
    // lấy toàn bộ lịch của sinh viên
    List<Schedule> getStudentSchedule(Long studentId);
    // lấy lịch theo tuần cho sinh viên
    List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd);
    // lấy lịch dạy theo tuần cho giảng viên
    List<Schedule> getLecturerScheduleByWeek(Long lecturerId, LocalDate weekStart, LocalDate weekEnd);
}
