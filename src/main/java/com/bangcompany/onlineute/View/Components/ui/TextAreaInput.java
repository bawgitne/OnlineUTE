/**
 * ô nhập dữ liệu
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class TextAreaInput extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JTextArea textArea;


    public TextAreaInput(String labelText, int preferredHeight) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        // cho tí bo góc đường viền
        setBorder(RoundedBorders.titleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                AppTheme.RADIUS_INPUT
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

        // cho cuộn khi quá cao
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        setPreferredSize(new Dimension(200, preferredHeight));

        add(scrollPane, BorderLayout.CENTER);
    }

    // lấy data
    public String getValue() {
        return textArea.getText();
    }

    // đặt trống để tái sử dụng
    public void setValue(String value) {
        textArea.setText(value == null ? "" : value);
    }

}
