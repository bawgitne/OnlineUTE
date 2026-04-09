package com.bangcompany.onlineute.View.Components.container;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class EntityTablePanel<T> extends JPanel implements Refreshable {
    private final Function<T, Object[]> rowMapper;
    private final Function<T, String> searchTextMapper;
    private final JLabel resultLabel = new JLabel("0 ket qua");

    private List<T> items = new ArrayList<>();

    private IntConsumer onPageRequested;
    private final Table table;
    private Consumer<T> onItemSelected;
    private JComponent detailPanel;

    public EntityTablePanel(String[] columns,
                            Function<T, Object[]> rowMapper,
                            Function<T, String> searchTextMapper) {
        this.rowMapper = rowMapper;
        this.searchTextMapper = searchTextMapper;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(AppTheme.PRIMARY_BLUE);
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        add(resultLabel, BorderLayout.NORTH);
        table = new Table(columns, AppTheme.RADIUS_TABLE, 44);
        add(table, BorderLayout.CENTER);

        table.setOnRowSelected(index -> {
            if (onItemSelected == null) {
                return;
            }
            if (index < 0 || index >= items.size()) {
                return;
            }
            onItemSelected.accept(items.get(index));
        });
    }

    public EntityTablePanel(String[] columns,
                            Function<T, Object[]> rowMapper,
                            Function<T, String> searchTextMapper,
                            IntConsumer onPageRequested) {
        this(columns, rowMapper, searchTextMapper);
        this.onPageRequested = onPageRequested;

        table.setOnPageChange(step -> {
            if (this.onPageRequested != null) {
                this.onPageRequested.accept(step);
            }
        });
    }

    @Override
    public void onEnter() {
        showItems(items, items == null ? 0 : items.size(), "");
    }

    public void showItems(List<T> loadedItems, String keyword) {
        items = loadedItems == null ? new ArrayList<>() : new ArrayList<>(loadedItems);
        table.clearRows();
        for (T item : items) {
            table.addRow(rowMapper.apply(item));
        }
        updateResultLabel(items.size(), keyword);
    }

    public void showItems(List<T> loadedItems, long totalItems, String keyword) {
        items = loadedItems == null ? new ArrayList<>() : new ArrayList<>(loadedItems);
        table.clearRows();
        for (T item : items) {
            table.addRow(rowMapper.apply(item));
        }
        updateResultLabel(totalItems, keyword);
    }

    public List<T> filter(List<T> sourceItems, String keyword) {
        if (sourceItems == null) {
            return List.of();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(sourceItems);
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
        if ("all".equals(normalizedKeyword)) {
            return new ArrayList<>(sourceItems);
        }
        List<T> filteredItems = new ArrayList<>();
        for (T item : sourceItems) {
            String searchText = searchTextMapper.apply(item);
            String normalizedText = searchText == null ? "" : searchText.toLowerCase();
            if (normalizedText.contains(normalizedKeyword)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    public void setPageHandler(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
    }

    public void setSelectionHandler(Consumer<T> onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    public void setDetailPanel(JComponent panel) {
        if (detailPanel != null) {
            remove(detailPanel);
        }
        detailPanel = panel;
        if (detailPanel != null) {
            add(detailPanel, BorderLayout.SOUTH);
        }
        revalidate();
        repaint();
    }

    public void updatePagination(int page, int totalPages, boolean hasPrevious, boolean hasNext) {
        table.setPageState(page, totalPages);
    }

    public void updateResultLabel(long totalItems, String keyword) {
        String safeKeyword = keyword == null ? "" : keyword.trim();
        if (safeKeyword.isBlank() || "all".equalsIgnoreCase(safeKeyword)) {
            resultLabel.setText(totalItems + " ban ghi");
            return;
        }
        resultLabel.setText(totalItems + " ket qua cho: " + safeKeyword);
    }
}
