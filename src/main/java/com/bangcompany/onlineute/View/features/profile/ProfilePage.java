/**
 * xem thông tin cá nhân
 */
package com.bangcompany.onlineute.View.features.profile;

import com.bangcompany.onlineute.View.shared.ViewContext;
import com.bangcompany.onlineute.View.Components.ui.Card;
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
    private final ViewContext viewContext;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JPanel bodyPanel = new JPanel(new BorderLayout());

    // khởi tạo khung+tiêu đề trang
    public ProfilePage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        bodyPanel.setOpaque(false);

        add(Card.titleCard("THÔNG TIN CÁ NHÂN"), BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
    }

    // load lại data mới nhất khi vào trang
    @Override
    public void onEnter() {
        UserProfile profile = viewContext.getUserProfileController().getCurrentUserProfile();
        bodyPanel.removeAll();
        bodyPanel.add(createProfileContent(profile), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // gom các mục vào bảng dọc có scroll
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

    // tạo header tóm tắt tên/mã/mail/sdt
    private JPanel createSummaryCard(UserProfile profile) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(RoundedBorders.paddedOutline(AppTheme.RADIUS_CARD, new Insets(24, 24, 24, 24)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        card.add(createSummaryInfo(profile), BorderLayout.CENTER);
        return card;
    }

    // nạp label thông tin vào header
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

    // hiện tên/nS/quê quán/cccd...
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
        return card;
    }

    // hiện khoa/ngành/lớp...
    private ProfileSectionCard createAcademicSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Thông tin học tập");
        card.addField("Khoa", valueOf(profile.getFacultyName()));
        card.addField("Ngành", valueOf(profile.getMajorName()));
        card.addField("Lớp", valueOf(profile.getClassName()));
        card.addField("Năm nhập học", valueOf(profile.getAcademicYear()));
        card.addField("Năm tốt nghiệp dự kiến", valueOf(profile.getExpectedGraduationYear()));
        return card;
    }

    // hiện địa chỉ+sdt liên hệ
    private ProfileSectionCard createContactSection(UserProfile profile) {
        ProfileSectionCard card = new ProfileSectionCard("Liên hệ");
        card.addField("Địa chỉ thường trú", valueOf(profile.getPermanentAddress()));
        card.addField("Địa chỉ tạm trú", valueOf(profile.getCurrentAddress()));
        card.addField("Số điện thoại", valueOf(profile.getPhoneNumber()));
        card.addField("Email", valueOf(profile.getEmail()));
        card.addField("Liên hệ khẩn cấp", valueOf(profile.getContactName()));
        card.addField("SDT khẩn cấp", valueOf(profile.getContactPhone()));
        return card;
    }

    // check null để ko hiện chữ null
    private String valueOf(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    // format ngày tháng vn
    private String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMATTER.format(value);
    }
}
