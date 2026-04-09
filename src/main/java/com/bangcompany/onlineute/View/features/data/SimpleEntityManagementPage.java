/**
 * Common management page
 */
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.container.EntityTablePanel;
import com.bangcompany.onlineute.View.Components.container.ManagementShellPage;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Model.DTO.PagedResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class SimpleEntityManagementPage<T> extends ManagementShellPage {
    private static final int DASHBOARD_THRESHOLD = 20;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final PageLoader<T> pageLoader;
    private final LongSupplier countSupplier;
    private final EntityTablePanel<T> resultPanel;
    private final SummaryPanel summaryPanel;
    private List<T> cachedItems = List.of();
    private String currentKeyword = "";
    private int currentPage = 1;
    private int totalPages = 1;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private long totalItems = 0;

    public SimpleEntityManagementPage(String searchPlaceholder,
                                      String summaryTitle,
                                      String guideHtml,
                                      String createButtonLabel,
                                      Runnable onCreateNew,
                                      PageLoader<T> pageLoader,
                                      LongSupplier countSupplier,
                                      String[] columns,
                                      Function<T, Object[]> rowMapper,
                                      Function<T, String> searchTextMapper) {
        this(searchPlaceholder, summaryTitle, guideHtml, createButtonLabel, onCreateNew, pageLoader,
                new EntityTablePanel<>(columns, rowMapper, searchTextMapper),
                new SummaryPanel(summaryTitle, countSupplier));
    }

    private SimpleEntityManagementPage(String searchPlaceholder,
                                       String summaryTitle,
                                       String guideHtml,
                                       String createButtonLabel,
                                       Runnable onCreateNew,
                                       PageLoader<T> pageLoader,
                                       EntityTablePanel<T> resultPanel,
                                       SummaryPanel summaryPanel) {
        super(searchPlaceholder, createButtonLabel, onCreateNew, 2, summaryPanel, resultPanel);
        this.pageLoader = pageLoader;
        this.countSupplier = summaryPanel.supplier;
        this.resultPanel = resultPanel;
        this.summaryPanel = summaryPanel;

        this.resultPanel.setPageHandler(this::handlePageChange);
    }

    public void setSelectionHandler(Consumer<T> onItemSelected) {
        resultPanel.setSelectionHandler(onItemSelected);
    }

    public void setDetailPanel(JComponent panel) {
        resultPanel.setDetailPanel(panel);
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword == null ? "" : keyword.trim();
        currentPage = 1;
        loadPage(getEffectiveKeyword(currentKeyword), currentPage);
        showResults();
    }

    @Override
    protected void onKeywordCleared() {
        currentKeyword = "";
        currentPage = 1;
        summaryPanel.refreshData();
        if (shouldShowResultsOnEmpty()) {
            loadPage("all", currentPage);
            showResults();
        }
    }

    @Override
    protected void showDefaultWhenNoSearch() {
        if (shouldShowResultsOnEmpty()) {
            loadPage("all", 1);
            showResults();
        } else {
            showDashboard();
        }
    }

    private boolean shouldShowResultsOnEmpty() {
        long total = countSupplier.getAsLong();
        totalItems = total;
        return total < DASHBOARD_THRESHOLD;
    }

    private void loadPage(String keyword, int page) {
        PagedResult<T> result = pageLoader.load(keyword, page, pageSize);
        if (result == null) {
            cachedItems = List.of();
            totalItems = 0;
            totalPages = 1;
            currentPage = 1;
            resultPanel.showItems(cachedItems, totalItems, keyword);
            resultPanel.updatePagination(currentPage, totalPages, false, false);
            return;
        }
        cachedItems = result.getItems() == null ? List.of() : result.getItems();
        totalItems = result.getTotalItems();
        totalPages = Math.max(1, result.getTotalPages());
        currentPage = result.getPage();

        resultPanel.showItems(cachedItems, totalItems, keyword);
        resultPanel.updatePagination(currentPage, totalPages, result.hasPrevious(), result.hasNext());
    }

    private void handlePageChange(int step) {
        int targetPage = currentPage + step;
        if (targetPage < 1 || targetPage > totalPages) {
            return;
        }
        loadPage(getEffectiveKeyword(currentKeyword), targetPage);
    }

    private String getEffectiveKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return "all";
        }
        return keyword;
    }

    @FunctionalInterface
    public interface PageLoader<T> {
        PagedResult<T> load(String keyword, int page, int pageSize);
    }

    private static final class SummaryPanel extends JPanel {
        private final JLabel valueLabel = new JLabel("0");
        private final LongSupplier supplier;

        private SummaryPanel(String title, LongSupplier supplier) {
            this.supplier = supplier;
            setOpaque(false);
            setLayout(new BorderLayout());

            Card card = new Card();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(AppTheme.FONT_BODY);
            titleLabel.setForeground(new Color(96, 110, 126));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            valueLabel.setForeground(AppTheme.PRIMARY_BLUE);
            valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            card.add(titleLabel);
            card.add(Box.createVerticalStrut(12));
            card.add(valueLabel);

            add(card, BorderLayout.NORTH);
            refreshData();
        }

        private void refreshData() {
            valueLabel.setText(String.valueOf(supplier.getAsLong()));
        }
    }
}
