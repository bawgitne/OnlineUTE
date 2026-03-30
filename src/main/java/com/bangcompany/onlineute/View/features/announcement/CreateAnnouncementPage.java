package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.View.Components.InputGroup;
import com.bangcompany.onlineute.View.Components.PageTitleLabel;
import com.bangcompany.onlineute.View.Components.PrimaryButton;
import com.bangcompany.onlineute.View.Components.SelectGroup;
import com.bangcompany.onlineute.View.Components.TextAreaGroup;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CreateAnnouncementPage extends JPanel implements Refreshable {
    private final JPanel mainPanel;
    private InputGroup titleInput;
    private TextAreaGroup contentInput;
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
            return courseName + " - Lớp: " + section.getId();
        }
    }

    public CreateAnnouncementPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 50, 0, 50));
        topPanel.setOpaque(false);
        topPanel.add(new PageTitleLabel("SOẠN THÔNG BÁO MỚI"), BorderLayout.NORTH);
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
        if (role == null) {
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        if ("ADMIN".equals(role)) {
            adminTargetSelect = new SelectGroup<>("Gửi đến", List.of("Toàn trường", "Toàn bộ Sinh viên", "Toàn bộ Giảng viên"));
            mainPanel.add(adminTargetSelect, gbc);
        } else if ("LECTURER".equals(role)) {
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
        contentInput = new TextAreaGroup("Nội dung thông báo", 250);
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String targetType;
        Long targetClassId = null;
        String senderName;

        if ("ADMIN".equals(role)) {
            String selection = adminTargetSelect.getSelectedValue();
            if (selection != null && selection.contains("Sinh viên")) {
                targetType = "ALL_STUDENTS";
            } else if (selection != null && selection.contains("Giảng viên")) {
                targetType = "ALL_LECTURERS";
            } else {
                targetType = "ALL";
            }
            senderName = "Phòng Đào tạo";
        } else {
            CourseSectionItem selectedClass = lecturerClassSelect.getSelectedValue();
            if (selectedClass == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn lớp để gửi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            targetType = "COURSE_SECTION";
            targetClassId = selectedClass.section.getId();
            var lecturer = SessionManager.getCurrentLecturer();
            senderName = lecturer != null ? "GV. " + lecturer.getFullName() : "Giảng viên";
        }

        try {
            AppContext.getNotificationController().createAnnouncement(title, content, targetType, targetClassId, senderName);
            JOptionPane.showMessageDialog(this, "Gửi thông báo thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            titleInput.setValue("");
            contentInput.setValue("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onEnter() {
        buildForm();
    }
}
