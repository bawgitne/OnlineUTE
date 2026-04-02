package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.util.List;

/**
 * SelectGroup - A beautiful custom ComboBox wrapper.
 * Completely overrides native Swing LookAndFeel to achieve a Flat Design appearance.
 */
public class SelectGroup<T> extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JComboBox<T> comboBox;

    public SelectGroup(String labelText, List<T> items) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        setBorder(new RoundedTitleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                18
        ));

        // Create the combo box and populate it
        comboBox = new JComboBox<>();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
        
        // 1. Remove standard border and background
        comboBox.setOpaque(false);
        comboBox.setBackground(FIELD_BACKGROUND);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(new Color(20, 30, 40));
        comboBox.setBorder(BorderFactory.createEmptyBorder());
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
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

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BE");
                button.setFont(new Font("Segoe UI", Font.BOLD, 9));
                button.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 2));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(new Color(120, 140, 160));
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setForeground(new Color(0, 84, 140));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        button.setForeground(new Color(120, 140, 160));
                    }
                });
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

    public T getSelectedValue() {
        return (T) comboBox.getSelectedItem();
    }

    public JComboBox<T> getComboBox() {
        return comboBox;
    }

    public void setSelectedItem(T item) {
        comboBox.setSelectedItem(item);
    }
    
    public void setItems(List<T> items) {
        comboBox.removeAllItems();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
    }
}
