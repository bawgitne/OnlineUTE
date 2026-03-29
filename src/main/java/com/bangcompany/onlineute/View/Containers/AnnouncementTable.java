package com.bangcompany.onlineute.View.Containers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AnnouncementTable extends JPanel {
    public AnnouncementTable() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title and Section Header
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(" THÔNG BÁO ");
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 84, 140));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(8, 20, 8, 20));
        titlePanel.add(titleLabel);

        JLabel tinTucLabel = new JLabel(" TIN TỨC ");
        tinTucLabel.setForeground(new Color(0, 84, 140));
        tinTucLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tinTucLabel.setBorder(new EmptyBorder(8, 20, 8, 20));
        titlePanel.add(tinTucLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Actual Data Table
        String[] columnNames = {"Tiêu đề", "Người gửi", "Thời gian gửi"};
        Object[][] data = {
            {"Thông báo về việc công bố lịch thi học kỳ 2/2025-2026...", "PDT_Phạm Thị Thúy Hạnh", "24/03/2026 07:37:08"},
            {"Phòng Đào tạo thông báo kết quả xét tốt nghiệp...", "PDT_Bùi Thị Quỳnh", "18/03/2026 18:44:22"},
            {"Thông báo lịch thi tiếng Anh đầu ra đợt tháng 3/2026", "PDT_Phạm Thị Thúy Hạnh", "16/03/2026 16:30:31"}
        };
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        table.setRowHeight(40);
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);
    }
}
