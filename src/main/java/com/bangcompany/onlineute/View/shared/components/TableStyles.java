package com.bangcompany.onlineute.View.shared.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public final class TableStyles {
    private TableStyles() {}

    public static void applyBlueHeader(JTable table) {
        table.getTableHeader().setBackground(new Color(0, 84, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setRowHeight(35);
    }

    public static void centerColumns(JTable table, int... columns) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c : columns) {
            table.getColumnModel().getColumn(c).setCellRenderer(center);
        }
    }
}
