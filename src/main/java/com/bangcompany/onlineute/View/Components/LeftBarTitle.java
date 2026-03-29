package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LeftBarTitle extends JLabel {
    public LeftBarTitle(String text) {
        super(text.toUpperCase());
        // Apply requested style: yellow color (209, 205, 20), Segoe UI, 12pt, Bold
        setForeground(new Color(209, 205, 20));
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 5, 5)); // Added some line-height-like space
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
