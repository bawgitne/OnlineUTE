package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class PaginationPanel extends JPanel {
    private final JButton previousButton = new JButton("Trang truoc");
    private final JButton nextButton = new JButton("Trang sau");
    private final JLabel infoLabel = new JLabel("Trang 1 / 1");

    public PaginationPanel(Runnable onPrevious, Runnable onNext) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(70, 85, 100));

        styleButton(previousButton);
        styleButton(nextButton);

        previousButton.addActionListener(e -> {
            if (onPrevious != null) {
                onPrevious.run();
            }
        });

        nextButton.addActionListener(e -> {
            if (onNext != null) {
                onNext.run();
            }
        });

        add(infoLabel);
        add(previousButton);
        add(nextButton);
    }

    public void updateState(int page, int totalPages, boolean hasPrevious, boolean hasNext) {
        int safeTotalPages = Math.max(totalPages, 1);
        infoLabel.setText("Trang " + page + " / " + safeTotalPages);
        previousButton.setEnabled(hasPrevious);
        nextButton.setEnabled(hasNext);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(210, 221, 235), 16, new Insets(8, 12, 8, 12)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
