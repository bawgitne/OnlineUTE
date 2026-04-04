package com.bangcompany.onlineute.View.Components;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedTitleBorder extends AbstractBorder {
    private final String title;
    private final Color borderColor;
    private final Color titleColor;
    private final Color backgroundColor;
    private final Font titleFont;
    private final int arc;

    public RoundedTitleBorder(String title, Color borderColor, Color titleColor, Color backgroundColor, Font titleFont, int arc) {
        this.title = title == null ? "" : title;
        this.borderColor = borderColor;
        this.titleColor = titleColor;
        this.backgroundColor = backgroundColor;
        this.titleFont = titleFont;
        this.arc = arc;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(18, 12, 10, 12);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 18;
        insets.left = 12;
        insets.bottom = 10;
        insets.right = 12;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleHeight = fm.getAscent();
        int titleX = x + 12;
        int titleY = y + titleHeight;

        int borderY = y + (titleHeight / 2) + 2;
        int borderHeight = height - borderY - 1;

        g2.setColor(borderColor);
        g2.drawRoundRect(x, borderY, width - 1, borderHeight, arc, arc);

        int patchX = titleX - 4;
        int patchY = y;
        int patchWidth = titleWidth + 8;
        int patchHeight = titleHeight + 2;
        g2.setColor(backgroundColor);
        g2.fillRect(patchX, patchY, patchWidth, patchHeight);

        g2.setColor(titleColor);
        g2.drawString(title, titleX, titleY);
        g2.dispose();
    }
}
