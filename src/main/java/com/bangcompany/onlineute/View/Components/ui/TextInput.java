/**
 * nhập liệu
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;

import javax.swing.*;
import java.awt.*;

public class TextInput extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JTextField textField;


    public TextInput(String labelText, boolean isPassword) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);
        
        // bo góc
        boolean hasLabel = labelText != null && !labelText.isBlank();
        if (hasLabel) {
            setBorder(RoundedBorders.titleBorder(
                    labelText,
                    BORDER_COLOR,
                    LABEL_COLOR,
                    FIELD_BACKGROUND,
                    new Font("Segoe UI", Font.BOLD, 11),
                    AppTheme.RADIUS_INPUT
            ));
        } else {
            setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_INPUT, new Insets(8, 12, 8, 12)));
        }

        // là pass thì ẩn đi
        textField = isPassword ? new JPasswordField() : new JTextField();
        textField.setOpaque(false);
        textField.setBorder(null);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(new Color(20, 30, 40));
        textField.setCaretColor(new Color(0, 84, 140));

        int height = hasLabel ? 56 : 44;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        setPreferredSize(new Dimension(200, height));

        add(textField, BorderLayout.CENTER);
    }


    public String getValue() {
        return textField.getText();
    }


    public void setValue(String value) {
        textField.setText(value == null ? "" : value);
    }


    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }


    public JTextField getTextField() {
        return textField;
    }
}
