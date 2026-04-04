package com.bangcompany.onlineute.View.features.grade;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewGradesPage extends JPanel implements Refreshable {
    private final DefaultTableModel tableModel;

    public ViewGradesPage() {
        setLayout(new BorderLayout());

        String[] cols = {"STT", "Ma mon hoc", "Ten mon hoc", "So TC", "Diem he 10", "Diem he 4", "Diem chu", "Ket qua"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        TableStyles.applyModernTable(table);
        TableStyles.centerColumns(table, 0, 1, 3, 4, 5, 6, 7);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        PageScaffold scaffold = new PageScaffold("Ket qua hoc tap");
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
        List<Course> allCourses = AppContext.getCourseService().getAllCourses();
        if (allCourses == null) {
            allCourses = new ArrayList<>();
        }

        Set<Long> studiedCourseIds = new HashSet<>();

        addHeaderRow("--- CAC MON DA VA DANG HOC ---");
        int stt = 1;
        for (CourseRegistration registration : registrations) {
            Course course = registration.getCourseSection() != null ? registration.getCourseSection().getCourse() : null;
            if (course != null) {
                studiedCourseIds.add(course.getId());
            }

            Mark mark = registration.getMark();
            String diem10 = "";
            String diem4 = "";
            String diemChu = "";
            String ketQua = "";

            if (mark != null && mark.getFinalScore() != null) {
                BigDecimal finalScore = mark.getFinalScore();
                diem10 = finalScore.toString();
                if (finalScore.compareTo(new BigDecimal("8.5")) >= 0) {
                    diem4 = "4.0";
                } else if (finalScore.compareTo(new BigDecimal("7.0")) >= 0) {
                    diem4 = "3.0";
                } else if (finalScore.compareTo(new BigDecimal("5.5")) >= 0) {
                    diem4 = "2.0";
                } else if (finalScore.compareTo(new BigDecimal("4.0")) >= 0) {
                    diem4 = "1.0";
                } else {
                    diem4 = "0.0";
                }

                diemChu = mark.getGradeChar() != null ? mark.getGradeChar() : "";
                ketQua = !"0.0".equals(diem4) ? "Dat" : "Rot";
            }

            tableModel.addRow(new Object[]{
                    stt++,
                    course != null ? "M" + course.getId() : "",
                    course != null ? course.getFullName() : "",
                    course != null ? course.getCredit() : "",
                    diem10, diem4, diemChu, ketQua
            });
        }

        addHeaderRow("--- CAC MON CHUA HOC (THAM KHAO) ---");
        stt = 1;
        for (Course course : allCourses) {
            if (!studiedCourseIds.contains(course.getId())) {
                tableModel.addRow(new Object[]{
                        stt++,
                        "M" + course.getId(),
                        course.getFullName(),
                        course.getCredit(),
                        "", "", "", ""
                });
            }
        }
    }

    private void addHeaderRow(String text) {
        tableModel.addRow(new Object[]{"", "", text, "", "", "", "", ""});
    }
}

