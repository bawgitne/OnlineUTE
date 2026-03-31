package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NavMenu extends JPanel {
    private final Map<String, LeftBarButton> buttonMap = new HashMap<>();
    private LeftBarButton activeButton;

    public NavMenu(List<SidebarItem> menuItems, Consumer<String> onNavigate) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        for (SidebarItem item : menuItems) {
            if (item.isTitle()) {
                gbc.insets = new Insets(15, 0, 5, 0);
                add(new LeftBarTitle(item.getLabel()), gbc);
                gbc.gridy++;
                continue;
            }

            gbc.insets = new Insets(4, 0, 4, 0);

            LeftBarButton button = new LeftBarButton(item.getLabel(), item.getIcon());
            buttonMap.put(item.getKey(), button);

            button.addActionListener(e -> {
                setActiveTab(item.getKey());
                if (onNavigate != null) {
                    onNavigate.accept(item.getKey());
                }
            });

            add(button, gbc);
            gbc.gridy++;
        }

        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    public void setActiveTab(String pageKey) {
        LeftBarButton button = buttonMap.get(pageKey);
        if (button == null) {
            return;
        }

        if (activeButton != null) {
            activeButton.setActive(false);
        }

        button.setActive(true);
        activeButton = button;
    }
}
