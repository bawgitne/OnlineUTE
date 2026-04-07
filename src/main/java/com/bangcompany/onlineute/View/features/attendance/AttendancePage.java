/**
 * Xem điểm chuyên cần
 */
package com.bangcompany.onlineute.View.features.attendance;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AttendancePage extends JPanel implements Refreshable {
    private final Table table;

    public AttendancePage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        // tạo cột cho 15 tuần học
        String[] cols = new String[16];
        cols[0] = "Tên môn học";
        for (int i = 1; i <= 15; i++) {
            cols[i] = "" + i;
        }

        table = new Table(cols, 12, 44);

        add(Card.titleCard("BẢNG CHUYÊN CẦN"), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
    }

    // hhiện data lên
    @Override
    public void onEnter() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return;
        }

        table.clearRows();
        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsByStudent(student.getId());

        for (CourseRegistration registration : registrations) {
            Object[] row = new Object[16];
            row[0] = registration.getCourseSection() != null && registration.getCourseSection().getCourse() != null
                    ? registration.getCourseSection().getCourse().getFullName() : "";

            Mark mark = registration.getMark();
            // lưu 15 buổi luuuw bằng 0 1 cho gọn
            String attendance = (mark != null && mark.getAttendance() != null) ? mark.getAttendance() : "000000000000000";
            while (attendance.length() < 15) {
                attendance += "0";
            }

            for (int i = 0; i < 15; i++) {
                row[i + 1] = attendance.charAt(i) == '1' ? "x" : ""; // x là đi học
            }
            table.addRow(row);
        }
    }
}
