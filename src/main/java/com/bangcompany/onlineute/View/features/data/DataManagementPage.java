/**
 * Quản lý dữ liệu hệ thống
 */
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.features.lecturer.LecturerManagementPage;
import com.bangcompany.onlineute.View.features.student.StudentManagementPage;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DataManagementPage extends JPanel implements Refreshable {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final StudentManagementPage studentManagementPage = new StudentManagementPage();
    private final LecturerManagementPage lecturerManagementPage = new LecturerManagementPage();
    
    // trang quản lý khoa
    private final SimpleEntityManagementPage<Faculty> facultyManagementPage = new SimpleEntityManagementPage<>(
            "Tìm theo mã khoa, tên khoa",
            "Tổng khoa",
            "<html>Tìm nhanh theo <b>mã khoa</b> hoặc <b>tên khoa</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị tổng quan dữ liệu.</html>",
            "Tạo mới",
            this::openCreateFacultyDialog,
            () -> AppContext.getFacultyService().getAllFaculties(),
            new String[]{
                    "Mã khoa",
                    "Tên khoa"
            },
            faculty -> new Object[]{faculty.getFacultyCode(), faculty.getFullName()},
            faculty -> faculty.getFacultyCode() + " " + faculty.getFullName()
    );

    // trang quản lý ngành
    private final SimpleEntityManagementPage<Major> majorManagementPage = new SimpleEntityManagementPage<>(
            "Tìm theo mã ngành, tên ngành, khoa",
            "Tổng ngành",
            "<html>Tìm nhanh theo <b>mã ngành</b>, <b>tên ngành</b> hoặc <b>khoa</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị tổng quan dữ liệu.</html>",
            "Tạo mới",
            this::openCreateMajorDialog,
            () -> AppContext.getMajorService().getAllMajors(),
            new String[]{
                    "Mã ngành",
                    "Tên ngành",
                    "Khoa",
                    "Tổng tín chỉ"
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

    // trang quản lý lớp
    private final SimpleEntityManagementPage<Class> classManagementPage = new SimpleEntityManagementPage<>(
            "Tìm theo lớp, khoa",
            "Tổng lớp",
            "<html>Tìm nhanh theo <b>tên lớp</b> hoặc <b>khoa</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị tổng quan dữ liệu.</html>",
            "Tạo mới",
            this::openCreateClassDialog,
            () -> AppContext.getClassService().getAllClasses(),
            new String[]{"Lớp", "Ngành", "Khoa"},
            classEntity -> new Object[]{
                    classEntity.getClassName(),
                    classEntity.getMajor() == null ? "" : classEntity.getMajor().getFullName(),
                    classEntity.getMajor() != null && classEntity.getMajor().getFaculty() != null
                            ? classEntity.getMajor().getFaculty().getFullName()
                            : ""
            },
            classEntity -> classEntity.getClassName()
                    + " " + (classEntity.getMajor() == null ? "" : classEntity.getMajor().getFullName())
                    + " " + (classEntity.getMajor() != null && classEntity.getMajor().getFaculty() != null
                    ? classEntity.getMajor().getFaculty().getFullName()
                    : "")
    );

    // trang quản lý môn học
    private final SimpleEntityManagementPage<Course> courseManagementPage = new SimpleEntityManagementPage<>(
            "Tìm theo mã môn, tên môn",
            "Tổng môn học",
            "<html>Tìm nhanh theo <b>mã môn</b> hoặc <b>tên môn</b>.<br>Khi ô tìm kiếm còn trống, màn này sẽ hiển thị tổng quan dữ liệu.</html>",
            "Tạo mới",
            this::openCreateCourseDialog,
            () -> AppContext.getCourseService().getAllCourses(),
            new String[]{
                    "Mã môn",
                    "Tên môn",
                    "Số tín chỉ"
            },
            course -> new Object[]{course.getCourseCode(), course.getFullName(), course.getCredit()},
            course -> course.getCourseCode() + " " + course.getFullName()
    );

    // khởi tạo các tab quản lý
    public DataManagementPage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        Card titleLabel = Card.titleCard("Quản lý dữ liệu");

        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("Sinh viên", studentManagementPage);
        tabbedPane.addTab("Giảng viên", lecturerManagementPage);
        tabbedPane.addTab("Khoa", facultyManagementPage);
        tabbedPane.addTab("Ngành", majorManagementPage);
        tabbedPane.addTab("Lớp", classManagementPage);
        tabbedPane.addTab("Môn học", courseManagementPage);
        tabbedPane.addChangeListener(e -> refreshSelectedTab());

        add(titleLabel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        refreshSelectedTab();
    }

    // reload tab đang chọn
    private void refreshSelectedTab() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }

    // dialog tạo mới khoa
    private void openCreateFacultyDialog() {
        TextInput codeInput = new TextInput("Mã khoa", false);
        TextInput nameInput = new TextInput("Tên khoa", false);

        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);

        openSimpleDialog("Tạo mới khoa", form, () -> {
            Faculty faculty = new Faculty();
            faculty.setFacultyCode(codeInput.getValue().trim());
            faculty.setFullName(nameInput.getValue().trim());
            AppContext.getFacultyController().createFaculty(faculty);
            facultyManagementPage.onEnter();
        });
    }

    // dialog tạo mới ngành
    private void openCreateMajorDialog() {
        TextInput codeInput = new TextInput("Mã ngành", false);
        TextInput nameInput = new TextInput("Tên ngành", false);
        TextInput creditInput = new TextInput("Tổng tín chỉ", false);
        SelectInput<Faculty> facultySelect = new SelectInput<>("Khoa", AppContext.getFacultyService().getAllFaculties());

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);
        form.add(creditInput);
        form.add(facultySelect);

        openSimpleDialog("Tạo mới ngành", form, () -> {
            Major major = new Major();
            major.setMajorCode(codeInput.getValue().trim());
            major.setFullName(nameInput.getValue().trim());
            major.setTotalCredit(Integer.parseInt(creditInput.getValue().trim()));
            major.setFaculty(facultySelect.getSelectedValue());
            AppContext.getMajorController().createMajor(major);
            majorManagementPage.onEnter();
        });
    }

    // dialog tạo mới lớp
    private void openCreateClassDialog() {
        TextInput nameInput = new TextInput("Tên lớp", false);
        SelectInput<Major> majorSelect = new SelectInput<>("Ngành", AppContext.getMajorService().getAllMajors());

        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        form.setOpaque(false);
        form.add(nameInput);
        form.add(majorSelect);

        openSimpleDialog("Tạo mới lớp", form, () -> {
            Class classEntity = new Class();
            classEntity.setClassName(nameInput.getValue().trim());
            classEntity.setMajor(majorSelect.getSelectedValue());
            AppContext.getClassController().createClass(classEntity);
            classManagementPage.onEnter();
        });
    }

    // dialog tạo mới môn học
    private void openCreateCourseDialog() {
        TextInput codeInput = new TextInput("Mã môn", false);
        TextInput nameInput = new TextInput("Tên môn", false);
        TextInput creditInput = new TextInput("Số tín chỉ", false);

        JPanel form = new JPanel(new GridLayout(3, 1, 0, 12));
        form.setOpaque(false);
        form.add(codeInput);
        form.add(nameInput);
        form.add(creditInput);

        openSimpleDialog("Tạo mới môn học", form, () -> {
            Course course = new Course();
            course.setCourseCode(codeInput.getValue().trim());
            course.setFullName(nameInput.getValue().trim());
            course.setCredit(Integer.parseInt(creditInput.getValue().trim()));
            AppContext.getCourseController().createCourse(course);
            courseManagementPage.onEnter();
        });
    }

    // hàm dựng dialog chung cho mấy cái create đơn giản
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
        Button saveButton = new Button("Lưu");
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
