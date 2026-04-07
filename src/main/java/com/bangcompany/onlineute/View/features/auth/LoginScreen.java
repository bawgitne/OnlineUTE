package com.bangcompany.onlineute.View.features.auth;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.navigation.MainNavigator;
import com.bangcompany.onlineute.View.Components.theme.ImageUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình đăng nhập chính của ứng dụng.
 */
public class LoginScreen extends JPanel {
    public LoginScreen() {
        setLayout(new GridBagLayout());     // tự động nằm giữa màn hình
        setBackground(new Color(230, 235, 240));

        JPanel cardHolder = new JPanel();   // container chứa nội dung đăng nhập
        cardHolder.setLayout(new BoxLayout(cardHolder, BoxLayout.Y_AXIS));// xếp theo chiều dọc
        cardHolder.setOpaque(false); // trong suốt background
        add(cardHolder);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = ImageUtil.createImageLabel("public/ute_logo.png", 120, 120, null);

        JLabel uniName1 = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT");
        uniName1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName1.setForeground(new Color(0, 40, 80));
        uniName1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uniName2 = new JLabel("TP.HCM");
        uniName2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName2.setForeground(new Color(0, 40, 80));
        uniName2.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(uniName1);
        logoPanel.add(uniName2);

        cardHolder.add(logoPanel);
        cardHolder.add(Box.createRigidArea(new Dimension(0, 30)));

        LoginForm loginForm = new LoginForm(this::attemptLogin);
        loginForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardHolder.add(loginForm);
    }

    private void attemptLogin(String user, String pass) {
        
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AppContext.authController.Login(user, pass).ifPresentOrElse(
                account -> MainNavigator.showDashboard(),
                () -> JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE)
        );
    }
}
