package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserProfileCard extends JPanel {

    public UserProfileCard(String userName, String userId, String roleDisplayName) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(createLogo());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createAccountInfo(userName, userId, roleDisplayName));
    }

    private JLabel createLogo() {
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(100, 127, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        return logoLabel;
    }

    private JPanel createAccountInfo(String userName, String userId, String roleDisplayName) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 50, 50);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        panel.add(avatarLabel);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.add(createLabel(userName, 14, Font.BOLD, Color.WHITE));
        detailsPanel.add(createLabel(roleDisplayName, 11, Font.PLAIN, new Color(200, 200, 200)));
        detailsPanel.add(createLabel(userId, 11, Font.PLAIN, new Color(200, 200, 200)));

        panel.add(detailsPanel);
        return panel;
    }

    private JLabel createLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", style, size));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(2, 0, 2, 0));
        return label;
    }
}
