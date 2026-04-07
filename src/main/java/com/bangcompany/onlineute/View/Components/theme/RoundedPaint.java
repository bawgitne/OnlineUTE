/**
 * RoundedPaint.java
 * Công dụng: Cung cấp các phương thức vẽ tùy chỉnh để tạo hiệu ứng nền bo góc cho các thành phần Swing.
 */
package com.bangcompany.onlineute.View.Components.theme;

import javax.swing.*;
import java.awt.*;

public final class RoundedPaint {
    private RoundedPaint() {}

    // Vô hiệu hóa các thuộc tính vẽ mặc định của nút để cho phép vẽ bo góc tùy chỉnh
    public static void prepareButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    // Vẽ hình chữ nhật bo góc làm nền cho một thành phần cụ thể
    public static void fillRoundBackground(Graphics graphics, JComponent component, Color color, int arc) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, component.getWidth(), component.getHeight(), arc, arc);
        g2.dispose();
    }
}
