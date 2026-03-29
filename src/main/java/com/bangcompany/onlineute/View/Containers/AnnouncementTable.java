package com.bangcompany.onlineute.View.Containers;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Announcement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnnouncementTable extends JPanel {
    private static final String[] COLUMN_NAMES = {"Tieu de", "Nguoi gui", "Thoi gian gui"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final DefaultTableModel model;

    public AnnouncementTable() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(" THONG BAO ");
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 84, 140));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(8, 20, 8, 20));
        titlePanel.add(titleLabel);

        JLabel newsLabel = new JLabel(" TIN TUC ");
        newsLabel.setForeground(new Color(0, 84, 140));
        newsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        newsLabel.setBorder(new EmptyBorder(8, 20, 8, 20));
        titlePanel.add(newsLabel);

        add(titlePanel, BorderLayout.NORTH);

        model = new DefaultTableModel(COLUMN_NAMES, 0);
        loadAnnouncements();

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        model.setRowCount(0);

        if (AppContext.getNotificationController() == null) {
            model.addRow(new Object[]{"Khong co thong bao moi", "", ""});
            return;
        }

        List<Announcement> announcements = AppContext.getNotificationController().getAnnouncementsForCurrentUser();
        for (Announcement announcement : announcements) {
            model.addRow(new Object[]{
                    announcement.getTitle(),
                    announcement.getSenderName(),
                    announcement.getCreatedAt().format(DATE_TIME_FORMATTER)
            });
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"Khong co thong bao moi", "", ""});
        }
    }
}
