package com.bangcompany.onlineute.View.Screens;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Containers.InputGroup;
import com.bangcompany.onlineute.View.WindowManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * LoginFrame - Interacts with WindowManager for screen transitions.
 */
public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Đăng nhập - OnlineUTE HCMUTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(230, 235, 240));
        add(mainPanel);

        JPanel cardHolder = new JPanel();
        cardHolder.setLayout(new BoxLayout(cardHolder, BoxLayout.Y_AXIS));
        cardHolder.setOpaque(false);
        mainPanel.add(cardHolder);

        // Header Section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uniNameLabel = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT");
        uniNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        uniNameLabel.setForeground(new Color(0, 85, 141));
        uniNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(uniNameLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardHolder.add(headerPanel);

        // Form Card
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 40, 30, 40)
        ));
        formCard.setPreferredSize(new Dimension(450, 400));
        formCard.setMaximumSize(new Dimension(450, 400));

        JLabel loginTitle = new JLabel("ĐĂNG NHẬP");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(new Color(52, 73, 94));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(loginTitle);
        
        JLabel subTitle = new JLabel("Cổng thông tin đào tạo & dịch vụ sinh viên");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(subTitle);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));

        InputGroup usernameGroup = new InputGroup("Tên đăng nhập / MSSV", false);
        InputGroup passwordGroup = new InputGroup("Mật khẩu", true);
        formCard.add(usernameGroup);
        formCard.add(passwordGroup);

        PrimaryButton btnLogin = new PrimaryButton("Đăng nhập ngay");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.addActionListener(e -> {
            String user = usernameGroup.getValue().trim();
            String pass = passwordGroup.getValue().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Using the global controller singleton
            AppContext.authController.Login(user, pass).ifPresentOrElse(
                account -> {
                    // Centralized window management
                    WindowManager.showDashboard();
                },
                () -> {
                    JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                }
            );
        });
        
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(btnLogin);
        
        cardHolder.add(formCard);
    }
}
