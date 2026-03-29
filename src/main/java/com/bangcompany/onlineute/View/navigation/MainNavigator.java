package com.bangcompany.onlineute.view.navigation;

import com.bangcompany.onlineute.view.app.WindowManager;
import com.bangcompany.onlineute.view.features.auth.LoginScreen;
import com.bangcompany.onlineute.view.layout.DashboardLayout;

public final class MainNavigator {
    private MainNavigator() {}

    public static void showLogin() {
        WindowManager.showScreen(new LoginScreen());
    }

    public static void showDashboard() {
        WindowManager.showScreen(new DashboardLayout());
    }
}
