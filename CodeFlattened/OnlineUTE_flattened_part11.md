# Project Digest Continued: OnlineUTE
Generated on: Sat Apr 04 2026 09:54:52 GMT+0700 (Indochina Time)


## src\main\java\com\bangcompany\onlineute\View\Components\AppLogoHeader.java <a id="AppLogoHeader_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AppLogoHeader - Reusable component for displaying the main application logo and unit name.
 */
public class AppLogoHeader extends JPanel {

    public AppLogoHeader() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full name line 1
        JLabel uniName1 = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ KỸ THUẬT");
        uniName1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName1.setForeground(new Color(0, 40, 80));
        uniName1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full name line 2
        JLabel uniName2 = new JLabel("TP.HCM");
        uniName2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uniName2.setForeground(new Color(0, 40, 80));
        uniName2.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(logoLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(uniName1);
        add(uniName2);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\LabelValuePanel.java <a id="LabelValuePanel_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelValuePanel extends JPanel {
    public LabelValuePanel(String label, String value) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(new Color(70, 86, 107));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueText.setForeground(new Color(33, 37, 41));

        add(labelText, BorderLayout.NORTH);
        add(valueText, BorderLayout.CENTER);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\leftbar\LeftBarButton.java <a id="LeftBarButton_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.RoundedPainter`
- `javax.imageio.ImageIO`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`
- `java.awt.event.MouseAdapter`
- `java.awt.event.MouseEvent`
- `java.awt.image.BufferedImage`
- `java.io.File`
- `java.io.IOException`

```java
package com.bangcompany.onlineute.View.Components.leftbar;

import com.bangcompany.onlineute.View.Components.RoundedPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LeftBarButton extends JButton {
    private static final Color DEFAULT_BACKGROUND = new Color(0, 85, 141);
    //private static final Color ACTIVE_BACKGROUND = new Color(0, 66, 110);
    private static final Color ACTIVE_BACKGROUND = new Color(0, 85, 141);
    private static final Color DEFAULT_FOREGROUND = Color.WHITE;
    private static final Color ACTIVE_FOREGROUND = new Color(201, 138, 23);
    private static final int DEFAULT_LEFT_PADDING = 14;
    private static final int HOVER_LEFT_PADDING = 24;

    private final String iconName;
    private final ImageIcon defaultIcon;
    private final ImageIcon activeIcon;
    private boolean active;

    public LeftBarButton(String text, String iconName) {
        this.iconName = iconName;
        setText(text);
        setHorizontalAlignment(SwingConstants.LEFT);
        setForeground(DEFAULT_FOREGROUND);
        setBackground(DEFAULT_BACKGROUND);
        RoundedPainter.prepareButton(this);
        setRolloverEnabled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setPreferredSize(new Dimension(220, 45));
        setIconTextGap(10);
        setBorder(new EmptyBorder(12, DEFAULT_LEFT_PADDING, 12, 16));

        defaultIcon = loadIcon(iconName, DEFAULT_FOREGROUND);
        activeIcon = loadIcon(iconName, ACTIVE_FOREGROUND);
        setIcon(defaultIcon);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                applyHoverStyle(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                applyHoverStyle(false);
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        setBackground(active ? ACTIVE_BACKGROUND : DEFAULT_BACKGROUND);
        setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 15));
        setForeground(active ? ACTIVE_FOREGROUND : DEFAULT_FOREGROUND);
        setIcon(active ? activeIcon : defaultIcon);
    }

    private void applyHoverStyle(boolean hovering) {
        int leftPadding = hovering ? HOVER_LEFT_PADDING : DEFAULT_LEFT_PADDING;
        setBorder(new EmptyBorder(12, leftPadding, 12, 16));
        setBackground(active ? ACTIVE_BACKGROUND : DEFAULT_BACKGROUND);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedPainter.fillRoundBackground(g, this, getBackground(), 16);
        super.paintComponent(g);
    }

    private ImageIcon loadIcon(String iconName, Color color) {
        BufferedImage sourceImage;
        try {
            sourceImage = ImageIO.read(new File("public/Icon/" + iconName));
        } catch (IOException e) {
            return null;
        }

        Image scaledImage = sourceImage.getScaledInstance(18, 18, Image.SCALE_SMOOTH);

        BufferedImage tintedImage = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tintedImage.createGraphics();
        g2.drawImage(scaledImage, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(color);
        g2.fillRect(0, 0, 18, 18);
        g2.dispose();

        return new ImageIcon(tintedImage);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\InputGroup.java <a id="InputGroup_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class InputGroup extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JTextField textField;

    public InputGroup(String labelText, boolean isPassword) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        setBorder(new RoundedTitleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                18
        ));

        textField = isPassword ? new JPasswordField() : new JTextField();
        textField.setOpaque(false);
        textField.setBorder(null);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(new Color(20, 30, 40));
        textField.setCaretColor(new Color(0, 84, 140));

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        setPreferredSize(new Dimension(200, 56));

        add(textField, BorderLayout.CENTER);
    }

    public String getValue() {
        return textField.getText();
    }

    public void setValue(String value) {
        textField.setText(value == null ? "" : value);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }

    public JTextField getTextField() {
        return textField;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\leftbar\LeftBarTitle.java <a id="LeftBarTitle_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LeftBarTitle extends JLabel {
    public LeftBarTitle(String text) {
        super(text);
        setForeground(new Color(209, 205, 20));
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setBorder(new EmptyBorder(10, 12, 8, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\leftbar\NavMenu.java <a id="NavMenu_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`
- `java.util.HashMap`
- `java.util.List`
- `java.util.Map`
- `java.util.function.Consumer`

```java
package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NavMenu extends JPanel {
    private final Map<String, LeftBarButton> buttonMap = new HashMap<>();
    private LeftBarButton activeButton;

    public NavMenu(List<SidebarItem> menuItems, Consumer<String> onNavigate) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        for (SidebarItem item : menuItems) {
            if (item.isTitle()) {
                gbc.insets = new Insets(15, 0, 5, 0);
                add(new LeftBarTitle(item.getLabel()), gbc);
                gbc.gridy++;
                continue;
            }

            gbc.insets = new Insets(4, 0, 4, 0);

            LeftBarButton button = new LeftBarButton(item.getLabel(), item.getIcon());
            buttonMap.put(item.getKey(), button);

            button.addActionListener(e -> {
                setActiveTab(item.getKey());
                if (onNavigate != null) {
                    onNavigate.accept(item.getKey());
                }
            });

            add(button, gbc);
            gbc.gridy++;
        }

        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    public void setActiveTab(String pageKey) {
        LeftBarButton button = buttonMap.get(pageKey);
        if (button == null) {
            return;
        }

        if (activeButton != null) {
            activeButton.setActive(false);
        }

        button.setActive(true);
        activeButton = button;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\leftbar\SidebarItem.java <a id="SidebarItem_java"></a>

```java
package com.bangcompany.onlineute.View.Components.leftbar;

public class SidebarItem {
    private final boolean title;
    private final String key;
    private final String label;
    private final String icon;

    private SidebarItem(boolean title, String key, String label, String icon) {
        this.title = title;
        this.key = key;
        this.label = label;
        this.icon = icon;
    }

    public static SidebarItem title(String label) {
        return new SidebarItem(true, null, label, null);
    }

    public static SidebarItem tab(String key, String label, String icon) {
        return new SidebarItem(false, key, label, icon);
    }

    public boolean isTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }
}

```

## src\main\java\com\bangcompany\onlineute\Service\UserProfileService.java <a id="UserProfileService_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Model.Entity.UserProfile`
- `java.util.Optional`

```java
package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findByAccountId(Long accountId);
    UserProfile getCurrentUserProfile();
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\leftbar\UserProfileCard.java <a id="UserProfileCard_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components.leftbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserProfileCard extends JPanel {

    public UserProfileCard(String userName, String userId, String roleDisplayName) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(createLogo());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createAccountInfo(userName, userId, roleDisplayName));
    }

    private JLabel createLogo() {
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("public/ute_logo.png");
            Image img = icon.getImage().getScaledInstance(100, 127, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        return logoLabel;
    }

    private JPanel createAccountInfo(String userName, String userId, String roleDisplayName) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

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
        panel.add(avatarLabel);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.add(createLabel(userName, 14, Font.BOLD, Color.WHITE));
        detailsPanel.add(createLabel(roleDisplayName, 11, Font.PLAIN, new Color(200, 200, 200)));
        detailsPanel.add(createLabel(userId, 11, Font.PLAIN, new Color(200, 200, 200)));

        panel.add(detailsPanel);
        return panel;
    }

    private JLabel createLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", style, size));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(2, 0, 2, 0));
        return label;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\PageTitleLabel.java <a id="PageTitleLabel_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageTitleLabel extends RoundedPanel {
    public PageTitleLabel(String text) {
        super(18);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(6, 12, 6, 12));

        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(0, 85, 141));
        label.setOpaque(false);

        add(label, BorderLayout.CENTER);
        setPreferredSize(new Dimension(0, 44));
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\RoundedPainter.java <a id="RoundedPainter_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public final class RoundedPainter {
    private RoundedPainter() {
    }

    public static void prepareButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    public static void fillRoundBackground(Graphics graphics, JComponent component, Color color, int arc) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, component.getWidth(), component.getHeight(), arc, arc);
        g2.dispose();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\PaginationPanel.java <a id="PaginationPanel_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
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

```

## src\main\java\com\bangcompany\onlineute\View\Components\PrimaryButton.java <a id="PrimaryButton_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {
    public PrimaryButton(String text) {
        super(text);
        setBackground(new Color(0, 84, 140));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        RoundedPainter.prepareButton(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedPainter.fillRoundBackground(g, this, getBackground(), 18);
        super.paintComponent(g);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\SelectGroup.java <a id="SelectGroup_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.plaf.basic.BasicComboBoxUI`
- `javax.swing.plaf.basic.ComboPopup`
- `javax.swing.plaf.basic.BasicComboPopup`
- `java.awt`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.util.List;

/**
 * SelectGroup - A beautiful custom ComboBox wrapper.
 * Completely overrides native Swing LookAndFeel to achieve a Flat Design appearance.
 */
public class SelectGroup<T> extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JComboBox<T> comboBox;

    public SelectGroup(String labelText, List<T> items) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        setBorder(new RoundedTitleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                18
        ));

        // Create the combo box and populate it
        comboBox = new JComboBox<>();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
        
        // 1. Remove standard border and background
        comboBox.setOpaque(false);
        comboBox.setBackground(FIELD_BACKGROUND);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(new Color(20, 30, 40));
        comboBox.setBorder(BorderFactory.createEmptyBorder());
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (index == -1) {
                    label.setBackground(FIELD_BACKGROUND);
                    label.setForeground(new Color(20, 30, 40));
                } else if (isSelected) {
                    label.setBackground(new Color(229, 239, 252));
                    label.setForeground(new Color(0, 84, 140));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(20, 30, 40));
                }
                return label;
            }
        });

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BE");
                button.setFont(new Font("Segoe UI", Font.BOLD, 9));
                button.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 2));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(new Color(120, 140, 160));
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setForeground(new Color(0, 84, 140));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        button.setForeground(new Color(120, 140, 160));
                    }
                });
                return button;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                popup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                return popup;
            }
        });

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        setPreferredSize(new Dimension(200, 56));

        add(comboBox, BorderLayout.CENTER);
    }

    public T getSelectedValue() {
        return (T) comboBox.getSelectedItem();
    }

    public JComboBox<T> getComboBox() {
        return comboBox;
    }

    public void setSelectedItem(T item) {
        comboBox.setSelectedItem(item);
    }
    
    public void setItems(List<T> items) {
        comboBox.removeAllItems();
        if (items != null) {
            for (T item : items) {
                comboBox.addItem(item);
            }
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\RoundedPanel.java <a id="RoundedPanel_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private final int arc;

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\RoundedOutlineBorder.java <a id="RoundedOutlineBorder_java"></a>

### Dependencies

- `javax.swing.border.AbstractBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedOutlineBorder extends AbstractBorder {
    private final Color borderColor;
    private final int arc;
    private final Insets insets;

    public RoundedOutlineBorder(Color borderColor, int arc, Insets insets) {
        this.borderColor = borderColor;
        this.arc = arc;
        this.insets = insets;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = this.insets.top;
        insets.left = this.insets.left;
        insets.bottom = this.insets.bottom;
        insets.right = this.insets.right;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
        g2.dispose();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\SearchActionTopbar.java <a id="SearchActionTopbar_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.event.DocumentEvent`
- `javax.swing.event.DocumentListener`
- `java.awt`
- `java.util.function.Consumer`

```java
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

```

## src\main\java\com\bangcompany\onlineute\View\Components\RoundedTitleBorder.java <a id="RoundedTitleBorder_java"></a>

### Dependencies

- `javax.swing.border.AbstractBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedTitleBorder extends AbstractBorder {
    private final String title;
    private final Color borderColor;
    private final Color titleColor;
    private final Color backgroundColor;
    private final Font titleFont;
    private final int arc;

    public RoundedTitleBorder(String title, Color borderColor, Color titleColor, Color backgroundColor, Font titleFont, int arc) {
        this.title = title == null ? "" : title;
        this.borderColor = borderColor;
        this.titleColor = titleColor;
        this.backgroundColor = backgroundColor;
        this.titleFont = titleFont;
        this.arc = arc;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(18, 12, 10, 12);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 18;
        insets.left = 12;
        insets.bottom = 10;
        insets.right = 12;
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleHeight = fm.getAscent();
        int titleX = x + 12;
        int titleY = y + titleHeight;

        int borderY = y + (titleHeight / 2) + 2;
        int borderHeight = height - borderY - 1;

        g2.setColor(borderColor);
        g2.drawRoundRect(x, borderY, width - 1, borderHeight, arc, arc);

        int patchX = titleX - 4;
        int patchY = y;
        int patchWidth = titleWidth + 8;
        int patchHeight = titleHeight + 2;
        g2.setColor(backgroundColor);
        g2.fillRect(patchX, patchY, patchWidth, patchHeight);

        g2.setColor(titleColor);
        g2.drawString(title, titleX, titleY);
        g2.dispose();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\TagChip.java <a id="TagChip_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TagChip extends JLabel {
    public TagChip(String text) {
        super(text);
        setOpaque(true);
        setBackground(new Color(238, 244, 251));
        setForeground(new Color(24, 70, 121));
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setBorder(new EmptyBorder(8, 12, 8, 12));
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\announcement\AnnouncementPage.java <a id="AnnouncementPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import java.awt.*;

public class AnnouncementPage extends JPanel implements Refreshable {
    private final AnnouncementTable announcementTable;

    public AnnouncementPage() {
        setLayout(new BorderLayout());
        announcementTable = new AnnouncementTable();

        PageScaffold scaffold = new PageScaffold("Thông báo");
        scaffold.setBody(announcementTable);
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        announcementTable.refreshData();
    }
}


```

## src\main\java\com\bangcompany\onlineute\View\Components\TableStyles.java <a id="TableStyles_java"></a>

### Dependencies

- `javax.swing`
- `javax.swing.table.DefaultTableCellRenderer`
- `javax.swing.table.JTableHeader`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class TableStyles {
    private TableStyles() {}

    public static void applyBlueHeader(JTable table) {
        applyModernTable(table);
    }

    public static void applyModernTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(12, 82, 140));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 36));

        table.setRowHeight(34);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(225, 236, 250));
        table.setSelectionForeground(new Color(17, 51, 88));
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 251, 255));
                    c.setForeground(new Color(25, 40, 60));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        base.setOpaque(true);
        table.setDefaultRenderer(Object.class, base);
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(225, 230, 236), 22, new Insets(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder()
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    public static void centerColumns(JTable table, int... columns) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setOpaque(true);
        for (int c : columns) {
            table.getColumnModel().getColumn(c).setCellRenderer(center);
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\announcement\CreateAnnouncementPage.java <a id="CreateAnnouncementPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.CourseSection`
- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.SelectGroup`
- `com.bangcompany.onlineute.View.Components.TextAreaGroup`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TextAreaGroup;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CreateAnnouncementPage extends JPanel implements Refreshable {
    private final JPanel mainPanel;
    private InputGroup titleInput;
    private TextAreaGroup contentInput;
    private SelectGroup<String> adminTargetSelect;
    private SelectGroup<CourseSectionItem> lecturerClassSelect;

    private static class CourseSectionItem {
        final CourseSection section;

        CourseSectionItem(CourseSection section) {
            this.section = section;
        }

        @Override
        public String toString() {
            String courseName = section.getCourse() != null ? section.getCourse().getFullName() : "Unknown course";
            return courseName + " - Lớp: " + section.getId();
        }
    }

    public CreateAnnouncementPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 50, 0, 50));
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("SOẠN THÔNG BÁO MỚI"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 50, 50, 50));

        buildForm();

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void buildForm() {
        mainPanel.removeAll();
        String role = SessionManager.getRole();
        if (role == null) {
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        if ("ADMIN".equals(role)) {
            adminTargetSelect = new SelectGroup<>("Gửi đến", List.of("Toàn trường", "Toàn bộ Sinh viên", "Toàn bộ Giảng viên"));
            mainPanel.add(adminTargetSelect, gbc);
        } else if ("LECTURER".equals(role)) {
            List<CourseSectionItem> myClasses = List.of();
            var lecturer = SessionManager.getCurrentLecturer();
            if (lecturer != null && AppContext.getNotificationController() != null) {
                myClasses = AppContext.getNotificationController()
                        .getCourseSectionsByLecturerId(lecturer.getId())
                        .stream()
                        .map(CourseSectionItem::new)
                        .toList();
            }
            lecturerClassSelect = new SelectGroup<>("Chọn lớp học phần", myClasses);
            mainPanel.add(lecturerClassSelect, gbc);
        }

        gbc.gridy++;
        titleInput = new InputGroup("Tiêu đề", false);
        mainPanel.add(titleInput, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentInput = new TextAreaGroup("Nội dung thông báo", 250);
        mainPanel.add(contentInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        PrimaryButton btnSend = new PrimaryButton("Gửi thông báo");
        btnSend.setPreferredSize(new Dimension(200, 45));
        btnSend.addActionListener(e -> submitAnnouncement(role));
        mainPanel.add(btnSend, gbc);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void submitAnnouncement(String role) {
        String title = titleInput.getValue().trim();
        String content = contentInput.getValue().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String targetType;
        Long targetClassId = null;
        String senderName;

        if ("ADMIN".equals(role)) {
            String selection = adminTargetSelect.getSelectedValue();
            if (selection != null && selection.contains("Sinh viên")) {
                targetType = "ALL_STUDENTS";
            } else if (selection != null && selection.contains("Giảng viên")) {
                targetType = "ALL_LECTURERS";
            } else {
                targetType = "ALL";
            }
            senderName = "Phòng Đào tạo";
        } else {
            CourseSectionItem selectedClass = lecturerClassSelect.getSelectedValue();
            if (selectedClass == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn lớp để gửi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            targetType = "COURSE_SECTION";
            targetClassId = selectedClass.section.getId();
            var lecturer = SessionManager.getCurrentLecturer();
            senderName = lecturer != null ? "GV. " + lecturer.getFullName() : "Giảng viên";
        }

        try {
            AppContext.getNotificationController().createAnnouncement(title, content, targetType, targetClassId, senderName);
            JOptionPane.showMessageDialog(this, "Gửi thông báo thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            titleInput.setValue("");
            contentInput.setValue("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onEnter() {
        buildForm();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\account\ChangePasswordPage.java <a id="ChangePasswordPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordPage extends JPanel implements Refreshable {
    private final InputGroup oldPassInput;
    private final InputGroup newPassInput;
    private final InputGroup confirmPassInput;

    public ChangePasswordPage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("DOI MAT KHAU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));
        form.setPreferredSize(new Dimension(500, 450));
        form.setMaximumSize(new Dimension(500, 450));

        JLabel title = new JLabel("DOI MAT KHAU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 80, 140));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);

        JLabel subTitle = new JLabel("Nhap mat khau moi de bao mat tai khoan");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(subTitle);

        form.add(Box.createRigidArea(new Dimension(0, 25)));

        oldPassInput = new InputGroup("Mat khau hien tai", true);
        newPassInput = new InputGroup("Mat khau moi", true);
        confirmPassInput = new InputGroup("Xac nhan mat khau moi", true);

        oldPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPassInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(oldPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(newPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(confirmPassInput);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        PrimaryButton btnSave = new PrimaryButton("Cap nhat mat khau");
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Chuc nang doi mat khau dang duoc phat trien.",
                "Thong bao",
                JOptionPane.INFORMATION_MESSAGE
        ));
        form.add(btnSave);

        centerPanel.add(form);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        oldPassInput.setValue("");
        newPassInput.setValue("");
        confirmPassInput.setValue("");
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\Components\TextAreaGroup.java <a id="TextAreaGroup_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.Components;

import javax.swing.*;
import java.awt.*;

public class TextAreaGroup extends JPanel {
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(178, 205, 234);
    private static final Color LABEL_COLOR = new Color(77, 111, 146);
    private final JTextArea textArea;

    public TextAreaGroup(String labelText, int preferredHeight) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(FIELD_BACKGROUND);

        setBorder(new RoundedTitleBorder(
                labelText,
                BORDER_COLOR,
                LABEL_COLOR,
                FIELD_BACKGROUND,
                new Font("Segoe UI", Font.BOLD, 11),
                18
        ));

        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(FIELD_BACKGROUND);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setForeground(new Color(20, 30, 40));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);
        textArea.setCaretColor(new Color(0, 84, 140));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        setPreferredSize(new Dimension(200, preferredHeight));

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getValue() {
        return textArea.getText();
    }

    public void setValue(String value) {
        textArea.setText(value == null ? "" : value);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\auth\LoginForm.java <a id="LoginForm_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`
- `java.awt.event.MouseAdapter`
- `java.awt.event.MouseEvent`
- `java.util.function.BiConsumer`

```java
package com.bangcompany.onlineute.View.features.auth;

import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class LoginForm extends JPanel {
    private final BiConsumer<String, String> onSubmit;
    private final InputGroup usernameGroup;
    private final InputGroup passwordGroup;

    public LoginForm(BiConsumer<String, String> onSubmit) {
        this.onSubmit = onSubmit;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));
        setPreferredSize(new Dimension(500, 450));
        setMaximumSize(new Dimension(500, 450));

        JLabel loginTitle = new JLabel("ĐĂNG NHẬP");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        loginTitle.setForeground(new Color(40, 80, 140));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(loginTitle);

        JLabel subTitle = new JLabel("Cổng thông tin đào tạo");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(subTitle);

        add(Box.createRigidArea(new Dimension(0, 25)));

        usernameGroup = new InputGroup("Tên đăng nhập", false);
        passwordGroup = new InputGroup("Mật khẩu", true);
        usernameGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordGroup.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(usernameGroup);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(passwordGroup);
        add(Box.createRigidArea(new Dimension(0, 15)));

        PrimaryButton btnLogin = new PrimaryButton("Đăng nhập");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            if (this.onSubmit != null) {
                this.onSubmit.accept(usernameGroup.getValue().trim(), passwordGroup.getValue().trim());
            }
        });
        add(btnLogin);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(230, 230, 230));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel forgotPassLabel = new JLabel("Quên mật khẩu");
        forgotPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPassLabel.setForeground(Color.GRAY);
        forgotPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPassLabel.setForeground(new Color(0, 84, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotPassLabel.setForeground(Color.GRAY);
            }
        });
        add(forgotPassLabel);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\announcement\AnnouncementTable.java <a id="AnnouncementTable_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.Announcement`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.time.format.DateTimeFormatter`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnnouncementTable extends JPanel {
    private static final String[] COLUMN_NAMES = {"Tiêu đề", "Người gửi", "Thời gian gửi"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final DefaultTableModel model;

    public AnnouncementTable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        model = new DefaultTableModel(COLUMN_NAMES, 0);
        loadAnnouncements();

        JTable table = new JTable(model);
        TableStyles.applyModernTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        model.setRowCount(0);

        if (AppContext.getNotificationController() == null) {
            model.addRow(new Object[]{"Không có thông báo mới", "", ""});
            return;
        }

        List<Announcement> announcements = AppContext.getNotificationController().getAnnouncementsForCurrentUser();
        for (Announcement announcement : announcements) {
            model.addRow(new Object[]{
                    announcement.getTitle(),
                    announcement.getSenderName(),
                    announcement.getCreatedAt().format(DATE_TIME_FORMATTER)
            });
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"Không có thông báo mới", "", ""});
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\attendance\AttendancePage.java <a id="AttendancePage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.CourseRegistration`
- `com.bangcompany.onlineute.Model.Entity.Mark`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.features.attendance;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendancePage extends JPanel implements Refreshable {
    private final DefaultTableModel tableModel;

    public AttendancePage() {
        setLayout(new BorderLayout());

        String[] cols = new String[18];
        cols[0] = "STT";
        cols[1] = "Ma mon hoc";
        cols[2] = "Ten mon hoc";
        for (int i = 1; i <= 15; i++) {
            cols[i + 2] = "B " + i;
        }

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex >= 3 && columnIndex <= 17) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        TableStyles.applyModernTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableStyles.centerColumns(table, 0, 1);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        for (int i = 3; i <= 17; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(45);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        PageScaffold scaffold = new PageScaffold("Bang chuyen can");
        scaffold.setBody(scrollPane);
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return;
        }

        tableModel.setRowCount(0);
        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsByStudent(student.getId());

        int stt = 1;
        for (CourseRegistration registration : registrations) {
            Object[] row = new Object[18];
            row[0] = String.valueOf(stt++);
            row[1] = registration.getCourseSection() != null && registration.getCourseSection().getCourse() != null
                    ? "M" + registration.getCourseSection().getCourse().getId() : "";
            row[2] = registration.getCourseSection() != null && registration.getCourseSection().getCourse() != null
                    ? registration.getCourseSection().getCourse().getFullName() : "";

            Mark mark = registration.getMark();
            String attendance = (mark != null && mark.getAttendance() != null) ? mark.getAttendance() : "000000000000000";
            while (attendance.length() < 15) {
                attendance += "0";
            }

            for (int i = 0; i < 15; i++) {
                row[i + 3] = attendance.charAt(i) == '1';
            }
            tableModel.addRow(row);
        }
    }
}


```

## src\main\java\com\bangcompany\onlineute\View\features\account\CreateAccountPage.java <a id="CreateAccountPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.Account`
- `com.bangcompany.onlineute.Model.Entity.Class`
- `com.bangcompany.onlineute.Model.Entity.Faculty`
- `com.bangcompany.onlineute.Model.Entity.Lecturer`
- `com.bangcompany.onlineute.Model.Entity.Major`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.Model.Entity.UserProfile`
- `com.bangcompany.onlineute.Model.EnumType.Role`
- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.RoundedOutlineBorder`
- `com.bangcompany.onlineute.View.Components.SelectGroup`
- `com.bangcompany.onlineute.View.Components.TextAreaGroup`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.event.DocumentEvent`
- `javax.swing.event.DocumentListener`
- `javax.swing.plaf.basic.BasicScrollBarUI`
- `java.awt`
- `java.time.LocalDate`
- `java.time.Year`
- `java.util.ArrayList`
- `java.util.Comparator`
- `java.util.HashMap`
- `java.util.List`
- `java.util.Locale`
- `java.util.Map`
- `java.util.Objects`

```java
package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TextAreaGroup;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateAccountPage extends JPanel implements Refreshable {
    private InputGroup codeInput;
    private InputGroup nameInput;
    private InputGroup emailInput;
    private InputGroup phoneInput;
    private InputGroup dobInput;
    private SelectGroup<String> genderSelect;
    private InputGroup placeOfBirthInput;
    private InputGroup nationalityInput;
    private SelectGroup<Faculty> facultySelect;
    private SelectGroup<Major> majorSelect;
    private SelectGroup<Class> classSelect;
    private InputGroup enrollmentYearInput;
    private InputGroup expectedGraduationYearInput;
    private InputGroup studentCodePreviewInput;
    private InputGroup citizenIdInput;
    private InputGroup citizenIssuePlaceInput;
    private InputGroup citizenIssueDateInput;
    private TextAreaGroup currentAddressInput;
    private TextAreaGroup permanentAddressInput;
    private InputGroup contactNameInput;
    private InputGroup contactPhoneInput;
    private TextAreaGroup bulkDataInput;

    private final String initialRole;
    private final boolean studentMode;

    public CreateAccountPage() {
        this("Sinh vien");
    }

    public CreateAccountPage(String initialRole) {
        this.initialRole = initialRole == null || initialRole.isBlank() ? "Sinh vien" : initialRole;
        this.studentMode = "Sinh vien".equalsIgnoreCase(this.initialRole);

        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        add(createManualTab(), BorderLayout.CENTER);
    }

    private JComponent createManualTab() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 0, 8, 0));

        codeInput = new InputGroup("Ma dinh danh *", false);
        codeInput.setEditable(true);

        nameInput = new InputGroup("Ho va ten *", false);
        emailInput = new InputGroup("Email *", false);
        phoneInput = new InputGroup("So dien thoai", false);
        dobInput = new InputGroup("Ngay sinh * (YYYY-MM-DD)", false);
        genderSelect = new SelectGroup<>("Gioi tinh", List.of("Nam", "Nu", "Khac"));
        placeOfBirthInput = new InputGroup("Noi sinh", false);
        nationalityInput = new InputGroup("Quoc tich", false);
        facultySelect = new SelectGroup<>("Khoa *", AppContext.getFacultyService().getAllFaculties());
        majorSelect = new SelectGroup<>("Nganh *", List.of());
        classSelect = new SelectGroup<>("Lop *", List.of());
        enrollmentYearInput = new InputGroup("Nam nhap hoc *", false);
        expectedGraduationYearInput = new InputGroup("Nam tot nghiep du kien", false);
        studentCodePreviewInput = new InputGroup("MSSV tu dong", false);
        studentCodePreviewInput.setEditable(false);

        citizenIdInput = new InputGroup("CCCD/CMND", false);
        citizenIssuePlaceInput = new InputGroup("Noi cap", false);
        citizenIssueDateInput = new InputGroup("Ngay cap (YYYY-MM-DD)", false);
        currentAddressInput = new TextAreaGroup("Dia chi hien tai", 96);
        permanentAddressInput = new TextAreaGroup("Dia chi thuong tru", 96);
        contactNameInput = new InputGroup("Nguoi lien he", false);
        contactPhoneInput = new InputGroup("SDT lien he", false);

        facultySelect.getComboBox().addActionListener(e -> {
            refreshAcademicOptions();
            refreshGeneratedStudentCode();
        });
        majorSelect.getComboBox().addActionListener(e -> refreshGeneratedStudentCode());
        enrollmentYearInput.getTextField().getDocument().addDocumentListener(new SimpleDocumentListener(this::refreshGeneratedStudentCode));

        //content.add(createHeroPanel());
        content.add(Box.createVerticalStrut(18));
        content.add(createSectionPanel(
                studentMode ? createSingleRow(nameInput) : createTwoColumnRow(codeInput, nameInput),
                createTwoColumnRow(emailInput, phoneInput),
                createThreeColumnRow(dobInput, genderSelect, nationalityInput)
        ));
        content.add(Box.createVerticalStrut(16));

        if (studentMode) {
            content.add(createSectionPanel(
                    createThreeColumnRow(facultySelect, majorSelect, classSelect),
                    createThreeColumnRow(enrollmentYearInput, expectedGraduationYearInput, studentCodePreviewInput)
            ));
            content.add(Box.createVerticalStrut(16));
        }

        content.add(createSectionPanel(
                createThreeColumnRow(citizenIdInput, citizenIssuePlaceInput, citizenIssueDateInput),
                createTwoColumnRow(contactNameInput, contactPhoneInput)
        ));
        content.add(Box.createVerticalStrut(16));

        content.add(createSectionPanel(
                createSingleRow(permanentAddressInput),
                createSingleRow(currentAddressInput)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createActionBar());

        if (studentMode) {
            refreshAcademicOptions();
            enrollmentYearInput.setValue(String.valueOf(Year.now().getValue()));
            expectedGraduationYearInput.setValue(String.valueOf(Year.now().getValue() + 4));
            nationalityInput.setValue("Viet Nam");
        }
        refreshGeneratedStudentCode();
        return createHiddenScrollPane(content);
    }


    private JPanel createActionBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(221, 227, 235), 24, new Insets(14, 18, 14, 18)),
                new EmptyBorder(14, 18, 14, 18)
        ));
        bar.setOpaque(true);

        if (studentMode) {
            JButton rawDataButton = new JButton("Nhap du lieu tho");
            styleGhostButton(rawDataButton);
            rawDataButton.addActionListener(e -> openBulkInputDialog());
            bar.add(rawDataButton);
        }

        PrimaryButton createButton = new PrimaryButton(studentMode ? "Luu sinh vien" : "Luu giang vien");
        createButton.setPreferredSize(new Dimension(170, 42));
        createButton.addActionListener(e -> createSingleAccount());
        bar.add(createButton);
        wrapper.add(bar, BorderLayout.CENTER);
        return wrapper;
    }

    private void styleGhostButton(JButton button) {
        button.setOpaque(true);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 91, 191));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(198, 210, 227), 18, new Insets(10, 16, 10, 16)),
                new EmptyBorder(10, 16, 10, 16)
        ));
        button.setContentAreaFilled(false);
    }

    private JPanel createBulkPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel helpLabel = new JLabel("<html>Moi dong 7 cot, cach nhau boi dau |<br>Ho ten|Email|Ngay sinh (YYYY-MM-DD)|Ma khoa|Ma nganh|Lop|Nam nhap hoc<br>He thong sap xep theo ten truoc khi sinh 3 so cuoi.</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        helpLabel.setForeground(new Color(110, 110, 110));
        panel.add(helpLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bulkDataInput = new TextAreaGroup("Du lieu bulk", 350);
        panel.add(bulkDataInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        PrimaryButton btnCreateBulk = new PrimaryButton("Luu hang loat");
        btnCreateBulk.setPreferredSize(new Dimension(180, 45));
        btnCreateBulk.addActionListener(e -> createBulkStudents());
        panel.add(btnCreateBulk, gbc);
        return panel;
    }

    private void openBulkInputDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Nhap du lieu tho", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createHiddenScrollPane(createBulkPanel()));
        dialog.setSize(760, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JScrollPane createHiddenScrollPane(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(18);
        verticalBar.setPreferredSize(new Dimension(0, 0));
        verticalBar.setOpaque(false);
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        return scrollPane;
    }

    private void createSingleAccount() {
        String fullName = nameInput.getValue().trim();
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap ho ten.", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (studentMode) {
                createStudentAccount(fullName);
            } else {
                createLecturerAccount(fullName);
            }

            JOptionPane.showMessageDialog(this, "Tao tai khoan thanh cong.", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
            onEnter();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi tao tai khoan: " + ex.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void createStudentAccount(String fullName) {
        String email = emailInput.getValue().trim();
        String dobText = dobInput.getValue().trim();
        String enrollmentYearText = enrollmentYearInput.getValue().trim();

        if (email.isEmpty() || dobText.isEmpty() || enrollmentYearText.isEmpty()) {
            throw new IllegalArgumentException("Sinh vien can email, ngay sinh va nam nhap hoc.");
        }

        Faculty selectedFaculty = facultySelect.getSelectedValue();
        Major selectedMajor = majorSelect.getSelectedValue();
        Class selectedClass = classSelect.getSelectedValue();
        if (selectedFaculty == null || selectedMajor == null || selectedClass == null) {
            throw new IllegalArgumentException("Sinh vien can chon khoa, nganh va lop.");
        }

        if (selectedClass.getFaculty() == null || !Objects.equals(selectedClass.getFaculty().getId(), selectedFaculty.getId())) {
            throw new IllegalArgumentException("Lop phai thuoc dung khoa da chon.");
        }
        if (selectedMajor.getFaculty() == null || !Objects.equals(selectedMajor.getFaculty().getId(), selectedFaculty.getId())) {
            throw new IllegalArgumentException("Nganh phai thuoc dung khoa da chon.");
        }

        String code = autoPreviewStudentCode();
        if (code.isBlank()) {
            throw new IllegalArgumentException("Khong the tao MSSV. Vui long kiem tra nganh va nam nhap hoc.");
        }

        Student student = new Student(code, fullName, LocalDate.parse(dobText), email, "");
        student.setClassEntity(selectedClass);
        student.setEnrollmentYear(Integer.parseInt(enrollmentYearText));

        Account account = new Account("123456", Role.STUDENT);
        Account savedAccount = AppContext.getAccountController().createStudentAccount(account, student);
        saveUserProfile(buildStudentProfile(savedAccount, student, selectedFaculty, selectedMajor));
    }

    private void createLecturerAccount(String fullName) {
        String code = codeInput.getValue().trim();
        if (code.isBlank()) {
            throw new IllegalArgumentException("Giang vien can ma giang vien.");
        }

        Lecturer lecturer = new Lecturer();
        lecturer.setCode(code);
        lecturer.setFullName(fullName);

        Account account = new Account("123456", Role.LECTURER);
        Account savedAccount = AppContext.getAccountController().createLecturerAccount(account, lecturer);
        saveUserProfile(buildLecturerProfile(savedAccount, lecturer));
    }

    private void createBulkStudents() {
        String rawData = bulkDataInput.getValue().trim();
        if (rawData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ban chua nhap du lieu.", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<BulkStudentRow> rows = parseBulkRows(rawData);
        rows.sort(Comparator.comparing(BulkStudentRow::sortableName, String.CASE_INSENSITIVE_ORDER));

        Map<String, Integer> nextSequenceByPrefix = new HashMap<>();
        int created = 0;

        for (BulkStudentRow row : rows) {
            String prefix = buildStudentPrefix(row.enrollmentYear(), row.major().getMajorCode());
            int nextStep = nextSequenceByPrefix.compute(prefix, (key, value) -> {
                if (value == null) {
                    return (int) AppContext.getStudentService().countStudentsByCodePrefix(prefix) + 1;
                }
                return value + 1;
            });

            String studentCode = prefix + String.format("%03d", nextStep);
            Student student = new Student(studentCode, row.fullName(), row.birthDate(), row.email(), "");
            student.setClassEntity(row.classEntity());
            student.setEnrollmentYear(row.enrollmentYear());

            Account account = new Account("123456", Role.STUDENT);
            Account savedAccount = AppContext.getAccountController().createStudentAccount(account, student);
            saveUserProfile(buildStudentProfile(savedAccount, student, row.faculty(), row.major()));
            created++;
        }

        JOptionPane.showMessageDialog(this, "Da tao " + created + " sinh vien.", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
        bulkDataInput.setValue("");
    }

    private UserProfile buildStudentProfile(Account account, Student student, Faculty faculty, Major major) {
        UserProfile profile = buildCommonProfile(account, student.getCode(), student.getFullName(), "Sinh vien");
        profile.setEmail(student.getEmail());
        profile.setBirthDate(student.getBirthOfDate());
        profile.setFacultyName(faculty != null ? faculty.getFullName() : "");
        profile.setMajorName(major != null ? major.getFullName() : "");
        profile.setClassName(student.getClassEntity() != null ? student.getClassEntity().getClassName() : "");
        profile.setAcademicYear(String.valueOf(student.getEnrollmentYear()));
        profile.setExpectedGraduationYear(expectedGraduationYearInput.getValue().trim());
        return profile;
    }

    private UserProfile buildLecturerProfile(Account account, Lecturer lecturer) {
        UserProfile profile = buildCommonProfile(account, lecturer.getCode(), lecturer.getFullName(), "Giang vien");
        profile.setEmail(emailInput.getValue().trim());
        return profile;
    }

    private UserProfile buildCommonProfile(Account account, String profileCode, String displayName, String roleTitle) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setProfileCode(profileCode);
        profile.setDisplayName(displayName);
        profile.setRoleTitle(roleTitle);
        profile.setEmail(emailInput.getValue().trim());
        profile.setPhoneNumber(phoneInput.getValue().trim());
        profile.setBirthDate(parseOptionalDate(dobInput.getValue().trim()));
        profile.setGender(valueOf(genderSelect.getSelectedValue()));
        profile.setPlaceOfBirth(placeOfBirthInput.getValue().trim());
        profile.setNationality(nationalityInput.getValue().trim());
        profile.setCitizenIdNumber(citizenIdInput.getValue().trim());
        profile.setCitizenIdIssuePlace(citizenIssuePlaceInput.getValue().trim());
        profile.setCitizenIdIssueDate(parseOptionalDate(citizenIssueDateInput.getValue().trim()));
        profile.setCurrentAddress(currentAddressInput.getValue().trim());
        profile.setPermanentAddress(permanentAddressInput.getValue().trim());
        profile.setContactName(contactNameInput.getValue().trim());
        profile.setContactPhone(contactPhoneInput.getValue().trim());
        return profile;
    }

    private void saveUserProfile(UserProfile userProfile) {
        AppContext.getUserProfileController().save(userProfile);
    }

    private List<BulkStudentRow> parseBulkRows(String rawData) {
        Map<String, Faculty> facultyByCode = new HashMap<>();
        for (Faculty faculty : AppContext.getFacultyService().getAllFaculties()) {
            facultyByCode.put(faculty.getFacultyCode().toUpperCase(Locale.ROOT), faculty);
        }

        List<BulkStudentRow> rows = new ArrayList<>();
        String[] lines = rawData.split("\\R");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length != 7) {
                throw new IllegalArgumentException("Dong " + (i + 1) + " phai co 7 cot: Ho ten|Email|Ngay sinh|Ma khoa|Ma nganh|Lop|Nam nhap hoc");
            }

            String fullName = parts[0].trim();
            String email = parts[1].trim();
            LocalDate birthDate = LocalDate.parse(parts[2].trim());
            String facultyCode = parts[3].trim().toUpperCase(Locale.ROOT);
            String majorCode = parts[4].trim();
            String className = parts[5].trim();
            int enrollmentYear = Integer.parseInt(parts[6].trim());

            Faculty faculty = facultyByCode.get(facultyCode);
            if (faculty == null) {
                throw new IllegalArgumentException("Khong tim thay khoa " + facultyCode + " o dong " + (i + 1));
            }

            Major major = findMajorByFacultyAndCode(faculty.getId(), majorCode);
            if (major == null) {
                throw new IllegalArgumentException("Khong tim thay nganh " + majorCode + " trong khoa " + facultyCode + " o dong " + (i + 1));
            }

            Class classEntity = findClassByFacultyAndName(faculty.getId(), className);
            if (classEntity == null) {
                throw new IllegalArgumentException("Khong tim thay lop " + className + " trong khoa " + facultyCode + " o dong " + (i + 1));
            }

            rows.add(new BulkStudentRow(fullName, email, birthDate, faculty, major, classEntity, enrollmentYear));
        }

        return rows;
    }
    private Major findMajorByFacultyAndCode(Long facultyId, String majorCode) {
        return AppContext.getMajorService().getMajorsByFaculty(facultyId).stream()
                .filter(major -> major.getMajorCode().equalsIgnoreCase(majorCode))
                .findFirst()
                .orElse(null);
    }

    private Class findClassByFacultyAndName(Long facultyId, String className) {
        return AppContext.getClassService().getClassesByFaculty(facultyId).stream()
                .filter(classEntity -> classEntity.getClassName().equalsIgnoreCase(className))
                .findFirst()
                .orElse(null);
    }

    private void refreshAcademicOptions() {
        if (!studentMode || facultySelect == null) {
            return;
        }

        Faculty selectedFaculty = facultySelect.getSelectedValue();
        if (majorSelect != null) {
            majorSelect.setItems(selectedFaculty == null ? List.of() : AppContext.getMajorService().getMajorsByFaculty(selectedFaculty.getId()));
        }
        if (classSelect != null) {
            classSelect.setItems(selectedFaculty == null ? List.of() : AppContext.getClassService().getClassesByFaculty(selectedFaculty.getId()));
        }
    }

    private void refreshGeneratedStudentCode() {
        if (!studentMode || studentCodePreviewInput == null) {
            return;
        }
        studentCodePreviewInput.setValue(autoPreviewStudentCode());
    }

    private String autoPreviewStudentCode() {
        try {
            String yearText = enrollmentYearInput == null ? "" : enrollmentYearInput.getValue().trim();
            Major selectedMajor = majorSelect == null ? null : majorSelect.getSelectedValue();
            if (yearText.isEmpty() || selectedMajor == null) {
                return "";
            }
            return generateNextStudentCode(Integer.parseInt(yearText), selectedMajor.getMajorCode(), 1);
        } catch (Exception ignored) {
            return "";
        }
    }

    private String generateNextStudentCode(int enrollmentYear, String majorCode, int step) {
        String prefix = buildStudentPrefix(enrollmentYear, majorCode);
        long existing = AppContext.getStudentService().countStudentsByCodePrefix(prefix);
        return prefix + String.format("%03d", existing + step);
    }

    private String buildStudentPrefix(int enrollmentYear, String majorCode) {
        String yearPart = String.format("%02d", enrollmentYear % 100);
        return yearPart + majorCode;
    }

    private JPanel createTwoColumnRow(Component left, Component right) {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.add(left);
        row.add(right);
        return row;
    }

    private JPanel createThreeColumnRow(Component left, Component middle, Component right) {
        JPanel row = new JPanel(new GridLayout(1, 3, 14, 0));
        row.setOpaque(false);
        row.add(left);
        row.add(middle);
        row.add(right);
        return row;
    }

    private JPanel createSingleRow(Component component) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private JPanel createSectionPanel(Component... rows) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(225, 230, 236), 26, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        for (int i = 0; i < rows.length; i++) {
            Component row = rows[i];
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
            section.add(row);
            if (i < rows.length - 1) {
                section.add(Box.createVerticalStrut(12));
            }
        }
        return section;
    }

    private LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value.trim());
    }

    private String valueOf(String value) {
        return value == null ? "" : value;
    }

    @Override
    public void onEnter() {
        if (codeInput != null) codeInput.setValue("");
        if (nameInput != null) nameInput.setValue("");
        if (emailInput != null) emailInput.setValue("");
        if (phoneInput != null) phoneInput.setValue("");
        if (dobInput != null) dobInput.setValue("");
        if (placeOfBirthInput != null) placeOfBirthInput.setValue("");
        if (nationalityInput != null) nationalityInput.setValue("");
        if (citizenIdInput != null) citizenIdInput.setValue("");
        if (citizenIssuePlaceInput != null) citizenIssuePlaceInput.setValue("");
        if (citizenIssueDateInput != null) citizenIssueDateInput.setValue("");
        if (currentAddressInput != null) currentAddressInput.setValue("");
        if (permanentAddressInput != null) permanentAddressInput.setValue("");
        if (contactNameInput != null) contactNameInput.setValue("");
        if (contactPhoneInput != null) contactPhoneInput.setValue("");
        if (studentCodePreviewInput != null) studentCodePreviewInput.setValue("");
        if (bulkDataInput != null) bulkDataInput.setValue("");

        if (studentMode) {
            if (enrollmentYearInput != null) enrollmentYearInput.setValue(String.valueOf(Year.now().getValue()));
            if (expectedGraduationYearInput != null) expectedGraduationYearInput.setValue(String.valueOf(Year.now().getValue() + 4));
            if (nationalityInput != null) nationalityInput.setValue("Viet Nam");
            if (facultySelect != null) facultySelect.setItems(AppContext.getFacultyService().getAllFaculties());
            refreshAcademicOptions();
            refreshGeneratedStudentCode();
        } else if (codeInput != null) {
            codeInput.setEditable(true);
        }
    }

    private record BulkStudentRow(
            String fullName,
            String email,
            LocalDate birthDate,
            Faculty faculty,
            Major major,
            Class classEntity,
            int enrollmentYear
    ) {
        private String sortableName() {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length == 0) {
                return fullName;
            }
            return parts[parts.length - 1] + " " + fullName;
        }
    }

    private static final class SimpleDocumentListener implements DocumentListener {
        private final Runnable onChange;

        private SimpleDocumentListener(Runnable onChange) {
            this.onChange = onChange;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange.run();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onChange.run();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onChange.run();
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\auth\LoginScreen.java <a id="LoginScreen_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.View.navigation.MainNavigator`
- `com.bangcompany.onlineute.View.Components.AppLogoHeader`
- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.auth;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.navigation.MainNavigator;
import com.bangcompany.onlineute.View.Components.AppLogoHeader;

import javax.swing.*;
import java.awt.*;

/**
 * màn hình đăng nhập
 */
public class LoginScreen extends JPanel {
    public LoginScreen() {
        setLayout(new GridBagLayout());     // tự đôộng nằm giữa screen
        setBackground(new Color(230, 235, 240));

        JPanel cardHolder = new JPanel();   //container đăng nhập
        cardHolder.setLayout(new BoxLayout(cardHolder, BoxLayout.Y_AXIS));// xếp theo chều dọc
        cardHolder.setOpaque(false); //tỏng suốt background
        add(cardHolder);

        cardHolder.add(new AppLogoHeader());
        cardHolder.add(Box.createRigidArea(new Dimension(0, 30)));

        LoginForm loginForm = new LoginForm(this::attemptLogin);
        loginForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardHolder.add(loginForm);
    }

    private void attemptLogin(String user, String pass) {
        
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AppContext.authController.Login(user, pass).ifPresentOrElse(
                account -> MainNavigator.showDashboard(),
                () -> JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE)
        );
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\dashboard\DashboardLayout.java <a id="DashboardLayout_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.View.Components.leftbar.SidebarItem`
- `com.bangcompany.onlineute.View.features.account.ChangePasswordPage`
- `com.bangcompany.onlineute.View.features.account.CreateAccountPage`
- `com.bangcompany.onlineute.View.features.announcement.AnnouncementPage`
- `com.bangcompany.onlineute.View.features.announcement.CreateAnnouncementPage`
- `com.bangcompany.onlineute.View.features.attendance.AttendancePage`
- `com.bangcompany.onlineute.View.features.data.DataManagementPage`
- `com.bangcompany.onlineute.View.features.grade.InputGradesPage`
- `com.bangcompany.onlineute.View.features.grade.ViewGradesPage`
- `com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage`
- `com.bangcompany.onlineute.View.features.profile.ProfilePage`
- `com.bangcompany.onlineute.View.features.registration.CourseRegistrationPage`
- `com.bangcompany.onlineute.View.features.registration.CreateRegistrationBatchPage`
- `com.bangcompany.onlineute.View.features.schedule.SchedulePage`
- `com.bangcompany.onlineute.View.features.student.StudentManagementPage`
- `javax.swing`
- `java.awt`
- `java.util.ArrayList`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.features.account.ChangePasswordPage;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.announcement.AnnouncementPage;
import com.bangcompany.onlineute.View.features.announcement.CreateAnnouncementPage;
import com.bangcompany.onlineute.View.features.attendance.AttendancePage;
import com.bangcompany.onlineute.View.features.data.DataManagementPage;
import com.bangcompany.onlineute.View.features.grade.InputGradesPage;
import com.bangcompany.onlineute.View.features.grade.ViewGradesPage;
import com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage;
import com.bangcompany.onlineute.View.features.profile.ProfilePage;
import com.bangcompany.onlineute.View.features.registration.CourseRegistrationPage;
import com.bangcompany.onlineute.View.features.registration.CreateRegistrationBatchPage;
import com.bangcompany.onlineute.View.features.schedule.SchedulePage;
import com.bangcompany.onlineute.View.features.student.StudentManagementPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardLayout extends JPanel {
    private final MainContent mainContent = new MainContent();
    private final List<SidebarItem> tabs = new ArrayList<>();
    private final Sidebar sidebar;

    public DashboardLayout() {
        setLayout(new BorderLayout());

        String userName = SessionManager.getProfileFullName();
        String userCode = SessionManager.getProfileCode();
        String roleDisplayName = SessionManager.getRoleDisplayName();

        buildTabs(SessionManager.getRole());
        registerPages();

        sidebar = new Sidebar(userName, userCode, roleDisplayName, tabs, this::showPage);

        add(sidebar, BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);

        showFirstTab();
    }

    private JPanel createMainArea() {
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(245, 248, 252));

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);
        headerWrapper.add(new TopHeader("\u0054\u0052\u01af\u1edc\u004e\u0047\u0020\u0110\u1ea0\u0049\u0020\u0048\u1ecc\u0043\u0020\u0043\u00d4\u004e\u0047\u0020\u004e\u0047\u0048\u1ec6\u0020\u004b\u1ef8\u0020\u0054\u0048\u0055\u1eac\u0054\u0020\u0054\u0050\u002e\u0048\u0043\u004d"), BorderLayout.NORTH);

        mainArea.add(headerWrapper, BorderLayout.NORTH);
        mainArea.add(mainContent, BorderLayout.CENTER);
        return mainArea;
    }

    private void buildTabs(String role) {
        tabs.clear();
        if ("ADMIN".equals(role)) {
            buildAdminTabs();
            return;
        }
        if ("LECTURER".equals(role)) {
            buildLecturerTabs();
            return;
        }
        buildStudentTabs();
    }

    private void buildAdminTabs() {
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0051\u0055\u1ea2\u004e\u0020\u004c\u00dd");
        addTab("MANAGE_DATA", "\u0051\u0075\u0061\u006e\u0020\u006c\u00fd\u0020\u0064\u1eef\u0020\u006c\u0069\u1ec7\u0075", "trangCuaBan.png");
        addTab("CREATE_REGISTRATION_BATCH", "\u0054\u1ea1\u006f\u0020\u0111\u1ee3\u0074\u0020\u0111\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u006d\u00f4\u006e", "chuongTrinhDaoTao.png");
        addTab("COMPOSE_ANNOUNCEMENT", "\u0047\u1eed\u0069\u0020\u0074\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void buildLecturerTabs() {
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0054\u0052\u0041\u0020\u0043\u1ee8\u0055\u0020\u0054\u0048\u00d4\u004e\u0047\u0020\u0054\u0049\u004e");
        addTab("MY_SCHEDULE", "\u004c\u1ecb\u0063\u0068\u0020\u0067\u0069\u1ea3\u006e\u0067\u0020\u0064\u1ea1\u0079", "lich.png");
        addTitle("\u0051\u0055\u1ea2\u004e\u0020\u004c\u00dd\u0020\u0053\u0049\u004e\u0048\u0020\u0056\u0049\u00ca\u004e");
        addTab("COMPOSE_ANNOUNCEMENT", "\u0047\u1eed\u0069\u0020\u0074\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTab("INPUT_GRADES", "\u0051\u0075\u1ea3\u006e\u0020\u006c\u00fd\u0020\u006c\u1edb\u0070\u0020\u0068\u1ecd\u0063", "xemDiem.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void buildStudentTabs() {
        addTitle("\u0054\u0052\u0041\u004e\u0047\u0020\u0043\u00c1\u0020\u004e\u0048\u00c2\u004e");
        addTab("PROFILE", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u0063\u00e1\u0020\u006e\u0068\u00e2\u006e", "thongTinCaNhan.png");
        addTab("ANNOUNCEMENT", "\u0054\u0068\u00f4\u006e\u0067\u0020\u0062\u00e1\u006f", "trangCuaBan.png");
        addTitle("\u0054\u0052\u0041\u0020\u0043\u1ee8\u0055\u0020\u0054\u0048\u00d4\u004e\u0047\u0020\u0054\u0049\u004e");
        addTab("REGISTER_COURSES", "\u0110\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u006d\u00f4\u006e\u0020\u0068\u1ecd\u0063", "chuongTrinhDaoTao.png");
        addTab("MY_SCHEDULE", "\u0054\u0068\u1eddi\u0020\u006b\u0068\u00f3\u0061\u0020\u0062\u0069\u1ec3\u0075", "lich.png");
        addTab("MY_GRADES", "\u0058\u0065\u006d\u0020\u0111\u0069\u1ec3\u006d", "xemDiem.png");
        addTab("ATTENDANCE", "\u0058\u0065\u006d\u0020\u0111\u0069\u1ec3\u006d\u0020\u0063\u0068\u0075\u0079\u00ea\u006e\u0020\u0063\u1ea7\u006e", "lich.png");
        addTab("CHANGE_PASSWORD", "\u0110\u1ed5\u0069\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075", "thongTinCaNhan.png");
    }

    private void addTitle(String label) {
        tabs.add(SidebarItem.title(label));
    }

    private void addTab(String key, String label, String icon) {
        tabs.add(SidebarItem.tab(key, label, icon));
    }

    private void registerPages() {
        for (SidebarItem item : tabs) {
            if (item.isTitle()) {
                continue;
            }
            mainContent.registerPage(item.getKey(), createPage(item.getKey()));
        }
    }

    private JPanel createPage(String pageKey) {
        return switch (pageKey) {
            case "ANNOUNCEMENT" -> new AnnouncementPage();
            case "COMPOSE_ANNOUNCEMENT" -> new CreateAnnouncementPage();
            case "CREATE_ACCOUNTS" -> new CreateAccountPage();
            case "CREATE_REGISTRATION_BATCH" -> new CreateRegistrationBatchPage();
            case "REGISTER_COURSES" -> new CourseRegistrationPage();
            case "MANAGE_STUDENT" -> new StudentManagementPage();
            case "MANAGE_LECTURER" -> new LecturerManagementPage();
            case "MANAGE_DATA" -> new DataManagementPage();
            case "CHANGE_PASSWORD" -> new ChangePasswordPage();
            case "PROFILE" -> new ProfilePage();
            case "MY_SCHEDULE" -> new SchedulePage();
            case "INPUT_GRADES" -> new InputGradesPage();
            case "MY_GRADES" -> new ViewGradesPage();
            case "ATTENDANCE" -> new AttendancePage();
            default -> createPlaceholder(pageKey);
        };
    }

    private JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void showFirstTab() {
        for (SidebarItem item : tabs) {
            if (!item.isTitle()) {
                showPage(item.getKey());
                return;
            }
        }
    }

    private void showPage(String pageKey) {
        mainContent.showPage(pageKey);
        sidebar.setActiveTab(pageKey);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\data\ManagementShellPage.java <a id="ManagementShellPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.SearchActionTopbar`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.SearchActionTopbar;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class ManagementShellPage extends JPanel implements Refreshable {
    private static final String DASHBOARD_CARD = "dashboard";
    private static final String RESULT_CARD = "result";

    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    private final SearchActionTopbar topbar;
    private final JComponent dashboardPanel;
    private final JComponent resultPanel;
    private final int minSearchLength;

    protected ManagementShellPage(String placeholder,
                                  String createButtonLabel,
                                  Runnable onCreateNew,
                                  int minSearchLength,
                                  JComponent dashboardPanel,
                                  JComponent resultPanel) {
        this.minSearchLength = Math.max(1, minSearchLength);
        this.dashboardPanel = dashboardPanel;
        this.resultPanel = resultPanel;

        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        topbar = new SearchActionTopbar(
                placeholder,
                createButtonLabel,
                this.minSearchLength,
                this::handleSearchChanged,
                onCreateNew
        );

        contentPanel.setOpaque(false);
        contentPanel.add(dashboardPanel, DASHBOARD_CARD);
        contentPanel.add(resultPanel, RESULT_CARD);

        add(topbar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }

    @Override
    public void onEnter() {
        String keyword = topbar.getKeyword();
        if (!isSearchReady(keyword)) {
            onKeywordCleared();
            showDashboard();
            return;
        }
        onKeywordActivated(keyword.trim());
        showResults();
    }

    protected abstract void onKeywordActivated(String keyword);

    protected abstract void onKeywordCleared();

    protected boolean isSearchReady(String keyword) {
        return keyword != null && keyword.trim().length() >= minSearchLength;
    }

    protected void showDashboard() {
        contentLayout.show(contentPanel, DASHBOARD_CARD);
    }

    protected void showResults() {
        contentLayout.show(contentPanel, RESULT_CARD);
    }

    protected String getKeyword() {
        return topbar.getKeyword();
    }

    protected void configureCreateAction(String label, Runnable action) {
        topbar.setCreateAction(label, action);
    }

    protected JComponent getDashboardComponent() {
        return dashboardPanel;
    }

    protected JComponent getResultComponent() {
        return resultPanel;
    }

    private void handleSearchChanged(String keyword) {
        if (!isSearchReady(keyword)) {
            onKeywordCleared();
            showDashboard();
            return;
        }
        onKeywordActivated(keyword.trim());
        showResults();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\dashboard\Sidebar.java <a id="Sidebar_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.View.Components.leftbar.NavMenu`
- `com.bangcompany.onlineute.View.Components.leftbar.SidebarItem`
- `com.bangcompany.onlineute.View.Components.leftbar.UserProfileCard`
- `com.bangcompany.onlineute.View.navigation.MainNavigator`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.plaf.basic.BasicScrollBarUI`
- `java.awt`
- `java.util.List`
- `java.util.function.Consumer`

```java
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.Components.leftbar.NavMenu;
import com.bangcompany.onlineute.View.Components.leftbar.SidebarItem;
import com.bangcompany.onlineute.View.Components.leftbar.UserProfileCard;
import com.bangcompany.onlineute.View.navigation.MainNavigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private final NavMenu navMenu;

    public Sidebar(
            String userName,
            String userId,
            String roleDisplayName,
            List<SidebarItem> menuItems,
            Consumer<String> onNavigate
    ) {
        setBackground(new Color(0, 85, 141));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        UserProfileCard profileCard = new UserProfileCard(userName, userId, roleDisplayName);
        add(profileCard, BorderLayout.NORTH);

        navMenu = new NavMenu(menuItems, onNavigate);

        JScrollPane menuScrollPane = new JScrollPane(navMenu);
        menuScrollPane.setBorder(null);
        menuScrollPane.setOpaque(false);
        menuScrollPane.getViewport().setOpaque(false);
        menuScrollPane.getViewport().setBackground(new Color(0, 85, 141));
        menuScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane.setWheelScrollingEnabled(true);

        JScrollBar verticalBar = menuScrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(20);
        verticalBar.setPreferredSize(new Dimension(0, 0));
        verticalBar.setOpaque(false);
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        add(menuScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 85, 141));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton logoutButton = new JButton("ĐĂNG XUẤT");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            SessionManager.logout();
            MainNavigator.showLogin();
        });

        bottomPanel.add(logoutButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setActiveTab(String pageKey) {
        navMenu.setActiveTab(pageKey);
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() == null) {
            return new Dimension(260, super.getPreferredSize().height);
        }

        int parentWidth = getParent().getWidth();
        int calculatedWidth = (int) (parentWidth * 0.25);
        int width = Math.min(280, Math.max(150, calculatedWidth));
        return new Dimension(width, super.getPreferredSize().height);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\dashboard\MainContent.java <a id="MainContent_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `java.awt`
- `java.util.HashMap`
- `java.util.Map`

```java
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainContent extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JPanel> pages = new HashMap<>();

    public MainContent() {
        setLayout(cardLayout);
    }

    public void registerPage(String pageKey, JPanel page) {
        if (page == null || pages.containsKey(pageKey)) {
            return;
        }

        pages.put(pageKey, page);
        add(page, pageKey);
    }

    public void showPage(String pageKey) {
        JPanel currentPage = pages.get(pageKey);
        if (currentPage == null) {
            return;
        }

        cardLayout.show(this, pageKey);
        if (currentPage instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\data\DataManagementPage.java <a id="DataManagementPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.Class`
- `com.bangcompany.onlineute.Model.Entity.Course`
- `com.bangcompany.onlineute.Model.Entity.Faculty`
- `com.bangcompany.onlineute.Model.Entity.Major`
- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.SelectGroup`
- `com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage`
- `com.bangcompany.onlineute.View.features.student.StudentManagementPage`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage;
import com.bangcompany.onlineute.View.features.student.StudentManagementPage;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DataManagementPage extends JPanel implements Refreshable {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final StudentManagementPage studentManagementPage = new StudentManagementPage();
    private final LecturerManagementPage lecturerManagementPage = new LecturerManagementPage();
    private final SimpleEntityManagementPage<Faculty> facultyManagementPage = new SimpleEntityManagementPage<>(
            "Tim theo ma khoa, ten khoa",
            "\u0054\u1ed5\u006e\u0067\u0020\u006b\u0068\u006f\u0061",
            "<html>Tim nhanh theo <b>ma khoa</b> hoac <b>ten khoa</b>.<br>Khi o tim kiem con trong, man nay se hien thi tong quan du lieu.</html>",
            "Tao moi",
            this::openCreateFacultyDialog,
            () -> AppContext.getFacultyService().getAllFaculties(),
            new String[]{
                    "\u004d\u00e3\u0020\u006b\u0068\u006f\u0061",
                    "\u0054\u00ea\u006e\u0020\u006b\u0068\u006f\u0061"
            },
            faculty -> new Object[]{faculty.getFacultyCode(), faculty.getFullName()},
            faculty -> faculty.getFacultyCode() + " " + faculty.getFullName()
    );
    private final SimpleEntityManagementPage<Major> majorManagementPage = new SimpleEntityManagementPage<>(
            "Tim theo ma nganh, ten nganh, khoa",
            "\u0054\u1ed5\u006e\u0067\u0020\u006e\u0067\u00e0\u006e\u0068",
            "<html>Tim nhanh theo <b>ma nganh</b>, <b>ten nganh</b> hoac <b>khoa</b>.<br>Khi o tim kiem con trong, man nay se hien thi tong quan du lieu.</html>",
            "Tao moi",
            this::openCreateMajorDialog,
            () -> AppContext.getMajorService().getAllMajors(),
            new String[]{
                    "\u004d\u00e3\u0020\u006e\u0067\u00e0\u006e\u0068",
                    "\u0054\u00ea\u006e\u0020\u006e\u0067\u00e0\u006e\u0068",
                    "\u004b\u0068\u006f\u0061",
                    "\u0054\u1ed5\u006e\u0067\u0020\u0074\u00ed\u006e\u0020\u0063\u0068\u1ec9"
            },
            major -> new Object[]{
                    major.getMajorCode(),
                    major.getFullName(),
                    major.getFaculty() == null ? "" : major.getFaculty().getFullName(),
                    major.getTotalCredit()
            },
            major -> major.getMajorCode() + " " + major.getFullName()
                    + " " + (major.getFaculty() == null ? "" : major.getFaculty().getFullName())
    );
    private final SimpleEntityManagementPage<Class> classManagementPage = new SimpleEntityManagementPage<>(
            "Tim theo lop, khoa",
            "\u0054\u1ed5\u006e\u0067\u0020\u006c\u1edb\u0070",
            "<html>Tim nhanh theo <b>ten lop</b> hoac <b>khoa</b>.<br>Khi o tim kiem con trong, man nay se hien thi tong quan du lieu.</html>",
            "Tao moi",
            this::openCreateClassDialog,
            () -> AppContext.getClassService().getAllClasses(),
            new String[]{"\u004c\u1edb\u0070", "\u004b\u0068\u006f\u0061"},
            classEntity -> new Object[]{
                    classEntity.getClassName(),
                    classEntity.getFaculty() == null ? "" : classEntity.getFaculty().getFullName()
            },
            classEntity -> classEntity.getClassName()
                    + " " + (classEntity.getFaculty() == null ? "" : classEntity.getFaculty().getFullName())
    );
    private final SimpleEntityManagementPage<Course> courseManagementPage = new SimpleEntityManagementPage<>(
            "Tim theo ma mon, ten mon",
            "\u0054\u1ed5\u006e\u0067\u0020\u006d\u00f4\u006e\u0020\u0068\u1ecd\u0063",
            "<html>Tim nhanh theo <b>ma mon</b> hoac <b>ten mon</b>.<br>Khi o tim kiem con trong, man nay se hien thi tong quan du lieu.</html>",
            "Tao moi",
            this::openCreateCourseDialog,
            () -> AppContext.getCourseService().getAllCourses(),
            new String[]{
                    "\u004d\u00e3\u0020\u006d\u00f4\u006e",
                    "\u0054\u00ea\u006e\u0020\u006d\u00f4\u006e",
                    "\u0053\u1ed1\u0020\u0074\u00ed\u006e\u0020\u0063\u0068\u1ec9"
            },
            course -> new Object[]{course.getCourseCode(), course.getFullName(), course.getCredit()},
            course -> course.getCourseCode() + " " + course.getFullName()
    );

    public DataManagementPage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        PageTitleLabel titleLabel = new PageTitleLabel("\u0051\u0075\u1ea3\u006e\u0020\u006c\u00fd\u0020\u0064\u1eef\u0020\u006c\u0069\u1ec7\u0075");

        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("\u0053\u0069\u006e\u0068\u0020\u0076\u0069\u00ea\u006e", studentManagementPage);
        tabbedPane.addTab("\u0047\u0069\u1ea3\u006e\u0067\u0020\u0076\u0069\u00ea\u006e", lecturerManagementPage);
        tabbedPane.addTab("\u004b\u0068\u006f\u0061", facultyManagementPage);
        tabbedPane.addTab("\u004e\u0067\u00e0\u006e\u0068", majorManagementPage);
        tabbedPane.addTab("\u004c\u1edb\u0070", classManagementPage);
        tabbedPane.addTab("\u004d\u00f4\u006e\u0020\u0068\u1ecd\u0063", courseManagementPage);
        tabbedPane.addChangeListener(e -> refreshSelectedTab());

        add(titleLabel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        refreshSelectedTab();
    }

    private void refreshSelectedTab() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }

    private void openCreateFacultyDialog() {
        InputGroup codeInput = new InputGroup("Ma khoa", false);
        InputGroup nameInput = new InputGroup("Ten khoa", false);

        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);

        openSimpleDialog("Tao moi khoa", form, () -> {
            Faculty faculty = new Faculty();
            faculty.setFacultyCode(codeInput.getValue().trim());
            faculty.setFullName(nameInput.getValue().trim());
            AppContext.getFacultyController().createFaculty(faculty);
            facultyManagementPage.onEnter();
        });
    }

    private void openCreateMajorDialog() {
        InputGroup codeInput = new InputGroup("Ma nganh", false);
        InputGroup nameInput = new InputGroup("Ten nganh", false);
        InputGroup creditInput = new InputGroup("Tong tin chi", false);
        SelectGroup<Faculty> facultySelect = new SelectGroup<>("Khoa", AppContext.getFacultyService().getAllFaculties());

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);
        form.add(creditInput);
        form.add(facultySelect);

        openSimpleDialog("Tao moi nganh", form, () -> {
            Major major = new Major();
            major.setMajorCode(codeInput.getValue().trim());
            major.setFullName(nameInput.getValue().trim());
            major.setTotalCredit(Integer.parseInt(creditInput.getValue().trim()));
            major.setFaculty(facultySelect.getSelectedValue());
            AppContext.getMajorController().createMajor(major);
            majorManagementPage.onEnter();
        });
    }

    private void openCreateClassDialog() {
        InputGroup nameInput = new InputGroup("Ten lop", false);
        SelectGroup<Faculty> facultySelect = new SelectGroup<>("Khoa", AppContext.getFacultyService().getAllFaculties());

        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        form.setOpaque(false);
        form.add(nameInput);
        form.add(facultySelect);

        openSimpleDialog("Tao moi lop", form, () -> {
            Class classEntity = new Class();
            classEntity.setClassName(nameInput.getValue().trim());
            classEntity.setFaculty(facultySelect.getSelectedValue());
            AppContext.getClassController().createClass(classEntity);
            classManagementPage.onEnter();
        });
    }

    private void openCreateCourseDialog() {
        InputGroup codeInput = new InputGroup("Ma mon", false);
        InputGroup nameInput = new InputGroup("Ten mon", false);
        InputGroup creditInput = new InputGroup("So tin chi", false);

        JPanel form = new JPanel(new GridLayout(3, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);
        form.add(creditInput);

        openSimpleDialog("Tao moi mon hoc", form, () -> {
            Course course = new Course();
            course.setCourseCode(codeInput.getValue().trim());
            course.setFullName(nameInput.getValue().trim());
            course.setCredit(Integer.parseInt(creditInput.getValue().trim()));
            AppContext.getCourseController().createCourse(course);
            courseManagementPage.onEnter();
        });
    }

    private void openSimpleDialog(String title, JComponent form, Runnable onSave) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout(0, 16));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));
        container.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        PrimaryButton saveButton = new PrimaryButton("Luu");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> {
            onSave.run();
            dialog.dispose();
        });
        actions.add(saveButton);
        container.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(container);
        dialog.setSize(520, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\data\EntityTablePanel.java <a id="EntityTablePanel_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.util.ArrayList`
- `java.util.List`
- `java.util.function.Function`

```java
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityTablePanel<T> extends JPanel implements Refreshable {
    private final Function<T, Object[]> rowMapper;
    private final Function<T, String> searchTextMapper;
    private final DefaultTableModel tableModel;
    private final JLabel resultLabel = new JLabel("0 ket qua");

    private List<T> items = new ArrayList<>();

    public EntityTablePanel(String[] columns,
                            Function<T, Object[]> rowMapper,
                            Function<T, String> searchTextMapper) {
        this.rowMapper = rowMapper;
        this.searchTextMapper = searchTextMapper;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 85, 141));
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        com.bangcompany.onlineute.View.Components.TableStyles.applyModernTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        com.bangcompany.onlineute.View.Components.TableStyles.styleScrollPane(scrollPane);

        add(resultLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        showItems(items, "");
    }

    public void showItems(List<T> loadedItems, String keyword) {
        items = loadedItems == null ? new ArrayList<>() : new ArrayList<>(loadedItems);
        tableModel.setRowCount(0);
        for (T item : items) {
            tableModel.addRow(rowMapper.apply(item));
        }
        if (keyword == null || keyword.isBlank()) {
            resultLabel.setText(items.size() + " ban ghi");
            return;
        }
        resultLabel.setText(items.size() + " ket qua cho: " + keyword.trim());
    }

    public List<T> filter(List<T> sourceItems, String keyword) {
        if (sourceItems == null) {
            return List.of();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(sourceItems);
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
        List<T> filteredItems = new ArrayList<>();
        for (T item : sourceItems) {
            String searchText = searchTextMapper.apply(item);
            String normalizedText = searchText == null ? "" : searchText.toLowerCase();
            if (normalizedText.contains(normalizedKeyword)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\dashboard\PageScaffold.java <a id="PageScaffold_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.RoundedPanel`
- `com.bangcompany.onlineute.View.Components.RoundedOutlineBorder`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.dashboard;

import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.RoundedPanel;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageScaffold extends JPanel {
    private final JPanel body;

    public PageScaffold(String title) {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = new RoundedPanel(26);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedOutlineBorder(new Color(225, 230, 236), 26, new Insets(20, 20, 20, 20)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        card.add(new PageTitleLabel(title), BorderLayout.NORTH);

        body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        card.add(body, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    public void setBody(Component component) {
        body.removeAll();
        body.add(component, BorderLayout.CENTER);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\dashboard\TopHeader.java <a id="TopHeader_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.dashboard;

import javax.swing.*;
import java.awt.*;

public class TopHeader extends JPanel {
    public TopHeader(String title) {
        Color primaryBlue = new Color(0, 85, 141);

        setBackground(primaryBlue);
        setPreferredSize(new Dimension(100, 47));
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
        leftPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        add(leftPanel, BorderLayout.WEST);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 66, 110)));
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\data\SimpleEntityManagementPage.java <a id="SimpleEntityManagementPage_java"></a>

### Dependencies

- `javax.swing`
- `java.util.List`
- `java.util.function.Function`
- `java.util.function.Supplier`

```java
package com.bangcompany.onlineute.View.features.data;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleEntityManagementPage<T> extends ManagementShellPage {
    private final Supplier<List<T>> loader;
    private final EntityTablePanel<T> resultPanel;
    private final SimpleManagementDashboard dashboardPanel;
    private List<T> cachedItems = List.of();

    public SimpleEntityManagementPage(String searchPlaceholder,
                                      String summaryTitle,
                                      String guideHtml,
                                      String createButtonLabel,
                                      Runnable onCreateNew,
                                      Supplier<List<T>> loader,
                                      String[] columns,
                                      Function<T, Object[]> rowMapper,
                                      Function<T, String> searchTextMapper) {
        this(searchPlaceholder, summaryTitle, guideHtml, createButtonLabel, onCreateNew, loader,
                new EntityTablePanel<>(columns, rowMapper, searchTextMapper),
                new SimpleManagementDashboard(summaryTitle, guideHtml, () -> {
                    List<T> loadedItems = loader.get();
                    return loadedItems == null ? 0 : loadedItems.size();
                }));
    }

    private SimpleEntityManagementPage(String searchPlaceholder,
                                       String summaryTitle,
                                       String guideHtml,
                                       String createButtonLabel,
                                       Runnable onCreateNew,
                                       Supplier<List<T>> loader,
                                       EntityTablePanel<T> resultPanel,
                                       SimpleManagementDashboard dashboardPanel) {
        super(searchPlaceholder, createButtonLabel, onCreateNew, 2, dashboardPanel, resultPanel);
        this.loader = loader;
        this.resultPanel = resultPanel;
        this.dashboardPanel = dashboardPanel;
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        loadItems();
        resultPanel.showItems(resultPanel.filter(cachedItems, keyword), keyword);
    }

    @Override
    protected void onKeywordCleared() {
        loadItems();
        dashboardPanel.refreshData();
    }

    private void loadItems() {
        List<T> loadedItems = loader.get();
        cachedItems = loadedItems == null ? List.of() : loadedItems;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\data\SimpleManagementDashboard.java <a id="SimpleManagementDashboard_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.RoundedOutlineBorder`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`
- `java.util.function.IntSupplier`

```java
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

        JLabel titleLabel = new JLabel("\u0042\u1eaf\u0074\u0020\u0111\u1ea7\u0075\u0020\u0062\u1eb1\u006e\u0067\u0020\u00f4\u0020\u0074\u00ec\u006d\u0020\u006b\u0069\u1ebf\u006d");
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

```

## src\main\java\com\bangcompany\onlineute\View\features\grade\InputGradesPage.java <a id="InputGradesPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.CourseRegistration`
- `com.bangcompany.onlineute.Model.Entity.CourseSection`
- `com.bangcompany.onlineute.Model.Entity.Lecturer`
- `com.bangcompany.onlineute.Model.Entity.Mark`
- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableCellRenderer`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.math.BigDecimal`
- `java.util.List`
- `java.util.stream.Collectors`

```java
package com.bangcompany.onlineute.View.features.grade;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class InputGradesPage extends JPanel implements Refreshable {
    private final JTabbedPane tabbedPane;
    private final JPanel noDataPanel;

    public InputGradesPage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("QUẢN LÝ SINH VIÊN - ĐIỂM DANH VÀ NHẬP ĐIỂM"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        noDataPanel = new JPanel(new BorderLayout());
        noDataPanel.setBackground(Color.WHITE);
        JLabel noDataLabel = new JLabel("Không có lớp học phần nào", SwingConstants.CENTER);
        noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        noDataLabel.setForeground(Color.GRAY);
        noDataPanel.add(noDataLabel, BorderLayout.CENTER);

        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        loadTabs();
    }

    private void loadTabs() {
        tabbedPane.removeAll();
        Lecturer currentLecturer = SessionManager.getCurrentLecturer();
        if (currentLecturer == null) {
            return;
        }

        List<CourseSection> mySections = AppContext.getCourseSectionService().getAllSections().stream()
                .filter(sec -> sec.getLecturer() != null && sec.getLecturer().getId().equals(currentLecturer.getId()))
                .collect(Collectors.toList());

        if (mySections.isEmpty()) {
            remove(tabbedPane);
            add(noDataPanel, BorderLayout.CENTER);
        } else {
            remove(noDataPanel);
            add(tabbedPane, BorderLayout.CENTER);
            for (CourseSection sec : mySections) {
                tabbedPane.addTab(sec.getCourse().getFullName() + " (Lớp: " + sec.getId() + ")", createSectionPanel(sec));
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createSectionPanel(CourseSection section) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = new String[21];
        columns[0] = "RegId";
        columns[1] = "Mã SV";
        columns[2] = "Họ tên";
        for (int i = 1; i <= 15; i++) {
            columns[i + 2] = "T" + i;
        }
        columns[18] = "Điểm QT";
        columns[19] = "Điểm thi";
        columns[20] = "Tổng kết";

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 17) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3 && column <= 19;
            }
        };

        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsBySection(section.getId());

        for (CourseRegistration reg : registrations) {
            Object[] rowData = new Object[21];
            rowData[0] = reg.getId().toString();
            rowData[1] = reg.getStudent().getCode();
            rowData[2] = reg.getStudent().getFullName();

            Mark mark = reg.getMark();
            String attendance = mark != null && mark.getAttendance() != null ? mark.getAttendance() : "000000000000000";
            while (attendance.length() < 15) {
                attendance += "0";
            }

            for (int i = 0; i < 15; i++) {
                rowData[i + 3] = attendance.charAt(i) == '1';
            }

            rowData[18] = mark != null && mark.getProcessScore() != null ? mark.getProcessScore().toString() : "";
            rowData[19] = mark != null && mark.getTestScore() != null ? mark.getTestScore().toString() : "";
            rowData[20] = mark != null && mark.getFinalScore() != null ? mark.getFinalScore().toString() : "";

            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        TableStyles.applyModernTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        for (int i = 2; i <= 16; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
        table.getColumnModel().getColumn(17).setPreferredWidth(70);
        table.getColumnModel().getColumn(18).setPreferredWidth(80);
        table.getColumnModel().getColumn(19).setPreferredWidth(70);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(17).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(18).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(19).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        PrimaryButton btnSave = new PrimaryButton("Lưu bảng điểm");
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveGrades(table, model, registrations));
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void saveGrades(JTable table, DefaultTableModel model, List<CourseRegistration> registrations) {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        try {
            for (int r = 0; r < model.getRowCount(); r++) {
                Long regId = Long.parseLong(model.getValueAt(r, 0).toString());
                CourseRegistration reg = registrations.stream().filter(x -> x.getId().equals(regId)).findFirst().orElse(null);
                if (reg == null) {
                    continue;
                }

                Mark mark = reg.getMark();
                if (mark == null) {
                    mark = new Mark();
                    mark.setCourseRegistration(reg);
                }

                StringBuilder attendanceBuilder = new StringBuilder();
                for (int c = 3; c <= 17; c++) {
                    Boolean isPresent = (Boolean) model.getValueAt(r, c);
                    attendanceBuilder.append((isPresent != null && isPresent) ? "1" : "0");
                }
                mark.setAttendance(attendanceBuilder.toString());
                mark.setProcessScore(parseScore(model.getValueAt(r, 18)));
                mark.setTestScore(parseScore(model.getValueAt(r, 19)));

                AppContext.getMarkService().saveMark(mark);
            }
            JOptionPane.showMessageDialog(this, "Lưu thành công bảng điểm và điểm danh.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadTabs();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Có ô điểm nhập sai định dạng hoặc vượt khoảng 0-10.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu bảng điểm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal parseScore(Object val) {
        if (val == null) {
            return null;
        }
        String s = val.toString().trim();
        if (s.isEmpty()) {
            return null;
        }
        BigDecimal bd = new BigDecimal(s);
        if (bd.compareTo(BigDecimal.ZERO) < 0 || bd.compareTo(new BigDecimal("10")) > 0) {
            throw new NumberFormatException("Điểm phải nằm trong khoảng 0-10");
        }
        return bd;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\registration\CourseRegistrationPage.java <a id="CourseRegistrationPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.CourseRegistration`
- `com.bangcompany.onlineute.Model.Entity.CourseSection`
- `com.bangcompany.onlineute.Model.Entity.RegistrationBatch`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.event.ListSelectionEvent`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `java.time.format.DateTimeFormatter`
- `java.util.HashSet`
- `java.util.List`
- `java.util.Set`

```java
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseRegistrationPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DefaultListModel<RegistrationBatch> batchListModel = new DefaultListModel<>();
    private final JList<RegistrationBatch> batchList = new JList<>(batchListModel);
    private final DefaultTableModel sectionTableModel = new DefaultTableModel(
            new Object[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Còn chỗ", "Trạng thái"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable sectionTable = new JTable(sectionTableModel);
    private final JLabel batchInfoLabel = new JLabel("Chọn một đợt đăng ký để xem các môn học.", SwingConstants.LEFT);

    public CourseRegistrationPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        PageScaffold scaffold = new PageScaffold("ĐĂNG KÝ MÔN HỌC");
        scaffold.setBody(createBody());
        add(scaffold, BorderLayout.CENTER);

        configureBatchList();
        configureTable();
        loadOpenBatches();
    }

    private Component createBody() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.add(createHintPanel(), BorderLayout.NORTH);
        container.add(createContentPanel(), BorderLayout.CENTER);
        return container;
    }

    private JPanel createHintPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel hintLabel = new JLabel("<html>Hệ thống chỉ hiển thị các đợt đăng ký đang mở theo thời gian thực.<br>Chọn một đợt ở bên trái để xem các lớp học phần và đăng ký.</html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(new Color(100, 110, 120));
        panel.add(hintLabel, BorderLayout.CENTER);
        return panel;
    }

    private Component createContentPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createBatchPanel(), createSectionPanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.3);
        return splitPane;
    }

    private Component createBatchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Đợt đăng ký đang mở");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(batchList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Component createSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        batchInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        batchInfoLabel.setForeground(new Color(80, 90, 100));
        panel.add(batchInfoLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(sectionTable);
        TableStyles.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        PrimaryButton registerButton = new PrimaryButton("Đăng ký lớp đã chọn");
        registerButton.setPreferredSize(new Dimension(190, 42));
        registerButton.addActionListener(e -> registerSelectedSection());
        actions.add(registerButton);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private void configureBatchList() {
        batchList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        batchList.setFixedCellHeight(56);
        batchList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel item = new JPanel(new BorderLayout(0, 6));
            item.setBorder(new EmptyBorder(10, 12, 10, 12));
            item.setBackground(isSelected ? new Color(223, 236, 255) : Color.WHITE);

            JLabel nameLabel = new JLabel(value.getName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(new Color(25, 35, 45));

            String termText = value.getTerm() == null ? "" : value.getTerm().toString();
            JLabel metaLabel = new JLabel(termText + " | " + DATE_TIME_FORMATTER.format(value.getOpenAt()) + " - " + DATE_TIME_FORMATTER.format(value.getCloseAt()));
            metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            metaLabel.setForeground(new Color(100, 110, 120));

            item.add(nameLabel, BorderLayout.NORTH);
            item.add(metaLabel, BorderLayout.CENTER);
            return item;
        });
        batchList.addListSelectionListener(this::onBatchSelected);
    }

    private void configureTable() {
        sectionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sectionTable.setSelectionBackground(new Color(223, 236, 255));
        sectionTable.setSelectionForeground(new Color(30, 30, 30));
        sectionTable.setGridColor(new Color(230, 235, 240));
        sectionTable.setFillsViewportHeight(true);
        TableStyles.applyModernTable(sectionTable);
        TableStyles.centerColumns(sectionTable, 0, 1, 4, 5, 6, 7, 8);
    }

    private void onBatchSelected(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            loadSectionsForSelectedBatch();
        }
    }

    private void loadOpenBatches() {
        batchListModel.clear();
        List<RegistrationBatch> openBatches = AppContext.getRegistrationBatchController().getOpenBatches(LocalDateTime.now());
        for (RegistrationBatch batch : openBatches) {
            batchListModel.addElement(batch);
        }

        if (!batchListModel.isEmpty()) {
            batchList.setSelectedIndex(0);
        } else {
            batchInfoLabel.setText("Hiện tại chưa có đợt đăng ký nào đang mở.");
            sectionTableModel.setRowCount(0);
        }
    }

    private void loadSectionsForSelectedBatch() {
        sectionTableModel.setRowCount(0);
        RegistrationBatch batch = batchList.getSelectedValue();
        if (batch == null) {
            batchInfoLabel.setText("Chọn một đợt đăng ký để xem các môn học.");
            return;
        }

        Set<Long> registeredSectionIds = getRegisteredSectionIds();
        batchInfoLabel.setText("Đợt " + batch.getName() + " | Học kỳ " + batch.getTerm() + " | Bắt đầu học " + DATE_FORMATTER.format(batch.getCommonStartDate()));

        List<CourseSection> sections = AppContext.getCourseSectionController().getSectionsByBatch(batch.getId());
        for (CourseSection section : sections) {
            int current = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
            int max = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
            String availability = current + "/" + max;
            String state = registeredSectionIds.contains(section.getId()) ? "Đã đăng ký" : (current >= max ? "Đã đầy" : "Có thể đăng ký");

            sectionTableModel.addRow(new Object[]{
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    availability,
                    state
            });
        }
    }

    private Set<Long> getRegisteredSectionIds() {
        Set<Long> ids = new HashSet<>();
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return ids;
        }
        for (CourseRegistration registration : AppContext.getCourseRegistrationController().getStudentRegistrations(student.getId())) {
            if (registration.getCourseSection() != null) {
                ids.add(registration.getCourseSection().getId());
            }
        }
        return ids;
    }

    private void registerSelectedSection() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sinh viên đang đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học phần trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long sectionId = ((Number) sectionTableModel.getValueAt(selectedRow, 0)).longValue();
        try {
            AppContext.getCourseRegistrationController().registerStudentToSection(student.getId(), sectionId);
            JOptionPane.showMessageDialog(this, "Đăng ký môn học thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadSectionsForSelectedBatch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDay(Integer dayOfWeek) {
        if (dayOfWeek == null) {
            return "";
        }
        return switch (dayOfWeek) {
            case 1 -> "Thứ 2";
            case 2 -> "Thứ 3";
            case 3 -> "Thứ 4";
            case 4 -> "Thứ 5";
            case 5 -> "Thứ 6";
            case 6 -> "Thứ 7";
            case 7 -> "Chủ nhật";
            default -> "Không rõ";
        };
    }

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }

    @Override
    public void onEnter() {
        loadOpenBatches();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\lecturer\LecturerManagementDashboard.java <a id="LecturerManagementDashboard_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.View.Components.RoundedOutlineBorder`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.lecturer;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.View.Components.RoundedOutlineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LecturerManagementDashboard extends JPanel {
    private final JLabel totalLecturersValue = new JLabel("0");

    public LecturerManagementDashboard() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.add(createSummaryCard("\u0054\u1ed5\u006e\u0067\u0020\u0067\u0069\u1ea3\u006e\u0067\u0020\u0076\u0069\u00ea\u006e", totalLecturersValue, new Color(0, 85, 141)));

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

        JLabel descLabel = new JLabel("<html>\u0054\u00ec\u006d\u0020\u006e\u0068\u0061\u006e\u0068\u0020\u0074\u0068\u0065\u006f\u0020<b>\u006d\u00e3\u0020\u0067\u0069\u1ea3\u006e\u0067\u0020\u0076\u0069\u00ea\u006e</b>\u0020\u0068\u006f\u1eb7\u0063\u0020<b>\u0068\u1ecd\u0020\u0074\u00ea\u006e</b>.<br>\u004b\u0068\u0069\u0020\u00f4\u0020\u0074\u00ec\u006d\u0020\u006b\u0069\u1ebf\u006d\u0020\u0063\u00f2\u006e\u0020\u0074\u0072\u1ed1\u006e\u0067, \u006d\u00e0\u006e\u0020\u006e\u00e0\u0079\u0020\u0073\u1ebd\u0020\u0068\u0069\u1ec3\u006e\u0020\u0074\u0068\u1ecb\u0020\u0064\u0061\u0073\u0068\u0062\u006f\u0061\u0072\u0064\u0020\u0074\u1ed5\u006e\u0067\u0020\u0071\u0075\u0061\u006e.</html>");
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
        totalLecturersValue.setText(String.valueOf(AppContext.getLecturerController().countAllLecturers()));
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

```

## src\main\java\com\bangcompany\onlineute\View\features\profile\ProfilePage.java <a id="ProfilePage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.UserProfile`
- `com.bangcompany.onlineute.View.Components.TagChip`
- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.CompoundBorder`
- `javax.swing.border.EmptyBorder`
- `javax.swing.border.LineBorder`
- `java.awt`
- `java.time.LocalDate`
- `java.time.format.DateTimeFormatter`

```java
package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.View.Components.TagChip;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfilePage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PageScaffold scaffold;

    public ProfilePage() {
        setLayout(new BorderLayout());

        scaffold = new PageScaffold("Thông tin cá nhân");
        scaffold.setBody(createLoadingState());
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        UserProfile profile = AppContext.getUserProfileController().getCurrentUserProfile();
        scaffold.setBody(createProfileContent(profile));
        revalidate();
        repaint();
    }

    private JComponent createLoadingState() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(new JLabel("Đang tải", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JComponent createProfileContent(UserProfile profile) {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createSummaryCard(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createPersonalSection(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createAcademicSection(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createContactSection(profile));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createSummaryCard(UserProfile profile) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(224, 229, 236), 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        card.add(createAvatarPanel(profile), BorderLayout.WEST);
        card.add(createSummaryInfo(profile), BorderLayout.CENTER);
        return card;
    }

    private JComponent createAvatarPanel(UserProfile profile) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(130, 130));

        JLabel avatar = new JLabel(initialsOf(profile.getDisplayName()), SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(96, 96));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(24, 70, 121));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 30));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(209, 220, 235), 4, true));
        wrapper.add(avatar);
        return wrapper;
    }

    private JComponent createSummaryInfo(UserProfile profile) {
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(valueOf(profile.getDisplayName()));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        nameLabel.setForeground(new Color(24, 70, 121));

        JLabel subLabel = new JLabel(valueOf(profile.getRoleTitle()) + "  |  " + valueOf(profile.getProfileCode()));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(new Color(99, 115, 129));

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        chips.setOpaque(false);
        chips.add(new TagChip("Email: " + valueOf(profile.getEmail())));
        chips.add(new TagChip("Điện thoại: " + valueOf(profile.getPhoneNumber())));
        chips.add(new TagChip("Lớp: " + valueOf(profile.getClassName())));

        info.add(nameLabel);
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(subLabel);
        info.add(Box.createRigidArea(new Dimension(0, 18)));
        info.add(chips);
        return info;
    }

    private ProfileSectionCard createPersonalSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin cá nhân");
        card.addField("Họ và tên", valueOf(profile.getDisplayName()));
        card.addField("Mã hồ sơ", valueOf(profile.getProfileCode()));
        card.addField("Ngày sinh", formatDate(profile.getBirthDate()));
        card.addField("Giới tính", valueOf(profile.getGender()));
        card.addField("Nơi sinh", valueOf(profile.getPlaceOfBirth()));
        card.addField("Quốc tịch", valueOf(profile.getNationality()));
        card.addField("Dân tộc", valueOf(profile.getEthnicity()));
        card.addField("Tôn giáo", valueOf(profile.getReligion()));
        card.addField("CCCD / CMND", valueOf(profile.getCitizenIdNumber()));
        card.addField("Nơi cấp", valueOf(profile.getCitizenIdIssuePlace()));
        card.addField("Ngày cấp", formatDate(profile.getCitizenIdIssueDate()));
        card.addField("Địa chỉ thường trú", valueOf(profile.getPermanentAddress()));
        card.addField("Địa chỉ liên lạc", valueOf(profile.getCurrentAddress()));
        card.addField("Email", valueOf(profile.getEmail()));
        return card;
    }

    private ProfileSectionCard createAcademicSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin học tập");
        card.addField("Vai trò", valueOf(profile.getRoleTitle()));
        card.addField("Khoa / Đơn vị", valueOf(profile.getFacultyName()));
        card.addField("Lớp", valueOf(profile.getClassName()));
        card.addField("Ngành", valueOf(profile.getMajorName()));
        card.addField("Niên khóa", valueOf(profile.getAcademicYear()));
        card.addField("Năm tốt nghiệp dự kiến", valueOf(profile.getExpectedGraduationYear()));
        return card;
    }

    private ProfileSectionCard createContactSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin liên hệ");
        card.addField("Người liên hệ", valueOf(profile.getContactName()));
        card.addField("Điện thoại liên hệ", valueOf(profile.getContactPhone()));
        card.addField("Địa chỉ liên hệ", valueOf(profile.getContactAddress()));
        card.addField("Họ tên cha", valueOf(profile.getFatherName()));
        card.addField("Điện thoại cha", valueOf(profile.getFatherPhone()));
        card.addField("Họ tên mẹ", valueOf(profile.getMotherName()));
        card.addField("Điện thoại mẹ", valueOf(profile.getMotherPhone()));
        return card;
    }

    private String valueOf(String value) {
        return value == null || value.isBlank() ? "Chưa cập nhật" : value;
    }

    private String formatDate(LocalDate date) {
        return date == null ? "Chưa cập nhật" : date.format(DATE_FORMATTER);
    }

    private String initialsOf(String value) {
        if (value == null || value.isBlank()) {
            return "?";
        }

        String[] parts = value.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        String first = parts[0].substring(0, 1);
        String last = parts[parts.length - 1].substring(0, 1);
        return (first + last).toUpperCase();
    }
}


```

## src\main\java\com\bangcompany\onlineute\View\features\lecturer\LecturerManagementPage.java <a id="LecturerManagementPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.DTO.PagedResult`
- `com.bangcompany.onlineute.Model.Entity.Lecturer`
- `com.bangcompany.onlineute.View.features.account.CreateAccountPage`
- `com.bangcompany.onlineute.View.features.data.ManagementShellPage`
- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.lecturer;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.data.ManagementShellPage;

import javax.swing.*;
import java.awt.*;

public class LecturerManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final LecturerManagementDashboard dashboardPanel;
    private final LecturerSearchResultPanel searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";

    public LecturerManagementPage() {
        this(new LecturerManagementDashboard());
    }

    private LecturerManagementPage(LecturerManagementDashboard dashboardPanel) {
        super(
                "Tim theo ma giang vien, ho ten",
                null,
                null,
                2,
                dashboardPanel,
                new LecturerSearchResultPanel(direction -> {})
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (LecturerSearchResultPanel) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        configureCreateAction("Tao moi", this::openCreateLecturerDialog);
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword;
        currentPage = 1;
        loadCurrentPage();
    }

    @Override
    protected void onKeywordCleared() {
        currentKeyword = "";
        currentPage = 1;
        dashboardPanel.refreshData();
    }

    private void loadCurrentPage() {
        PagedResult<Lecturer> result = AppContext.getLecturerController()
                .searchLecturers(currentKeyword, currentPage, PAGE_SIZE);
        searchResultPanel.showResults(result, currentKeyword);
        showResults();
    }

    private void changePage(int direction) {
        int nextPage = currentPage + direction;
        if (nextPage < 1) {
            return;
        }
        currentPage = nextPage;
        loadCurrentPage();
    }

    private void openCreateLecturerDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tao moi giang vien", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Giang vien"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\grade\ViewGradesPage.java <a id="ViewGradesPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.Course`
- `com.bangcompany.onlineute.Model.Entity.CourseRegistration`
- `com.bangcompany.onlineute.Model.Entity.Mark`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.math.BigDecimal`
- `java.util.ArrayList`
- `java.util.HashSet`
- `java.util.List`
- `java.util.Set`

```java
package com.bangcompany.onlineute.View.features.grade;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewGradesPage extends JPanel implements Refreshable {
    private final DefaultTableModel tableModel;

    public ViewGradesPage() {
        setLayout(new BorderLayout());

        String[] cols = {"STT", "Ma mon hoc", "Ten mon hoc", "So TC", "Diem he 10", "Diem he 4", "Diem chu", "Ket qua"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        TableStyles.applyModernTable(table);
        TableStyles.centerColumns(table, 0, 1, 3, 4, 5, 6, 7);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        PageScaffold scaffold = new PageScaffold("Ket qua hoc tap");
        scaffold.setBody(scrollPane);
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return;
        }

        tableModel.setRowCount(0);
        List<CourseRegistration> registrations = AppContext.getCourseRegistrationService().getRegistrationsByStudent(student.getId());
        List<Course> allCourses = AppContext.getCourseService().getAllCourses();
        if (allCourses == null) {
            allCourses = new ArrayList<>();
        }

        Set<Long> studiedCourseIds = new HashSet<>();

        addHeaderRow("--- CAC MON DA VA DANG HOC ---");
        int stt = 1;
        for (CourseRegistration registration : registrations) {
            Course course = registration.getCourseSection() != null ? registration.getCourseSection().getCourse() : null;
            if (course != null) {
                studiedCourseIds.add(course.getId());
            }

            Mark mark = registration.getMark();
            String diem10 = "";
            String diem4 = "";
            String diemChu = "";
            String ketQua = "";

            if (mark != null && mark.getFinalScore() != null) {
                BigDecimal finalScore = mark.getFinalScore();
                diem10 = finalScore.toString();
                if (finalScore.compareTo(new BigDecimal("8.5")) >= 0) {
                    diem4 = "4.0";
                } else if (finalScore.compareTo(new BigDecimal("7.0")) >= 0) {
                    diem4 = "3.0";
                } else if (finalScore.compareTo(new BigDecimal("5.5")) >= 0) {
                    diem4 = "2.0";
                } else if (finalScore.compareTo(new BigDecimal("4.0")) >= 0) {
                    diem4 = "1.0";
                } else {
                    diem4 = "0.0";
                }

                diemChu = mark.getGradeChar() != null ? mark.getGradeChar() : "";
                ketQua = !"0.0".equals(diem4) ? "Dat" : "Rot";
            }

            tableModel.addRow(new Object[]{
                    stt++,
                    course != null ? "M" + course.getId() : "",
                    course != null ? course.getFullName() : "",
                    course != null ? course.getCredit() : "",
                    diem10, diem4, diemChu, ketQua
            });
        }

        addHeaderRow("--- CAC MON CHUA HOC (THAM KHAO) ---");
        stt = 1;
        for (Course course : allCourses) {
            if (!studiedCourseIds.contains(course.getId())) {
                tableModel.addRow(new Object[]{
                        stt++,
                        "M" + course.getId(),
                        course.getFullName(),
                        course.getCredit(),
                        "", "", "", ""
                });
            }
        }
    }

    private void addHeaderRow(String text) {
        tableModel.addRow(new Object[]{"", "", text, "", "", "", "", ""});
    }
}


```

## src\main\java\com\bangcompany\onlineute\View\features\profile\ProfileSectionCard.java <a id="ProfileSectionCard_java"></a>

### Dependencies

- `com.bangcompany.onlineute.View.Components.LabelValuePanel`
- `javax.swing`
- `javax.swing.border.CompoundBorder`
- `javax.swing.border.EmptyBorder`
- `javax.swing.border.LineBorder`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.View.Components.LabelValuePanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ProfileSectionCard extends JPanel {
    private final JPanel contentPanel;

    public ProfileSectionCard(String title) {
        setLayout(new BorderLayout(0, 14));
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
                new LineBorder(new Color(224, 229, 236), 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 70, 121));
        add(titleLabel, BorderLayout.NORTH);

        contentPanel = new JPanel(new GridLayout(0, 2, 24, 0));
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void addField(String label, String value) {
        contentPanel.add(new LabelValuePanel(label, value));
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\registration\CourseSectionDialog.java <a id="CourseSectionDialog_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.Course`
- `com.bangcompany.onlineute.Model.Entity.CourseSection`
- `com.bangcompany.onlineute.Model.Entity.Lecturer`
- `com.bangcompany.onlineute.Model.Entity.RegistrationBatch`
- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.SelectGroup`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.util.List`

```java
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseSectionDialog extends JDialog {
    private final RegistrationBatch selectedBatch;

    private final InputGroup sectionCodeInput = new InputGroup("Mã lớp học phần", false);
    private final SelectGroup<Course> courseSelect = new SelectGroup<>("Môn học", AppContext.getCourseService().getAllCourses());
    private final SelectGroup<Lecturer> lecturerSelect = new SelectGroup<>("Giảng viên", AppContext.getLecturerDAO().findAll());
    private final InputGroup roomInput = new InputGroup("Phòng học", false);
    private final InputGroup maxCapacityInput = new InputGroup("Số lượng tối đa", false);
    private final SelectGroup<DayOption> daySelect = new SelectGroup<>("Thứ học", List.of(
            new DayOption(1, "Thứ 2"),
            new DayOption(2, "Thứ 3"),
            new DayOption(3, "Thứ 4"),
            new DayOption(4, "Thứ 5"),
            new DayOption(5, "Thứ 6"),
            new DayOption(6, "Thứ 7"),
            new DayOption(7, "Chủ nhật")
    ));
    private final InputGroup startSlotInput = new InputGroup("Tiết bắt đầu", false);
    private final InputGroup endSlotInput = new InputGroup("Tiết kết thúc", false);
    private final InputGroup totalWeeksInput = new InputGroup("Số tuần học", false);

    private final DefaultTableModel sectionTableModel = new DefaultTableModel(
            new Object[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Số tuần", "Sĩ số"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable sectionTable = new JTable(sectionTableModel);

    public CourseSectionDialog(Window owner, RegistrationBatch selectedBatch) {
        super(owner, "Quản lý lớp học phần", ModalityType.APPLICATION_MODAL);
        this.selectedBatch = selectedBatch;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(owner);
        setContentPane(createContent());

        fillDefaultValues();
        configureSectionTable();
        loadSectionsIntoTable();
    }

    private Container createContent() {
        JPanel container = new JPanel(new BorderLayout(0, 16));
        container.setBackground(new Color(245, 245, 245));
        container.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Tạo lớp học phần cho đợt: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        container.add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createTablePanel());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.42);
        splitPane.setDividerLocation(360);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        splitPane.setOneTouchExpandable(false);
        container.add(splitPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel hint = new JLabel("<html>Khai báo môn học, giảng viên, phòng, thứ học, tiết học và số tuần.<br>Hệ thống sẽ tự động tính ngày học đầu tiên và sinh lịch học theo tuần.</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(100, 110, 120));
        panel.add(hint, gbc);

        gbc.gridy++;
        panel.add(sectionCodeInput, gbc);

        gbc.gridy++;
        panel.add(courseSelect, gbc);

        gbc.gridy++;
        panel.add(lecturerSelect, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(roomInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(maxCapacityInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(daySelect, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(totalWeeksInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 18, 10);
        panel.add(startSlotInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 18, 0);
        panel.add(endSlotInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        PrimaryButton createSectionButton = new PrimaryButton("\u0054\u1ea1\u006f\u0020\u006c\u1edb\u0070\u0020\u0068\u1ecd\u0063\u0020\u0070\u0068\u1ea7\u006e");
        createSectionButton.setPreferredSize(new Dimension(180, 40));
        createSectionButton.addActionListener(e -> createCourseSection());

        actions.add(createSectionButton);
        panel.add(actions, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Danh sách lớp học phần của đợt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(sectionTable);
        TableStyles.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void configureSectionTable() {
        sectionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sectionTable.setSelectionBackground(new Color(223, 236, 255));
        sectionTable.setSelectionForeground(new Color(30, 30, 30));
        sectionTable.setGridColor(new Color(230, 235, 240));
        sectionTable.setFillsViewportHeight(true);
        TableStyles.applyModernTable(sectionTable);
        TableStyles.centerColumns(sectionTable, 0, 1, 4, 5, 6, 7, 8);
    }

    private void createCourseSection() {
        try {
            CourseSection section = new CourseSection();
            section.setSectionCode(sectionCodeInput.getValue().trim());
            section.setCourse(courseSelect.getSelectedValue());
            section.setLecturer(lecturerSelect.getSelectedValue());
            section.setRoom(roomInput.getValue().trim());
            section.setMaxCapacity(Integer.parseInt(maxCapacityInput.getValue().trim()));
            section.setCurrentCapacity(0);
            section.setDayOfWeek(daySelect.getSelectedValue().value());
            section.setStartSlot(Integer.parseInt(startSlotInput.getValue().trim()));
            section.setEndSlot(Integer.parseInt(endSlotInput.getValue().trim()));
            section.setTotalWeeks(Integer.parseInt(totalWeeksInput.getValue().trim()));

            AppContext.getCourseSectionController().createSectionForBatch(selectedBatch, section);

            JOptionPane.showMessageDialog(this, "Tạo lớp học phần thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearSectionForm();
            loadSectionsIntoTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được lớp học phần: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSectionsIntoTable() {
        sectionTableModel.setRowCount(0);
        List<CourseSection> sections = AppContext.getCourseSectionController().getSectionsByBatch(selectedBatch.getId());
        for (CourseSection section : sections) {
            sectionTableModel.addRow(new Object[]{
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    section.getTotalWeeks(),
                    (section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity()) + "/" + (section.getMaxCapacity() == null ? 0 : section.getMaxCapacity())
            });
        }
    }

    private void clearSectionForm() {
        sectionCodeInput.setValue("");
        roomInput.setValue("");
        maxCapacityInput.setValue("70");
        startSlotInput.setValue("");
        endSlotInput.setValue("");
        totalWeeksInput.setValue("15");
    }

    private void fillDefaultValues() {
        if (maxCapacityInput.getValue().isBlank()) {
            maxCapacityInput.setValue("70");
        }
        if (totalWeeksInput.getValue().isBlank()) {
            totalWeeksInput.setValue("15");
        }
    }

    private String formatDay(Integer value) {
        if (value == null) {
            return "";
        }
        return switch (value) {
            case 1 -> "Thứ 2";
            case 2 -> "Thứ 3";
            case 3 -> "Thứ 4";
            case 4 -> "Thứ 5";
            case 5 -> "Thứ 6";
            case 6 -> "Thứ 7";
            case 7 -> "Chủ nhật";
            default -> "Không rõ";
        };
    }

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }

    private record DayOption(int value, String label) {
        @Override
        public String toString() {
            return label;
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\lecturer\LecturerSearchResultPanel.java <a id="LecturerSearchResultPanel_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Model.DTO.PagedResult`
- `com.bangcompany.onlineute.Model.Entity.Lecturer`
- `com.bangcompany.onlineute.View.Components.PaginationPanel`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.util.function.IntConsumer`

```java
package com.bangcompany.onlineute.View.features.lecturer;

import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.View.Components.PaginationPanel;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.IntConsumer;

public class LecturerSearchResultPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JLabel resultLabel = new JLabel("0 ket qua");
    private final PaginationPanel paginationPanel;
    private IntConsumer onPageRequested;

    public LecturerSearchResultPanel(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 85, 141));
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        tableModel = new DefaultTableModel(
                new Object[]{"Ma GV", "Ho ten"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(235, 240, 245));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        TableStyles.applyModernTable(table);
        TableStyles.centerColumns(table, 0);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        paginationPanel = new PaginationPanel(
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(-1);
                    }
                },
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(1);
                    }
                }
        );

        add(resultLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    public void showResults(PagedResult<Lecturer> result, String keyword) {
        tableModel.setRowCount(0);

        for (Lecturer lecturer : result.getItems()) {
            tableModel.addRow(new Object[]{
                    lecturer.getCode(),
                    lecturer.getFullName()
            });
        }

        resultLabel.setText(result.getTotalItems() + " ket qua cho: " + keyword.trim());
        paginationPanel.updateState(result.getPage(), result.getTotalPages(), result.hasPrevious(), result.hasNext());
    }

    public void setPageHandler(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\student\StudentSearchResultPanel.java <a id="StudentSearchResultPanel_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Model.DTO.PagedResult`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.View.Components.PaginationPanel`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.util.function.IntConsumer`

```java
package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.PaginationPanel;
import com.bangcompany.onlineute.View.Components.TableStyles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.IntConsumer;

public class StudentSearchResultPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JLabel resultLabel = new JLabel("0 ket qua");
    private final PaginationPanel paginationPanel;
    private IntConsumer onPageRequested;

    public StudentSearchResultPanel(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultLabel.setForeground(new Color(0, 85, 141));
        resultLabel.setBorder(new EmptyBorder(0, 4, 0, 0));

        tableModel = new DefaultTableModel(
                new Object[]{"Ma SV", "Ho ten", "Email", "Lop", "Khoa", "Nam nhap hoc"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(235, 240, 245));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        TableStyles.applyModernTable(table);
        TableStyles.centerColumns(table, 0, 5);

        JScrollPane scrollPane = new JScrollPane(table);
        TableStyles.styleScrollPane(scrollPane);

        paginationPanel = new PaginationPanel(
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(-1);
                    }
                },
                () -> {
                    if (this.onPageRequested != null) {
                        this.onPageRequested.accept(1);
                    }
                }
        );

        add(resultLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    public void showResults(PagedResult<Student> result, String keyword) {
        tableModel.setRowCount(0);

        for (Student student : result.getItems()) {
            String className = student.getClassEntity() != null ? student.getClassEntity().getClassName() : "";
            String facultyName = student.getClassEntity() != null && student.getClassEntity().getFaculty() != null
                    ? student.getClassEntity().getFaculty().getFullName()
                    : "";

            tableModel.addRow(new Object[]{
                    student.getCode(),
                    student.getFullName(),
                    student.getEmail(),
                    className,
                    facultyName,
                    student.getEnrollmentYear()
            });
        }

        resultLabel.setText(result.getTotalItems() + " ket qua cho: " + keyword.trim());
        paginationPanel.updateState(result.getPage(), result.getTotalPages(), result.hasPrevious(), result.hasNext());
    }

    public void setPageHandler(IntConsumer onPageRequested) {
        this.onPageRequested = onPageRequested;
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\registration\CreateRegistrationBatchPage.java <a id="CreateRegistrationBatchPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.Entity.RegistrationBatch`
- `com.bangcompany.onlineute.Model.Entity.Term`
- `com.bangcompany.onlineute.View.Components.InputGroup`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.Components.SelectGroup`
- `com.bangcompany.onlineute.View.Components.TableStyles`
- `com.bangcompany.onlineute.View.features.dashboard.PageScaffold`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.event.ListSelectionEvent`
- `javax.swing.table.DefaultTableModel`
- `java.awt`
- `java.awt.event.MouseAdapter`
- `java.awt.event.MouseEvent`
- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `java.time.format.DateTimeFormatter`

```java
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TableStyles;
import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateRegistrationBatchPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final InputGroup batchNameInput = new InputGroup("Tên đợt đăng ký", false);
    private final InputGroup openAtInput = new InputGroup("Mở đăng ký (yyyy-MM-dd HH:mm)", false);
    private final InputGroup closeAtInput = new InputGroup("Đóng đăng ký (yyyy-MM-dd HH:mm)", false);
    private final InputGroup commonStartDateInput = new InputGroup("Ngày bắt đầu học chung (yyyy-MM-dd)", false);
    private final SelectGroup<Term> termSelect = new SelectGroup<>("Học kỳ áp dụng", AppContext.getTermService().getAllTerms());
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tên đợt", "Học kỳ", "Mở đăng ký", "Đóng đăng ký", "Bắt đầu học"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable batchTable = new JTable(tableModel);
    private final JLabel selectedBatchLabel = new JLabel("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.", SwingConstants.LEFT);

    private RegistrationBatch selectedBatch;

    public CreateRegistrationBatchPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        PageScaffold scaffold = new PageScaffold("TẠO ĐỢT ĐĂNG KÝ MÔN");
        scaffold.setBody(createBody());
        add(scaffold, BorderLayout.CENTER);

        configureTable();
        fillDefaultValues();
        loadBatches();
    }

    private Component createBody() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.add(createFormPanel(), BorderLayout.NORTH);
        container.add(createBatchTablePanel(), BorderLayout.CENTER);
        container.add(createActionPanel(), BorderLayout.SOUTH);
        return container;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel hintLabel = new JLabel("<html>Admin khai báo tên đợt, thời gian mở đóng đăng ký, học kỳ và ngày bắt đầu học chung.<br>Sau đó chọn đợt đăng ký để mở popup quản lý lớp học phần.</html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(new Color(100, 110, 120));
        panel.add(hintLabel, gbc);

        gbc.gridy++;
        panel.add(batchNameInput, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(openAtInput, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(closeAtInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 10);
        panel.add(termSelect, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 14, 0);
        panel.add(commonStartDateInput, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        PrimaryButton createButton = new PrimaryButton("Tạo đợt đăng ký");
        createButton.setPreferredSize(new Dimension(180, 44));
        createButton.addActionListener(e -> createBatch());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createBatchTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Danh sách đợt đăng ký đã tạo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(25, 35, 45));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(batchTable);
        TableStyles.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        selectedBatchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedBatchLabel.setForeground(new Color(25, 35, 45));
        panel.add(selectedBatchLabel, BorderLayout.WEST);

        PrimaryButton openDialogButton = new PrimaryButton("Thêm lớp học phần");
        openDialogButton.setPreferredSize(new Dimension(200, 44));
        openDialogButton.addActionListener(e -> openSectionDialog());
        panel.add(openDialogButton, BorderLayout.EAST);

        return panel;
    }

    private void configureTable() {
        batchTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        batchTable.setSelectionBackground(new Color(223, 236, 255));
        batchTable.setSelectionForeground(new Color(30, 30, 30));
        batchTable.setGridColor(new Color(230, 235, 240));
        batchTable.setFillsViewportHeight(true);
        batchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableStyles.applyModernTable(batchTable);
        TableStyles.centerColumns(batchTable, 0, 3, 4, 5);
        batchTable.getSelectionModel().addListSelectionListener(this::onBatchTableSelectionChanged);
        batchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    SwingUtilities.invokeLater(() -> {
                        if (selectedBatch != null) {
                            openSectionDialog();
                        }
                    });
                }
            }
        });
    }

    private void createBatch() {
        try {
            RegistrationBatch batch = new RegistrationBatch();
            batch.setName(batchNameInput.getValue().trim());
            batch.setOpenAt(LocalDateTime.parse(openAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setCloseAt(LocalDateTime.parse(closeAtInput.getValue().trim(), DATE_TIME_FORMATTER));
            batch.setTerm(termSelect.getSelectedValue());
            batch.setCommonStartDate(LocalDate.parse(commonStartDateInput.getValue().trim(), DATE_FORMATTER));

            AppContext.getRegistrationBatchController().createBatch(batch);

            JOptionPane.showMessageDialog(this, "Tạo đợt đăng ký thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            fillDefaultValues();
            loadBatches();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được đợt đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBatches() {
        tableModel.setRowCount(0);
        for (RegistrationBatch batch : AppContext.getRegistrationBatchController().getAllBatches()) {
            tableModel.addRow(new Object[]{
                    batch.getId(),
                    batch.getName(),
                    batch.getTerm() == null ? "" : batch.getTerm().toString(),
                    formatDateTime(batch.getOpenAt()),
                    formatDateTime(batch.getCloseAt()),
                    formatDate(batch.getCommonStartDate())
            });
        }
        if (tableModel.getRowCount() > 0 && batchTable.getSelectedRow() < 0) {
            batchTable.setRowSelectionInterval(0, 0);
        }
    }

    private void onBatchTableSelectionChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = batchTable.getSelectedRow();
        if (selectedRow < 0) {
            selectedBatch = null;
            selectedBatchLabel.setText("Chon mot dot dang ky o bang ben tren de mo popup tao course section.");
            return;
        }

        Long batchId = ((Number) tableModel.getValueAt(selectedRow, 0)).longValue();
        selectedBatch = AppContext.getRegistrationBatchController().getBatchById(batchId).orElse(null);
        if (selectedBatch == null) {
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.");
            return;
        }
        selectedBatchLabel.setText("Đã chọn: " + selectedBatch.getName() + " | " + selectedBatch.getTerm());
    }

    private void openSectionDialog() {
        if (selectedBatch == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đợt đăng ký trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CourseSectionDialog dialog = new CourseSectionDialog(SwingUtilities.getWindowAncestor(this), selectedBatch);
        dialog.setVisible(true);
    }

    private String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATE_TIME_FORMATTER.format(value);
    }

    private String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMATTER.format(value);
    }

    private void clearForm() {
        batchNameInput.setValue("");
        openAtInput.setValue("");
        closeAtInput.setValue("");
        commonStartDateInput.setValue("");
    }

    private void fillDefaultValues() {
        if (openAtInput.getValue().isBlank()) {
            openAtInput.setValue(LocalDateTime.now().withMinute(0).format(DATE_TIME_FORMATTER));
        }
        if (closeAtInput.getValue().isBlank()) {
            closeAtInput.setValue(LocalDateTime.now().plusDays(7).withMinute(0).format(DATE_TIME_FORMATTER));
        }
        if (commonStartDateInput.getValue().isBlank()) {
            commonStartDateInput.setValue(LocalDate.now().plusWeeks(1).format(DATE_FORMATTER));
        }
    }

    @Override
    public void onEnter() {
        termSelect.setItems(AppContext.getTermService().getAllTerms());
        loadBatches();
        if (selectedBatch == null) {
            selectedBatchLabel.setText("Chọn một đợt đăng ký ở bảng bên trên để mở popup tạo lớp học phần.");
        }
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\student\StudentManagementDashboard.java <a id="StudentManagementDashboard_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.View.Components.RoundedOutlineBorder`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `java.awt`

```java
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

```

## src\main\java\com\bangcompany\onlineute\View\navigation\Refreshable.java <a id="Refreshable_java"></a>

```java
package com.bangcompany.onlineute.View.navigation;

public interface Refreshable {
    void onEnter();
}

```

## src\main\java\com\bangcompany\onlineute\View\features\schedule\SchedulePage.java <a id="SchedulePage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.Model.Entity.Schedule`
- `com.bangcompany.onlineute.View.Components.PageTitleLabel`
- `com.bangcompany.onlineute.View.Components.PrimaryButton`
- `com.bangcompany.onlineute.View.navigation.Refreshable`
- `javax.swing`
- `javax.swing.border.EmptyBorder`
- `javax.swing.border.MatteBorder`
- `java.awt`
- `java.time.DayOfWeek`
- `java.time.LocalDate`
- `java.time.format.DateTimeFormatter`
- `java.util.ArrayList`
- `java.util.HashMap`
- `java.util.List`
- `java.util.Map`

```java
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Color primaryColor = new Color(0, 85, 141);
    private final String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    private final String[] periods = {"Sáng", "Chiều", "Tối"};

    private final JPanel gridPanel = new JPanel(new GridBagLayout());
    private final JLabel weekLabel = new JLabel("", SwingConstants.LEFT);
    private LocalDate selectedWeekStart;

    public SchedulePage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("THỜI KHÓA BIỂU"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentWrapper.add(createFilterPanel(), BorderLayout.NORTH);

        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        selectedWeekStart = getWeekStart(LocalDate.now());
        onEnter();
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);

        JLabel currentScheduleLabel = new JLabel("Sinh viên có thể chuyển tuần để xem môn học sẽ học");
        currentScheduleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentScheduleLabel.setForeground(new Color(0, 85, 141));
        leftPanel.add(currentScheduleLabel);

        weekLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        weekLabel.setForeground(new Color(80, 90, 100));
        leftPanel.add(weekLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        PrimaryButton previousWeekButton = new PrimaryButton("Tuần trước");
        previousWeekButton.setPreferredSize(new Dimension(120, 40));
        previousWeekButton.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            refreshScheduleGrid();
        });

        PrimaryButton currentWeekButton = new PrimaryButton("Tuần hiện tại");
        currentWeekButton.setPreferredSize(new Dimension(130, 40));
        currentWeekButton.addActionListener(e -> {
            selectedWeekStart = getWeekStart(LocalDate.now());
            refreshScheduleGrid();
        });

        PrimaryButton nextWeekButton = new PrimaryButton("Tuần sau");
        nextWeekButton.setPreferredSize(new Dimension(120, 40));
        nextWeekButton.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            refreshScheduleGrid();
        });

        rightPanel.add(previousWeekButton);
        rightPanel.add(currentWeekButton);
        rightPanel.add(nextWeekButton);

        filterPanel.add(leftPanel, BorderLayout.WEST);
        filterPanel.add(rightPanel, BorderLayout.EAST);
        return filterPanel;
    }

    @Override
    public void onEnter() {
        refreshScheduleGrid();
    }

    private void refreshScheduleGrid() {
        LocalDate weekEnd = selectedWeekStart.plusDays(6);
        weekLabel.setText("Tuần: " + DATE_FORMATTER.format(selectedWeekStart) + " - " + DATE_FORMATTER.format(weekEnd));

        gridPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        addHeaders(gbc);
        addScheduleRows(gbc, loadSchedulesForCurrentWeek());

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addHeaders(GridBagConstraints gbc) {
        gbc.gridy = 0;
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
    }

    private void addScheduleRows(GridBagConstraints gbc, List<Schedule> schedules) {
        Map<String, List<Schedule>> scheduleMap = buildScheduleMap(schedules);

        for (int dayRow = 0; dayRow < days.length; dayRow++) {
            gbc.gridy = dayRow + 1;

            gbc.gridx = 0;
            gbc.weightx = 0.2;
            JPanel dayLabelPanel = createDayLabelPanel(days[dayRow], selectedWeekStart.plusDays(dayRow));
            gridPanel.add(dayLabelPanel, gbc);

            for (int periodColumn = 0; periodColumn < periods.length; periodColumn++) {
                gbc.gridx = periodColumn + 1;
                gbc.weightx = 1.0;

                JPanel cell = new JPanel();
                cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                cell.setBackground(Color.WHITE);
                cell.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));

                List<Schedule> schedulesInCell = scheduleMap.getOrDefault(dayRow + "_" + periods[periodColumn], List.of());
                if (schedulesInCell.isEmpty()) {
                    cell.add(Box.createVerticalGlue());
                } else {
                    for (Schedule schedule : schedulesInCell) {
                        cell.add(createCourseCard(schedule));
                        cell.add(Box.createRigidArea(new Dimension(0, 6)));
                    }
                }

                gridPanel.add(cell, gbc);
            }
        }
    }

    private JPanel createDayLabelPanel(String dayText, LocalDate actualDate) {
        JPanel dayLabelPanel = new JPanel(new GridBagLayout());
        dayLabelPanel.setBackground(new Color(255, 220, 180));
        dayLabelPanel.setBorder(new MatteBorder(0, 0, 1, 1, new Color(220, 230, 240)));
        dayLabelPanel.setPreferredSize(new Dimension(90, 115));

        JLabel dayLabel = new JLabel("<html><center>" + dayText + "<br>" + DATE_FORMATTER.format(actualDate) + "</center></html>");
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dayLabelPanel.add(dayLabel);
        return dayLabelPanel;
    }

    private List<Schedule> loadSchedulesForCurrentWeek() {
        var student = SessionManager.getCurrentStudent();
        if (student == null || AppContext.getScheduleService() == null) {
            return List.of();
        }
        return AppContext.getScheduleService().getStudentScheduleByWeek(student.getId(), selectedWeekStart, selectedWeekStart.plusDays(6));
    }

    private Map<String, List<Schedule>> buildScheduleMap(List<Schedule> schedules) {
        Map<String, List<Schedule>> scheduleMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            String period = resolvePeriod(schedule.getStartSlot());
            int dayIndex = schedule.getDayOfWeek() - 1;
            String key = dayIndex + "_" + period;
            scheduleMap.computeIfAbsent(key, ignored -> new ArrayList<>()).add(schedule);
        }
        return scheduleMap;
    }

    private String resolvePeriod(Integer startSlot) {
        if (startSlot == null) {
            return "Sáng";
        }
        if (startSlot <= 5) {
            return "Sáng";
        }
        if (startSlot <= 10) {
            return "Chiều";
        }
        return "Tối";
    }

    private JPanel createCourseCard(Schedule schedule) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(187, 222, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(144, 202, 249)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("<html><center><b>Môn: " + schedule.getCourseSection().getCourse().getFullName()
                + " (" + schedule.getCourseSection().getCourse().getCourseCode() + ")</b></center></html>");
        nameLabel.setForeground(new Color(198, 40, 40));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String details = String.format(
                "<html><center>Ngày học: %s<br>Tiết: %d -> %d<br>Phòng: <b>%s</b><br>GV: <font color='blue'>%s</font><br>Tuần: %d</center></html>",
                DATE_FORMATTER.format(schedule.getStudyDate()),
                schedule.getStartSlot(),
                schedule.getEndSlot(),
                schedule.getRoom(),
                schedule.getCourseSection().getLecturer().getFullName(),
                schedule.getWeekNumber()
        );

        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(detailsLabel);
        return card;
    }

    private LocalDate getWeekStart(LocalDate date) {
        int offset = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        if (offset < 0) {
            offset += 7;
        }
        return date.minusDays(offset);
    }
}

```

## src\main\java\com\bangcompany\onlineute\View\features\student\StudentManagementPage.java <a id="StudentManagementPage_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.AppContext`
- `com.bangcompany.onlineute.Model.DTO.PagedResult`
- `com.bangcompany.onlineute.Model.Entity.Student`
- `com.bangcompany.onlineute.View.features.account.CreateAccountPage`
- `com.bangcompany.onlineute.View.features.data.ManagementShellPage`
- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.data.ManagementShellPage;

import javax.swing.*;
import java.awt.*;

public class StudentManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final StudentManagementDashboard dashboardPanel;
    private final StudentSearchResultPanel searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";

    public StudentManagementPage() {
        this(new StudentManagementDashboard());
    }

    private StudentManagementPage(StudentManagementDashboard dashboardPanel) {
        super(
                "Tim theo ma sinh vien, ho ten, email, lop, khoa",
                null,
                null,
                2,
                dashboardPanel,
                new StudentSearchResultPanel(direction -> {})
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (StudentSearchResultPanel) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        configureCreateAction("Tao moi", this::openCreateStudentDialog);
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword;
        currentPage = 1;
        loadCurrentPage();
    }

    @Override
    protected void onKeywordCleared() {
        currentKeyword = "";
        currentPage = 1;
        dashboardPanel.refreshData();
    }

    private void loadCurrentPage() {
        PagedResult<Student> result = AppContext.getStudentController()
                .searchStudents(currentKeyword, currentPage, PAGE_SIZE);
        searchResultPanel.showResults(result, currentKeyword);
        showResults();
    }

    private void changePage(int direction) {
        int nextPage = currentPage + direction;
        if (nextPage < 1) {
            return;
        }
        currentPage = nextPage;
        loadCurrentPage();
    }

    private void openCreateStudentDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tao moi sinh vien", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Sinh vien"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }
}

```

## src\main\resources\application.properties <a id="application_properties"></a>

```properties
spring.application.name=OnlineUTE

```

## src\main\java\com\bangcompany\onlineute\View\WindowManager.java <a id="WindowManager_java"></a>

### Dependencies

- `javax.swing`
- `java.awt`

```java
package com.bangcompany.onlineute.View;

import javax.swing.*;
import java.awt.*;

public final class WindowManager extends JFrame {
    private static WindowManager instance;
    private static JPanel container;

    /**
     * Khởi tạo cửa sổ window chính
     */
    private WindowManager() {
        setTitle("OnlineUTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //tắt chương trình khi cửa sổ ắt
        setSize(1200, 800);
        setResizable(true); // có thể tùy chỉnh kích thước
        setLocationRelativeTo(null);
      //  setUndecorated(true);   //xóa viền của window
        container = new JPanel(new BorderLayout()); // tạo container làm nội dung
        add(container);
    }

    private static void init() {
        if (instance == null) {
            instance = new WindowManager(); // khởi tạo cửa số khi bắt đầu
        }
    }

    public static void show(JPanel panel) {
        init();
        container.removeAll();  // xóa hết các phần tử bên trong
        container.add(panel, BorderLayout.CENTER);  //thêm 1 panel mới
        container.revalidate(); //kiểm tra lại
        container.repaint();    // vẽ lại

        if (!instance.isVisible()) {    //kiểm tra nếu chưa hiện thì hiện lại cửa sổ
            instance.setVisible(true);
        }
    }

    public static void exit() {// khi thoát
        if (instance != null) {
            instance.dispose();
        }
        System.exit(0);
    }
}

```

## src\main\resources\META-INF\persistence.xml <a id="persistence_xml"></a>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="3.0"
             xsi:schemaLocation="
               https://jakarta.ee/xml/ns/persistence
               https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">

    <persistence-unit name="OnlineUtePU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <class>com.bangcompany.onlineute.Model.Entity.Account</class>
        <class>com.bangcompany.onlineute.Model.Entity.Admin</class>
        <class>com.bangcompany.onlineute.Model.Entity.Student</class>
        <class>com.bangcompany.onlineute.Model.Entity.Lecturer</class>
        <class>com.bangcompany.onlineute.Model.Entity.Class</class>
        <class>com.bangcompany.onlineute.Model.Entity.Course</class>
        <class>com.bangcompany.onlineute.Model.Entity.Term</class>
        <class>com.bangcompany.onlineute.Model.Entity.Faculty</class>
        <class>com.bangcompany.onlineute.Model.Entity.Major</class>
        <class>com.bangcompany.onlineute.Model.Entity.CourseSection</class>
        <class>com.bangcompany.onlineute.Model.Entity.CourseRegistration</class>
        <class>com.bangcompany.onlineute.Model.Entity.Schedule</class>
        <class>com.bangcompany.onlineute.Model.Entity.ExamSchedule</class>
        <class>com.bangcompany.onlineute.Model.Entity.Mark</class>
        <class>com.bangcompany.onlineute.Model.Entity.Announcement</class>
        <class>com.bangcompany.onlineute.Model.Entity.UserProfile</class>
        <class>com.bangcompany.onlineute.Model.Entity.RegistrationBatch</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/online_ute?useSSL=false&amp;serverTimezone=Asia/Ho_Chi_Minh&amp;allowPublicKeyRetrieval=true"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="1234"/>

            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>

```

## src\main\java\com\bangcompany\onlineute\View\navigation\MainNavigator.java <a id="MainNavigator_java"></a>

### Dependencies

- `com.bangcompany.onlineute.Config.SessionManager`
- `com.bangcompany.onlineute.View.WindowManager`
- `com.bangcompany.onlineute.View.features.auth.LoginScreen`
- `com.bangcompany.onlineute.View.features.dashboard.DashboardLayout`

```java
package com.bangcompany.onlineute.View.navigation;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.View.WindowManager;
import com.bangcompany.onlineute.View.features.auth.LoginScreen;
import com.bangcompany.onlineute.View.features.dashboard.DashboardLayout;

public final class MainNavigator {
    private MainNavigator() {}


    public static void checkSession() {
        if(SessionManager.getCurrentAccount()==null) {
            showLogin();
        } else {
            showDashboard();
        }
    }
    public static void showLogin() {
        WindowManager.show(new LoginScreen());
    }

    public static void showDashboard() {
        WindowManager.show(new DashboardLayout());
    }
}
// hàm trung gian để lệnh show nó gọn hơn

```

## src\test\java\com\bangcompany\onlineute\OnlineUteApplicationTests.java <a id="OnlineUteApplicationTests_java"></a>

### Dependencies

- `org.junit.jupiter.api.Test`
- `org.springframework.boot.test.context.SpringBootTest`

```java
package com.bangcompany.onlineute;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineUteApplicationTests {

    @Test
    void contextLoads() {
    }

}

```

## target\classes\application.properties <a id="application_properties"></a>

```properties
spring.application.name=OnlineUTE

```

## target\classes\META-INF\persistence.xml <a id="persistence_xml"></a>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="3.0"
             xsi:schemaLocation="
               https://jakarta.ee/xml/ns/persistence
               https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">

    <persistence-unit name="OnlineUtePU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <class>com.bangcompany.onlineute.Model.Entity.Account</class>
        <class>com.bangcompany.onlineute.Model.Entity.Admin</class>
        <class>com.bangcompany.onlineute.Model.Entity.Student</class>
        <class>com.bangcompany.onlineute.Model.Entity.Lecturer</class>
        <class>com.bangcompany.onlineute.Model.Entity.Class</class>
        <class>com.bangcompany.onlineute.Model.Entity.Course</class>
        <class>com.bangcompany.onlineute.Model.Entity.Term</class>
        <class>com.bangcompany.onlineute.Model.Entity.Faculty</class>
        <class>com.bangcompany.onlineute.Model.Entity.Major</class>
        <class>com.bangcompany.onlineute.Model.Entity.CourseSection</class>
        <class>com.bangcompany.onlineute.Model.Entity.CourseRegistration</class>
        <class>com.bangcompany.onlineute.Model.Entity.Schedule</class>
        <class>com.bangcompany.onlineute.Model.Entity.ExamSchedule</class>
        <class>com.bangcompany.onlineute.Model.Entity.Mark</class>
        <class>com.bangcompany.onlineute.Model.Entity.Announcement</class>
        <class>com.bangcompany.onlineute.Model.Entity.UserProfile</class>
        <class>com.bangcompany.onlineute.Model.Entity.RegistrationBatch</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/online_ute?useSSL=false&amp;serverTimezone=Asia/Ho_Chi_Minh&amp;allowPublicKeyRetrieval=true"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="1234"/>

            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>

```

## Dependency Diagram

Below is a visualization of file dependencies in the codebase:

```mermaid
graph TD
  F1_c:\Users\bangn\Downloads\OnlineUTE\.git_config["config"]
  F2_c:\Users\bangn\Downloads\OnlineUTE\.git_HEAD["HEAD"]
  F3_c:\Users\bangn\Downloads\OnlineUTE\.git_description["description"]
  F4_c:\Users\bangn\Downloads\OnlineUTE\.git_FETCH_HEAD["FETCH_HEAD"]
  F5_c:\Users\bangn\Downloads\OnlineUTE\.git_COMMIT_EDITMSG["COMMIT_EDITMSG"]
  F6_c:\Users\bangn\Downloads\OnlineUTE\.git_index["index"]
  F7_c:\Users\bangn\Downloads\OnlineUTE\.git\info_exclude["exclude"]
  F8_c:\Users\bangn\Downloads\OnlineUTE\.git\logs_HEAD["HEAD"]
  F9_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\00_a63f6d6744aab5acb3a9c130877e08efec4b0d["a63f6d6744aab5acb3a9c130877e08efec4b0d"]
  F10_c:\Users\bangn\Downloads\OnlineUTE\.git\logs\refs\remotes\origin_HEAD["HEAD"]
  F11_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\01_893201d795eab5ae47bdd5511e565a0e1dd6c9["893201d795eab5ae47bdd5511e565a0e1dd6c9"]
  F12_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\01_4010cb4cce60e05a64fe9c8177644eaa531177["4010cb4cce60e05a64fe9c8177644eaa531177"]
  F13_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\00_2ea313db8ae41b16c665d25c2da72757e12bfb["2ea313db8ae41b16c665d25c2da72757e12bfb"]
  F14_c:\Users\bangn\Downloads\OnlineUTE\.git\logs\refs\heads_master["master"]
  F15_c:\Users\bangn\Downloads\OnlineUTE\.git\logs\refs\remotes\origin_master["master"]
  F16_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\01_698a9d0574afc91b12a2c65c5c663b63db7b2c["698a9d0574afc91b12a2c65c5c663b63db7b2c"]
  F17_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_85e7ce5fd27f094237cbcfae13338e11436ce6["85e7ce5fd27f094237cbcfae13338e11436ce6"]
  F18_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_ad299918629ed7100628b035ff75367a7b79ba["ad299918629ed7100628b035ff75367a7b79ba"]
  F19_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\01_8944e674cd0a38faa0329aa2e838aa68502b08["8944e674cd0a38faa0329aa2e838aa68502b08"]
  F20_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_8218edd6960b0b7156ac7100a5ba110df09041["8218edd6960b0b7156ac7100a5ba110df09041"]
  F21_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_62942df01edc9982ac995243808e9528b27e07["62942df01edc9982ac995243808e9528b27e07"]
  F22_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\03_8fae74e5fefb383ded8a2a7a316454c1049276["8fae74e5fefb383ded8a2a7a316454c1049276"]
  F23_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_59641db3764d14eb9def35b1fac0269f6c4ba7["59641db3764d14eb9def35b1fac0269f6c4ba7"]
  F24_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\01_f6eb4dd08aef98b39f1707aaea537bbc89bf36["f6eb4dd08aef98b39f1707aaea537bbc89bf36"]
  F25_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\02_ee9dc94b1aa81ab9d46a694e356b7f469b41f6["ee9dc94b1aa81ab9d46a694e356b7f469b41f6"]
  F26_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\03_45c64c6b043a683df5b14e5dabb66b003d1ca1["45c64c6b043a683df5b14e5dabb66b003d1ca1"]
  F27_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_5ec1f05db6b667a82bac4b3baa79cee2767452["5ec1f05db6b667a82bac4b3baa79cee2767452"]
  F28_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_addfac703a4bb2cbdc1ac893943d542cced852["addfac703a4bb2cbdc1ac893943d542cced852"]
  F29_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_23ed04cbac4be090e7b70bde0e1f1e7f3b6738["23ed04cbac4be090e7b70bde0e1f1e7f3b6738"]
  F30_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\05_9373e95f00c0803555e52269f1aa39ffac9853["9373e95f00c0803555e52269f1aa39ffac9853"]
  F31_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_6203f4b556159b0e405cb3b0b65a1cda3c4797["6203f4b556159b0e405cb3b0b65a1cda3c4797"]
  F32_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_a6a9c3744a60cab122822a986138a7498a4572["a6a9c3744a60cab122822a986138a7498a4572"]
  F33_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\05_d2765f013e63cf5022f51ce89e2b2b1c4a9e58["d2765f013e63cf5022f51ce89e2b2b1c4a9e58"]
  F34_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\04_b90d6c57673f905d231ef626cd81174b3095a9["b90d6c57673f905d231ef626cd81174b3095a9"]
  F35_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\05_b35b7a5621eb52ffee9b3bbcf4650414be46b8["b35b7a5621eb52ffee9b3bbcf4650414be46b8"]
  F36_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\05_aaa7e91c46b7f356e9e45a41967e68b63e4494["aaa7e91c46b7f356e9e45a41967e68b63e4494"]
  F37_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_2a924bc3c6e7a11d0082c9d7f73fad8bc41708["2a924bc3c6e7a11d0082c9d7f73fad8bc41708"]
  F38_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\05_f814970767b33d24bc8b7cb4cdc882d5e26ab7["f814970767b33d24bc8b7cb4cdc882d5e26ab7"]
  F39_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\06_99f24e85f700ddd1594adc5cfe01b0507b92d2["99f24e85f700ddd1594adc5cfe01b0507b92d2"]
  F40_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\06_269fc1fc8856881574ec92329a2dc6141f78fe["269fc1fc8856881574ec92329a2dc6141f78fe"]
  F41_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_327a6abf646b63005b24408aae485174943ae3["327a6abf646b63005b24408aae485174943ae3"]
  F42_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\06_8bc5f9c6a910ba11c2a858d32dc8a21bba5714["8bc5f9c6a910ba11c2a858d32dc8a21bba5714"]
  F43_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_5d4faec60fca7c2e1f4779cb7e643833aff282["5d4faec60fca7c2e1f4779cb7e643833aff282"]
  F44_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_6fb72f623c214b1b6ea341b7f4e06aea5e6c8d["6fb72f623c214b1b6ea341b7f4e06aea5e6c8d"]
  F45_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\06_6ed01470ef015a643daac704ea61244f5c6475["6ed01470ef015a643daac704ea61244f5c6475"]
  F46_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\06_25ac92469b3023a93496634a65eefe3c56c324["25ac92469b3023a93496634a65eefe3c56c324"]
  F47_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_086333334f2bce79f05285239b2208cfe40146["086333334f2bce79f05285239b2208cfe40146"]
  F48_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\08_7a81b09cab31c259be575742d11b733a6b7f9c["7a81b09cab31c259be575742d11b733a6b7f9c"]
  F49_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_4d38910cabeed1dcbd7fb9cdda633cacc9fd92["4d38910cabeed1dcbd7fb9cdda633cacc9fd92"]
  F50_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\08_288f04c197c60a0ea544feec0a124a730a4300["288f04c197c60a0ea544feec0a124a730a4300"]
  F51_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_d0667e224fa493b30519118557b4ee573374e0["d0667e224fa493b30519118557b4ee573374e0"]
  F52_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\07_dec2c251f2d863aeda9a14b37eeba83103ef4b["dec2c251f2d863aeda9a14b37eeba83103ef4b"]
  F53_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\08_c2611a3d103818a6d7fa78a1752541513e14aa["c2611a3d103818a6d7fa78a1752541513e14aa"]
  F54_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\08_480e02009e06533f624d85a056ae6f4ec986ed["480e02009e06533f624d85a056ae6f4ec986ed"]
  F55_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_645f151df1b87a8231df657df165413c1efea1["645f151df1b87a8231df657df165413c1efea1"]
  F56_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\08_a784bec09694e717a7523672a77637e705695f["a784bec09694e717a7523672a77637e705695f"]
  F57_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_351f567c568c2ee5d5bbb35fc69b6dc88b25a2["351f567c568c2ee5d5bbb35fc69b6dc88b25a2"]
  F58_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_cea0e724edd173429ba6ab38e6e0a644cd601c["cea0e724edd173429ba6ab38e6e0a644cd601c"]
  F59_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_142fa2e678472f905b685da06eb96f2ca6db75["142fa2e678472f905b685da06eb96f2ca6db75"]
  F60_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_f54245816cc7fa5610736149b26f84374be13c["f54245816cc7fa5610736149b26f84374be13c"]
  F61_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_83c2b9610a208f6f3395fdc19c60547618b76d["83c2b9610a208f6f3395fdc19c60547618b76d"]
  F62_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_9c41307e881d72945071d24fbb87c8849dbbdd["9c41307e881d72945071d24fbb87c8849dbbdd"]
  F63_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_6e1511013f37e9d6501b62ee8ec0d3873686a6["6e1511013f37e9d6501b62ee8ec0d3873686a6"]
  F64_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_e783fdf82b21bb1073a846b4d411113cc41343["e783fdf82b21bb1073a846b4d411113cc41343"]
  F65_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_ea62eb40b6ce6eee2af47ee5973954e5538f01["ea62eb40b6ce6eee2af47ee5973954e5538f01"]
  F66_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\09_e00701301fed9f3861e1591cd3ac2ea6e62bb4["e00701301fed9f3861e1591cd3ac2ea6e62bb4"]
  F67_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0d_7ba287c0f367ac7220ffdfb44cb3b092bb73d7["7ba287c0f367ac7220ffdfb44cb3b092bb73d7"]
  F68_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0b_6984c544cd6fe494117d5fa1b7870ba92316dc["6984c544cd6fe494117d5fa1b7870ba92316dc"]
  F69_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0a_ef67bfe36249d92e9b719cf7628108e8bb8bd2["ef67bfe36249d92e9b719cf7628108e8bb8bd2"]
  F70_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0c_4ffe68c768363ff77733f2e1b9b556d575ce96["4ffe68c768363ff77733f2e1b9b556d575ce96"]
  F71_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0e_1720e489a28c2581d224c82430b41a2d2653a9["1720e489a28c2581d224c82430b41a2d2653a9"]
  F72_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0c_5af29ec1c930d060b2c3bf4f0555a30955f430["5af29ec1c930d060b2c3bf4f0555a30955f430"]
  F73_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0c_8f93653dd4a435218f2551a80065a855d418c7["8f93653dd4a435218f2551a80065a855d418c7"]
  F74_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0e_30418b338d3e7c757c88ab7a31f3150e2a157b["30418b338d3e7c757c88ab7a31f3150e2a157b"]
  F75_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0e_25eda0691d7943af34768f5449209f82c76b27["25eda0691d7943af34768f5449209f82c76b27"]
  F76_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0e_788b8252509ff4ce9981e65a93ebc56f2e8490["788b8252509ff4ce9981e65a93ebc56f2e8490"]
  F77_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0f_96f7770a25f125a498ed72b6aa363018814616["96f7770a25f125a498ed72b6aa363018814616"]
  F78_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\10_381e27cb0e0811f1db3b308af473a6a42d2f8e["381e27cb0e0811f1db3b308af473a6a42d2f8e"]
  F79_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0e_c597a098a3222469f6664423b035fb166346fc["c597a098a3222469f6664423b035fb166346fc"]
  F80_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\11_b7f084e223a1f39edfface14625c9bf64e79b9["b7f084e223a1f39edfface14625c9bf64e79b9"]
  F81_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\10_8dce745ef78e4effe73381bff5e4a233748447["8dce745ef78e4effe73381bff5e4a233748447"]
  F82_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0f_46d91d1d21d6a7cb570dc5412e8c2e3f683a8e["46d91d1d21d6a7cb570dc5412e8c2e3f683a8e"]
  F83_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0f_dfd3f91b1613574cfff01c959e64b2f45af6ab["dfd3f91b1613574cfff01c959e64b2f45af6ab"]
  F84_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\0f_f1d3138581cc58c4f3976cfc3928f01bc4c4cf["f1d3138581cc58c4f3976cfc3928f01bc4c4cf"]
  F85_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\11_cae28a0a77b91843731356d48c8ec56c301022["cae28a0a77b91843731356d48c8ec56c301022"]
  F86_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\10_9fd0f89048cf054e06c7d0b114fddcdced3c8f["9fd0f89048cf054e06c7d0b114fddcdced3c8f"]
  F87_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_386e043fbca47b849829ef66e5e0eafa8518fc["386e043fbca47b849829ef66e5e0eafa8518fc"]
  F88_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\13_696d4c9bb564e53c6213628ce00a6dcde5592f["696d4c9bb564e53c6213628ce00a6dcde5592f"]
  F89_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_40c358aa1cc095838bcfd386c84e242e324bf8["40c358aa1cc095838bcfd386c84e242e324bf8"]
  F90_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_179767349506ed315b0f5fc75fa6c812aaae19["179767349506ed315b0f5fc75fa6c812aaae19"]
  F91_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_41bb616658b36be4d4eb08e5b5eb09d02ae32c["41bb616658b36be4d4eb08e5b5eb09d02ae32c"]
  F92_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\13_7be7e2e494275f31815dd0478224042fc50816["7be7e2e494275f31815dd0478224042fc50816"]
  F93_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_b69de45f889fb4b88bc37ef6f0bf7f004f16e0["b69de45f889fb4b88bc37ef6f0bf7f004f16e0"]
  F94_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_47e24ae47e298d946a734a5eeb1ea08b076ba4["47e24ae47e298d946a734a5eeb1ea08b076ba4"]
  F95_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\13_badec28f6fe9cc76f17424b4416d6e8bf64678["badec28f6fe9cc76f17424b4416d6e8bf64678"]
  F96_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\12_418a4f2f79f2b01456623d3a9b5b6d637d07cd["418a4f2f79f2b01456623d3a9b5b6d637d07cd"]
  F97_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_6ce238b2c51c7764954833ac00a65a2d346d79["6ce238b2c51c7764954833ac00a65a2d346d79"]
  F98_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\13_f24b672c9858999ca20a65878144896ae1aa22["f24b672c9858999ca20a65878144896ae1aa22"]
  F99_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_25bf81e6fc3aa6125de682b5d20cbcd051305d["25bf81e6fc3aa6125de682b5d20cbcd051305d"]
  F100_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_95a647a85b8a716a4372df0932247d88bdc6aa["95a647a85b8a716a4372df0932247d88bdc6aa"]
  F101_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_1c1d9ee2f05fff13ee81ff4102d7af36997adb["1c1d9ee2f05fff13ee81ff4102d7af36997adb"]
  F102_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\13_d127ddab2b7cdd7dd4f8f653d99e62cba5eea1["d127ddab2b7cdd7dd4f8f653d99e62cba5eea1"]
  F103_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\15_42d3b7bcc25188ed0d895645a4b24a55211b1a["42d3b7bcc25188ed0d895645a4b24a55211b1a"]
  F104_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_21d7aee622e3081da13f07fc6e8be9c31ddf47["21d7aee622e3081da13f07fc6e8be9c31ddf47"]
  F105_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_f84a6bafde1bf17c48fb677a50911243f27778["f84a6bafde1bf17c48fb677a50911243f27778"]
  F106_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\14_5524426f5e937abf56430562788500e450bfb3["5524426f5e937abf56430562788500e450bfb3"]
  F107_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\15_e000f19def69cc548283352cf72e08f2825bc4["e000f19def69cc548283352cf72e08f2825bc4"]
  F108_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_0d957735099a99f5687ff106806df5b74f8df0["0d957735099a99f5687ff106806df5b74f8df0"]
  F109_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_338197decacb75d05429e06884028da560b718["338197decacb75d05429e06884028da560b718"]
  F110_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_62167ccf72b744e3db27027a4d079b15044a8c["62167ccf72b744e3db27027a4d079b15044a8c"]
  F111_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_6a4846bdf600ed47c76979e5c32352a99633ba["6a4846bdf600ed47c76979e5c32352a99633ba"]
  F112_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\15_ca5f2a5530437e18b336838d0001ff59476fd7["ca5f2a5530437e18b336838d0001ff59476fd7"]
  F113_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_f6dcbbdb8d705d9374c6bbc07549547965129a["f6dcbbdb8d705d9374c6bbc07549547965129a"]
  F114_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\15_4a8b95e802135d09a6abf62703ac4019701f90["4a8b95e802135d09a6abf62703ac4019701f90"]
  F115_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\15_c874c77dd1cecf1447bfe1b44a3cf0bc4445a6["c874c77dd1cecf1447bfe1b44a3cf0bc4445a6"]
  F116_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\16_c2311efc5b274d6ab9e8d763640ca423d77f5c["c2311efc5b274d6ab9e8d763640ca423d77f5c"]
  F117_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\17_7e62717c5c4bae65ac7d99061004b23d9f8988["7e62717c5c4bae65ac7d99061004b23d9f8988"]
  F118_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\17_9787d7268bb68cbba194cd93eedfb046a7423e["9787d7268bb68cbba194cd93eedfb046a7423e"]
  F119_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\18_08b0d305cb4ade218d54b85266ff1987f46da7["08b0d305cb4ade218d54b85266ff1987f46da7"]
  F120_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\19_1229144b65370a5b63fe51d298cc789c9c7a8e["1229144b65370a5b63fe51d298cc789c9c7a8e"]
  F121_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\18_746d5e0f20aa3ced3c314e25844239328a739e["746d5e0f20aa3ced3c314e25844239328a739e"]
  F122_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\18_12cb198eaf4cb05f9ff54da96ea734f87dfd55["12cb198eaf4cb05f9ff54da96ea734f87dfd55"]
  F123_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\18_653509cfce8e12477c82b979cb86267a4454b7["653509cfce8e12477c82b979cb86267a4454b7"]
  F124_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\19_4da7117dff890f8f11ac4ccf84ff73a935e41b["4da7117dff890f8f11ac4ccf84ff73a935e41b"]
  F125_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\19_48e18fb1ba4991a72d980c8ec21c2ff1529e2f["48e18fb1ba4991a72d980c8ec21c2ff1529e2f"]
  F126_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\18_5fcb59f1b3cae682f315b7400af2fb22e78537["5fcb59f1b3cae682f315b7400af2fb22e78537"]
  F127_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1a_54755fdb3d9a100b77c744b8065716f0bfa9d9["54755fdb3d9a100b77c744b8065716f0bfa9d9"]
  F128_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1a_8c05d46535485d5118916a29c617f6459e2b56["8c05d46535485d5118916a29c617f6459e2b56"]
  F129_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_43a9a73f6b04320d3bcdec6b14eea2dda12ad5["43a9a73f6b04320d3bcdec6b14eea2dda12ad5"]
  F130_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_19471fe7a9b034b6cf42a7abbe869ca93346aa["19471fe7a9b034b6cf42a7abbe869ca93346aa"]
  F131_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_600a2815dbe5b8ca0e4638538061bb1ca79528["600a2815dbe5b8ca0e4638538061bb1ca79528"]
  F132_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\19_bc48c9b6c97e83b188c7c8ce193d81cc378371["bc48c9b6c97e83b188c7c8ce193d81cc378371"]
  F133_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_2271e781528e15ce16e92a6bd59049dbd2d526["2271e781528e15ce16e92a6bd59049dbd2d526"]
  F134_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1a_64c773f6db93c0c88042bd5aa30567a2f29c8d["64c773f6db93c0c88042bd5aa30567a2f29c8d"]
  F135_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_2eaebb88b990b801ab70c2cdf747c5effe0ec4["2eaebb88b990b801ab70c2cdf747c5effe0ec4"]
  F136_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_23d822cb49edeaae2f944c9fe269e74d987264["23d822cb49edeaae2f944c9fe269e74d987264"]
  F137_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_3f98ab7d66d6921acd679b85f019130d4e2eee["3f98ab7d66d6921acd679b85f019130d4e2eee"]
  F138_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_a75b63f3919b9de926ada78ff7d1957db6d53e["a75b63f3919b9de926ada78ff7d1957db6d53e"]
  F139_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_90c8eec818e84b0dd55847e3b64774afb7a390["90c8eec818e84b0dd55847e3b64774afb7a390"]
  F140_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_8828bb5e306cf0a7add2b4381d2248cdef4206["8828bb5e306cf0a7add2b4381d2248cdef4206"]
  F141_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_79ebf9720baadafbb656382b79255fa5b3ad4e["79ebf9720baadafbb656382b79255fa5b3ad4e"]
  F142_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_ac920137930913ce0004f768b5307a53966044["ac920137930913ce0004f768b5307a53966044"]
  F143_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_d3ddfbbbb0f059f260612ff1d3bffc53f012f8["d3ddfbbbb0f059f260612ff1d3bffc53f012f8"]
  F144_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_6d7f053cb93a946536d41576151a7fee961169["6d7f053cb93a946536d41576151a7fee961169"]
  F145_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_7756ac69af3d21a073a40c21c52057c018bac0["7756ac69af3d21a073a40c21c52057c018bac0"]
  F146_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1b_f78fdc5299e7468aeeb3ad33bc888b08d815b0["f78fdc5299e7468aeeb3ad33bc888b08d815b0"]
  F147_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_d3720d3ae2885194c7ae67129b513855c45ef3["d3720d3ae2885194c7ae67129b513855c45ef3"]
  F148_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1d_53acf0a4d75994ee77860c207c3d2e97843fcd["53acf0a4d75994ee77860c207c3d2e97843fcd"]
  F149_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1d_c507fe08791f700f654a0a323b72bed24b5130["c507fe08791f700f654a0a323b72bed24b5130"]
  F150_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1e_d8cf7914a65fbd8065b2d60de202ad420d218e["d8cf7914a65fbd8065b2d60de202ad420d218e"]
  F151_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1e_e59f76d62463c8cdbaa80ba9b7d1b5bb1e5db1["e59f76d62463c8cdbaa80ba9b7d1b5bb1e5db1"]
  F152_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1e_0d7df545ad40598898cf0855b1ef63f116bc3c["0d7df545ad40598898cf0855b1ef63f116bc3c"]
  F153_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1e_1f5c28ee871881f4c8bebb1ffdb640aba0d71e["1f5c28ee871881f4c8bebb1ffdb640aba0d71e"]
  F154_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1c_d190ce2520b4ef999dd2921b9f13ffaeab21ae["d190ce2520b4ef999dd2921b9f13ffaeab21ae"]
  F155_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1d_b248303e9565dfe7db373c763eff2cce0b8920["b248303e9565dfe7db373c763eff2cce0b8920"]
  F156_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1e_986a8611b245a4d338c92f15979060b2755da1["986a8611b245a4d338c92f15979060b2755da1"]
  F157_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_1c4da502a772e311a738f31cd94cca593114da["1c4da502a772e311a738f31cd94cca593114da"]
  F158_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_bc00483e5c9adb6434fb660b4edbc216c376fe["bc00483e5c9adb6434fb660b4edbc216c376fe"]
  F159_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_216bbb2feb90069aded8aef41666f6ebb55752["216bbb2feb90069aded8aef41666f6ebb55752"]
  F160_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_1d45cfca9f914d669da1017f438da2eb7e526d["1d45cfca9f914d669da1017f438da2eb7e526d"]
  F161_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_1da311e1b9014ae26bf9c1959ec55f6f517d6d["1da311e1b9014ae26bf9c1959ec55f6f517d6d"]
  F162_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\20_2837ae8015ca9ce284de94a626665996b89ac5["2837ae8015ca9ce284de94a626665996b89ac5"]
  F163_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\20_7aaf1bd220c4f60c25f696e3ffbc88ed24432f["7aaf1bd220c4f60c25f696e3ffbc88ed24432f"]
  F164_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\20_44218b4a54efb13806f69416634b4c31cfa1fb["44218b4a54efb13806f69416634b4c31cfa1fb"]
  F165_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\1f_3b3a002167b772bf048bf18a26b66d92ef56cd["3b3a002167b772bf048bf18a26b66d92ef56cd"]
  F166_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\20_6b0a8afc8417b1d415687d74e60f8673207429["6b0a8afc8417b1d415687d74e60f8673207429"]
  F167_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\21_c749af5040700a7d1aa7f76eec9089e69edde1["c749af5040700a7d1aa7f76eec9089e69edde1"]
  F168_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\20_7b15545b107ee62402164dc2922f9440644fd8["7b15545b107ee62402164dc2922f9440644fd8"]
  F169_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\21_e0ee57f7337a7537edb73e7d5fbdb99149c831["e0ee57f7337a7537edb73e7d5fbdb99149c831"]
  F170_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_17bfa5f053d027fe98e2910101f3472a844f75["17bfa5f053d027fe98e2910101f3472a844f75"]
  F171_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\22_864620de99e77a3e3f360fc5ca81824404bb8d["864620de99e77a3e3f360fc5ca81824404bb8d"]
  F172_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\21_ef3035db3b6acbeb049c533a22528158ef505d["ef3035db3b6acbeb049c533a22528158ef505d"]
  F173_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\21_c81a0adc2e3fa7d1c85af59e0863d77f4eaa56["c81a0adc2e3fa7d1c85af59e0863d77f4eaa56"]
  F174_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_243933a4575d7815eb16c96c800fd15c1ebc03["243933a4575d7815eb16c96c800fd15c1ebc03"]
  F175_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\21_19ebb587bf07da43a054558604a3bafb1c27f9["19ebb587bf07da43a054558604a3bafb1c27f9"]
  F176_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\22_556e8d60adfa0f78a827dc9b7911cf18b7532c["556e8d60adfa0f78a827dc9b7911cf18b7532c"]
  F177_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_a0c621e9fe55aa3182f1fc0a776bf2de7af766["a0c621e9fe55aa3182f1fc0a776bf2de7af766"]
  F178_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_7558e6597338013546a88b7d156158e7817b6f["7558e6597338013546a88b7d156158e7817b6f"]
  F179_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_e66dbe6004f4d75debd92258208e11dcdfa46f["e66dbe6004f4d75debd92258208e11dcdfa46f"]
  F180_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_e83da4469c5055d056e360cc366052df6e7198["e83da4469c5055d056e360cc366052df6e7198"]
  F181_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_3b91049b03ceb6aad77c65a99d71524aad2d4c["3b91049b03ceb6aad77c65a99d71524aad2d4c"]
  F182_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_5a475130b0a4895df47ad3cf921f272822d54c["5a475130b0a4895df47ad3cf921f272822d54c"]
  F183_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_2ae22c64d02dab56a44a9ab4bb2c709bf62ae3["2ae22c64d02dab56a44a9ab4bb2c709bf62ae3"]
  F184_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\23_8dce48ab99398d2fbe0a32596b0d4dfacb150b["8dce48ab99398d2fbe0a32596b0d4dfacb150b"]
  F185_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_18e05d43fe736de0142250d2958bb690476d83["18e05d43fe736de0142250d2958bb690476d83"]
  F186_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_7ddebeaee3a29b2b816129ef595d3c7898bf60["7ddebeaee3a29b2b816129ef595d3c7898bf60"]
  F187_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_89b4f7987885c6e3b2cd3ab4ae7921de7a1d69["89b4f7987885c6e3b2cd3ab4ae7921de7a1d69"]
  F188_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_ac342bf334bc1384cb4c9dbc20dcef45e094fc["ac342bf334bc1384cb4c9dbc20dcef45e094fc"]
  F189_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\26_099b99f38f688a243c8524cd2b530dd7341c58["099b99f38f688a243c8524cd2b530dd7341c58"]
  F190_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_ceee45849e8e2a98830e6b3cf5ce4035e7ed14["ceee45849e8e2a98830e6b3cf5ce4035e7ed14"]
  F191_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\24_e3331215861130493175e03b242b5462994ae8["e3331215861130493175e03b242b5462994ae8"]
  F192_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\26_a1e63357d55f03f21092c4256b61b6f4dfd7a3["a1e63357d55f03f21092c4256b61b6f4dfd7a3"]
  F193_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\27_2a764a9681583125cec903fb47e254229a5796["2a764a9681583125cec903fb47e254229a5796"]
  F194_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\27_3db2866ddbee5cbb1c571a0049254d9987e439["3db2866ddbee5cbb1c571a0049254d9987e439"]
  F195_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\25_5ab715c840619a9775c384f53aaa202470b9a6["5ab715c840619a9775c384f53aaa202470b9a6"]
  F196_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\26_98f7f86f7867536dc63d973eb466b5c5ddb3b4["98f7f86f7867536dc63d973eb466b5c5ddb3b4"]
  F197_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_626dd6861f2dcd5e83c9fe54d78d4f14d181af["626dd6861f2dcd5e83c9fe54d78d4f14d181af"]
  F198_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_e3f1bdfa7d3a5f5e976f810544df9645bc89cb["e3f1bdfa7d3a5f5e976f810544df9645bc89cb"]
  F199_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_a791e6de82d4d3492f9f59661976dfd5d2ee12["a791e6de82d4d3492f9f59661976dfd5d2ee12"]
  F200_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\27_6984c828deb7d9e353a7da9da0f95c48bcfa6f["6984c828deb7d9e353a7da9da0f95c48bcfa6f"]
  F201_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_d8dc1a01ddcc7f3f3a980d02fd11a9f0d24e00["d8dc1a01ddcc7f3f3a980d02fd11a9f0d24e00"]
  F202_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_38e6c54faff8502ddd77acb04a5874a7c8fa8a["38e6c54faff8502ddd77acb04a5874a7c8fa8a"]
  F203_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_397e551fed1e75c348a076b26dae2f44872aed["397e551fed1e75c348a076b26dae2f44872aed"]
  F204_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_73091f07529f287e6d0ac5331e2a23e7a062d8["73091f07529f287e6d0ac5331e2a23e7a062d8"]
  F205_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_e1af4db7a130a59b287618c6d18856bcda4228["e1af4db7a130a59b287618c6d18856bcda4228"]
  F206_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\28_914a755f5e994ea58cabc6192e704cfc30ec16["914a755f5e994ea58cabc6192e704cfc30ec16"]
  F207_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_f4655bcbed5693f49489de2b202407746be772["f4655bcbed5693f49489de2b202407746be772"]
  F208_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_f815a45e4cd6f6d24e78e38752ef3fc3843be1["f815a45e4cd6f6d24e78e38752ef3fc3843be1"]
  F209_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2a_2fcc9f20e364efd1f4e3edac93dda881ff9dc1["2fcc9f20e364efd1f4e3edac93dda881ff9dc1"]
  F210_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2a_5f467b4575d321874c71e302bb80ca5e8b9045["5f467b4575d321874c71e302bb80ca5e8b9045"]
  F211_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_4b7a91e9137123255a5d758166af84b2033758["4b7a91e9137123255a5d758166af84b2033758"]
  F212_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\29_df79ce88aa8ac98ef7445a5b56ef843531ca47["df79ce88aa8ac98ef7445a5b56ef843531ca47"]
  F213_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_06d6ea9e5a8417e03eb56c5c3f4148254121a5["06d6ea9e5a8417e03eb56c5c3f4148254121a5"]
  F214_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_51b8841f4f1d181fda58dc33dffcf3dfd649a2["51b8841f4f1d181fda58dc33dffcf3dfd649a2"]
  F215_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_4eda5dfd51cbce8f724133ec52ee87cec1c6cd["4eda5dfd51cbce8f724133ec52ee87cec1c6cd"]
  F216_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2a_75b1e87445b5315620a32612d07fa31213ba07["75b1e87445b5315620a32612d07fa31213ba07"]
  F217_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_9601a85bfc95802d7c4abf1c265cf680158d8e["9601a85bfc95802d7c4abf1c265cf680158d8e"]
  F218_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_52b2c0807271f1e3168556a90766139060d1b5["52b2c0807271f1e3168556a90766139060d1b5"]
  F219_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_c7c5123a9141faaaeddb9659ca7bd27abc0722["c7c5123a9141faaaeddb9659ca7bd27abc0722"]
  F220_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_f9faf7cc032f42b1628531af3710d6fdf69ab8["f9faf7cc032f42b1628531af3710d6fdf69ab8"]
  F221_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_66abda621a928fc50adbb890c3ac69732185ab["66abda621a928fc50adbb890c3ac69732185ab"]
  F222_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_7d299c70b7ea4b4d2c2d3fe4f110daefc3c55b["7d299c70b7ea4b4d2c2d3fe4f110daefc3c55b"]
  F223_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_7ee24b65789a8c2ad6a0e2e577d2d62a7a212f["7ee24b65789a8c2ad6a0e2e577d2d62a7a212f"]
  F224_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2b_ead3ca3d21e23284fc0236f99a39124395960f["ead3ca3d21e23284fc0236f99a39124395960f"]
  F225_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_31f96135204d75585b148f412dbf4cb6ab8f7c["31f96135204d75585b148f412dbf4cb6ab8f7c"]
  F226_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_2ecb7299f4458b0a1e338ee3211e0b8978e5a8["2ecb7299f4458b0a1e338ee3211e0b8978e5a8"]
  F227_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_bdf96b81ddcf488c3e388291879ef7f589c404["bdf96b81ddcf488c3e388291879ef7f589c404"]
  F228_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_ccf3f3157f15da1bb572cab1c96e4b03902a98["ccf3f3157f15da1bb572cab1c96e4b03902a98"]
  F229_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_61af912c96b150b50ef62312c550732189b455["61af912c96b150b50ef62312c550732189b455"]
  F230_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_550554750ff9d475a88ec9f87c64e609f41451["550554750ff9d475a88ec9f87c64e609f41451"]
  F231_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_2aa5b3f612806908a57419d02cf4bb3c5bea6c["2aa5b3f612806908a57419d02cf4bb3c5bea6c"]
  F232_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_c661e9cab572edcab64807c31582ed4d6425f6["c661e9cab572edcab64807c31582ed4d6425f6"]
  F233_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_2129fc5c4e70ea592e33f839e965fd20e709fe["2129fc5c4e70ea592e33f839e965fd20e709fe"]
  F234_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2c_90d7e1179a0028eff53ac15adcbc9d44a0d6dd["90d7e1179a0028eff53ac15adcbc9d44a0d6dd"]
  F235_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_bdda10c998e8f57c01b002519d37c0d7ddb0fd["bdda10c998e8f57c01b002519d37c0d7ddb0fd"]
  F236_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_56f5ce0a7993ce08545f2a7e24ad812465d702["56f5ce0a7993ce08545f2a7e24ad812465d702"]
  F237_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_cd69fbb809532a92dcd12d1cf9119cd90655de["cd69fbb809532a92dcd12d1cf9119cd90655de"]
  F238_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2e_02830b8537b9b1e19e4bfc77f573f6a1f897b0["02830b8537b9b1e19e4bfc77f573f6a1f897b0"]
  F239_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2f_1dae3572e5356dfcc93f80f72a295af8e561b1["1dae3572e5356dfcc93f80f72a295af8e561b1"]
  F240_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2d_c2a29fd321607b007e1a664ef9ed2a102679c6["c2a29fd321607b007e1a664ef9ed2a102679c6"]
  F241_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2e_8361094b89243af69da15570cd61078c1b6691["8361094b89243af69da15570cd61078c1b6691"]
  F242_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2e_f3604b6ccd7e790ffbc3c2f2199a1e94c6572c["f3604b6ccd7e790ffbc3c2f2199a1e94c6572c"]
  F243_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2f_90c5533ecd931e28421e919cb1d664f8f4cb0c["90c5533ecd931e28421e919cb1d664f8f4cb0c"]
  F244_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2f_d8fe439a80f132facba6412ff0ce6933608e3c["d8fe439a80f132facba6412ff0ce6933608e3c"]
  F245_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2f_4d6c34f754ebb04006a759eee91969897b1bd1["4d6c34f754ebb04006a759eee91969897b1bd1"]
  F246_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\2e_4272fc86e9841ee3c7ade6e718a6e1d6437dcd["4272fc86e9841ee3c7ade6e718a6e1d6437dcd"]
  F247_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_18fae3f8aaf403c225009b930c0a6ea68ae065["18fae3f8aaf403c225009b930c0a6ea68ae065"]
  F248_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_0cdfc2374eb53afa46ed821ae0857829933119["0cdfc2374eb53afa46ed821ae0857829933119"]
  F249_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_1002a13bb431ac0cc76dd74f6054fd8acd9924["1002a13bb431ac0cc76dd74f6054fd8acd9924"]
  F250_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_088196d2850b3d6b0a81c0fd46e9308d282694["088196d2850b3d6b0a81c0fd46e9308d282694"]
  F251_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_16726f482fd5f7439a6a319c9f9d228d5fcf12["16726f482fd5f7439a6a319c9f9d228d5fcf12"]
  F252_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_4acc001359b5df73745836cf9d6641b05db051["4acc001359b5df73745836cf9d6641b05db051"]
  F253_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_2b632a7461f288f8516b8dcfb70fd19c0be013["2b632a7461f288f8516b8dcfb70fd19c0be013"]
  F254_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_535782f60b899aafbe75f5843d69e319a5485d["535782f60b899aafbe75f5843d69e319a5485d"]
  F255_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_4812684dec405778d405ce85f7593b51c5b9b7["4812684dec405778d405ce85f7593b51c5b9b7"]
  F256_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_34667053f0aa726c921cbc911f49db5b51f06c["34667053f0aa726c921cbc911f49db5b51f06c"]
  F257_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_b2471ebe7576d1dec53df175434afed8373064["b2471ebe7576d1dec53df175434afed8373064"]
  F258_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_ee971677256903e65ce1bd96e9c722b712d3a8["ee971677256903e65ce1bd96e9c722b712d3a8"]
  F259_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_3ae95b76d06ae272085afaebfecc2d0200e6d8["3ae95b76d06ae272085afaebfecc2d0200e6d8"]
  F260_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_4445720811a3dbd10c3a53fa948ffa887f7d5d["4445720811a3dbd10c3a53fa948ffa887f7d5d"]
  F261_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_459320fb3f01b29787b018002dfbb2f4c53664["459320fb3f01b29787b018002dfbb2f4c53664"]
  F262_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_0c7b36e377c2a662ba20213802897802d2a507["0c7b36e377c2a662ba20213802897802d2a507"]
  F263_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_9131b8cb08052f33f51149439cdbfe58b14e85["9131b8cb08052f33f51149439cdbfe58b14e85"]
  F264_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_b7a1d93bc29da0226af4ea8c5d9f78d23ff70b["b7a1d93bc29da0226af4ea8c5d9f78d23ff70b"]
  F265_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\30_e4f5a52d1c5385ba91b7da8b495fa19cfe0b25["e4f5a52d1c5385ba91b7da8b495fa19cfe0b25"]
  F266_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_2a7c23e3502f1d930864080d03058228bf0811["2a7c23e3502f1d930864080d03058228bf0811"]
  F267_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_cf81de407a57d86147adfda378c21facb96556["cf81de407a57d86147adfda378c21facb96556"]
  F268_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\31_c4f7c23ee9536ead96367d35ecfed5dfa61be2["c4f7c23ee9536ead96367d35ecfed5dfa61be2"]
  F269_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\33_0a452f3c8eaa8e8d676e0a8e156f343956a5ad["0a452f3c8eaa8e8d676e0a8e156f343956a5ad"]
  F270_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\32_f5fc1b2beb39de1757781d36f90f433021f0e1["f5fc1b2beb39de1757781d36f90f433021f0e1"]
  F271_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\33_6855ac5b99ce2bb4b99cac33f85a0176f1f519["6855ac5b99ce2bb4b99cac33f85a0176f1f519"]
  F272_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\32_279a4286106274a75912df9c63bdff5206ccb2["279a4286106274a75912df9c63bdff5206ccb2"]
  F273_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\33_2b45f6c855addbf91e6ee98b0c4b9f277f68db["2b45f6c855addbf91e6ee98b0c4b9f277f68db"]
  F274_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\32_d6cdb9eb0d70c15c2310cf3cb0f08471206d38["d6cdb9eb0d70c15c2310cf3cb0f08471206d38"]
  F275_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\32_2c9320f1a3ea5541195ed048df70bf47540b4e["2c9320f1a3ea5541195ed048df70bf47540b4e"]
  F276_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\32_27700e259c2eeb43b55d33fc0b582dfa83bfeb["27700e259c2eeb43b55d33fc0b582dfa83bfeb"]
  F277_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\34_292d5c6ac2857abd042acaf0e27f7427e007a6["292d5c6ac2857abd042acaf0e27f7427e007a6"]
  F278_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\33_baba570cda9d707aff84ca6afbe13bd8d490fd["baba570cda9d707aff84ca6afbe13bd8d490fd"]
  F279_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\33_db2ee99095881ccbb70ded6db06bcfa03b1945["db2ee99095881ccbb70ded6db06bcfa03b1945"]
  F280_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\34_b5e1a177a0eda44611cba39fd1b16e98fbb608["b5e1a177a0eda44611cba39fd1b16e98fbb608"]
  F281_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\34_ec8f611942a00baca42b7e116a4aba95c1aaed["ec8f611942a00baca42b7e116a4aba95c1aaed"]
  F282_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\34_eea6b734b84cf4e7c0efd4266e422b3168f0b9["eea6b734b84cf4e7c0efd4266e422b3168f0b9"]
  F283_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_3707c3b6fa813239c14a3cf244723931056153["3707c3b6fa813239c14a3cf244723931056153"]
  F284_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_3c16f147d91bd984f288430cdc22da29234ea8["3c16f147d91bd984f288430cdc22da29234ea8"]
  F285_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_0b040d9f3fb2728d5fe3a4049afc30e61f60d6["0b040d9f3fb2728d5fe3a4049afc30e61f60d6"]
  F286_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\34_d99b545237e4ba2cbb070f20d2f911366e1c9d["d99b545237e4ba2cbb070f20d2f911366e1c9d"]
  F287_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_9011e270f87e1f9c95e4e2faa4476cad36abf0["9011e270f87e1f9c95e4e2faa4476cad36abf0"]
  F288_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\36_17d106607b440639500aedb939130eb223d45f["17d106607b440639500aedb939130eb223d45f"]
  F289_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_6516622afbe4dc99ab59d7193905a20dda69c1["6516622afbe4dc99ab59d7193905a20dda69c1"]
  F290_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\36_e3fe21e5920911e34415d433d959f37771c497["e3fe21e5920911e34415d433d959f37771c497"]
  F291_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\37_7cab8a13e22669c80cc90d60ee8b6c81082125["7cab8a13e22669c80cc90d60ee8b6c81082125"]
  F292_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\37_6a2dcb8558e60489810b19848b24c940554f15["6a2dcb8558e60489810b19848b24c940554f15"]
  F293_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\35_41e28c5ba2ca58803fe9ec333a6ad16cd07a16["41e28c5ba2ca58803fe9ec333a6ad16cd07a16"]
  F294_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\36_c744114c580d886c130023e00f2caf747a6d07["c744114c580d886c130023e00f2caf747a6d07"]
  F295_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\36_70c95f1c118e66afac0a2e9b1ce833e47a83d4["70c95f1c118e66afac0a2e9b1ce833e47a83d4"]
  F296_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\36_f992a5cb51fd67f5fd9fd5db54f75351327c1c["f992a5cb51fd67f5fd9fd5db54f75351327c1c"]
  F297_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\37_bfbf5e62e22f9e898fc78b5b85482da1bdf949["bfbf5e62e22f9e898fc78b5b85482da1bdf949"]
  F298_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\37_bf5db85b6fa7b0e134bed66c2b68115f2b3aff["bf5db85b6fa7b0e134bed66c2b68115f2b3aff"]
  F299_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\38_8384d382c60cbb018e95beadf725a0f206640d["8384d382c60cbb018e95beadf725a0f206640d"]
  F300_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\37_820678e0f84e7e6645675489b4226ebb520ad6["820678e0f84e7e6645675489b4226ebb520ad6"]
  F301_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\39_01738ee84c790fdf30818139fd267d6c3761dc["01738ee84c790fdf30818139fd267d6c3761dc"]
  F302_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_08cad2b0cd42cce1c862a38c22bf35d69bdbea["08cad2b0cd42cce1c862a38c22bf35d69bdbea"]
  F303_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\38_9b25f47a279fa43f6f7ce46276e0e4bab81f60["9b25f47a279fa43f6f7ce46276e0e4bab81f60"]
  F304_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\39_480752a82dc952b7f74d2ecadf61b95d859b09["480752a82dc952b7f74d2ecadf61b95d859b09"]
  F305_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\39_c461613399846babecad67bee00fe123c37ee1["c461613399846babecad67bee00fe123c37ee1"]
  F306_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\38_ac0c456d432eeaffd8c07bdd151109c3fadae0["ac0c456d432eeaffd8c07bdd151109c3fadae0"]
  F307_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_2d15a71fc8800e383bfc6835228848cbf4f460["2d15a71fc8800e383bfc6835228848cbf4f460"]
  F308_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_411843e6adfa601ac1b1bbca327b7841b890e3["411843e6adfa601ac1b1bbca327b7841b890e3"]
  F309_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_654c4940be4443d13f0070af1127e170ff4d49["654c4940be4443d13f0070af1127e170ff4d49"]
  F310_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_62857bf349f33514bd3ce2a04ef8b36c6cbace["62857bf349f33514bd3ce2a04ef8b36c6cbace"]
  F311_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_1b1cd37d6cf76cab506b3635ffa94a2eebca1b["1b1cd37d6cf76cab506b3635ffa94a2eebca1b"]
  F312_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_5940ed99567d24c6f0fe31c345d04b5a177ef3["5940ed99567d24c6f0fe31c345d04b5a177ef3"]
  F313_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_da3b579ff9053d8f1948f11415b9db72e20025["da3b579ff9053d8f1948f11415b9db72e20025"]
  F314_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_53a59a1fd51d66a686576653fc59b23bee4db9["53a59a1fd51d66a686576653fc59b23bee4db9"]
  F315_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_287c5c91e7ec369775a6b4fd995a5ce89f2566["287c5c91e7ec369775a6b4fd995a5ce89f2566"]
  F316_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3a_5e8ba9eeb7fea08e8d72034c7f2658ffb1a27e["5e8ba9eeb7fea08e8d72034c7f2658ffb1a27e"]
  F317_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3c_6b344494c74cdd1268c17104383dcbd1122d81["6b344494c74cdd1268c17104383dcbd1122d81"]
  F318_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_6372da057d01d45fa3c3c8153be6f3f8dc2f73["6372da057d01d45fa3c3c8153be6f3f8dc2f73"]
  F319_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3c_5e526ade4958bd550aab6b90ec1553f320c144["5e526ade4958bd550aab6b90ec1553f320c144"]
  F320_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_f039efbc0a323f31d704faab880defdc8f273b["f039efbc0a323f31d704faab880defdc8f273b"]
  F321_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3d_56d468943561a31468b2036258c9b35cbdd389["56d468943561a31468b2036258c9b35cbdd389"]
  F322_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_41682ac579fafb665abb4dfcdaa6aaaa712184["41682ac579fafb665abb4dfcdaa6aaaa712184"]
  F323_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3d_f74fc9068e0605fca9ee9b19e23c83b1122b55["f74fc9068e0605fca9ee9b19e23c83b1122b55"]
  F324_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3b_8bd0725bff2d8d7f46825d06cbe4110eb39f70["8bd0725bff2d8d7f46825d06cbe4110eb39f70"]
  F325_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3d_7712dba7eab72637970a32284eba1b7cf73429["7712dba7eab72637970a32284eba1b7cf73429"]
  F326_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3c_b19012a5b6e8ff04e22d97d94fe41226a73bc8["b19012a5b6e8ff04e22d97d94fe41226a73bc8"]
  F327_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3e_df8fb16a458b7ef39b84fe37f13838007c4c66["df8fb16a458b7ef39b84fe37f13838007c4c66"]
  F328_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3e_2cb0e4d340f2ae9a765a948efaff2165c81424["2cb0e4d340f2ae9a765a948efaff2165c81424"]
  F329_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3e_659025c1896e481e8f30abe95f144f33a8f5e3["659025c1896e481e8f30abe95f144f33a8f5e3"]
  F330_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3f_b1227f5c608ecd025691e091cc16c13a64ebbd["b1227f5c608ecd025691e091cc16c13a64ebbd"]
  F331_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\40_cc8ae606463257fd9e05a5458ad7aaaefd33c5["cc8ae606463257fd9e05a5458ad7aaaefd33c5"]
  F332_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\40_877a00bec7c67b03ad182a875b0f4ebd7a871b["877a00bec7c67b03ad182a875b0f4ebd7a871b"]
  F333_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3f_75867801833e204006d482cef12487457f8f56["75867801833e204006d482cef12487457f8f56"]
  F334_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3f_2cccd73a122b7129f6d1bb9d6fca0b4c1bbb0c["2cccd73a122b7129f6d1bb9d6fca0b4c1bbb0c"]
  F335_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\40_1f07b75974dbe18846cbc8f3f67350e716d841["1f07b75974dbe18846cbc8f3f67350e716d841"]
  F336_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\3f_ad5f1633573afcda2a9bc8cf538f14596b88bb["ad5f1633573afcda2a9bc8cf538f14596b88bb"]
  F337_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\40_f21cc46e10ed3191a493acda1552ffe0ce9097["f21cc46e10ed3191a493acda1552ffe0ce9097"]
  F338_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\40_ef3cd5325485885150653471c31153b3834d58["ef3cd5325485885150653471c31153b3834d58"]
  F339_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_a5e6b30543839e7d3c89160c501bca6120aa82["a5e6b30543839e7d3c89160c501bca6120aa82"]
  F340_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_b3331ecc3c8778a79b101a01d46283430d8477["b3331ecc3c8778a79b101a01d46283430d8477"]
  F341_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_fa4c77b4a809c72e80bdeeb124c9ff40bc2668["fa4c77b4a809c72e80bdeeb124c9ff40bc2668"]
  F342_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_82cf4758b8e3becd3d89a31c9a574d3ece25f9["82cf4758b8e3becd3d89a31c9a574d3ece25f9"]
  F343_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_e4c10ecf8c8280daa01e65aa1923dbc93a96c5["e4c10ecf8c8280daa01e65aa1923dbc93a96c5"]
  F344_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_70c36d8fb56c8aebe2d6a0137d6de6813b1137["70c36d8fb56c8aebe2d6a0137d6de6813b1137"]
  F345_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_da1a31cf937e0d005195f7387bc0161066ef6c["da1a31cf937e0d005195f7387bc0161066ef6c"]
  F346_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_b4600f6493bbdc1226c05c8124cce74b4a3de6["b4600f6493bbdc1226c05c8124cce74b4a3de6"]
  F347_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\43_5a8e6b21894d7e0322269d8c746c7c3b10e931["5a8e6b21894d7e0322269d8c746c7c3b10e931"]
  F348_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\42_596d67eb385056a991006390c1c69c9983fa19["596d67eb385056a991006390c1c69c9983fa19"]
  F349_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\42_bf5d64e2c172c37e9fcf5f4a7fda3ba3e9660f["bf5d64e2c172c37e9fcf5f4a7fda3ba3e9660f"]
  F350_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\43_b6b17a9ea7eec68a5cfaacaa84ef00ad247ea0["b6b17a9ea7eec68a5cfaacaa84ef00ad247ea0"]
  F351_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\44_59c0a61a79aedd14f580074562269ce47e3951["59c0a61a79aedd14f580074562269ce47e3951"]
  F352_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\42_5f8ba17194d9a7cd21fcc036d398d4a80410c8["5f8ba17194d9a7cd21fcc036d398d4a80410c8"]
  F353_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\43_e441168d6f94726d2bc99079d2b8aadce4647b["e441168d6f94726d2bc99079d2b8aadce4647b"]
  F354_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\43_c95f6d308f222d8e101e076fc88a49d2015ad5["c95f6d308f222d8e101e076fc88a49d2015ad5"]
  F355_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\43_5d7747dd97a169d6a32061957310ce7f4d8ca1["5d7747dd97a169d6a32061957310ce7f4d8ca1"]
  F356_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\41_fc716f54fd8a978067849dee9fd6174d4de6fc["fc716f54fd8a978067849dee9fd6174d4de6fc"]
  F357_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\44_cffe1563351e63dce6ea6728e0f4d0f670a1c4["cffe1563351e63dce6ea6728e0f4d0f670a1c4"]
  F358_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\44_89806bd9ac03c54c5e2bf5a419ebb77195fdc1["89806bd9ac03c54c5e2bf5a419ebb77195fdc1"]
  F359_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\44_eea52e89a70cffb147d01f97a0b22279152f3b["eea52e89a70cffb147d01f97a0b22279152f3b"]
  F360_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_6ea0fdf4f2b820a179b83f9e14ca4aba6e159b["6ea0fdf4f2b820a179b83f9e14ca4aba6e159b"]
  F361_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_6fc0a6f7576608066342e76a69e42205399619["6fc0a6f7576608066342e76a69e42205399619"]
  F362_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_a6758e20480c5a07b626c1d32cf67028e69758["a6758e20480c5a07b626c1d32cf67028e69758"]
  F363_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_a7443f9a91ed7dcf392b1541de7940a1a974b6["a7443f9a91ed7dcf392b1541de7940a1a974b6"]
  F364_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_c4b9ab378a63524eb81d1221239983dbe3279a["c4b9ab378a63524eb81d1221239983dbe3279a"]
  F365_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_1fd3576d57329b9a9a672f5b2151f7b72529e3["1fd3576d57329b9a9a672f5b2151f7b72529e3"]
  F366_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\45_90c9752a3aa02de34cfe4bc24782d95bc77a6f["90c9752a3aa02de34cfe4bc24782d95bc77a6f"]
  F367_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_278bf70bf30d4e48d7842757a90ba5e3e2cb9e["278bf70bf30d4e48d7842757a90ba5e3e2cb9e"]
  F368_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_1534572cafcde10aa3312605c0288c906a12dc["1534572cafcde10aa3312605c0288c906a12dc"]
  F369_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_251f7ccc2b66ea097ca7d6927a8c88b5ade083["251f7ccc2b66ea097ca7d6927a8c88b5ade083"]
  F370_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_10835662ebbb11329191f04a42b6b03cdb83a4["10835662ebbb11329191f04a42b6b03cdb83a4"]
  F371_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_f99db29ff9f1528e8cdd6de118379ca92c0a32["f99db29ff9f1528e8cdd6de118379ca92c0a32"]
  F372_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\47_11313eed9ef402aa1faa3e53c556e83127d834["11313eed9ef402aa1faa3e53c556e83127d834"]
  F373_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_26f3908d5e59790f28d2f7e4b2ba73b11f9e3c["26f3908d5e59790f28d2f7e4b2ba73b11f9e3c"]
  F374_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_65d6a4383fc12c9d0e998ac35695e0f050c9ad["65d6a4383fc12c9d0e998ac35695e0f050c9ad"]
  F375_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_203f2f7483b46db9d3549bd3f50ef7ed22da6b["203f2f7483b46db9d3549bd3f50ef7ed22da6b"]
  F376_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\46_7b872e73f0ad8dc8cc1a95ab2127fce47a867a["7b872e73f0ad8dc8cc1a95ab2127fce47a867a"]
  F377_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\47_37c9006147c9830abcca7dc76b37f063e6d991["37c9006147c9830abcca7dc76b37f063e6d991"]
  F378_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\47_81e40870d4b03489a73b9b5a580493b0ca5506["81e40870d4b03489a73b9b5a580493b0ca5506"]
  F379_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\49_18ed466144698031671bdc7553e970c1806b05["18ed466144698031671bdc7553e970c1806b05"]
  F380_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\47_a7af009691384e66fbcae6616f6053969a9d74["a7af009691384e66fbcae6616f6053969a9d74"]
  F381_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\49_cac0d1059b30ad1c9a524867c991b23a8bb523["cac0d1059b30ad1c9a524867c991b23a8bb523"]
  F382_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\48_359b665eadde07d32baf9a642c42b1ef531d81["359b665eadde07d32baf9a642c42b1ef531d81"]
  F383_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\48_b343695c8eec9785fecb5e0167c7a5083bec41["b343695c8eec9785fecb5e0167c7a5083bec41"]
  F384_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4a_0fb2e9b4f07a5bf6eff396181f304d9b6e60f8["0fb2e9b4f07a5bf6eff396181f304d9b6e60f8"]
  F385_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\48_fe1b98a1e6cd6998df6f7d31bcd6fcd5de9837["fe1b98a1e6cd6998df6f7d31bcd6fcd5de9837"]
  F386_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\49_d7f4a002ae296e5c1e95596aca86d9c69eb025["d7f4a002ae296e5c1e95596aca86d9c69eb025"]
  F387_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4b_5ba78152fc46fe1684e0c5c30ab11d7c571490["5ba78152fc46fe1684e0c5c30ab11d7c571490"]
  F388_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4b_a56116c801e916d75be712db6aa41decf8afd6["a56116c801e916d75be712db6aa41decf8afd6"]
  F389_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4a_bf38386551bd5af20cbc7dcd62d5d74bda6b1b["bf38386551bd5af20cbc7dcd62d5d74bda6b1b"]
  F390_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4a_2cc9c00008fabb6dc50ace4d2f8541281d886a["2cc9c00008fabb6dc50ace4d2f8541281d886a"]
  F391_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_4c9e901d25fe4886ef36164ce4117306cd1a08["4c9e901d25fe4886ef36164ce4117306cd1a08"]
  F392_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_83c61cadddd18fd4f4ce58b8a75be5faab15a4["83c61cadddd18fd4f4ce58b8a75be5faab15a4"]
  F393_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4a_3f65566aa000705afe86c8e8abd1562bafb8ec["3f65566aa000705afe86c8e8abd1562bafb8ec"]
  F394_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_6cbe7b76d326fb09adad0b65d0f1764f751da1["6cbe7b76d326fb09adad0b65d0f1764f751da1"]
  F395_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_bf3f5f614f38a71ab5dbf53ded30a98b5f97e1["bf3f5f614f38a71ab5dbf53ded30a98b5f97e1"]
  F396_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4b_1188ce8f39e6e911da4d38c6af15fc53350c12["1188ce8f39e6e911da4d38c6af15fc53350c12"]
  F397_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_d8969938e06d9b03cd5f104ca649749a9b65cb["d8969938e06d9b03cd5f104ca649749a9b65cb"]
  F398_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_1e68c3c108f824c74f979eed9d8655e540a819["1e68c3c108f824c74f979eed9d8655e540a819"]
  F399_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_fde6b9d934071592e02a3ec2194003c8a8a0e3["fde6b9d934071592e02a3ec2194003c8a8a0e3"]
  F400_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_560c594b96dcd358bdfc7fe43369906fbf3bf9["560c594b96dcd358bdfc7fe43369906fbf3bf9"]
  F401_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4d_cb15af442e3caa417818ea96d99cadee5aab8d["cb15af442e3caa417818ea96d99cadee5aab8d"]
  F402_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4d_64244627f3181d06d32749462193ee1a96c4c3["64244627f3181d06d32749462193ee1a96c4c3"]
  F403_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_7661080cee59e1ecab88ffa7b7c41173d74d17["7661080cee59e1ecab88ffa7b7c41173d74d17"]
  F404_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_80327cf5dc83212361663d966c521d9e12a42a["80327cf5dc83212361663d966c521d9e12a42a"]
  F405_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4c_ebd34a8065c7840ccfe58090aa5ce5a574ba00["ebd34a8065c7840ccfe58090aa5ce5a574ba00"]
  F406_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_ac9da4de6ef9bb40eda54ab0dfb930044e06f2["ac9da4de6ef9bb40eda54ab0dfb930044e06f2"]
  F407_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_3b87cc1e6bfc2cd7695fa2217d5d0159af457d["3b87cc1e6bfc2cd7695fa2217d5d0159af457d"]
  F408_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_e89c468a5a095cbf699704889a53b3ff5917c1["e89c468a5a095cbf699704889a53b3ff5917c1"]
  F409_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_fa18cd0bc51a1743dd59caa17a7623a7b6bd48["fa18cd0bc51a1743dd59caa17a7623a7b6bd48"]
  F410_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4e_bdd747776fa669592e12416c426ece30aa85e4["bdd747776fa669592e12416c426ece30aa85e4"]
  F411_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_0291fa8d74a40e258b13dcc011f072d1afc575["0291fa8d74a40e258b13dcc011f072d1afc575"]
  F412_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_62f4443003f71b9a8727503aa7a8885c7891b4["62f4443003f71b9a8727503aa7a8885c7891b4"]
  F413_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_ea21e3c22b57822036b78bd2a5350d1189756c["ea21e3c22b57822036b78bd2a5350d1189756c"]
  F414_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_3cfcb16d202bbecc8e9240866dec69d0fd245f["3cfcb16d202bbecc8e9240866dec69d0fd245f"]
  F415_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_b403d907d3770dde46abdfe4fdec6e008eb9a7["b403d907d3770dde46abdfe4fdec6e008eb9a7"]
  F416_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\4f_d2cf4054ebbbfb56b8e5ea621259836f0a97bc["d2cf4054ebbbfb56b8e5ea621259836f0a97bc"]
  F417_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_55de3afce6f77e9e3cadc04ba749c9036b2cf9["55de3afce6f77e9e3cadc04ba749c9036b2cf9"]
  F418_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_8ab29f3e46c9dd1b75853f154decf0bf5b545c["8ab29f3e46c9dd1b75853f154decf0bf5b545c"]
  F419_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_fa77ee01928934b241ed66815536e3e834e6a2["fa77ee01928934b241ed66815536e3e834e6a2"]
  F420_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_4ff4b45cbd2e540bfef398654437a7f5888f74["4ff4b45cbd2e540bfef398654437a7f5888f74"]
  F421_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_c038bed6c92ba754f41fba46161b0f70ab988b["c038bed6c92ba754f41fba46161b0f70ab988b"]
  F422_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_10759cce3919c0ee9968774b192f88e6c2e39c["10759cce3919c0ee9968774b192f88e6c2e39c"]
  F423_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_0bc0fedb731ae397db8a154663cebc641714fc["0bc0fedb731ae397db8a154663cebc641714fc"]
  F424_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_8429290c2cd9812c323eac14b1b6ac681e92da["8429290c2cd9812c323eac14b1b6ac681e92da"]
  F425_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_05e456e9e64c1e49b689890393e0d004652b7f["05e456e9e64c1e49b689890393e0d004652b7f"]
  F426_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\50_c628b7284474a85b761484b0157217bc591259["c628b7284474a85b761484b0157217bc591259"]
  F427_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_4ffcbce1ba20ac0212506dc534364afa550f79["4ffcbce1ba20ac0212506dc534364afa550f79"]
  F428_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_c3ced8d822588b2de7dd6f7d6dbe259df16e27["c3ced8d822588b2de7dd6f7d6dbe259df16e27"]
  F429_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\52_e4e36d8ab230432374e5037585e01124aaa418["e4e36d8ab230432374e5037585e01124aaa418"]
  F430_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\52_0337b8e521cbb9c1be7165dc1b2e24e737897d["0337b8e521cbb9c1be7165dc1b2e24e737897d"]
  F431_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_c019fdadd992ae373d2c7b272e599a9ae4d040["c019fdadd992ae373d2c7b272e599a9ae4d040"]
  F432_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\52_50c8ff9a81d15824ba541d13a678e8a1132daa["50c8ff9a81d15824ba541d13a678e8a1132daa"]
  F433_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\52_f90a66e26a97eaa13eb32e7e746c4a60c02896["f90a66e26a97eaa13eb32e7e746c4a60c02896"]
  F434_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_382db2319883653731f5ee336117c98f05960b["382db2319883653731f5ee336117c98f05960b"]
  F435_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\51_3e7cdc0f6464c5892cbaf9512d274c0929714c["3e7cdc0f6464c5892cbaf9512d274c0929714c"]
  F436_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_541414ee14b386fe9124eb35186f8d9c8e11c1["541414ee14b386fe9124eb35186f8d9c8e11c1"]
  F437_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\54_a2515b24fae17882e88a3394479a688a89cf15["a2515b24fae17882e88a3394479a688a89cf15"]
  F438_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\54_57134fd1fc42a5153f1364c248b091370968f4["57134fd1fc42a5153f1364c248b091370968f4"]
  F439_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_8b89521bdb0fdaa7e14140d52b8a104eaa4d15["8b89521bdb0fdaa7e14140d52b8a104eaa4d15"]
  F440_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_ae926ce94339ab18334b2f7e8c582c90781d9a["ae926ce94339ab18334b2f7e8c582c90781d9a"]
  F441_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\54_e169f8ee81cdd994230bab1917d16f13c92255["e169f8ee81cdd994230bab1917d16f13c92255"]
  F442_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\54_edceb1fcbc2a0b2bd8a23711110591513db703["edceb1fcbc2a0b2bd8a23711110591513db703"]
  F443_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\54_cf6962200b0a7697919921d1ee7e7a4d4125ae["cf6962200b0a7697919921d1ee7e7a4d4125ae"]
  F444_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_e47e14f3dce2504e409987dd26bcbdc86cea13["e47e14f3dce2504e409987dd26bcbdc86cea13"]
  F445_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_83a6cf148299b96b85127189fafb0ee7ab6ae1["83a6cf148299b96b85127189fafb0ee7ab6ae1"]
  F446_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\53_b618dd855a1fd8ab363eecce83384f88ed92b1["b618dd855a1fd8ab363eecce83384f88ed92b1"]
  F447_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\55_5461a6099e4768e4ae5fdfe67974fb276a766c["5461a6099e4768e4ae5fdfe67974fb276a766c"]
  F448_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_21edd599f3ebee4a01f542b9091b462d21f210["21edd599f3ebee4a01f542b9091b462d21f210"]
  F449_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_7744045e781ab6fb5d6757627c623c72fe935c["7744045e781ab6fb5d6757627c623c72fe935c"]
  F450_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_84a65fb15f8a1400394ea3bd005c5bae714f01["84a65fb15f8a1400394ea3bd005c5bae714f01"]
  F451_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_aebe2d2a4d8ab3e85956db77a3a606f6f0f5b3["aebe2d2a4d8ab3e85956db77a3a606f6f0f5b3"]
  F452_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_2ecaccb38e28d09d805252cd82948507c6505a["2ecaccb38e28d09d805252cd82948507c6505a"]
  F453_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_c18af5309f1f36172b397131af6d173bc16194["c18af5309f1f36172b397131af6d173bc16194"]
  F454_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\55_72c9ce6af711eeceeed80b3ced29d01ac80096["72c9ce6af711eeceeed80b3ced29d01ac80096"]
  F455_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\56_2ff32efe342e84beb3565ac109949c1157d34c["2ff32efe342e84beb3565ac109949c1157d34c"]
  F456_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_085f24899be92d62b5d15e682a3eb14010a3c6["085f24899be92d62b5d15e682a3eb14010a3c6"]
  F457_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_306fd83e0178cb4e756702dc124e6357113bf4["306fd83e0178cb4e756702dc124e6357113bf4"]
  F458_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_0f4ee45a38a2fd82e7f94691a6dfb7a6bb91ea["0f4ee45a38a2fd82e7f94691a6dfb7a6bb91ea"]
  F459_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\58_a5d21acad767af99a1a11e63d419889a0ddf41["a5d21acad767af99a1a11e63d419889a0ddf41"]
  F460_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\58_b43cdff48341c14fed951cffa3fef7b45ff2e6["b43cdff48341c14fed951cffa3fef7b45ff2e6"]
  F461_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\58_e05408c793403faf9285943ff33726906a0e6d["e05408c793403faf9285943ff33726906a0e6d"]
  F462_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_52038383a73d31d390414b162c095c39ccd96b["52038383a73d31d390414b162c095c39ccd96b"]
  F463_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_53174c7f6aac5b988183d36123aa99d24139fd["53174c7f6aac5b988183d36123aa99d24139fd"]
  F464_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\58_54aab056ae7bde3c0c3f8c0ce0f63ae7253eaa["54aab056ae7bde3c0c3f8c0ce0f63ae7253eaa"]
  F465_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\57_af52842799ccb0ce0d153d9c2e0d642e69431e["af52842799ccb0ce0d153d9c2e0d642e69431e"]
  F466_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\58_d71e83a09c89e9666a0b1eb1a7c63f1b643810["d71e83a09c89e9666a0b1eb1a7c63f1b643810"]
  F467_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_0c67dbd556b774fce23b9f34daa8e8a3f3a31f["0c67dbd556b774fce23b9f34daa8e8a3f3a31f"]
  F468_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_7230cc3eb40841db3c2a3b6d884eb33fd89214["7230cc3eb40841db3c2a3b6d884eb33fd89214"]
  F469_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_1c4d9c83d0149505fb1627a0a2055c509a4904["1c4d9c83d0149505fb1627a0a2055c509a4904"]
  F470_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_e198ede6264fe3192830c97c63e1398f0c3e8a["e198ede6264fe3192830c97c63e1398f0c3e8a"]
  F471_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_e9196cf08f8f3e67e35487c37b0703486d66ed["e9196cf08f8f3e67e35487c37b0703486d66ed"]
  F472_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_097d6d76b761d210794cf835ca3c839de9bffc["097d6d76b761d210794cf835ca3c839de9bffc"]
  F473_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_7a2034b9cdcaafdec13bd39cd08b39614cbe54["7a2034b9cdcaafdec13bd39cd08b39614cbe54"]
  F474_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_4d4b3877b4f51abf7069c91953caa3d65beea1["4d4b3877b4f51abf7069c91953caa3d65beea1"]
  F475_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_363d2b4b55e144eb25d225abd8e1c1c2433b8f["363d2b4b55e144eb25d225abd8e1c1c2433b8f"]
  F476_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\59_f74c889a3fc98aa9dd3291a81ff17f95fc5880["f74c889a3fc98aa9dd3291a81ff17f95fc5880"]
  F477_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5b_1647ca9d9424d4fe239c42e8a876f931f9be06["1647ca9d9424d4fe239c42e8a876f931f9be06"]
  F478_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_4793c1599dd8d13547edca3ddbe4d39e5a9444["4793c1599dd8d13547edca3ddbe4d39e5a9444"]
  F479_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5b_94026cd5f729b409d82f810cf5d814310daef5["94026cd5f729b409d82f810cf5d814310daef5"]
  F480_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5b_019d5f66b16ff32c35abada3636b6a8112bf5f["019d5f66b16ff32c35abada3636b6a8112bf5f"]
  F481_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_ff14fff8fceaabde44f12dd10f1dfc5b3773f0["ff14fff8fceaabde44f12dd10f1dfc5b3773f0"]
  F482_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_4c8c58a46521230372b24e3a960878b07c550e["4c8c58a46521230372b24e3a960878b07c550e"]
  F483_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_f0849b4e2a33090ea28798de72195de614ed8e["f0849b4e2a33090ea28798de72195de614ed8e"]
  F484_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5b_129b72a1e53a239d28fd1c6b3f6b14aab42830["129b72a1e53a239d28fd1c6b3f6b14aab42830"]
  F485_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5b_bdb452183127a800a6822dd80dd35927d381b8["bdb452183127a800a6822dd80dd35927d381b8"]
  F486_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5a_cb230884f8c99e5bd3b2496ca46ae5986886e7["cb230884f8c99e5bd3b2496ca46ae5986886e7"]
  F487_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5c_86a3818925e4f14c002707f2b285a3fd56a5bb["86a3818925e4f14c002707f2b285a3fd56a5bb"]
  F488_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5c_5481c9ff77dcff51a3149f4e14a6952e144a01["5481c9ff77dcff51a3149f4e14a6952e144a01"]
  F489_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5d_1047fab057b1358673a4de08425f5173a334b4["1047fab057b1358673a4de08425f5173a334b4"]
  F490_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5d_83f6b0dfcba3de872e1f6c2bb2b277b5411cde["83f6b0dfcba3de872e1f6c2bb2b277b5411cde"]
  F491_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5d_ee8569d8159f69bba8b50fe6144834848c0367["ee8569d8159f69bba8b50fe6144834848c0367"]
  F492_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5d_28680d4c7d9749bdfba0895b6152aec23e2ab5["28680d4c7d9749bdfba0895b6152aec23e2ab5"]
  F493_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5c_e057c69baf48a7fdebeae8df2a9e802dde79e3["e057c69baf48a7fdebeae8df2a9e802dde79e3"]
  F494_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5c_ddd789299a277f1b32863b6714361af256a2b9["ddd789299a277f1b32863b6714361af256a2b9"]
  F495_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_110a37f10fd42eea35080a6f910edd9d39a1f8["110a37f10fd42eea35080a6f910edd9d39a1f8"]
  F496_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5d_4ed45f30207e93926f91ce70c53ec4870a0176["4ed45f30207e93926f91ce70c53ec4870a0176"]
  F497_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_42f3d1c31bb4ee735604d03d94be301fd2a95b["42f3d1c31bb4ee735604d03d94be301fd2a95b"]
  F498_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_401e3dea9d64c1d5a428abc00db5029fbe0e47["401e3dea9d64c1d5a428abc00db5029fbe0e47"]
  F499_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_fad79962af0d6db358efaa67a0618bd0832891["fad79962af0d6db358efaa67a0618bd0832891"]
  F500_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_1c6bb52d162a98df2968c5b8043de630fe1d5b["1c6bb52d162a98df2968c5b8043de630fe1d5b"]
  F501_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_ea56a309343029abf319862b334fadf2e115f6["ea56a309343029abf319862b334fadf2e115f6"]
  F502_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_01e9cdfbfcbb3f63148b719be348fc9f43223a["01e9cdfbfcbb3f63148b719be348fc9f43223a"]
  F503_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_4db2ff4e1d55f8336bdd14c72d4abd466d6122["4db2ff4e1d55f8336bdd14c72d4abd466d6122"]
  F504_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_0b99c9a10f66e741e3fcb3e464bb2e83e32dda["0b99c9a10f66e741e3fcb3e464bb2e83e32dda"]
  F505_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\5f_20e437f2f9eb5cc5e59ac03ea6ee7cb451e511["20e437f2f9eb5cc5e59ac03ea6ee7cb451e511"]
  F506_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_4b06058e72fef103bf3d7e023081bc435fbdce["4b06058e72fef103bf3d7e023081bc435fbdce"]
  F507_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_eafa9486663d5fd1125b7699e18d950d9068c0["eafa9486663d5fd1125b7699e18d950d9068c0"]
  F508_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\61_0e314a09db1399f73e36d6c8cf49a6add9a4f0["0e314a09db1399f73e36d6c8cf49a6add9a4f0"]
  F509_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_53b9674d7dabfae04510b9ec19b1aa3ad0fcb2["53b9674d7dabfae04510b9ec19b1aa3ad0fcb2"]
  F510_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\61_12bdbc3c289b79247c825c35ce6808609e3c49["12bdbc3c289b79247c825c35ce6808609e3c49"]
  F511_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_4e54952620305a8283e757de7e6e7772d5729a["4e54952620305a8283e757de7e6e7772d5729a"]
  F512_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_b70cfade956fa6f82b6d9cc70ee0df8c88c318["b70cfade956fa6f82b6d9cc70ee0df8c88c318"]
  F513_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_8ef58ab575f08e01dd97b5288747a022288a53["8ef58ab575f08e01dd97b5288747a022288a53"]
  F514_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_e1d9e28cc8f5df67ea46e8f3e1038dd4de4be1["e1d9e28cc8f5df67ea46e8f3e1038dd4de4be1"]
  F515_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\61_8129b28edd5c78a9f4460ab01cbd6277bd2f9b["8129b28edd5c78a9f4460ab01cbd6277bd2f9b"]
  F516_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\60_a84f0e22f524affe375ca9103e09fb5110c9fe["a84f0e22f524affe375ca9103e09fb5110c9fe"]
  F517_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_458bb67498c6dc2d3bbd52728f27b0db44d8ad["458bb67498c6dc2d3bbd52728f27b0db44d8ad"]
  F518_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_2410ab8ded7d543fca2d62b7f11918255a686c["2410ab8ded7d543fca2d62b7f11918255a686c"]
  F519_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_b89b44bdf6f44bb51a308d3afa89d0fb7abf65["b89b44bdf6f44bb51a308d3afa89d0fb7abf65"]
  F520_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_86a8022ff6657a35d00dd6dac53d316e962257["86a8022ff6657a35d00dd6dac53d316e962257"]
  F521_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_b7d820a8a13802fb06993124cdb490206cdc62["b7d820a8a13802fb06993124cdb490206cdc62"]
  F522_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\63_aeb388cc57aa46ba69cee00941efdd2a564dbd["aeb388cc57aa46ba69cee00941efdd2a564dbd"]
  F523_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_e0387ca530723c9843ba49401f12a1c9b650ad["e0387ca530723c9843ba49401f12a1c9b650ad"]
  F524_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\63_37cc1e2e048b33c790be01baf09b99ebbf9c24["37cc1e2e048b33c790be01baf09b99ebbf9c24"]
  F525_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\62_7e702eeee38705c8e191002ab8716b11db55de["7e702eeee38705c8e191002ab8716b11db55de"]
  F526_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\63_a6a1fbc6e6c1f4ef74e282a97c0594d8d621c4["a6a1fbc6e6c1f4ef74e282a97c0594d8d621c4"]
  F527_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_009949b3518c3344c823a48616e0b1d2af6946["009949b3518c3344c823a48616e0b1d2af6946"]
  F528_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_7898f5144e28160f1aeb1eaa915507d3ffe594["7898f5144e28160f1aeb1eaa915507d3ffe594"]
  F529_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\63_eeb92954a1a0d1ecb04ef16a3c1be44e1673db["eeb92954a1a0d1ecb04ef16a3c1be44e1673db"]
  F530_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_d9c3b20c9e3c18aae38a629fc608fe2b6fc189["d9c3b20c9e3c18aae38a629fc608fe2b6fc189"]
  F531_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_b06c269eca62ddec5e7ee8ce562960b1bce9c5["b06c269eca62ddec5e7ee8ce562960b1bce9c5"]
  F532_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_8fdb20934ee29ea6067639c1f9db91eb166633["8fdb20934ee29ea6067639c1f9db91eb166633"]
  F533_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_001bd935491bbf5d3b736b50d1bb068036707d["001bd935491bbf5d3b736b50d1bb068036707d"]
  F534_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_a0820487cd384ca9fa78f8397aa97adb912ddb["a0820487cd384ca9fa78f8397aa97adb912ddb"]
  F535_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_d0441c0b25eb41afc2cfd9955abff4ff5a581a["d0441c0b25eb41afc2cfd9955abff4ff5a581a"]
  F536_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_e7eff7a3c9658b1d9d43d433a4d80c1ce7a7c1["e7eff7a3c9658b1d9d43d433a4d80c1ce7a7c1"]
  F537_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\64_fdd0e2e81ac5441683b8bb578f3f53d2fc23ee["fdd0e2e81ac5441683b8bb578f3f53d2fc23ee"]
  F538_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_1550b50f10e85df71e4365c64b32d79dcdae69["1550b50f10e85df71e4365c64b32d79dcdae69"]
  F539_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_005458d2d9cefb2d7cb7f53e7eeb3f0167ac74["005458d2d9cefb2d7cb7f53e7eeb3f0167ac74"]
  F540_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_2c8d5730175e10653eecf3a93476042cf94157["2c8d5730175e10653eecf3a93476042cf94157"]
  F541_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_3f181e8f14b39caf74643fb3af6157b09c40d8["3f181e8f14b39caf74643fb3af6157b09c40d8"]
  F542_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_5c9cc9ad0ea9c3a85ab4dcc641392c094cf30e["5c9cc9ad0ea9c3a85ab4dcc641392c094cf30e"]
  F543_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_4905ccfe99b088081c48b67e62645dcb67f0c1["4905ccfe99b088081c48b67e62645dcb67f0c1"]
  F544_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_5b5305cbcba9fc25bd983f97a0577cf6f52d67["5b5305cbcba9fc25bd983f97a0577cf6f52d67"]
  F545_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\65_513252942ebdf46fc0151d31b33380ee59f69a["513252942ebdf46fc0151d31b33380ee59f69a"]
  F546_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_26989e52e3163909ef67208febaefec69581c0["26989e52e3163909ef67208febaefec69581c0"]
  F547_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_5817887c7e009873c240d9a7d267e5b4c506ef["5817887c7e009873c240d9a7d267e5b4c506ef"]
  F548_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_4947aa16f516d2d2d9ce4ecd4986846155f486["4947aa16f516d2d2d9ce4ecd4986846155f486"]
  F549_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\67_083069511a7b24750e0e016294345b0ba48841["083069511a7b24750e0e016294345b0ba48841"]
  F550_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_7aaef0c891a18c6177b09b53418bf59c6ab91f["7aaef0c891a18c6177b09b53418bf59c6ab91f"]
  F551_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\67_32c9aaecb2127e24f05c960a9f61f219b36925["32c9aaecb2127e24f05c960a9f61f219b36925"]
  F552_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_5f4b2caffa04def9ec3c31b79ff0048244c435["5f4b2caffa04def9ec3c31b79ff0048244c435"]
  F553_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\67_4989c6aa26e37cd79d1cd4fe4e92fd7aa0bfcb["4989c6aa26e37cd79d1cd4fe4e92fd7aa0bfcb"]
  F554_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\67_0c2309e7963c783b018629df8dfcd0ca177db5["0c2309e7963c783b018629df8dfcd0ca177db5"]
  F555_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\67_10e20a0203e3013a457a2e74f4e8a6618e3b4e["10e20a0203e3013a457a2e74f4e8a6618e3b4e"]
  F556_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\66_d7a46362be6163594ae63f73ba625f420bf177["d7a46362be6163594ae63f73ba625f420bf177"]
  F557_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\68_71635051818647118dc35c0727fcfc97ba2b50["71635051818647118dc35c0727fcfc97ba2b50"]
  F558_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\69_4277361bbc0cfff6fa51bfbf7c851d053626f1["4277361bbc0cfff6fa51bfbf7c851d053626f1"]
  F559_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\68_2720e10ef1df4a7a45f04e7b4a8ac1d2af72fb["2720e10ef1df4a7a45f04e7b4a8ac1d2af72fb"]
  F560_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6a_125bf6c02c74e62ca99a664299afddf7d2c022["125bf6c02c74e62ca99a664299afddf7d2c022"]
  F561_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\69_0e326d10b352f830dc374aad6d2a900f05d352["0e326d10b352f830dc374aad6d2a900f05d352"]
  F562_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6a_30348d164b55315c3fd98ee7f1306b50ba53b0["30348d164b55315c3fd98ee7f1306b50ba53b0"]
  F563_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\69_6c947cb406858dc3dc7abc82f336392a4742c2["6c947cb406858dc3dc7abc82f336392a4742c2"]
  F564_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6a_1ca6cffb2872831573ceedb10688f730adabff["1ca6cffb2872831573ceedb10688f730adabff"]
  F565_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\69_851a04cedba63efbc23bbb7ee3bfc1630a0378["851a04cedba63efbc23bbb7ee3bfc1630a0378"]
  F566_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\68_c2e908e8751650d5027e7bf50891ce9513cc59["c2e908e8751650d5027e7bf50891ce9513cc59"]
  F567_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6a_dedbf70a0f5592cbaca1b3e47bb514d007645b["dedbf70a0f5592cbaca1b3e47bb514d007645b"]
  F568_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_869b59c592bd73473d0bdb7144803e140bae9d["869b59c592bd73473d0bdb7144803e140bae9d"]
  F569_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_83fda5d370e866229a3bee3b09d5021e7ddeae["83fda5d370e866229a3bee3b09d5021e7ddeae"]
  F570_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_8e7b1f8ff804f150021493182462f4805b4311["8e7b1f8ff804f150021493182462f4805b4311"]
  F571_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_d73668354b0fd683c6499a55885b894b889dc2["d73668354b0fd683c6499a55885b894b889dc2"]
  F572_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6c_e3beb8caeaa34295e7755a48de121d4899e73d["e3beb8caeaa34295e7755a48de121d4899e73d"]
  F573_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6c_b6f096fd0d773d5bc29d45ce09ee127d0a4ee4["b6f096fd0d773d5bc29d45ce09ee127d0a4ee4"]
  F574_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_5ef714ac0488b449842d11c08680713a3c9a72["5ef714ac0488b449842d11c08680713a3c9a72"]
  F575_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_722839b333d71175825cbdf741f65565a13536["722839b333d71175825cbdf741f65565a13536"]
  F576_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6b_ffd660d9f5e19b00205e60663f5ccb7f9d1ad0["ffd660d9f5e19b00205e60663f5ccb7f9d1ad0"]
  F577_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6c_e884d2bcbe292e463d2ab33418aa947303bdb6["e884d2bcbe292e463d2ab33418aa947303bdb6"]
  F578_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6d_a3dc530ce146e90afe62d68d957093415c0b62["a3dc530ce146e90afe62d68d957093415c0b62"]
  F579_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_250c90f5c2d1c54448f0497cc5d7a617cd74eb["250c90f5c2d1c54448f0497cc5d7a617cd74eb"]
  F580_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6d_a08676f6032ead57eb3924f3705cf4e96678bd["a08676f6032ead57eb3924f3705cf4e96678bd"]
  F581_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6d_cdaf2e59a022f1e0753933e590524e739c5998["cdaf2e59a022f1e0753933e590524e739c5998"]
  F582_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6d_a548592d7d46528587863175c9bfc8ebb0b220["a548592d7d46528587863175c9bfc8ebb0b220"]
  F583_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_9f997c7e1ac9944b616e2959480d083070e62a["9f997c7e1ac9944b616e2959480d083070e62a"]
  F584_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_78981caa183d20e6b9fe1b36bb35f189e28b38["78981caa183d20e6b9fe1b36bb35f189e28b38"]
  F585_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6c_e952505c287f53bcd2468044a7586ac5015a56["e952505c287f53bcd2468044a7586ac5015a56"]
  F586_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6d_c686b797c8abd9da71689c3f7874f1b23e9ac5["c686b797c8abd9da71689c3f7874f1b23e9ac5"]
  F587_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_b39861ce41d7d927662fce6d3f65f2abce4450["b39861ce41d7d927662fce6d3f65f2abce4450"]
  F588_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_0adf5dcafd8e7ae6f1dcb6ec398b27100fa1b3["0adf5dcafd8e7ae6f1dcb6ec398b27100fa1b3"]
  F589_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_ee324d58d38212f8fafda0f8540933d98a9732["ee324d58d38212f8fafda0f8540933d98a9732"]
  F590_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_79a42902bdcee61f96adb752af96b277633471["79a42902bdcee61f96adb752af96b277633471"]
  F591_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_43fbde9859c08cb8e84837408a8306b8f4238a["43fbde9859c08cb8e84837408a8306b8f4238a"]
  F592_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_83ca8069ee904ea6f6c7bbe6046b6d3dc98945["83ca8069ee904ea6f6c7bbe6046b6d3dc98945"]
  F593_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_998c799dca53f29bfb9ed568a1e66b142b0c78["998c799dca53f29bfb9ed568a1e66b142b0c78"]
  F594_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_cccc29be339549665378044b4e3f1b9af3b57c["cccc29be339549665378044b4e3f1b9af3b57c"]
  F595_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_2a3a7b219891160239b127dad36627d824b7bf["2a3a7b219891160239b127dad36627d824b7bf"]
  F596_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6e_d313ea9f352021d6aab8931d7fe428379687a5["d313ea9f352021d6aab8931d7fe428379687a5"]
  F597_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\70_00c6d57040ba7f99adc37e0c67e65f1de3abfe["00c6d57040ba7f99adc37e0c67e65f1de3abfe"]
  F598_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\71_10f4e3bf5d6d7e9b99ec5279fdd8fc0a73197b["10f4e3bf5d6d7e9b99ec5279fdd8fc0a73197b"]
  F599_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\6f_cce2e906e76b2346a28942bb1db217eacdde10["cce2e906e76b2346a28942bb1db217eacdde10"]
  F600_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\71_2bf9bc43149cedd3fe51e65bd6525d389c3a23["2bf9bc43149cedd3fe51e65bd6525d389c3a23"]
  F601_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\70_5d11b095441eaaddffa8569a048b87cf2d92a9["5d11b095441eaaddffa8569a048b87cf2d92a9"]
  F602_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\71_3e8e67d512611b65a907ae4e1435f488af4d31["3e8e67d512611b65a907ae4e1435f488af4d31"]
  F603_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\71_45050743c78491adddb158f54376d70053cfff["45050743c78491adddb158f54376d70053cfff"]
  F604_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\72_3d1186caf4bb772d0c76ce3a384fdc02a842ae["3d1186caf4bb772d0c76ce3a384fdc02a842ae"]
  F605_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\70_ee11e0ab2afb25436ba2e162594cc7bfcecafa["ee11e0ab2afb25436ba2e162594cc7bfcecafa"]
  F606_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\72_900150580306f929e1d821b3ff7176226ec76d["900150580306f929e1d821b3ff7176226ec76d"]
  F607_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\72_bc8649236196d905fc6b6be2cd08a6aea6aa25["bc8649236196d905fc6b6be2cd08a6aea6aa25"]
  F608_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\73_d500388c2cca6cda4eb51e5324900dd17f9f2b["d500388c2cca6cda4eb51e5324900dd17f9f2b"]
  F609_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_9a087d96fa097f914ca736f001ca1fc13d7190["9a087d96fa097f914ca736f001ca1fc13d7190"]
  F610_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_ce3bac0c811fa7fb547d0bf266960b55212671["ce3bac0c811fa7fb547d0bf266960b55212671"]
  F611_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_e1f3127c40125fcb63f449059a7a14646fff25["e1f3127c40125fcb63f449059a7a14646fff25"]
  F612_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_b28206c56a7bb91408338002ed8b35de9726b8["b28206c56a7bb91408338002ed8b35de9726b8"]
  F613_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\72_a1860c9a817c8107261a6f4dc0353b3db40b53["a1860c9a817c8107261a6f4dc0353b3db40b53"]
  F614_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_d33297023143e16e33b846f52d48b52102e203["d33297023143e16e33b846f52d48b52102e203"]
  F615_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_94c7f5b5614de85ebc4bd24c8599b6e14e91e2["94c7f5b5614de85ebc4bd24c8599b6e14e91e2"]
  F616_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_cb7a7fb149994dab8d4af8772f54d0da40e3cb["cb7a7fb149994dab8d4af8772f54d0da40e3cb"]
  F617_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_de7b60a904d401d6b2f4392975f599a45eac71["de7b60a904d401d6b2f4392975f599a45eac71"]
  F618_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_7827056bdd53d56a88968631c1853e30a9f30d["7827056bdd53d56a88968631c1853e30a9f30d"]
  F619_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_0af92a37fa04901b40681963b7a3c307e05aab["0af92a37fa04901b40681963b7a3c307e05aab"]
  F620_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_34f4446ef3db4ac8e7ad0cea3e8bddf8fdf503["34f4446ef3db4ac8e7ad0cea3e8bddf8fdf503"]
  F621_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\76_5cb5b4975e19049542ea884b9b0b27e380ea40["5cb5b4975e19049542ea884b9b0b27e380ea40"]
  F622_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\76_e485a8e80bb7603540d484f70e6a3c196a2a01["e485a8e80bb7603540d484f70e6a3c196a2a01"]
  F623_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_77200bf9e9cc783a99bad98c54a9c701835baf["77200bf9e9cc783a99bad98c54a9c701835baf"]
  F624_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\76_e4977749d55f216745d619c4a6b42b6f533654["e4977749d55f216745d619c4a6b42b6f533654"]
  F625_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\75_b6beb90cccca36c89f35440d7b7dfc787187be["b6beb90cccca36c89f35440d7b7dfc787187be"]
  F626_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\74_efe757b7714d0cd974ce607b96a9ccb084c0ce["efe757b7714d0cd974ce607b96a9ccb084c0ce"]
  F627_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\77_df18ecf80f7ecdd07f7927d7b39785e6076a8b["df18ecf80f7ecdd07f7927d7b39785e6076a8b"]
  F628_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\77_d342709337afa415c0ed7ba4c04de5e8f2a01d["d342709337afa415c0ed7ba4c04de5e8f2a01d"]
  F629_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\78_f2155811cb95a4135cae1d6a1305b62489182d["f2155811cb95a4135cae1d6a1305b62489182d"]
  F630_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\78_71addd42962efa80fd61d5893f120d65627fcf["71addd42962efa80fd61d5893f120d65627fcf"]
  F631_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\77_efb4e55ae371d13a22055f31291ac891b2ea47["efb4e55ae371d13a22055f31291ac891b2ea47"]
  F632_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\79_3b4f47b62ce9e2795ee4894974a3aaaa331ab9["3b4f47b62ce9e2795ee4894974a3aaaa331ab9"]
  F633_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\79_6b4285421a238e640de4c846d07527f8be47e2["6b4285421a238e640de4c846d07527f8be47e2"]
  F634_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_0ddee12a3f71df142578b8333dedb54de5c004["0ddee12a3f71df142578b8333dedb54de5c004"]
  F635_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\77_fb4df5c67857ccf8bf5e7462ec45cab0284771["fb4df5c67857ccf8bf5e7462ec45cab0284771"]
  F636_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\79_8348f5193193a2a1901c37571bb3838e53bee7["8348f5193193a2a1901c37571bb3838e53bee7"]
  F637_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_1cdba66cc4e6c389676d442cd4818257918773["1cdba66cc4e6c389676d442cd4818257918773"]
  F638_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_be9731f7f3ef2873e6d877c613651e487bb61d["be9731f7f3ef2873e6d877c613651e487bb61d"]
  F639_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_b8d72c971bdb17a7303b8bb18c27fe7e062a50["b8d72c971bdb17a7303b8bb18c27fe7e062a50"]
  F640_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_9f93e2a32e3dc044fa6dbde8e5f311b2140030["9f93e2a32e3dc044fa6dbde8e5f311b2140030"]
  F641_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_f211ba34e0607345a6f48930c7a57a18710ce2["f211ba34e0607345a6f48930c7a57a18710ce2"]
  F642_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_f251b679a34cd347424eab13389a31114114ea["f251b679a34cd347424eab13389a31114114ea"]
  F643_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7b_aab6f6dcb31868a15edcc1cfdf4d7f98d932ea["aab6f6dcb31868a15edcc1cfdf4d7f98d932ea"]
  F644_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7b_bd9e9ad40a7acc221aff2206f7e1f9cad36772["bd9e9ad40a7acc221aff2206f7e1f9cad36772"]
  F645_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7b_2f932e7863fb5bd92a8990ae0cc0b97b61b265["2f932e7863fb5bd92a8990ae0cc0b97b61b265"]
  F646_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7a_aa14f9cfb28188b7e26cbdae4f1d9f716aed21["aa14f9cfb28188b7e26cbdae4f1d9f716aed21"]
  F647_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_4d89f33d46161dfcfe20368002d1ef23a295ca["4d89f33d46161dfcfe20368002d1ef23a295ca"]
  F648_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7b_ecd59a3f0dd8cdada4be5dc0952caca7db87d5["ecd59a3f0dd8cdada4be5dc0952caca7db87d5"]
  F649_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_25c2a8ce7770ecbc90ad9efd6f5117270ac5f9["25c2a8ce7770ecbc90ad9efd6f5117270ac5f9"]
  F650_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_567bec53c9625ac0f2aa19342ef73eb1b54324["567bec53c9625ac0f2aa19342ef73eb1b54324"]
  F651_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_49e5529b46657a8613fe8aced9782adb860d82["49e5529b46657a8613fe8aced9782adb860d82"]
  F652_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_3f32759a3495c7b7713f24e3a3d8516c236803["3f32759a3495c7b7713f24e3a3d8516c236803"]
  F653_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_bf7ca1c75629a492c1c0a37dc58a6939fb332a["bf7ca1c75629a492c1c0a37dc58a6939fb332a"]
  F654_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_4b84e2ac7621088a51d765489e4f169bbfa697["4b84e2ac7621088a51d765489e4f169bbfa697"]
  F655_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_0b8745356e4f4e26701edf5af3c0f0aa1177e1["0b8745356e4f4e26701edf5af3c0f0aa1177e1"]
  F656_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7c_a7a3d67063fc18f8103c7c3dfb3903a84b7600["a7a3d67063fc18f8103c7c3dfb3903a84b7600"]
  F657_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_6d938e46d6f3b1e7b69753b09bbbe3f753c3e3["6d938e46d6f3b1e7b69753b09bbbe3f753c3e3"]
  F658_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_5be36de3ae14fd2ca303b7b45fa3ecdddf13ab["5be36de3ae14fd2ca303b7b45fa3ecdddf13ab"]
  F659_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_0f7bce6a939be78ba86b377e0dd9d87ade55f5["0f7bce6a939be78ba86b377e0dd9d87ade55f5"]
  F660_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_a97aca68ff835e72d778768aa6c20b8f17f69c["a97aca68ff835e72d778768aa6c20b8f17f69c"]
  F661_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_2bdcdeef27fae126f7a73ec6c6b098dff4153e["2bdcdeef27fae126f7a73ec6c6b098dff4153e"]
  F662_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_b2ab604c29dc7ae3d3a6a1e8ecf0a5d347f2c6["b2ab604c29dc7ae3d3a6a1e8ecf0a5d347f2c6"]
  F663_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7d_bc7ffe666b9dc416c54cc40d567dd336c61158["bc7ffe666b9dc416c54cc40d567dd336c61158"]
  F664_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_273c5f73429bf2932f83636bf311e8b0a71a59["273c5f73429bf2932f83636bf311e8b0a71a59"]
  F665_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_336266ec30d8eb06019cf55b44df7183764148["336266ec30d8eb06019cf55b44df7183764148"]
  F666_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_7200d3f724e1938a50da9aed28506fb765bab8["7200d3f724e1938a50da9aed28506fb765bab8"]
  F667_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7f_299f6e77593deca5454ba5e9ef00d6e84e58e0["299f6e77593deca5454ba5e9ef00d6e84e58e0"]
  F668_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\80_2862a1e5876d22dc990c737f4ffa3c4307b6f2["2862a1e5876d22dc990c737f4ffa3c4307b6f2"]
  F669_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\80_43dda397f6a9bb07778c8a7325494e2518c474["43dda397f6a9bb07778c8a7325494e2518c474"]
  F670_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_9b2f0166681f042afaf424c810dcd20ac8ef32["9b2f0166681f042afaf424c810dcd20ac8ef32"]
  F671_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7e_d6d9a0affa22c09398baa526fd724b2bbdc46a["d6d9a0affa22c09398baa526fd724b2bbdc46a"]
  F672_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7f_b516197a07aa4d2084e2cf1392f65c42cae1ad["b516197a07aa4d2084e2cf1392f65c42cae1ad"]
  F673_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\81_5d9efc0e81fb8580aa71a7fa73de19720274ac["5d9efc0e81fb8580aa71a7fa73de19720274ac"]
  F674_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\80_dd8a5210687668d773ebe157d52d25012b06ea["dd8a5210687668d773ebe157d52d25012b06ea"]
  F675_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\80_cec709c135fc9353fb630610a358646ea3bcc4["cec709c135fc9353fb630610a358646ea3bcc4"]
  F676_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\7f_740e3419004bbf6c1691c8bee0f126ba60baf8["740e3419004bbf6c1691c8bee0f126ba60baf8"]
  F677_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\82_ddef33e4891b87501ae99b45d47c9a865ef9b9["ddef33e4891b87501ae99b45d47c9a865ef9b9"]
  F678_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\83_8007553f59760b757428abf5c1a8a3f09aeb52["8007553f59760b757428abf5c1a8a3f09aeb52"]
  F679_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\81_8470edb432b724c59b4373b3f98f1d307aff35["8470edb432b724c59b4373b3f98f1d307aff35"]
  F680_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\82_ea60447bf30b73b4122616840f4fee3dff3e07["ea60447bf30b73b4122616840f4fee3dff3e07"]
  F681_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\83_8823f66263e029b4df045c9b037ccf6672c7dc["8823f66263e029b4df045c9b037ccf6672c7dc"]
  F682_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\83_dfc4eebbfd0c3bff5d702434dd2624e4cfbe9a["dfc4eebbfd0c3bff5d702434dd2624e4cfbe9a"]
  F683_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\82_66e0196bc8e75351d46c79b8440b97bf003518["66e0196bc8e75351d46c79b8440b97bf003518"]
  F684_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\81_b9e279d69c6be6c51cb4ebccb5a1034013f157["b9e279d69c6be6c51cb4ebccb5a1034013f157"]
  F685_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_099181b9290e9d6612bdddb4fb2af79f073acb["099181b9290e9d6612bdddb4fb2af79f073acb"]
  F686_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\83_e05075b07a1d820f313817005c5a9a3cf40c1d["e05075b07a1d820f313817005c5a9a3cf40c1d"]
  F687_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_7422ad0a4a3a5e45a096fbab6c299309d717d8["7422ad0a4a3a5e45a096fbab6c299309d717d8"]
  F688_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_ec9134611dd8cfac3567230e23ac2336e215e6["ec9134611dd8cfac3567230e23ac2336e215e6"]
  F689_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_a2dce2858754ed19929d7e70d1070130c8deb4["a2dce2858754ed19929d7e70d1070130c8deb4"]
  F690_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\85_1343ad104f2fd2b45f202ae053f8d23da49b41["1343ad104f2fd2b45f202ae053f8d23da49b41"]
  F691_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_1c314de3137c01a19c4f05b85a8f5aaf4b8605["1c314de3137c01a19c4f05b85a8f5aaf4b8605"]
  F692_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\85_fa2e06bedf2a84bab678a573699cd0a0764582["fa2e06bedf2a84bab678a573699cd0a0764582"]
  F693_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\85_b11ffa9e8a9d68222ab0d7ac952d08df09c2fc["b11ffa9e8a9d68222ab0d7ac952d08df09c2fc"]
  F694_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\84_5a9766c5d5eb71cf1c3d01cc824cfdc8cc37a4["5a9766c5d5eb71cf1c3d01cc824cfdc8cc37a4"]
  F695_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\86_06785f409fa2b812377f3ea3d5fa8c3f66e626["06785f409fa2b812377f3ea3d5fa8c3f66e626"]
  F696_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\85_fc69ec6343fed66996e1e411d1326b36be276f["fc69ec6343fed66996e1e411d1326b36be276f"]
  F697_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\86_5aa5e95f14098d63c4b85170c41c8196a883c8["5aa5e95f14098d63c4b85170c41c8196a883c8"]
  F698_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\86_bc68999450c4f76da770047e05e086ff70ec31["bc68999450c4f76da770047e05e086ff70ec31"]
  F699_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_4260aeab9a1a2fbfac13b24499df6485b8616a["4260aeab9a1a2fbfac13b24499df6485b8616a"]
  F700_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_149f75d367ae7bbde8186e4e8a6973166fb370["149f75d367ae7bbde8186e4e8a6973166fb370"]
  F701_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_5832033c085e7de22a9309578790921b5621e5["5832033c085e7de22a9309578790921b5621e5"]
  F702_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_fda1393b26bc9706bcea2f284317f3446c9f6f["fda1393b26bc9706bcea2f284317f3446c9f6f"]
  F703_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\86_0a9ca9b2e776b3e17932eb6b2b578354146488["0a9ca9b2e776b3e17932eb6b2b578354146488"]
  F704_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_e22cecebd20f3f64ea2d1b62285502cd3d6452["e22cecebd20f3f64ea2d1b62285502cd3d6452"]
  F705_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\87_45d5220c86a37040ce1a2b9babd0075739db79["45d5220c86a37040ce1a2b9babd0075739db79"]
  F706_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\88_40e74493c196a415b19efdff4d43dc4693bbc4["40e74493c196a415b19efdff4d43dc4693bbc4"]
  F707_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_16203e8e9828cdd2062bc26947a3ed29a0e308["16203e8e9828cdd2062bc26947a3ed29a0e308"]
  F708_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\88_905706a10798cca79a6c9e288afc540652ba36["905706a10798cca79a6c9e288afc540652ba36"]
  F709_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_1a45df97467931bf5792c1d5e52e3298552ab9["1a45df97467931bf5792c1d5e52e3298552ab9"]
  F710_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\88_dd842c3884c9b627d411f973cae4cdc978b814["dd842c3884c9b627d411f973cae4cdc978b814"]
  F711_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_6d71b8d1acca6626671c57c1f3bbfe72101257["6d71b8d1acca6626671c57c1f3bbfe72101257"]
  F712_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_e1f80ac29ef35f6281d8f01d8920c9e15adfea["e1f80ac29ef35f6281d8f01d8920c9e15adfea"]
  F713_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_109b2a86a25729b4ba8c4db7e6e00abd6abd59["109b2a86a25729b4ba8c4db7e6e00abd6abd59"]
  F714_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\88_b6697391d94e04d8506209bdf8856492210bc7["b6697391d94e04d8506209bdf8856492210bc7"]
  F715_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_389590461eeb39a5e965f6ffdd43e15666684d["389590461eeb39a5e965f6ffdd43e15666684d"]
  F716_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\89_7c630995783fb29a2bfbfce1edffa91e32609a["7c630995783fb29a2bfbfce1edffa91e32609a"]
  F717_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_7a0c28bb8c51e9e58637ee4c5ae476a5a52fde["7a0c28bb8c51e9e58637ee4c5ae476a5a52fde"]
  F718_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_10d1b5bddb870cc124830a97603c3a509a6008["10d1b5bddb870cc124830a97603c3a509a6008"]
  F719_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_ef74ef78d70b55528e99cb0f0116c131c12462["ef74ef78d70b55528e99cb0f0116c131c12462"]
  F720_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_bf04f902ee2bf103fb50a1115818c4d2171a87["bf04f902ee2bf103fb50a1115818c4d2171a87"]
  F721_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_8abc1f649b5f563918ea7769f4fb05c726f57d["8abc1f649b5f563918ea7769f4fb05c726f57d"]
  F722_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_3c6aef4d93d05b8f62eaedaab55b13ad16b861["3c6aef4d93d05b8f62eaedaab55b13ad16b861"]
  F723_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_a46651fdb9d6746eaea5dfe90b6ef8f2b55000["a46651fdb9d6746eaea5dfe90b6ef8f2b55000"]
  F724_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_47c61b0b269682d6ff8d01bf25f1bf707c68ae["47c61b0b269682d6ff8d01bf25f1bf707c68ae"]
  F725_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_0cc721ab7461ef4af499492aa32d57be40cd79["0cc721ab7461ef4af499492aa32d57be40cd79"]
  F726_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8a_19c481166aaa86f29ecf7e61d272e5c70f4a2f["19c481166aaa86f29ecf7e61d272e5c70f4a2f"]
  F727_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_57a6cea5b543619ac02fbecea7ebbcff886221["57a6cea5b543619ac02fbecea7ebbcff886221"]
  F728_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_84a81b8a0e5cc5d41674ef4833deca21508d9a["84a81b8a0e5cc5d41674ef4833deca21508d9a"]
  F729_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_29905bf0c48403c55f9174ff8564ab1b90a96e["29905bf0c48403c55f9174ff8564ab1b90a96e"]
  F730_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_17c449735fd1681ec7073f9ff202d983743ed2["17c449735fd1681ec7073f9ff202d983743ed2"]
  F731_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_911845c6986c1bce902691931d36d0c9acaedd["911845c6986c1bce902691931d36d0c9acaedd"]
  F732_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_623d54ed4eddffe62198c9db332cb8df6b99a2["623d54ed4eddffe62198c9db332cb8df6b99a2"]
  F733_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_a328e95e0c896c0cfaa3a56a9444dd9a653be5["a328e95e0c896c0cfaa3a56a9444dd9a653be5"]
  F734_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_d37b240ea63d9fd7558e06bb8130123c3b03dd["d37b240ea63d9fd7558e06bb8130123c3b03dd"]
  F735_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8b_beec6253683ada53b903d1d4e0372525c84c57["beec6253683ada53b903d1d4e0372525c84c57"]
  F736_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_2c51b47f623ca016241384c3785b8b95a5d849["2c51b47f623ca016241384c3785b8b95a5d849"]
  F737_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_2418136ea81c7a82e681e635473a5f3ee35475["2418136ea81c7a82e681e635473a5f3ee35475"]
  F738_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_b8f0af5423d02e5c7c8c41bf56891da8eb9313["b8f0af5423d02e5c7c8c41bf56891da8eb9313"]
  F739_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_0358adab8c60ed9c30bcc93b6d9b035df95681["0358adab8c60ed9c30bcc93b6d9b035df95681"]
  F740_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_99ce0bcb07da53d62612df23a0314b84cf52a6["99ce0bcb07da53d62612df23a0314b84cf52a6"]
  F741_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_ba30d399e9bd09a712182dab3b0901ba9fc276["ba30d399e9bd09a712182dab3b0901ba9fc276"]
  F742_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_97b7da41ce83e4bb5cb089915fac62345f2a49["97b7da41ce83e4bb5cb089915fac62345f2a49"]
  F743_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_c5f9f8d8a3b6154d8c1c891111d14ff1b2cbdc["c5f9f8d8a3b6154d8c1c891111d14ff1b2cbdc"]
  F744_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8c_83bd9fa6f5c005229c735d2e600f7b28441eba["83bd9fa6f5c005229c735d2e600f7b28441eba"]
  F745_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_8b1b845bcb1dbf137835b1159f8d178ba04cf1["8b1b845bcb1dbf137835b1159f8d178ba04cf1"]
  F746_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_9eb1c0c9c6c4e387526f96e2000a8ca9922bb2["9eb1c0c9c6c4e387526f96e2000a8ca9922bb2"]
  F747_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8e_4408d7b7f2c6c8f6195993d1bf21622a3f2ddc["4408d7b7f2c6c8f6195993d1bf21622a3f2ddc"]
  F748_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8e_5a7f5aa2454b11a919c70ddd51f03666fdd773["5a7f5aa2454b11a919c70ddd51f03666fdd773"]
  F749_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8e_0084c6d859ca88ef70abd66fbe9fda8dd9ce0c["0084c6d859ca88ef70abd66fbe9fda8dd9ce0c"]
  F750_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8f_ba7feaf6cc7c279857c961ae473684ecc83446["ba7feaf6cc7c279857c961ae473684ecc83446"]
  F751_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8f_0a2243a831a049f57148f000bd9794c3ea75ce["0a2243a831a049f57148f000bd9794c3ea75ce"]
  F752_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8f_87a9bc71b19f5b6b3e83e62f11dff7ddbb6219["87a9bc71b19f5b6b3e83e62f11dff7ddbb6219"]
  F753_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8f_981548fb7455b8d721dd3683beac94580531f9["981548fb7455b8d721dd3683beac94580531f9"]
  F754_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8d_bebb63aa119d6d8ff5d0c6b0e1c3d804c23938["bebb63aa119d6d8ff5d0c6b0e1c3d804c23938"]
  F755_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\90_1d5d5327bb7d7c19990135cf9c9d8bcefee9aa["1d5d5327bb7d7c19990135cf9c9d8bcefee9aa"]
  F756_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\8f_82eb0ea3fbb26f5ffbba999641fa4c41cadf6d["82eb0ea3fbb26f5ffbba999641fa4c41cadf6d"]
  F757_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\90_8ec191416ed13bb857d589661a750982b3bfad["8ec191416ed13bb857d589661a750982b3bfad"]
  F758_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\90_7a41e82d6017b26cb67ec9ad70c4c6fa24fec6["7a41e82d6017b26cb67ec9ad70c4c6fa24fec6"]
  F759_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\90_4b71b931d499c3f6f95c1434370ce7be2abef7["4b71b931d499c3f6f95c1434370ce7be2abef7"]
  F760_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\90_3f558ede76b015eb4d4749c0d46e3c45586bea["3f558ede76b015eb4d4749c0d46e3c45586bea"]
  F761_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\91_95833e01269c5c8a1c6953c161e6deb3207fcf["95833e01269c5c8a1c6953c161e6deb3207fcf"]
  F762_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\91_7248d7866286fc58226136427767d131a2c45e["7248d7866286fc58226136427767d131a2c45e"]
  F763_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\91_31ac236ddb02361162bafc97dbc96a34d20327["31ac236ddb02361162bafc97dbc96a34d20327"]
  F764_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\91_85f20789a69e1e452232d876141f5936407484["85f20789a69e1e452232d876141f5936407484"]
  F765_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\91_e0c7966701ee122c897d9267376452d4f66417["e0c7966701ee122c897d9267376452d4f66417"]
  F766_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\92_450f93273470af42eeee491874afb2039b700a["450f93273470af42eeee491874afb2039b700a"]
  F767_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\92_d6c2529c8195e6a91b342cce0b6908a01966d7["d6c2529c8195e6a91b342cce0b6908a01966d7"]
  F768_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_076a615fba4068a727185a421e784d2f3d744d["076a615fba4068a727185a421e784d2f3d744d"]
  F769_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_12e0611e0b59c788a24c54aa9be75cfbe6a484["12e0611e0b59c788a24c54aa9be75cfbe6a484"]
  F770_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_de1639076af695375a1947f1a800ea5878a25e["de1639076af695375a1947f1a800ea5878a25e"]
  F771_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_16e6e2aad1a45adc046e1391a682c7f0585b38["16e6e2aad1a45adc046e1391a682c7f0585b38"]
  F772_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\94_3bf0750a317ca25e8978df3dec62cdca7fb08d["3bf0750a317ca25e8978df3dec62cdca7fb08d"]
  F773_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\94_a093c2aca23b3e06788861198f409a82cf4ef2["a093c2aca23b3e06788861198f409a82cf4ef2"]
  F774_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_d50ef9360697d7adb7dcd4e55375047ad6343b["d50ef9360697d7adb7dcd4e55375047ad6343b"]
  F775_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_9ae58e375db5e8d2260d84e77c552f1f4a9814["9ae58e375db5e8d2260d84e77c552f1f4a9814"]
  F776_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\93_e3676be6296402d4c47853905238b2ce4f90b1["e3676be6296402d4c47853905238b2ce4f90b1"]
  F777_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\95_15836dfc9029c7d443ff4059b5c07ba08b7305["15836dfc9029c7d443ff4059b5c07ba08b7305"]
  F778_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\94_b559eb92afcd6778d8526bb53b84b08d885fbb["b559eb92afcd6778d8526bb53b84b08d885fbb"]
  F779_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\95_11f90ae474480ae2e086b2eeb934184a329d96["11f90ae474480ae2e086b2eeb934184a329d96"]
  F780_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\96_a619d6a56da3a09e9a57396b64caa63a31dc41["a619d6a56da3a09e9a57396b64caa63a31dc41"]
  F781_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\96_3b62e442fe8338215eb61448ae67f242fff863["3b62e442fe8338215eb61448ae67f242fff863"]
  F782_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\95_584e2bd4a233343a6e2ffc1a1a7124b24b6cc6["584e2bd4a233343a6e2ffc1a1a7124b24b6cc6"]
  F783_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\95_7e6ad3ab8c906e2c275737862cb76bb2940c0e["7e6ad3ab8c906e2c275737862cb76bb2940c0e"]
  F784_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\95_7b313f7ad0c6f1bac4ddca70f3668afd642213["7b313f7ad0c6f1bac4ddca70f3668afd642213"]
  F785_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\97_1a7449be71ec28bf30c8b848fcedb0b3a808b7["1a7449be71ec28bf30c8b848fcedb0b3a808b7"]
  F786_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\97_5614bad34606b7254d3ad1c532a6dbe0b51c8f["5614bad34606b7254d3ad1c532a6dbe0b51c8f"]
  F787_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_5632dec4b9a6d78f913ed42c1c42501c17026d["5632dec4b9a6d78f913ed42c1c42501c17026d"]
  F788_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\97_740e9a1140506d24219dec35545df4e28d8d8e["740e9a1140506d24219dec35545df4e28d8d8e"]
  F789_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_659a1d363c5e7e76807cd97af6029481254d77["659a1d363c5e7e76807cd97af6029481254d77"]
  F790_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\99_343174da0b980915c006259da33246e497ddec["343174da0b980915c006259da33246e497ddec"]
  F791_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_9b59607bc2b834a37f5a9b3a55a58acb000c99["9b59607bc2b834a37f5a9b3a55a58acb000c99"]
  F792_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_1037d0da79331b4025fbc7211d2cf90fbdd032["1037d0da79331b4025fbc7211d2cf90fbdd032"]
  F793_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_adad8fa95d857022af73877c2330761fd773d9["adad8fa95d857022af73877c2330761fd773d9"]
  F794_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\97_a37333c4aa7d992c617e40547c48beb91340de["a37333c4aa7d992c617e40547c48beb91340de"]
  F795_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\99_012e36180be91d79f511e389f8de73de699d2e["012e36180be91d79f511e389f8de73de699d2e"]
  F796_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\98_50c8c58709a45c44fbad53f4aa37051f8b3349["50c8c58709a45c44fbad53f4aa37051f8b3349"]
  F797_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9a_a6b6250aceb064d95f8d6fd57b961ed57b4eb2["a6b6250aceb064d95f8d6fd57b961ed57b4eb2"]
  F798_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9a_ba2b86d162a935cff44f72498a226cd26789b3["ba2b86d162a935cff44f72498a226cd26789b3"]
  F799_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\99_421482aabe6c778194ac158a3a3f33cc59eef6["421482aabe6c778194ac158a3a3f33cc59eef6"]
  F800_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\99_c8aca37d5f1f55e904f50ac64112951ce26897["c8aca37d5f1f55e904f50ac64112951ce26897"]
  F801_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9a_f8f6423504d7f28698aed19044fdb27051a920["f8f6423504d7f28698aed19044fdb27051a920"]
  F802_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9c_75a756880529a82ca7fbb7f75337465cdb3058["75a756880529a82ca7fbb7f75337465cdb3058"]
  F803_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9b_a12bd75e68238c233eb5d27675cc2457aae1a7["a12bd75e68238c233eb5d27675cc2457aae1a7"]
  F804_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\99_aab21bea80b481f9b76b875d4ab0b67531859b["aab21bea80b481f9b76b875d4ab0b67531859b"]
  F805_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9b_1ab0dc9aadd9bb64ae9866ebb7d4d5a49c44dd["1ab0dc9aadd9bb64ae9866ebb7d4d5a49c44dd"]
  F806_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9c_3e5089e45923d18f7749d280297360b2b9f72e["3e5089e45923d18f7749d280297360b2b9f72e"]
  F807_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9d_12aa09ac26c8282f81e693972d237e57cdfa79["12aa09ac26c8282f81e693972d237e57cdfa79"]
  F808_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9d_6f6c30e5ba51eb7e7160f338eef5392742d88a["6f6c30e5ba51eb7e7160f338eef5392742d88a"]
  F809_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9e_4eaa3b1a6878f9f5c852ac0067cf661db483f5["4eaa3b1a6878f9f5c852ac0067cf661db483f5"]
  F810_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9d_43720a9d79056e9db99402407dd21f5436729d["43720a9d79056e9db99402407dd21f5436729d"]
  F811_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9c_e1e3e3cc7335e5b0c795014cae015dc626d306["e1e3e3cc7335e5b0c795014cae015dc626d306"]
  F812_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9e_97bf03c5e9e8c533c632dab70c228d3a3abb4b["97bf03c5e9e8c533c632dab70c228d3a3abb4b"]
  F813_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9e_d1035e694b281e90cce89e101dd489582a8783["d1035e694b281e90cce89e101dd489582a8783"]
  F814_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9d_14edeeb7fe3104b922ee47d6e69da39ec11326["14edeeb7fe3104b922ee47d6e69da39ec11326"]
  F815_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_18a0d6546ae4d74a9c5c695b9124e8fe2d3c83["18a0d6546ae4d74a9c5c695b9124e8fe2d3c83"]
  F816_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9e_1bdef3ce859f6618603bbc7843d9534659cd5b["1bdef3ce859f6618603bbc7843d9534659cd5b"]
  F817_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_788f987a40770b8e6250e514c3e95b12d6f770["788f987a40770b8e6250e514c3e95b12d6f770"]
  F818_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_fb08383660dd00160bc6ca9ccb23f90b4fb14b["fb08383660dd00160bc6ca9ccb23f90b4fb14b"]
  F819_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_42c5ccf29212f8e621bdd93f42a0feefa2e5b2["42c5ccf29212f8e621bdd93f42a0feefa2e5b2"]
  F820_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_457279ef3657f74ddcac64910adac0eb93d234["457279ef3657f74ddcac64910adac0eb93d234"]
  F821_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a0_132ea2a3acad43b937de30925a47122c39e639["132ea2a3acad43b937de30925a47122c39e639"]
  F822_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_f39e39b23eec524ac5399f1d86eedbf026af79["f39e39b23eec524ac5399f1d86eedbf026af79"]
  F823_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a0_5d955546fbe7dd5df787cc0dcb1ad453b10555["5d955546fbe7dd5df787cc0dcb1ad453b10555"]
  F824_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a0_3429ca4bfede765d8d00b6121c23f2070ffc0d["3429ca4bfede765d8d00b6121c23f2070ffc0d"]
  F825_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a0_ca8901b2e497844148f56ecdc0d3d035de9ade["ca8901b2e497844148f56ecdc0d3d035de9ade"]
  F826_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\9f_7b6f44e701f53057596c4cfed8828621c19071["7b6f44e701f53057596c4cfed8828621c19071"]
  F827_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a0_f37417a8f47cfcb43e43c4665d2b9ca96dc836["f37417a8f47cfcb43e43c4665d2b9ca96dc836"]
  F828_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a1_130452eaebc5c1d8ef109d89559a5e75b7c911["130452eaebc5c1d8ef109d89559a5e75b7c911"]
  F829_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a1_63dd89e1169ef636de6d82a343965423a7df5f["63dd89e1169ef636de6d82a343965423a7df5f"]
  F830_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_07bf40cd6b20a46655f3ecde0579e65ab3f005["07bf40cd6b20a46655f3ecde0579e65ab3f005"]
  F831_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_8716e5b47ddcd78691bb93a89a906038daebe2["8716e5b47ddcd78691bb93a89a906038daebe2"]
  F832_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a1_4847ae3b3b6ff7c1c55116dd5ea3513e43dea9["4847ae3b3b6ff7c1c55116dd5ea3513e43dea9"]
  F833_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_045f5c88d696a75a465c757d6714d423591bc1["045f5c88d696a75a465c757d6714d423591bc1"]
  F834_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_93fa5b4e082caa2243a49e2ba0e96de209c943["93fa5b4e082caa2243a49e2ba0e96de209c943"]
  F835_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_ae4f44af9fac669f7168d2de25f866c0bebfd6["ae4f44af9fac669f7168d2de25f866c0bebfd6"]
  F836_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_7a3fe8afd287281c57edd4f1815f249e5047e4["7a3fe8afd287281c57edd4f1815f249e5047e4"]
  F837_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_d585c8d3661cf1e6acaf7912542f9026a9629f["d585c8d3661cf1e6acaf7912542f9026a9629f"]
  F838_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a4_6d1959dc7073daaf8823851d0681ce7ab6d825["6d1959dc7073daaf8823851d0681ce7ab6d825"]
  F839_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a3_e6009c5e1121d3be09e027a8ca676e87dccb21["e6009c5e1121d3be09e027a8ca676e87dccb21"]
  F840_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a4_7b989095b064d9460f0f94c2487b6942bd61b5["7b989095b064d9460f0f94c2487b6942bd61b5"]
  F841_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a3_f27129ab86603144edb75679e366cb246255d8["f27129ab86603144edb75679e366cb246255d8"]
  F842_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a2_b53e67425ddba55267390e25cfbc50ff562505["b53e67425ddba55267390e25cfbc50ff562505"]
  F843_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a4_d4dd58fe2239f3d55f3eeaf8d1540f0c6bf7df["d4dd58fe2239f3d55f3eeaf8d1540f0c6bf7df"]
  F844_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a3_9d0e8cd3a7a774f59f2f80de2e2b404d529008["9d0e8cd3a7a774f59f2f80de2e2b404d529008"]
  F845_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a4_014fe4a76a1885556d3d544762ffc80e4ef086["014fe4a76a1885556d3d544762ffc80e4ef086"]
  F846_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a3_3bff9c22c886c45fe9d5e5b89f07f45bd664e6["3bff9c22c886c45fe9d5e5b89f07f45bd664e6"]
  F847_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_8557e5b3632caa162f44886d922ad3097053ac["8557e5b3632caa162f44886d922ad3097053ac"]
  F848_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a4_fb054167cfef45584db6dee14da39306572694["fb054167cfef45584db6dee14da39306572694"]
  F849_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_3c86ab4f2450a2b3dee27fd07f0c93f7327718["3c86ab4f2450a2b3dee27fd07f0c93f7327718"]
  F850_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_73d0d8e92d9f2865878c227901e8c848330448["73d0d8e92d9f2865878c227901e8c848330448"]
  F851_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_420fe1a5692dffed006968434db175ff61c418["420fe1a5692dffed006968434db175ff61c418"]
  F852_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_ca61352cf9b8780b5b676da46094fef934ec69["ca61352cf9b8780b5b676da46094fef934ec69"]
  F853_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_375de1a717af268f07eb8165baeac6be9c51f6["375de1a717af268f07eb8165baeac6be9c51f6"]
  F854_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_eb1deaa6594c847c50e6754edfe5f4e07f9cab["eb1deaa6594c847c50e6754edfe5f4e07f9cab"]
  F855_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_97f621184bf2a32073c84e3728930dcaca9c72["97f621184bf2a32073c84e3728930dcaca9c72"]
  F856_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a5_0c97ffabace038e4af5491ce9d474e3a0376ca["0c97ffabace038e4af5491ce9d474e3a0376ca"]
  F857_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_ba838072423ae58351cc0337ca5d03c895be16["ba838072423ae58351cc0337ca5d03c895be16"]
  F858_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_3bfc707515d5135ef2a996c213b48d267a6dd2["3bfc707515d5135ef2a996c213b48d267a6dd2"]
  F859_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_fe09fdee0306d067b7d789bd86bbd5dd0d3f0d["fe09fdee0306d067b7d789bd86bbd5dd0d3f0d"]
  F860_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_ab89496b94964e020a6e6769d9a5c242d266b3["ab89496b94964e020a6e6769d9a5c242d266b3"]
  F861_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_f1bf6055752fec720f27658bc9acdd2c0acb4a["f1bf6055752fec720f27658bc9acdd2c0acb4a"]
  F862_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a7_4c917c302aed1f0c46761da0284e702b4214b4["4c917c302aed1f0c46761da0284e702b4214b4"]
  F863_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_a79810b83edd8dc5bfeb7271e1e3272e1dbf7b["a79810b83edd8dc5bfeb7271e1e3272e1dbf7b"]
  F864_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_e0566532993b5b83045c10590fb0682cd998ea["e0566532993b5b83045c10590fb0682cd998ea"]
  F865_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a6_0d16edfc540ec473b6aa1e938d23cfa5891eaf["0d16edfc540ec473b6aa1e938d23cfa5891eaf"]
  F866_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a7_1175d18e562feff76b0ad527c22a93e533df08["1175d18e562feff76b0ad527c22a93e533df08"]
  F867_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a8_fcd04ebb339375279a0366c70e41f332e9dc85["fcd04ebb339375279a0366c70e41f332e9dc85"]
  F868_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a7_e4c2120399e343eee100eb1174ee37fed0db29["e4c2120399e343eee100eb1174ee37fed0db29"]
  F869_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_7739f8458967ca7aab4aaadd3f887439aebab5["7739f8458967ca7aab4aaadd3f887439aebab5"]
  F870_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_5034532faebcb9dc4d05cb45ab707fd72f3ad9["5034532faebcb9dc4d05cb45ab707fd72f3ad9"]
  F871_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a8_085b1a389c09f8eed32200a4814dad7c44d141["085b1a389c09f8eed32200a4814dad7c44d141"]
  F872_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_85242b00c714444e579dfc23da612d070a5ba2["85242b00c714444e579dfc23da612d070a5ba2"]
  F873_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a8_05e40e679d5cd6cfe788cbb2353b69af1a71fa["05e40e679d5cd6cfe788cbb2353b69af1a71fa"]
  F874_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_0b0df1878b8e004b517fc48f8852db43cf3501["0b0df1878b8e004b517fc48f8852db43cf3501"]
  F875_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_28151c8d87e8267892d82132c4d4e39d0e31ac["28151c8d87e8267892d82132c4d4e39d0e31ac"]
  F876_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\a9_707357707852c6f5bd3dc351eaf42219838ad9["707357707852c6f5bd3dc351eaf42219838ad9"]
  F877_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\aa_299cf9a70d6e1c5cf3908899203b47f98b13d0["299cf9a70d6e1c5cf3908899203b47f98b13d0"]
  F878_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\aa_52ac02366210e52ef953fc77a39d6a8874755b["52ac02366210e52ef953fc77a39d6a8874755b"]
  F879_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_0936e85634c8243a36570509f86d4f70063d91["0936e85634c8243a36570509f86d4f70063d91"]
  F880_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\aa_6d6c7bd58aad04b64e271477983dde775cee03["6d6c7bd58aad04b64e271477983dde775cee03"]
  F881_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_3083307a477736c84e5cc68d4e6bb37cefd714["3083307a477736c84e5cc68d4e6bb37cefd714"]
  F882_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_4e6fee2373ab7dc524d01dfcf30737eed4b2de["4e6fee2373ab7dc524d01dfcf30737eed4b2de"]
  F883_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_7b1c2281c6142c3ad3220a0f02d3ab0a8ad40c["7b1c2281c6142c3ad3220a0f02d3ab0a8ad40c"]
  F884_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_3422c8b744cd829233689075e14deb50e3e9f8["3422c8b744cd829233689075e14deb50e3e9f8"]
  F885_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_7e377dfe146cc9169cc54acf64e6012c34301c["7e377dfe146cc9169cc54acf64e6012c34301c"]
  F886_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ab_1b04a7191f8f6f3aa3397188f534a1df6c4c0f["1b04a7191f8f6f3aa3397188f534a1df6c4c0f"]
  F887_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_4104c24f98336761dda10e93e7b9f77462039a["4104c24f98336761dda10e93e7b9f77462039a"]
  F888_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_ddae5738ec89267d493b335b51fa4b60da7b4a["ddae5738ec89267d493b335b51fa4b60da7b4a"]
  F889_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_e7b5485d9d517d3c460d486c283fb4a285a8e4["e7b5485d9d517d3c460d486c283fb4a285a8e4"]
  F890_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_8267aa0f97b0dff52658294467d8dbaed39a55["8267aa0f97b0dff52658294467d8dbaed39a55"]
  F891_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ad_2bb8b91780036fc4eed8deec41f55833db5b52["2bb8b91780036fc4eed8deec41f55833db5b52"]
  F892_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_797dc646f0baf557fed9a86dec3411c06401fc["797dc646f0baf557fed9a86dec3411c06401fc"]
  F893_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_a47cac6ff8740f383abd3bb1118fca2a4e575c["a47cac6ff8740f383abd3bb1118fca2a4e575c"]
  F894_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ad_3ba4037d1c03f9072ac8bf2de7b253cf3fc232["3ba4037d1c03f9072ac8bf2de7b253cf3fc232"]
  F895_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ad_7909d3aa774ab96b932457a1f36f4b6f0fb85d["7909d3aa774ab96b932457a1f36f4b6f0fb85d"]
  F896_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ac_de3c61f1ec955003bca6a736a26858102b4a24["de3c61f1ec955003bca6a736a26858102b4a24"]
  F897_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_1af7f3b22bf4b6ffe39f88c2c80f746b6b1c9f["1af7f3b22bf4b6ffe39f88c2c80f746b6b1c9f"]
  F898_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_144f3247b40a2f31e50cab5cb6b213f24b1ef7["144f3247b40a2f31e50cab5cb6b213f24b1ef7"]
  F899_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_6f1601ce5cbc244d6b379b3cc361efa138c95a["6f1601ce5cbc244d6b379b3cc361efa138c95a"]
  F900_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ad_cc9b69e1c5ffa67602499e92cd8bc7269400de["cc9b69e1c5ffa67602499e92cd8bc7269400de"]
  F901_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_360bfbea768635f639bc7a44ba3f34dfd559b2["360bfbea768635f639bc7a44ba3f34dfd559b2"]
  F902_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_085a688cc1e4b50c4ef2b3fa5333514787b435["085a688cc1e4b50c4ef2b3fa5333514787b435"]
  F903_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_f035f3368dbf095bfff3f7879c75303feef40b["f035f3368dbf095bfff3f7879c75303feef40b"]
  F904_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_940fbb6210219779f9c36ff7cf6541ab8a8206["940fbb6210219779f9c36ff7cf6541ab8a8206"]
  F905_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ae_d8b82f58e05cdfe8d79ec83702c1f858c9ebdb["d8b82f58e05cdfe8d79ec83702c1f858c9ebdb"]
  F906_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ad_d36f414603c4b8fe9f3f9fe4724fbadc13782d["d36f414603c4b8fe9f3f9fe4724fbadc13782d"]
  F907_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_68e9ce1bf6d42c39b7c19aa4dbce6bb58c196d["68e9ce1bf6d42c39b7c19aa4dbce6bb58c196d"]
  F908_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_2997cfed592da306b8c7f453948e41bfbfbf04["2997cfed592da306b8c7f453948e41bfbfbf04"]
  F909_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_1459fecde46ebb00223be5af68a25cc5da4f3e["1459fecde46ebb00223be5af68a25cc5da4f3e"]
  F910_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_7588b7653d3099231c1182f52b983f42e7f944["7588b7653d3099231c1182f52b983f42e7f944"]
  F911_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_7663df1fb4ee2d98fc2cbfb6b65d982eb112ac["7663df1fb4ee2d98fc2cbfb6b65d982eb112ac"]
  F912_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_e8fcd8ddbc283767ab7af5bd3550a987fe6762["e8fcd8ddbc283767ab7af5bd3550a987fe6762"]
  F913_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_cd95aea770cbba57e6629f352057363afe7e29["cd95aea770cbba57e6629f352057363afe7e29"]
  F914_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_4282222642e36ade8bc8e943a9c757595007a8["4282222642e36ade8bc8e943a9c757595007a8"]
  F915_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\af_bd4c4d0b687eea4afc1b10f4ae7255188e2d0b["bd4c4d0b687eea4afc1b10f4ae7255188e2d0b"]
  F916_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_7c14355bcc3c73248b60fe2dc2a268fd40b1ae["7c14355bcc3c73248b60fe2dc2a268fd40b1ae"]
  F917_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b0_c63d0574d15cca90fced75e610450295920c15["c63d0574d15cca90fced75e610450295920c15"]
  F918_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b2_03fe96eb8332a94f3262ec6b5a3b753a3565b2["03fe96eb8332a94f3262ec6b5a3b753a3565b2"]
  F919_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b1_14690aa3159a7009762e6148785f5d6ddc36f6["14690aa3159a7009762e6148785f5d6ddc36f6"]
  F920_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b2_636656ab76e4892058c602adaca489df45ef16["636656ab76e4892058c602adaca489df45ef16"]
  F921_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b1_096fcef9a05d21329e88d4ddba2abdda98ad02["096fcef9a05d21329e88d4ddba2abdda98ad02"]
  F922_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b1_baba0fb2dd2fce712a0acba63388f1b0178d0f["baba0fb2dd2fce712a0acba63388f1b0178d0f"]
  F923_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b2_b066bc3621b7ff36a95645c1d319094a99966c["b066bc3621b7ff36a95645c1d319094a99966c"]
  F924_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_089f9eb2350e927210a062f843af2fb5fec4a2["089f9eb2350e927210a062f843af2fb5fec4a2"]
  F925_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b2_d85dbd62418546f6fbed4b9c43eb975b31625e["d85dbd62418546f6fbed4b9c43eb975b31625e"]
  F926_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b2_15784daef3ce7f7d1561c91b030e2e29f4260a["15784daef3ce7f7d1561c91b030e2e29f4260a"]
  F927_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_413ee82ef6869a1c9305dcd7631f9777ecce27["413ee82ef6869a1c9305dcd7631f9777ecce27"]
  F928_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_703263b2f6c6a433e09434eface4b674be8fb7["703263b2f6c6a433e09434eface4b674be8fb7"]
  F929_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_9e6fb7b072696d8a039fc05584b72cd4278de3["9e6fb7b072696d8a039fc05584b72cd4278de3"]
  F930_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b4_9284b9f22e26ae0732d0e07fb35251e0c66b0f["9284b9f22e26ae0732d0e07fb35251e0c66b0f"]
  F931_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b4_f772bc520c3256412e398ca6d567bb324572fc["f772bc520c3256412e398ca6d567bb324572fc"]
  F932_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b4_5d8608d6f574723d52fa230ce69f8fd9960247["5d8608d6f574723d52fa230ce69f8fd9960247"]
  F933_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_e81f515b6d8d85c94f4882ab3f60aff8ca8cee["e81f515b6d8d85c94f4882ab3f60aff8ca8cee"]
  F934_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b4_9db9fc97cda7cbb46ca8bb6b6f084ac019ccd3["9db9fc97cda7cbb46ca8bb6b6f084ac019ccd3"]
  F935_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_bd34c83e90f5bdb3feb7546be9924bbdddda84["bd34c83e90f5bdb3feb7546be9924bbdddda84"]
  F936_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b3_a7e1c72bb2856cebd16ad8af3faaaf1a148838["a7e1c72bb2856cebd16ad8af3faaaf1a148838"]
  F937_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_2a9afdf7e6455abd342d759b6ef81e5e7a7990["2a9afdf7e6455abd342d759b6ef81e5e7a7990"]
  F938_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_aafe159050269044a4474c5fb589b2913eb979["aafe159050269044a4474c5fb589b2913eb979"]
  F939_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_33aa412f9d390c34cbf621c287a3e90b3e6b84["33aa412f9d390c34cbf621c287a3e90b3e6b84"]
  F940_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_e41840218df5b94d2c0f5855e50d92fcbb5f57["e41840218df5b94d2c0f5855e50d92fcbb5f57"]
  F941_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_b1608a544b0b9401c1f2501522c7697afe754e["b1608a544b0b9401c1f2501522c7697afe754e"]
  F942_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_3ce617493b070bdc2847790f81aefb37760d5d["3ce617493b070bdc2847790f81aefb37760d5d"]
  F943_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b6_51015701f5202f1c09b75ef488ce0e0eb74706["51015701f5202f1c09b75ef488ce0e0eb74706"]
  F944_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_a8dbf4c3f038ddf157f9212416a437fb5b1cca["a8dbf4c3f038ddf157f9212416a437fb5b1cca"]
  F945_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_9866b6b07f71d0e9a97dbea7219f16b170b387["9866b6b07f71d0e9a97dbea7219f16b170b387"]
  F946_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b5_56dc9a5732b8f0005b4067a848c94a36902f11["56dc9a5732b8f0005b4067a848c94a36902f11"]
  F947_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b6_85c565e4fa76d887face37479d49dd1206a29a["85c565e4fa76d887face37479d49dd1206a29a"]
  F948_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b6_daec9e1ecc69d7f7ed783226ac909757f3dd3f["daec9e1ecc69d7f7ed783226ac909757f3dd3f"]
  F949_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_4b89aa259a4ee943464e8afc2222a44c031758["4b89aa259a4ee943464e8afc2222a44c031758"]
  F950_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b6_ac0fb0387871f9f24aaa4592307489c4bafd34["ac0fb0387871f9f24aaa4592307489c4bafd34"]
  F951_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b6_7865c76028751d1529668b03b5a56de9a838df["7865c76028751d1529668b03b5a56de9a838df"]
  F952_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_60615403031604be8d6e7b8c76508b9444f25c["60615403031604be8d6e7b8c76508b9444f25c"]
  F953_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_e00434b8154122a9a3e453a317a05a43268a32["e00434b8154122a9a3e453a317a05a43268a32"]
  F954_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_75cad63a511bc96f596bd79ff034af366b2ddf["75cad63a511bc96f596bd79ff034af366b2ddf"]
  F955_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_ef84067dfa97be757252e16b68587e8082a36d["ef84067dfa97be757252e16b68587e8082a36d"]
  F956_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b7_bb197a9a685ab478d76fd365b7885a51c26a11["bb197a9a685ab478d76fd365b7885a51c26a11"]
  F957_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b8_230f774183eadf3256bed15af52cf273ca4f57["230f774183eadf3256bed15af52cf273ca4f57"]
  F958_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b8_3d360701d4bcd204c9323f1125ae41ff167a16["3d360701d4bcd204c9323f1125ae41ff167a16"]
  F959_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b8_0d90618c0a63cfd59b66768bf08699b134f863["0d90618c0a63cfd59b66768bf08699b134f863"]
  F960_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_b8262b405f3363cedf6fda4c57415209b857b1["b8262b405f3363cedf6fda4c57415209b857b1"]
  F961_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_633fa5a258a04017062ec611fd267b16c9f590["633fa5a258a04017062ec611fd267b16c9f590"]
  F962_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_65830831e412ddc1a46f6d97042a45d98903df["65830831e412ddc1a46f6d97042a45d98903df"]
  F963_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_be0cb7d8769ab9219cb88d94b6a448e3751d07["be0cb7d8769ab9219cb88d94b6a448e3751d07"]
  F964_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b8_c32f0a3e3816aef1197955804eceb9655fd287["c32f0a3e3816aef1197955804eceb9655fd287"]
  F965_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b8_c57513f1469cb00674800bcde362ff8efc6ff4["c57513f1469cb00674800bcde362ff8efc6ff4"]
  F966_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_5a7a876db231f560f2863505a304a58e064ba8["5a7a876db231f560f2863505a304a58e064ba8"]
  F967_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\b9_e6d6593b987328e0a2bf1d45e8a590ead61998["e6d6593b987328e0a2bf1d45e8a590ead61998"]
  F968_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_abbdb44fd4259a605ca4c7ba83306ac71b4c03["abbdb44fd4259a605ca4c7ba83306ac71b4c03"]
  F969_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_5b580389fa21914b7e419c94e4387bdf5e3d15["5b580389fa21914b7e419c94e4387bdf5e3d15"]
  F970_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_6e480e9407ec4760080c5ff39aec95463c6cb9["6e480e9407ec4760080c5ff39aec95463c6cb9"]
  F971_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_5504cf8238dcbb645f7ffc625509e30244a861["5504cf8238dcbb645f7ffc625509e30244a861"]
  F972_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_49f0da49a412a65c89731799ab8c5f90c3d548["49f0da49a412a65c89731799ab8c5f90c3d548"]
  F973_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_d900d7a38bccdf2a6c2014fe6a30740a991145["d900d7a38bccdf2a6c2014fe6a30740a991145"]
  F974_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_2abec79a8898e68f47e0bd124ef20e59e5c6ee["2abec79a8898e68f47e0bd124ef20e59e5c6ee"]
  F975_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_513720d83203b6c0fce266ed6123e63e1596d5["513720d83203b6c0fce266ed6123e63e1596d5"]
  F976_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ba_39dc0798c3693ad3cdcab17a27a491e5b31fa8["39dc0798c3693ad3cdcab17a27a491e5b31fa8"]
  F977_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bc_728c54f32117ea3e8001cfa0ca97a7812960cd["728c54f32117ea3e8001cfa0ca97a7812960cd"]
  F978_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_b56ff264ee7b3e4dc3eb272fb66fabbfe8e9e8["b56ff264ee7b3e4dc3eb272fb66fabbfe8e9e8"]
  F979_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_81c7734290730aa5ce26917f759a6301261224["81c7734290730aa5ce26917f759a6301261224"]
  F980_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_183ff215fbb211fe5ca8eb9184f3d5f3b08229["183ff215fbb211fe5ca8eb9184f3d5f3b08229"]
  F981_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_a2f1c43c40b61ac3da77a2978d6b6ad9ab9ab6["a2f1c43c40b61ac3da77a2978d6b6ad9ab9ab6"]
  F982_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_1fcba8c5ebab9ca0dff06d49555c5a4a236df8["1fcba8c5ebab9ca0dff06d49555c5a4a236df8"]
  F983_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bc_122a0954d8b10ab90b19ee7790dbf34c625948["122a0954d8b10ab90b19ee7790dbf34c625948"]
  F984_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bc_f4d4bb1641ca8e75f01217a48ebb51d096499e["f4d4bb1641ca8e75f01217a48ebb51d096499e"]
  F985_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_27cf32698763a0fb9a0db51bcbbb6ac38cdba4["27cf32698763a0fb9a0db51bcbbb6ac38cdba4"]
  F986_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bb_99b3511c7ded78a75ef07505261ef418452e25["99b3511c7ded78a75ef07505261ef418452e25"]
  F987_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_19a8bf66e96370ecabc16c7f1f58ca4e30b972["19a8bf66e96370ecabc16c7f1f58ca4e30b972"]
  F988_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_cdd6c55b51c80666eba38f8ba5b8e5889b1caf["cdd6c55b51c80666eba38f8ba5b8e5889b1caf"]
  F989_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_8896bf2217b46faa0291585e01ac1a3441a958["8896bf2217b46faa0291585e01ac1a3441a958"]
  F990_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_5965dd6732231551c5760dbbafa4efcbdf82dc["5965dd6732231551c5760dbbafa4efcbdf82dc"]
  F991_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_46e351d767fed7b89048137ba365bdc41db5ad["46e351d767fed7b89048137ba365bdc41db5ad"]
  F992_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_1bb8c11ceddf23702dc1e03b56e7d7f8a8df8d["1bb8c11ceddf23702dc1e03b56e7d7f8a8df8d"]
  F993_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_98fc1d33ac34ea07f6f7e43815d77f0ce2b592["98fc1d33ac34ea07f6f7e43815d77f0ce2b592"]
  F994_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_463e2b8348067f4a2adab274e96d91e8ddcc4a["463e2b8348067f4a2adab274e96d91e8ddcc4a"]
  F995_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bd_d03976707c97abc9a1cf00cacd8db9961dc2d4["d03976707c97abc9a1cf00cacd8db9961dc2d4"]
  F996_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_04a0653be56f1c0ce86d9822bd2761c2198ca0["04a0653be56f1c0ce86d9822bd2761c2198ca0"]
  F997_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_574c00007901cc48eccd0871ef9a40fd857308["574c00007901cc48eccd0871ef9a40fd857308"]
  F998_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bf_2397a8a891e9d19ddf03d651f5ec8c046cacb6["2397a8a891e9d19ddf03d651f5ec8c046cacb6"]
  F999_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_a61e56a5fbda41d492d5b20cd907ce77c2146b["a61e56a5fbda41d492d5b20cd907ce77c2146b"]
  F1000_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_3971e54d85a4dac5d630372688ee15edbe0d3c["3971e54d85a4dac5d630372688ee15edbe0d3c"]
  F1001_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bf_632153b38806f0958ac96301dacb370228639c["632153b38806f0958ac96301dacb370228639c"]
  F1002_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_3b81af75389cbc214f41bcaa282c21c1dc8786["3b81af75389cbc214f41bcaa282c21c1dc8786"]
  F1003_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\be_d47ee730e67cb553e5e1dd3735b37c53ec08e5["d47ee730e67cb553e5e1dd3735b37c53ec08e5"]
  F1004_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_15402ccbeacf622c9087a9317d205f3fdc6f70["15402ccbeacf622c9087a9317d205f3fdc6f70"]
  F1005_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_a3797972df8c5e4402681b6e9c01c92f9f5631["a3797972df8c5e4402681b6e9c01c92f9f5631"]
  F1006_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\bf_f5ddca7b8a382974eeb30da358cd1793b958c5["f5ddca7b8a382974eeb30da358cd1793b958c5"]
  F1007_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_0dbf391a5d707ca633823ab79201abe42e4176["0dbf391a5d707ca633823ab79201abe42e4176"]
  F1008_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_fd23ab0e73e61d3bfc0e3f2dba40fdfb8fe80c["fd23ab0e73e61d3bfc0e3f2dba40fdfb8fe80c"]
  F1009_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_c430d8d07c8159ed9968d510e1f7209de761f6["c430d8d07c8159ed9968d510e1f7209de761f6"]
  F1010_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_287423ffd44538b741684a95d3c4e6b267e169["287423ffd44538b741684a95d3c4e6b267e169"]
  F1011_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_4d1993baf7736beebf2d84b0fc8c377c628c57["4d1993baf7736beebf2d84b0fc8c377c628c57"]
  F1012_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_7f2d8bb599444b815e374d1be6b59b5e10675f["7f2d8bb599444b815e374d1be6b59b5e10675f"]
  F1013_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_14285e1ae957aaf49e8f31ad117d47c1dbad2b["14285e1ae957aaf49e8f31ad117d47c1dbad2b"]
  F1014_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_9f882314b13aaa1bae6083d62252573c236fe9["9f882314b13aaa1bae6083d62252573c236fe9"]
  F1015_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_a83848cce17199c852577689c82d4ed62edcf8["a83848cce17199c852577689c82d4ed62edcf8"]
  F1016_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c0_e3156751f25965201e7e78196e139e62180bbd["e3156751f25965201e7e78196e139e62180bbd"]
  F1017_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c2_5aac8178b8ff47b61fdcdce389d2d102f84231["5aac8178b8ff47b61fdcdce389d2d102f84231"]
  F1018_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c1_a96fbaa80d03d18b40d167485d4d3613043e5d["a96fbaa80d03d18b40d167485d4d3613043e5d"]
  F1019_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_8c92a01c6867f41c542e4054f9a91e9591ffa0["8c92a01c6867f41c542e4054f9a91e9591ffa0"]
  F1020_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c2_72a2c3c23b04946438dc450b1f91bb8759a4fd["72a2c3c23b04946438dc450b1f91bb8759a4fd"]
  F1021_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c2_5af2e47523b6af938ab1acac919c1995345680["5af2e47523b6af938ab1acac919c1995345680"]
  F1022_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c2_a6f6d1eda69941120183d1fffd941d0639bf2a["a6f6d1eda69941120183d1fffd941d0639bf2a"]
  F1023_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_9a81961e98421911688882bef05a750c654723["9a81961e98421911688882bef05a750c654723"]
  F1024_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_139b15bd0fd36f37aa14d80038ae88869233f4["139b15bd0fd36f37aa14d80038ae88869233f4"]
  F1025_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_60a9ba42b2bdf7d61c01f11fea96c3bf262a8f["60a9ba42b2bdf7d61c01f11fea96c3bf262a8f"]
  F1026_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_dc020845bf5dfb4a90210b8e78e45da3746b02["dc020845bf5dfb4a90210b8e78e45da3746b02"]
  F1027_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_0019db29e656a2d6342c19654da766d6f0235e["0019db29e656a2d6342c19654da766d6f0235e"]
  F1028_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_2972ffac52ca8fb313fb50f49a62c1f4639d31["2972ffac52ca8fb313fb50f49a62c1f4639d31"]
  F1029_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_2a8931ed84bdd40b2b7dce083803535ac0ad06["2a8931ed84bdd40b2b7dce083803535ac0ad06"]
  F1030_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_f4686d47572762fcbe40ef282dda84d315c632["f4686d47572762fcbe40ef282dda84d315c632"]
  F1031_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c5_95b0093af6d10847a5d6a8ab7507698021e66c["95b0093af6d10847a5d6a8ab7507698021e66c"]
  F1032_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_fc39b239cffb05dcf1679b8de5adacd1d466e1["fc39b239cffb05dcf1679b8de5adacd1d466e1"]
  F1033_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_ca2b61e42ab74f82f59471e39ca13ac507e39c["ca2b61e42ab74f82f59471e39ca13ac507e39c"]
  F1034_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c4_83f473ae45417f78372c174aba804067887cab["83f473ae45417f78372c174aba804067887cab"]
  F1035_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c3_fcb37ff6dbde50b0e42fbd89c4b0bee7a36aff["fcb37ff6dbde50b0e42fbd89c4b0bee7a36aff"]
  F1036_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c5_9ba961ec182405e191089a17ab0e367e67df03["9ba961ec182405e191089a17ab0e367e67df03"]
  F1037_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c5_a34b0252a8e87329435a5a0167fec7311762ea["a34b0252a8e87329435a5a0167fec7311762ea"]
  F1038_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_68db0d116b0e8a5ebfbc9702a1a3cf4e6ea6ea["68db0d116b0e8a5ebfbc9702a1a3cf4e6ea6ea"]
  F1039_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c7_81e31f146d653979d5b8bd78c5252204d230a7["81e31f146d653979d5b8bd78c5252204d230a7"]
  F1040_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c5_d5c5a4bd550f2a83587dc30a48f31939562950["d5c5a4bd550f2a83587dc30a48f31939562950"]
  F1041_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_47d2b36ba5576e5127293ddec5e52d0ed123b7["47d2b36ba5576e5127293ddec5e52d0ed123b7"]
  F1042_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_6d6cc566f21d992f9fde6b4d9ae7a27a900b12["6d6cc566f21d992f9fde6b4d9ae7a27a900b12"]
  F1043_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_eba5e64920b8e89b80806a5b62cbc1a6049abd["eba5e64920b8e89b80806a5b62cbc1a6049abd"]
  F1044_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c7_0b731a84fb63bbd882c4baf191db249ff81d07["0b731a84fb63bbd882c4baf191db249ff81d07"]
  F1045_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_f3627039d569d66286591884f1f1b504248c4a["f3627039d569d66286591884f1f1b504248c4a"]
  F1046_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c6_4efb06751984ea64bd945d4bdf4ef3da245c4c["4efb06751984ea64bd945d4bdf4ef3da245c4c"]
  F1047_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c7_f5ded2c98627373dc7856c92fde596e64d7ffc["f5ded2c98627373dc7856c92fde596e64d7ffc"]
  F1048_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c8_85e904740bd690e0f9b1835530138c712f3299["85e904740bd690e0f9b1835530138c712f3299"]
  F1049_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c8_4faa741946a2e01891b3f603044ae5355910d3["4faa741946a2e01891b3f603044ae5355910d3"]
  F1050_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c9_88f6614e28028fad4591bf3fe72e41cb0b2de0["88f6614e28028fad4591bf3fe72e41cb0b2de0"]
  F1051_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c9_7100dc6b923a717d26ebfefa4a5b8f14b9ab5d["7100dc6b923a717d26ebfefa4a5b8f14b9ab5d"]
  F1052_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c8_4204fd2103d553daf5f4cc471952d4063c2ec4["4204fd2103d553daf5f4cc471952d4063c2ec4"]
  F1053_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c9_1219ea2ba535f2e91c9cfeeef1a442eacd4ad1["1219ea2ba535f2e91c9cfeeef1a442eacd4ad1"]
  F1054_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c8_90b4ee31d23d02d0a0031295ead84f5b21c578["90b4ee31d23d02d0a0031295ead84f5b21c578"]
  F1055_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c7_b86631f2a21400608a8bed7223e127714dfa26["b86631f2a21400608a8bed7223e127714dfa26"]
  F1056_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c8_0a6a57f6a7a2af25b2d0b805f90731b2705b28["0a6a57f6a7a2af25b2d0b805f90731b2705b28"]
  F1057_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ca_a8794e6ea7e1e38bee6b916c9bbe4cc8fc1ab8["a8794e6ea7e1e38bee6b916c9bbe4cc8fc1ab8"]
  F1058_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c9_f0fe4cf53b9c532477990b25c04e989f037d25["f0fe4cf53b9c532477990b25c04e989f037d25"]
  F1059_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ca_b4172328d590fade6f3295ccd83f5db01703ea["b4172328d590fade6f3295ccd83f5db01703ea"]
  F1060_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\c9_a75abd0258cc9facc10dd6fc805ff8023a1ce4["a75abd0258cc9facc10dd6fc805ff8023a1ce4"]
  F1061_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cb_06ed958be4073c5adf1d66a6124a824f5bc3a3["06ed958be4073c5adf1d66a6124a824f5bc3a3"]
  F1062_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ca_d01ed855869d2611838e4e9c9d15cf7b1afb49["d01ed855869d2611838e4e9c9d15cf7b1afb49"]
  F1063_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ca_c9e39a2549937cba182cf3348be33d6c62caf4["c9e39a2549937cba182cf3348be33d6c62caf4"]
  F1064_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ca_8ae71b707255270bfd102c15724957ea2e5708["8ae71b707255270bfd102c15724957ea2e5708"]
  F1065_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cb_daa55ad3282470f3ff0dc92705d8dc0ce65916["daa55ad3282470f3ff0dc92705d8dc0ce65916"]
  F1066_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cb_46710c153ce5ecec980118310f77be00d18eac["46710c153ce5ecec980118310f77be00d18eac"]
  F1067_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_28737f91429db7cbb14ae379523cbf5a4b2bb1["28737f91429db7cbb14ae379523cbf5a4b2bb1"]
  F1068_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_b4e50af93e46e22635b551ee623abe83f5d9d0["b4e50af93e46e22635b551ee623abe83f5d9d0"]
  F1069_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_770d14371fa496dbd86229021f318227fad044["770d14371fa496dbd86229021f318227fad044"]
  F1070_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_b8ab0e19b5fa716a7c385179b1c2859180e660["b8ab0e19b5fa716a7c385179b1c2859180e660"]
  F1071_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_96c3943536df42b1b3e084a794feced9a4fc34["96c3943536df42b1b3e084a794feced9a4fc34"]
  F1072_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_4d91cf7455e1481fbdc59b4402b9a75ea34ff6["4d91cf7455e1481fbdc59b4402b9a75ea34ff6"]
  F1073_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_598800d3ef1c349f3277acec679fc43824866a["598800d3ef1c349f3277acec679fc43824866a"]
  F1074_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cc_1df6639f3ff191af04870a5b7f65d9c1af814d["1df6639f3ff191af04870a5b7f65d9c1af814d"]
  F1075_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_4eaa3f33ed2c52c6873f72cb8cf41d2bd45af4["4eaa3f33ed2c52c6873f72cb8cf41d2bd45af4"]
  F1076_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_6fbc2210cefce8d3076c7dd4784d77469c51fb["6fbc2210cefce8d3076c7dd4784d77469c51fb"]
  F1077_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ce_148d04a8d5ceca149eee5e62be080cb34c7f55["148d04a8d5ceca149eee5e62be080cb34c7f55"]
  F1078_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_f5e4b07660db8425fb7a7f63e847483937f406["f5e4b07660db8425fb7a7f63e847483937f406"]
  F1079_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_8dba77db14f68befe9dbc1c598457c206060f1["8dba77db14f68befe9dbc1c598457c206060f1"]
  F1080_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_36c8197a81a621565ec7326b4d59320fa66956["36c8197a81a621565ec7326b4d59320fa66956"]
  F1081_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_37d71efd5054fd58c30ae94deccb6d754f95ad["37d71efd5054fd58c30ae94deccb6d754f95ad"]
  F1082_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cd_b219ff1a940b395fa20e144f71a4217ef8a76e["b219ff1a940b395fa20e144f71a4217ef8a76e"]
  F1083_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_7aedbe4bfd86bda9e41fb1f0e175704af520fe["7aedbe4bfd86bda9e41fb1f0e175704af520fe"]
  F1084_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_3b2a005e8c726ad923c14c81a62082b1e37d32["3b2a005e8c726ad923c14c81a62082b1e37d32"]
  F1085_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_c76cf5b353c007dd42fb7d3faf3fe837cbdddb["c76cf5b353c007dd42fb7d3faf3fe837cbdddb"]
  F1086_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\cf_afd1ec27ef9c1dec9e5a1010006c5678971b9e["afd1ec27ef9c1dec9e5a1010006c5678971b9e"]
  F1087_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_ee83c245de10031e79c38bee6633c1c21ffa9e["ee83c245de10031e79c38bee6633c1c21ffa9e"]
  F1088_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_1d7d33767c75effec54eb8e1e908b5140956e9["1d7d33767c75effec54eb8e1e908b5140956e9"]
  F1089_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_0992da6f98def1ca766c62cdef04ef94d113c6["0992da6f98def1ca766c62cdef04ef94d113c6"]
  F1090_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_47140b896de872f71492f772ac4025d3dc8244["47140b896de872f71492f772ac4025d3dc8244"]
  F1091_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_216912abc693c0e0f1ed687eb7e0bcc69b7e6a["216912abc693c0e0f1ed687eb7e0bcc69b7e6a"]
  F1092_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_0e227467cb433dbed10d56aca6bc2b34c7b6f8["0e227467cb433dbed10d56aca6bc2b34c7b6f8"]
  F1093_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_7770ec8629904ceb9c50418d45f535aa98232f["7770ec8629904ceb9c50418d45f535aa98232f"]
  F1094_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_8a4424a1c36af28a6e24006c98f5455a77c89a["8a4424a1c36af28a6e24006c98f5455a77c89a"]
  F1095_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_b30c97dd7d7422e4f23463d6c9eee211230253["b30c97dd7d7422e4f23463d6c9eee211230253"]
  F1096_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d0_30140096917d7efba6fe9fa0d1b831820eaf8a["30140096917d7efba6fe9fa0d1b831820eaf8a"]
  F1097_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_defedbc6f675d1136e3f8f8805127829657e97["defedbc6f675d1136e3f8f8805127829657e97"]
  F1098_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_5dc20e6ae8705eb1767944edb4ffeef2823ba9["5dc20e6ae8705eb1767944edb4ffeef2823ba9"]
  F1099_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_ff6eebca281b2639b01507999e2abf53b7a618["ff6eebca281b2639b01507999e2abf53b7a618"]
  F1100_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_44e96f1dcf30af7624357e71d5b02b94b1e522["44e96f1dcf30af7624357e71d5b02b94b1e522"]
  F1101_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_8c786bcc2715f061b8ab59bcc3301fdedff2e0["8c786bcc2715f061b8ab59bcc3301fdedff2e0"]
  F1102_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_a18a70caf6fe97c3594adb1ce2d0d5591a9a4f["a18a70caf6fe97c3594adb1ce2d0d5591a9a4f"]
  F1103_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_527d36d027608722865ed6f6c463f893b153bf["527d36d027608722865ed6f6c463f893b153bf"]
  F1104_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d1_f990b323e1198266f1247e675a2f2d4e8bfcb4["f990b323e1198266f1247e675a2f2d4e8bfcb4"]
  F1105_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_5b29c4552f75d77e35e78eb844aa80c79f6a64["5b29c4552f75d77e35e78eb844aa80c79f6a64"]
  F1106_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_375471949ae2f6c5fcab4bcd94ac62d0f2b200["375471949ae2f6c5fcab4bcd94ac62d0f2b200"]
  F1107_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_6e2137fec25b9af47a5d64a09c9161dbfd5ba6["6e2137fec25b9af47a5d64a09c9161dbfd5ba6"]
  F1108_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_983b42852da44cd586764f1ccfcbba71d15795["983b42852da44cd586764f1ccfcbba71d15795"]
  F1109_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_4278a913e06ec9fccb9e3facd48fc5bd213abf["4278a913e06ec9fccb9e3facd48fc5bd213abf"]
  F1110_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_9e89e6e9b7549452bf51bcee863d8d78b30607["9e89e6e9b7549452bf51bcee863d8d78b30607"]
  F1111_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_8bd9243a76002419e95f63e568c873ed3a9b90["8bd9243a76002419e95f63e568c873ed3a9b90"]
  F1112_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_0b224715f50e3365f29e4e1493b4ee733cfdd7["0b224715f50e3365f29e4e1493b4ee733cfdd7"]
  F1113_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_d9e38d3a47385244fd24f3193d8eb38e09eb60["d9e38d3a47385244fd24f3193d8eb38e09eb60"]
  F1114_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_50061375809f87dc1a326f7782360bfddd393b["50061375809f87dc1a326f7782360bfddd393b"]
  F1115_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d2_6384e00fa807ce60970e1a1faa721cb9142f53["6384e00fa807ce60970e1a1faa721cb9142f53"]
  F1116_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_5f7873be98a46fec3e277819116c9009a35ce7["5f7873be98a46fec3e277819116c9009a35ce7"]
  F1117_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_e8844c5d770a5c5cdd1509d849187110a5ec54["e8844c5d770a5c5cdd1509d849187110a5ec54"]
  F1118_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_76e20080eb8808c7fe699ab98a8c8bff59d3bd["76e20080eb8808c7fe699ab98a8c8bff59d3bd"]
  F1119_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_32f5ee84b8c3ab9fa1ef93e0cbbc98471884dc["32f5ee84b8c3ab9fa1ef93e0cbbc98471884dc"]
  F1120_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_feb4df5292bddef0aafdafb7965d4811e17803["feb4df5292bddef0aafdafb7965d4811e17803"]
  F1121_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_55eef34c46c21a4b3b3ccefff34b2cc0c33615["55eef34c46c21a4b3b3ccefff34b2cc0c33615"]
  F1122_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_ee75ab930662a4bd77925dd8ad0b3f378abf1c["ee75ab930662a4bd77925dd8ad0b3f378abf1c"]
  F1123_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_84c7b37be705f621ff06d170705cd8f90c3858["84c7b37be705f621ff06d170705cd8f90c3858"]
  F1124_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d5_00123564ecab5429625ed9fcdb844de380b84b["00123564ecab5429625ed9fcdb844de380b84b"]
  F1125_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d3_ad538c48a9281be2af0e118650d255a6fe8a38["ad538c48a9281be2af0e118650d255a6fe8a38"]
  F1126_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d4_4e551fdb2280f6d25821d643d7892adfa37f80["4e551fdb2280f6d25821d643d7892adfa37f80"]
  F1127_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d5_c45e4c0d75315d02b4084f0031f2c1c14f13e4["c45e4c0d75315d02b4084f0031f2c1c14f13e4"]
  F1128_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d5_ff9644fc5b2514780a79f820607af1f51d5085["ff9644fc5b2514780a79f820607af1f51d5085"]
  F1129_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_1938b519484e405f64c5164dd8ee64a55e989a["1938b519484e405f64c5164dd8ee64a55e989a"]
  F1130_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_2f7ad1d5e5307f3d32deb1c7b6d8c551fb4564["2f7ad1d5e5307f3d32deb1c7b6d8c551fb4564"]
  F1131_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d5_a034bb8fa7e9b9373cc4e8e365b2e33e491595["a034bb8fa7e9b9373cc4e8e365b2e33e491595"]
  F1132_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d5_78d0222e4815b080183c86a78d16a31b405634["78d0222e4815b080183c86a78d16a31b405634"]
  F1133_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d6_72a69c31ec325632dcc4b33b4719b86fe47d38["72a69c31ec325632dcc4b33b4719b86fe47d38"]
  F1134_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d6_b0c4b6f1199857ad52aecdc2a25e208490d001["b0c4b6f1199857ad52aecdc2a25e208490d001"]
  F1135_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d6_ea21f4bffdcc4e4662256cde3f61cf2e21c4ce["ea21f4bffdcc4e4662256cde3f61cf2e21c4ce"]
  F1136_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d6_6b94f1d305c137d7f02fbb01d83d4b634a1ee0["6b94f1d305c137d7f02fbb01d83d4b634a1ee0"]
  F1137_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_824d297209f1d3435c9e8e121358273af7bd8f["824d297209f1d3435c9e8e121358273af7bd8f"]
  F1138_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_691795806270ff85d991f728b5d799d522d2bc["691795806270ff85d991f728b5d799d522d2bc"]
  F1139_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_19fc1c6f8a7d5ab25b0f0611c72da0880ec34a["19fc1c6f8a7d5ab25b0f0611c72da0880ec34a"]
  F1140_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_44b9d6581ece6f8f0dac9dd71185dc84e653f8["44b9d6581ece6f8f0dac9dd71185dc84e653f8"]
  F1141_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d8_650c722a34be800ac878bf76689bfa083349b8["650c722a34be800ac878bf76689bfa083349b8"]
  F1142_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d7_8c5049e694973418b1b9f7c3bd0b87c0cf2149["8c5049e694973418b1b9f7c3bd0b87c0cf2149"]
  F1143_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_3befd4ca4083de59b9dbe53d7cad3682456344["3befd4ca4083de59b9dbe53d7cad3682456344"]
  F1144_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d8_a96aa85b99ed9c7b9a2ac679f33ae5e766b187["a96aa85b99ed9c7b9a2ac679f33ae5e766b187"]
  F1145_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d8_884b0690664bbd0878b8968d8fc04bf09803e0["884b0690664bbd0878b8968d8fc04bf09803e0"]
  F1146_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_561cd40903524caf63d35c312a6c8cee3c4427["561cd40903524caf63d35c312a6c8cee3c4427"]
  F1147_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_61321a72d37c2d3a3cfb384509a52f5d11fd69["61321a72d37c2d3a3cfb384509a52f5d11fd69"]
  F1148_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_c5af6834ec3276fa24f8ac2bf55a52264d6124["c5af6834ec3276fa24f8ac2bf55a52264d6124"]
  F1149_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_91ceed4b1da949a92fde6a4ae645facef2d055["91ceed4b1da949a92fde6a4ae645facef2d055"]
  F1150_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_adf7300e16a059ebc1f9eb085c310bc6bc5c0e["adf7300e16a059ebc1f9eb085c310bc6bc5c0e"]
  F1151_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_02a0a4d679dc907e180dea55fa97c49ae479a5["02a0a4d679dc907e180dea55fa97c49ae479a5"]
  F1152_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_ef6dffcb4f9056d8cbc5ccddab7b85f0f7d784["ef6dffcb4f9056d8cbc5ccddab7b85f0f7d784"]
  F1153_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\d9_585879ed661a339a168122c47f622e8338de8c["585879ed661a339a168122c47f622e8338de8c"]
  F1154_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_3e0cfc7942b18be3572e5ead50bfbfb1ff4b0c["3e0cfc7942b18be3572e5ead50bfbfb1ff4b0c"]
  F1155_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_43d35d5ed0498e3fb0578d1a281d14ede05e79["43d35d5ed0498e3fb0578d1a281d14ede05e79"]
  F1156_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_ae56fa837de31541f758bf0025d01a5a456e66["ae56fa837de31541f758bf0025d01a5a456e66"]
  F1157_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_dfc5d96b8de1864e1a71b1442675a1f387560c["dfc5d96b8de1864e1a71b1442675a1f387560c"]
  F1158_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_c4c9256ef32255c5db317d5012d6edcc9d00f9["c4c9256ef32255c5db317d5012d6edcc9d00f9"]
  F1159_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_285e3a935a30ee46d1a2af4134ef73c562ca56["285e3a935a30ee46d1a2af4134ef73c562ca56"]
  F1160_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_3f75f7672dd0fcc47b20961d5977386a4d871e["3f75f7672dd0fcc47b20961d5977386a4d871e"]
  F1161_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_0051d650974145bcc5fff5e76ab6b39d80423f["0051d650974145bcc5fff5e76ab6b39d80423f"]
  F1162_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_ea7557904fbb6f901aede8ae2e5616479c1c75["ea7557904fbb6f901aede8ae2e5616479c1c75"]
  F1163_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\da_eb9134c3551da6145d82945bec94f07ad28fef["eb9134c3551da6145d82945bec94f07ad28fef"]
  F1164_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_b847db31f6da775252bc4e39843a43a1e6268e["b847db31f6da775252bc4e39843a43a1e6268e"]
  F1165_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_aba41109538b9e742b22bbf44536e859939281["aba41109538b9e742b22bbf44536e859939281"]
  F1166_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_700c36e7893d05161d98ff920d934eac85124c["700c36e7893d05161d98ff920d934eac85124c"]
  F1167_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_c29bcc1b9287a611cf305cc3a460766bef2fd8["c29bcc1b9287a611cf305cc3a460766bef2fd8"]
  F1168_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_3fac91b2a18d2055e97280c510714c5639e1b0["3fac91b2a18d2055e97280c510714c5639e1b0"]
  F1169_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_b9f9b22d13568938f29b60bf3591fa3c80936f["b9f9b22d13568938f29b60bf3591fa3c80936f"]
  F1170_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_25176684b17f814c6c8eefbf2fb37d1082e267["25176684b17f814c6c8eefbf2fb37d1082e267"]
  F1171_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_5077089b49dae09ddafec646078738020af98d["5077089b49dae09ddafec646078738020af98d"]
  F1172_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_781b9e3b2ba7599d8989718440c679c4794422["781b9e3b2ba7599d8989718440c679c4794422"]
  F1173_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_2fa8082aa5785cb28ea6bef62945a0a56a53a2["2fa8082aa5785cb28ea6bef62945a0a56a53a2"]
  F1174_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_8a84bee6008c4a313833a894eeec784039b012["8a84bee6008c4a313833a894eeec784039b012"]
  F1175_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dc_dea7431157694464359e1b4daaf0dd56f9969e["dea7431157694464359e1b4daaf0dd56f9969e"]
  F1176_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\db_f5d0eb984d87fa1462ae0a2e2c035e820592ba["f5d0eb984d87fa1462ae0a2e2c035e820592ba"]
  F1177_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dd_2cd03fb47e807890d67b69ace7cc020a3bd93d["2cd03fb47e807890d67b69ace7cc020a3bd93d"]
  F1178_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_31d4a65e9f7e955e2da7b1b8349c1b61c30a63["31d4a65e9f7e955e2da7b1b8349c1b61c30a63"]
  F1179_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dd_dfa4f14d8b5022bf9490f116d4e639f64caa9a["dfa4f14d8b5022bf9490f116d4e639f64caa9a"]
  F1180_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dd_2e0fa21e7f77112d55a96ee10ea2e9091f21e8["2e0fa21e7f77112d55a96ee10ea2e9091f21e8"]
  F1181_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_23fb531e42326ecc55db763e89defdff5aa29f["23fb531e42326ecc55db763e89defdff5aa29f"]
  F1182_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_4b2aae6332edc5a1818546b53a7e3dade23e27["4b2aae6332edc5a1818546b53a7e3dade23e27"]
  F1183_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_99bed8e3167893dd47d9339cc25a48fd935fd1["99bed8e3167893dd47d9339cc25a48fd935fd1"]
  F1184_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\dd_d07efd3b3e41268b170c374b7cd4f2c6e549d8["d07efd3b3e41268b170c374b7cd4f2c6e549d8"]
  F1185_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_503fdbbafcf34a2cc718cd7481862a0cf40dce["503fdbbafcf34a2cc718cd7481862a0cf40dce"]
  F1186_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_1e1062498f8fcf2508ef6b75c69f8185f3e310["1e1062498f8fcf2508ef6b75c69f8185f3e310"]
  F1187_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_bbc7759077c8f18b83868acb40ad084819316e["bbc7759077c8f18b83868acb40ad084819316e"]
  F1188_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_0b3cf4e283792f9b266a643fec5d14d52f7553["0b3cf4e283792f9b266a643fec5d14d52f7553"]
  F1189_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_979064680cd23495986a5f48386a91e9a66e1d["979064680cd23495986a5f48386a91e9a66e1d"]
  F1190_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_903a03476650e18fd23d088094aaaeee06f9c8["903a03476650e18fd23d088094aaaeee06f9c8"]
  F1191_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_5d0a795c441bf94d7c8654a794805c10ce5902["5d0a795c441bf94d7c8654a794805c10ce5902"]
  F1192_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_b16b78b2e5afa72fec3aad316b65859d1fa959["b16b78b2e5afa72fec3aad316b65859d1fa959"]
  F1193_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_9ca96749107892fa8941a2e393e9d513f70fed["9ca96749107892fa8941a2e393e9d513f70fed"]
  F1194_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_1769b939f25a681b22b1b322eec310d0cca6b5["1769b939f25a681b22b1b322eec310d0cca6b5"]
  F1195_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_c705aa62b9e8e9ba497727602c709dab8a5fe9["c705aa62b9e8e9ba497727602c709dab8a5fe9"]
  F1196_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\de_e5e46a831cb5161a19db6393e3ff9c0b78ed9b["e5e46a831cb5161a19db6393e3ff9c0b78ed9b"]
  F1197_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_0d53c74ee80b51b030969810f512c23c411704["0d53c74ee80b51b030969810f512c23c411704"]
  F1198_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_63b83bcdfb83ca6ebf64f09d8a792ae10b9a6b["63b83bcdfb83ca6ebf64f09d8a792ae10b9a6b"]
  F1199_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_ebccbbf69170ab7831f0583033e2024e4646ad["ebccbbf69170ab7831f0583033e2024e4646ad"]
  F1200_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_a4e6deb53707d19fcdedb07ae291bcd984a593["a4e6deb53707d19fcdedb07ae291bcd984a593"]
  F1201_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e1_171a80f36aabcfd18a41717d52a6d727f995cc["171a80f36aabcfd18a41717d52a6d727f995cc"]
  F1202_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_fb8f7f769a39c4bf37d6b164c9d16b42f2f4c3["fb8f7f769a39c4bf37d6b164c9d16b42f2f4c3"]
  F1203_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_f4ce6a9233687c910baabfe47435f989bc23ff["f4ce6a9233687c910baabfe47435f989bc23ff"]
  F1204_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_e89aab877b35d8fe5261e9f59b99a73068a0c5["e89aab877b35d8fe5261e9f59b99a73068a0c5"]
  F1205_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\df_a3db21987b3b0a7283a8c5bbb729db3c89e800["a3db21987b3b0a7283a8c5bbb729db3c89e800"]
  F1206_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e0_6c4f4437125d120013be9ee134df4a7018ee36["6c4f4437125d120013be9ee134df4a7018ee36"]
  F1207_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_379e39bd4b69f362df10f71c3769918e8ed6e6["379e39bd4b69f362df10f71c3769918e8ed6e6"]
  F1208_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e1_50d575f49dea6f848de7365d7cd778a8523939["50d575f49dea6f848de7365d7cd778a8523939"]
  F1209_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_8446831193fc67ef3d17bf17d89936e2004c79["8446831193fc67ef3d17bf17d89936e2004c79"]
  F1210_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_56bd87daaa616c353e1383563c017e0ee15e70["56bd87daaa616c353e1383563c017e0ee15e70"]
  F1211_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e1_c682044d15497e29fe952a0eff741acdf113b0["c682044d15497e29fe952a0eff741acdf113b0"]
  F1212_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e3_16d893a7cf5986186d14924468101053a1e1e1["16d893a7cf5986186d14924468101053a1e1e1"]
  F1213_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_8b675cacd1cfdde32520aab0ce98953a1c5594["8b675cacd1cfdde32520aab0ce98953a1c5594"]
  F1214_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e3_2bdc93453149e629bb7eb05f34615c5de029fc["2bdc93453149e629bb7eb05f34615c5de029fc"]
  F1215_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_b27cbf08a4a3d9d27c46c65a82292cbbb8402c["b27cbf08a4a3d9d27c46c65a82292cbbb8402c"]
  F1216_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e2_ddcad6f751d689dd949c67edb18f158825b0c3["ddcad6f751d689dd949c67edb18f158825b0c3"]
  F1217_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e3_b3f36396fe956414ca71771c69436bb2b6a3f9["b3f36396fe956414ca71771c69436bb2b6a3f9"]
  F1218_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e6_0b2ce3e28e8d92a3bce0c6a82cb9ed5dfe8f47["0b2ce3e28e8d92a3bce0c6a82cb9ed5dfe8f47"]
  F1219_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e4_c8a7aa5c12b49ae77ec468fb21285e76d1a95b["c8a7aa5c12b49ae77ec468fb21285e76d1a95b"]
  F1220_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e3_e8e6f33a8866534e39ca2eb56ac14452e5d168["e8e6f33a8866534e39ca2eb56ac14452e5d168"]
  F1221_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e6_e9a38037148d3c52e2ac74b54903f2f418bd3d["e9a38037148d3c52e2ac74b54903f2f418bd3d"]
  F1222_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e6_4031cfc24eec36a3789fe37251148eb0e5ea72["4031cfc24eec36a3789fe37251148eb0e5ea72"]
  F1223_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e6_9de29bb2d1d6434b8b29ae775ad8c2e48c5391["9de29bb2d1d6434b8b29ae775ad8c2e48c5391"]
  F1224_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e4_243385b047e821c2b8c4256322868ffe093244["243385b047e821c2b8c4256322868ffe093244"]
  F1225_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e5_76870b29e855ca60ce345f052ac656cd7b80a9["76870b29e855ca60ce345f052ac656cd7b80a9"]
  F1226_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e5_df79d3220c04c15983d4536b16b40fb1740e83["df79d3220c04c15983d4536b16b40fb1740e83"]
  F1227_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_caed5940ccc8bb561d94da1712eb4086f3a6f1["caed5940ccc8bb561d94da1712eb4086f3a6f1"]
  F1228_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_1733ae03187ad87a92f377c40015a29802c1ca["1733ae03187ad87a92f377c40015a29802c1ca"]
  F1229_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e6_fef360328bb1be59f26146f6802fbeb7991138["fef360328bb1be59f26146f6802fbeb7991138"]
  F1230_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e8_5985e6048b41e77e12e677d0474919d2ff8ff6["5985e6048b41e77e12e677d0474919d2ff8ff6"]
  F1231_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e8_3084024a398490902b37e3b92a2c12c9b69fd4["3084024a398490902b37e3b92a2c12c9b69fd4"]
  F1232_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e8_2ffdf5328b8999c93f4f0e2c3af0e68dee5ea6["2ffdf5328b8999c93f4f0e2c3af0e68dee5ea6"]
  F1233_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_de4b66466f1b26750f699deb4e5b558ab50a6b["de4b66466f1b26750f699deb4e5b558ab50a6b"]
  F1234_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_426d04d4f844b334c39aac7d47e8a05cdb011f["426d04d4f844b334c39aac7d47e8a05cdb011f"]
  F1235_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_1f7f271ff16b1c1003641c9eb68fd84570d3b6["1f7f271ff16b1c1003641c9eb68fd84570d3b6"]
  F1236_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e7_e0a0365ea5af5a060091ead11d6a87464f3b98["e0a0365ea5af5a060091ead11d6a87464f3b98"]
  F1237_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_24123bb1d4650789e357215dfd36fb34ed2ab7["24123bb1d4650789e357215dfd36fb34ed2ab7"]
  F1238_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_2e409f172ddf1cc6bb65ebf0f4a88a6423fb72["2e409f172ddf1cc6bb65ebf0f4a88a6423fb72"]
  F1239_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_82100a97f0a5c144bc97f2ec9a904d962b1637["82100a97f0a5c144bc97f2ec9a904d962b1637"]
  F1240_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_3acc6be33da9bafbcdea16c8f1dcdedb2b8b5f["3acc6be33da9bafbcdea16c8f1dcdedb2b8b5f"]
  F1241_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_c30ac83cb6287b8883196c06578ce0656bcb58["c30ac83cb6287b8883196c06578ce0656bcb58"]
  F1242_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_c783abc171f7f0e56b8fe83058e30a1728e320["c783abc171f7f0e56b8fe83058e30a1728e320"]
  F1243_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e8_c5f14a5521b251ea8c49805c7fe295eafb381e["c5f14a5521b251ea8c49805c7fe295eafb381e"]
  F1244_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_5f209a24a260886567dc4280983073449c6e38["5f209a24a260886567dc4280983073449c6e38"]
  F1245_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_93593cc9b309db75eca222ac149f9107bdcf0c["93593cc9b309db75eca222ac149f9107bdcf0c"]
  F1246_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_3a9316eb3a3b11021a5ca86f3c4df3546044c9["3a9316eb3a3b11021a5ca86f3c4df3546044c9"]
  F1247_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_cc71b2f77d69f3c03af67cfb4741b67a4f2387["cc71b2f77d69f3c03af67cfb4741b67a4f2387"]
  F1248_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_5cddc7bf5326bb9d4ed01c0162db6efb259f9c["5cddc7bf5326bb9d4ed01c0162db6efb259f9c"]
  F1249_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_d94ff2b8834a04d465d11c799c3514fcaedd38["d94ff2b8834a04d465d11c799c3514fcaedd38"]
  F1250_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_cd36d6c3ed49bc3c77f5f0626526af809df6e5["cd36d6c3ed49bc3c77f5f0626526af809df6e5"]
  F1251_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\e9_cebde88ebf9a930cb9b0cc229c8e5f55b2e8b3["cebde88ebf9a930cb9b0cc229c8e5f55b2e8b3"]
  F1252_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_1ff156ce797fe47506fa7daa3cf70b293a1482["1ff156ce797fe47506fa7daa3cf70b293a1482"]
  F1253_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_9e870338c3deb98436487e5d5b902c31f5520c["9e870338c3deb98436487e5d5b902c31f5520c"]
  F1254_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_1b87c200a31949fae33147cbe84d5f592fd1c3["1b87c200a31949fae33147cbe84d5f592fd1c3"]
  F1255_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_00b8baf5c2117f38d7c938e845e5948110008d["00b8baf5c2117f38d7c938e845e5948110008d"]
  F1256_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ea_234f8f3ac429905f0e964eb00d9cc380b18016["234f8f3ac429905f0e964eb00d9cc380b18016"]
  F1257_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_daa57eed50e17e653998eba20dae4fe02e1757["daa57eed50e17e653998eba20dae4fe02e1757"]
  F1258_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_1191f2e24079e5df233a1cc8868b542ea8a4b9["1191f2e24079e5df233a1cc8868b542ea8a4b9"]
  F1259_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_32f9214deefe18e211277e0f56fe223deb8c3d["32f9214deefe18e211277e0f56fe223deb8c3d"]
  F1260_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_7a790c933a7ea53208d1bd9b6b412412539108["7a790c933a7ea53208d1bd9b6b412412539108"]
  F1261_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_47ae9f4651bde0c2ce1f23223138e3f25ce94f["47ae9f4651bde0c2ce1f23223138e3f25ce94f"]
  F1262_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_8d225debb8fda79c891702a6665d7a14f39bda["8d225debb8fda79c891702a6665d7a14f39bda"]
  F1263_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_9ddcbd00720696e7adb9695fb6fa4df7a67c8d["9ddcbd00720696e7adb9695fb6fa4df7a67c8d"]
  F1264_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\eb_ed5b2a386e0edd95ac076d2f03d8d3888ad047["ed5b2a386e0edd95ac076d2f03d8d3888ad047"]
  F1265_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_98aa3e24b7f3d6a805ae347b6bad49486f4251["98aa3e24b7f3d6a805ae347b6bad49486f4251"]
  F1266_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_91a703ca4a2bc59fd39f0065a2848f48bcdc35["91a703ca4a2bc59fd39f0065a2848f48bcdc35"]
  F1267_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_a8721f140f6da45a4b07a28ac2d71a029db330["a8721f140f6da45a4b07a28ac2d71a029db330"]
  F1268_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_f54a9e7a19d2df10b1b14e074ec5f3cba60660["f54a9e7a19d2df10b1b14e074ec5f3cba60660"]
  F1269_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_b9233ba2db19c693d8264a09176233830fbefc["b9233ba2db19c693d8264a09176233830fbefc"]
  F1270_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_e439500a382402f3a60a59bd948cc5aaa925b0["e439500a382402f3a60a59bd948cc5aaa925b0"]
  F1271_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_498ed6e43b60d7542eee271e992d9bcc27cad7["498ed6e43b60d7542eee271e992d9bcc27cad7"]
  F1272_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ed_21b95b5ee3a17994fa2f95ff21780aeb875c72["21b95b5ee3a17994fa2f95ff21780aeb875c72"]
  F1273_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_37f8f8d00aa11034cb8bc279fa319e74bc938f["37f8f8d00aa11034cb8bc279fa319e74bc938f"]
  F1274_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ec_d99264ed27630c89e7f5d8ff82cca2d4edbf34["d99264ed27630c89e7f5d8ff82cca2d4edbf34"]
  F1275_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ed_2413f0b64cf3587860e4954845d565a82b67ce["2413f0b64cf3587860e4954845d565a82b67ce"]
  F1276_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ed_74f1a9605568aa78dffb481a07ce9e7919e5f6["74f1a9605568aa78dffb481a07ce9e7919e5f6"]
  F1277_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_6132141d295bea0c855e40a6674e1e8424f6f6["6132141d295bea0c855e40a6674e1e8424f6f6"]
  F1278_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_74d2a1c4defdc7ca25b5fff8d79fbd163083fd["74d2a1c4defdc7ca25b5fff8d79fbd163083fd"]
  F1279_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_76999af3a1cade6dca3ec38ae13c8b4429d5f9["76999af3a1cade6dca3ec38ae13c8b4429d5f9"]
  F1280_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_bfd3e2c8f1facd9f7d6350c97bd6ac9685162d["bfd3e2c8f1facd9f7d6350c97bd6ac9685162d"]
  F1281_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ee_9b6f93b7d6f100627945be20a029a8a0d27152["9b6f93b7d6f100627945be20a029a8a0d27152"]
  F1282_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_32055f5eb69e72d677f6321aa498a18a13df67["32055f5eb69e72d677f6321aa498a18a13df67"]
  F1283_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_5066b80d28b89a5bae5430c11cc14f58b4cd6e["5066b80d28b89a5bae5430c11cc14f58b4cd6e"]
  F1284_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_d1e76277039333d0064ea83088fdc4d3a04bd5["d1e76277039333d0064ea83088fdc4d3a04bd5"]
  F1285_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_20451a2c7d93ad9691eaf228a3b7a59a82d1f3["20451a2c7d93ad9691eaf228a3b7a59a82d1f3"]
  F1286_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ef_3f7c6db0bcf99037bcc173f4408cddc867b65c["3f7c6db0bcf99037bcc173f4408cddc867b65c"]
  F1287_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_13ec85ed5b4278c6ece5f026e4ab0984f1f435["13ec85ed5b4278c6ece5f026e4ab0984f1f435"]
  F1288_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_59d6fdaf395893deda0afbba41a788e1fc2566["59d6fdaf395893deda0afbba41a788e1fc2566"]
  F1289_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_f4991067239b7a267720467d3bf55c359feeac["f4991067239b7a267720467d3bf55c359feeac"]
  F1290_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_9ca0a76b7673180c81a64289f9a78601e7659c["9ca0a76b7673180c81a64289f9a78601e7659c"]
  F1291_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_789dddfad8a29feb645c170ec856e31f2715c3["789dddfad8a29feb645c170ec856e31f2715c3"]
  F1292_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_6d9e69f2e2aa24398929467a8e02d00ad3bfc6["6d9e69f2e2aa24398929467a8e02d00ad3bfc6"]
  F1293_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f1_04c70fd0c59e0110cb9c375c1e5939fea2499d["04c70fd0c59e0110cb9c375c1e5939fea2499d"]
  F1294_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_90cdb4a740d6085f6283fb234e18db3d0c9637["90cdb4a740d6085f6283fb234e18db3d0c9637"]
  F1295_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f1_2b8932c7547f0c0d3b95fc45fcb5426cf02764["2b8932c7547f0c0d3b95fc45fcb5426cf02764"]
  F1296_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f0_4170c6679e4c465925331e802a274d067b3c0f["4170c6679e4c465925331e802a274d067b3c0f"]
  F1297_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f1_92ea0b8c32e21408837a7e76b0a57c4c491620["92ea0b8c32e21408837a7e76b0a57c4c491620"]
  F1298_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_33caa444179fc04b44d7149edf3fa902019d3b["33caa444179fc04b44d7149edf3fa902019d3b"]
  F1299_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f2_3f17bd4ff0aea7a169e8a43d18bde625398d78["3f17bd4ff0aea7a169e8a43d18bde625398d78"]
  F1300_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f2_44b1840c195c7b75bba3d2fe315d39211e790e["44b1840c195c7b75bba3d2fe315d39211e790e"]
  F1301_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f2_e725e382976e3132d54359dafb78f4b0de3a15["e725e382976e3132d54359dafb78f4b0de3a15"]
  F1302_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f1_e3f18deeaa081b89c174111ceb210218b0c8c9["e3f18deeaa081b89c174111ceb210218b0c8c9"]
  F1303_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_084093806ffc51f7312097719cf08c6458a4e3["084093806ffc51f7312097719cf08c6458a4e3"]
  F1304_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f1_7f00971b05650e14a6250a98b7bb628abfd0f1["7f00971b05650e14a6250a98b7bb628abfd0f1"]
  F1305_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f2_1819f676db8aeb6b27b18f2c70b3ba34fe9520["1819f676db8aeb6b27b18f2c70b3ba34fe9520"]
  F1306_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_5543b0a53eb4ad375ea0cf193a39c88dd64d7a["5543b0a53eb4ad375ea0cf193a39c88dd64d7a"]
  F1307_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f4_64719d18b0349e326885cd3ce71e60445d7768["64719d18b0349e326885cd3ce71e60445d7768"]
  F1308_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f4_4c09044f5b77d5b66f2da47ef67245594a9e36["4c09044f5b77d5b66f2da47ef67245594a9e36"]
  F1309_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f4_4febfb8a3d77e9b1ee776125a285ee265a1c94["4febfb8a3d77e9b1ee776125a285ee265a1c94"]
  F1310_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f6_42441e25198e6435356c3a4377cf62435781ef["42441e25198e6435356c3a4377cf62435781ef"]
  F1311_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_a932ef83f783f05df12d8dbd957436c1a72901["a932ef83f783f05df12d8dbd957436c1a72901"]
  F1312_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f4_cc2f18d2b2eb1cecccb2990502c4f32ed2e462["cc2f18d2b2eb1cecccb2990502c4f32ed2e462"]
  F1313_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_f7e6ba47479636acef6bcbcc00b3f215b5369a["f7e6ba47479636acef6bcbcc00b3f215b5369a"]
  F1314_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_9bedad59d2b38032a7cacabd7d1e8a07459336["9bedad59d2b38032a7cacabd7d1e8a07459336"]
  F1315_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f5_e5ffe54767f5c378e33b8f47bf1edb0ef9a304["e5ffe54767f5c378e33b8f47bf1edb0ef9a304"]
  F1316_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f3_dc28072a5759f1acb829cb2eaa48ca12e83248["dc28072a5759f1acb829cb2eaa48ca12e83248"]
  F1317_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f6_ea69ef2e272d4b18ef19e53c8338f6a111d35e["ea69ef2e272d4b18ef19e53c8338f6a111d35e"]
  F1318_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f6_9a413664eb3100713470699d6b775d667dee70["9a413664eb3100713470699d6b775d667dee70"]
  F1319_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f6_883838a22548a7e90510957e6b2b0ee16e0fa7["883838a22548a7e90510957e6b2b0ee16e0fa7"]
  F1320_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f6_c09e2b76ddc43fa39cab118bcf8d3011071026["c09e2b76ddc43fa39cab118bcf8d3011071026"]
  F1321_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_9f80d446c487ebb0c09c4c42076977fe260b69["9f80d446c487ebb0c09c4c42076977fe260b69"]
  F1322_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_62634767cc4b429d5ec9c1c7cfd3504a0bb643["62634767cc4b429d5ec9c1c7cfd3504a0bb643"]
  F1323_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_55482b16cf943cca42aba35304fcc350397587["55482b16cf943cca42aba35304fcc350397587"]
  F1324_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_419d84a479765869f84d000b7550ffd362f855["419d84a479765869f84d000b7550ffd362f855"]
  F1325_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_72cedc14624d1c09be95d3a152bc003c4c411f["72cedc14624d1c09be95d3a152bc003c4c411f"]
  F1326_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_5c59ef9f875a1ebca4b356a3ddd1c3c456d728["5c59ef9f875a1ebca4b356a3ddd1c3c456d728"]
  F1327_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_8dd11abd9f8680f8b78dde66faf1731b2b927d["8dd11abd9f8680f8b78dde66faf1731b2b927d"]
  F1328_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_4370312cf80d724e7de9655f620c500cfda518["4370312cf80d724e7de9655f620c500cfda518"]
  F1329_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_a534c0d52117cba7ac2d8234881fcef55a1bc2["a534c0d52117cba7ac2d8234881fcef55a1bc2"]
  F1330_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_a0b8acd912eb6fdbee0d5bafcab2721bebafa7["a0b8acd912eb6fdbee0d5bafcab2721bebafa7"]
  F1331_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_8fdf370448fd0434b5d75bbfa5825f3c36f180["8fdf370448fd0434b5d75bbfa5825f3c36f180"]
  F1332_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_ad8aca4e9f4726e509332eabf6f964d8892460["ad8aca4e9f4726e509332eabf6f964d8892460"]
  F1333_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f7_ba6ebdcf0ff0cfdf5b91b3d228cb74fcdc3198["ba6ebdcf0ff0cfdf5b91b3d228cb74fcdc3198"]
  F1334_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_7aa21e926ef9d34185977a24de32f6ca917fc7["7aa21e926ef9d34185977a24de32f6ca917fc7"]
  F1335_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f9_2527167bb9bb2724885fcae01f86793002f8cd["2527167bb9bb2724885fcae01f86793002f8cd"]
  F1336_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f8_c34c5bcd06050de808dfb1bc64bb126c35d23f["c34c5bcd06050de808dfb1bc64bb126c35d23f"]
  F1337_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fa_a14ed1ddad8f58c24e91ec313194e3a0c9b6e9["a14ed1ddad8f58c24e91ec313194e3a0c9b6e9"]
  F1338_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fa_38fb59b7a645dc0b1481b354291721b5d5fcef["38fb59b7a645dc0b1481b354291721b5d5fcef"]
  F1339_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f9_4f7387d6a7f999615407d5b24665d52312d99d["4f7387d6a7f999615407d5b24665d52312d99d"]
  F1340_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_3dea2bab0b9cb048dfb407f180611b97f64020["3dea2bab0b9cb048dfb407f180611b97f64020"]
  F1341_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_843d2abb0c0027fe6a887faff03ab1ef212677["843d2abb0c0027fe6a887faff03ab1ef212677"]
  F1342_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_4de5a18c5fd6f353208cd42d1ea846a03b1433["4de5a18c5fd6f353208cd42d1ea846a03b1433"]
  F1343_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fa_c5886fb71a8c3210057dca4cf09dc89fada150["c5886fb71a8c3210057dca4cf09dc89fada150"]
  F1344_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_7fa9347bcd1d34d2980adbb9ee8f4ae2720cf4["7fa9347bcd1d34d2980adbb9ee8f4ae2720cf4"]
  F1345_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_20978cc635c893506f846c151186b25987a1d0["20978cc635c893506f846c151186b25987a1d0"]
  F1346_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\f9_cbce60c9f0813961efe790513916be83cbc497["cbce60c9f0813961efe790513916be83cbc497"]
  F1347_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_a77ec8a8cb91e47719cf9a045366758ff7c9a6["a77ec8a8cb91e47719cf9a045366758ff7c9a6"]
  F1348_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fd_3679f219f33ca958803ca2f7b5bae1eee98b20["3679f219f33ca958803ca2f7b5bae1eee98b20"]
  F1349_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fd_52d2d53620e5232674bb29cc9f8b9841f5f880["52d2d53620e5232674bb29cc9f8b9841f5f880"]
  F1350_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_2a68c68baf1df3c7e7a5dde336471f2273907f["2a68c68baf1df3c7e7a5dde336471f2273907f"]
  F1351_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fd_43808eee9dbae54b4a898bc8e64787618e8e86["43808eee9dbae54b4a898bc8e64787618e8e86"]
  F1352_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_bf0752a7ae6779e55e42cf9984210a8c89b492["bf0752a7ae6779e55e42cf9984210a8c89b492"]
  F1353_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_43db751c4d00f8bb47fc46e5181284f251454b["43db751c4d00f8bb47fc46e5181284f251454b"]
  F1354_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_457e9d4faa2a5ccb7073418a793f5a40e998b0["457e9d4faa2a5ccb7073418a793f5a40e998b0"]
  F1355_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fc_851d4ae96cd750d8c21b8a260c1d131c3bc4d7["851d4ae96cd750d8c21b8a260c1d131c3bc4d7"]
  F1356_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fb_c973968173934e7c76fd100e87b8855aa5fe3b["c973968173934e7c76fd100e87b8855aa5fe3b"]
  F1357_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_ea2826d15af182fec47b9c50f2cbfbb0b607d8["ea2826d15af182fec47b9c50f2cbfbb0b607d8"]
  F1358_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_ed78340b55f786a17897218363098b2d02c61d["ed78340b55f786a17897218363098b2d02c61d"]
  F1359_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_15491b89df58bd73052d92c4a8137481c80a40["15491b89df58bd73052d92c4a8137481c80a40"]
  F1360_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_a6d1732d98419c1f365990b8f39a08ee2c92c3["a6d1732d98419c1f365990b8f39a08ee2c92c3"]
  F1361_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_44500090366815726f363d3167fb6a12540ab4["44500090366815726f363d3167fb6a12540ab4"]
  F1362_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_32f3ccd5b6ce9411c477be26223f196c3e025e["32f3ccd5b6ce9411c477be26223f196c3e025e"]
  F1363_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_2b57a0b23d3c22c4367a095020dd40e68369e1["2b57a0b23d3c22c4367a095020dd40e68369e1"]
  F1364_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_22484819cacb5c87b41a5b3f9296cf62217003["22484819cacb5c87b41a5b3f9296cf62217003"]
  F1365_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_0a3e70aeae7d7948405422b176a40624690fe0["0a3e70aeae7d7948405422b176a40624690fe0"]
  F1366_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\fe_f923ce25b1e9e37c40d13bd452cb524eb9b72a["f923ce25b1e9e37c40d13bd452cb524eb9b72a"]
  F1367_c:\Users\bangn\Downloads\OnlineUTE\.git\refs\heads_master["master"]
  F1368_c:\Users\bangn\Downloads\OnlineUTE\.git\refs\remotes\origin_master["master"]
  F1369_c:\Users\bangn\Downloads\OnlineUTE\.git\refs\remotes\origin_HEAD["HEAD"]
  F1370_c:\Users\bangn\Downloads\OnlineUTE_.gitignore[".gitignore"]
  F1371_c:\Users\bangn\Downloads\OnlineUTE_.gitattributes[".gitattributes"]
  F1372_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_d0c507f323fd18ba1ca0d02aa9f3ddfede3ad3["d0c507f323fd18ba1ca0d02aa9f3ddfede3ad3"]
  F1373_c:\Users\bangn\Downloads\OnlineUTE\.idea_compiler.xml["compiler.xml"]
  F1374_c:\Users\bangn\Downloads\OnlineUTE\.git_ORIG_HEAD["ORIG_HEAD"]
  F1375_c:\Users\bangn\Downloads\OnlineUTE\.idea_.gitignore[".gitignore"]
  F1376_c:\Users\bangn\Downloads\OnlineUTE\.git\objects\ff_b27d0d2b144bb0b226086eb65f9910e12aa22e["b27d0d2b144bb0b226086eb65f9910e12aa22e"]
  F1377_c:\Users\bangn\Downloads\OnlineUTE\.idea_encodings.xml["encodings.xml"]
  F1378_c:\Users\bangn\Downloads\OnlineUTE\.idea_dataSources.xml["dataSources.xml"]
  F1379_c:\Users\bangn\Downloads\OnlineUTE\.idea_data_source_mapping.xml["data_source_mapping.xml"]
  F1380_c:\Users\bangn\Downloads\OnlineUTE\.idea_jarRepositories.xml["jarRepositories.xml"]
  F1381_c:\Users\bangn\Downloads\OnlineUTE\.idea_dataSources.local.xml["dataSources.local.xml"]
  F1382_c:\Users\bangn\Downloads\OnlineUTE\.idea\dataSources_2c241e0a-e39a-4b0d-828e-9c184c1bbe1c.xml["2c241e0a-e39a-4b0d-828e-9c184c1bbe1c.xml"]
  F1383_c:\Users\bangn\Downloads\OnlineUTE\.idea_misc.xml["misc.xml"]
  F1384_c:\Users\bangn\Downloads\OnlineUTE\.idea_sqldialects.xml["sqldialects.xml"]
  F1385_c:\Users\bangn\Downloads\OnlineUTE\.idea_modules.xml["modules.xml"]
  F1386_c:\Users\bangn\Downloads\OnlineUTE\.idea_vcs.xml["vcs.xml"]
  F1387_c:\Users\bangn\Downloads\OnlineUTE\.mvn\wrapper_maven-wrapper.properties["maven-wrapper.properties"]
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql["setup.sql"]
  F1389_c:\Users\bangn\Downloads\OnlineUTE\.idea_workspace.xml["workspace.xml"]
  F1390_c:\Users\bangn\Downloads\OnlineUTE\config_session.properties["session.properties"]
  F1391_c:\Users\bangn\Downloads\OnlineUTE\document_api.md["api.md"]
  F1392_c:\Users\bangn\Downloads\OnlineUTE\CodeFlattened_OnlineUTE_flattened.md["OnlineUTE_flattened.md"]
  F1393_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_.gitignore[".gitignore"]
  F1394_c:\Users\bangn\Downloads\OnlineUTE\document_entity_relationship.md["entity_relationship.md"]
  F1395_c:\Users\bangn\Downloads\OnlineUTE\document_view-structure.md["view-structure.md"]
  F1396_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_compiler.xml["compiler.xml"]
  F1397_c:\Users\bangn\Downloads\OnlineUTE\document_service-readme.md["service-readme.md"]
  F1398_c:\Users\bangn\Downloads\OnlineUTE\document_ui_component_architecture.md["ui_component_architecture.md"]
  F1399_c:\Users\bangn\Downloads\OnlineUTE\document_logic_implementation.md["logic_implementation.md"]
  F1400_c:\Users\bangn\Downloads\OnlineUTE\document_ui_guide.md["ui_guide.md"]
  F1401_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_encodings.xml["encodings.xml"]
  F1402_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_dataSources.xml["dataSources.xml"]
  F1403_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_sqldialects.xml["sqldialects.xml"]
  F1404_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_dataSources.local.xml["dataSources.local.xml"]
  F1405_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_jarRepositories.xml["jarRepositories.xml"]
  F1406_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_misc.xml["misc.xml"]
  F1407_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_data_source_mapping.xml["data_source_mapping.xml"]
  F1408_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea\dataSources_b2b7c3c3-790e-4b2c-bade-6d7187f5f329.xml["b2b7c3c3-790e-4b2c-bade-6d7187f5f329.xml"]
  F1409_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\config_JpaUtil.java["JpaUtil.java"]
  F1410_c:\Users\bangn\Downloads\OnlineUTE\Example\database_setup.sql["setup.sql"]
  F1411_c:\Users\bangn\Downloads\OnlineUTE\Example\.idea_workspace.xml["workspace.xml"]
  F1412_c:\Users\bangn\Downloads\OnlineUTE\Example_pom.xml["pom.xml"]
  F1413_c:\Users\bangn\Downloads\OnlineUTE\Example_part1.md["part1.md"]
  F1414_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis_AppLauncher.java["AppLauncher.java"]
  F1415_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\controller_CustomerController.java["CustomerController.java"]
  F1416_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\controller_OrderController.java["OrderController.java"]
  F1417_c:\Users\bangn\Downloads\OnlineUTE\Example_part2.md["part2.md"]
  F1418_c:\Users\bangn\Downloads\OnlineUTE\Example_part3.md["part3.md"]
  F1419_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao_ProductDAO.java["ProductDAO.java"]
  F1420_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao\impl_ProductDAOImpl.java["ProductDAOImpl.java"]
  F1421_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\controller_ProductController.java["ProductController.java"]
  F1422_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao\impl_CustomerDAOImpl.java["CustomerDAOImpl.java"]
  F1423_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao_CustomerDAO.java["CustomerDAO.java"]
  F1424_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao_OrderDetailDAO.java["OrderDetailDAO.java"]
  F1425_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao\impl_OrderDetailDAOImpl.java["OrderDetailDAOImpl.java"]
  F1426_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao\impl_SalesOrderDAOImpl.java["SalesOrderDAOImpl.java"]
  F1427_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao\impl_ReportDAOImpl.java["ReportDAOImpl.java"]
  F1428_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao_ReportDAO.java["ReportDAO.java"]
  F1429_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\dto_MonthlyRevenueDTO.java["MonthlyRevenueDTO.java"]
  F1430_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\dao_SalesOrderDAO.java["SalesOrderDAO.java"]
  F1431_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\dto_CustomerRevenueDTO.java["CustomerRevenueDTO.java"]
  F1432_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\dto_ProductSalesDTO.java["ProductSalesDTO.java"]
  F1433_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\dto_StatusCountDTO.java["StatusCountDTO.java"]
  F1434_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\entity_SalesOrder.java["SalesOrder.java"]
  F1435_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\dto_OrderLineInput.java["OrderLineInput.java"]
  F1436_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\entity_OrderDetail.java["OrderDetail.java"]
  F1437_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\entity_Customer.java["Customer.java"]
  F1438_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\entity_Product.java["Product.java"]
  F1439_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service_LookupService.java["LookupService.java"]
  F1440_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service\impl_CustomerServiceImpl.java["CustomerServiceImpl.java"]
  F1441_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service\impl_OrderServiceImpl.java["OrderServiceImpl.java"]
  F1442_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service_ProductService.java["ProductService.java"]
  F1443_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service_CustomerService.java["CustomerService.java"]
  F1444_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service\impl_LookupServiceImpl.java["LookupServiceImpl.java"]
  F1445_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\view_CustomerManagementPanel.java["CustomerManagementPanel.java"]
  F1446_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\model\enumtype_OrderStatus.java["OrderStatus.java"]
  F1447_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service_OrderService.java["OrderService.java"]
  F1448_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\service\impl_ProductServiceImpl.java["ProductServiceImpl.java"]
  F1449_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\resources\META-INF_persistence.xml["persistence.xml"]
  F1450_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\view_OrderManagementPanel.java["OrderManagementPanel.java"]
  F1451_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\view_MainFrame.java["MainFrame.java"]
  F1452_c:\Users\bangn\Downloads\OnlineUTE\Example\src\main\java\com\example\salesmis\view_ProductManagementPanel.java["ProductManagementPanel.java"]
  F1453_c:\Users\bangn\Downloads\OnlineUTE_HELP.md["HELP.md"]
  F1454_c:\Users\bangn\Downloads\OnlineUTE\Example\target\classes\META-INF_persistence.xml["persistence.xml"]
  F1455_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_109.38516929.chunk.css["109.38516929.chunk.css"]
  F1456_c:\Users\bangn\Downloads\OnlineUTE\HTML_Thông tin sinh viên.html["Thông tin sinh viên.html"]
  F1457_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_148.44ef9ac2.chunk.css["148.44ef9ac2.chunk.css"]
  F1458_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_163.0e433876.chunk.css["163.0e433876.chunk.css"]
  F1459_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_146.1bdd8728.chunk.css["146.1bdd8728.chunk.css"]
  F1460_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_32.0e433876.chunk.css["32.0e433876.chunk.css"]
  F1461_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_57.89dc7092.chunk.css["57.89dc7092.chunk.css"]
  F1462_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_39.51904ad8.chunk.css["39.51904ad8.chunk.css"]
  F1463_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_73.ee602a01.chunk.css["73.ee602a01.chunk.css"]
  F1464_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_90.0e433876.chunk.css["90.0e433876.chunk.css"]
  F1465_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_68.0e433876.chunk.css["68.0e433876.chunk.css"]
  F1466_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_Flag_of_Vietnam.svg["Flag_of_Vietnam.svg"]
  F1467_c:\Users\bangn\Downloads\OnlineUTE_mvnw["mvnw"]
  F1468_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_main.c930b340.chunk.css["main.c930b340.chunk.css"]
  F1469_c:\Users\bangn\Downloads\OnlineUTE_pom.xml["pom.xml"]
  F1470_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_client["client"]
  F1471_c:\Users\bangn\Downloads\OnlineUTE\HTML\Thông tin sinh viên_files_css["css"]
  F1472_c:\Users\bangn\Downloads\OnlineUTE_README.md["README.md"]
  F1473_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Config_PasswordUtil.java["PasswordUtil.java"]
  F1474_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Config_JpaUtil.java["JpaUtil.java"]
  F1475_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_AccountController.java["AccountController.java"]
  F1476_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_AuthController.java["AuthController.java"]
  F1477_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Config_AppContext.java["AppContext.java"]
  F1478_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Config_SessionManager.java["SessionManager.java"]
  F1479_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_ClassController.java["ClassController.java"]
  F1480_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Config_TransactionUtil.java["TransactionUtil.java"]
  F1481_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_CourseSectionController.java["CourseSectionController.java"]
  F1482_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_CourseRegistrationController.java["CourseRegistrationController.java"]
  F1483_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_MajorController.java["MajorController.java"]
  F1484_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_MarkController.java["MarkController.java"]
  F1485_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_LecturerController.java["LecturerController.java"]
  F1486_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_CourseController.java["CourseController.java"]
  F1487_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_NotificationController.java["NotificationController.java"]
  F1488_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_RegistrationBatchController.java["RegistrationBatchController.java"]
  F1489_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_FacultyController.java["FacultyController.java"]
  F1490_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_StudentController.java["StudentController.java"]
  F1491_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_ClassDAO.java["ClassDAO.java"]
  F1492_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_AccountDAO.java["AccountDAO.java"]
  F1493_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_CourseDAO.java["CourseDAO.java"]
  F1494_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_TermController.java["TermController.java"]
  F1495_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_AbstractDAO.java["AbstractDAO.java"]
  F1496_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Controller_UserProfileController.java["UserProfileController.java"]
  F1497_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_AdminDAO.java["AdminDAO.java"]
  F1498_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_CourseSectionDAO.java["CourseSectionDAO.java"]
  F1499_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_AnnouncementDAO.java["AnnouncementDAO.java"]
  F1500_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_CourseRegistrationDAO.java["CourseRegistrationDAO.java"]
  F1501_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_FacultyDAO.java["FacultyDAO.java"]
  F1502_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_CourseRegistrationDAOImpl.java["CourseRegistrationDAOImpl.java"]
  F1503_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_CourseDAOImpl.java["CourseDAOImpl.java"]
  F1504_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_AdminDAOImpl.java["AdminDAOImpl.java"]
  F1505_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_LecturerDAOImpl.java["LecturerDAOImpl.java"]
  F1506_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_ClassDAOImpl.java["ClassDAOImpl.java"]
  F1507_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_AccountDAOImpl.java["AccountDAOImpl.java"]
  F1508_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_FacultyDAOImpl.java["FacultyDAOImpl.java"]
  F1509_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_CourseSectionDAOImpl.java["CourseSectionDAOImpl.java"]
  F1510_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_AnnouncementDAOImpl.java["AnnouncementDAOImpl.java"]
  F1511_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_LecturerDAO.java["LecturerDAO.java"]
  F1512_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_StudentDAOImpl.java["StudentDAOImpl.java"]
  F1513_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_MajorDAOImpl.java["MajorDAOImpl.java"]
  F1514_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_RegistrationBatchDAOImpl.java["RegistrationBatchDAOImpl.java"]
  F1515_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_MarkDAO.java["MarkDAO.java"]
  F1516_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_ScheduleDAOImpl.java["ScheduleDAOImpl.java"]
  F1517_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_MajorDAO.java["MajorDAO.java"]
  F1518_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_TermDAOImpl.java["TermDAOImpl.java"]
  F1519_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_UserProfileDAOImpl.java["UserProfileDAOImpl.java"]
  F1520_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO\Impl_MarkDAOImpl.java["MarkDAOImpl.java"]
  F1521_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_TermDAO.java["TermDAO.java"]
  F1522_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_StudentDAO.java["StudentDAO.java"]
  F1523_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\DTO_PageRequest.java["PageRequest.java"]
  F1524_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_UserProfileDAO.java["UserProfileDAO.java"]
  F1525_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_RegistrationBatchDAO.java["RegistrationBatchDAO.java"]
  F1526_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\DAO_ScheduleDAO.java["ScheduleDAO.java"]
  F1527_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\DTO_PagedResult.java["PagedResult.java"]
  F1528_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\DTO_RegistrationRequest.java["RegistrationRequest.java"]
  F1529_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\DTO_PaginationSupport.java["PaginationSupport.java"]
  F1530_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Account.java["Account.java"]
  F1531_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_CourseRegistration.java["CourseRegistration.java"]
  F1532_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Admin.java["Admin.java"]
  F1533_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_ExamSchedule.java["ExamSchedule.java"]
  F1534_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Announcement.java["Announcement.java"]
  F1535_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_CourseSection.java["CourseSection.java"]
  F1536_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Faculty.java["Faculty.java"]
  F1537_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Course.java["Course.java"]
  F1538_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Major.java["Major.java"]
  F1539_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Class.java["Class.java"]
  F1540_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Lecturer.java["Lecturer.java"]
  F1541_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Mark.java["Mark.java"]
  F1542_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_RegistrationBatch.java["RegistrationBatch.java"]
  F1543_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\EnumType_Registration.java["Registration.java"]
  F1544_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Student.java["Student.java"]
  F1545_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\EnumType_Role.java["Role.java"]
  F1546_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\EnumType_MenuItem.java["MenuItem.java"]
  F1547_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\EnumType_RegistrationStatus.java["RegistrationStatus.java"]
  F1548_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_UserProfile.java["UserProfile.java"]
  F1549_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Schedule.java["Schedule.java"]
  F1550_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Model\Entity_Term.java["Term.java"]
  F1551_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_AuthService.java["AuthService.java"]
  F1552_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute_OnlineUteApplication.java["OnlineUteApplication.java"]
  F1553_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_ClassService.java["ClassService.java"]
  F1554_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_CourseService.java["CourseService.java"]
  F1555_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_CourseSectionService.java["CourseSectionService.java"]
  F1556_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_FacultyService.java["FacultyService.java"]
  F1557_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_CourseRegistrationService.java["CourseRegistrationService.java"]
  F1558_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_AccountServiceImpl.java["AccountServiceImpl.java"]
  F1559_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_AnnouncementService.java["AnnouncementService.java"]
  F1560_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_AccountService.java["AccountService.java"]
  F1561_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_FacultyServiceImpl.java["FacultyServiceImpl.java"]
  F1562_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_AuthServiceImpl.java["AuthServiceImpl.java"]
  F1563_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_AnnouncementServiceImpl.java["AnnouncementServiceImpl.java"]
  F1564_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_ClassServiceImpl.java["ClassServiceImpl.java"]
  F1565_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_CourseServiceImpl.java["CourseServiceImpl.java"]
  F1566_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_LecturerServiceImpl.java["LecturerServiceImpl.java"]
  F1567_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_MarkServiceImpl.java["MarkServiceImpl.java"]
  F1568_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_MajorServiceImpl.java["MajorServiceImpl.java"]
  F1569_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_CourseRegistrationServiceImpl.java["CourseRegistrationServiceImpl.java"]
  F1570_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_CourseSectionServiceImpl.java["CourseSectionServiceImpl.java"]
  F1571_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_ScheduleServiceImpl.java["ScheduleServiceImpl.java"]
  F1572_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_TermServiceImpl.java["TermServiceImpl.java"]
  F1573_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_StudentServiceImpl.java["StudentServiceImpl.java"]
  F1574_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_LecturerService.java["LecturerService.java"]
  F1575_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_MarkService.java["MarkService.java"]
  F1576_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_ScheduleService.java["ScheduleService.java"]
  F1577_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_MajorService.java["MajorService.java"]
  F1578_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_RegistrationBatchServiceImpl.java["RegistrationBatchServiceImpl.java"]
  F1579_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl_UserProfileServiceImpl.java["UserProfileServiceImpl.java"]
  F1580_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_RegistrationBatchService.java["RegistrationBatchService.java"]
  F1581_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_LabelValuePanel.java["LabelValuePanel.java"]
  F1582_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_AppLogoHeader.java["AppLogoHeader.java"]
  F1583_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components\leftbar_LeftBarButton.java["LeftBarButton.java"]
  F1584_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components\leftbar_LeftBarTitle.java["LeftBarTitle.java"]
  F1585_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_InputGroup.java["InputGroup.java"]
  F1586_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components\leftbar_SidebarItem.java["SidebarItem.java"]
  F1587_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components\leftbar_NavMenu.java["NavMenu.java"]
  F1588_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_UserProfileService.java["UserProfileService.java"]
  F1589_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_TermService.java["TermService.java"]
  F1590_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service_StudentService.java["StudentService.java"]
  F1591_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components\leftbar_UserProfileCard.java["UserProfileCard.java"]
  F1592_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_PageTitleLabel.java["PageTitleLabel.java"]
  F1593_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_RoundedPainter.java["RoundedPainter.java"]
  F1594_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_PrimaryButton.java["PrimaryButton.java"]
  F1595_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_PaginationPanel.java["PaginationPanel.java"]
  F1596_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_RoundedOutlineBorder.java["RoundedOutlineBorder.java"]
  F1597_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_RoundedPanel.java["RoundedPanel.java"]
  F1598_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_SelectGroup.java["SelectGroup.java"]
  F1599_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_SearchActionTopbar.java["SearchActionTopbar.java"]
  F1600_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_RoundedTitleBorder.java["RoundedTitleBorder.java"]
  F1601_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_TableStyles.java["TableStyles.java"]
  F1602_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\announcement_AnnouncementPage.java["AnnouncementPage.java"]
  F1603_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_TagChip.java["TagChip.java"]
  F1604_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\account_ChangePasswordPage.java["ChangePasswordPage.java"]
  F1605_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\announcement_AnnouncementTable.java["AnnouncementTable.java"]
  F1606_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\announcement_CreateAnnouncementPage.java["CreateAnnouncementPage.java"]
  F1607_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\Components_TextAreaGroup.java["TextAreaGroup.java"]
  F1608_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\auth_LoginForm.java["LoginForm.java"]
  F1609_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\attendance_AttendancePage.java["AttendancePage.java"]
  F1610_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\account_CreateAccountPage.java["CreateAccountPage.java"]
  F1611_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\auth_LoginScreen.java["LoginScreen.java"]
  F1612_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\dashboard_DashboardLayout.java["DashboardLayout.java"]
  F1613_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\data_ManagementShellPage.java["ManagementShellPage.java"]
  F1614_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\dashboard_Sidebar.java["Sidebar.java"]
  F1615_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\dashboard_MainContent.java["MainContent.java"]
  F1616_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\data_DataManagementPage.java["DataManagementPage.java"]
  F1617_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\data_EntityTablePanel.java["EntityTablePanel.java"]
  F1618_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\dashboard_PageScaffold.java["PageScaffold.java"]
  F1619_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\data_SimpleEntityManagementPage.java["SimpleEntityManagementPage.java"]
  F1620_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\dashboard_TopHeader.java["TopHeader.java"]
  F1621_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\data_SimpleManagementDashboard.java["SimpleManagementDashboard.java"]
  F1622_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\grade_InputGradesPage.java["InputGradesPage.java"]
  F1623_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\lecturer_LecturerManagementDashboard.java["LecturerManagementDashboard.java"]
  F1624_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\lecturer_LecturerManagementPage.java["LecturerManagementPage.java"]
  F1625_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\registration_CourseRegistrationPage.java["CourseRegistrationPage.java"]
  F1626_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\profile_ProfilePage.java["ProfilePage.java"]
  F1627_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\profile_ProfileSectionCard.java["ProfileSectionCard.java"]
  F1628_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\grade_ViewGradesPage.java["ViewGradesPage.java"]
  F1629_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\registration_CourseSectionDialog.java["CourseSectionDialog.java"]
  F1630_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\lecturer_LecturerSearchResultPanel.java["LecturerSearchResultPanel.java"]
  F1631_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\registration_CreateRegistrationBatchPage.java["CreateRegistrationBatchPage.java"]
  F1632_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\student_StudentSearchResultPanel.java["StudentSearchResultPanel.java"]
  F1633_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\schedule_SchedulePage.java["SchedulePage.java"]
  F1634_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\student_StudentManagementDashboard.java["StudentManagementDashboard.java"]
  F1635_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\student_StudentManagementPage.java["StudentManagementPage.java"]
  F1636_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\navigation_Refreshable.java["Refreshable.java"]
  F1637_c:\Users\bangn\Downloads\OnlineUTE\src\main\resources_application.properties["application.properties"]
  F1638_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View_WindowManager.java["WindowManager.java"]
  F1639_c:\Users\bangn\Downloads\OnlineUTE\src\main\resources\META-INF_persistence.xml["persistence.xml"]
  F1640_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\navigation_MainNavigator.java["MainNavigator.java"]
  F1641_c:\Users\bangn\Downloads\OnlineUTE\src\test\java\com\bangcompany\onlineute_OnlineUteApplicationTests.java["OnlineUteApplicationTests.java"]
  F1642_c:\Users\bangn\Downloads\OnlineUTE\target\classes_application.properties["application.properties"]
  F1643_c:\Users\bangn\Downloads\OnlineUTE\target\classes\META-INF_persistence.xml["persistence.xml"]
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql --> F1632_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\student_StudentSearchResultPanel.java
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql --> F1623_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\lecturer_LecturerManagementDashboard.java
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql --> F1604_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\account_ChangePasswordPage.java
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql --> F1633_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\schedule_SchedulePage.java
  F1388_c:\Users\bangn\Downloads\OnlineUTE\database_setup.sql --> F1602_c:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\View\features\announcement_AnnouncementPage.java
```

