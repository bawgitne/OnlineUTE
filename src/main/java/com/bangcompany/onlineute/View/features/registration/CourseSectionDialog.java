package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.View.shared.ViewContext;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.FormRow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSectionDialog extends JDialog {
    private final RegistrationBatch selectedBatch;
    private final ViewContext viewContext;
    private final CourseSection editingSection;

    private final TextInput sectionCodeInput = new TextInput("Mã lớp học phần", false);
    private final SelectInput<Course> courseSelect = new SelectInput<>("Môn học", List.of());
    private final SelectInput<Lecturer> lecturerSelect = new SelectInput<>("Giảng viên", List.of());
    private final TextInput roomInput = new TextInput("Phòng", false);
    private final TextInput maxCapacityInput = new TextInput("Sĩ số tối đa", false);
    private final SelectInput<DayOption> daySelect = new SelectInput<>("Thứ học", List.of());
    private final TextInput startSlotInput = new TextInput("Tiết bắt đầu", false);
    private final TextInput endSlotInput = new TextInput("Tiết kết thúc", false);
    private final TextInput totalWeeksInput = new TextInput("Số tuần học", false);

    private final Button saveButton = new Button("Lưu lớp học phần");
    private Button deleteButton;
    private List<DayOption> dayOptions = new ArrayList<>();

    public CourseSectionDialog(Window owner, RegistrationBatch selectedBatch, ViewContext viewContext) {
        this(owner, selectedBatch, null, viewContext);
    }

    public CourseSectionDialog(Window owner, RegistrationBatch selectedBatch, CourseSection editingSection, ViewContext viewContext) {
        super(owner, editingSection == null ? "Tạo lớp học phần" : "Chỉnh sửa lớp học phần", ModalityType.APPLICATION_MODAL);
        this.selectedBatch = selectedBatch;
        this.viewContext = viewContext;
        this.editingSection = editingSection;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(720, 560);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout(0, 12));
        add(Card.titleCard(editingSection == null ? "TẠO LỚP HỌC PHẦN" : "CHỈNH SỬA LỚP HỌC PHẦN"), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        loadOptions();
        if (editingSection != null) {
            fillForm(editingSection);
        }
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(FormRow.two(sectionCodeInput, courseSelect));
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(FormRow.two(lecturerSelect, roomInput));
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(FormRow.three(maxCapacityInput, daySelect, startSlotInput));
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(FormRow.two(endSlotInput, totalWeeksInput));

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        if (editingSection != null) {
            deleteButton = new Button("Xóa", new Color(230, 234, 240), new Color(30, 35, 40));
            deleteButton.addActionListener(e -> deleteSection());
            left.add(deleteButton);
        }

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        Button cancelButton = new Button("Đóng", new Color(230, 234, 240), new Color(30, 35, 40));
        cancelButton.addActionListener(e -> dispose());

        saveButton.setText(editingSection == null ? "Lưu lớp học phần" : "Cập nhật");
        saveButton.addActionListener(e -> saveSection());

        right.add(cancelButton);
        right.add(saveButton);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void loadOptions() {
        courseSelect.setItems(viewContext.getCourseController().getAllCourses());
        lecturerSelect.setItems(viewContext.getLecturerController().getAllLecturers());
        dayOptions = defaultDays();
        daySelect.setItems(dayOptions);
    }

    private void fillForm(CourseSection section) {
        sectionCodeInput.setValue(section.getSectionCode());
        courseSelect.setSelectedItem(section.getCourse());
        lecturerSelect.setSelectedItem(section.getLecturer());
        roomInput.setValue(section.getRoom());
        maxCapacityInput.setValue(section.getMaxCapacity() == null ? "" : String.valueOf(section.getMaxCapacity()));
        startSlotInput.setValue(section.getStartSlot() == null ? "" : String.valueOf(section.getStartSlot()));
        endSlotInput.setValue(section.getEndSlot() == null ? "" : String.valueOf(section.getEndSlot()));
        totalWeeksInput.setValue(section.getTotalWeeks() == null ? "" : String.valueOf(section.getTotalWeeks()));
        DayOption option = findDayOption(section.getDayOfWeek());
        if (option != null) {
            daySelect.setSelectedItem(option);
        }
    }

    private DayOption findDayOption(Integer day) {
        if (day == null) return null;
        for (DayOption option : dayOptions) {
            if (option.day == day) {
                return option;
            }
        }
        return null;
    }

    private List<DayOption> defaultDays() {
        List<DayOption> options = new ArrayList<>();
        options.add(new DayOption(1, "Thứ 2"));
        options.add(new DayOption(2, "Thứ 3"));
        options.add(new DayOption(3, "Thứ 4"));
        options.add(new DayOption(4, "Thứ 5"));
        options.add(new DayOption(5, "Thứ 6"));
        options.add(new DayOption(6, "Thứ 7"));
        options.add(new DayOption(7, "Chủ nhật"));
        return options;
    }

    private void saveSection() {
        try {
            CourseSection section = editingSection == null ? new CourseSection() : editingSection;
            section.setSectionCode(sectionCodeInput.getValue().trim());
            section.setCourse(courseSelect.getSelectedValue());
            section.setLecturer(lecturerSelect.getSelectedValue());
            section.setRoom(roomInput.getValue().trim());
            section.setMaxCapacity(parseInteger(maxCapacityInput.getValue(), "Sĩ số tối đa"));
            section.setDayOfWeek(daySelect.getSelectedValue() == null ? null : daySelect.getSelectedValue().day);
            section.setStartSlot(parseInteger(startSlotInput.getValue(), "Tiết bắt đầu"));
            section.setEndSlot(parseInteger(endSlotInput.getValue(), "Tiết kết thúc"));
            section.setTotalWeeks(parseInteger(totalWeeksInput.getValue(), "Số tuần học"));
            section.setRegistrationBatch(selectedBatch);
            if (selectedBatch != null) {
                section.setTerm(selectedBatch.getTerm());
            }

            if (editingSection == null) {
                viewContext.getCourseSectionController().createSectionForBatch(selectedBatch, section);
                ExceptionHandler.showInfo(this, "Đã tạo lớp học phần.", "Thông báo");
            } else {
                viewContext.getCourseSectionController().updateSection(section);
                ExceptionHandler.showInfo(this, "Đã cập nhật lớp học phần.", "Thông báo");
            }
            dispose();
        } catch (Exception ex) {
            ExceptionHandler.showError(this, ex, "Không thể lưu lớp học phần.");
        }
    }

    private void deleteSection() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa lớp học phần này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            viewContext.getCourseSectionController().deleteSection(editingSection);
            ExceptionHandler.showInfo(this, "Đã xóa lớp học phần.", "Thông báo");
            dispose();
        } catch (Exception ex) {
            ExceptionHandler.showError(this, ex, "Không thể xóa lớp học phần.");
        }
    }

    private Integer parseInteger(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " không được để trống.");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " không hợp lệ.");
        }
    }

    private static final class DayOption {
        private final int day;
        private final String label;

        private DayOption(int day, String label) {
            this.day = day;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
