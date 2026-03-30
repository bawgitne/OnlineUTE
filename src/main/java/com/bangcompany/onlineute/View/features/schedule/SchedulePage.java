package com.bangcompany.onlineute.View.features.schedule;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Color primaryColor = new Color(0, 85, 141);
    private final String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    private final String[] periods = {"Sáng", "Chiều", "Tối"};

    private final JPanel gridPanel = new JPanel(new GridBagLayout());
    private final JLabel weekLabel = new JLabel("", SwingConstants.LEFT);
    private LocalDate selectedWeekStart;

    public SchedulePage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("THỜI KHÓA BIỂU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);

        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        selectedWeekStart = getWeekStart(LocalDate.now());
        onEnter();
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Sinh viên có thể chuyển tuần để xem môn học sẽ học");
        currentScheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentScheduleLabel.setForeground(new Color(0, 85, 141));
        leftPanel.add(currentScheduleLabel);

        weekLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        weekLabel.setForeground(new Color(80, 90, 100));
        leftPanel.add(weekLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        PrimaryButton previousWeekButton = new PrimaryButton("Tuần trước");
        previousWeekButton.setPreferredSize(new Dimension(120, 40));
        previousWeekButton.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            refreshScheduleGrid();
        });

        PrimaryButton currentWeekButton = new PrimaryButton("Tuần hiện tại");
        currentWeekButton.setPreferredSize(new Dimension(130, 40));
        currentWeekButton.addActionListener(e -> {
            selectedWeekStart = getWeekStart(LocalDate.now());
            refreshScheduleGrid();
        });

        PrimaryButton nextWeekButton = new PrimaryButton("Tuần sau");
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

    private void refreshScheduleGrid() {
        LocalDate weekEnd = selectedWeekStart.plusDays(6);
        weekLabel.setText("Tuần: " + DATE_FORMATTER.format(selectedWeekStart) + " - " + DATE_FORMATTER.format(weekEnd));

        gridPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        addHeaders(gbc);
        addScheduleRows(gbc, loadSchedulesForCurrentWeek());

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addHeaders(GridBagConstraints gbc) {
        gbc.gridy = 0;
        String[] colHeaders = {"", "Sáng", "Chiều", "Tối"};
        for (int col = 0; col < colHeaders.length; col++) {
            gbc.gridx = col;
            gbc.weightx = col == 0 ? 0.2 : 1.0;

            JPanel headerTile = new JPanel(new GridBagLayout());
            headerTile.setBackground(primaryColor);
            headerTile.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));

            JLabel label = new JLabel(colHeaders[col]);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            headerTile.add(label);
            headerTile.setPreferredSize(new Dimension(0, 40));
            gridPanel.add(headerTile, gbc);
        }
    }

    private void addScheduleRows(GridBagConstraints gbc, List<Schedule> schedules) {
        Map<String, List<Schedule>> scheduleMap = buildScheduleMap(schedules);

        for (int dayRow = 0; dayRow < days.length; dayRow++) {
            gbc.gridy = dayRow + 1;

            gbc.gridx = 0;
            gbc.weightx = 0.2;
            JPanel dayLabelPanel = createDayLabelPanel(days[dayRow], selectedWeekStart.plusDays(dayRow));
            gridPanel.add(dayLabelPanel, gbc);

            for (int periodColumn = 0; periodColumn < periods.length; periodColumn++) {
                gbc.gridx = periodColumn + 1;
                gbc.weightx = 1.0;

                JPanel cell = new JPanel();
                cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                cell.setBackground(Color.WHITE);
                cell.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));

                List<Schedule> schedulesInCell = scheduleMap.getOrDefault(dayRow + "_" + periods[periodColumn], List.of());
                if (schedulesInCell.isEmpty()) {
                    cell.add(Box.createVerticalGlue());
                } else {
                    for (Schedule schedule : schedulesInCell) {
                        cell.add(createCourseCard(schedule));
                        cell.add(Box.createRigidArea(new Dimension(0, 6)));
                    }
                }

                gridPanel.add(cell, gbc);
            }
        }
    }

    private JPanel createDayLabelPanel(String dayText, LocalDate actualDate) {
        JPanel dayLabelPanel = new JPanel(new GridBagLayout());
        dayLabelPanel.setBackground(new Color(255, 220, 180));
        dayLabelPanel.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));
        dayLabelPanel.setPreferredSize(new Dimension(90, 115));

        JLabel dayLabel = new JLabel("<html><center>" + dayText + "<br>" + DATE_FORMATTER.format(actualDate) + "</center></html>");
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dayLabelPanel.add(dayLabel);
        return dayLabelPanel;
    }

    private List<Schedule> loadSchedulesForCurrentWeek() {
        var student = SessionManager.getCurrentStudent();
        if (student == null || AppContext.getScheduleService() == null) {
            return List.of();
        }
        return AppContext.getScheduleService().getStudentScheduleByWeek(student.getId(), selectedWeekStart, selectedWeekStart.plusDays(6));
    }

    private Map<String, List<Schedule>> buildScheduleMap(List<Schedule> schedules) {
        Map<String, List<Schedule>> scheduleMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            String period = resolvePeriod(schedule.getStartSlot());
            int dayIndex = schedule.getDayOfWeek() - 1;
            String key = dayIndex + "_" + period;
            scheduleMap.computeIfAbsent(key, ignored -> new ArrayList<>()).add(schedule);
        }
        return scheduleMap;
    }

    private String resolvePeriod(Integer startSlot) {
        if (startSlot == null) {
            return "Sáng";
        }
        if (startSlot <= 5) {
            return "Sáng";
        }
        if (startSlot <= 10) {
            return "Chiều";
        }
        return "Tối";
    }

    private JPanel createCourseCard(Schedule schedule) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(187, 222, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("<html><center><b>Môn: " + schedule.getCourseSection().getCourse().getFullName()
                + " (" + schedule.getCourseSection().getCourse().getCourseCode() + ")</b></center></html>");
        nameLabel.setForeground(new Color(198, 40, 40));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String details = String.format(
                "<html><center>Ngày học: %s<br>Tiết: %d -> %d<br>Phòng: <b>%s</b><br>GV: <font color='blue'>%s</font><br>Tuần: %d</center></html>",
                DATE_FORMATTER.format(schedule.getStudyDate()),
                schedule.getStartSlot(),
                schedule.getEndSlot(),
                schedule.getRoom(),
                schedule.getCourseSection().getLecturer().getFullName(),
                schedule.getWeekNumber()
        );

        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(detailsLabel);
        return card;
    }

    private LocalDate getWeekStart(LocalDate date) {
        int offset = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        if (offset < 0) {
            offset += 7;
        }
        return date.minusDays(offset);
    }
}
