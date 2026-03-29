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
        try {
            MenuItem item = MenuItem.valueOf(pageKey);
            return switch (item) {
                case ANNOUNCEMENT    -> new AnnouncementPage();
                case PROFILE         -> new ProfilePage();
                case MY_SCHEDULE     -> new SchedulePage();
                // Add more cases mapping MenuItem to specific Page classes
                default             -> createPlaceholder(item.getLabel());
            };
        } catch (IllegalArgumentException e) {
            return createPlaceholder("Trang không tồn tại: " + pageKey);
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
