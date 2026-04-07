/**
 * Quản lý sinh viên
 */
package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.container.Dashboard;
import com.bangcompany.onlineute.View.Components.container.EntityTablePanel;
import com.bangcompany.onlineute.View.Components.container.ManagementShellPage;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.FormRow;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final Dashboard dashboardPanel;
    private final EntityTablePanel<Student> searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";
    private Student selectedStudent;
    private TextInput codeInput;
    private TextInput fullNameInput;
    private TextInput emailInput;
    private TextInput enrollmentYearInput;
    private SelectInput<Class> classSelect;
    private Button saveButton;
    private Button deleteButton;

    // khởi tạo page với dashboard tổng quan
    public StudentManagementPage() {
        this(createDashboard());
    }

    private StudentManagementPage(Dashboard dashboardPanel) {
        super(
                "Tìm theo mã sinh viên, họ tên, email, lớp, khoa",
                null,
                null,
                2,
                dashboardPanel,
                createResultPanel()
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (EntityTablePanel<Student>) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        this.searchResultPanel.setSelectionHandler(this::showEditorForStudent);
        this.searchResultPanel.setDetailPanel(buildEditorPanel());
        configureCreateAction("Tạo mới", this::openCreateStudentDialog);
    }

    // xử lý khi gõ tìm kiếm
    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword;
        currentPage = 1;
        if (isAllKeyword(keyword)) {
            loadAllStudents();
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
        PagedResult<Student> result = AppContext.getStudentController()
                .searchStudents(currentKeyword, currentPage, PAGE_SIZE);
        searchResultPanel.showItems(result.getItems(), currentKeyword);
        searchResultPanel.updateResultLabel(result.getTotalItems(), currentKeyword);
        searchResultPanel.updatePagination(result.getPage(), result.getTotalPages(), result.hasPrevious(), result.hasNext());
        showResults();
    }

    // load hết luôn khi gõ "all"
    private void loadAllStudents() {
        List<Student> students = AppContext.getStudentController().getAllStudents();
        searchResultPanel.showItems(students, currentKeyword);
        searchResultPanel.updateResultLabel(students == null ? 0 : students.size(), currentKeyword);
        searchResultPanel.updatePagination(1, 1, false, false);
        showResults();
    }

    // build cái form chỉnh sửa hiện ở dưới table
    private JComponent buildEditorPanel() {
        Card card = new Card();
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);

        JLabel title = new JLabel("CHỈNH SỬA SINH VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(AppTheme.PRIMARY_BLUE);
        wrapper.add(title, BorderLayout.NORTH);

        codeInput = new TextInput("Mã số sinh viên", false);
        codeInput.setEditable(false);
        fullNameInput = new TextInput("Họ và tên", false);
        emailInput = new TextInput("Email", false);
        enrollmentYearInput = new TextInput("Năm nhập học", false);
        classSelect = new SelectInput<>("Lớp", AppContext.getClassService().getAllClasses());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(FormRow.two(codeInput, fullNameInput));
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(FormRow.two(emailInput, enrollmentYearInput));
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(FormRow.single(classSelect));
        wrapper.add(form, BorderLayout.CENTER);

        saveButton = new Button("Lưu");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveStudentEdits());

        deleteButton = new Button("Xóa", new Color(220, 53, 69));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.addActionListener(e -> deleteSelectedStudent());

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
    private void showEditorForStudent(Student student) {
        selectedStudent = student;
        if (student == null) {
            return;
        }
        codeInput.setValue(student.getCode());
        fullNameInput.setValue(student.getFullName());
        emailInput.setValue(student.getEmail());
        enrollmentYearInput.setValue(student.getEnrollmentYear() == null ? "" : String.valueOf(student.getEnrollmentYear()));
        classSelect.setSelectedItem(student.getClassEntity());
    }

    // lưu lại thay đổi
    private void saveStudentEdits() {
        if (selectedStudent == null) {
            return;
        }
        String fullName = fullNameInput.getValue().trim();
        String email = emailInput.getValue().trim();
        String yearText = enrollmentYearInput.getValue().trim();

        if (fullName.isEmpty() || email.isEmpty() || yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Năm nhập học không hợp lệ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedStudent.setFullName(fullName);
        selectedStudent.setEmail(email);
        selectedStudent.setEnrollmentYear(year);
        selectedStudent.setClassEntity(classSelect.getSelectedValue());

        AppContext.getStudentController().updateStudent(selectedStudent);
        onEnter();
    }

    // xóa sv hiện tại
    private void deleteSelectedStudent() {
        if (selectedStudent == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa sinh viên đã chọn?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        AppContext.getStudentController().deleteStudent(selectedStudent.getId());
        selectedStudent = null;
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
    private void openCreateStudentDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tạo mới sinh viên", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Sinh viên"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }

    // tạo 3 cái thẻ thống kê ở dashboard
    private static Dashboard createDashboard() {
        List<Dashboard.Metric> metrics = List.of(
                new Dashboard.Metric(
                        "Tổng sinh viên",
                        () -> AppContext.getStudentController().countAllStudents(),
                        AppTheme.PRIMARY_BLUE
                ),
                new Dashboard.Metric(
                        "Tổng lớp",
                        () -> (long) AppContext.getClassService().getAllClasses().size(),
                        new Color(0, 123, 167)
                ),
                new Dashboard.Metric(
                        "Tổng khoa",
                        () -> (long) AppContext.getFacultyService().getAllFaculties().size(),
                        new Color(19, 135, 84)
                )
        );

        return new Dashboard(
                metrics,
                "<html>Tìm nhanh theo <b>mã sinh viên</b>, <b>họ tên</b>, <b>email</b>, <b>lớp</b> hoặc <b>khoa</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị dashboard tổng quan.</html>"
        );
    }

    // cấu hình mapping dữ liệu vào table
    private static EntityTablePanel<Student> createResultPanel() {
        return new EntityTablePanel<>(
                new String[]{"Mã SV", "Họ tên", "Email", "Lớp", "Khoa", "Năm nhập học"},
                student -> new Object[]{
                        student.getCode(),
                        student.getFullName(),
                        student.getEmail(),
                        student.getClassEntity() != null ? student.getClassEntity().getClassName() : "",
                        student.getClassEntity() != null && student.getClassEntity().getMajor() != null
                                && student.getClassEntity().getMajor().getFaculty() != null
                                ? student.getClassEntity().getMajor().getFaculty().getFullName()
                                : "",
                        student.getEnrollmentYear()
                },
                student -> {
                    String className = student.getClassEntity() != null ? student.getClassEntity().getClassName() : "";
                    String facultyName = student.getClassEntity() != null && student.getClassEntity().getMajor() != null
                            && student.getClassEntity().getMajor().getFaculty() != null
                            ? student.getClassEntity().getMajor().getFaculty().getFullName()
                            : "";
                    return student.getCode() + " " + student.getFullName() + " " + student.getEmail() + " "
                            + className + " " + facultyName;
                },
                null
        );
    }
}
