/**
 * chọn droplist
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.util.List;

public class SelectInput<T> extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JComboBox<T> comboBox;

    // ô hiển thị và text trong droplist
    public SelectInput(String labelText, List<T> items) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        //cho nó bo góc
        setBorder(RoundedBorders.titleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                AppTheme.RADIUS_INPUT
        ));

        comboBox = new JComboBox<>();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
        
        // làm tí giao diện vô cho ó
        comboBox.setOpaque(false);
        comboBox.setBackground(FIELD_BACKGROUND);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(new Color(20, 30, 40));
        comboBox.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        comboBox.setFocusable(false);

        // hiện thị mấy option, 1 option là 1 label mới
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (index == -1) {
                    label.setBackground(FIELD_BACKGROUND);
                    label.setForeground(new Color(20, 30, 40));
                } else if (isSelected) {
                    label.setBackground(new Color(229, 239, 252));
                    label.setForeground(new Color(0, 84, 140));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(20, 30, 40));
                }
                return label;
            }
        });

        // Thay đổi giao diện cho nút mũi tên và khung đổ xuống của ComboBox
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                // Trả về một nút không có kích thước để ẩn mũi tên
                JButton button = new JButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setVisible(false);
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                popup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                return popup;
            }
        });

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        setPreferredSize(new Dimension(200, 56));

        add(comboBox, BorderLayout.CENTER);
    }

    // Lấy đối tượng đang được chọn hiện tại
    public T getSelectedValue() {
        return (T) comboBox.getSelectedItem();
    }

    // Trả về đối tượng JComboBox nội bộ
    public JComboBox<T> getComboBox() {
        return comboBox;
    }

    // Đặt một mục làm đối tượng được chọn
    public void setSelectedItem(T item) {
        comboBox.setSelectedItem(item);
    }
    
    // Xóa toàn bộ và cập nhật lại danh sách lựa chọn mới
    public void setItems(List<T> items) {
        comboBox.removeAllItems();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
    }
}
