package com.bangcompany.onlineute;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;
import com.bangcompany.onlineute.View.navigation.MainNavigator;
import javax.swing.*;
import java.awt.*;

/**
 * OnlineUteApplication - Main entry point.
 */
public class OnlineUteApplication {
    public static void main(String[] args) {
        System.out.println("OnlineUTE Application Starting...");

        // 1. Initialize Database
        try {
            System.out.println("Connecting to Database...");
            JpaUtil.getEntityManager().close();
            System.out.println("OnlineUTE Database connected.");
        } catch (Exception e) {
            System.err.println("Database Startup Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Khong the ket noi Database!",
                "Loi khoi dong",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // 2. Initialize AppContext (instance)
        AppContext appContext = AppContext.init();
        MainNavigator.init(appContext);

        // Global exception handler for UI safety
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
            SwingUtilities.invokeLater(() ->
                ExceptionHandler.showError(null, throwable, "Da xay ra loi.")
            )
        );
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
            @Override
            protected void dispatchEvent(AWTEvent event) {
                try {
                    super.dispatchEvent(event);
                } catch (Throwable throwable) {
                    ExceptionHandler.showError(null, throwable, "Da xay ra loi.");
                }
            }
        });

        // 3. Launch Interface using WindowManager (Centralized)
        SwingUtilities.invokeLater(() -> {
            try {
                MainNavigator.checkSession();
            } catch (Exception ex) {
                ExceptionHandler.showError(null, ex, "Da xay ra loi.");
            }
        });
        System.out.println("Xem file readme: https://github.com/bawgitne/OnlineUTE/blob/master/README.md");
        System.out.println("Thông tin tài khoản tạo sẵn\n" +
                "#### 1. Role ADMIN:\n" +
                "- Username: AD001\n" +
                "- Password: admin123\n" +
                "#### 2. Role GIANGVIEN:\n" +
                "- Username: GV001 - GV011\n" +
                "- Password: 123456\n" +
                "#### 3. Role SINHVIEC:\n" +
                "- Username: 24110001 - 24110200\n" +
                "- Password: 123456");
        // 4. Cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JpaUtil.shutdown();
        }));
    }
}