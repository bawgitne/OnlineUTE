package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentManagementDashboard extends JPanel {
    private final JLabel totalStudentsValue = new JLabel("0");
    private final JLabel totalClassesValue = new JLabel("0");
    private final JLabel totalFacultiesValue = new JLabel("0");

    public StudentManagementDashboard() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.add(createSummaryCard("\u0054\u1ed5\u006e\u0067\u0020\u0073\u0069\u006e\u0068\u0020\u0076\u0069\u00ea\u006e", totalStudentsValue, new Color(0, 85, 141)));
        summaryPanel.add(createSummaryCard("\u0054\u1ed5\u006e\u0067\u0020\u006c\u1edb\u0070", totalClassesValue, new Color(0, 123, 167)));
        summaryPanel.add(createSummaryCard("\u0054\u1ed5\u006e\u0067\u0020\u006b\u0068\u006f\u0061", totalFacultiesValue, new Color(19, 135, 84)));

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
        guidePanel.setBackground(Color.WHITE);
        guidePanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(222, 230, 239), 24, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("\u0042\u1eaf\u0074\u0020\u0111\u1ea7\u0075\u0020\u0062\u1eb1\u006e\u0067\u0020\u00f4\u0020\u0074\u00ec\u006d\u0020\u006b\u0069\u1ebf\u006d");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 85, 141));

        JLabel descLabel = new JLabel("<html>\u0054\u00ec\u006d\u0020\u006e\u0068\u0061\u006e\u0068\u0020\u0074\u0068\u0065\u006f\u0020<b>\u006d\u00e3\u0020\u0073\u0069\u006e\u0068\u0020\u0076\u0069\u00ea\u006e</b>, <b>\u0068\u1ecd\u0020\u0074\u00ea\u006e</b>, <b>\u0065\u006d\u0061\u0069\u006c</b>, <b>\u006c\u1edb\u0070</b> \u0068\u006f\u1eb7\u0063 <b>\u006b\u0068\u006f\u0061</b>.<br>\u004b\u0068\u0069\u0020\u00f4\u0020\u0074\u00ec\u006d\u0020\u006b\u0069\u1ebf\u006d\u0020\u0063\u00f2\u006e\u0020\u0074\u0072\u1ed1\u006e\u0067, \u006d\u00e0\u006e\u0020\u006e\u00e0\u0079\u0020\u0073\u1ebd\u0020\u0068\u0069\u1ec3\u006e\u0020\u0074\u0068\u1ecb\u0020\u0064\u0061\u0073\u0068\u0062\u006f\u0061\u0072\u0064\u0020\u0074\u1ed5\u006e\u0067\u0020\u0071\u0075\u0061\u006e.</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(70, 85, 100));

        guidePanel.add(titleLabel);
        guidePanel.add(Box.createVerticalStrut(12));
        guidePanel.add(descLabel);

        add(summaryPanel, BorderLayout.NORTH);
        add(guidePanel, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        totalStudentsValue.setText(String.valueOf(AppContext.getStudentController().countAllStudents()));
        totalClassesValue.setText(String.valueOf(AppContext.getClassService().getAllClasses().size()));
        totalFacultiesValue.setText(String.valueOf(AppContext.getFacultyService().getAllFaculties().size()));
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(222, 230, 239), 24, new Insets(18, 18, 18, 18)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(96, 110, 126));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);
        return card;
    }
}
