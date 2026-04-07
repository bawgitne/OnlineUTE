/**
 * Thanh sidebar bên trái chứa menu điều hướng và thông tin admin/sinh viên
 */
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.features.leftbar.NavMenu;
import com.bangcompany.onlineute.View.features.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.features.leftbar.UserProfileCard;
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
        setBackground(AppTheme.PRIMARY_BLUE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        UserProfileCard profileCard = new UserProfileCard(userName, userId, roleDisplayName);
        add(profileCard, BorderLayout.NORTH);

        navMenu = new NavMenu(menuItems, onNavigate);

        JScrollPane menuScrollPane = new JScrollPane(navMenu);
        menuScrollPane.setBorder(null);
        menuScrollPane.setOpaque(false);
        menuScrollPane.getViewport().setOpaque(false);
        menuScrollPane.getViewport().setBackground(AppTheme.PRIMARY_BLUE);
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
        bottomPanel.setBackground(AppTheme.PRIMARY_BLUE);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        Button logoutButton = new Button("ĐĂNG XUẤT", new Color(220, 53, 69), Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        logoutButton.setPreferredSize(new Dimension(0, 44));
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
