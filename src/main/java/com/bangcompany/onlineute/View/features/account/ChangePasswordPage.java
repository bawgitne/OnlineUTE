package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordPage extends JPanel implements Refreshable {
    private final InputGroup oldPassInput;
    private final InputGroup newPassInput;
    private final InputGroup confirmPassInput;

    public ChangePasswordPage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("DOI MAT KHAU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));
        form.setPreferredSize(new Dimension(500, 450));
        form.setMaximumSize(new Dimension(500, 450));

        JLabel title = new JLabel("DOI MAT KHAU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 80, 140));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);

        JLabel subTitle = new JLabel("Nhap mat khau moi de bao mat tai khoan");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(subTitle);

        form.add(Box.createRigidArea(new Dimension(0, 25)));

        oldPassInput = new InputGroup("Mat khau hien tai", true);
        newPassInput = new InputGroup("Mat khau moi", true);
        confirmPassInput = new InputGroup("Xac nhan mat khau moi", true);

        oldPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(oldPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(newPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(confirmPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        PrimaryButton btnSave = new PrimaryButton("Cap nhat mat khau");
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Chuc nang doi mat khau dang duoc phat trien.",
                "Thong bao",
                JOptionPane.INFORMATION_MESSAGE
        ));
        form.add(btnSave);

        centerPanel.add(form);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        oldPassInput.setValue("");
        newPassInput.setValue("");
        confirmPassInput.setValue("");
    }
}
