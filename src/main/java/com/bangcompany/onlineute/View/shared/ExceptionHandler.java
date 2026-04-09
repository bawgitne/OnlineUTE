package com.bangcompany.onlineute.View.shared;

import com.bangcompany.onlineute.Exception.BusinessException;

import javax.swing.*;
import java.awt.*;

public final class ExceptionHandler {
    private ExceptionHandler() {}

    public static void showError(Component parent, Throwable throwable, String fallbackMessage) {
        String message = resolveMessage(throwable, fallbackMessage);
        if (throwable instanceof BusinessException) {
            JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showError(Component parent, Throwable throwable) {
        showError(parent, throwable, "Đã xảy ra lỗi.");
    }

    public static void showError(Component parent, Exception ex, String fallbackMessage) {
        showError(parent, (Throwable) ex, fallbackMessage);
    }

    public static void showError(Component parent, Exception ex) {
        showError(parent, (Throwable) ex, "Đã xảy ra lỗi.");
    }

    public static void showInfo(Component parent, String message, String title) {
        String safeTitle = title == null || title.isBlank() ? "Thông báo" : title;
        String safeMessage = message == null || message.isBlank() ? "Hoàn thành." : message;
        JOptionPane.showMessageDialog(parent, safeMessage, safeTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    private static String resolveMessage(Throwable throwable, String fallbackMessage) {
        if (throwable == null) {
            return fallbackMessage == null || fallbackMessage.isBlank() ? "Đã xảy ra lỗi." : fallbackMessage;
        }
        String message = throwable.getMessage();
        if (message != null && !message.isBlank()) {
            return message;
        }
        Throwable cause = throwable.getCause();
        if (cause != null && cause.getMessage() != null && !cause.getMessage().isBlank()) {
            return cause.getMessage();
        }
        return fallbackMessage == null || fallbackMessage.isBlank() ? "Đã xảy ra lỗi." : fallbackMessage;
    }
}
