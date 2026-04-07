/**
 * Xem thông tin cá nhân
 */
package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.Entity.UserProfile;
import com.bangcompany.onlineute.View.Components.theme.AppTheme;
import com.bangcompany.onlineute.View.Components.theme.RoundedBorders;
import com.bangcompany.onlineute.View.Components.theme.SwingUtils;
import com.bangcompany.onlineute.View.Components.ui.TagChip;
import com.bangcompany.onlineute.View.shared.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfilePage extends JPanel implements Refreshable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JPanel bodyPanel = new JPanel(new BorderLayout());

    public ProfilePage() {
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        bodyPanel.setOpaque(false);

        add(Card.titleCard("THÔNG TIN CÁ NHÂN"), BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
    }

    // lấy data
    @Override
    public void onEnter() {
        UserProfile profile = AppContext.getUserProfileController().getCurrentUserProfile();
        bodyPanel.removeAll();
        bodyPanel.add(createProfileContent(profile), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // chia thành các section nhỏ: cá nhân, học thuật, liên hệ
    private JComponent createProfileContent(UserProfile profile) {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createSummaryCard(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createPersonalSection(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createAcademicSection(profile));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createContactSection(profile));

        return SwingUtils.hiddenScrollPane(content);
    }

    // cái card to đầu tiên chứa avatar và tên
    private JPanel createSummaryCard(UserProfile profile) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_CARD, new Insets(24, 24, 24, 24)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));


        card.add(createSummaryInfo(profile), BorderLayout.CENTER);
        return card;
    }


    // thông tin tóm tắt bên cạnh avatar
    private JComponent createSummaryInfo(UserProfile profile) {
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(valueOf(profile.getDisplayName()));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        nameLabel.setForeground(new Color(24, 70, 121));

        JLabel subLabel = new JLabel(valueOf(profile.getRoleTitle()) + "  |  " + valueOf(profile.getProfileCode()));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(new Color(99, 115, 129));

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        chips.setOpaque(false);
        chips.add(new TagChip("Email: " + valueOf(profile.getEmail())));
        chips.add(new TagChip("Điện thoại: " + valueOf(profile.getPhoneNumber())));
        chips.add(new TagChip("Lớp: " + valueOf(profile.getClassName())));

        info.add(nameLabel);
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(subLabel);
        info.add(Box.createRigidArea(new Dimension(0, 18)));
        info.add(chips);
        return info;
    }

    // các field chi tiết của cá nhân
    private ProfileSectionCard createPersonalSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin cá nhân");
        card.addField("Họ và tên", valueOf(profile.getDisplayName()));
        card.addField("Mã hồ sơ", valueOf(profile.getProfileCode()));
        card.addField("Ngày sinh", formatDate(profile.getBirthDate()));
        card.addField("Giới tính", valueOf(profile.getGender()));
        card.addField("Nơi sinh", valueOf(profile.getPlaceOfBirth()));
        card.addField("Quốc tịch", valueOf(profile.getNationality()));
        card.addField("Dân tộc", valueOf(profile.getEthnicity()));
        card.addField("Tôn giáo", valueOf(profile.getReligion()));
        card.addField("CCCD / CMND", valueOf(profile.getCitizenIdNumber()));
        card.addField("Nơi cấp", valueOf(profile.getCitizenIdIssuePlace()));
        card.addField("Ngày cấp", formatDate(profile.getCitizenIdIssueDate()));
        card.addField("Địa chỉ thường trú", valueOf(profile.getPermanentAddress()));
        card.addField("Địa chỉ liên lạc", valueOf(profile.getCurrentAddress()));
        card.addField("Email", valueOf(profile.getEmail()));
        return card;
    }

    // các field chi tiết về học tập
    private ProfileSectionCard createAcademicSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin học tập");
        card.addField("Vai trò", valueOf(profile.getRoleTitle()));
        card.addField("Khoa / Đơn vị", valueOf(profile.getFacultyName()));
        card.addField("Lớp", valueOf(profile.getClassName()));
        card.addField("Ngành", valueOf(profile.getMajorName()));
        card.addField("Niên khóa", valueOf(profile.getAcademicYear()));
        card.addField("Năm tốt nghiệp dự kiến", valueOf(profile.getExpectedGraduationYear()));
        return card;
    }

    // các field chi tiết về gia đình/liên hệ
    private ProfileSectionCard createContactSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin liên hệ");
        card.addField("Người liên hệ", valueOf(profile.getContactName()));
        card.addField("Điện thoại liên hệ", valueOf(profile.getContactPhone()));
        card.addField("Địa chỉ liên hệ", valueOf(profile.getContactAddress()));
        card.addField("Họ tên cha", valueOf(profile.getFatherName()));
        card.addField("Điện thoại cha", valueOf(profile.getFatherPhone()));
        card.addField("Họ tên mẹ", valueOf(profile.getMotherName()));
        card.addField("Điện thoại mẹ", valueOf(profile.getMotherPhone()));
        return card;
    }

    // kiểm tra null để hiện "Chưa cập nhật"
    private String valueOf(String value) {
        return value == null || value.isBlank() ? "Chưa cập nhật" : value;
    }

    private String formatDate(LocalDate date) {
        return date == null ? "Chưa cập nhật" : date.format(DATE_FORMATTER);
    }

    // lấy kí tự đầu tên để làm avatar
    private String initialsOf(String value) {
        if (value == null || value.isBlank()) {
            return "?";
        }

        String[] parts = value.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        String first = parts[0].substring(0, 1);
        String last = parts[parts.length - 1].substring(0, 1);
        return (first + last).toUpperCase();
    }
}
