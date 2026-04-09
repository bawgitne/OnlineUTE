package com.bangcompany.onlineute.View.features.dataManagement;

import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.TextInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MajorDialog extends JPanel {
    private final TextInput codeInput;
    private final TextInput nameInput;
    private final TextInput creditInput;
    private final SelectInput<Faculty> facultySelect;
    private final JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    public MajorDialog(List<Faculty> faculties) {
        setLayout(new BorderLayout(0, 16));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        codeInput = new TextInput("Mã ngành", false);
        nameInput = new TextInput("Tên ngành", false);
        creditInput = new TextInput("Tổng tín chỉ", false);
        facultySelect = new SelectInput<>("Khoa", faculties);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);
        form.add(creditInput);
        form.add(facultySelect);

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

    public void setValues(String code, String name, Integer totalCredit, Faculty faculty) {
        codeInput.setValue(code == null ? "" : code);
        nameInput.setValue(name == null ? "" : name);
        creditInput.setValue(totalCredit == null ? "" : String.valueOf(totalCredit));
        facultySelect.setSelectedItem(faculty);
    }

    public String getCodeValue() {
        return codeInput.getValue().trim();
    }

    public String getNameValue() {
        return nameInput.getValue().trim();
    }

    public String getCreditValue() {
        return creditInput.getValue().trim();
    }

    public Faculty getSelectedFaculty() {
        return facultySelect.getSelectedValue();
    }

    public void setCodeEditable(boolean editable) {
        codeInput.setEditable(editable);
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