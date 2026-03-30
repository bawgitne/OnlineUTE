package com.bangcompany.onlineute.view.layout;

import com.bangcompany.onlineute.View.Pages.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainContent extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JPanel> pages = new HashMap<>();

    public MainContent() {
        setLayout(cardLayout);
    }

    public void registerPage(String pageKey, JPanel page) {
        if (page == null || pages.containsKey(pageKey)) {
            return;
        }

        pages.put(pageKey, page);
        add(page, pageKey);
    }

    public void showPage(String pageKey) {
        JPanel currentPage = pages.get(pageKey);
        if (currentPage == null) {
            return;
        }

        cardLayout.show(this, pageKey);
        if (currentPage instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }
}
