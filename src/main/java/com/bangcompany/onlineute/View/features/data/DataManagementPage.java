/**
 * Data management tabs
 */
package com.bangcompany.onlineute.View.features.data;

import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Tabs;
import com.bangcompany.onlineute.View.features.dataManagement.DataManagementDialogLogic;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DataManagementPage extends JPanel implements Refreshable {
    private final Tabs tabs = new Tabs();
    private final ViewContext viewContext;
    private final SimpleEntityManagementPage<Student> studentManagementPage;
    private final SimpleEntityManagementPage<Lecturer> lecturerManagementPage;
    private final SimpleEntityManagementPage<Faculty> facultyManagementPage;
    private final SimpleEntityManagementPage<Major> majorManagementPage;
    private final SimpleEntityManagementPage<Class> classManagementPage;
    private final SimpleEntityManagementPage<Course> courseManagementPage;

    public DataManagementPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        DataManagementDialogLogic.setContext(viewContext);
        studentManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo mã sinh viên, họ tên, email, lớp, khoa",
                "Tổng sinh viên",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateStudent(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getStudentController().searchStudents(keyword, page, pageSize),
                () -> viewContext.getStudentController().countAllStudents(),
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
                }
        );
        studentManagementPage.setSelectionHandler(student ->
                DataManagementDialogLogic.openEditStudent(this, student, this::refreshSelectedTab));

        lecturerManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo mã giảng viên, họ tên",
                "Tổng giảng viên",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateLecturer(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getLecturerController().searchLecturers(keyword, page, pageSize),
                () -> viewContext.getLecturerController().countAllLecturers(),
                new String[]{"Mã GV", "Họ tên"},
                lecturer -> new Object[]{
                        lecturer.getCode(),
                        lecturer.getFullName()
                },
                lecturer -> lecturer.getCode() + " " + lecturer.getFullName()
        );
        lecturerManagementPage.setSelectionHandler(lecturer ->
                DataManagementDialogLogic.openEditLecturer(this, lecturer, this::refreshSelectedTab));

        facultyManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo mã khoa, tên khoa",
                "Tổng khoa",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateFaculty(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getFacultyController().searchFaculties(keyword, page, pageSize),
                () -> viewContext.getFacultyController().countAllFaculties(),
                new String[]{"Mã khoa", "Tên khoa"},
                faculty -> new Object[]{faculty.getFacultyCode(), faculty.getFullName()},
                faculty -> faculty.getFacultyCode() + " " + faculty.getFullName()
        );
        facultyManagementPage.setSelectionHandler(faculty ->
                DataManagementDialogLogic.openEditFaculty(this, faculty, this::refreshSelectedTab));

        majorManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo mã ngành, tên ngành, khoa",
                "Tổng ngành",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateMajor(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getMajorController().searchMajors(keyword, page, pageSize),
                () -> viewContext.getMajorController().countAllMajors(),
                new String[]{"Mã ngành", "Tên ngành", "Khoa", "Tổng tín chỉ"},
                major -> new Object[]{
                        major.getMajorCode(),
                        major.getFullName(),
                        major.getFaculty() == null ? "" : major.getFaculty().getFullName(),
                        major.getTotalCredit()
                },
                major -> major.getMajorCode() + " " + major.getFullName()
                        + " " + (major.getFaculty() == null ? "" : major.getFaculty().getFullName())
        );
        majorManagementPage.setSelectionHandler(major ->
                DataManagementDialogLogic.openEditMajor(this, major, this::refreshSelectedTab));

        classManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo lớp, khoa",
                "Tổng lớp",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateClass(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getClassController().searchClasses(keyword, page, pageSize),
                () -> viewContext.getClassController().countAllClasses(),
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
        classManagementPage.setSelectionHandler(classEntity ->
                DataManagementDialogLogic.openEditClass(this, classEntity, this::refreshSelectedTab));

        courseManagementPage = new SimpleEntityManagementPage<>(
                "Tìm theo mã môn, tên môn",
                "Tổng môn học",
                "",
                "Tạo mới",
                () -> DataManagementDialogLogic.openCreateCourse(this, this::refreshSelectedTab),
                (keyword, page, pageSize) -> viewContext.getCourseController().searchCourses(keyword, page, pageSize),
                () -> viewContext.getCourseController().countAllCourses(),
                new String[]{"Mã môn", "Tên môn", "Số tín chỉ"},
                course -> new Object[]{course.getCourseCode(), course.getFullName(), course.getCredit()},
                course -> course.getCourseCode() + " " + course.getFullName()
        );
        courseManagementPage.setSelectionHandler(course ->
                DataManagementDialogLogic.openEditCourse(this, course, this::refreshSelectedTab));

        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        Card titleLabel = Card.titleCard("Quản lý dữ liệu");

        tabs.addTab("Sinh viên", studentManagementPage);
        tabs.addTab("Giảng viên", lecturerManagementPage);
        tabs.addTab("Khoa", facultyManagementPage);
        tabs.addTab("Ngành", majorManagementPage);
        tabs.addTab("Lớp", classManagementPage);
        tabs.addTab("Môn học", courseManagementPage);
        tabs.setOnTabChanged(index -> refreshSelectedTab());

        add(titleLabel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        refreshSelectedTab();
    }

    private void refreshSelectedTab() {
        Component selectedComponent = tabs.getActiveComponent();
        if (selectedComponent instanceof Refreshable refreshable) {
            refreshable.onEnter();
        }
    }
}
