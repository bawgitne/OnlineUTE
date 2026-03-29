package com.bangcompany.onlineute.View.Components;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * NavMenu - Reusable dumb component.
 * Renders a list of navigation buttons and emits click events.
 * Can be used in Sidebar, bottom nav, settings panel, etc.
 */
public class NavMenu extends JPanel {

    private final Map<String, LeftBarButton> buttonMap = new HashMap<>();
    private LeftBarButton activeButton;

    /**
     * @param sectionTitle  Title header for the menu section (e.g. "MENU CHÍNH").
     * @param menuItems     Pre-filtered list of MenuItems to render.
     * @param onNavigate    Callback emitted when a menu item is clicked.
     */
    public NavMenu(String sectionTitle, List<MenuItem> menuItems, Consumer<String> onNavigate) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        // Section title
        gbc.insets = new Insets(15, 0, 5, 0);
        add(new LeftBarTitle(sectionTitle), gbc);
        gbc.gridy++;

        // Menu buttons
        for (MenuItem item : menuItems) {
            gbc.insets = new Insets(2, 0, 2, 0);
            LeftBarButton btn = new LeftBarButton(item.getLabel(), item.getIcon());
            buttonMap.put(item.name(), btn);

            btn.addActionListener(e -> {
                setActiveTab(item.name());
                if (onNavigate != null) onNavigate.accept(item.name());
            });

            add(btn, gbc);
            gbc.gridy++;
        }

        // Push to top
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    public void setActiveTab(String pageKey) {
        LeftBarButton btn = buttonMap.get(pageKey);
        if (btn != null) {
            if (activeButton != null) activeButton.setActive(false);
            btn.setActive(true);
            activeButton = btn;
        }
    }
}
