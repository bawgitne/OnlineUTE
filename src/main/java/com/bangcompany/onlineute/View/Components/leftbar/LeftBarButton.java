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
    private static final Color ACTIVE_BACKGROUND = new Color(0, 66, 110);
    private static final int DEFAULT_LEFT_PADDING = 14;
    private static final int HOVER_LEFT_PADDING = 24;

    private final String buttonText;
    private boolean active;

    public LeftBarButton(String text, String iconName) {
        this.buttonText = text;
        setText(text);
        setHorizontalAlignment(SwingConstants.LEFT);
        setForeground(Color.WHITE);
        setBackground(DEFAULT_BACKGROUND);
        RoundedPainter.prepareButton(this);
        setRolloverEnabled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setPreferredSize(new Dimension(220, 45));
        setIconTextGap(10);
        setBorder(new EmptyBorder(12, DEFAULT_LEFT_PADDING, 12, 16));

        try {
            setIcon(createWhiteIcon(iconName));
        } catch (Exception e) {
            setIcon(null);
        }

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
        setForeground(active ? Color.orange : Color.WHITE);
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

    private ImageIcon createWhiteIcon(String iconName) {
        BufferedImage sourceImage;
        try {
            sourceImage = ImageIO.read(new File("public/Icon/" + iconName));
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load icon: " + iconName, e);
        }

        Image scaledImage = sourceImage.getScaledInstance(18, 18, Image.SCALE_SMOOTH);

        BufferedImage tintedImage = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tintedImage.createGraphics();
        g2.drawImage(scaledImage, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 18, 18);
        g2.dispose();

        return new ImageIcon(tintedImage);
    }
}
