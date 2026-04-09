package com.bangcompany.onlineute.View.navigation;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.WindowManager;
import com.bangcompany.onlineute.View.features.auth.LoginScreen;
import com.bangcompany.onlineute.View.features.dashboard.DashboardLayout;
import com.bangcompany.onlineute.View.shared.ViewContext;

public final class MainNavigator {
    private MainNavigator() {}

    private static ViewContext viewContext;
    private static AppContext appContext;

    public static void init(AppContext context) {
        appContext = context;
    }

    private static ViewContext getViewContext() {
        if (viewContext == null) {
            if (appContext == null) {
                throw new IllegalStateException("AppContext chua khoi tao.");
            }
            viewContext = new ViewContext(
                    appContext.accountController,
                    appContext.authController,
                    appContext.studentController,
                    appContext.lecturerController,
                    appContext.facultyController,
                    appContext.majorController,
                    appContext.classController,
                    appContext.courseController,
                    appContext.termController,
                    appContext.registrationBatchController,
                    appContext.courseSectionController,
                    appContext.courseRegistrationController,
                    appContext.notificationController,
                    appContext.markController,
                    appContext.userProfileController,
                    appContext.scheduleController
            );
        }
        return viewContext;
    }

    public static void checkSession() {
        if (SessionManager.getCurrentAccount() == null) {
            showLogin();
        } else {
            showDashboard();
        }
    }

    public static void showLogin() {
        WindowManager.show(new LoginScreen(getViewContext()));
    }

    public static void showDashboard() {
        WindowManager.show(new DashboardLayout(getViewContext()));
    }
}