package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {
    public PrimaryButton(String text) {
        super(text);
        setBackground(new Color(0, 84, 140));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        RoundedPainter.prepareButton(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedPainter.fillRoundBackground(g, this, getBackground(), 18);
        super.paintComponent(g);
    }
}
