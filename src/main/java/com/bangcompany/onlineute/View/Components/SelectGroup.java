package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.util.List;

/**
 * SelectGroup - A beautiful custom ComboBox wrapper.
 * Completely overrides native Swing LookAndFeel to achieve a Flat Design appearance.
 */
public class SelectGroup<T> extends JPanel {
    private final JComboBox<T> comboBox;

    public SelectGroup(String labelText, List<T> items) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(240, 245, 255)); // Light blue tint

        // TitledBorder overlapping a clear gray/blue border
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(160, 180, 200), 1, true),
                labelText,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 12),
                new Color(100, 120, 140)
        );

        setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                new EmptyBorder(2, 5, 5, 5) // inner padding
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
        comboBox.setBackground(new Color(240, 245, 255));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboBox.setForeground(new Color(20, 30, 40));
        comboBox.setBorder(BorderFactory.createEmptyBorder());

        // 2. Override the native UI drawing to completely strip 3D borders and system arrows
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                // Custom Flat Arrow Button
                JButton button = new JButton("\u25BC"); // Unicode Down Arrow
                button.setFont(new Font("Segoe UI", Font.BOLD, 10));
                button.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(new Color(120, 140, 160));
                
                // Hover effect for the arrow
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
        });

        // 3. Force the internal text editor/renderer to be transparent
        if (comboBox.getRenderer() instanceof JComponent) {
            ((JComponent) comboBox.getRenderer()).setOpaque(false);
        }

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setPreferredSize(new Dimension(200, 50));

        add(comboBox, BorderLayout.CENTER);
    }

    public T getSelectedValue() {
        return (T) comboBox.getSelectedItem();
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
