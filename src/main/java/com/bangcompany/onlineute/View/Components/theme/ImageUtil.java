/**
 * ImageUtil.java
 * Công dụng: Cung cấp các phương thức tiện ích để tải, thay đổi kích thước và xử lý hình ảnh/biểu tượng.
 */
package com.bangcompany.onlineute.View.Components.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public final class ImageUtil {
    private static final String ICON_DIR = "public/Icon/";

    private ImageUtil() {}

    // load hình ảnh ste kích thước
    public static ImageIcon loadImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // tạo label chứa mage
    public static JLabel createImageLabel(String path, int width, int height, Insets padding) {
        JLabel label = new JLabel();
        ImageIcon icon = loadImage(path, width, height);
        if (icon != null) {
            label.setIcon(icon);
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (padding != null) {
            label.setBorder(new EmptyBorder(padding));
        }
        return label;
    }

    // Lấy đối tượng File của biểu tượng từ thư mục mặc định
    public static File getIconFile(String iconName) {
        return new File(ICON_DIR + iconName);
    }
}
