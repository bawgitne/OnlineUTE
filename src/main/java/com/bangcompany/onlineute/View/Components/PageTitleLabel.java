package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class PageTitleLabel extends JLabel {
    public PageTitleLabel(String text) {
        super("   " + text.toUpperCase());
        setFont(new Font("Segoe UI", Font.BOLD, 18));
        setForeground(new Color(0, 85, 141));
        setOpaque(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 40));
    }
}
