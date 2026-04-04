package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnnouncementTable extends JPanel {
    private static final String[] COLUMN_NAMES = {"Tiêu đề", "Người gửi", "Thời gian gửi"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final DefaultTableModel model;

    public AnnouncementTable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        model = new DefaultTableModel(COLUMN_NAMES, 0);
        loadAnnouncements();

        JTable table = new JTable(model);
        TableStyles.applyModernTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        model.setRowCount(0);

        if (AppContext.getNotificationController() == null) {
            model.addRow(new Object[]{"Không có thông báo mới", "", ""});
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
            model.addRow(new Object[]{"Không có thông báo mới", "", ""});
        }
    }
}
