package com.bangcompany.onlineute.View.Containers;

import com.bangcompany.onlineute.View.Factory.PageFactory;
import com.bangcompany.onlineute.View.Pages.Refreshable;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MainView - Lazy load pages and handle refreshable content.
 */
public class MainView extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JPanel> pageCache = new HashMap<>();

    public MainView() {
        setLayout(cardLayout);
    }

    public void showPage(String pageKey) {
        if (!pageCache.containsKey(pageKey)) {
            JPanel page = PageFactory.create(pageKey);
            add(page, pageKey);
            pageCache.put(pageKey, page);
        }

        cardLayout.show(this, pageKey);

        // Refresh logic
        JPanel currentPage = pageCache.get(pageKey);
        if (currentPage instanceof Refreshable r) {
            r.onEnter();
        }
    }
}
