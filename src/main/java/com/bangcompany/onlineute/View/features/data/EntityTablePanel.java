package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityTablePanel<T> extends JPanel implements Refreshable {
    private final Function<T, Object[]> rowMapper;
    private final Function<T, String> searchTextMapper;
    private final DefaultTableModel tableModel;
    private final JLabel resultLabel = new JLabel("0 ket qua");

    private List<T> items = new ArrayList<>();

    public EntityTablePanel(String[] columns,
                            Function<T, Object[]> rowMapper,
                            Function<T, String> searchTextMapper) {
        this.rowMapper = rowMapper;
        this.searchTextMapper = searchTextMapper;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 85, 141));
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        com.bangcompany.onlineute.View.Components.TableStyles.applyModernTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        com.bangcompany.onlineute.View.Components.TableStyles.styleScrollPane(scrollPane);

        add(resultLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        showItems(items, "");
    }

    public void showItems(List<T> loadedItems, String keyword) {
        items = loadedItems == null ? new ArrayList<>() : new ArrayList<>(loadedItems);
        tableModel.setRowCount(0);
        for (T item : items) {
            tableModel.addRow(rowMapper.apply(item));
        }
        if (keyword == null || keyword.isBlank()) {
            resultLabel.setText(items.size() + " ban ghi");
            return;
        }
        resultLabel.setText(items.size() + " ket qua cho: " + keyword.trim());
    }

    public List<T> filter(List<T> sourceItems, String keyword) {
        if (sourceItems == null) {
            return List.of();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(sourceItems);
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
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
}
