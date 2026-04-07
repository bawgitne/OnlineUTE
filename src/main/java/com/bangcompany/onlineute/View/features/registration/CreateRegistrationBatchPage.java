/**
 * Tạo đợt đăng ký môn
 */
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateRegistrationBatchPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TextInput batchNameInput = new TextInput("Tên đợt đăng ký", false);
    private final TextInput openAtInput = new TextInput("Mở đăng ký (yyyy-MM-dd HH:mm)", false);
    private final TextInput closeAtInput = new TextInput("Đóng đăng ký (yyyy-MM-dd HH:mm)", false);
    private final TextInput commonStartDateInput = new TextInput("Ngày bắt đầu học chung (yyyy-MM-dd)", false);
    private final SelectInput<Term> termSelect = new SelectInput<>("Học kỳ áp dụng", AppContext.getTermService().getAllTerms());
    private final Table batchTable;
    private final JLabel selectedBatchLabel = new JLabel("Chọn một đợt đăng ký ở bảng bên dưới để mở popup tạo lớp học phần.", SwingConstants.LEFT);

    private final List<RegistrationBatch> batchRows = new ArrayList<>();
    private RegistrationBatch selectedBatch;

    // hàm tạo giao diện chính
    public CreateRegistrationBatchPage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        batchTable = new Table(new String[]{"ID", "Tên đợt", "Học kỳ", "Mở đăng ký", "Đóng đăng ký", "Bắt đầu học"}, 12, 44);
        batchTable.setOnRowSelected(index -> {
            selectBatchByIndex(index);
            if (selectedBatch != null) {
                openSectionDialog(); // chọn xong thì mở popup tạo lớp luôn
            }
        });

        add(Card.titleCard("TẠO ĐỢT ĐĂNG KÝ MÔN"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        fillDefaultValues();
        loadBatches();
    }

    private Component createBody() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.add(createFormPanel(), BorderLayout.NORTH);
        container.add(createBatchTablePanel(), BorderLayout.CENTER);
        container.add(createActionPanel(), BorderLayout.SOUTH);
        return container;
    }

    // form nhập thông tin đợt mới
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel hintLabel = new JLabel("<html>Admin khai báo tên đợt, thời gian mở/đóng đăng ký, học kỳ và ngày bắt đầu học chung.<br>Sau đó chọn đợt đăng ký để mở popup quản lý lớp học phần.</html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(new Color(100, 110, 120));
        panel.add(hintLabel, gbc);

        gbc.gridy++;
        panel.add(batchNameInput, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(openAtInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(closeAtInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(termSelect, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(commonStartDateInput, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        Button createButton = new Button("Tạo đợt đăng ký");
        createButton.setPreferredSize(new Dimension(180, 44));
        createButton.addActionListener(e -> createBatch());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createBatchTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        Card title = Card.titleCard("Danh sách đợt đăng ký đã tạo");
        panel.add(title, BorderLayout.NORTH);

        panel.add(batchTable, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        selectedBatchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedBatchLabel.setForeground(new Color(25, 35, 45));
        panel.add(selectedBatchLabel, BorderLayout.WEST);

        Button openDialogButton = new Button("Thêm lớp học phần");
        openDialogButton.setPreferredSize(new Dimension(200, 44));
        openDialogButton.addActionListener(e -> openSectionDialog());
        panel.add(openDialogButton, BorderLayout.EAST);

        return panel;
    }

    // lưu đợt mới vào db
    private void createBatch() {
        try {
            RegistrationBatch batch = new RegistrationBatch();
            batch.setName(batchNameInput.getValue().trim());
            batch.setOpenAt(LocalDateTime.parse(openAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setCloseAt(LocalDateTime.parse(closeAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setTerm(termSelect.getSelectedValue());
            batch.setCommonStartDate(LocalDate.parse(commonStartDateInput.getValue().trim(), DATE_FORMATTER));

            AppContext.getRegistrationBatchController().createBatch(batch);

            JOptionPane.showMessageDialog(this, "Tạo đợt đăng ký thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            fillDefaultValues();
            loadBatches();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được đợt đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // load danh sách các đợt đã có
    private void loadBatches() {
        batchTable.clearRows();
        batchRows.clear();

        for (RegistrationBatch batch : AppContext.getRegistrationBatchController().getAllBatches()) {
            batchRows.add(batch);
            batchTable.addRow(
                    batch.getId(),
                    batch.getName(),
                    batch.getTerm() == null ? "" : batch.getTerm().toString(),
                    formatDateTime(batch.getOpenAt()),
                    formatDateTime(batch.getCloseAt()),
                    formatDate(batch.getCommonStartDate())
            );
        }

        if (!batchRows.isEmpty()) {
            selectBatchByIndex(0);
        }
    }

    private void selectBatchByIndex(int index) {
        if (index < 0 || index >= batchRows.size()) {
            selectedBatch = null;
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên dưới để mở popup tạo lớp học phần.");
            return;
        }
        selectedBatch = batchRows.get(index);
        selectedBatchLabel.setText("Đã chọn: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
    }

    // mở cái popup quản lý lớp học phần cho đợt được chọn
    private void openSectionDialog() {
        if (selectedBatch == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đợt đăng ký trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CourseSectionDialog dialog = new CourseSectionDialog(SwingUtilities.getWindowAncestor(this), selectedBatch);
        dialog.setVisible(true);
    }

    private String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATE_TIME_FORMATTER.format(value);
    }

    private String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMATTER.format(value);
    }

    private void clearForm() {
        batchNameInput.setValue("");
        openAtInput.setValue("");
        closeAtInput.setValue("");
        commonStartDateInput.setValue("");
    }

    // điền mồi mấy cái date để admin đỡ phải gõ nhiều
    private void fillDefaultValues() {
        if (openAtInput.getValue().isBlank()) {
            openAtInput.setValue(LocalDateTime.now().withMinute(0).format(DATE_TIME_FORMATTER));
        }
        if (closeAtInput.getValue().isBlank()) {
            closeAtInput.setValue(LocalDateTime.now().plusDays(7).withMinute(0).format(DATE_TIME_FORMATTER));
        }
        if (commonStartDateInput.getValue().isBlank()) {
            commonStartDateInput.setValue(LocalDate.now().plusWeeks(1).format(DATE_FORMATTER));
        }
    }

    @Override
    public void onEnter() {
        termSelect.setItems(AppContext.getTermService().getAllTerms());
        loadBatches();
        if (selectedBatch == null) {
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên dưới để mở popup tạo lớp học phần.");
        }
    }
}
