package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("  KẾT QUẢ HỌC TẬP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 85, 141));
        titleLabel.setPreferredSize(new Dimension(0, 40));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã môn học", "Tên môn học", "Số TC", "Điểm hệ 10", "Điểm hệ 4", "Điểm chữ", "Kết quả"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        int[] centerCols = {0, 1, 3, 4, 5, 6, 7};
        for (int c : centerCols) {
            table.getColumnModel().getColumn(c).setCellRenderer(centerRender);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);

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
        List<Course> allCourses = AppContext.getCourseService().getAllCourses(); // Assuming AppContext has access to CourseService
        if (allCourses == null) allCourses = new ArrayList<>(); // Fallback

        Set<Long> studiedCourseIds = new HashSet<>();

        // Section 1: Đã / Đang học
        addHeaderRow("--- CÁC MÔN ĐÃ VÀ ĐANG HỌC ---");
        int stt = 1;
        for (CourseRegistration cr : regs) {
            Course c = cr.getCourseSection() != null ? cr.getCourseSection().getCourse() : null;
            if (c != null) studiedCourseIds.add(c.getId());

            Mark m = cr.getMark();
            String diem10 = "", diem4 = "", diemChu = "", ketQua = "";
            if (m != null && m.getFinalScore() != null) {
                BigDecimal f = m.getFinalScore();
                diem10 = f.toString();
                if (f.compareTo(new BigDecimal("8.5")) >= 0) diem4 = "4.0";
                else if (f.compareTo(new BigDecimal("7.0")) >= 0) diem4 = "3.0";
                else if (f.compareTo(new BigDecimal("5.5")) >= 0) diem4 = "2.0";
                else if (f.compareTo(new BigDecimal("4.0")) >= 0) diem4 = "1.0";
                else diem4 = "0.0";
                
                diemChu = m.getGradeChar() != null ? m.getGradeChar() : "";
                ketQua = !diem4.equals("0.0") ? "Đạt" : "Rớt";
            }

            tableModel.addRow(new Object[]{
                stt++, 
                c != null ? "M" + c.getId() : "", 
                c != null ? c.getFullName() : "", 
                c != null ? c.getCredit() : "", 
                diem10, diem4, diemChu, ketQua
            });
        }

        // Section 2: Chưa học
        addHeaderRow("--- CÁC MÔN CHƯA HỌC (THAM KHẢO) ---");
        stt = 1;
        for (Course c : allCourses) {
            if (!studiedCourseIds.contains(c.getId())) {
                tableModel.addRow(new Object[]{
                    stt++, 
                    "M" + c.getId(), 
                    c.getFullName(), 
                    c.getCredit(), 
                    "", "", "", ""
                });
            }
        }
    }

    private void addHeaderRow(String text) {
        tableModel.addRow(new Object[]{"", "", text, "", "", "", "", ""});
    }
}
