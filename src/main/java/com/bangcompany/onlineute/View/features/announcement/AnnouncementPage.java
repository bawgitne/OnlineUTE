/**
 * Xem các thông báo
 */
package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementPage extends JPanel implements Refreshable {
    private static final String[] COLUMN_NAMES = {"Tiêu đề", "Người gửi", "Thời gian gửi"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int PAGE_SIZE = 10;

    private final Table table;

    private List<Announcement> cached = new ArrayList<>();
    private int currentPage = 1;

    public AnnouncementPage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        // bảng list thông báo
        table = new Table(COLUMN_NAMES, 12, 44);

        Card titleCard = Card.titleCard("THÔNG BÁO");

        Card tableCard = new Card(22, new Insets(0, 0, 0, 0));
        tableCard.setLayout(new BorderLayout());
        tableCard.add(table, BorderLayout.CENTER);

        add(titleCard, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
    }

    // render lại
    @Override
    public void onEnter() {
        loadData();
        renderPage(1);
    }

    // kéo data từ sv về
    private void loadData() {
        if (AppContext.getNotificationController() == null) {
            cached = new ArrayList<>();
            return;
        }
        List<Announcement> list = AppContext.getNotificationController().getAnnouncementsForCurrentUser();
        cached = list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    // phân trang và hiện lên table
    private void renderPage(int page) {
        int totalPages = Math.max(1, (int) Math.ceil(cached.size() / (double) PAGE_SIZE));
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        currentPage = page;

        table.clearRows();
        int start = (currentPage - 1) * PAGE_SIZE;
        for (int i = 0; i < PAGE_SIZE; i++) {
            int index = start + i;
            if (index < cached.size()) {
                Announcement a = cached.get(index);
                String title = a.getTitle();
                String sender = a.getSenderName();
                String time = a.getCreatedAt().format(DATE_TIME_FORMATTER);
                table.addRow(title, sender, time);
            } else {
                table.addRow("", "", "");
            }
        }

        table.setPageState(currentPage, totalPages);
    }
}
