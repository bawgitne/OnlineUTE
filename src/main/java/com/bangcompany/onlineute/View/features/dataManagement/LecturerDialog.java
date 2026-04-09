package com.bangcompany.onlineute.View.features.dataManagement;

import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.View.Components.theme.SwingUtils;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.FormRow;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.TextAreaInput;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LecturerDialog extends JPanel {
    private TextInput codeInput;
    private TextInput fullNameInput;
    private TextInput emailInput;
    private TextInput phoneInput;
    private TextInput dobInput;
    private SelectInput<String> genderSelect;
    private TextInput placeOfBirthInput;
    private TextInput nationalityInput;
    private TextInput citizenIdInput;
    private TextInput citizenIssuePlaceInput;
    private TextInput citizenIssueDateInput;
    private TextAreaInput currentAddressInput;
    private TextAreaInput permanentAddressInput;
    private TextInput contactNameInput;
    private TextInput contactPhoneInput;
    private TextInput contactAddressInput;

    private final JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    public LecturerDialog() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        add(buildContent(), BorderLayout.CENTER);
    }

    private JComponent buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 0, 8, 0));

        codeInput = new TextInput("Mã giảng viên *", false);
        fullNameInput = new TextInput("Họ và tên *", false);
        emailInput = new TextInput("Email *", false);
        phoneInput = new TextInput("Số điện thoại", false);
        dobInput = new TextInput("Ngày sinh (YYYY-MM-DD)", false);
        genderSelect = new SelectInput<>("Giới tính", java.util.List.of("Nam", "Nữ", "Khác"));
        placeOfBirthInput = new TextInput("Nơi sinh", false);
        nationalityInput = new TextInput("Quốc tịch", false);
        citizenIdInput = new TextInput("CCCD/CMND", false);
        citizenIssuePlaceInput = new TextInput("Nơi cấp", false);
        citizenIssueDateInput = new TextInput("Ngày cấp (YYYY-MM-DD)", false);
        currentAddressInput = new TextAreaInput("Địa chỉ hiện tại", 96);
        permanentAddressInput = new TextAreaInput("Địa chỉ thường trú", 96);
        contactNameInput = new TextInput("Người liên hệ", false);
        contactPhoneInput = new TextInput("SDT liên hệ", false);
        contactAddressInput = new TextInput("Địa chỉ liên hệ", false);

        nationalityInput.setValue("Việt Nam");

        content.add(Box.createVerticalStrut(16));
        content.add(createSectionPanel(
                FormRow.two(codeInput, fullNameInput),
                FormRow.two(emailInput, phoneInput)
        ));
        content.add(Box.createVerticalStrut(16));

        content.add(createSectionPanel(
                FormRow.three(dobInput, genderSelect, nationalityInput),
                FormRow.single(placeOfBirthInput)
        ));
        content.add(Box.createVerticalStrut(16));

        content.add(createSectionPanel(
                FormRow.three(citizenIdInput, citizenIssuePlaceInput, citizenIssueDateInput),
                FormRow.single(contactAddressInput),
                FormRow.two(contactNameInput, contactPhoneInput)
        ));
        content.add(Box.createVerticalStrut(16));

        content.add(createSectionPanel(
                FormRow.single(permanentAddressInput),
                FormRow.single(currentAddressInput)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createActionBar());

        return SwingUtils.hiddenScrollPane(content);
    }

    private JPanel createActionBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(com.bangcompany.onlineute.View.Components.theme.RoundedBorders.paddedOutline(
                com.bangcompany.onlineute.View.Components.theme.AppTheme.RADIUS_PANEL,
                new Insets(14, 18, 14, 18)
        ));
        bar.setOpaque(true);

        leftActions.setOpaque(false);
        rightActions.setOpaque(false);
        bar.add(leftActions, BorderLayout.WEST);
        bar.add(rightActions, BorderLayout.EAST);
        wrapper.add(bar, BorderLayout.CENTER);
        return wrapper;
    }

    private Card createSectionPanel(Component... rows) {
        Card section = new Card();
        section.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        for (int i = 0; i < rows.length; i++) {
            Component row = rows[i];
            if (row != null) {
                content.add(row);
                if (i < rows.length - 1) {
                    content.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }
        }
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    public void setValues(Lecturer lecturer) {
        if (lecturer == null) {
            return;
        }
        codeInput.setValue(lecturer.getCode());
        fullNameInput.setValue(lecturer.getFullName());
    }

    public void setCodeEditable(boolean editable) {
        codeInput.setEditable(editable);
    }

    public String getCodeValue() {
        return codeInput.getValue().trim();
    }

    public String getFullNameValue() {
        return fullNameInput.getValue().trim();
    }

    public String getEmailValue() {
        return emailInput.getValue().trim();
    }

    public String getPhoneValue() {
        return phoneInput.getValue().trim();
    }

    public String getDobValue() {
        return dobInput.getValue().trim();
    }

    public String getGenderValue() {
        return genderSelect.getSelectedValue();
    }

    public String getPlaceOfBirthValue() {
        return placeOfBirthInput.getValue().trim();
    }

    public String getNationalityValue() {
        return nationalityInput.getValue().trim();
    }

    public String getCitizenIdValue() {
        return citizenIdInput.getValue().trim();
    }

    public String getCitizenIssuePlaceValue() {
        return citizenIssuePlaceInput.getValue().trim();
    }

    public String getCitizenIssueDateValue() {
        return citizenIssueDateInput.getValue().trim();
    }

    public String getCurrentAddressValue() {
        return currentAddressInput.getValue().trim();
    }

    public String getPermanentAddressValue() {
        return permanentAddressInput.getValue().trim();
    }

    public String getContactNameValue() {
        return contactNameInput.getValue().trim();
    }

    public String getContactPhoneValue() {
        return contactPhoneInput.getValue().trim();
    }

    public String getContactAddressValue() {
        return contactAddressInput.getValue().trim();
    }

    public void setLeftAction(String label, Color bg, Runnable action) {
        leftActions.removeAll();
        if (label != null) {
            Button button = bg == null ? new Button(label) : new Button(label, bg);
            button.setPreferredSize(new Dimension(160, 42));
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
            button.setPreferredSize(new Dimension(170, 42));
            button.addActionListener(e -> action.run());
            rightActions.add(button);
        }
        rightActions.revalidate();
        rightActions.repaint();
    }

    public void resetForm() {
        if (codeInput != null) codeInput.setValue("");
        if (fullNameInput != null) fullNameInput.setValue("");
        if (emailInput != null) emailInput.setValue("");
        if (phoneInput != null) phoneInput.setValue("");
        if (dobInput != null) dobInput.setValue("");
        if (placeOfBirthInput != null) placeOfBirthInput.setValue("");
        if (nationalityInput != null) nationalityInput.setValue("Việt Nam");
        if (citizenIdInput != null) citizenIdInput.setValue("");
        if (citizenIssuePlaceInput != null) citizenIssuePlaceInput.setValue("");
        if (citizenIssueDateInput != null) citizenIssueDateInput.setValue("");
        if (currentAddressInput != null) currentAddressInput.setValue("");
        if (permanentAddressInput != null) permanentAddressInput.setValue("");
        if (contactNameInput != null) contactNameInput.setValue("");
        if (contactPhoneInput != null) contactPhoneInput.setValue("");
        if (contactAddressInput != null) contactAddressInput.setValue("");
    }
}