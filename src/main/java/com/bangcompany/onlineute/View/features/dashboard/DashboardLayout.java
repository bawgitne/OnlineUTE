/**
 * giao diện chính sau khi login
 */
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.features.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.features.account.ChangePasswordPage;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.announcement.AnnouncementPage;
import com.bangcompany.onlineute.View.features.announcement.CreateAnnouncementPage;
import com.bangcompany.onlineute.View.features.attendance.AttendancePage;
import com.bangcompany.onlineute.View.features.data.DataManagementPage;
import com.bangcompany.onlineute.View.features.grade.InputGradesPage;
import com.bangcompany.onlineute.View.features.grade.ViewGradesPage;
import com.bangcompany.onlineute.View.features.profile.ProfilePage;
import com.bangcompany.onlineute.View.features.registration.CourseRegistrationPage;
import com.bangcompany.onlineute.View.features.registration.CourseManagementPage;
import com.bangcompany.onlineute.View.features.registration.CreateRegistrationBatchPage;
import com.bangcompany.onlineute.View.features.schedule.SchedulePage;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardLayout extends JPanel {
    private final MainContent mainContent = new MainContent();
    private final List<SidebarItem> tabs = new ArrayList<>();
    private final Sidebar sidebar;
    private final ViewContext viewContext;

    // khởi tạo menu theo quyền
    public DashboardLayout(ViewContext viewContext) {
        setLayout(new BorderLayout());

        String userName = SessionManager.getProfileFullName();
        String userCode = SessionManager.getProfileCode();
        String roleDisplayName = SessionManager.getRoleDisplayName();

        this.viewContext = viewContext;

        buildTabs(SessionManager.getRole());
        registerPages();

        sidebar = new Sidebar(userName, userCode, roleDisplayName, tabs, this::showPage);

        add(sidebar, BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);

        showFirstTab();
    }

    // tạo vùng nội dung bên phải
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

    // check role để nạp tab
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

    // menu cho admin
    private void buildAdminTabs() {
        addTitle("TRANG CÁ NHÂN");
        addTab("PROFILE", "Thông tin cá nhân", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "Thông báo", "trangCuaBan.png");
        addTitle("QUẢN LÝ");
        addTab("MANAGE_DATA", "Quản lý dữ liệu", "trangCuaBan.png");
        addTab("CREATE_REGISTRATION_BATCH", "Quản lý đăng ký môn", "chuongTrinhDaoTao.png");
        addTab("COMPOSE_ANNOUNCEMENT", "Gửi thông báo", "trangCuaBan.png");
        addTab("CHANGE_PASSWORD", "Đổi mật khẩu", "thongTinCaNhan.png");
    }

    // menu cho giảng viên
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

    // menu cho sinh viên
    private void buildStudentTabs() {
        addTitle("TRANG CÁ NHÂN");
        addTab("PROFILE", "Thông tin cá nhân", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "Thông báo", "trangCuaBan.png");
        addTitle("TRA CỨU THÔNG TIN");
        addTab("REGISTER_COURSES", "Đăng ký môn học", "chuongTrinhDaoTao.png");
        addTab("MANAGE_COURSES", "Quản lý môn học", "chuongTrinhDaoTao.png");
        addTab("MY_SCHEDULE", "Thời khóa biểu", "lich.png");
        addTab("MY_GRADES", "Xem điểm", "xemDiem.png");
        addTab("ATTENDANCE", "Xem điểm chuyên cần", "lich.png");
        addTab("CHANGE_PASSWORD", "Đổi mật khẩu", "thongTinCaNhan.png");
    }

    // Thêm một tiêu đề mục vào menu trái
    private void addTitle(String label) {
        tabs.add(SidebarItem.title(label));
    }

    // Thêm một mục (tab) có thể click vào menu trái
    private void addTab(String key, String label, String icon) {
        tabs.add(SidebarItem.tab(key, label, icon));
    }

    // Đăng ký tất cả các trang vào Container để có thể switch qua lại
    private void registerPages() {
        for (SidebarItem item : tabs) {
            if (item.isTitle()) {
                continue;
            }
            mainContent.registerPage(item.getKey(), createPage(item.getKey()));
        }
    }

    // gọi trang theo key
    private JPanel createPage(String pageKey) {
        return switch (pageKey) {
            case "ANNOUNCEMENT" -> new AnnouncementPage(viewContext);
            case "COMPOSE_ANNOUNCEMENT" -> new CreateAnnouncementPage(viewContext);
            case "CREATE_ACCOUNTS" -> new CreateAccountPage(viewContext);
            case "CREATE_REGISTRATION_BATCH" -> new CreateRegistrationBatchPage(viewContext);
            case "REGISTER_COURSES" -> new CourseRegistrationPage(viewContext);
            case "MANAGE_COURSES" -> new CourseManagementPage(viewContext);
            case "MANAGE_STUDENT" -> new DataManagementPage(viewContext);
            case "MANAGE_LECTURER" -> new DataManagementPage(viewContext);
            case "MANAGE_DATA" -> new DataManagementPage(viewContext);
            case "CHANGE_PASSWORD" -> new ChangePasswordPage(viewContext);
            case "PROFILE" -> new ProfilePage(viewContext);
            case "MY_SCHEDULE" -> new SchedulePage(viewContext);
            case "INPUT_GRADES" -> new InputGradesPage(viewContext);
            case "MY_GRADES" -> new ViewGradesPage(viewContext);
            case "ATTENDANCE" -> new AttendancePage(viewContext);
            default -> createPlaceholder(pageKey);
        };
    }

    // panel hiện chữ cho mấy trang chưa code
    private JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // hiện trang đầu khi mới vào
    private void showFirstTab() {
        for (SidebarItem item : tabs) {
            if (!item.isTitle()) {
                showPage(item.getKey());
                return;
            }
        }
    }

    // nhảy trang mới
    private void showPage(String pageKey) {
        mainContent.showPage(pageKey);
        sidebar.setActiveTab(pageKey);
    }
}
