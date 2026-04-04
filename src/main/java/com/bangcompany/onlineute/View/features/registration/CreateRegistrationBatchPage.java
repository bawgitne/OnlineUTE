package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateRegistrationBatchPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final InputGroup batchNameInput = new InputGroup("Tên đợt đăng ký", false);
    private final InputGroup openAtInput = new InputGroup("Mở đăng ký (yyyy-MM-dd HH:mm)", false);
    private final InputGroup closeAtInput = new InputGroup("Đóng đăng ký (yyyy-MM-dd HH:mm)", false);
    private final InputGroup commonStartDateInput = new InputGroup("Ngày bắt đầu học chung (yyyy-MM-dd)", false);
    private final SelectGroup<Term> termSelect = new SelectGroup<>("Học kỳ áp dụng", AppContext.getTermService().getAllTerms());
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tên đợt", "Học kỳ", "Mở đăng ký", "Đóng đăng ký", "Bắt đầu học"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable batchTable = new JTable(tableModel);
    private final JLabel selectedBatchLabel = new JLabel("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.", SwingConstants.LEFT);

    private RegistrationBatch selectedBatch;

    public CreateRegistrationBatchPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        PageScaffold scaffold = new PageScaffold("TẠO ĐỢT ĐĂNG KÝ MÔN");
        scaffold.setBody(createBody());
        add(scaffold, BorderLayout.CENTER);

        configureTable();
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

        JLabel hintLabel = new JLabel("<html>Admin khai báo tên đợt, thời gian mở đóng đăng ký, học kỳ và ngày bắt đầu học chung.<br>Sau đó chọn đợt đăng ký để mở popup quản lý lớp học phần.</html>");
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

        PrimaryButton createButton = new PrimaryButton("Tạo đợt đăng ký");
        createButton.setPreferredSize(new Dimension(180, 44));
        createButton.addActionListener(e -> createBatch());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createBatchTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Danh sách đợt đăng ký đã tạo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(batchTable);
        TableStyles.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        selectedBatchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedBatchLabel.setForeground(new Color(25, 35, 45));
        panel.add(selectedBatchLabel, BorderLayout.WEST);

        PrimaryButton openDialogButton = new PrimaryButton("Thêm lớp học phần");
        openDialogButton.setPreferredSize(new Dimension(200, 44));
        openDialogButton.addActionListener(e -> openSectionDialog());
        panel.add(openDialogButton, BorderLayout.EAST);

        return panel;
    }

    private void configureTable() {
        batchTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        batchTable.setSelectionBackground(new Color(223, 236, 255));
        batchTable.setSelectionForeground(new Color(30, 30, 30));
        batchTable.setGridColor(new Color(230, 235, 240));
        batchTable.setFillsViewportHeight(true);
        batchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableStyles.applyModernTable(batchTable);
        TableStyles.centerColumns(batchTable, 0, 3, 4, 5);
        batchTable.getSelectionModel().addListSelectionListener(this::onBatchTableSelectionChanged);
        batchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    SwingUtilities.invokeLater(() -> {
                        if (selectedBatch != null) {
                            openSectionDialog();
                        }
                    });
                }
            }
        });
    }

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

    private void loadBatches() {
        tableModel.setRowCount(0);
        for (RegistrationBatch batch : AppContext.getRegistrationBatchController().getAllBatches()) {
            tableModel.addRow(new Object[]{
                    batch.getId(),
                    batch.getName(),
                    batch.getTerm() == null ? "" : batch.getTerm().toString(),
                    formatDateTime(batch.getOpenAt()),
                    formatDateTime(batch.getCloseAt()),
                    formatDate(batch.getCommonStartDate())
            });
        }
        if (tableModel.getRowCount() > 0 && batchTable.getSelectedRow() < 0) {
            batchTable.setRowSelectionInterval(0, 0);
        }
    }

    private void onBatchTableSelectionChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = batchTable.getSelectedRow();
        if (selectedRow < 0) {
            selectedBatch = null;
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.");
            return;
        }

        Long batchId = ((Number) tableModel.getValueAt(selectedRow, 0)).longValue();
        selectedBatch = AppContext.getRegistrationBatchController().getBatchById(batchId).orElse(null);
        if (selectedBatch == null) {
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.");
            return;
        }
        selectedBatchLabel.setText("Đã chọn: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
    }

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
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.");
        }
    }
}
