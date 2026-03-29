package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Schedule;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getStudentSchedule(Long studentId);
}
