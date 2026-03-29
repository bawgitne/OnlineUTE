package com.bangcompany.onlineute.view.layout;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.EnumType.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardLayout extends JPanel {
    private final MainContent mainContent;
    private final Sidebar sidebar;

    public DashboardLayout() {
        setLayout(new BorderLayout());

        String name = SessionManager.getProfileFullName();
        String code = SessionManager.getProfileCode();
        String roleDisplay = SessionManager.getRoleDisplayName();
        List<MenuItem> accessibleItems = getAccessibleMenuItems();

        mainContent = new MainContent();
        TopHeader topHeader = new TopHeader("TRUONG DAI HOC CONG NGHE KY THUAT TP.HCM");
        sidebar = new Sidebar(name, code, roleDisplay, accessibleItems, pageKey -> mainContent.showPage(pageKey));

        add(sidebar, BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(topHeader, BorderLayout.NORTH);
        mainArea.add(mainContent, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        showDefaultPage();
    }

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
        mainContent.showPage(defaultPageKey);
        sidebar.setActiveTab(defaultPageKey);
    }
}
