package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseSectionDialog extends JDialog {
    private final RegistrationBatch selectedBatch;

    private final InputGroup sectionCodeInput = new InputGroup("Mã lớp học phần", false);
    private final SelectGroup<Course> courseSelect = new SelectGroup<>("Môn học", AppContext.getCourseService().getAllCourses());
    private final SelectGroup<Lecturer> lecturerSelect = new SelectGroup<>("Giảng viên", AppContext.getLecturerDAO().findAll());
    private final InputGroup roomInput = new InputGroup("Phòng học", false);
    private final InputGroup maxCapacityInput = new InputGroup("Số lượng tối đa", false);
    private final SelectGroup<DayOption> daySelect = new SelectGroup<>("Thứ học", List.of(
            new DayOption(1, "Thứ 2"),
            new DayOption(2, "Thứ 3"),
            new DayOption(3, "Thứ 4"),
            new DayOption(4, "Thứ 5"),
            new DayOption(5, "Thứ 6"),
            new DayOption(6, "Thứ 7"),
            new DayOption(7, "Chủ nhật")
    ));
    private final InputGroup startSlotInput = new InputGroup("Tiết bắt đầu", false);
    private final InputGroup endSlotInput = new InputGroup("Tiết kết thúc", false);
    private final InputGroup totalWeeksInput = new InputGroup("Số tuần học", false);

    private final DefaultTableModel sectionTableModel = new DefaultTableModel(
            new Object[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Số tuần", "Sĩ số"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable sectionTable = new JTable(sectionTableModel);

    public CourseSectionDialog(Window owner, RegistrationBatch selectedBatch) {
        super(owner, "Quản lý lớp học phần", ModalityType.APPLICATION_MODAL);
        this.selectedBatch = selectedBatch;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(owner);
        setContentPane(createContent());

        fillDefaultValues();
        configureSectionTable();
        loadSectionsIntoTable();
    }

    private Container createContent() {
        JPanel container = new JPanel(new BorderLayout(0, 16));
        container.setBackground(new Color(245, 245, 245));
        container.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Tạo lớp học phần cho đợt: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        container.add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createTablePanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.42);
        splitPane.setDividerLocation(360);
        container.add(splitPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel hint = new JLabel("<html>Khai báo môn học, giảng viên, phòng, thứ học, tiết học và số tuần.<br>Hệ thống sẽ tự động tính ngày học đầu tiên và sinh lịch học theo tuần.</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(100, 110, 120));
        panel.add(hint, gbc);

        gbc.gridy++;
        panel.add(sectionCodeInput, gbc);

        gbc.gridy++;
        panel.add(courseSelect, gbc);

        gbc.gridy++;
        panel.add(lecturerSelect, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(roomInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(maxCapacityInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(daySelect, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(totalWeeksInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 18, 10);
        panel.add(startSlotInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 18, 0);
        panel.add(endSlotInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        JButton closeButton = new JButton("Đóng");
        closeButton.setPreferredSize(new Dimension(100, 40));
        closeButton.addActionListener(e -> dispose());

        PrimaryButton createSectionButton = new PrimaryButton("Tạo lớp học phần");
        createSectionButton.setPreferredSize(new Dimension(180, 40));
        createSectionButton.addActionListener(e -> createCourseSection());

        actions.add(closeButton);
        actions.add(createSectionButton);
        panel.add(actions, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Danh sách lớp học phần của đợt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(sectionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void configureSectionTable() {
        sectionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sectionTable.setSelectionBackground(new Color(223, 236, 255));
        sectionTable.setSelectionForeground(new Color(30, 30, 30));
        sectionTable.setGridColor(new Color(230, 235, 240));
        sectionTable.setFillsViewportHeight(true);
        TableStyles.applyBlueHeader(sectionTable);
        TableStyles.centerColumns(sectionTable, 0, 1, 4, 5, 6, 7, 8);
    }

    private void createCourseSection() {
        try {
            CourseSection section = new CourseSection();
            section.setSectionCode(sectionCodeInput.getValue().trim());
            section.setCourse(courseSelect.getSelectedValue());
            section.setLecturer(lecturerSelect.getSelectedValue());
            section.setRoom(roomInput.getValue().trim());
            section.setMaxCapacity(Integer.parseInt(maxCapacityInput.getValue().trim()));
            section.setCurrentCapacity(0);
            section.setDayOfWeek(daySelect.getSelectedValue().value());
            section.setStartSlot(Integer.parseInt(startSlotInput.getValue().trim()));
            section.setEndSlot(Integer.parseInt(endSlotInput.getValue().trim()));
            section.setTotalWeeks(Integer.parseInt(totalWeeksInput.getValue().trim()));

            AppContext.getCourseSectionController().createSectionForBatch(selectedBatch, section);

            JOptionPane.showMessageDialog(this, "Tạo lớp học phần thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearSectionForm();
            loadSectionsIntoTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được lớp học phần: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSectionsIntoTable() {
        sectionTableModel.setRowCount(0);
        List<CourseSection> sections = AppContext.getCourseSectionController().getSectionsByBatch(selectedBatch.getId());
        for (CourseSection section : sections) {
            sectionTableModel.addRow(new Object[]{
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    section.getTotalWeeks(),
                    (section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity()) + "/" + (section.getMaxCapacity() == null ? 0 : section.getMaxCapacity())
            });
        }
    }

    private void clearSectionForm() {
        sectionCodeInput.setValue("");
        roomInput.setValue("");
        maxCapacityInput.setValue("70");
        startSlotInput.setValue("");
        endSlotInput.setValue("");
        totalWeeksInput.setValue("15");
    }

    private void fillDefaultValues() {
        if (maxCapacityInput.getValue().isBlank()) {
            maxCapacityInput.setValue("70");
        }
        if (totalWeeksInput.getValue().isBlank()) {
            totalWeeksInput.setValue("15");
        }
    }

    private String formatDay(Integer value) {
        if (value == null) {
            return "";
        }
        return switch (value) {
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

    private record DayOption(int value, String label) {
        @Override
        public String toString() {
            return label;
        }
    }
}
