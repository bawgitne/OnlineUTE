package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.View.Components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class InputGradesPage extends JPanel implements Refreshable {

    private JTabbedPane tabbedPane;
    private JPanel noDataPanel;

    public InputGradesPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel(" QUẢN LÝ SINH VIÊN - ĐIỂM DANH & NHẬP ĐIỂM ", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 85, 141));
        headerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(headerLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        noDataPanel = new JPanel(new BorderLayout());
        noDataPanel.setBackground(Color.WHITE);
        JLabel noDataLabel = new JLabel("Không có lớp học phần nào", SwingConstants.CENTER);
        noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        noDataLabel.setForeground(Color.GRAY);
        noDataPanel.add(noDataLabel, BorderLayout.CENTER);

        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        loadTabs();
    }

    private void loadTabs() {
        tabbedPane.removeAll();
        Lecturer currentLecturer = SessionManager.getCurrentLecturer();
        if (currentLecturer == null) {
            return;
        }

        List<CourseSection> mySections = AppContext.getCourseSectionService().getAllSections().stream()
                .filter(sec -> sec.getLecturer() != null && sec.getLecturer().getId().equals(currentLecturer.getId()))
                .collect(Collectors.toList());

        if (mySections.isEmpty()) {
            remove(tabbedPane);
            add(noDataPanel, BorderLayout.CENTER);
        } else {
            remove(noDataPanel);
            add(tabbedPane, BorderLayout.CENTER);
            for (CourseSection sec : mySections) {
                tabbedPane.addTab(sec.getCourse().getFullName() + " (Lớp: " + sec.getId() + ")", createSectionPanel(sec));
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createSectionPanel(CourseSection section) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = new String[21];
        columns[0] = "RegId";
        columns[1] = "Mã SV";
        columns[2] = "Họ tên";
        for (int i = 1; i <= 15; i++) columns[i + 2] = "T" + i;
        columns[18] = "Đ.Quá Trình";
        columns[19] = "Đ.Thi Cuối Kỳ";
        columns[20] = "Tổng Kết";

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 17) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3 && column <= 19;
            }
        };

        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsBySection(section.getId());
        
        for (CourseRegistration reg : registrations) {
            Object[] rowData = new Object[21];
            rowData[0] = reg.getId().toString();
            rowData[1] = reg.getStudent().getCode();
            rowData[2] = reg.getStudent().getFullName();

            Mark mark = reg.getMark();
            String attendance = mark != null && mark.getAttendance() != null ? mark.getAttendance() : "000000000000000";
            while (attendance.length() < 15) attendance += "0";

            for (int i = 0; i < 15; i++) {
                rowData[i + 3] = attendance.charAt(i) == '1';
            }

            rowData[18] = mark != null && mark.getProcessScore() != null ? mark.getProcessScore().toString() : "";
            rowData[19] = mark != null && mark.getTestScore() != null ? mark.getTestScore().toString() : "";
            rowData[20] = mark != null && mark.getFinalScore() != null ? mark.getFinalScore().toString() : "";

            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0)); 
        table.getColumnModel().getColumn(0).setPreferredWidth(100); 
        table.getColumnModel().getColumn(1).setPreferredWidth(150); 
        for (int i = 2; i <= 16; i++) table.getColumnModel().getColumn(i).setPreferredWidth(30); 
        table.getColumnModel().getColumn(17).setPreferredWidth(70); 
        table.getColumnModel().getColumn(18).setPreferredWidth(80); 
        table.getColumnModel().getColumn(19).setPreferredWidth(70); 

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(17).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(18).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(19).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        PrimaryButton btnSave = new PrimaryButton("Lưu bảng điểm");
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveGrades(table, model, registrations));
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void saveGrades(JTable table, DefaultTableModel model, List<CourseRegistration> registrations) {
        if (table.isEditing()) {
             table.getCellEditor().stopCellEditing();
        }

        try {
            for (int r = 0; r < model.getRowCount(); r++) {
                Long regId = Long.parseLong(model.getValueAt(r, 0).toString());
                CourseRegistration reg = registrations.stream().filter(x -> x.getId().equals(regId)).findFirst().orElse(null);
                if (reg == null) continue;

                Mark mark = reg.getMark();
                if (mark == null) {
                    mark = new Mark();
                    mark.setCourseRegistration(reg);
                }

                StringBuilder attendanceBuilder = new StringBuilder();
                for (int c = 3; c <= 17; c++) {
                    Boolean isPresent = (Boolean) model.getValueAt(r, c);
                    attendanceBuilder.append((isPresent != null && isPresent) ? "1" : "0");
                }
                mark.setAttendance(attendanceBuilder.toString());

                mark.setProcessScore(parseScore(model.getValueAt(r, 18)));
                mark.setTestScore(parseScore(model.getValueAt(r, 19)));
                
                AppContext.getMarkService().saveMark(mark);
            }
            JOptionPane.showMessageDialog(this, "Lưu thành công bảng điểm và điểm danh!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadTabs(); // Refresh data from backend
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Có ô điểm nhập sai định dạng số hoặc vượt quá 0-10. Vui lòng kiểm tra lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu bảng điểm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal parseScore(Object val) throws NumberFormatException {
        if (val == null) return null;
        String s = val.toString().trim();
        if (s.isEmpty()) return null;
        BigDecimal bd = new BigDecimal(s);
        if (bd.compareTo(BigDecimal.ZERO) < 0 || bd.compareTo(new BigDecimal("10")) > 0) {
            throw new NumberFormatException("Điểm phải nằm trong khoản 0-10");
        }
        return bd;
    }
}
