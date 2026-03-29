package com.bangcompany.onlineute.View.Screens;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.View.Containers.LeftBar;
import com.bangcompany.onlineute.View.Containers.MainView;
import com.bangcompany.onlineute.View.Containers.TopHeader;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardFrame - Minimal header, no breadcrumb.
 */
public class DashboardFrame extends JFrame {
    private MainView mainView;
    private TopHeader topHeader;
    private LeftBar leftBar;

    public DashboardFrame() {
        setTitle("Hệ thống thông tin sinh viên - HCMUTE");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Get Dynamic Data from Session
        String name = SessionManager.getProfileFullName();
        String code = SessionManager.getProfileCode();
        Account currentAcc = SessionManager.getCurrentAccount();

        // 1. Initialize Components
        mainView = new MainView();
        topHeader = new TopHeader("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT TP.HCM");

        // 2. Sidebar with Logic (Sync with your previous edit removing 'status')
        leftBar = new LeftBar(
            name, 
            code, 
            (currentAcc != null) ? currentAcc.getRole() : null, 
            pageKey -> mainView.showPage(pageKey) // No breadcrumb update
        );

        add(leftBar, BorderLayout.WEST);

        // 3. Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(topHeader, BorderLayout.NORTH);
        mainArea.add(mainView, BorderLayout.CENTER);

        add(mainArea, BorderLayout.CENTER);

        // 4. Set Default Page
        showDefaultPage();
    }

    private void showDefaultPage() {
        String defaultPageKey = MenuItem.ANNOUNCEMENT.name();
        mainView.showPage(defaultPageKey);
        leftBar.setActiveTab(defaultPageKey);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DashboardFrame().setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
