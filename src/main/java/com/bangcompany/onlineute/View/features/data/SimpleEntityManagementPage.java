package com.bangcompany.onlineute.View.features.data;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleEntityManagementPage<T> extends ManagementShellPage {
    private final Supplier<List<T>> loader;
    private final EntityTablePanel<T> resultPanel;
    private final SimpleManagementDashboard dashboardPanel;
    private List<T> cachedItems = List.of();

    public SimpleEntityManagementPage(String searchPlaceholder,
                                      String summaryTitle,
                                      String guideHtml,
                                      String createButtonLabel,
                                      Runnable onCreateNew,
                                      Supplier<List<T>> loader,
                                      String[] columns,
                                      Function<T, Object[]> rowMapper,
                                      Function<T, String> searchTextMapper) {
        this(searchPlaceholder, summaryTitle, guideHtml, createButtonLabel, onCreateNew, loader,
                new EntityTablePanel<>(columns, rowMapper, searchTextMapper),
                new SimpleManagementDashboard(summaryTitle, guideHtml, () -> {
                    List<T> loadedItems = loader.get();
                    return loadedItems == null ? 0 : loadedItems.size();
                }));
    }

    private SimpleEntityManagementPage(String searchPlaceholder,
                                       String summaryTitle,
                                       String guideHtml,
                                       String createButtonLabel,
                                       Runnable onCreateNew,
                                       Supplier<List<T>> loader,
                                       EntityTablePanel<T> resultPanel,
                                       SimpleManagementDashboard dashboardPanel) {
        super(searchPlaceholder, createButtonLabel, onCreateNew, 2, dashboardPanel, resultPanel);
        this.loader = loader;
        this.resultPanel = resultPanel;
        this.dashboardPanel = dashboardPanel;
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        loadItems();
        resultPanel.showItems(resultPanel.filter(cachedItems, keyword), keyword);
    }

    @Override
    protected void onKeywordCleared() {
        loadItems();
        dashboardPanel.refreshData();
    }

    private void loadItems() {
        List<T> loadedItems = loader.get();
        cachedItems = loadedItems == null ? List.of() : loadedItems;
    }
}
