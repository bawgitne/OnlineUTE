/**
 * cái bảng
 */
package com.bangcompany.onlineute.View.Components.ui;

import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.SwingUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class Table extends JPanel {
    private final JPanel listPanel = new JPanel();
    private final String[] titles;
    private final int arc;
    private final int rowHeight;
    private final JPanel headerPanel;
    private final JPanel pager = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    private final Button prevButton = createPagerButton("<");
    private final Button nextButton = createPagerButton(">");
    private final JLabel pageLabel = new JLabel("", SwingConstants.CENTER);
    private IntConsumer onPageChange;
    private IntConsumer onRowSelected;
    private final List<JPanel> dataRows = new ArrayList<>();
    private int selectedIndex = -1;

    //chèn mấy cái field vào
    public Table(String[] titles) {
        this(titles, AppTheme.RADIUS_TABLE, 44);
    }

    // có thêm độ bo góc và chiều cao hàng
    public Table(String[] titles, int arc, int rowHeight) {
        this.titles = titles == null ? new String[0] : titles;
        this.arc = arc;
        this.rowHeight = rowHeight;

        setLayout(new BorderLayout());
        setOpaque(false);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        // ẩn scroll
        JScrollPane scrollPane = SwingUtils.hiddenScrollPane(listPanel);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(28);
        verticalBar.setBlockIncrement(120);

        Card card = new Card(AppTheme.RADIUS_CARD, new Insets(0, 0, 0, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel = createHeaderPanel();
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        buildPager();
    }

    // Xóa toàn bộ dữ liệu 
    public void clearRows() {
        listPanel.removeAll();
        dataRows.clear();
        selectedIndex = -1;
        refresh();
    }

    // Thêm hàng mới
    public void addRow(String... values) {
        addRowComponent(createDataRow(values));
        refresh();
    }

    // ép string cho nhiều kiểu dtaaa
    public void addRow(Object... values) {
        String[] row = null;
        if (values != null) {
            row = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                Object v = values[i];
                row[i] = v == null ? "" : String.valueOf(v);
            }
        }
        addRowComponent(createDataRow(row));
        refresh();
    }

    // Thêm nhiều cái
    public void addRows(List<String[]> rows) {
        if (rows == null) {
            return;
        }
        for (String[] row : rows) {
            addRowComponent(createDataRow(row));
        }
        refresh();
    }


    public void setEmptyRow() {
        setEmptyRow("");
    }

    public void setEmptyRow(String message) {
        addRow(new String[0]);
    }

    public void setOnPageChange(IntConsumer onPageChange) {
        this.onPageChange = onPageChange;
    }

    public void setOnRowSelected(IntConsumer onRowSelected) {
        this.onRowSelected = onRowSelected;
    }

    // lấy số trang
    public int getSelectedIndex() {
        return selectedIndex;
    }

    //  >1 thì mới có nút chyển trang
    public void setPageState(int currentPage, int totalPages) {
        int safeTotal = Math.max(1, totalPages);
        boolean showPager = safeTotal > 1;
        pager.setVisible(showPager);
        if (showPager) {
            pageLabel.setText("Trang " + currentPage + "/" + safeTotal);
        }
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage < safeTotal);
    }

    // tạo 2 cái bo góc ở trên
    private JPanel createHeaderPanel() {
        int headerHeight = 44;
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.PRIMARY_BLUE);
                int w = getWidth();
                int h = getHeight();
                int r = arc;
                int d = r * 2;

                Polygon top = new Polygon();
                top.addPoint(r, 0);
                top.addPoint(w - r, 0);
                top.addPoint(w, r);
                top.addPoint(w, h);
                top.addPoint(0, h);
                top.addPoint(0, r);
                top.addPoint(r, 0);
                g2.fillPolygon(top);

                g2.fillArc(0, 0, d, d, 90, 90);
                g2.fillArc(w - d, 0, d, d, 0, 90);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setLayout(new GridLayout(1, Math.max(1, titles.length), 0, 0));
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        header.setPreferredSize(new Dimension(0, headerHeight));

        if (titles.length == 0) {
            header.add(createHeaderLabel(""));
        } else {
            for (String title : titles) {
                header.add(createHeaderLabel(title));
            }
        }
        return header;
    }

    // thêm dòng chèn panel vô
    private void addRowComponent(JComponent row) {
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(row);
    }

    // thêm nút vô
    private void buildPager() {
        pager.setOpaque(false);
        pager.setVisible(false);

        prevButton.addActionListener(e -> triggerPageChange(-1));
        nextButton.addActionListener(e -> triggerPageChange(1));

        pager.add(pageLabel);
        pager.add(prevButton);
        pager.add(nextButton);

        add(pager, BorderLayout.SOUTH);
    }



    // Tạo nút
    private Button createPagerButton(String text) {
        Button button = new Button(text, new Color(245, 247, 250), new Color(60, 80, 110));
      button.setPreferredSize(new Dimension(36, 28));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return button;
    }

    // yêu cầu chuyển trang 
    private void triggerPageChange(int step) {
        if (onPageChange != null) {
            onPageChange.accept(step);
        }
    }

    // hàng có data
    private JComponent createDataRow(String[] values) {
        int cols = Math.max(1, titles.length);
        JPanel row = new JPanel(new GridLayout(1, cols, 0, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(236, 240, 246)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
        row.setPreferredSize(new Dimension(0, rowHeight));
        row.setMinimumSize(new Dimension(0, rowHeight));

        for (int i = 0; i < cols; i++) {
            String text = values != null && i < values.length ? values[i] : "";
            row.add(createBodyLabel(text));
        }
        int rowIndex = dataRows.size();
        dataRows.add(row);
        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedRow(rowIndex);
                if (onRowSelected != null) {
                    onRowSelected.accept(rowIndex);
                }
            }
        });
        return row;
    }

    // cập nahajt thôi
    private void refresh() {
        revalidate();
        repaint();
    }

    // text trên row đầu chứa title
    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text == null ? "" : text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        return label;
    }

    // tạo ô textx cho từng row
    private JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text == null ? "" : text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(45, 55, 70));
        label.setBorder(new EmptyBorder(10, 6, 10, 6));
        return label;
    }

    // chọn nó đổi màu xanh
    private void setSelectedRow(int index) {
        if (index < 0 || index >= dataRows.size()) {
            return;
        }
        if (selectedIndex >= 0 && selectedIndex < dataRows.size()) {
            dataRows.get(selectedIndex).setBackground(Color.WHITE);
        }
        selectedIndex = index;
        dataRows.get(selectedIndex).setBackground(new Color(235, 238, 255));
        repaint();
    }
}
