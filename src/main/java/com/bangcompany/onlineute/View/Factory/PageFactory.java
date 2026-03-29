package com.bangcompany.onlineute.View.Factory;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.View.Pages.*;
import javax.swing.*;

/**
 * PageFactory - Responsible for creating page instances based on a MenuItem key.
 */
public class PageFactory {
    public static JPanel create(String pageKey) {
        // Find if it's a valid MenuItem name
        MenuItem item;
        try {
            item = MenuItem.valueOf(pageKey);
        } catch (IllegalArgumentException e) {
            return createPlaceholder("Trang không tồn tại: " + pageKey);
        }

        try {
            return switch (item) {
                case ANNOUNCEMENT         -> new AnnouncementPage();
                case COMPOSE_ANNOUNCEMENT -> new CreateAnnouncementPage();
                case PROFILE              -> new ProfilePage();
                case MY_SCHEDULE          -> new SchedulePage();
                case INPUT_GRADES         -> new InputGradesPage();
                // Add more cases mapping MenuItem to specific Page classes
                default                   -> createPlaceholder(item.getLabel());
            };
        } catch (Exception e) {
            e.printStackTrace();
            return createPlaceholder("Lỗi tải trang: " + e.getMessage());
        }
    }

    private static JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        JLabel label = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        panel.add(label, java.awt.BorderLayout.CENTER);
        return panel;
    }
}
