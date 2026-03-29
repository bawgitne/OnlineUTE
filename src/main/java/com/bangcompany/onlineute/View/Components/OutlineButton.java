package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * OutlineButton - A white button with a border, used for secondary actions like Google Login.
 */
public class OutlineButton extends JButton {

    public OutlineButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(true);
        setBackground(Color.WHITE);
        setForeground(new Color(0, 84, 140)); // Blue text
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        // Optional hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(245, 248, 250));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }
        });
    }

    // Custom paint to maintain background color consistency
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
