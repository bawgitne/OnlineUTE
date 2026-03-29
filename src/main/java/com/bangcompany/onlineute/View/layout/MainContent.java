package com.bangcompany.onlineute.view.layout;

import com.bangcompany.onlineute.View.Pages.Refreshable;
import com.bangcompany.onlineute.view.navigation.PageRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainContent extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JPanel> pageCache = new HashMap<>();

    public MainContent() {
        setLayout(cardLayout);
    }

    public void showPage(String pageKey) {
        if (!pageCache.containsKey(pageKey)) {
            JPanel page = PageRegistry.create(pageKey);
            add(page, pageKey);
            pageCache.put(pageKey, page);
        }

        cardLayout.show(this, pageKey);

        JPanel currentPage = pageCache.get(pageKey);
        if (currentPage instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }
}
