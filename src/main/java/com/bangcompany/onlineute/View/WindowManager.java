/**
 * quản lý cửa sổ chính của app
 */
package com.bangcompany.onlineute.View;

import javax.swing.*;
import java.awt.*;

public final class WindowManager extends JFrame {
    private static WindowManager instance;
    private static JPanel container;

    // setup kích thước, tên app
    private WindowManager() {
        setTitle("OnlineUTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setResizable(true);
        setLocationRelativeTo(null);
        container = new JPanel(new BorderLayout());
        add(container);
    }

    // tạo instance duy nhất
    public static void init() {
        if (instance == null) {
            instance = new WindowManager();
        }
    }

    // xóa nội dung cũ, nạp panel mới vào màn hình
    public static void show(JPanel panel) {
        init();
        container.removeAll();
        container.add(panel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();

        if (!instance.isVisible()) {
            instance.setVisible(true);
        }
    }

    // đóng app
    public static void close() {
        if (instance != null) {
            instance.dispose();
        }
        System.exit(0);
    }
}
