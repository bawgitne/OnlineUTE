/**
 * đăng ký môn học
 */
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.RegistrationBatch;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.DateUtils;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.Components.ui.Tabs;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseRegistrationPage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ViewContext viewContext;

    private Tabs batchTabs = new Tabs();
    private final JPanel tabsContainer = new JPanel(new BorderLayout());
    private final List<BatchTab> tabItems = new ArrayList<>();

    // class gom data cho mỗi tab (mỗi đợt)
    private static final class BatchTab {
        private final RegistrationBatch batch;
        private final Table table;
        private final JLabel infoLabel;
        private final List<CourseSection> sections = new ArrayList<>();
        private final JPanel panel;

        private BatchTab(RegistrationBatch batch, Table table, JLabel infoLabel, JPanel panel) {
            this.batch = batch;
            this.table = table;
            this.infoLabel = infoLabel;
            this.panel = panel;
        }
    }

    // khởi tạo trang đăng ký môn
    public CourseRegistrationPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(Card.titleCard("ĐĂNG KÝ MÔN HỌC"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        loadOpenBatches();
    }

    private JComponent createBody() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.add(createTabsContainer(), BorderLayout.CENTER);
        return container;
    }

    // vùng chứa các tab đợt đăng ký môn
    private JComponent createTabsContainer() {
        tabsContainer.setOpaque(false);
        return tabsContainer;
    }

    // hiện lúc ko có đợt nào đang mở
    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        Card card = new Card(AppTheme.RADIUS_CARD, new Insets(18, 18, 18, 18));
        card.setLayout(new BorderLayout());
        JLabel label = new JLabel("Không có đợt khả thi để đăng ký.", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(AppTheme.TEXT_MUTED);
        card.add(label, BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    // build nội dung cho 1 tab (bảng lớp học phần)
    private BatchTab buildBatchTab(RegistrationBatch batch) {
        Table table = new Table(new String[]{"ID", "Mã lớp", "Môn học", "Giảng viên", "Phòng", "Thứ", "Tiết", "Còn chỗ", "Trạng thái"}, 12, 44);
        table.setOnRowSelected(index -> {});

        JLabel infoLabel = new JLabel("", SwingConstants.LEFT);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(80, 90, 100));

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        panel.add(buildActionsPanel(table, batch), BorderLayout.SOUTH);

        BatchTab tab = new BatchTab(batch, table, infoLabel, panel);
        loadSectionsForTab(tab);
        return tab;
    }

    // cụm nút bấm đăng ký dưới bảng
    private JPanel buildActionsPanel(Table table, RegistrationBatch batch) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        Button registerButton = new Button("Đăng ký lớp đã chọn");
        registerButton.setPreferredSize(new Dimension(190, 42));
        registerButton.addActionListener(e -> registerSelectedSection(batch, table));
        actions.add(registerButton);

        return actions;
    }

    // load mấy đợt đang mở từ db
    private void loadOpenBatches() {
        tabItems.clear();
        batchTabs = new Tabs();
        tabsContainer.removeAll();

        List<RegistrationBatch> openBatches = viewContext.getRegistrationBatchController().getOpenBatches(LocalDateTime.now());
        if (openBatches == null || openBatches.isEmpty()) {
            tabsContainer.add(createEmptyPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }

        for (RegistrationBatch batch : openBatches) {
            BatchTab tab = buildBatchTab(batch);
            tabItems.add(tab);
            batchTabs.addTab(batch.getName(), tab.panel);
        }

        tabsContainer.add(batchTabs, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // nạp danh sách lớp vào bảng tương ứng
    private void loadSectionsForTab(BatchTab tab) {
        tab.table.clearRows();
        tab.sections.clear();

        RegistrationBatch batch = tab.batch;
        tab.infoLabel.setText("Đợt " + batch.getName() + " | Học kỳ " + batch.getTerm() + " | Bắt đầu học " + DATE_FORMATTER.format(batch.getCommonStartDate()));

        Set<Long> registeredSectionIds = getRegisteredSectionIds();
        List<CourseSection> sections = viewContext.getCourseSectionController().getSectionsByBatch(batch.getId());
        for (CourseSection section : sections) {
            int current = section.getCurrentCapacity() == null ? 0 : section.getCurrentCapacity();
            int max = section.getMaxCapacity() == null ? 0 : section.getMaxCapacity();
            String availability = current + "/" + max;
            String state = registeredSectionIds.contains(section.getId()) ? "Đã đăng ký" : (current >= max ? "Đã đầy" : "Có thể đăng ký");

            tab.sections.add(section);
            tab.table.addRow(
                    section.getId(),
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    section.getRoom(),
                    DateUtils.formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    availability,
                    state
            );
        }
    }

    // lấy danh sách môn đã đăng ký để check trùng
    private Set<Long> getRegisteredSectionIds() {
        Set<Long> ids = new HashSet<>();
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return ids;
        }
        for (CourseRegistration registration : viewContext.getCourseRegistrationController().getStudentRegistrations(student.getId())) {
            if (registration.getCourseSection() != null) {
                ids.add(registration.getCourseSection().getId());
            }
        }
        return ids;
    }

    // gửi lệnh đăng ký môn lên database
    private void registerSelectedSection(RegistrationBatch batch, Table table) {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            ExceptionHandler.showError(this, new IllegalArgumentException("Không tìm thấy thông tin sinh viên đang đăng nhập."), "Lỗi");
            return;
        }

        int selectedIndex = table.getSelectedIndex();
        BatchTab tab = findTabByBatch(batch);
        if (tab == null || selectedIndex < 0 || selectedIndex >= tab.sections.size()) {
            ExceptionHandler.showError(this, new IllegalArgumentException("Vui lòng chọn một lớp học phần trước."), "Thông báo");
            return;
        }

        Long sectionId = tab.sections.get(selectedIndex).getId();
        try {
            viewContext.getCourseRegistrationController().registerStudentToSection(student.getId(), sectionId);
            ExceptionHandler.showInfo(this, "Đăng ký môn học thành công.", "Thành công");
            loadSectionsForTab(tab); // Tải lại bảng để cập nhật trạng thái "Đã đăng ký"
        } catch (Exception ex) {
            ExceptionHandler.showError(this, ex, "Không thể đăng ký.");
        }
    }

    // tìm tab theo mã đợt đăng ký
    private BatchTab findTabByBatch(RegistrationBatch batch) {
        for (BatchTab tab : tabItems) {
            if (tab.batch != null && tab.batch.getId() != null && tab.batch.getId().equals(batch.getId())) {
                return tab;
            }
        }
        return null;
    }

    // hiện tiết học kiểu 1-3
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
