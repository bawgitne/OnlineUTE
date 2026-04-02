package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class TextAreaGroup extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JTextArea textArea;

    public TextAreaGroup(String labelText, int preferredHeight) {
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

        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(FIELD_BACKGROUND);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setForeground(new Color(20, 30, 40));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);
        textArea.setCaretColor(new Color(0, 84, 140));

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
