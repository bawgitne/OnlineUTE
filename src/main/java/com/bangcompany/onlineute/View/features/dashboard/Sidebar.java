package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.leftbar.NavMenu;
import com.bangcompany.onlineute.View.Components.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.Components.leftbar.UserProfileCard;
import com.bangcompany.onlineute.View.navigation.MainNavigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
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

        navMenu = new NavMenu(menuItems, onNavigate);

        JScrollPane menuScrollPane = new JScrollPane(navMenu);
        menuScrollPane.setBorder(null);
        menuScrollPane.setOpaque(false);
        menuScrollPane.getViewport().setOpaque(false);
        menuScrollPane.getViewport().setBackground(new Color(0, 85, 141));
        menuScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane.setWheelScrollingEnabled(true);

        JScrollBar verticalBar = menuScrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(20);
        verticalBar.setPreferredSize(new Dimension(0, 0));
        verticalBar.setOpaque(false);
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        add(menuScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 85, 141));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton logoutButton = new JButton("ĐĂNG XUẤT");
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
