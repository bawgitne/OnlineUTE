package com.bangcompany.onlineute.View.features.dataManagement;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
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

public class DataManagementDialogLogic {
    private static ViewContext context;

    private DataManagementDialogLogic() {}

    public static void setContext(ViewContext viewContext) {
        context = viewContext;
    }

    public static void openCreateFaculty(Component parent, Runnable afterSave) {
        FacultyDialog form = new FacultyDialog();
        JDialog dialog = buildDialog(parent, "Tạo mới khoa", form, new Dimension(520, 320));
        form.setRightAction("Lưu", () -> {
            try {
                Faculty faculty = new Faculty();
                faculty.setFacultyCode(form.getCodeValue());
                faculty.setFullName(form.getNameValue());
                context.getFacultyController().createFaculty(faculty);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditFaculty(Component parent, Faculty faculty, Runnable afterSave) {
        if (faculty == null) {
            return;
        }
        FacultyDialog form = new FacultyDialog();
        form.setValues(faculty.getFacultyCode(), faculty.getFullName());
        form.setCodeEditable(false);
        JDialog dialog = buildDialog(parent, "Chỉnh sửa khoa", form, new Dimension(520, 320));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getFacultyController().deleteFaculty(faculty.getId());
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                faculty.setFullName(form.getNameValue());
                context.getFacultyController().updateFaculty(faculty);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openCreateMajor(Component parent, Runnable afterSave) {
        MajorDialog form = new MajorDialog(context.getFacultyController().getAllFaculties());
        JDialog dialog = buildDialog(parent, "Tạo mới ngành", form, new Dimension(520, 380));
        form.setRightAction("Lưu", () -> {
            try {
                Major major = new Major();
                major.setMajorCode(form.getCodeValue());
                major.setFullName(form.getNameValue());
                major.setTotalCredit(parseInt(form.getCreditValue(), 0));
                major.setFaculty(form.getSelectedFaculty());
                context.getMajorController().createMajor(major);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditMajor(Component parent, Major major, Runnable afterSave) {
        if (major == null) {
            return;
        }
        MajorDialog form = new MajorDialog(context.getFacultyController().getAllFaculties());
        form.setValues(major.getMajorCode(), major.getFullName(), major.getTotalCredit(), major.getFaculty());
        form.setCodeEditable(false);
        JDialog dialog = buildDialog(parent, "Chỉnh sửa ngành", form, new Dimension(520, 380));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getMajorController().deleteMajor(major.getId());
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                major.setFullName(form.getNameValue());
                major.setTotalCredit(parseInt(form.getCreditValue(), major.getTotalCredit() == null ? 0 : major.getTotalCredit()));
                major.setFaculty(form.getSelectedFaculty());
                context.getMajorController().updateMajor(major);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openCreateClass(Component parent, Runnable afterSave) {
        ClassDialog form = new ClassDialog(context.getMajorController().getAllMajors());
        JDialog dialog = buildDialog(parent, "Tạo mới lớp", form, new Dimension(520, 300));
        form.setRightAction("Lưu", () -> {
            try {
                Class classEntity = new Class();
                classEntity.setClassName(form.getClassNameValue());
                classEntity.setMajor(form.getSelectedMajor());
                context.getClassController().createClass(classEntity);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditClass(Component parent, Class classEntity, Runnable afterSave) {
        if (classEntity == null) {
            return;
        }
        ClassDialog form = new ClassDialog(context.getMajorController().getAllMajors());
        form.setValues(classEntity.getClassName(), classEntity.getMajor());
        JDialog dialog = buildDialog(parent, "Chỉnh sửa lớp", form, new Dimension(520, 300));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getClassController().deleteClass(classEntity.getId());
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                classEntity.setClassName(form.getClassNameValue());
                classEntity.setMajor(form.getSelectedMajor());
                context.getClassController().updateClass(classEntity);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openCreateCourse(Component parent, Runnable afterSave) {
        CourseDialog form = new CourseDialog();
        JDialog dialog = buildDialog(parent, "Tạo mới môn học", form, new Dimension(520, 320));
        form.setRightAction("Lưu", () -> {
            try {
                Course course = new Course();
                course.setCourseCode(form.getCodeValue());
                course.setFullName(form.getNameValue());
                course.setCredit(parseInt(form.getCreditValue(), 0));
                context.getCourseController().createCourse(course);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditCourse(Component parent, Course course, Runnable afterSave) {
        if (course == null) {
            return;
        }
        CourseDialog form = new CourseDialog();
        form.setValues(course.getCourseCode(), course.getFullName(), course.getCredit());
        form.setCodeEditable(false);
        JDialog dialog = buildDialog(parent, "Chỉnh sửa môn học", form, new Dimension(520, 320));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getCourseController().deleteCourse(course);
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                course.setFullName(form.getNameValue());
                course.setCredit(parseInt(form.getCreditValue(), course.getCredit()));
                context.getCourseController().updateCourse(course);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openCreateLecturer(Component parent, Runnable afterSave) {
        LecturerDialog form = new LecturerDialog();
        JDialog dialog = buildDialog(parent, "Tạo mới giảng viên", form, new Dimension(900, 640));
        form.setLeftAction(null, null, null);
        form.resetForm();
        form.setRightAction("Lưu giảng viên", () -> {
            try {
                createLecturer(form);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditLecturer(Component parent, Lecturer lecturer, Runnable afterSave) {
        if (lecturer == null) {
            return;
        }
        LecturerDialog form = new LecturerDialog();
        form.setValues(lecturer);
        form.setCodeEditable(false);
        JDialog dialog = buildDialog(parent, "Chỉnh sửa giảng viên", form, new Dimension(900, 640));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getLecturerController().deleteLecturer(lecturer.getId());
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                lecturer.setFullName(form.getFullNameValue());
                context.getLecturerController().updateLecturer(lecturer);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openCreateStudent(Component parent, Runnable afterSave) {
        StudentDialog form = new StudentDialog();
        initStudentForm(form);
        JDialog dialog = buildDialog(parent, "Tạo mới sinh viên", form, new Dimension(1100, 760));
        form.setLeftAction("Nhập dữ liệu thô", new Color(245, 247, 250), form::openBulkDialog);
        form.setBulkSaveAction(() -> {
            try {
                createBulkStudents(form);
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        form.setRightAction("Lưu sinh viên", () -> {
            try {
                createStudent(form);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    public static void openEditStudent(Component parent, Student student, Runnable afterSave) {
        if (student == null) {
            return;
        }
        StudentDialog form = new StudentDialog();
        initStudentForm(form);
        if (student.getClassEntity() != null && student.getClassEntity().getMajor() != null) {
            Faculty faculty = student.getClassEntity().getMajor().getFaculty();
            if (faculty != null) {
                form.setMajorItems(context.getMajorController().getMajorsByFaculty(faculty.getId()));
            }
        }
        form.setValuesFromStudent(student);
        JDialog dialog = buildDialog(parent, "Chỉnh sửa sinh viên", form, new Dimension(1100, 760));
        form.setLeftAction("Xóa", new Color(220, 53, 69), () -> {
            if (!confirmDelete(parent)) {
                return;
            }
            context.getStudentController().deleteStudent(student.getId());
            safeAfterSave(afterSave);
            dialog.dispose();
        });
        form.setRightAction("Lưu", () -> {
            try {
                updateStudent(form, student);
                safeAfterSave(afterSave);
                dialog.dispose();
            } catch (Exception ex) {
                showError(dialog, ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private static JDialog buildDialog(Component parent, String title, JComponent content, Dimension size) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(content);
        dialog.setSize(size);
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    private static void initStudentForm(StudentDialog form) {
        List<Faculty> faculties = context.getFacultyController().getAllFaculties();
        form.setFacultyItems(faculties);
        form.setFacultyChangeHandler(faculty -> {
            if (faculty == null) {
                form.setMajorItems(List.of());
            } else {
                form.setMajorItems(context.getMajorController().getMajorsByFaculty(faculty.getId()));
            }
        });
        form.setCodeSupplier(() -> generateNextStudentCode(form));
        form.setMajorItems(List.of());
        form.setEnrollmentYearValue(String.valueOf(Year.now().getValue()));
        form.setExpectedGraduationYearValue(String.valueOf(Year.now().getValue() + 4));
        form.setNationalityValue("Việt Nam");
        form.refreshGeneratedStudentCode();
    }

    private static void createStudent(StudentDialog form) {
        String fullName = form.getFullNameValue();
        String email = form.getEmailValue();
        String dobText = form.getDobValue();
        String enrollmentYearText = form.getEnrollmentYearValue();

        if (fullName.isEmpty() || email.isEmpty() || dobText.isEmpty() || enrollmentYearText.isEmpty()) {
            throw new IllegalArgumentException("Thiếu thông tin bắt buộc.");
        }

        Faculty selectedFaculty = form.getSelectedFaculty();
        Major selectedMajor = form.getSelectedMajor();
        if (selectedFaculty == null || selectedMajor == null) {
            throw new IllegalArgumentException("Cần chọn khoa và ngành.");
        }

        if (selectedMajor.getFaculty() == null || !Objects.equals(selectedMajor.getFaculty().getId(), selectedFaculty.getId())) {
            throw new IllegalArgumentException("Ngành phải thuộc đúng khoa.");
        }

        Class selectedClass = resolveClassByMajorAndYear(selectedFaculty, selectedMajor, enrollmentYearText);
        String code = form.getStudentCodeValue();

        Student student = new Student(code, fullName, LocalDate.parse(dobText), email, "");
        student.setClassEntity(selectedClass);
        student.setEnrollmentYear(Integer.parseInt(enrollmentYearText));

        Account account = new Account("123456", Role.STUDENT);
        Account savedAccount = context.getAccountController().createStudentAccount(account, student);
        saveUserProfile(buildStudentProfile(form, savedAccount, student, selectedFaculty, selectedMajor));
    }

    private static void updateStudent(StudentDialog form, Student student) {
        String fullName = form.getFullNameValue();
        String email = form.getEmailValue();
        String yearText = form.getEnrollmentYearValue();

        if (fullName.isEmpty() || email.isEmpty() || yearText.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin.");
        }

        int year = parseInt(yearText, student.getEnrollmentYear() == null ? 0 : student.getEnrollmentYear());
        Faculty selectedFaculty = form.getSelectedFaculty();
        Major selectedMajor = form.getSelectedMajor();
        Class selectedClass = resolveClassByMajorAndYear(selectedFaculty, selectedMajor, yearText);

        student.setFullName(fullName);
        student.setEmail(email);
        student.setEnrollmentYear(year);
        student.setClassEntity(selectedClass);
        context.getStudentController().updateStudent(student);
    }

    private static void createBulkStudents(StudentDialog form) {
        String rawData = form.getBulkDataValue().trim();
        if (rawData.isEmpty()) {
            throw new IllegalArgumentException("Chưa nhập dữ liệu.");
        }

        List<BulkStudentRow> rows = parseBulkRows(rawData);
        rows.sort(Comparator.comparing(BulkStudentRow::sortableName, String.CASE_INSENSITIVE_ORDER));

        Map<String, Integer> nextSequenceByPrefix = new HashMap<>();
        int created = 0;

        for (BulkStudentRow row : rows) {
            String prefix = buildStudentPrefix(row.enrollmentYear(), row.major().getMajorCode());
            int nextStep = nextSequenceByPrefix.compute(prefix, (key, value) -> {
                if (value == null) {
                    return (int) context.getStudentController().countStudentsByCodePrefix(prefix) + 1;
                }
                return value + 1;
            });

            String studentCode = prefix + String.format("%03d", nextStep);
            Student student = new Student(studentCode, row.fullName(), row.birthDate(), row.email(), "");
            student.setClassEntity(row.classEntity());
            student.setEnrollmentYear(row.enrollmentYear());

            Account account = new Account("123456", Role.STUDENT);
            Account savedAccount = context.getAccountController().createStudentAccount(account, student);
            saveUserProfile(buildStudentProfile(form, savedAccount, student, row.faculty(), row.major()));
            created++;
        }

        JOptionPane.showMessageDialog(form, "Đã tạo " + created + " sinh viên.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private static List<BulkStudentRow> parseBulkRows(String rawData) {
        Map<String, Faculty> facultyByCode = new HashMap<>();
        for (Faculty faculty : context.getFacultyController().getAllFaculties()) {
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
            if (parts.length != 6) {
                throw new IllegalArgumentException("Dòng " + (i + 1) + " phải có 6 cột: Họ tên|Email|Ngày sinh|Mã khoa|Mã ngành|Năm nhập học");
            }

            String fullName = parts[0].trim();
            String email = parts[1].trim();
            LocalDate birthDate = LocalDate.parse(parts[2].trim());
            String facultyCode = parts[3].trim().toUpperCase(Locale.ROOT);
            String majorCode = parts[4].trim();
            int enrollmentYear = Integer.parseInt(parts[5].trim());

            Faculty faculty = facultyByCode.get(facultyCode);
            if (faculty == null) {
                throw new IllegalArgumentException("Không tìm thấy khoa " + facultyCode + " ở dòng " + (i + 1));
            }

            Major major = findMajorByFacultyAndCode(faculty.getId(), majorCode);
            if (major == null) {
                throw new IllegalArgumentException("Không tìm thấy ngành " + majorCode + " trong khoa " + facultyCode + " ở dòng " + (i + 1));
            }

            Class classEntity = resolveClassByMajorAndYear(faculty, major, String.valueOf(enrollmentYear));
            rows.add(new BulkStudentRow(fullName, email, birthDate, faculty, major, classEntity, enrollmentYear));
        }

        return rows;
    }

    private static Major findMajorByFacultyAndCode(Long facultyId, String majorCode) {
        return context.getMajorController().getMajorsByFaculty(facultyId).stream()
                .filter(major -> major.getMajorCode().equalsIgnoreCase(majorCode))
                .findFirst()
                .orElse(null);
    }

    private static Class resolveClassByMajorAndYear(Faculty faculty, Major major, String enrollmentYearText) {
        if (faculty == null || major == null || enrollmentYearText == null || enrollmentYearText.isBlank()) {
            throw new IllegalArgumentException("Cần chọn năm nhập học, khoa và ngành.");
        }
        String yearPrefix = enrollmentYearText.trim();
        if (yearPrefix.length() >= 2) {
            yearPrefix = yearPrefix.substring(yearPrefix.length() - 2);
        }
        String majorCode = major.getMajorCode() == null ? "" : major.getMajorCode().trim();
        String yearPart = yearPrefix;
        List<Class> classes = context.getClassController().getClassesByMajor(major.getId());
        for (Class classEntity : classes) {
            String name = classEntity.getClassName() == null ? "" : classEntity.getClassName();
            if (!majorCode.isEmpty() && !name.contains(majorCode)) {
                continue;
            }
            if (!yearPart.isEmpty() && !name.startsWith(yearPart)) {
                continue;
            }
            return classEntity;
        }
        throw new IllegalArgumentException("Không tìm thấy lớp phù hợp theo ngành và năm nhập học.");
    }

    private static String generateNextStudentCode(StudentDialog form) {
        try {
            String yearText = form.getEnrollmentYearValue();
            Major selectedMajor = form.getSelectedMajor();
            if (yearText.isEmpty() || selectedMajor == null) {
                return "";
            }
            String prefix = buildStudentPrefix(Integer.parseInt(yearText), selectedMajor.getMajorCode());
            long existing = context.getStudentController().countStudentsByCodePrefix(prefix);
            return prefix + String.format("%03d", existing + 1);
        } catch (Exception ignored) {
            return "";
        }
    }

    private static String buildStudentPrefix(int enrollmentYear, String majorCode) {
        String yearPart = String.format("%02d", enrollmentYear % 100);
        return yearPart + majorCode;
    }

    private static void createLecturer(LecturerDialog form) {
        String code = form.getCodeValue();
        String fullName = form.getFullNameValue();
        String email = form.getEmailValue();
        if (code.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Thiếu thông tin bắt buộc.");
        }
        Lecturer lecturer = new Lecturer(null, code, fullName);
        Account account = new Account("123456", Role.LECTURER);
        Account savedAccount = context.getAccountController().createLecturerAccount(account, lecturer);
        saveUserProfile(buildLecturerProfile(form, savedAccount, lecturer));
    }

    private static UserProfile buildStudentProfile(StudentDialog form, Account account, Student student, Faculty faculty, Major major) {
        UserProfile profile = buildCommonProfile(form, account, student.getCode(), student.getFullName(), "Sinh viên");
        profile.setEmail(student.getEmail());
        profile.setBirthDate(student.getBirthOfDate());
        profile.setFacultyName(faculty != null ? faculty.getFullName() : "");
        profile.setMajorName(major != null ? major.getFullName() : "");
        profile.setClassName(student.getClassEntity() != null ? student.getClassEntity().getClassName() : "");
        profile.setAcademicYear(String.valueOf(student.getEnrollmentYear()));
        profile.setExpectedGraduationYear(form.getExpectedGraduationYearValue());
        return profile;
    }

    private static UserProfile buildCommonProfile(StudentDialog form, Account account, String profileCode, String displayName, String roleTitle) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setProfileCode(profileCode);
        profile.setDisplayName(displayName);
        profile.setRoleTitle(roleTitle);
        profile.setEmail(form.getEmailValue());
        profile.setPhoneNumber(form.getPhoneValue());
        profile.setBirthDate(parseOptionalDate(form.getDobValue()));
        profile.setGender(valueOf(form.getGenderValue()));
        profile.setPlaceOfBirth(form.getPlaceOfBirthValue());
        profile.setNationality(form.getNationalityValue());
        profile.setCitizenIdNumber(form.getCitizenIdValue());
        profile.setCitizenIdIssuePlace(form.getCitizenIssuePlaceValue());
        profile.setCitizenIdIssueDate(parseOptionalDate(form.getCitizenIssueDateValue()));
        profile.setCurrentAddress(form.getCurrentAddressValue());
        profile.setPermanentAddress(form.getPermanentAddressValue());
        profile.setContactName(form.getContactNameValue());
        profile.setContactPhone(form.getContactPhoneValue());
        return profile;
    }

    private static UserProfile buildLecturerProfile(LecturerDialog form, Account account, Lecturer lecturer) {
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setProfileCode(lecturer.getCode());
        profile.setDisplayName(lecturer.getFullName());
        profile.setRoleTitle("Giảng viên");
        profile.setEmail(form.getEmailValue());
        profile.setPhoneNumber(form.getPhoneValue());
        profile.setBirthDate(parseOptionalDate(form.getDobValue()));
        profile.setGender(valueOf(form.getGenderValue()));
        profile.setPlaceOfBirth(form.getPlaceOfBirthValue());
        profile.setNationality(form.getNationalityValue());
        profile.setCitizenIdNumber(form.getCitizenIdValue());
        profile.setCitizenIdIssuePlace(form.getCitizenIssuePlaceValue());
        profile.setCitizenIdIssueDate(parseOptionalDate(form.getCitizenIssueDateValue()));
        profile.setCurrentAddress(form.getCurrentAddressValue());
        profile.setPermanentAddress(form.getPermanentAddressValue());
        profile.setContactName(form.getContactNameValue());
        profile.setContactPhone(form.getContactPhoneValue());
        profile.setContactAddress(form.getContactAddressValue());
        return profile;
    }

    private static void saveUserProfile(UserProfile userProfile) {
        context.getUserProfileController().save(userProfile);
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value.trim());
    }

    private static String valueOf(String value) {
        return value == null ? "" : value;
    }

    private static boolean confirmDelete(Component parent) {
        return JOptionPane.showConfirmDialog(parent, "Xóa dữ liệu đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    private static void safeAfterSave(Runnable afterSave) {
        if (afterSave != null) {
            afterSave.run();
        }
    }

    private static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message == null ? "Có lỗi xảy ra." : message, "Lỗi", JOptionPane.ERROR_MESSAGE);
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
}
