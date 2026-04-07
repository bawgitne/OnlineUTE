/**
 * cái nút
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedPaint;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    // hàm tạo mặc định
    public Button(String text) {
        this(text, AppTheme.PRIMARY_BLUE, Color.WHITE);
    }

    // chỉnh bg
    public Button(String text, Color background) {
        this(text, background, Color.WHITE);
    }

    // chỉnh màu text
    public Button(String text, Color background, Color foreground) {
        super(text);
        applyStyle(background, foreground);
    }

    // set bg mới
    public void setBackgroundColor(Color background) {
        setBackground(background);
        repaint();
    }

    // set c bg và màu text
    private void applyStyle(Color background, Color foreground) {
        setBackground(background);
        setForeground(foreground);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        RoundedPaint.prepareButton(this);
    }

    // bo góc
    @Override
    protected void paintComponent(Graphics g) {
        RoundedPaint.fillRoundBackground(g, this, getBackground(), AppTheme.RADIUS_BUTTON);
        super.paintComponent(g);
    }
}
