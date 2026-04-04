package com.bangcompany.onlineute.View.Components;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedOutlineBorder extends AbstractBorder {
    private final Color borderColor;
    private final int arc;
    private final Insets insets;

    public RoundedOutlineBorder(Color borderColor, int arc, Insets insets) {
        this.borderColor = borderColor;
        this.arc = arc;
        this.insets = insets;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = this.insets.top;
        insets.left = this.insets.left;
        insets.bottom = this.insets.bottom;
        insets.right = this.insets.right;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
        g2.dispose();
    }
}
