package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Schedule;
import java.util.List;

public interface ScheduleDAO {
    List<Schedule> findByStudentId(Long studentId);
}
