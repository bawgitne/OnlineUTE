package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.SearchActionTopbar;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class ManagementShellPage extends JPanel implements Refreshable {
    private static final String DASHBOARD_CARD = "dashboard";
    private static final String RESULT_CARD = "result";

    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    private final SearchActionTopbar topbar;
    private final JComponent dashboardPanel;
    private final JComponent resultPanel;
    private final int minSearchLength;

    protected ManagementShellPage(String placeholder,
                                  String createButtonLabel,
                                  Runnable onCreateNew,
                                  int minSearchLength,
                                  JComponent dashboardPanel,
                                  JComponent resultPanel) {
        this.minSearchLength = Math.max(1, minSearchLength);
        this.dashboardPanel = dashboardPanel;
        this.resultPanel = resultPanel;

        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        topbar = new SearchActionTopbar(
                placeholder,
                createButtonLabel,
                this.minSearchLength,
                this::handleSearchChanged,
                onCreateNew
        );

        contentPanel.setOpaque(false);
        contentPanel.add(dashboardPanel, DASHBOARD_CARD);
        contentPanel.add(resultPanel, RESULT_CARD);

        add(topbar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }

    @Override
    public void onEnter() {
        String keyword = topbar.getKeyword();
        if (!isSearchReady(keyword)) {
            onKeywordCleared();
            showDashboard();
            return;
        }
        onKeywordActivated(keyword.trim());
        showResults();
    }

    protected abstract void onKeywordActivated(String keyword);

    protected abstract void onKeywordCleared();

    protected boolean isSearchReady(String keyword) {
        return keyword != null && keyword.trim().length() >= minSearchLength;
    }

    protected void showDashboard() {
        contentLayout.show(contentPanel, DASHBOARD_CARD);
    }

    protected void showResults() {
        contentLayout.show(contentPanel, RESULT_CARD);
    }

    protected String getKeyword() {
        return topbar.getKeyword();
    }

    protected void configureCreateAction(String label, Runnable action) {
        topbar.setCreateAction(label, action);
    }

    protected JComponent getDashboardComponent() {
        return dashboardPanel;
    }

    protected JComponent getResultComponent() {
        return resultPanel;
    }

    private void handleSearchChanged(String keyword) {
        if (!isSearchReady(keyword)) {
            onKeywordCleared();
            showDashboard();
            return;
        }
        onKeywordActivated(keyword.trim());
        showResults();
    }
}
