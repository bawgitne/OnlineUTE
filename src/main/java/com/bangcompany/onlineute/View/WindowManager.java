package com.bangcompany.onlineute.View;

import com.bangcompany.onlineute.View.Screens.DashboardFrame;
import com.bangcompany.onlineute.View.Screens.LoginFrame;
import javax.swing.*;

public final class WindowManager {
    private static JFrame currentFrame;

    private WindowManager() {}

    public static void showLogin() {
        switchFrame(new LoginFrame());
    }

    public static void showDashboard() {
        switchFrame(new DashboardFrame());
    }

    private static void switchFrame(JFrame nextFrame) {
        SwingUtilities.invokeLater(() -> {
            if (currentFrame != null) {
                currentFrame.dispose();
            }
            currentFrame = nextFrame;
            currentFrame.setVisible(true);
        });
    }

    public static void exit() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        System.exit(0);
    }
}
