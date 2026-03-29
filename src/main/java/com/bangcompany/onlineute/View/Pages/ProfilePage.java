package com.bangcompany.onlineute.View.Pages;

import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JPanel {
    public ProfilePage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("TRANG THÔNG TIN CÁ NHÂN", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
}
