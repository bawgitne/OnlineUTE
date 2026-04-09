package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.View.Components.theme.DateUtils;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSectionManagerDialog extends JDialog {
    private final RegistrationBatch batch;
    private final ViewContext viewContext;
    private final Table table;
    private final List<CourseSection> sections = new ArrayList<>();

    public CourseSectionManagerDialog(Window owner, RegistrationBatch batch, ViewContext viewContext) {
        super(owner, "Quản lý lớp học phần", ModalityType.APPLICATION_MODAL);
        this.batch = batch;
        this.viewContext = viewContext;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout(0, 12));
        add(Card.titleCard("DANH SÁCH LỚP HỌC PHẦN"), BorderLayout.NORTH);

        table = new Table(new String[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Sĩ số", "Tuần"}, 12, 44);
        table.setOnRowSelected(index -> openEditDialog(index));

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(12, 12, 12, 12));
        body.add(table, BorderLayout.CENTER);
        body.add(buildActions(), BorderLayout.SOUTH);

        add(body, BorderLayout.CENTER);

        loadSections();
    }

    private JPanel buildActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        Button createButton = new Button("Tạo mới");
        createButton.setPreferredSize(new Dimension(120, 40));
        createButton.addActionListener(e -> openCreateDialog());

        panel.add(createButton);
        return panel;
    }

    private void loadSections() {
        table.clearRows();
        sections.clear();

        if (batch == null || batch.getId() == null) {
            return;
        }

        List<CourseSection> results = viewContext.getCourseSectionController().getSectionsByBatch(batch.getId());
        for (CourseSection section : results) {
            sections.add(section);
            int current = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
            int max = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
            String capacity = current + "/" + max;
            String slots = formatSlots(section.getStartSlot(), section.getEndSlot());
            String weeks = section.getTotalWeeks() == null ? "" : String.valueOf(section.getTotalWeeks());

            table.addRow(
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    DateUtils.formatDay(section.getDayOfWeek()),
                    slots,
                    capacity,
                    weeks
            );
        }
    }

    private void openCreateDialog() {
        CourseSectionDialog dialog = new CourseSectionDialog(this, batch, viewContext);
        dialog.setVisible(true);
        loadSections();
    }

    private void openEditDialog(int index) {
        if (index < 0 || index >= sections.size()) {
            return;
        }
        CourseSection section = sections.get(index);
        CourseSectionDialog dialog = new CourseSectionDialog(this, batch, section, viewContext);
        dialog.setVisible(true);
        loadSections();
    }

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }
}