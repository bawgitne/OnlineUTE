package com.bangcompany.onlineute.Model.EnumType;

/**
 * MenuItem - Defined the sidebar menu items and their role-based accessibility.
 */
public enum MenuItem {
    // ADMIN
    MANAGE_ACCOUNTS("Quản lý tài khoản", "ADMIN", "account.png"),
    MANAGE_STUDENTS("Quản lý sinh viên", "ADMIN", "student.png"),
    MANAGE_LECTURERS("Quản lý giảng viên", "ADMIN", "lecturer.png"),
    
    // LECTURER  
    MY_CLASSES("Lớp học của tôi", "LECTURER", "class.png"),
    INPUT_GRADES("Nhập điểm", "LECTURER", "grade.png"),
    
    // STUDENT
    MY_GRADES("Xem điểm", "STUDENT", "grade.png"),
    MY_SCHEDULE("Thời khóa biểu", "STUDENT", "lich.png"),
    
    // ALL roles
    PROFILE("Hồ sơ cá nhân", "ALL", "thongTinCaNhan.png"),
    CHANGE_PASSWORD("Đổi mật khẩu", "ALL", "password.png"),
    ANNOUNCEMENT("Thông báo", "ALL", "thongTinCaNhan.png");

    private final String label;
    private final String requiredRole;
    private final String icon;

    MenuItem(String label, String requiredRole, String icon) {
        this.label = label;
        this.requiredRole = requiredRole;
        this.icon = icon;
    }

    public String getLabel() { return label; }
    public String getIcon() { return icon; }

    public boolean isAccessibleBy(String role) {
        return this.requiredRole.equals("ALL") || 
               this.requiredRole.equals(role);
    }
}
