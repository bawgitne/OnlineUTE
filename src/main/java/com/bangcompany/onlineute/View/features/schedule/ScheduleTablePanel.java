package com.bangcompany.onlineute.View.features.schedule;

import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cái giao diện hiện table lên, ko chứa logic
 */
public class ScheduleTablePanel extends JPanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Color primaryColor = AppTheme.PRIMARY_BLUE;
    private final String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    private final String[] periods = {"Sáng", "Chiều", "Tối"};

    private final JPanel gridPanel = new JPanel(new GridBagLayout());
    private LocalDate currentWeekStart = LocalDate.now();

    public ScheduleTablePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        gridPanel.setBackground(AppTheme.BACKGROUND_CARD);
        gridPanel.setBorder(RoundedBorders.outline(AppTheme.BORDER_LIGHT, AppTheme.RADIUS_PANEL, new Insets(0, 0, 0, 0)));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void renderWeek(LocalDate weekStart, List<Schedule> schedules) {
        if (weekStart != null) {
            currentWeekStart = weekStart;
        }
        gridPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        addHeaders(gbc);
        addScheduleRows(gbc, schedules == null ? List.of() : schedules);

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
            headerTile.setBorder(RoundedBorders.outline(new Color(255, 255, 255, 50), AppTheme.RADIUS_INPUT, new Insets(0, 0, 0, 0)));

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
            JPanel dayLabelPanel = createDayLabelPanel(days[dayRow], currentWeekStart.plusDays(dayRow));
            gridPanel.add(dayLabelPanel, gbc);

            for (int periodColumn = 0; periodColumn < periods.length; periodColumn++) {
                gbc.gridx = periodColumn + 1;
                gbc.weightx = 1.0;

                JPanel cell = new JPanel();
                cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                cell.setBackground(AppTheme.BACKGROUND_CARD);
                cell.setBorder(new MatteBorder(0, 0, 1, 1, AppTheme.BORDER_LIGHT));

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
        dayLabelPanel.setBackground(new Color(255, 240, 220));
        dayLabelPanel.setBorder(new MatteBorder(0, 0, 1, 1, AppTheme.BORDER_LIGHT));
        dayLabelPanel.setPreferredSize(new Dimension(90, 115));

        JLabel dayLabel = new JLabel("<html><center>" + dayText + "<br>" + DATE_FORMATTER.format(actualDate) + "</center></html>");
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dayLabelPanel.add(dayLabel);
        return dayLabelPanel;
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
        card.setBackground(new Color(225, 235, 255));
        card.setBorder(RoundedBorders.paddedOutline(new Color(180, 210, 255), AppTheme.RADIUS_PANEL, new Insets(8, 10, 8, 10)));
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
}
