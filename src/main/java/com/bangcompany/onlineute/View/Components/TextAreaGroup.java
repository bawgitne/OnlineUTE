package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TextAreaGroup extends JPanel {
    private final JTextArea textArea;

    public TextAreaGroup(String labelText, int preferredHeight) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(240, 245, 255)); // Matches InputGroup

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
                new EmptyBorder(5, 5, 5, 5)
        ));

        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(new Color(240, 245, 255));
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textArea.setForeground(new Color(20, 30, 40));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        setPreferredSize(new Dimension(200, preferredHeight));

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getValue() {
        return textArea.getText();
    }

    public void setValue(String value) {
        textArea.setText(value == null ? "" : value);
    }
}
