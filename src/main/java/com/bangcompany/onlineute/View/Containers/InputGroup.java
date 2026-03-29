package com.bangcompany.onlineute.View.Containers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InputGroup extends JPanel {
    private final JTextField textField;

    public InputGroup(String labelText, boolean isPassword) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(44, 62, 80));
        
        textField = isPassword ? new JPasswordField() : new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setBackground(new Color(240, 245, 255));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 240)),
                new EmptyBorder(0, 10, 0, 10)
        ));

        add(label);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(textField);
        add(Box.createRigidArea(new Dimension(0, 15)));
    }

    public String getValue() {
        return textField.getText();
    }
}
