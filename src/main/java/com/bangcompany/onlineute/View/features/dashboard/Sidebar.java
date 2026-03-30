package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.NavMenu;
import com.bangcompany.onlineute.View.Components.SidebarItem;
import com.bangcompany.onlineute.View.Components.UserProfileCard;
import com.bangcompany.onlineute.View.navigation.MainNavigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private final NavMenu navMenu;

    public Sidebar(
            String userName,
            String userId,
            String roleDisplayName,
            List<SidebarItem> menuItems,
            Consumer<String> onNavigate
    ) {
        setBackground(new Color(0, 85, 141));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        UserProfileCard profileCard = new UserProfileCard(userName, userId, roleDisplayName);
        add(profileCard, BorderLayout.NORTH);

        navMenu = new NavMenu("MENU CHINH", menuItems, onNavigate);
        add(navMenu, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 85, 141));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton logoutButton = new JButton(" Dang xuat");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            SessionManager.logout();
            MainNavigator.showLogin();
        });

        bottomPanel.add(logoutButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setActiveTab(String pageKey) {
        navMenu.setActiveTab(pageKey);
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() == null) {
            return new Dimension(260, super.getPreferredSize().height);
        }

        int parentWidth = getParent().getWidth();
        int calculatedWidth = (int) (parentWidth * 0.25);
        int width = Math.min(280, Math.max(150, calculatedWidth));
        return new Dimension(width, super.getPreferredSize().height);
    }
}
