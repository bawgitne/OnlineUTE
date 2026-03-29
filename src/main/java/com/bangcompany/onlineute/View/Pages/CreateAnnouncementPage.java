package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CreateAnnouncementPage extends JPanel implements Refreshable {
    private JPanel mainPanel;
    private InputGroup titleInput;
    private com.bangcompany.onlineute.View.Components.TextAreaGroup contentInput;
    private SelectGroup<String> adminTargetSelect;
    private SelectGroup<CourseSectionItem> lecturerClassSelect;

    private static class CourseSectionItem {
        final CourseSection section;

        CourseSectionItem(CourseSection section) {
            this.section = section;
        }

        @Override
        public String toString() {
            String courseName = section.getCourse() != null ? section.getCourse().getFullName() : "Unknown course";
            return courseName + " - Lop: " + section.getId();
        }
    }

    public CreateAnnouncementPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 50, 0, 50));
        topPanel.setOpaque(false);
        topPanel.add(new com.bangcompany.onlineute.View.Components.PageTitleLabel("SOẠN THÔNG BÁO MỚI"), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 50, 50, 50));

        buildForm();

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void buildForm() {
        mainPanel.removeAll();
        String role = SessionManager.getRole();
        if (role == null) return;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        if (role.equals("ADMIN")) {
            adminTargetSelect = new SelectGroup<>("Gui den", List.of("Toan truong (Tat ca)", "Toan bo Sinh vien", "Toan bo Giang vien"));
            mainPanel.add(adminTargetSelect, gbc);
        } else if (role.equals("LECTURER")) {
            List<CourseSectionItem> myClasses = List.of();
            var lecturer = SessionManager.getCurrentLecturer();
            if (lecturer != null && AppContext.getNotificationController() != null) {
                myClasses = AppContext.getNotificationController()
                        .getCourseSectionsByLecturerId(lecturer.getId())
                        .stream()
                        .map(CourseSectionItem::new)
                        .toList();
            }
            lecturerClassSelect = new SelectGroup<>("Chọn lớp học phần", myClasses);
            mainPanel.add(lecturerClassSelect, gbc);
        }

        gbc.gridy++;
        titleInput = new InputGroup("Tiêu đề", false);
        mainPanel.add(titleInput, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);

        contentInput = new com.bangcompany.onlineute.View.Components.TextAreaGroup("Nội dung thông báo", 250);
        mainPanel.add(contentInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        PrimaryButton btnSend = new PrimaryButton("Gửi thông báo");
        btnSend.setPreferredSize(new Dimension(200, 45));
        btnSend.addActionListener(e -> submitAnnouncement(role));
        mainPanel.add(btnSend, gbc);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void submitAnnouncement(String role) {
        String title = titleInput.getValue().trim();
        String content = contentInput.getValue().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap day du tieu de va noi dung!", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String targetType = "ALL";
        Long targetClassId = null;
        String senderName = "System";

        if (role.equals("ADMIN")) {
            String selection = adminTargetSelect.getSelectedValue();
            if (selection.contains("Sinh vien")) targetType = "ALL_STUDENTS";
            else if (selection.contains("Giang vien")) targetType = "ALL_LECTURERS";
            else targetType = "ALL";
            senderName = "Phong Dao Tao";
        } else if (role.equals("LECTURER")) {
            CourseSectionItem selectedClass = lecturerClassSelect.getSelectedValue();
            if (selectedClass == null) {
                JOptionPane.showMessageDialog(this, "Ban chua chon lop nao de gui!", "Loi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            targetType = "COURSE_SECTION";
            targetClassId = selectedClass.section.getId();
            var lecturer = SessionManager.getCurrentLecturer();
            senderName = lecturer != null ? "GV. " + lecturer.getFullName() : "Giang vien";
        }

        try {
            if (AppContext.getNotificationController() == null) {
                JOptionPane.showMessageDialog(this, "He thong thong bao chua san sang.", "Loi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AppContext.getNotificationController()
                    .createAnnouncement(title, content, targetType, targetClassId, senderName);

            JOptionPane.showMessageDialog(this, "Gui thong bao thanh cong!", "Hoan tat", JOptionPane.INFORMATION_MESSAGE);
            titleInput.setValue("");
            contentInput.setValue("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi khi gui: " + ex.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onEnter() {
        buildForm();
    }
}
