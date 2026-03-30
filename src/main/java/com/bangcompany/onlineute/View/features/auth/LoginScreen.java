package com.bangcompany.onlineute.View.features.auth;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.navigation.MainNavigator;
import com.bangcompany.onlineute.View.Components.AppLogoHeader;

import javax.swing.*;
import java.awt.*;

/**
 * màn hình đăng nhập
 */
public class LoginScreen extends JPanel {
    public LoginScreen() {
        setLayout(new GridBagLayout());     // tự đôộng nằm giữa screen
        setBackground(new Color(230, 235, 240));

        JPanel cardHolder = new JPanel();   //container đăng nhập
        cardHolder.setLayout(new BoxLayout(cardHolder, BoxLayout.Y_AXIS));// xếp theo chều dọc
        cardHolder.setOpaque(false); //tỏng suốt background
        add(cardHolder);

        cardHolder.add(new AppLogoHeader());
        cardHolder.add(Box.createRigidArea(new Dimension(0, 30)));

        LoginForm loginForm = new LoginForm(this::attemptLogin);
        loginForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardHolder.add(loginForm);
    }

    private void attemptLogin(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NHẬP ĐẦY ĐỦ TÀI KHOẢN, MẶT KHẨU DÔ", "Canh bao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AppContext.authController.Login(user, pass).ifPresentOrElse(
                account -> MainNavigator.showDashboard(),
                () -> JOptionPane.showMessageDialog(this, "TÀI KHOẢN HOẶC PASS SAI", "Loi dang nhap", JOptionPane.ERROR_MESSAGE)
        );
    }
}
