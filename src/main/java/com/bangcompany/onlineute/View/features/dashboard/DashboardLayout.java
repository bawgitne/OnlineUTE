package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.SidebarItem;
import com.bangcompany.onlineute.View.features.account.ChangePasswordPage;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.announcement.AnnouncementPage;
import com.bangcompany.onlineute.View.features.announcement.CreateAnnouncementPage;
import com.bangcompany.onlineute.View.features.attendance.AttendancePage;
import com.bangcompany.onlineute.View.features.grade.InputGradesPage;
import com.bangcompany.onlineute.View.features.grade.ViewGradesPage;
import com.bangcompany.onlineute.View.features.profile.ProfilePage;
import com.bangcompany.onlineute.View.features.schedule.SchedulePage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardLayout extends JPanel {
    private final MainContent mainContent = new MainContent();
    private final List<SidebarItem> tabs = new ArrayList<>();
    private final Sidebar sidebar;

    public DashboardLayout() {
        setLayout(new BorderLayout());

        String userName = SessionManager.getProfileFullName();
        String userCode = SessionManager.getProfileCode();
        String roleDisplayName = SessionManager.getRoleDisplayName();

        buildTabs(SessionManager.getRole());
        registerPages();

        sidebar = new Sidebar(userName, userCode, roleDisplayName, tabs, this::showPage);

        add(sidebar, BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);

        showFirstTab();
    }

    private JPanel createMainArea() {
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(new TopHeader("TRUONG DAI HOC CONG NGHE KY THUAT TP.HCM"), BorderLayout.NORTH);
        mainArea.add(mainContent, BorderLayout.CENTER);
        return mainArea;
    }

    private void buildTabs(String role) {
        tabs.clear();

        if ("ADMIN".equals(role)) {
            buildAdminTabs();
            return;
        }

        if ("LECTURER".equals(role)) {
            buildLecturerTabs();
            return;
        }

        buildStudentTabs();
    }

    private void buildAdminTabs() {
        addTab("ANNOUNCEMENT", "Thong bao", "thongTinCaNhan.png");
        addTab("COMPOSE_ANNOUNCEMENT", "Gui thong bao", "account.png");
        addTab("CREATE_ACCOUNTS", "Cap tai khoan moi", "account.png");
        addTab("PROFILE", "Ho so ca nhan", "thongTinCaNhan.png");
        addTab("CHANGE_PASSWORD", "Doi mat khau", "password.png");
    }

    private void buildLecturerTabs() {
        addTab("ANNOUNCEMENT", "Thong bao", "thongTinCaNhan.png");
        addTab("COMPOSE_ANNOUNCEMENT", "Gui thong bao", "account.png");
        addTab("INPUT_GRADES", "Quan ly sinh vien", "grade.png");
        addTab("MY_SCHEDULE", "Thoi khoa bieu", "lich.png");
        addTab("PROFILE", "Ho so ca nhan", "thongTinCaNhan.png");
        addTab("CHANGE_PASSWORD", "Doi mat khau", "password.png");
    }

    private void buildStudentTabs() {
        addTab("ANNOUNCEMENT", "Thong bao", "thongTinCaNhan.png");
        addTab("MY_SCHEDULE", "Thoi khoa bieu", "lich.png");
        addTab("MY_GRADES", "Xem diem", "grade.png");
        addTab("ATTENDANCE", "Chuyen can", "lich.png");
        addTab("PROFILE", "Ho so ca nhan", "thongTinCaNhan.png");
        addTab("CHANGE_PASSWORD", "Doi mat khau", "password.png");
    }

    private void addTab(String key, String label, String icon) {
        tabs.add(new SidebarItem(key, label, icon));
    }

    private void registerPages() {
        for (SidebarItem tab : tabs) {
            mainContent.registerPage(tab.getKey(), createPage(tab.getKey()));
        }
    }

    private JPanel createPage(String pageKey) {
        return switch (pageKey) {
            case "ANNOUNCEMENT" -> new AnnouncementPage();
            case "COMPOSE_ANNOUNCEMENT" -> new CreateAnnouncementPage();
            case "CREATE_ACCOUNTS" -> new CreateAccountPage();
            case "CHANGE_PASSWORD" -> new ChangePasswordPage();
            case "PROFILE" -> new ProfilePage();
            case "MY_SCHEDULE" -> new SchedulePage();
            case "INPUT_GRADES" -> new InputGradesPage();
            case "MY_GRADES" -> new ViewGradesPage();
            case "ATTENDANCE" -> new AttendancePage();
            default -> createPlaceholder(pageKey);
        };
    }

    private JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void showFirstTab() {
        if (tabs.isEmpty()) {
            return;
        }

        showPage(tabs.get(0).getKey());
    }

    private void showPage(String pageKey) {
        mainContent.showPage(pageKey);
        sidebar.setActiveTab(pageKey);
    }
}
