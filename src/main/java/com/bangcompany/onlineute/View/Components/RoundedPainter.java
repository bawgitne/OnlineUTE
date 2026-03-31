package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public final class RoundedPainter {
    private RoundedPainter() {
    }

    public static void prepareButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    public static void fillRoundBackground(Graphics graphics, JComponent component, Color color, int arc) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, component.getWidth(), component.getHeight(), arc, arc);
        g2.dispose();
    }
}
