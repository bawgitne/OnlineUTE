package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentManagementDashboard extends JPanel {
    private final JLabel totalStudentsValue = new JLabel("0");
    private final JLabel totalClassesValue = new JLabel("0");
    private final JLabel totalFacultiesValue = new JLabel("0");

    public StudentManagementDashboard() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.add(createSummaryCard("Tổng sinh viên", totalStudentsValue, new Color(0, 85, 141)));
        summaryPanel.add(createSummaryCard("Tổng lớp", totalClassesValue, new Color(0, 123, 167)));
        summaryPanel.add(createSummaryCard("Tổng khoa", totalFacultiesValue, new Color(19, 135, 84)));

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
        guidePanel.setBackground(Color.WHITE);
        guidePanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(222, 230, 239), 24, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Bắt đầu bằng ô tìm kiếm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 85, 141));

        JLabel descLabel = new JLabel("<html>Tìm nhanh theo <b>mã sinh viên</b>, <b>họ tên</b>, <b>email</b>, <b>lớp</b> hoặc <b>khoa</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị dashboard tổng quan.</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(70, 85, 100));

        guidePanel.add(titleLabel);
        guidePanel.add(Box.createVerticalStrut(12));
        guidePanel.add(descLabel);

        add(summaryPanel, BorderLayout.NORTH);
        add(guidePanel, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        totalStudentsValue.setText(String.valueOf(AppContext.getStudentController().countAllStudents()));
        totalClassesValue.setText(String.valueOf(AppContext.getClassService().getAllClasses().size()));
        totalFacultiesValue.setText(String.valueOf(AppContext.getFacultyService().getAllFaculties().size()));
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(222, 230, 239), 24, new Insets(18, 18, 18, 18)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(96, 110, 126));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);
        return card;
    }
}
