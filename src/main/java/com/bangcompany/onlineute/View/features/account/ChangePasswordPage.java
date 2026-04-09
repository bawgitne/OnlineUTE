package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordPage extends JPanel implements Refreshable {
    private final ViewContext viewContext;
    private final TextInput oldPassInput;
    private final TextInput newPassInput;
    private final TextInput confirmPassInput;

    /**
     * Trang thay đổi mật khẩu
     */
    public ChangePasswordPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel form = new Card(26, new Insets(40, 50, 40, 50));//layout
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(500, 450));
        form.setMaximumSize(new Dimension(500, 450));

        JLabel title = new JLabel("ĐỔI MẬT KHẨU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 80, 140));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);

        JLabel subTitle = new JLabel("Nhập mật khẩu mới để bảo mật tài khoản");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(subTitle);

        form.add(Box.createRigidArea(new Dimension(0, 25)));

        oldPassInput = new TextInput("Mật khẩu hiện tại", true);
        newPassInput = new TextInput("Mật khẩu mới", true);
        confirmPassInput = new TextInput("Xác nhận mật khẩu mới", true);

        oldPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(oldPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(newPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(confirmPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        Button btnSave = new Button("Cập nhật mật khẩu");
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.addActionListener(e -> handleChangePassword());
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

    private void handleChangePassword() {
        if (SessionManager.getCurrentAccount() == null) {
            JOptionPane.showMessageDialog(this, "Phiên đăng nhập không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String oldPass = oldPassInput.getValue().trim();
        String newPass = newPassInput.getValue().trim();
        String confirm = confirmPassInput.getValue().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới và mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = viewContext.getAccountController().changePassword(
                SessionManager.getCurrentAccount().getId(),
                oldPass,
                newPass
        );

        if (!ok) {
            JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Mật khẩu đã được cập nhật.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        onEnter();
    }
}
