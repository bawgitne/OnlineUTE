package com.bangcompany.onlineute.View.Screens;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.SidebarItem;
import com.bangcompany.onlineute.View.Containers.Sidebar;
import com.bangcompany.onlineute.View.Containers.MainView;
import com.bangcompany.onlineute.View.Containers.TopHeader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


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
        List<SidebarItem> accessibleItems = getAccessibleMenuItems();

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
     * Legacy dashboard uses a minimal fixed sidebar list.
     */
    private List<SidebarItem> getAccessibleMenuItems() {
        List<SidebarItem> items = new ArrayList<>();
        items.add(new SidebarItem("ANNOUNCEMENT", "Thong bao", "thongTinCaNhan.png"));
        items.add(new SidebarItem("PROFILE", "Ho so ca nhan", "thongTinCaNhan.png"));
        items.add(new SidebarItem("CHANGE_PASSWORD", "Doi mat khau", "password.png"));
        return items;
    }

    private void showDefaultPage() {
        String defaultPageKey = "ANNOUNCEMENT";
        mainView.showPage(defaultPageKey);
        sidebar.setActiveTab(defaultPageKey);
    }
}
