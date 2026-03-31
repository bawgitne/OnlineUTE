package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LeftBarTitle extends JLabel {
    public LeftBarTitle(String text) {
        super(text);
        setForeground(new Color(173, 216, 230));
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setBorder(new EmptyBorder(10, 12, 8, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
