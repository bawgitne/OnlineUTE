package com.bangcompany.onlineute.View.Containers;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.View.Components.NavMenu;
import com.bangcompany.onlineute.View.Components.UserProfileCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Sidebar - Composition of UserProfileCard + NavMenu.
 * A thin wrapper that assembles reusable sub-components.
 */
public class Sidebar extends JPanel {

    private final NavMenu navMenu;

    public Sidebar(String userName, String userId, String roleDisplayName,
                   List<MenuItem> menuItems, Consumer<String> onNavigate) {

        setBackground(new Color(0, 85, 141));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Top: User Profile
        UserProfileCard profileCard = new UserProfileCard(userName, userId, roleDisplayName);
        add(profileCard, BorderLayout.NORTH);

        // Center: Navigation Menu
        navMenu = new NavMenu("MENU CHÍNH", menuItems, onNavigate);
        add(navMenu, BorderLayout.CENTER);
    }

    public void setActiveTab(String pageKey) {
        navMenu.setActiveTab(pageKey);
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() != null) {
            int parentWidth = getParent().getWidth();
            int calculatedWidth = (int) (parentWidth * 0.25);
            int maxWidth = 280;
            int minWidth = 150;
            return new Dimension(Math.min(maxWidth, Math.max(minWidth, calculatedWidth)), super.getPreferredSize().height);
        }
        return new Dimension(260, super.getPreferredSize().height);
    }
}
