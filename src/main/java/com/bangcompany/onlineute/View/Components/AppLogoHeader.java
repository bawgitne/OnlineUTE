package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AppLogoHeader - Reusable component for displaying the main application logo and unit name.
 */
public class AppLogoHeader extends JPanel {

    public AppLogoHeader() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // HCM-UTE subtitle
        JLabel shortName = new JLabel("HCM-UTE");
        shortName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        shortName.setForeground(new Color(0, 85, 141));
        shortName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full name line 1
        JLabel uniName1 = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT");
        uniName1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName1.setForeground(new Color(0, 40, 80));
        uniName1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full name line 2
        JLabel uniName2 = new JLabel("TP.HCM");
        uniName2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName2.setForeground(new Color(0, 40, 80));
        uniName2.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(logoLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(shortName);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(uniName1);
        add(uniName2);
    }
}
