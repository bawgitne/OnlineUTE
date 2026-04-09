package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.Config.JpaUtil;
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

    // lưu buổi học lẻ vào db
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleDAO.save(schedule);
    }

    // xóa sạch lịch cũ của lớp rồi nạp lại list lịch mới (dùng khi đổi phòng/giờ)
    @Override
    public void regenerateSectionSchedules(Long courseSectionId, List<Schedule> schedules) {
        JpaUtil.doInTransaction(() -> {
            scheduleDAO.deleteByCourseSectionId(courseSectionId);
            for (Schedule schedule : schedules) {
                scheduleDAO.save(schedule);
            }
        });
    }

    // lấy các buổi học của 1 lớp học phần
    @Override
    public List<Schedule> getSectionSchedules(Long courseSectionId) {
        return scheduleDAO.findByCourseSectionId(courseSectionId);
    }

    // lấy hết lịch học của sv
    @Override
    public List<Schedule> getStudentSchedule(Long studentId) {
        return scheduleDAO.findByStudentId(studentId);
    }

    // lấy lịch học sv theo tuần để hiện lên bảng tkb
    @Override
    public List<Schedule> getStudentScheduleByWeek(Long studentId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleDAO.findByStudentIdAndDateRange(studentId, weekStart, weekEnd);
    }

    // lấy lịch dạy gv theo tuần
    @Override
    public List<Schedule> getLecturerScheduleByWeek(Long lecturerId, LocalDate weekStart, LocalDate weekEnd) {
        return scheduleDAO.findByLecturerIdAndDateRange(lecturerId, weekStart, weekEnd);
    }
}