package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendancePage extends JPanel implements Refreshable {
    private final DefaultTableModel tableModel;

    public AttendancePage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("  CHI TIẾT CHUYÊN CẦN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 85, 141));
        titleLabel.setPreferredSize(new Dimension(0, 40));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        String[] cols = new String[18];
        cols[0] = "STT";
        cols[1] = "Mã môn học";
        cols[2] = "Tên môn học";
        for (int i = 1; i <= 15; i++) {
            cols[i + 2] = "Buổi " + i;
        }

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 17) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Student can only view attendance, not edit
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRender);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        
        for (int i = 3; i <= 17; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(45);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        Student st = SessionManager.getCurrentStudent();
        if (st == null) return;
        tableModel.setRowCount(0);

        List<CourseRegistration> regs = AppContext.getCourseRegistrationService().getRegistrationsByStudent(st.getId());

        int stt = 1;
        for (CourseRegistration cr : regs) {
            Object[] row = new Object[18];
            row[0] = String.valueOf(stt++);
            row[1] = cr.getCourseSection() != null && cr.getCourseSection().getCourse() != null ? "M" + cr.getCourseSection().getCourse().getId() : "";
            row[2] = cr.getCourseSection() != null && cr.getCourseSection().getCourse() != null ? cr.getCourseSection().getCourse().getFullName() : "";

            Mark m = cr.getMark();
            String att = (m != null && m.getAttendance() != null) ? m.getAttendance() : "000000000000000";
            while (att.length() < 15) att += "0";

            for (int i = 0; i < 15; i++) {
                row[i + 3] = att.charAt(i) == '1';
            }
            tableModel.addRow(row);
        }
    }
}
