package com.bangcompany.onlineute.View.Screens;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.View.Containers.Sidebar;
import com.bangcompany.onlineute.View.Containers.MainView;
import com.bangcompany.onlineute.View.Containers.TopHeader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DashboardFrame - Smart Container.
 * Handles business logic: role filtering, data preparation, navigation.
 * Passes pre-processed data down to dumb components (Sidebar, MainView, TopHeader).
 */
public class DashboardFrame extends JPanel {
    private MainView mainView;
    private TopHeader topHeader;
    private Sidebar sidebar;

    public DashboardFrame() {
        setLayout(new BorderLayout());

        // --- Smart Logic: Prepare data from Session ---
        String name = SessionManager.getProfileFullName();
        String code = SessionManager.getProfileCode();

        // Business logic: determine role display name
        String roleDisplay = SessionManager.getRoleDisplayName();

        // Business logic: filter menu items by role
        List<MenuItem> accessibleItems = getAccessibleMenuItems();

        // --- Assemble dumb components with pre-processed data ---
        mainView = new MainView();
        topHeader = new TopHeader("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT TP.HCM");

        sidebar = new Sidebar(
            name,
            code,
            roleDisplay,
            accessibleItems,
            pageKey -> mainView.showPage(pageKey)
        );

        add(sidebar, BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(topHeader, BorderLayout.NORTH);
        mainArea.add(mainView, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        // Default page
        showDefaultPage();
    }

    /**
     * Business logic: Filter MenuItem enum by the current user's role.
     */
    private List<MenuItem> getAccessibleMenuItems() {
        String roleName = SessionManager.getRole();
        if (roleName == null) roleName = "NONE";

        List<MenuItem> items = new ArrayList<>();
        for (MenuItem item : MenuItem.values()) {
            if (item.isAccessibleBy(roleName)) {
                items.add(item);
            }
        }
        return items;
    }

    private void showDefaultPage() {
        String defaultPageKey = MenuItem.ANNOUNCEMENT.name();
        mainView.showPage(defaultPageKey);
        sidebar.setActiveTab(defaultPageKey);
    }
}
