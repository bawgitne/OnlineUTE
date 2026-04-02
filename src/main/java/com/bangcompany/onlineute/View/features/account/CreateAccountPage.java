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
