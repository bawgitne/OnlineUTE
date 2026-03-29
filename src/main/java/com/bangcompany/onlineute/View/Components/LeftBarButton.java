package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LeftBarButton extends JButton {
    public LeftBarButton(String text, String iconName) {
        super("  " + text);
        setForeground(Color.WHITE);
        // Using Segoe UI for a modern system look (standard on Windows)
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Load and tint Icon to White (for the blue background)
        try {
            ImageIcon originalIcon = new ImageIcon("public/Icon/" + iconName);
            if (originalIcon.getImage() != null) {
                Image whiteImg = tintImageToWhite(originalIcon.getImage());
                Image scaledImg = whiteImg.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImg));
            }
        } catch (Exception e) {}

        setMaximumSize(new Dimension(280, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Tints a PNG image to pure white while preserving transparency.
     */
    private Image tintImageToWhite(Image src) {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        java.awt.image.BufferedImage tinted = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tinted.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return tinted;
    }

    private boolean active = false;

    public void setActive(boolean isActive) {
        this.active = isActive;
        if (active) {
            setBackground(new Color(0, 105, 175)); // Lighter blue for active
            setOpaque(true);
        } else {
            setOpaque(false);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (active) {
            g.setColor(new Color(0, 105, 175));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}
