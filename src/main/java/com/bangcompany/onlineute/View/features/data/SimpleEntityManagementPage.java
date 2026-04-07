/**
 * Giao diện quản lý chung
 */
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.container.EntityTablePanel;
import com.bangcompany.onlineute.View.Components.container.ManagementShellPage;
import com.bangcompany.onlineute.View.Components.container.Dashboard;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleEntityManagementPage<T> extends ManagementShellPage {
    private static final int DASHBOARD_THRESHOLD = 20; // ít hơn 20 bản ghi thì hiện table ngay
    private final Supplier<List<T>> loader;
    private final EntityTablePanel<T> resultPanel;
    private final Dashboard dashboardPanel;
    private List<T> cachedItems = List.of();

    // cấu hình các tham số hiển thị cho entity
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
                new Dashboard(summaryTitle, guideHtml, () -> {
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
                                       Dashboard dashboardPanel) {
        super(searchPlaceholder, createButtonLabel, onCreateNew, 2, dashboardPanel, resultPanel);
        this.loader = loader;
        this.resultPanel = resultPanel;
        this.dashboardPanel = dashboardPanel;
    }

    public void setSelectionHandler(Consumer<T> onItemSelected) {
        resultPanel.setSelectionHandler(onItemSelected);
    }

    public void setDetailPanel(JComponent panel) {
        resultPanel.setDetailPanel(panel);
    }

    // tìm khi gõ
    @Override
    protected void onKeywordActivated(String keyword) {
        loadItems();
        resultPanel.showItems(resultPanel.filter(cachedItems, keyword), keyword);
    }

    // reload khi xóa tìm kiếm
    @Override
    protected void onKeywordCleared() {
        loadItems();
        dashboardPanel.refreshData();
        if (shouldShowResultsOnEmpty()) {
            resultPanel.showItems(cachedItems, "");
        }
    }

    // quyết định hiện dashboard hay hiện table
    @Override
    protected void showDefaultWhenNoSearch() {
        if (shouldShowResultsOnEmpty()) {
            showResults();
        } else {
            showDashboard();
        }
    }

    private void loadItems() {
        List<T> loadedItems = loader.get();
        cachedItems = loadedItems == null ? List.of() : loadedItems;
    }

    private boolean shouldShowResultsOnEmpty() {
        return cachedItems.size() < DASHBOARD_THRESHOLD;
    }
}
