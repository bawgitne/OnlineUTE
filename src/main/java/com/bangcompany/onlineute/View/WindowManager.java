package com.bangcompany.onlineute.View;

import javax.swing.*;
import java.awt.*;

/**
 * WindowManager - The singleton window for the whole application.
 * Generalized to show any JPanel as the main screen.
 */
public final class WindowManager extends JFrame {
    private static WindowManager instance;
    private static JPanel container;



    private WindowManager() {
        setTitle("U Tê E");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200,800);
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

    /**
     * Generalized method to switch the main content panel.
     * @param panel The JPanel to display.
     */
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
