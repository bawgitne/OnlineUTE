package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.Account;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.Model.Entity.StudyProgram;
import com.bangcompany.onlineute.Model.Entity.Term;
import com.bangcompany.onlineute.Model.EnumType.Role;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TextAreaGroup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class CreateAccountPage extends JPanel implements Refreshable {
    private JTabbedPane tabbedPane;

    // Manual Tab
    private SelectGroup<String> roleSelect;
    private InputGroup codeInput;
    private InputGroup nameInput;
    private InputGroup emailInput;
    private SelectGroup<Class> classSelect;
    private SelectGroup<StudyProgram> studyProgramSelect;
    private SelectGroup<Term> termSelect;
    private InputGroup dobInput;
    private PrimaryButton btnCreateSingle;

    // Bulk Tab
    private SelectGroup<String> bulkRoleSelect;
    private TextAreaGroup bulkDataInput;
    private PrimaryButton btnCreateBulk;

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

        tabbedPane.addTab("Tạo 1 cái", createManualTab());
        tabbedPane.addTab("Tạo 1 đống", createBulkTab());

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

        roleSelect = new SelectGroup<>("Vai trò Account", java.util.List.of("Sinh viên (Student)", "Giảng viên (Lecturer)"));
        panel.add(roleSelect, gbc);

        gbc.gridy++;
        codeInput = new InputGroup("Mã (MSSV hoặc Mã GV)", false);
        panel.add(codeInput, gbc);

        gbc.gridy++;
        nameInput = new InputGroup("Họ và Tên", false);
        panel.add(nameInput, gbc);

        gbc.gridy++;
        emailInput = new InputGroup("Email (Chỉ dành cho Sinh viên)", false);
        panel.add(emailInput, gbc);

        gbc.gridy++;
        dobInput = new InputGroup("Ngày sinh (YYYY-MM-DD / Dành cho SV)", false);
        panel.add(dobInput, gbc);

        gbc.gridy++;
        classSelect = new SelectGroup<>("Mã lớp (Dành cho SV)", AppContext.getClassService().getAllClasses());
        panel.add(classSelect, gbc);

        gbc.gridy++;
        studyProgramSelect = new SelectGroup<>("Chương trình đào tạo (Dành cho SV)", AppContext.getStudyProgramService().getAllStudyPrograms());
        panel.add(studyProgramSelect, gbc);

        gbc.gridy++;
        termSelect = new SelectGroup<>("Học kỳ (Dành cho SV)", AppContext.getTermService().getAllTerms());
        panel.add(termSelect, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        btnCreateSingle = new PrimaryButton("Khởi tạo tài khoản");
        btnCreateSingle.setPreferredSize(new Dimension(150, 45));
        panel.add(btnCreateSingle, gbc);
        btnCreateSingle.addActionListener(e -> {
            try {
                Student student = new Student(codeInput.getValue(), nameInput.getValue(), LocalDate.parse(dobInput.getValue()), emailInput.getValue(), "");
                student.setClassEntity(classSelect.getSelectedValue());
                student.setStudyProgram(studyProgramSelect.getSelectedValue());
                student.setTerm(termSelect.getSelectedValue());
                
                AppContext.getAuthService().registerStudent(
                    new Account(codeInput.getValue(), "123456", Role.STUDENT),
                    student
                );
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                onEnter();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Setup role switch listener if needed, but for dummy UI we can just leave it as is.
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private JScrollPane createBulkTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        bulkRoleSelect = new SelectGroup<>("Định dạng dữ liệu chèn hàng loạt", java.util.List.of(
                "Sinh viên (Mã SV \\t Họ Tên \\t Ngày Sinh \\t Email \\t Mã Lớp)",
                "Giảng viên (Mã GV \\t Họ Tên)"
        ));
        panel.add(bulkRoleSelect, gbc);

        gbc.gridy++;
        JLabel helpLabel = new JLabel("<html>Bôi đen các dòng trên Excel/Google Sheets và dán thẳng vào ô bên dưới.<br>" +
                "Mỗi dòng tương ứng 1 Account, cách nhau bằng phím Tab (Tự động nhận diện khi dán). Mật khẩu mặc định: 123456</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        helpLabel.setForeground(Color.GRAY);
        panel.add(helpLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bulkDataInput = new TextAreaGroup("Dữ liệu Bulk Copy/Paste", 350);
        panel.add(bulkDataInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        btnCreateBulk = new PrimaryButton("Tiến hành Batch Insert");
        btnCreateBulk.setPreferredSize(new Dimension(200, 45));
        panel.add(btnCreateBulk, gbc);

        btnCreateBulk.addActionListener(e -> {
            String data = bulkDataInput.getValue().trim();
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty data!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int rows = data.split("\\n").length;
            JOptionPane.showMessageDialog(this, "Đã quét được " + rows + " khối dữ liệu " + bulkRoleSelect.getSelectedValue() + ".\nBatch-Insert Backend Controller cần được tích hợp!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            bulkDataInput.setValue("");
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    @Override
    public void onEnter() {
        codeInput.setValue("");
        nameInput.setValue("");
        emailInput.setValue("");
        dobInput.setValue("");
        bulkDataInput.setValue("");
        
        classSelect.setItems(AppContext.getClassService().getAllClasses());
        studyProgramSelect.setItems(AppContext.getStudyProgramService().getAllStudyPrograms());
        termSelect.setItems(AppContext.getTermService().getAllTerms());
    }
}
