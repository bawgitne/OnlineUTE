/**
 * quản lí tab
 */
package com.bangcompany.onlineute.View;

import javax.swing.*;
import java.awt.*;

public final class WindowManager extends JFrame {
    private static WindowManager instance;
    private static JPanel container;

    private WindowManager() {
        setTitle("OnlineUTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setResizable(true);
        setLocationRelativeTo(null);
        container = new JPanel(new BorderLayout());
        add(container);
    }

    private static void init() {
        if (instance == null) {
            instance = new WindowManager();
        }
    }

    // xóa tab khác dduaw tab kia vào 1 lần 1 tab
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

    public static void exit() {
        if (instance != null) {
            instance.dispose();
        }
        System.exit(0);
    }
}
