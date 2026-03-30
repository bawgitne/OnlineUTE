package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;

import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JPanel {
    public ProfilePage() {
        setLayout(new BorderLayout());

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(new JLabel("Thong tin ca nhan", SwingConstants.CENTER), BorderLayout.CENTER);

        PageScaffold scaffold = new PageScaffold("Trang thong tin ca nhan");
        scaffold.setBody(body);
        add(scaffold, BorderLayout.CENTER);
    }
}

