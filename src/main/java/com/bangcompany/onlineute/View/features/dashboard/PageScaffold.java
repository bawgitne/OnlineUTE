package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.RoundedPanel;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageScaffold extends JPanel {
    private final JPanel body;

    public PageScaffold(String title) {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = new RoundedPanel(26);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(225, 230, 236), 26, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        card.add(new PageTitleLabel(title), BorderLayout.NORTH);

        body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        card.add(body, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    public void setBody(Component component) {
        body.removeAll();
        body.add(component, BorderLayout.CENTER);
    }
}
