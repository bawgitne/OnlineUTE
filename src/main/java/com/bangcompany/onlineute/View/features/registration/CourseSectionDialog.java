/**
 * Tạo lớp học phần
 */
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.FormRow;
import com.bangcompany.onlineute.View.Components.theme.DateUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CourseSectionDialog extends JDialog {
    private final RegistrationBatch selectedBatch;

    private final TextInput sectionCodeInput = new TextInput("Mã lớp học phần", false);
    private final SelectInput<Course> courseSelect = new SelectInput<>("Môn học", AppContext.getCourseService().getAllCourses());
    private final SelectInput<Lecturer> lecturerSelect = new SelectInput<>("Giảng viên", AppContext.getLecturerDAO().findAll());
    private final TextInput roomInput = new TextInput("Phòng học", false);
    private final TextInput maxCapacityInput = new TextInput("Số lượng tối đa", false);
    private final SelectInput<DayOption> daySelect = new SelectInput<>("Thứ học", List.of(
            new DayOption(1, DateUtils.formatDay(1)),
            new DayOption(2, DateUtils.formatDay(2)),
            new DayOption(3, DateUtils.formatDay(3)),
            new DayOption(4, DateUtils.formatDay(4)),
            new DayOption(5, DateUtils.formatDay(5)),
            new DayOption(6, DateUtils.formatDay(6)),
            new DayOption(7, DateUtils.formatDay(7))
    ));
    private final TextInput startSlotInput = new TextInput("Tiết bắt đầu", false);
    private final TextInput endSlotInput = new TextInput("Tiết kết thúc", false);
    private final TextInput totalWeeksInput = new TextInput("Số tuần học", false);

    private final Table sectionTable = new Table(
            new String[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Số tuần", "Sĩ số"},
            12,
            44
    );

    // hàm tạo dialog quản lí lớp học phần
    public CourseSectionDialog(Window owner, RegistrationBatch selectedBatch) {
        super(owner, "Quản lý lớp học phần", ModalityType.APPLICATION_MODAL);
        this.selectedBatch = selectedBatch;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(owner);
        setContentPane(createContent());

        fillDefaultValues();
        loadSectionsIntoTable();
    }

    private Container createContent() {
        JPanel container = new JPanel(new BorderLayout(0, 16));
        container.setBackground(new Color(245, 245, 245));
        container.setBorder(new EmptyBorder(18, 18, 18, 18));

        Card title = Card.titleCard("Tạo lớp học phần cho đợt: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
        container.add(title, BorderLayout.NORTH);

        // chia 2 nửa: bên trái nhập form, bên phải xem list
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createTablePanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.42);
        splitPane.setDividerLocation(360);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        splitPane.setOneTouchExpandable(false);
        container.add(splitPane, BorderLayout.CENTER);

        return container;
    }

    // cái form dài loằng ngoằng để nhập thông tin lớp
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel hint = new JLabel("<html>Khai báo môn học, giảng viên, phòng, thứ học, tiết học và số tuần.<br>Hệ thống sẽ tự động tính ngày học đầu tiên và sinh lịch theo tuần.</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(100, 110, 120));
        panel.add(hint);
        panel.add(Box.createVerticalStrut(14));

        panel.add(sectionCodeInput);
        panel.add(Box.createVerticalStrut(12));
        panel.add(courseSelect);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lecturerSelect);
        panel.add(Box.createVerticalStrut(12));
        panel.add(FormRow.two(roomInput, maxCapacityInput));
        panel.add(Box.createVerticalStrut(12));
        panel.add(FormRow.two(daySelect, totalWeeksInput));
        panel.add(Box.createVerticalStrut(12));
        panel.add(FormRow.two(startSlotInput, endSlotInput));
        panel.add(Box.createVerticalStrut(18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        Button createSectionButton = new Button("Tạo lớp học phần");
        createSectionButton.setPreferredSize(new Dimension(180, 40));
        createSectionButton.addActionListener(e -> createCourseSection());

        actions.add(createSectionButton);
        panel.add(actions);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        Card title = Card.titleCard("Danh sách lớp học phần của đợt");
        panel.add(title, BorderLayout.NORTH);

        panel.add(sectionTable, BorderLayout.CENTER);
        return panel;
    }

    // lưu lớp học phần và tự động sinh lịch học kèm theo
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

    // load list lớp con của đợt vào table
    private void loadSectionsIntoTable() {
        sectionTable.clearRows();
        List<CourseSection> sections = AppContext.getCourseSectionController().getSectionsByBatch(selectedBatch.getId());
        for (CourseSection section : sections) {
            sectionTable.addRow(
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    DateUtils.formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    section.getTotalWeeks(),
                    (section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity()) + "/" + (section.getMaxCapacity() == null ? 0 : section.getMaxCapacity())
            );
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

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }

    // model bọc thứ học để hiện text trong comboBox
    private record DayOption(int value, String label) {
        @Override
        public String toString() {
            return label;
        }
    }
}
