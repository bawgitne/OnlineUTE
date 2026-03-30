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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePage extends JPanel implements Refreshable {
    private JPanel gridPanel;
    private final Color primaryColor = new Color(0, 85, 141);
    private final String[] days = {"Thu 2", "Thu 3", "Thu 4", "Thu 5", "Thu 6", "Thu 7", "Chu nhat"};
    private final String[] periods = {"Sang", "Chieu", "Toi"};

    public SchedulePage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("THOI KHOA BIEU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        onEnter();
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Lich hoc hoc ky hien tai");
        currentScheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentScheduleLabel.setForeground(new Color(0, 85, 141));
        filterPanel.add(currentScheduleLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        PrimaryButton btnPrint = new PrimaryButton("In thoi khoa bieu");
        btnPrint.setPreferredSize(new Dimension(150, 45));
        filterPanel.add(btnPrint);
        return filterPanel;
    }

    @Override
    public void onEnter() {
        gridPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridy = 0;
        String[] colHeaders = {"", "Sang", "Chieu", "Toi"};
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

        var student = SessionManager.getCurrentStudent();
        List<Schedule> scheduleList = new ArrayList<>();
        if (student != null && AppContext.getScheduleService() != null) {
            scheduleList = AppContext.getScheduleService().getStudentSchedule(student.getId());
        }

        Map<String, Schedule> scheduleMap = new HashMap<>();
        for (Schedule s : scheduleList) {
            String period;
            if (s.getStartSlot() <= 5) {
                period = "Sang";
            } else if (s.getStartSlot() <= 10) {
                period = "Chieu";
            } else {
                period = "Toi";
            }
            scheduleMap.put((s.getDayOfWeek() - 1) + "_" + period, s);
        }

        for (int dayRow = 0; dayRow < days.length; dayRow++) {
            gbc.gridy = dayRow + 1;

            gbc.gridx = 0;
            gbc.weightx = 0.2;
            JPanel dayLabelPanel = new JPanel(new GridBagLayout());
            dayLabelPanel.setBackground(new Color(255, 220, 180));
            dayLabelPanel.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));
            JLabel dayLabel = new JLabel(days[dayRow]);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dayLabelPanel.add(dayLabel);
            dayLabelPanel.setPreferredSize(new Dimension(80, 100));
            gridPanel.add(dayLabelPanel, gbc);

            for (int pCol = 0; pCol < periods.length; pCol++) {
                gbc.gridx = pCol + 1;
                gbc.weightx = 1.0;

                JPanel cell = new JPanel(new BorderLayout());
                cell.setBackground(Color.WHITE);
                cell.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));

                Schedule s = scheduleMap.get(dayRow + "_" + periods[pCol]);
                if (s != null) {
                    cell.add(createCourseCard(s));
                }

                gridPanel.add(cell, gbc);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCourseCard(Schedule s) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(187, 222, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel nameLabel = new JLabel("<html><center><b>Mon: " + s.getCourseSection().getCourse().getFullName()
                + " (" + s.getCourseSection().getCourse().getCourseCode() + ")</b></center></html>");
        nameLabel.setForeground(new Color(198, 40, 40));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String details = String.format("<html><center>Lop: %s<br>Tiet: %d -> %d<br>Phong: <b>%s</b><br>GV: <font color='blue'>%s</font><br></center></html>",
                "241103A",
                s.getStartSlot(), s.getEndSlot(),
                s.getRoom(),
                s.getCourseSection().getLecturer().getFullName());

        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(detailsLabel);
        return card;
    }
}
