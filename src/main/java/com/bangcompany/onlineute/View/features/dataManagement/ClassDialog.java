package com.bangcompany.onlineute.View.features.dataManagement;

import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.TextInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ClassDialog extends JPanel {
    private final TextInput nameInput;
    private final SelectInput<Major> majorSelect;
    private final JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    public ClassDialog(List<Major> majors) {
        setLayout(new BorderLayout(0, 16));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        nameInput = new TextInput("Tên lớp", false);
        majorSelect = new SelectInput<>("Ngành", majors);

        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        form.setOpaque(false);
        form.add(nameInput);
        form.add(majorSelect);

        add(form, BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);
    }

    private JPanel buildActions() {
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        leftActions.setOpaque(false);
        rightActions.setOpaque(false);
        actionPanel.add(leftActions, BorderLayout.WEST);
        actionPanel.add(rightActions, BorderLayout.EAST);
        return actionPanel;
    }

    public void setValues(String className, Major major) {
        nameInput.setValue(className == null ? "" : className);
        majorSelect.setSelectedItem(major);
    }

    public String getClassNameValue() {
        return nameInput.getValue().trim();
    }

    public Major getSelectedMajor() {
        return majorSelect.getSelectedValue();
    }

    public void setLeftAction(String label, Color bg, Runnable action) {
        leftActions.removeAll();
        if (label != null) {
            Button button = bg == null ? new Button(label) : new Button(label, bg);
            button.setPreferredSize(new Dimension(120, 40));
            button.addActionListener(e -> action.run());
            leftActions.add(button);
        }
        leftActions.revalidate();
        leftActions.repaint();
    }

    public void setRightAction(String label, Runnable action) {
        rightActions.removeAll();
        if (label != null) {
            Button button = new Button(label);
            button.setPreferredSize(new Dimension(120, 40));
            button.addActionListener(e -> action.run());
            rightActions.add(button);
        }
        rightActions.revalidate();
        rightActions.repaint();
    }
}