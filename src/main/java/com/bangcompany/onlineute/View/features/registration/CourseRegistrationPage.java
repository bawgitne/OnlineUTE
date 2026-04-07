/**
 * Đăng ký môn học
 */
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.DateUtils;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseRegistrationPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DefaultListModel<RegistrationBatch> batchListModel = new DefaultListModel<>();
    private final JList<RegistrationBatch> batchList = new JList<>(batchListModel);
    private final Table sectionTable;
    private final JLabel batchInfoLabel = new JLabel("Chọn một đợt đăng ký để xem các môn học.", SwingConstants.LEFT);
    private final List<CourseSection> currentSections = new ArrayList<>();

    // hàm tạo giao diện chính
    public CourseRegistrationPage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        sectionTable = new Table(new String[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Còn chỗ", "Trạng thái"}, 12, 44);
        sectionTable.setOnRowSelected(index -> {});

        add(Card.titleCard("ĐĂNG KÝ MÔN HỌC"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        configureBatchList();
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
        panel.setBackground(AppTheme.BACKGROUND_CARD);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel hintLabel = new JLabel("<html>Hệ thống chỉ hiển thị các đợt đăng ký đang mở theo thời gian thực.<br>Chọn một đợt ở bên trái để xem các lớp học phần và đăng ký.</html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(AppTheme.TEXT_MUTED);
        panel.add(hintLabel, BorderLayout.CENTER);
        return panel;
    }

    // chia 2 cột: trái là ds đợt, phải là ds lớp môn học
    private Component createContentPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createBatchPanel(), createSectionPanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.3);
        return splitPane;
    }

    private Component createBatchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(AppTheme.BACKGROUND_CARD);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        Card title = Card.titleCard("Đợt đăng ký đang mở");
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

        panel.add(sectionTable, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        Button registerButton = new Button("Đăng ký lớp đã chọn");
        registerButton.setPreferredSize(new Dimension(190, 42));
        registerButton.addActionListener(e -> registerSelectedSection());
        actions.add(registerButton);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    // vẽ giao diện cho từng đợt đăng ký trong list bên trái
    private void configureBatchList() {
        batchList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        batchList.setFixedCellHeight(56);
        batchList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel item = new JPanel(new BorderLayout(0, 6));
            item.setBorder(new EmptyBorder(10, 12, 10, 12));
            item.setBackground(isSelected ? new Color(223, 236, 255) : AppTheme.BACKGROUND_CARD);

            JLabel nameLabel = new JLabel(value.getName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(new Color(25, 35, 45));

            String termText = value.getTerm() == null ? "" : value.getTerm().toString();
            JLabel metaLabel = new JLabel(termText + " | " + DATE_TIME_FORMATTER.format(value.getOpenAt()) + " - " + DATE_TIME_FORMATTER.format(value.getCloseAt()));
            metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            metaLabel.setForeground(AppTheme.TEXT_MUTED);

            item.add(nameLabel, BorderLayout.NORTH);
            item.add(metaLabel, BorderLayout.CENTER);
            return item;
        });
        batchList.addListSelectionListener(this::onBatchSelected);
    }

    private void onBatchSelected(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            loadSectionsForSelectedBatch();
        }
    }

    // chỉ load những đợt đang trong thời gian mở
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
            sectionTable.clearRows();
            sectionTable.setEmptyRow("");
        }
    }

    // load các lớp học phần thuộc đợt được chọn
    private void loadSectionsForSelectedBatch() {
        sectionTable.clearRows();
        currentSections.clear();
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

            currentSections.add(section);
            sectionTable.addRow(
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    DateUtils.formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    availability,
                    state
            );
        }
    }

    // lấy ds ID các lớp mà sinh viên này đã đăng ký trước đó
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

    // gọi controller để thực hiện đăng ký 1 lớp học phần
    private void registerSelectedSection() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sinh viên đang đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedIndex = sectionTable.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= currentSections.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học phần trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long sectionId = currentSections.get(selectedIndex).getId();
        try {
            AppContext.getCourseRegistrationController().registerStudentToSection(student.getId(), sectionId);
            JOptionPane.showMessageDialog(this, "Đăng ký môn học thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadSectionsForSelectedBatch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
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
