/**
 * Trang dashboard tổng quan trong các màn hình quản lý
 */
package com.bangcompany.onlineute.View.Components.container;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

public class Dashboard extends JPanel {
    private final List<MetricState> metricStates = new ArrayList<>();

    public static class Metric {
        private final String title;
        private final LongSupplier supplier;
        private final Color accentColor;

        public Metric(String title, LongSupplier supplier, Color accentColor) {
            this.title = title;
            this.supplier = supplier;
            this.accentColor = accentColor;
        }
    }

    private static class MetricState {
        private final JLabel valueLabel;
        private final LongSupplier supplier;

        private MetricState(JLabel valueLabel, LongSupplier supplier) {
            this.valueLabel = valueLabel;
            this.supplier = supplier;
        }
    }

    public Dashboard(String title, String guideHtml, LongSupplier totalSupplier) {
        this(List.of(new Metric(title, totalSupplier, AppTheme.PRIMARY_BLUE)), guideHtml);
    }

    public Dashboard(List<Metric> metrics, String guideHtml) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        int columns = Math.max(1, metrics == null ? 0 : metrics.size());
        JPanel summaryPanel = new JPanel(new GridLayout(1, columns, 16, 0));
        summaryPanel.setOpaque(false);
        if (metrics != null) {
            for (Metric metric : metrics) {
                JLabel valueLabel = new JLabel("0");
                metricStates.add(new MetricState(valueLabel, metric.supplier));
                summaryPanel.add(createSummaryCard(metric.title, valueLabel, metric.accentColor));
            }
        }

        JPanel guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
        guidePanel.setBackground(AppTheme.BACKGROUND_CARD);
        guidePanel.setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_CARD, new Insets(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel("Bắt đầu bằng ô tìm kiếm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE);

        JLabel descLabel = new JLabel(guideHtml);
        descLabel.setFont(AppTheme.FONT_BODY);
        descLabel.setForeground(new Color(70, 85, 100));

        guidePanel.add(titleLabel);
        guidePanel.add(Box.createVerticalStrut(12));
        guidePanel.add(descLabel);

        add(summaryPanel, BorderLayout.NORTH);
        add(guidePanel, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        for (MetricState metricState : metricStates) {
            metricState.valueLabel.setText(String.valueOf(metricState.supplier.getAsLong()));
        }
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_CARD, new Insets(18, 18, 18, 18)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.FONT_BODY);
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
