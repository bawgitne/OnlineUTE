/**
 * ô nhập chia 1 2 3
 */
package com.bangcompany.onlineute.View.Components.ui;

import javax.swing.*;
import java.awt.*;

public final class FormRow {
    private FormRow() {}


    public static JPanel two(Component left, Component right) {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.add(left);
        row.add(right);
        return row;
    }


    public static JPanel three(Component left, Component middle, Component right) {
        JPanel row = new JPanel(new GridLayout(1, 3, 14, 0));
        row.setOpaque(false);
        row.add(left);
        row.add(middle);
        row.add(right);
        return row;
    }


    public static JPanel single(Component component) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(component, BorderLayout.CENTER);
        return row;
    }
}
