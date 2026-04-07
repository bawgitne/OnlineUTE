/**
 * Xem thời khóa biểu
 */
package com.bangcompany.onlineute.View.features.schedule;

import com.bangcompany.onlineute.Config.AppContext;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JLabel weekLabel = new JLabel("", SwingConstants.LEFT);
    private final ScheduleTablePanel tablePanel = new ScheduleTablePanel();
    private LocalDate selectedWeekStart;

    public SchedulePage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        add(Card.titleCard("THỜI KHÓA BIỂU"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        // xem tuần hiện tại
        selectedWeekStart = getWeekStart(LocalDate.now());
        onEnter();
    }

    private JComponent createBody() {
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(AppTheme.BACKGROUND_CARD);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);
        contentWrapper.add(tablePanel, BorderLayout.CENTER);
        return contentWrapper;
    }

    // thaanh giao diện ở trên có nút đổi tuần
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Chuyển tuần để xem môn học sẽ học/dạy");
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

    // vẽ table
    private void refreshScheduleGrid() {
        LocalDate weekEnd = selectedWeekStart.plusDays(6);
        weekLabel.setText("Tuần: " + DATE_FORMATTER.format(selectedWeekStart) + " - " + DATE_FORMATTER.format(weekEnd));
        tablePanel.renderWeek(selectedWeekStart, loadSchedulesForCurrentWeek());
    }

    // kiểm tra role để load data lịch tương ứng
    private List<Schedule> loadSchedulesForCurrentWeek() {
        if (AppContext.getScheduleService() == null) {
            return List.of();
        }

        var student = SessionManager.getCurrentStudent();
        if (student != null) {
            return AppContext.getScheduleService().getStudentScheduleByWeek(
                    student.getId(),
                    selectedWeekStart,
                    selectedWeekStart.plusDays(6)
            );
        }

        var lecturer = SessionManager.getCurrentLecturer();
        if (lecturer != null) {
            return AppContext.getScheduleService().getLecturerScheduleByWeek(
                    lecturer.getId(),
                    selectedWeekStart,
                    selectedWeekStart.plusDays(6)
            );
        }

        return List.of();
    }

    // đặt thứ 2 làm mốc
    private LocalDate getWeekStart(LocalDate date) {
        int offset = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        if (offset < 0) {
            offset += 7;
        }
        return date.minusDays(offset);
    }
}
