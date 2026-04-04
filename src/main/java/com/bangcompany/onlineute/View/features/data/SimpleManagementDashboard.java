package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.IntSupplier;

public class SimpleManagementDashboard extends JPanel {
    private final JLabel totalValueLabel = new JLabel("0");
    private final IntSupplier totalSupplier;

    public SimpleManagementDashboard(String title, String guideHtml, IntSupplier totalSupplier) {
        this.totalSupplier = totalSupplier;

        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.add(createSummaryCard(title, totalValueLabel, new Color(0, 85, 141)));

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
        guidePanel.setBackground(Color.WHITE);
        guidePanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(222, 230, 239), 24, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Bắt đầu bằng ô tìm kiếm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 85, 141));

        JLabel descLabel = new JLabel(guideHtml);
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
        totalValueLabel.setText(String.valueOf(totalSupplier.getAsInt()));
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
