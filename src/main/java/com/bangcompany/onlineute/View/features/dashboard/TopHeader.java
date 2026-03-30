package com.bangcompany.onlineute.View.features.dashboard;

import javax.swing.*;
import java.awt.*;

public class TopHeader extends JPanel {
    public TopHeader(String title) {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(100, 100));
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 85, 141));
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
    }
}
