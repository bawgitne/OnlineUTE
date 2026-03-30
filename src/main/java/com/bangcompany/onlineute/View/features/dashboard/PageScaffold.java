package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.View.Components.PageTitleLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageScaffold extends JPanel {
    private final JPanel body;

    public PageScaffold(String title) {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(new PageTitleLabel(title), BorderLayout.NORTH);

        body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        add(body, BorderLayout.CENTER);
    }

    public void setBody(Component component) {
        body.removeAll();
        body.add(component, BorderLayout.CENTER);
    }
}
