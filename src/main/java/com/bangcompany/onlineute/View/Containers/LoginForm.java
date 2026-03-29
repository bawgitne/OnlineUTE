package com.bangcompany.onlineute.View.Containers;

import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;


public class LoginForm extends JPanel {

    private final BiConsumer<String, String> onSubmit;
    private final InputGroup usernameGroup;
    private final InputGroup passwordGroup;

    public LoginForm(BiConsumer<String, String> onSubmit) {
        this.onSubmit = onSubmit;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));
        setPreferredSize(new Dimension(500, 450));
        setMaximumSize(new Dimension(500, 450));

        // Form Title
        JLabel loginTitle = new JLabel("ĐĂNG NHẬP");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        loginTitle.setForeground(new Color(40, 80, 140));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(loginTitle);
        
        JLabel subTitle = new JLabel("Cổng thông tin đào tạo");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(subTitle);
        
        add(Box.createRigidArea(new Dimension(0, 25)));

        // Inputs
        usernameGroup = new InputGroup("Tên đăng nhập", false);
        passwordGroup = new InputGroup("Mật khẩu", true);
        usernameGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordGroup.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(usernameGroup);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(passwordGroup);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Primary Login Button
        PrimaryButton btnLogin = new PrimaryButton("Đăng nhập");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            if (this.onSubmit != null) {
                this.onSubmit.accept(usernameGroup.getValue().trim(), passwordGroup.getValue().trim());
            }
        });
        
        add(btnLogin);
        JLabel forgotPassLabel = new JLabel("Quên mật khẩu");
        forgotPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPassLabel.setForeground(Color.GRAY);
        forgotPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(forgotPassLabel);
    }
}
