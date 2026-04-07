/**
 * RoundedBorders.java
 * Công dụng: Cung cấp các loại đường viền bo góc tùy chỉnh cho các thành phần UI.
 */
package com.bangcompany.onlineute.View.Components.theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class RoundedBorders {
    private RoundedBorders() {}

    // Tạo đường viền nét mảnh bo góc với màu sắc và khoảng cách lề tùy chỉnh
    public static Border outline(Color color, int arc, Insets insets) {
        return new OutlineBorder(color, arc, insets == null ? new Insets(0, 0, 0, 0) : insets);
    }

    // Tạo đường viền bo góc kết hợp khảng cách đệm (padding) bên trong
    public static Border paddedOutline(Color color, int arc, Insets padding) {
        return BorderFactory.createCompoundBorder(
                outline(color, arc, new Insets(0, 0, 0, 0)),
                new EmptyBorder(padding)
        );
    }

    // Tạo đường viền bo góc có đệm với màu đường viền mặc định của hệ thống
    public static Border paddedOutline(int arc, Insets padding) {
        return paddedOutline(AppTheme.BORDER_LIGHT, arc, padding);
    }

    // Tạo đường viền có nhãn tiêu đề (title) đè lên nét vẽ bo góc
    public static Border titleBorder(String title, Color borderColor, Color titleColor, Color backgroundColor, Font titleFont, int arc) {
        return new TitleBorder(title, borderColor, titleColor, backgroundColor, titleFont, arc);
    }

    // Lớp nội bộ thực hiện việc vẽ đường viền bo góc cơ bản
    private static class OutlineBorder extends AbstractBorder {
        private final Color borderColor;
        private final int arc;
        private final Insets insets;

        private OutlineBorder(Color borderColor, int arc, Insets insets) {
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

    // Lớp nội bộ thực hiện việc vẽ đường viền có tiêu đề phức tạp
    private static class TitleBorder extends AbstractBorder {
        private final String title;
        private final Color borderColor;
        private final Color titleColor;
        private final Color backgroundColor;
        private final Font titleFont;
        private final int arc;

        private TitleBorder(String title, Color borderColor, Color titleColor, Color backgroundColor, Font titleFont, int arc) {
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

            // Vẽ khung bo góc
            g2.setColor(borderColor);
            g2.drawRoundRect(x, borderY, width - 1, borderHeight, arc, arc);

            // Vẽ một hình chữ nhật nhỏ cùng màu nền để chèn văn bản tiêu đề vào giữa đường viền
            int patchX = titleX - 4;
            int patchY = y;
            int patchWidth = titleWidth + 8;
            int patchHeight = titleHeight + 2;
            g2.setColor(backgroundColor);
            g2.fillRect(patchX, patchY, patchWidth, patchHeight);

            // Vẽ văn bản tiêu đề
            g2.setColor(titleColor);
            g2.drawString(title, titleX, titleY);
            g2.dispose();
        }
    }
}
