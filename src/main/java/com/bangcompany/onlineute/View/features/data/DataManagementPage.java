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
