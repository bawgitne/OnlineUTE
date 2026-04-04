package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.PaginationPanel;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.IntConsumer;

public class StudentSearchResultPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JLabel resultLabel = new JLabel("0 ket qua");
    private final PaginationPanel paginationPanel;
    private IntConsumer onPageRequested;

    public StudentSearchResultPanel(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 85, 141));
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        tableModel = new DefaultTableModel(
                new Object[]{"Ma SV", "Ho ten", "Email", "Lop", "Khoa", "Nam nhap hoc"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(235, 240, 245));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        TableStyles.applyModernTable(table);
        TableStyles.centerColumns(table, 0, 5);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        paginationPanel = new PaginationPanel(
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(-1);
                    }
                },
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(1);
                    }
                }
        );

        add(resultLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    public void showResults(PagedResult<Student> result, String keyword) {
        tableModel.setRowCount(0);

        for (Student student : result.getItems()) {
            String className = student.getClassEntity() != null ? student.getClassEntity().getClassName() : "";
            String facultyName = student.getClassEntity() != null && student.getClassEntity().getFaculty() != null
                    ? student.getClassEntity().getFaculty().getFullName()
                    : "";

            tableModel.addRow(new Object[]{
                    student.getCode(),
                    student.getFullName(),
                    student.getEmail(),
                    className,
                    facultyName,
                    student.getEnrollmentYear()
            });
        }

        resultLabel.setText(result.getTotalItems() + " ket qua cho: " + keyword.trim());
        paginationPanel.updateState(result.getPage(), result.getTotalPages(), result.hasPrevious(), result.hasNext());
    }

    public void setPageHandler(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
    }
}
