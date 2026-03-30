package com.bangcompany.onlineute.View.navigation;

import com.bangcompany.onlineute.View.WindowManager;
import com.bangcompany.onlineute.View.features.auth.LoginScreen;
import com.bangcompany.onlineute.View.features.dashboard.DashboardScreen;

public final class MainNavigator {
    private MainNavigator() {}

    public static void showLogin() {
        WindowManager.show(new LoginScreen());
    }

    public static void showDashboard() {
        WindowManager.show(new DashboardScreen());
    }
}
// hàm trung gian để lệnh show nó gọn hơn