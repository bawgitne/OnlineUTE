package com.bangcompany.onlineute.view.features.auth;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.view.navigation.MainNavigator;
import com.bangcompany.onlineute.view.shared.components.AppLogoHeader;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {
    public LoginScreen() {
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
            JOptionPane.showMessageDialog(this, "Vui long nhap day du tai khoan va mat khau!", "Canh bao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AppContext.authController.Login(user, pass).ifPresentOrElse(
                account -> MainNavigator.showDashboard(),
                () -> JOptionPane.showMessageDialog(this, "Tai khoan hoac mat khau khong chinh xac!", "Loi dang nhap", JOptionPane.ERROR_MESSAGE)
        );
    }
}
