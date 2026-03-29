package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Schedule;
import com.bangcompany.onlineute.View.Components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class SchedulePage extends JPanel implements Refreshable {
    private JPanel gridPanel;
    private final Color primaryColor = new Color(0, 85, 141);
    private final Color secondaryBg = new Color(245, 247, 249);
    private final String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    private final String[] periods = {"Sáng", "Chiều", "Tối"};

    public SchedulePage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Title Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new com.bangcompany.onlineute.View.Components.PageTitleLabel("THỜI KHÓA BIỂU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // 2. Main Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));

        // 2.1 Filter & Navigation (Year, Term, Week)
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);

        // 2.2 The Schedule Grid
        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        
        onEnter(); // Load data
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Lịch học Học kỳ hiện tại");
        currentScheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentScheduleLabel.setForeground(new Color(0, 85, 141));
        
        filterPanel.add(currentScheduleLabel);
        
        // Push the print button to the right if we had a BorderLayout or just keep flow
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        PrimaryButton btnPrint = new PrimaryButton("In thời khóa biểu");
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

        // Header Rows (Day labels)
        gbc.gridy = 0;
        // Day column header (Sáng, Chiều, Tối)
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

        // Fetch Data
        var student = SessionManager.getCurrentStudent();
        List<Schedule> scheduleList = new ArrayList<>();
        if (student != null && AppContext.getScheduleService() != null) {
            scheduleList = AppContext.getScheduleService().getStudentSchedule(student.getId());
        }

        // Map schedule by day and period (Morning: slots 1-5, Afternoon: 6-10, Evening: 11-14)
        Map<String, Schedule> scheduleMap = new HashMap<>(); // key: day_period
        for (Schedule s : scheduleList) {
            String period = "";
            if (s.getStartSlot() <= 5) period = "Sáng";
            else if (s.getStartSlot() <= 10) period = "Chiều";
            else period = "Tối";
            
            // day_of_week is 1 (Monday) to 7 (Sunday) in our day array (indices 0..6)
            scheduleMap.put((s.getDayOfWeek() - 1) + "_" + period, s);
        }

        // Grid contents
        for (int dayRow = 0; dayRow < days.length; dayRow++) {
            gbc.gridy = dayRow + 1;
            
            // Left column (Day Name)
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

            // Periods
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
        card.setBackground(new Color(187, 222, 251)); // Light blue
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(144, 202, 249)),
            new EmptyBorder(8, 10, 8, 10)
        ));

        // Course Name
        JLabel nameLabel = new JLabel("<html><center><b>Môn: " + s.getCourseSection().getCourse().getFullName() + " (" + s.getCourseSection().getCourse().getCourseCode() + ")</b></center></html>");
        nameLabel.setForeground(new Color(198, 40, 40)); // Reddish from image
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Details
        String details = String.format("<html><center>" +
                "Lớp: %s<br>" +
                "Tiết: %d -> %d<br>" +
                "Phòng: <b>%s</b><br>" +
                "GV: <font color='blue'>%s</font><br>" +
                "</center></html>", 
                "241103A", // Dummy
                s.getStartSlot(), s.getEndSlot(),
                s.getRoom(),
                s.getCourseSection().getLecturer().getFullName()
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
