package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateRegistrationBatchPage extends JPanel implements Refreshable {
    private final ViewContext viewContext;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TextInput batchNameInput = new TextInput("Tên đợt đăng ký", false);
    private final TextInput openAtInput = new TextInput("Mở đăng ký (yyyy-MM-dd HH:mm)", false);
    private final TextInput closeAtInput = new TextInput("Đóng đăng ký (yyyy-MM-dd HH:mm)", false);
    private final TextInput commonStartDateInput = new TextInput("Ngày bắt đầu học chung (yyyy-MM-dd)", false);
    private final SelectInput<Term> termSelect = new SelectInput<>("Học kỳ áp dụng", java.util.List.of());
    private final Table batchTable;
    private final JLabel selectedBatchLabel = new JLabel("Chọn một đợt đăng ký ở bảng bên dưới để mở popup tạo lớp học phần.", SwingConstants.LEFT);

    private final List<RegistrationBatch> batchRows = new ArrayList<>();
    private RegistrationBatch selectedBatch;

    public CreateRegistrationBatchPage(ViewContext viewContext) {
        this.viewContext = viewContext;

        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        batchTable = new Table(new String[]{"ID", "Tên đợt", "Học kỳ", "Mở đăng ký", "Đóng đăng ký", "Bắt đầu học"}, 12, 44);
        batchTable.setOnRowSelected(index -> {
            selectBatchByIndex(index);
            if (selectedBatch != null) {
                openSectionManagerDialog();
            }
        });

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
        openDialogButton.addActionListener(e -> openSectionManagerDialog());
        panel.add(openDialogButton, BorderLayout.EAST);

        return panel;
    }

    private void createBatch() {
        try {
            RegistrationBatch batch = new RegistrationBatch();
            batch.setName(batchNameInput.getValue().trim());
            batch.setOpenAt(LocalDateTime.parse(openAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setCloseAt(LocalDateTime.parse(closeAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setTerm(termSelect.getSelectedValue());
            batch.setCommonStartDate(LocalDate.parse(commonStartDateInput.getValue().trim(), DATE_FORMATTER));

            viewContext.getRegistrationBatchController().createBatch(batch);

            ExceptionHandler.showInfo(this, "Tạo đợt đăng ký thành công.", "Thành công");
            clearForm();
            fillDefaultValues();
            loadBatches();
        } catch (Exception ex) {
            ExceptionHandler.showError(this, ex, "Không thể tạo đợt đăng ký.");
        }
    }

    private void loadBatches() {
        batchTable.clearRows();
        batchRows.clear();

        for (RegistrationBatch batch : viewContext.getRegistrationBatchController().getAllBatches()) {
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

    private void openSectionManagerDialog() {
        if (selectedBatch == null) {
            ExceptionHandler.showInfo(this, "Vui lòng chọn một đợt đăng ký trước.", "Thông báo");
            return;
        }

        CourseSectionManagerDialog dialog = new CourseSectionManagerDialog(SwingUtilities.getWindowAncestor(this), selectedBatch, viewContext);
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

    private void fillDefaultValues() {
        if (batchNameInput.getValue().isBlank()) {
            batchNameInput.setValue("Đợt đăng ký mới");
        }
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
        termSelect.setItems(viewContext.getTermController().getAllTerms());
        loadBatches();
        if (selectedBatch == null) {
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên dưới để mở popup tạo lớp học phần.");
        }
    }
}