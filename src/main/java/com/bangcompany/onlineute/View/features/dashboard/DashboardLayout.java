package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.features.account.ChangePasswordPage;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.announcement.AnnouncementPage;
import com.bangcompany.onlineute.View.features.announcement.CreateAnnouncementPage;
import com.bangcompany.onlineute.View.features.attendance.AttendancePage;
import com.bangcompany.onlineute.View.features.data.DataManagementPage;
import com.bangcompany.onlineute.View.features.grade.InputGradesPage;
import com.bangcompany.onlineute.View.features.grade.ViewGradesPage;
import com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage;
import com.bangcompany.onlineute.View.features.profile.ProfilePage;
import com.bangcompany.onlineute.View.features.registration.CourseRegistrationPage;
import com.bangcompany.onlineute.View.features.registration.CreateRegistrationBatchPage;
import com.bangcompany.onlineute.View.features.schedule.SchedulePage;
import com.bangcompany.onlineute.View.features.student.StudentManagementPage;

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
        mainArea.setBackground(new Color(245, 248, 252));

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);
        headerWrapper.add(new TopHeader("\u0054\u0052\u01af\u1edc\u004e\u0047\u0020\u0110\u1ea0\u0049\u0020\u0048\u1ecc\u0043\u0020\u0043\u00d4\u004e\u0047\u0020\u004e\u0047\u0048\u1ec6\u0020\u004b\u1ef8\u0020\u0054\u0048\u0055\u1eac\u0054\u0020\u0054\u0050\u002e\u0048\u0043\u004d"), BorderLayout.NORTH);

        mainArea.add(headerWrapper, BorderLayout.NORTH);
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
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0051\u0055\u1ea2\u004e\u0020\u004c\u00dd");
        addTab("MANAGE_DATA", "\u0051\u0075\u0061\u006e\u0020\u006c\u00fd\u0020\u0064\u1eef\u0020\u006c\u0069\u1ec7\u0075", "trangCuaBan.png");
        addTab("CREATE_REGISTRATION_BATCH", "\u0054\u1ea1\u006f\u0020\u0111\u1ee3\u0074\u0020\u0111\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u006d\u00f4\u006e", "chuongTrinhDaoTao.png");
        addTab("COMPOSE_ANNOUNCEMENT", "\u0047\u1eed\u0069\u0020\u0074\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void buildLecturerTabs() {
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0054\u0052\u0041\u0020\u0043\u1ee8\u0055\u0020\u0054\u0048\u00d4\u004e\u0047\u0020\u0054\u0049\u004e");
        addTab("MY_SCHEDULE", "\u004c\u1ecb\u0063\u0068\u0020\u0067\u0069\u1ea3\u006e\u0067\u0020\u0064\u1ea1\u0079", "lich.png");
        addTitle("\u0051\u0055\u1ea2\u004e\u0020\u004c\u00dd\u0020\u0053\u0049\u004e\u0048\u0020\u0056\u0049\u00ca\u004e");
        addTab("COMPOSE_ANNOUNCEMENT", "\u0047\u1eed\u0069\u0020\u0074\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTab("INPUT_GRADES", "\u0051\u0075\u1ea3\u006e\u0020\u006c\u00fd\u0020\u006c\u1edb\u0070\u0020\u0068\u1ecd\u0063", "xemDiem.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void buildStudentTabs() {
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0054\u0052\u0041\u0020\u0043\u1ee8\u0055\u0020\u0054\u0048\u00d4\u004e\u0047\u0020\u0054\u0049\u004e");
        addTab("REGISTER_COURSES", "\u0110\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u006d\u00f4\u006e\u0020\u0068\u1ecd\u0063", "chuongTrinhDaoTao.png");
        addTab("MY_SCHEDULE", "\u0054\u0068\u1eddi\u0020\u006b\u0068\u00f3\u0061\u0020\u0062\u0069\u1ec3\u0075", "lich.png");
        addTab("MY_GRADES", "\u0058\u0065\u006d\u0020\u0111\u0069\u1ec3\u006d", "xemDiem.png");
        addTab("ATTENDANCE", "\u0058\u0065\u006d\u0020\u0111\u0069\u1ec3\u006d\u0020\u0063\u0068\u0075\u0079\u00ea\u006e\u0020\u0063\u1ea7\u006e", "lich.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void addTitle(String label) {
        tabs.add(SidebarItem.title(label));
    }

    private void addTab(String key, String label, String icon) {
        tabs.add(SidebarItem.tab(key, label, icon));
    }

    private void registerPages() {
        for (SidebarItem item : tabs) {
            if (item.isTitle()) {
                continue;
            }
            mainContent.registerPage(item.getKey(), createPage(item.getKey()));
        }
    }

    private JPanel createPage(String pageKey) {
        return switch (pageKey) {
            case "ANNOUNCEMENT" -> new AnnouncementPage();
            case "COMPOSE_ANNOUNCEMENT" -> new CreateAnnouncementPage();
            case "CREATE_ACCOUNTS" -> new CreateAccountPage();
            case "CREATE_REGISTRATION_BATCH" -> new CreateRegistrationBatchPage();
            case "REGISTER_COURSES" -> new CourseRegistrationPage();
            case "MANAGE_STUDENT" -> new StudentManagementPage();
            case "MANAGE_LECTURER" -> new LecturerManagementPage();
            case "MANAGE_DATA" -> new DataManagementPage();
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
        for (SidebarItem item : tabs) {
            if (!item.isTitle()) {
                showPage(item.getKey());
                return;
            }
        }
    }

    private void showPage(String pageKey) {
        mainContent.showPage(pageKey);
        sidebar.setActiveTab(pageKey);
    }
}
