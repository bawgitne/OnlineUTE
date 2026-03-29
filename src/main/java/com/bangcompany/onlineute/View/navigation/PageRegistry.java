package com.bangcompany.onlineute.view.navigation;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.view.features.announcement.AnnouncementPage;
import com.bangcompany.onlineute.view.features.announcement.CreateAnnouncementPage;
import com.bangcompany.onlineute.view.features.profile.ProfilePage;
import com.bangcompany.onlineute.view.features.schedule.SchedulePage;

import javax.swing.*;
import java.awt.*;

public final class PageRegistry {
    private PageRegistry() {}

    public static JPanel create(String pageKey) {
        MenuItem item;
        try {
            item = MenuItem.valueOf(pageKey);
        } catch (IllegalArgumentException e) {
            return createPlaceholder("Trang khong ton tai: " + pageKey);
        }

        return switch (item) {
            case ANNOUNCEMENT -> new AnnouncementPage();
            case COMPOSE_ANNOUNCEMENT -> new CreateAnnouncementPage();
            case PROFILE -> new ProfilePage();
            case MY_SCHEDULE -> new SchedulePage();
            case INPUT_GRADES -> new com.bangcompany.onlineute.View.Pages.InputGradesPage();
            case MY_GRADES -> new com.bangcompany.onlineute.View.Pages.ViewGradesPage();
            case ATTENDANCE -> new com.bangcompany.onlineute.View.Pages.AttendancePage();
            default -> createPlaceholder(item.getLabel());
        };
    }

    private static JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
