package com.bangcompany.onlineute.View.Components.leftbar;

import com.bangcompany.onlineute.View.Components.RoundedPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LeftBarButton extends JButton {
    private static final Color DEFAULT_BACKGROUND = new Color(0, 85, 141);
    //private static final Color ACTIVE_BACKGROUND = new Color(0, 66, 110);
    private static final Color ACTIVE_BACKGROUND = new Color(0, 85, 141);
    private static final Color DEFAULT_FOREGROUND = Color.WHITE;
    private static final Color ACTIVE_FOREGROUND = new Color(201, 138, 23);
    private static final int DEFAULT_LEFT_PADDING = 14;
    private static final int HOVER_LEFT_PADDING = 24;

    private final String iconName;
    private final ImageIcon defaultIcon;
    private final ImageIcon activeIcon;
    private boolean active;

    public LeftBarButton(String text, String iconName) {
        this.iconName = iconName;
        setText(text);
        setHorizontalAlignment(SwingConstants.LEFT);
        setForeground(DEFAULT_FOREGROUND);
        setBackground(DEFAULT_BACKGROUND);
        RoundedPainter.prepareButton(this);
        setRolloverEnabled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setPreferredSize(new Dimension(220, 45));
        setIconTextGap(10);
        setBorder(new EmptyBorder(12, DEFAULT_LEFT_PADDING, 12, 16));

        defaultIcon = loadIcon(iconName, DEFAULT_FOREGROUND);
        activeIcon = loadIcon(iconName, ACTIVE_FOREGROUND);
        setIcon(defaultIcon);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                applyHoverStyle(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                applyHoverStyle(false);
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        setBackground(active ? ACTIVE_BACKGROUND : DEFAULT_BACKGROUND);
        setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 15));
        setForeground(active ? ACTIVE_FOREGROUND : DEFAULT_FOREGROUND);
        setIcon(active ? activeIcon : defaultIcon);
    }

    private void applyHoverStyle(boolean hovering) {
        int leftPadding = hovering ? HOVER_LEFT_PADDING : DEFAULT_LEFT_PADDING;
        setBorder(new EmptyBorder(12, leftPadding, 12, 16));
        setBackground(active ? ACTIVE_BACKGROUND : DEFAULT_BACKGROUND);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedPainter.fillRoundBackground(g, this, getBackground(), 16);
        super.paintComponent(g);
    }

    private ImageIcon loadIcon(String iconName, Color color) {
        BufferedImage sourceImage;
        try {
            sourceImage = ImageIO.read(new File("public/Icon/" + iconName));
        } catch (IOException e) {
            return null;
        }

        Image scaledImage = sourceImage.getScaledInstance(18, 18, Image.SCALE_SMOOTH);

        BufferedImage tintedImage = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tintedImage.createGraphics();
        g2.drawImage(scaledImage, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(color);
        g2.fillRect(0, 0, 18, 18);
        g2.dispose();

        return new ImageIcon(tintedImage);
    }
}
