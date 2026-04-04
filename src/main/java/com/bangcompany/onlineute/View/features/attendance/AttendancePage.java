package com.bangcompany.onlineute.View.features.attendance;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendancePage extends JPanel implements Refreshable {
    private final DefaultTableModel tableModel;

    public AttendancePage() {
        setLayout(new BorderLayout());

        String[] cols = new String[18];
        cols[0] = "STT";
        cols[1] = "Ma mon hoc";
        cols[2] = "Ten mon hoc";
        for (int i = 1; i <= 15; i++) {
            cols[i + 2] = "B " + i;
        }

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex >= 3 && columnIndex <= 17) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        TableStyles.applyModernTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableStyles.centerColumns(table, 0, 1);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        for (int i = 3; i <= 17; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(45);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        PageScaffold scaffold = new PageScaffold("Bang chuyen can");
        scaffold.setBody(scrollPane);
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return;
        }

        tableModel.setRowCount(0);
        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsByStudent(student.getId());

        int stt = 1;
        for (CourseRegistration registration : registrations) {
            Object[] row = new Object[18];
            row[0] = String.valueOf(stt++);
            row[1] = registration.getCourseSection() != null && registration.getCourseSection().getCourse() != null
                    ? "M" + registration.getCourseSection().getCourse().getId() : "";
            row[2] = registration.getCourseSection() != null && registration.getCourseSection().getCourse() != null
                    ? registration.getCourseSection().getCourse().getFullName() : "";

            Mark mark = registration.getMark();
            String attendance = (mark != null && mark.getAttendance() != null) ? mark.getAttendance() : "000000000000000";
            while (attendance.length() < 15) {
                attendance += "0";
            }

            for (int i = 0; i < 15; i++) {
                row[i + 3] = attendance.charAt(i) == '1';
            }
            tableModel.addRow(row);
        }
    }
}

