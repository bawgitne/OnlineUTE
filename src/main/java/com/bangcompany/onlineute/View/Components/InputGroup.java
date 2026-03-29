package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InputGroup extends JPanel {
    private final JTextField textField;

    public InputGroup(String labelText, boolean isPassword) {
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

        textField = isPassword ? new JPasswordField() : new JTextField();
        textField.setOpaque(false); // Make it transparent to show panel's background
        textField.setBorder(null); // Remove default borders
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textField.setForeground(new Color(20, 30, 40));

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setPreferredSize(new Dimension(200, 50));

        add(textField, BorderLayout.CENTER);
    }

    public String getValue() {
        return textField.getText();
    }

    public void setValue(String value) {
        textField.setText(value == null ? "" : value);
    }
}
