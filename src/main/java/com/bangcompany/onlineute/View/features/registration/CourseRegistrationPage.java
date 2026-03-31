package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseRegistrationPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DefaultListModel<RegistrationBatch> batchListModel = new DefaultListModel<>();
    private final JList<RegistrationBatch> batchList = new JList<>(batchListModel);
    private final DefaultTableModel sectionTableModel = new DefaultTableModel(
            new Object[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Còn chỗ", "Trạng thái"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable sectionTable = new JTable(sectionTableModel);
    private final JLabel batchInfoLabel = new JLabel("Chọn một đợt đăng ký để xem các môn học.", SwingConstants.LEFT);

    public CourseRegistrationPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        PageScaffold scaffold = new PageScaffold("ĐĂNG KÝ MÔN HỌC");
        scaffold.setBody(createBody());
        add(scaffold, BorderLayout.CENTER);

        configureBatchList();
        configureTable();
        loadOpenBatches();
    }

    private Component createBody() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.add(createHintPanel(), BorderLayout.NORTH);
        container.add(createContentPanel(), BorderLayout.CENTER);
        return container;
    }

    private JPanel createHintPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel hintLabel = new JLabel("<html>Hệ thống chỉ hiển thị các đợt đăng ký đang mở theo thời gian thực.<br>Chọn một đợt ở bên trái để xem các lớp học phần và đăng ký.</html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(new Color(100, 110, 120));
        panel.add(hintLabel, BorderLayout.CENTER);
        return panel;
    }

    private Component createContentPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createBatchPanel(), createSectionPanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.3);
        return splitPane;
    }

    private Component createBatchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Đợt đăng ký đang mở");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(batchList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Component createSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        batchInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        batchInfoLabel.setForeground(new Color(80, 90, 100));
        panel.add(batchInfoLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(sectionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        PrimaryButton registerButton = new PrimaryButton("Đăng ký lớp đã chọn");
        registerButton.setPreferredSize(new Dimension(190, 42));
        registerButton.addActionListener(e -> registerSelectedSection());
        actions.add(registerButton);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private void configureBatchList() {
        batchList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        batchList.setFixedCellHeight(56);
        batchList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel item = new JPanel(new BorderLayout(0, 6));
            item.setBorder(new EmptyBorder(10, 12, 10, 12));
            item.setBackground(isSelected ? new Color(223, 236, 255) : Color.WHITE);

            JLabel nameLabel = new JLabel(value.getName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(new Color(25, 35, 45));

            String termText = value.getTerm() == null ? "" : value.getTerm().toString();
            JLabel metaLabel = new JLabel(termText + " | " + DATE_TIME_FORMATTER.format(value.getOpenAt()) + " - " + DATE_TIME_FORMATTER.format(value.getCloseAt()));
            metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            metaLabel.setForeground(new Color(100, 110, 120));

            item.add(nameLabel, BorderLayout.NORTH);
            item.add(metaLabel, BorderLayout.CENTER);
            return item;
        });
        batchList.addListSelectionListener(this::onBatchSelected);
    }

    private void configureTable() {
        sectionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sectionTable.setSelectionBackground(new Color(223, 236, 255));
        sectionTable.setSelectionForeground(new Color(30, 30, 30));
        sectionTable.setGridColor(new Color(230, 235, 240));
        sectionTable.setFillsViewportHeight(true);
        TableStyles.applyBlueHeader(sectionTable);
        TableStyles.centerColumns(sectionTable, 0, 1, 4, 5, 6, 7, 8);
    }

    private void onBatchSelected(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            loadSectionsForSelectedBatch();
        }
    }

    private void loadOpenBatches() {
        batchListModel.clear();
        List<RegistrationBatch> openBatches = AppContext.getRegistrationBatchController().getOpenBatches(LocalDateTime.now());
        for (RegistrationBatch batch : openBatches) {
            batchListModel.addElement(batch);
        }

        if (!batchListModel.isEmpty()) {
            batchList.setSelectedIndex(0);
        } else {
            batchInfoLabel.setText("Hiện tại chưa có đợt đăng ký nào đang mở.");
            sectionTableModel.setRowCount(0);
        }
    }

    private void loadSectionsForSelectedBatch() {
        sectionTableModel.setRowCount(0);
        RegistrationBatch batch = batchList.getSelectedValue();
        if (batch == null) {
            batchInfoLabel.setText("Chọn một đợt đăng ký để xem các môn học.");
            return;
        }

        Set<Long> registeredSectionIds = getRegisteredSectionIds();
        batchInfoLabel.setText("Đợt " + batch.getName() + " | Học kỳ " + batch.getTerm() + " | Bắt đầu học " + DATE_FORMATTER.format(batch.getCommonStartDate()));

        List<CourseSection> sections = AppContext.getCourseSectionController().getSectionsByBatch(batch.getId());
        for (CourseSection section : sections) {
            int current = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
            int max = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
            String availability = current + "/" + max;
            String state = registeredSectionIds.contains(section.getId()) ? "Đã đăng ký" : (current >= max ? "Đã đầy" : "Có thể đăng ký");

            sectionTableModel.addRow(new Object[]{
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    availability,
                    state
            });
        }
    }

    private Set<Long> getRegisteredSectionIds() {
        Set<Long> ids = new HashSet<>();
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return ids;
        }
        for (CourseRegistration registration : AppContext.getCourseRegistrationController().getStudentRegistrations(student.getId())) {
            if (registration.getCourseSection() != null) {
                ids.add(registration.getCourseSection().getId());
            }
        }
        return ids;
    }

    private void registerSelectedSection() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sinh viên đang đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học phần trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long sectionId = ((Number) sectionTableModel.getValueAt(selectedRow, 0)).longValue();
        try {
            AppContext.getCourseRegistrationController().registerStudentToSection(student.getId(), sectionId);
            JOptionPane.showMessageDialog(this, "Đăng ký môn học thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadSectionsForSelectedBatch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDay(Integer dayOfWeek) {
        if (dayOfWeek == null) {
            return "";
        }
        return switch (dayOfWeek) {
            case 1 -> "Thứ 2";
            case 2 -> "Thứ 3";
            case 3 -> "Thứ 4";
            case 4 -> "Thứ 5";
            case 5 -> "Thứ 6";
            case 6 -> "Thứ 7";
            case 7 -> "Chủ nhật";
            default -> "Không rõ";
        };
    }

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }

    @Override
    public void onEnter() {
        loadOpenBatches();
    }
}
