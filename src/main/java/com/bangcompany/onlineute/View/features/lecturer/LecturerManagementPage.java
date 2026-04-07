/**
 * Quản lý giảng viên
 */
package com.bangcompany.onlineute.View.features.lecturer;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.View.Components.container.Dashboard;
import com.bangcompany.onlineute.View.Components.container.EntityTablePanel;
import com.bangcompany.onlineute.View.Components.container.ManagementShellPage;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.FormRow;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LecturerManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final Dashboard dashboardPanel;
    private final EntityTablePanel<Lecturer> searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";
    private Lecturer selectedLecturer;
    private TextInput codeInput;
    private TextInput fullNameInput;
    private Button saveButton;
    private Button deleteButton;

    // khởi tạo page với dashboard tổng quan
    public LecturerManagementPage() {
        this(createDashboard());
    }

    private LecturerManagementPage(Dashboard dashboardPanel) {
        super(
                "Tìm theo mã giảng viên, họ tên",
                null,
                null,
                2,
                dashboardPanel,
                createResultPanel()
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (EntityTablePanel<Lecturer>) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        this.searchResultPanel.setSelectionHandler(this::showEditorForLecturer);
        this.searchResultPanel.setDetailPanel(buildEditorPanel());
        configureCreateAction("Tạo mới", this::openCreateLecturerDialog);
    }

    // xử lý khi gõ tìm kiếm
    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword;
        currentPage = 1;
        if (isAllKeyword(keyword)) {
            loadAllLecturers();
        } else {
            loadCurrentPage();
        }
    }

    // xóa filter thì về dashboard
    @Override
    protected void onKeywordCleared() {
        currentKeyword = "";
        currentPage = 1;
        dashboardPanel.refreshData();
    }

    // load data phân trang
    private void loadCurrentPage() {
        PagedResult<Lecturer> result = AppContext.getLecturerController()
                .searchLecturers(currentKeyword, currentPage, PAGE_SIZE);
        searchResultPanel.showItems(result.getItems(), currentKeyword);
        searchResultPanel.updateResultLabel(result.getTotalItems(), currentKeyword);
        searchResultPanel.updatePagination(result.getPage(), result.getTotalPages(), result.hasPrevious(), result.hasNext());
        showResults();
    }

    // load hết luôn khi gõ "all"
    private void loadAllLecturers() {
        List<Lecturer> lecturers = AppContext.getLecturerController().getAllLecturers();
        searchResultPanel.showItems(lecturers, currentKeyword);
        searchResultPanel.updateResultLabel(lecturers == null ? 0 : lecturers.size(), currentKeyword);
        searchResultPanel.updatePagination(1, 1, false, false);
        showResults();
    }

    // build cái form chỉnh sửa hiện ở dưới table
    private JComponent buildEditorPanel() {
        Card card = new Card();
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);

        JLabel title = new JLabel("CHỈNH SỬA GIẢNG VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AppTheme.PRIMARY_BLUE);
        wrapper.add(title, BorderLayout.NORTH);

        codeInput = new TextInput("Mã giảng viên", false);
        codeInput.setEditable(false);
        fullNameInput = new TextInput("Họ và tên", false);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(FormRow.two(codeInput, fullNameInput));
        wrapper.add(form, BorderLayout.CENTER);

        saveButton = new Button("Lưu");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveLecturerEdits());

        deleteButton = new Button("Xóa", new Color(220, 53, 69));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.addActionListener(e -> deleteSelectedLecturer());

        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(deleteButton);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(saveButton);
        actions.add(left, BorderLayout.WEST);
        actions.add(right, BorderLayout.EAST);
        wrapper.add(actions, BorderLayout.SOUTH);

        card.add(wrapper, BorderLayout.CENTER);
        return card;
    }

    // điền data vào form khi chọn 1 row
    private void showEditorForLecturer(Lecturer lecturer) {
        selectedLecturer = lecturer;
        if (lecturer == null) {
            return;
        }
        codeInput.setValue(lecturer.getCode());
        fullNameInput.setValue(lecturer.getFullName());
    }

    // lưu lại thay đổi
    private void saveLecturerEdits() {
        if (selectedLecturer == null) {
            return;
        }
        String fullName = fullNameInput.getValue().trim();
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedLecturer.setFullName(fullName);
        AppContext.getLecturerController().updateLecturer(selectedLecturer);
        onEnter();
    }

    // xóa giảng viên hiện tại
    private void deleteSelectedLecturer() {
        if (selectedLecturer == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa giảng viên đã chọn?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        AppContext.getLecturerController().deleteLecturer(selectedLecturer.getId());
        selectedLecturer = null;
        onEnter();
    }

    // đổi trang tới/lui
    private void changePage(int direction) {
        int nextPage = currentPage + direction;
        if (nextPage < 1) {
            return;
        }
        currentPage = nextPage;
        loadCurrentPage();
    }

    private boolean isAllKeyword(String keyword) {
        return keyword != null && "all".equalsIgnoreCase(keyword.trim());
    }

    // hiện dialog tạo mới
    private void openCreateLecturerDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tạo mới giảng viên", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Giảng viên"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }

    // tạo thẻ thống kê ở dashboard
    private static Dashboard createDashboard() {
        List<Dashboard.Metric> metrics = List.of(
                new Dashboard.Metric(
                        "Tổng giảng viên",
                        () -> AppContext.getLecturerController().countAllLecturers(),
                        AppTheme.PRIMARY_BLUE
                )
        );

        return new Dashboard(
                metrics,
                "<html>Tìm nhanh theo <b>mã giảng viên</b> hoặc <b>họ tên</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị dashboard tổng quan.</html>"
        );
    }

    // cấu hình mapping dữ liệu vào table
    private static EntityTablePanel<Lecturer> createResultPanel() {
        return new EntityTablePanel<>(
                new String[]{"Mã GV", "Họ tên"},
                lecturer -> new Object[]{
                        lecturer.getCode(),
                        lecturer.getFullName()
                },
                lecturer -> lecturer.getCode() + " " + lecturer.getFullName(),
                null
        );
    }
}
