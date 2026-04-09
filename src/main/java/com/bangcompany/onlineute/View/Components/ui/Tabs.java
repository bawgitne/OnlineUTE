package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabs extends JPanel {
    private final JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final List<TabItem> items = new ArrayList<>();
    private Consumer<Integer> onTabChanged;
    private int activeIndex = -1;

    public Tabs() {
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 230, 236)),
                new EmptyBorder(0, 0, 6, 0)
        ));

        contentPanel.setOpaque(false);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addTab(String title, JComponent content) {
        String key = "tab-" + items.size();
        TabButton button = new TabButton(title, items.size());
        TabItem item = new TabItem(key, title, button, content);
        items.add(item);

        headerPanel.add(button);
        contentPanel.add(content, key);

        if (activeIndex == -1) {
            setActive(0);
        }
    }

    public void setOnTabChanged(Consumer<Integer> listener) {
        this.onTabChanged = listener;
    }

    public JComponent getActiveComponent() {
        if (activeIndex < 0 || activeIndex >= items.size()) {
            return null;
        }
        return items.get(activeIndex).content;
    }

    public void setActive(int index) {
        if (index < 0 || index >= items.size()) {
            return;
        }
        activeIndex = index;
        TabItem item = items.get(index);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, item.key);
        updateButtons();
        if (onTabChanged != null) {
            onTabChanged.accept(index);
        }
    }

    private void updateButtons() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).button.setActive(i == activeIndex);
        }
    }

    private static final class TabItem {
        private final String key;
        private final String title;
        private final TabButton button;
        private final JComponent content;

        private TabItem(String key, String title, TabButton button, JComponent content) {
            this.key = key;
            this.title = title;
            this.button = button;
            this.content = content;
        }
    }

    private static final class TabButton extends JButton {
        private final int index;
        private boolean active;

        private TabButton(String text, int index) {
            super(text);
            this.index = index;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(new EmptyBorder(8, 16, 8, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setActive(false);
            addActionListener(e -> {
                Container parent = getParent();
                while (parent != null && !(parent instanceof Tabs)) {
                    parent = parent.getParent();
                }
                if (parent instanceof Tabs tabs) {
                    tabs.setActive(index);
                }
            });
        }

        private void setActive(boolean active) {
            this.active = active;
            setForeground(active ? AppTheme.PRIMARY_BLUE : new Color(78, 90, 104));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (active) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int height = getHeight();
                int width = getWidth();
                int barHeight = 3;
                int barWidth = Math.max(24, width - 24);
                int barX = (width - barWidth) / 2;
                int barY = height - barHeight;
                g2.setColor(AppTheme.PRIMARY_BLUE);
                g2.fillRoundRect(barX, barY, barWidth, barHeight, barHeight, barHeight);
                g2.dispose();
            }
        }
    }
}
