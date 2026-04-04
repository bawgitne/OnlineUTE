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
        headerWrapper.add(new TopHeader("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT TP.HCM"), BorderLayout.NORTH);

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
        addTitle("TRANG CÁ NHÂN");
        addTab("PROFILE", "Thông tin cá nhân", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "Thông báo", "trangCuaBan.png");
        addTitle("QUẢN LÝ");
        addTab("MANAGE_DATA", "Quản lý dữ liệu", "trangCuaBan.png");
        addTab("CREATE_REGISTRATION_BATCH", "Tạo đợt đăng ký môn", "chuongTrinhDaoTao.png");
        addTab("COMPOSE_ANNOUNCEMENT", "Gửi thông báo", "trangCuaBan.png");
        addTab("CHANGE_PASSWORD", "Đổi mật khẩu", "thongTinCaNhan.png");
    }

    private void buildLecturerTabs() {
        addTitle("TRANG CÁ NHÂN");
        addTab("PROFILE", "Thông tin cá nhân", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "Thông báo", "trangCuaBan.png");
        addTitle("TRA CỨU THÔNG TIN");
        addTab("MY_SCHEDULE", "Lịch giảng dạy", "lich.png");
        addTitle("QUẢN LÝ SINH VIÊN");
        addTab("COMPOSE_ANNOUNCEMENT", "Gửi thông báo", "trangCuaBan.png");
        addTab("INPUT_GRADES", "Quản lý lớp học", "xemDiem.png");
        addTab("CHANGE_PASSWORD", "Đổi mật khẩu", "thongTinCaNhan.png");
    }

    private void buildStudentTabs() {
        addTitle("TRANG CÁ NHÂN");
        addTab("PROFILE", "Thông tin cá nhân", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "Thông báo", "trangCuaBan.png");
        addTitle("TRA CỨU THÔNG TIN");
        addTab("REGISTER_COURSES", "Đăng ký môn học", "chuongTrinhDaoTao.png");
        addTab("MY_SCHEDULE", "Thời khóa biểu", "lich.png");
        addTab("MY_GRADES", "Xem điểm", "xemDiem.png");
        addTab("ATTENDANCE", "Xem điểm chuyên cần", "lich.png");
        addTab("CHANGE_PASSWORD", "Đổi mật khẩu", "thongTinCaNhan.png");
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
