package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;
import com.bangcompany.onlineute.View.Components.ui.LabelValuePanel;

import javax.swing.*;
import java.awt.*;

public class ProfileSectionCard extends JPanel {
    private final JPanel contentPanel;

    public ProfileSectionCard(String title) {
        setLayout(new BorderLayout(0, 14));
        setBackground(Color.WHITE);
        setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_CARD, new Insets(18, 20, 18, 20)));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE);
        add(titleLabel, BorderLayout.NORTH);

        contentPanel = new JPanel(new GridLayout(0, 2, 24, 0));
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addField(String label, String value) {
        contentPanel.add(new LabelValuePanel(label, value));
    }
}

