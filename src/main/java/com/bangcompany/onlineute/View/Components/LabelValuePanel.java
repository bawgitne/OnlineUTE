package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelValuePanel extends JPanel {
    public LabelValuePanel(String label, String value) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(new Color(70, 86, 107));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueText.setForeground(new Color(33, 37, 41));

        add(labelText, BorderLayout.NORTH);
        add(valueText, BorderLayout.CENTER);
    }
}
