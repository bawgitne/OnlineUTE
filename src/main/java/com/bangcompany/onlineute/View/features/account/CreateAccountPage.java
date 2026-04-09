package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Faculty;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Major;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.FormRow;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.theme.SwingUtils;
import com.bangcompany.onlineute.View.Components.ui.TextAreaInput;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.Normalizer;
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
    private final ViewContext viewContext;
    private TextInput codeInput;
    private TextInput nameInput;
    private TextInput emailInput;
    private TextInput phoneInput;
    private TextInput dobInput;
    private SelectInput<String> genderSelect;
    private TextInput placeOfBirthInput;
    private TextInput nationalityInput;
    private SelectInput<Faculty> facultySelect;
    private SelectInput<Major> majorSelect;
    private TextInput enrollmentYearInput;
    private TextInput expectedGraduationYearInput;
    private TextInput studentCodePreviewInput;
    private TextInput citizenIdInput;
    private TextInput citizenIssuePlaceInput;
    private TextInput citizenIssueDateInput;
    private TextAreaInput currentAddressInput;
    private TextAreaInput permanentAddressInput;
    private TextInput contactNameInput;
    private TextInput contactPhoneInput;
    private TextAreaInput bulkDataInput;

    private final String initialRole;
    private final boolean studentMode;

    public CreateAccountPage(ViewContext viewContext) {
        this(viewContext, "Sinh viên");
    }

    public CreateAccountPage(ViewContext viewContext, String initialRole) {
        this.viewContext = viewContext;
        this.initialRole = initialRole == null || initialRole.isBlank() ? "Sinh viên" : initialRole;
        this.studentMode = isStudentRole(this.initialRole);

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

        codeInput = new TextInput("Mã định danh *", false);
        codeInput.setEditable(true);

        nameInput = new TextInput("Họ và tên *", false);
        emailInput = new TextInput("Email *", false);
        phoneInput = new TextInput("Số điện thoại", false);
        dobInput = new TextInput("Ngày sinh * (YYYY-MM-DD)", false);
        genderSelect = new SelectInput<>("Giới tính", List.of("Nam", "Nữ", "Khác"));
        placeOfBirthInput = new TextInput("Nơi sinh", false);
        nationalityInput = new TextInput("Quốc tịch", false);
        facultySelect = new SelectInput<>("Khoa *", viewContext.getFacultyController().getAllFaculties());
        majorSelect = new SelectInput<>("Ngành *", List.of());
        enrollmentYearInput = new TextInput("Năm nhập học *", false);
        expectedGraduationYearInput = new TextInput("Năm tốt nghiệp dự kiến", false);
        studentCodePreviewInput = new TextInput("MSSV tự động", false);
        studentCodePreviewInput.setEditable(false);

        citizenIdInput = new TextInput("CCCD/CMND", false);
        citizenIssuePlaceInput = new TextInput("Nơi cấp", false);
        citizenIssueDateInput = new TextInput("Ngày cấp (YYYY-MM-DD)", false);
        currentAddressInput = new TextAreaInput("Địa chỉ hiện tại", 96);
        permanentAddressInput = new TextAreaInput("Địa chỉ thường trú", 96);
        contactNameInput = new TextInput("Người liên hệ", false);
        contactPhoneInput = new TextInput("SĐT liên hệ", false);

        facultySelect.getComboBox().addActionListener(e -> {
            refreshAcademicOptions();
            refreshGeneratedStudentCode();
        });
        majorSelect.getComboBox().addActionListener(e -> {
            refreshAcademicOptions();
            refreshGeneratedStudentCode();
        });
        enrollmentYearInput.getTextField().getDocument()
                .addDocumentListener(new SimpleDocumentListener(this::refreshGeneratedStudentCode));

        content.add(Box.createVerticalStrut(18));
        content.add(createSectionPanel(
                studentMode ? FormRow.single(nameInput) : FormRow.two(codeInput, nameInput),
                FormRow.two(emailInput, phoneInput),
                FormRow.three(dobInput, genderSelect, nationalityInput)
        ));
        content.add(Box.createVerticalStrut(16));

        if (studentMode) {
            content.add(createSectionPanel(
                    FormRow.three(enrollmentYearInput, facultySelect, majorSelect),
                    FormRow.two(expectedGraduationYearInput, studentCodePreviewInput)
            ));
            content.add(Box.createVerticalStrut(16));
        }

        content.add(createSectionPanel(
                FormRow.three(citizenIdInput, citizenIssuePlaceInput, citizenIssueDateInput),
                FormRow.two(contactNameInput, contactPhoneInput)
        ));
        content.add(Box.createVerticalStrut(16));

        content.add(createSectionPanel(
                FormRow.single(permanentAddressInput),
                FormRow.single(currentAddressInput)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createActionBar());

        if (studentMode) {
            refreshAcademicOptions();
            enrollmentYearInput.setValue(String.valueOf(Year.now().getValue()));
            expectedGraduationYearInput.setValue(String.valueOf(Year.now().getValue() + 4));
            nationalityInput.setValue("Việt Nam");
        }
        refreshGeneratedStudentCode();
        return SwingUtils.hiddenScrollPane(content);
    }

    private JPanel createActionBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(com.bangcompany.onlineute.View.Components.theme.RoundedBorders.paddedOutline(
                com.bangcompany.onlineute.View.Components.theme.AppTheme.RADIUS_PANEL,
                new Insets(14, 18, 14, 18)
        ));
        bar.setOpaque(true);

        if (studentMode) {
            Button rawDataButton = new Button("Nhập dữ liệu thô", new Color(245, 247, 250), new Color(60, 80, 110));
            rawDataButton.addActionListener(e -> openBulkInputDialog());
            bar.add(rawDataButton);
        }

        Button createButton = new Button(studentMode ? "Lưu sinh viên" : "Lưu giảng viên");
        createButton.setPreferredSize(new Dimension(170, 42));
        createButton.addActionListener(e -> createSingleAccount());
        bar.add(createButton);
        wrapper.add(bar, BorderLayout.CENTER);
        return wrapper;
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

        JLabel helpLabel = new JLabel("<html>Mỗi dòng 6 cột, cách nhau bởi dấu |<br>Họ tên|Email|Ngày sinh (YYYY-MM-DD)|Mã khoa|Mã ngành|Năm nhập học<br>Hệ thống sẽ tự sắp xếp theo tên trước khi sinh 3 số cuối.</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        helpLabel.setForeground(new Color(110, 110, 110));
        panel.add(helpLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bulkDataInput = new TextAreaInput("Dữ liệu bulk", 350);
        panel.add(bulkDataInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        Button btnCreateBulk = new Button("Lưu hàng loạt");
        btnCreateBulk.setPreferredSize(new Dimension(180, 45));
        btnCreateBulk.addActionListener(e -> createBulkStudents());
        panel.add(btnCreateBulk, gbc);
        return panel;
    }

    private void openBulkInputDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Nhập dữ liệu thô", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(SwingUtils.hiddenScrollPane(createBulkPanel()));
        dialog.setSize(760, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void createSingleAccount() {
        String fullName = nameInput.getValue().trim();
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (studentMode) {
                createStudentAccount(fullName);
            } else {
                createLecturerAccount(fullName);
            }

            JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            onEnter();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStudentAccount(String fullName) {
        String email = emailInput.getValue().trim();
        String dobText = dobInput.getValue().trim();
        String enrollmentYearText = enrollmentYearInput.getValue().trim();

        if (email.isEmpty() || dobText.isEmpty() || enrollmentYearText.isEmpty()) {
            throw new IllegalArgumentException("Sinh viên cần email, ngày sinh và năm nhập học.");
        }

        Faculty selectedFaculty = facultySelect.getSelectedValue();
        Major selectedMajor = majorSelect.getSelectedValue();
        if (selectedFaculty == null || selectedMajor == null) {
            throw new IllegalArgumentException("Sinh viên cần chọn khoa và ngành.");
        }

        if (selectedMajor.getFaculty() == null || !Objects.equals(selectedMajor.getFaculty().getId(), selectedFaculty.getId())) {
            throw new IllegalArgumentException("Ngành phải thuộc đúng khoa đã chọn.");
        }

        Class selectedClass = resolveClassByMajorAndYear(selectedFaculty, selectedMajor, enrollmentYearText);

        String code = autoPreviewStudentCode();
        if (code.isBlank()) {
            throw new IllegalArgumentException("Không thể tạo MSSV. Vui lòng kiểm tra ngành và năm nhập học.");
        }

        Student student = new Student(code, fullName, LocalDate.parse(dobText), email, "");
        student.setClassEntity(selectedClass);
        student.setEnrollmentYear(Integer.parseInt(enrollmentYearText));

        Account account = new Account("123456", Role.STUDENT);
        Account savedAccount = viewContext.getAccountController().createStudentAccount(account, student);
        saveUserProfile(buildStudentProfile(savedAccount, student, selectedFaculty, selectedMajor));
    }

    private void createLecturerAccount(String fullName) {
        String code = codeInput.getValue().trim();
        if (code.isBlank()) {
            throw new IllegalArgumentException("Giảng viên cần mã giảng viên.");
        }

        Lecturer lecturer = new Lecturer();
        lecturer.setCode(code);
        lecturer.setFullName(fullName);

        Account account = new Account("123456", Role.LECTURER);
        Account savedAccount = viewContext.getAccountController().createLecturerAccount(account, lecturer);
        saveUserProfile(buildLecturerProfile(savedAccount, lecturer));
    }

    private void createBulkStudents() {
        String rawData = bulkDataInput.getValue().trim();
        if (rawData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                    return (int) viewContext.getStudentController().countStudentsByCodePrefix(prefix) + 1;
                }
                return value + 1;
            });

            String studentCode = prefix + String.format("%03d", nextStep);
            Student student = new Student(studentCode, row.fullName(), row.birthDate(), row.email(), "");
            student.setClassEntity(row.classEntity());
            student.setEnrollmentYear(row.enrollmentYear());

            Account account = new Account("123456", Role.STUDENT);
            Account savedAccount = viewContext.getAccountController().createStudentAccount(account, student);
            saveUserProfile(buildStudentProfile(savedAccount, student, row.faculty(), row.major()));
            created++;
        }

        JOptionPane.showMessageDialog(this, "Đã tạo " + created + " sinh viên.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        bulkDataInput.setValue("");
    }

    private UserProfile buildStudentProfile(Account account, Student student, Faculty faculty, Major major) {
        UserProfile profile = buildCommonProfile(account, student.getCode(), student.getFullName(), "Sinh viên");
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
        UserProfile profile = buildCommonProfile(account, lecturer.getCode(), lecturer.getFullName(), "Giảng viên");
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
        viewContext.getUserProfileController().save(userProfile);
    }

    private List<BulkStudentRow> parseBulkRows(String rawData) {
        Map<String, Faculty> facultyByCode = new HashMap<>();
        for (Faculty faculty : viewContext.getFacultyController().getAllFaculties()) {
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

    private Major findMajorByFacultyAndCode(Long facultyId, String majorCode) {
        return viewContext.getMajorController().getMajorsByFaculty(facultyId).stream()
                .filter(major -> major.getMajorCode().equalsIgnoreCase(majorCode))
                .findFirst()
                .orElse(null);
    }

    private Class resolveClassByMajorAndYear(Faculty faculty, Major major, String enrollmentYearText) {
        if (faculty == null || major == null || enrollmentYearText == null || enrollmentYearText.isBlank()) {
            throw new IllegalArgumentException("Cần chọn năm nhập học, khoa và ngành.");
        }
        String yearPrefix = enrollmentYearText.trim();
        if (yearPrefix.length() >= 2) {
            yearPrefix = yearPrefix.substring(yearPrefix.length() - 2);
        }
        String majorCode = major.getMajorCode() == null ? "" : major.getMajorCode().trim();
        String yearPart = yearPrefix;
        List<Class> classes = viewContext.getClassController().getClassesByMajor(major.getId());
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

    private void refreshAcademicOptions() {
        if (!studentMode || facultySelect == null) {
            return;
        }

        Faculty selectedFaculty = facultySelect.getSelectedValue();
        if (majorSelect != null) {
            majorSelect.setItems(selectedFaculty == null ? List.of() : viewContext.getMajorController().getMajorsByFaculty(selectedFaculty.getId()));
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
        long existing = viewContext.getStudentController().countStudentsByCodePrefix(prefix);
        return prefix + String.format("%03d", existing + step);
    }

    private String buildStudentPrefix(int enrollmentYear, String majorCode) {
        String yearPart = String.format("%02d", enrollmentYear % 100);
        return yearPart + majorCode;
    }

    private Card createSectionPanel(Component... rows) {
        Card section = new Card();
        section.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        for (int i = 0; i < rows.length; i++) {
            Component row = rows[i];
            if (row != null) {
                content.add(row);
                if (i < rows.length - 1) {
                    content.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }
        }
        section.add(content, BorderLayout.CENTER);
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
            if (nationalityInput != null) nationalityInput.setValue("Việt Nam");
            if (facultySelect != null) facultySelect.setItems(viewContext.getFacultyController().getAllFaculties());
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

    private boolean isStudentRole(String role) {
        String normalized = normalizeRole(role);
        return "sinh vien".equalsIgnoreCase(normalized) || "student".equalsIgnoreCase(normalized);
    }

    private String normalizeRole(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}+", "").trim().toLowerCase();
    }
}
