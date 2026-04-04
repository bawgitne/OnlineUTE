package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class SearchActionTopbar extends JPanel {
    private static final int DEBOUNCE_MS = 350;

    private final JTextField searchField = new JTextField();
    private final Timer debounceTimer;
    private final JButton clearButton = new JButton("\u00d7");
    private final JPanel actionPanel = new JPanel(new BorderLayout());
    private final int minSearchLength;

    public SearchActionTopbar(String placeholder, String createButtonLabel, int minSearchLength, Consumer<String> onSearchChanged, Runnable onCreateNew) {
        setLayout(new BorderLayout(16, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, 8, 0));
        this.minSearchLength = Math.max(1, minSearchLength);
        actionPanel.setOpaque(false);

        debounceTimer = new Timer(DEBOUNCE_MS, e -> {
            if (onSearchChanged == null) {
                return;
            }

            String keyword = searchField.getText();
            String trimmedKeyword = keyword == null ? "" : keyword.trim();
            if (trimmedKeyword.isEmpty() || trimmedKeyword.length() >= this.minSearchLength) {
                onSearchChanged.accept(keyword);
            }
        });
        debounceTimer.setRepeats(false);

        JPanel searchPanel = new RoundedSearchPanel();
        searchPanel.setLayout(new BorderLayout(10, 0));
        searchPanel.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel searchLabel = new JLabel("");
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        searchLabel.setForeground(new Color(90, 110, 130));

        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(30, 45, 65));
        searchField.setToolTipText(placeholder);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateClearButtonVisibility();
                debounceTimer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateClearButtonVisibility();
                debounceTimer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateClearButtonVisibility();
                debounceTimer.restart();
            }
        });

        clearButton.setBorder(null);
        clearButton.setFocusPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setOpaque(false);
        clearButton.setForeground(new Color(120, 135, 150));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clearButton.setVisible(false);
        clearButton.addActionListener(e -> clearSearch());

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        add(searchPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.EAST);
        setCreateAction(createButtonLabel, onCreateNew);
    }

    public String getKeyword() {
        return searchField.getText();
    }

    public void clearSearch() {
        searchField.setText("");
        updateClearButtonVisibility();
        debounceTimer.restart();
    }

    public void setCreateAction(String createButtonLabel, Runnable onCreateNew) {
        actionPanel.removeAll();
        if (onCreateNew != null && createButtonLabel != null && !createButtonLabel.isBlank()) {
            PrimaryButton createButton = new PrimaryButton(createButtonLabel);
            createButton.addActionListener(e -> onCreateNew.run());
            actionPanel.add(createButton, BorderLayout.CENTER);
        }
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void updateClearButtonVisibility() {
        clearButton.setVisible(!searchField.getText().trim().isEmpty());
    }

    private static class RoundedSearchPanel extends JPanel {
        private RoundedSearchPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g2.setColor(new Color(210, 221, 235));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
