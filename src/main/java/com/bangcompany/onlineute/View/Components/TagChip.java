package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TagChip extends JLabel {
    public TagChip(String text) {
        super(text);
        setOpaque(true);
        setBackground(new Color(238, 244, 251));
        setForeground(new Color(24, 70, 121));
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setBorder(new EmptyBorder(8, 12, 8, 12));
    }
}
