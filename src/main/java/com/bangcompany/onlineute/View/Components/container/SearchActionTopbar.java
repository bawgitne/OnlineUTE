/**
 * Thanh tìm kiếm và nút hành động nhanh
 */
package com.bangcompany.onlineute.View.Components.container;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedPaint;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.TextInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class SearchActionTopbar extends JPanel {

    private final TextInput searchInput = new TextInput("", false);
    private final Button clearButton = new Button("X", new Color(245, 247, 250), new Color(60, 80, 110));
    private final JPanel actionPanel = new JPanel(new BorderLayout());

    public SearchActionTopbar(String placeholder, String createButtonLabel, int minSearchLength, Consumer<String> onSearchChanged, Runnable onCreateNew) {
        setLayout(new BorderLayout(16, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, 8, 0));
        actionPanel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // tìm kiếm ngay lập tức khi gõ
        searchInput.setToolTipText(placeholder);
        searchInput.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (onSearchChanged != null) onSearchChanged.accept(searchInput.getValue());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (onSearchChanged != null) onSearchChanged.accept(searchInput.getValue());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (onSearchChanged != null) onSearchChanged.accept(searchInput.getValue());
            }
        });

        clearButton.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setVisible(true); 
        clearButton.addActionListener(e -> clearSearch());

        searchPanel.add(searchInput, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        add(searchPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.EAST);
        setCreateAction(createButtonLabel, onCreateNew);
    }

    public String getKeyword() {
        return searchInput.getValue();
    }

    public void clearSearch() {
        searchInput.setValue("");
    }

    public void setCreateAction(String createButtonLabel, Runnable onCreateNew) {
        actionPanel.removeAll();
        if (onCreateNew != null && createButtonLabel != null && !createButtonLabel.isBlank()) {
            Button createButton = new Button(createButtonLabel);
            createButton.addActionListener(e -> onCreateNew.run());
            actionPanel.add(createButton, BorderLayout.CENTER);
        }
        actionPanel.revalidate();
        actionPanel.repaint();
    }

}
