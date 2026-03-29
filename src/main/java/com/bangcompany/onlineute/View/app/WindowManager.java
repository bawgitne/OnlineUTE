package com.bangcompany.onlineute.view.app;

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

    public static void showScreen(JPanel panel) {
        init();
        container.removeAll();
        container.add(panel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();

        if (!instance.isVisible()) {
            instance.setVisible(true);
        }
    }
}
