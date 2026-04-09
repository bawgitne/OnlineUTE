/**
 * Gửi thông báo
 */
package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.View.Components.ui.TextInput;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.SelectInput;
import com.bangcompany.onlineute.View.Components.theme.SwingUtils;
import com.bangcompany.onlineute.View.Components.ui.TextAreaInput;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CreateAnnouncementPage extends JPanel implements Refreshable {
    private final JPanel mainPanel;
    private final ViewContext viewContext;
    private TextInput titleInput;
    private TextAreaInput contentInput;
    private SelectInput<String> adminTargetSelect;
    private SelectInput<CourseSectionItem> lecturerClassSelect;

    // load class chuyển thành string luôn
    private static class CourseSectionItem {
        final CourseSection section;

        CourseSectionItem(CourseSection section) {
            this.section = section;
        }

        @Override
        public String toString() {
            String courseName = section.getCourse() != null ? section.getCourse().getFullName() : "Không xác định";
            return courseName + " - Lớp: " + section.getId();
        }
    }

    // hàm tạo giao diện chính
    public CreateAnnouncementPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 24, 24, 24));

        JPanel titleArea = Card.titleCard("SOẠN THÔNG BÁO MỚI");

        Card formCard = new Card(22, new Insets(0, 0, 0, 0));
        formCard.setLayout(new BorderLayout());
        formCard.add(createBody(), BorderLayout.CENTER);

        add(titleArea, BorderLayout.NORTH);
        add(formCard, BorderLayout.CENTER);

        buildForm();
    }

    private JComponent createBody() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(SwingUtils.hiddenScrollPane(mainPanel), BorderLayout.CENTER);
        return wrapper;
    }

    // build form tùy theo role là admin hay giảng viên
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
            // admin gửi cho nhiều cái
            adminTargetSelect = new SelectInput<>("Gửi đến", List.of("Toàn trường", "Toàn bộ Sinh viên", "Toàn bộ Giảng viên"));
            mainPanel.add(adminTargetSelect, gbc);
        } else if ("LECTURER".equals(role)) {
            // giảng viên chỉ gửi cho lớp mình dạy
            List<CourseSectionItem> myClasses = List.of();
            var lecturer = SessionManager.getCurrentLecturer();
            if (lecturer != null && viewContext.getNotificationController() != null) {
                myClasses = viewContext.getNotificationController()
                        .getCourseSectionsByLecturerId(lecturer.getId())
                        .stream()
                        .map(CourseSectionItem::new)
                        .toList();
            }
            lecturerClassSelect = new SelectInput<>("Chọn lớp học phần", myClasses);
            mainPanel.add(lecturerClassSelect, gbc);
        }

        gbc.gridy++;
        titleInput = new TextInput("Tiêu đề", false);
        mainPanel.add(titleInput, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentInput = new TextAreaInput("Nội dung thông báo", 250);
        mainPanel.add(contentInput, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        Button btnSend = new Button("Gửi thông báo");
        btnSend.setPreferredSize(new Dimension(200, 45));
        btnSend.addActionListener(e -> submitAnnouncement(role));
        mainPanel.add(btnSend, gbc);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // xử lý logic gửi thông báo
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
            viewContext.getNotificationController().createAnnouncement(title, content, targetType, targetClassId, senderName);
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
