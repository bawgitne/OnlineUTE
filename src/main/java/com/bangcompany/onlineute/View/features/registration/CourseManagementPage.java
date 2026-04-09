/**
 * Quản lý môn học đã đăng ký
 */
package com.bangcompany.onlineute.View.features.registration;

import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.CourseSection;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.theme.DateUtils;
import com.bangcompany.onlineute.View.Components.ui.Button;
import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;
import com.bangcompany.onlineute.View.shared.ExceptionHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CourseManagementPage extends JPanel implements Refreshable {
    private final ViewContext viewContext;
    private final Table registeredTable = new Table(new String[]{"Mã lớp", "Môn học", "Giảng viên", "Thứ", "Tiết", "Trạng thái"}, 12, 44);

    public CourseManagementPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(Card.titleCard("QUẢN LÝ MÔN HỌC"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        loadRegisteredTable();
    }

    private JComponent createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);

        body.add(registeredTable, BorderLayout.CENTER);
        body.add(buildActions(), BorderLayout.SOUTH);
        return body;
    }

    private JPanel buildActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        Button cancelButton = new Button("Hủy lớp đã đăng ký", new Color(220, 53, 69));
        cancelButton.setPreferredSize(new Dimension(190, 42));
        cancelButton.addActionListener(e -> cancelSelectedRegistered());
        actions.add(cancelButton);
        return actions;
    }

    private void cancelSelectedRegistered() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            ExceptionHandler.showError(this, new IllegalArgumentException("Không tìm thấy thông tin sinh viên đang đăng nhập."), "Lỗi");
            return;
        }

        int selectedIndex = registeredTable.getSelectedIndex();
        if (selectedIndex < 0) {
            ExceptionHandler.showError(this, new com.bangcompany.onlineute.Exception.BusinessException("Vui lòng chọn một môn đã đăng ký."), "Thông báo");
            return;
        }

        List<CourseRegistration> registrations = viewContext.getCourseRegistrationController().getStudentRegistrations(student.getId());
        if (registrations == null || selectedIndex >= registrations.size()) {
            ExceptionHandler.showError(this, new com.bangcompany.onlineute.Exception.BusinessException("Không tìm thấy đăng ký phù hợp."), "Thông báo");
            return;
        }

        CourseRegistration registration = registrations.get(selectedIndex);
        CourseSection section = registration.getCourseSection();
        if (section == null || section.getId() == null) {
            ExceptionHandler.showError(this, new com.bangcompany.onlineute.Exception.BusinessException("Không tìm thấy lớp học phần."), "Thông báo");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hủy đăng ký lớp học phần đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            viewContext.getCourseRegistrationController().cancel(student.getId(), section.getId());
            ExceptionHandler.showInfo(this, "Đã hủy đăng ký thành công.", "Thành công");
            loadRegisteredTable();
        } catch (Exception ex) {
            ExceptionHandler.showError(this, ex, "Không thể hủy đăng ký.");
        }
    }

    private void loadRegisteredTable() {
        registeredTable.clearRows();
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            registeredTable.setEmptyRow("");
            return;
        }
        List<CourseRegistration> registrations = viewContext.getCourseRegistrationController().getStudentRegistrations(student.getId());
        if (registrations == null || registrations.isEmpty()) {
            registeredTable.setEmptyRow("");
            return;
        }
        for (CourseRegistration registration : registrations) {
            CourseSection section = registration.getCourseSection();
            if (section == null) {
                continue;
            }
            registeredTable.addRow(
                    section.getSectionCode(),
                    section.getCourse() == null ? "" : section.getCourse().getFullName(),
                    section.getLecturer() == null ? "" : section.getLecturer().getFullName(),
                    DateUtils.formatDay(section.getDayOfWeek()),
                    formatSlots(section.getStartSlot(), section.getEndSlot()),
                    registration.getStatus() == null ? "" : registration.getStatus().name()
            );
        }
    }

    private String formatSlots(Integer startSlot, Integer endSlot) {
        if (startSlot == null || endSlot == null) {
            return "";
        }
        return startSlot + " - " + endSlot;
    }

    @Override
    public void onEnter() {
        loadRegisteredTable();
    }
}
