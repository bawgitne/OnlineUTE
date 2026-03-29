package com.bangcompany.onlineute.view.features.auth;

import com.bangcompany.onlineute.view.shared.components.InputGroup;
import com.bangcompany.onlineute.view.shared.components.PrimaryButton;

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

        JLabel loginTitle = new JLabel("DANG NHAP");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        loginTitle.setForeground(new Color(40, 80, 140));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(loginTitle);

        JLabel subTitle = new JLabel("Cong thong tin dao tao");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(subTitle);

        add(Box.createRigidArea(new Dimension(0, 25)));

        usernameGroup = new InputGroup("Ten dang nhap", false);
        passwordGroup = new InputGroup("Mat khau", true);
        usernameGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordGroup.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(usernameGroup);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(passwordGroup);
        add(Box.createRigidArea(new Dimension(0, 15)));

        PrimaryButton btnLogin = new PrimaryButton("Dang nhap");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            if (this.onSubmit != null) {
                this.onSubmit.accept(usernameGroup.getValue().trim(), passwordGroup.getValue().trim());
            }
        });
        add(btnLogin);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(230, 230, 230));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel forgotPassLabel = new JLabel("Quen mat khau");
        forgotPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPassLabel.setForeground(Color.GRAY);
        forgotPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPassLabel.setForeground(new Color(0, 84, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotPassLabel.setForeground(Color.GRAY);
            }
        });
        add(forgotPassLabel);
    }
}
