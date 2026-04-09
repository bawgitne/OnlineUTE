/**
 * xem thời khóa biểu
 */
package com.bangcompany.onlineute.View.features.schedule;

import com.bangcompany.onlineute.View.shared.ViewContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SchedulePage extends JPanel implements Refreshable {
    private final ViewContext viewContext;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JLabel weekLabel = new JLabel("", SwingConstants.LEFT);
    private final ScheduleTablePanel tablePanel = new ScheduleTablePanel();
    private LocalDate selectedWeekStart;

    // khởi tạo lịch học, mặc định tuần này
    public SchedulePage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(Card.titleCard("THỜI KHÓA BIỂU"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        // mốc là thứ 2 tuần này
        selectedWeekStart = getWeekStart(LocalDate.now());
        onEnter();
    }

    // vùng nút bấm và bảng lịch
    private JComponent createBody() {
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(AppTheme.BACKGROUND_CARD);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);
        contentWrapper.add(tablePanel, BorderLayout.CENTER);
        return contentWrapper;
    }

    // cụm nút chuyển tuần
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Chuyển tuần để xem môn học");
        currentScheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentScheduleLabel.setForeground(AppTheme.PRIMARY_BLUE);
        leftPanel.add(currentScheduleLabel);

        weekLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        weekLabel.setForeground(AppTheme.TEXT_MUTED);
        leftPanel.add(weekLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        Button previousWeekButton = new Button("Tuần trước");
        previousWeekButton.setPreferredSize(new Dimension(120, 40));
        previousWeekButton.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            refreshScheduleGrid();
        });

        Button currentWeekButton = new Button("Tuần hiện tại");
        currentWeekButton.setPreferredSize(new Dimension(130, 40));
        currentWeekButton.addActionListener(e -> {
            selectedWeekStart = getWeekStart(LocalDate.now());
            refreshScheduleGrid();
        });

        Button nextWeekButton = new Button("Tuần sau");
        nextWeekButton.setPreferredSize(new Dimension(120, 40));
        nextWeekButton.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            refreshScheduleGrid();
        });

        rightPanel.add(previousWeekButton);
        rightPanel.add(currentWeekButton);
        rightPanel.add(nextWeekButton);

        filterPanel.add(leftPanel, BorderLayout.WEST);
        filterPanel.add(rightPanel, BorderLayout.EAST);
        return filterPanel;
    }

    @Override
    public void onEnter() {
        refreshScheduleGrid();
    }

    // vẽ lại bảng theo tuần chọn
    private void refreshScheduleGrid() {
        LocalDate weekEnd = selectedWeekStart.plusDays(6);
        weekLabel.setText("Tuần: " + DATE_FORMATTER.format(selectedWeekStart) + " - " + DATE_FORMATTER.format(weekEnd));
        tablePanel.renderWeek(selectedWeekStart, loadSchedulesForCurrentWeek());
    }

    // lấy lịch từ db theo role gv/sv
    private List<Schedule> loadSchedulesForCurrentWeek() {
        if (viewContext.getScheduleController() == null) {
            return List.of();
        }

        var student = SessionManager.getCurrentStudent();
        if (student != null) {
            return viewContext.getScheduleController().getStudentScheduleByWeek(
                    student.getId(),
                    selectedWeekStart,
                    selectedWeekStart.plusDays(6)
            );
        }

        var lecturer = SessionManager.getCurrentLecturer();
        if (lecturer != null) {
            return viewContext.getScheduleController().getLecturerScheduleByWeek(
                    lecturer.getId(),
                    selectedWeekStart,
                    selectedWeekStart.plusDays(6)
            );
        }

        return List.of();
    }

    // tính ngày thứ 2 đầu tuần
    private LocalDate getWeekStart(LocalDate date) {
        int offset = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        if (offset < 0) {
            offset += 7;
        }
        return date.minusDays(offset);
    }
}
