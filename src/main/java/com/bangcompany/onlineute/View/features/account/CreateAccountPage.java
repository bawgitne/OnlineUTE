package com.bangcompany.onlineute.View.features.account;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.StudyProgram;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TextAreaGroup;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class CreateAccountPage extends JPanel implements Refreshable {
    private final JTabbedPane tabbedPane;
    private SelectGroup<String> roleSelect;
    private InputGroup codeInput;
    private InputGroup nameInput;
    private InputGroup emailInput;
    private SelectGroup<Class> classSelect;
    private SelectGroup<StudyProgram> studyProgramSelect;
    private SelectGroup<Term> termSelect;
    private InputGroup dobInput;
    private TextAreaGroup bulkDataInput;

    public CreateAccountPage() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("TẠO MỚI TÀI KHOẢN"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setFocusable(false);
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.addTab("Tạo từng tài khoản", createManualTab());
        tabbedPane.addTab("Nhập danh sách", createBulkTab());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane createManualTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);

        roleSelect = new SelectGroup<>("Vai trò", java.util.List.of("Sinh viên", "Giảng viên"));
        panel.add(roleSelect, gbc);

        gbc.gridy++;
        codeInput = new InputGroup("Mã", false);
        panel.add(codeInput, gbc);

        gbc.gridy++;
        nameInput = new InputGroup("Họ và tên", false);
        panel.add(nameInput, gbc);

        gbc.gridy++;
        emailInput = new InputGroup("Email (cho sinh viên)", false);
        panel.add(emailInput, gbc);

        gbc.gridy++;
        dobInput = new InputGroup("Ngày sinh YYYY-MM-DD", false);
        panel.add(dobInput, gbc);

        gbc.gridy++;
        classSelect = new SelectGroup<>("Lớp", AppContext.getClassService().getAllClasses());
        panel.add(classSelect, gbc);

        gbc.gridy++;
        studyProgramSelect = new SelectGroup<>("Chương trình đào tạo", AppContext.getStudyProgramService().getAllStudyPrograms());
        panel.add(studyProgramSelect, gbc);

        gbc.gridy++;
        termSelect = new SelectGroup<>("Học kỳ", AppContext.getTermService().getAllTerms());
        panel.add(termSelect, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        PrimaryButton btnCreate = new PrimaryButton("Khởi tạo tài khoản");
        btnCreate.setPreferredSize(new Dimension(170, 45));
        btnCreate.addActionListener(e -> createSingleAccount());
        panel.add(btnCreate, gbc);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private JScrollPane createBulkTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel helpLabel = new JLabel("<html>Dán dữ liệu từ Excel vào đây.<br>Backend tạo hàng loạt chưa được nối.</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        helpLabel.setForeground(Color.GRAY);
        panel.add(helpLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bulkDataInput = new TextAreaGroup("Dữ liệu bulk", 350);
        panel.add(bulkDataInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        PrimaryButton btnCreateBulk = new PrimaryButton("Lưu hàng loạt");
        btnCreateBulk.setPreferredSize(new Dimension(180, 45));
        btnCreateBulk.addActionListener(e -> {
            String data = bulkDataInput.getValue().trim();
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa nhập dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int rows = data.split("\\n").length;
            JOptionPane.showMessageDialog(this, "Đã nhận " + rows + " dòng dữ liệu. Phần bulk sẽ nối backend sau.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            bulkDataInput.setValue("");
        });
        panel.add(btnCreateBulk, gbc);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private void createSingleAccount() {
        String roleLabel = roleSelect.getSelectedValue();
        String code = codeInput.getValue().trim();
        String fullName = nameInput.getValue().trim();

        if (code.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và họ tên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if ("Sinh viên".equals(roleLabel)) {
                createStudentAccount(code, fullName);
            } else {
                createLecturerAccount(code, fullName);
            }

            JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            onEnter();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStudentAccount(String code, String fullName) {
        String email = emailInput.getValue().trim();
        String dobText = dobInput.getValue().trim();

        if (email.isEmpty() || dobText.isEmpty()) {
            throw new IllegalArgumentException("Sinh viên cần email và ngày sinh.");
        }

        Student student = new Student(code, fullName, LocalDate.parse(dobText), email, "");
        student.setClassEntity(classSelect.getSelectedValue());
        student.setStudyProgram(studyProgramSelect.getSelectedValue());
        student.setTerm(termSelect.getSelectedValue());

        if (student.getClassEntity() == null || student.getStudyProgram() == null || student.getTerm() == null) {
            throw new IllegalArgumentException("Sinh viên cần lớp, chương trình và học kỳ.");
        }

        Account account = new Account(code, "123456", Role.STUDENT);
        AppContext.getAccountController().createStudentAccount(account, student);
    }

    private void createLecturerAccount(String code, String fullName) {
        Lecturer lecturer = new Lecturer();
        lecturer.setCode(code);
        lecturer.setFullName(fullName);

        Account account = new Account(code, "123456", Role.LECTURER);
        AppContext.getAccountController().createLecturerAccount(account, lecturer);
    }

    @Override
    public void onEnter() {
        if (codeInput != null) codeInput.setValue("");
        if (nameInput != null) nameInput.setValue("");
        if (emailInput != null) emailInput.setValue("");
        if (dobInput != null) dobInput.setValue("");
        if (bulkDataInput != null) bulkDataInput.setValue("");

        if (classSelect != null) classSelect.setItems(AppContext.getClassService().getAllClasses());
        if (studyProgramSelect != null) studyProgramSelect.setItems(AppContext.getStudyProgramService().getAllStudyPrograms());
        if (termSelect != null) termSelect.setItems(AppContext.getTermService().getAllTerms());
    }
}
