/**
 * thẻ thôi,
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;
import com.bangcompany.onlineute.View.Components.theme.RoundedPaint;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Card extends JPanel {
    private final int arc;

    // tạo mặc định
    public Card() {
        this(AppTheme.RADIUS_CARD, new Insets(18, 18, 18, 18));
    }

    // độ round và padding
    public Card(int arc, Insets padding) {
        this.arc = arc;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                RoundedBorders.outline(AppTheme.BORDER_LIGHT, arc, new Insets(0, 0, 0, 0)),
                new EmptyBorder(padding)
        ));
        setOpaque(false);
    }

    // mục tiêu đề từng tab
    public static Card titleCard(String text) {
        Card card = new Card(AppTheme.RADIUS_PANEL, new Insets(6, 12, 6, 12));
        JLabel label = new JLabel(text == null ? "" : text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(AppTheme.PRIMARY_BLUE);
        label.setOpaque(false);
        card.add(label, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(0, 44));
        return card;
    }

    // bo góc
    @Override
    protected void paintComponent(Graphics g) {
        RoundedPaint.fillRoundBackground(g, this, getBackground(), arc);
        super.paintComponent(g);
    }
}
