/**
 * Xem điểm
 */
package com.bangcompany.onlineute.View.features.grade;

import com.bangcompany.onlineute.View.Components.ui.Card;
import com.bangcompany.onlineute.Config.SessionManager;
import com.bangcompany.onlineute.Model.Entity.Course;
import com.bangcompany.onlineute.Model.Entity.CourseRegistration;
import com.bangcompany.onlineute.Model.Entity.Mark;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.Components.ui.Table;
import com.bangcompany.onlineute.View.shared.Refreshable;
import com.bangcompany.onlineute.View.shared.ViewContext;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewGradesPage extends JPanel implements Refreshable {
    private final Table table;
    private final ViewContext viewContext;

    public ViewGradesPage(ViewContext viewContext) {
        this.viewContext = viewContext;
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 245, 245));
        setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        String[] cols = {"STT", "Mã môn học", "Tên môn học", "Số TC", "Điểm hệ 10", "Điểm hệ 4", "Điểm chữ", "Kết quả"};
        table = new Table(cols, 12, 44);

        add(Card.titleCard("KẾT QUẢ HỌC TẬP"), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
    }

    // load data cho nó
    @Override
    public void onEnter() {
        Student student = SessionManager.getCurrentStudent();
        if (student == null) {
            return;
        }

        table.clearRows();
        List<CourseRegistration> registrations = viewContext.getCourseRegistrationController().getStudentRegistrations(student.getId());
        List<Course> allCourses = viewContext.getCourseController().getAllCourses();
        if (allCourses == null) {
            allCourses = new ArrayList<>();
        }

        Set<Long> studiedCourseIds = new HashSet<>();

        addHeaderRow("--- CÁC MÔN ĐÃ VÀ ĐANG HỌC ---");
        int stt = 1;
        for (CourseRegistration registration : registrations) {
            Course course = registration.getCourseSection() != null ? registration.getCourseSection().getCourse() : null;
            if (course != null) {
                studiedCourseIds.add(course.getId());
            }

            Mark mark = registration.getMark();
            String diem10 = "";
            String diem4 = "";
            String diemChu = "";
            String ketQua = "";

            // tính toán quy đổi điểm hệ 4 và điểm chữ
            if (mark != null && mark.getFinalScore() != null) {
                BigDecimal finalScore = mark.getFinalScore();
                diem10 = finalScore.toString();
                if (finalScore.compareTo(new BigDecimal("8.5")) >= 0) {
                    diem4 = "4.0";
                } else if (finalScore.compareTo(new BigDecimal("7.0")) >= 0) {
                    diem4 = "3.0";
                } else if (finalScore.compareTo(new BigDecimal("5.5")) >= 0) {
                    diem4 = "2.0";
                } else if (finalScore.compareTo(new BigDecimal("4.0")) >= 0) {
                    diem4 = "1.0";
                } else {
                    diem4 = "0.0";
                }

                diemChu = mark.getGradeChar() != null ? mark.getGradeChar() : "";
                ketQua = !"0.0".equals(diem4) ? "Đạt" : "Rớt";
            }

            table.addRow(
                    stt++,
                    course != null ? "M" + course.getId() : "",
                    course != null ? course.getFullName() : "",
                    course != null ? course.getCredit() : "",
                    diem10, diem4, diemChu, ketQua
            );
        }

        // liệt kê thêm các môn có trong chương trình nhưng chưa học
        addHeaderRow("--- CÁC MÔN CHƯA HỌC (THAM KHẢO) ---");
        stt = 1;
        for (Course course : allCourses) {
            if (!studiedCourseIds.contains(course.getId())) {
                table.addRow(
                        stt++,
                        "M" + course.getId(),
                        course.getFullName(),
                        course.getCredit(),
                        "", "", "", ""
                );
            }
        }
    }

    private void addHeaderRow(String text) {
        table.addRow("", "", text, "", "", "", "", "");
    }
}
