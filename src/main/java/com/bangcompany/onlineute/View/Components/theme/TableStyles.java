/**
 * TableStyles.java
 * Công dụng: Cung cấp các phương thức để áp dụng phong cách (styling) hiện đại cho các bảng JTable trong ứng dụng.
 */
package com.bangcompany.onlineute.View.Components.theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class TableStyles {
    private TableStyles() {}

    // Bí danh để gọi phương thức áp dụng giao diện bảng hiện đại
    public static void applyBlueHeader(JTable table) {
        applyModernTable(table);
    }

    // Thiết lập phong cách hiện đại cho bảng: màu tiêu đề, chiều cao hàng, màu nền xen kẽ và hiệu ứng chọn hàng
    public static void applyModernTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(12, 82, 140));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 36));

        table.setRowHeight(34);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(225, 236, 250));
        table.setSelectionForeground(new Color(17, 51, 88));
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Tùy chỉnh renderer cho các ô để tạo hiệu ứng màu nền xen kẽ (zebra striping)
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 251, 255));
                    c.setForeground(new Color(25, 40, 60));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        base.setOpaque(true);
        table.setDefaultRenderer(Object.class, base);
    }

    // Áp dụng đường viền bo góc và cấu hình khung nhìn (viewport) cho khung cuộn chứa bảng
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                RoundedBorders.outline(AppTheme.BORDER_LIGHT, AppTheme.RADIUS_PANEL, new Insets(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder()
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    // Căn giữa dữ liệu cho các cột cụ thể được chỉ định bằng mảng chỉ số
    public static void centerColumns(JTable table, int... columns) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setOpaque(true);
        for (int c : columns) {
            table.getColumnModel().getColumn(c).setCellRenderer(center);
        }
    }
}
