package com.bangcompany.onlineute.View.Containers;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.Components.LeftBarButton;
import com.bangcompany.onlineute.View.Components.LeftBarTitle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LeftBar extends JPanel {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private Consumer<String> onNavigate;
    private final Map<String, LeftBarButton> navButtonMap = new HashMap<>();
    private LeftBarButton activeButton;

    public LeftBar(String userName, String userId, Role role, Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        
        setBackground(new Color(0, 85, 141));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(2, 0, 2, 0);

        addLogo();
        addAccountInfo(userName, userId, role);
        add(Box.createRigidArea(new Dimension(0, 30)), gbc);
        gbc.gridy++;

        buildMenu(role);

        // Push everything to the top
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    public void setActiveTab(String pageKey) {
        LeftBarButton btn = navButtonMap.get(pageKey);
        if (btn != null) {
            if (activeButton != null) activeButton.setActive(false);
            btn.setActive(true);
            activeButton = btn;
        }
    }

    private void buildMenu(Role role) {
        addSectionTitle("MENU CHÍNH");
        
        String roleName = (role != null) ? role.name() : "NONE";
        
        for (MenuItem item : MenuItem.values()) {
            if (item.isAccessibleBy(roleName)) {
                addNavButton(item.getLabel(), item.getIcon(), item.name());
            }
        }
    }

    private void addNavButton(String text, String icon, String pageKey) {
        LeftBarButton btn = new LeftBarButton(text, icon);
        navButtonMap.put(pageKey, btn);
        
        btn.addActionListener(e -> {
            setActiveTab(pageKey);
            if (onNavigate != null) onNavigate.accept(pageKey);
        });
        
        gbc.insets = new Insets(2, 0, 2, 0);
        add(btn, gbc);
        gbc.gridy++;
    }

    private void addSectionTitle(String title) {
        gbc.insets = new Insets(15, 0, 5, 0);
        add(new LeftBarTitle(title), gbc);
        gbc.gridy++;
    }

    private void addLogo() {
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(100, 127, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(logoLabel, gbc);
        gbc.gridy++;
    }

    private void addAccountInfo(String userName, String userId, Role role) {
        JPanel accountInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        accountInfoPanel.setOpaque(false);
        accountInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 50, 50);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        accountInfoPanel.add(avatarLabel);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.add(createProfileLabel(userName, 14, Font.BOLD, Color.WHITE));
        
        String roleStr = (role != null) ? role.toString() : "Người dùng";
        detailsPanel.add(createProfileLabel(roleStr, 11, Font.PLAIN, new Color(200, 200, 200)));
        detailsPanel.add(createProfileLabel(userId, 11, Font.PLAIN, new Color(200, 200, 200)));

        accountInfoPanel.add(detailsPanel);
        add(accountInfoPanel, gbc);
        gbc.gridy++;
    }

    private JLabel createProfileLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", style, size));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(2, 0, 2, 0));
        return label;
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() != null) {
            int parentWidth = getParent().getWidth();
            int calculatedWidth = (int) (parentWidth * 0.25);
            int maxWidth = 280;
            int minWidth = 150;
            return new Dimension(Math.min(maxWidth, Math.max(minWidth, calculatedWidth)), super.getPreferredSize().height);
        }
        return new Dimension(260, super.getPreferredSize().height);
    }
}
