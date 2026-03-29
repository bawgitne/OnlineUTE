package com.bangcompany.onlineute;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.JpaUtil;
import com.bangcompany.onlineute.View.WindowManager;
import com.bangcompany.onlineute.View.Screens.LoginFrame;
import javax.swing.*;

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
                "Không thể kết nối Database!", 
                "Lỗi khởi động", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // 2. Initialize AppContext
        AppContext.init();

        // 3. Launch Interface using WindowManager (Centralized)
        SwingUtilities.invokeLater(() -> {
            try {
                // Show the Login screen centrally
                WindowManager.show(new LoginFrame());
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 4. Cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JpaUtil.shutdown();
        }));
    }
}
