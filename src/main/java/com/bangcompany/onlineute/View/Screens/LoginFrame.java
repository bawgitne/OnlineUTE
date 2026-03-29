package com.bangcompany.onlineute.View.Screens;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.Components.AppLogoHeader;
import com.bangcompany.onlineute.View.Containers.LoginForm;
import com.bangcompany.onlineute.View.WindowManager;

import javax.swing.*;
import java.awt.*;


public class LoginFrame extends JPanel {

    public LoginFrame() {
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 235, 240));

        JPanel cardHolder = new JPanel();
        cardHolder.setLayout(new BoxLayout(cardHolder, BoxLayout.Y_AXIS));
        cardHolder.setOpaque(false);
        add(cardHolder);

        cardHolder.add(new AppLogoHeader());
        cardHolder.add(Box.createRigidArea(new Dimension(0, 30)));

        LoginForm loginForm = new LoginForm(this::attemptLogin);
        loginForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardHolder.add(loginForm);
    }

    private void attemptLogin(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Delegate to AuthController
        AppContext.authController.Login(user, pass).ifPresentOrElse(
            account -> {
                // Success: Navigate to Dashboard
                WindowManager.show(new DashboardFrame());
            },
            () -> {
                // Error: Show feedback popup
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        );
    }
}
