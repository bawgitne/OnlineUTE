package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageTitleLabel extends RoundedPanel {
    public PageTitleLabel(String text) {
        super(18);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(6, 12, 6, 12));

        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(0, 85, 141));
        label.setOpaque(false);

        add(label, BorderLayout.CENTER);
        setPreferredSize(new Dimension(0, 44));
    }
}
