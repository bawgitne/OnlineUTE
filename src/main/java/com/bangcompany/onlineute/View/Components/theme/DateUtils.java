/**
 * DateUtils.java
 * Công dụng: Tiện ích xử lý và định dạng các thông tin liên quan đến ngày tháng.
 */
package com.bangcompany.onlineute.View.Components.theme;

public final class DateUtils {
    private DateUtils() {}

    // Chuyển đổi số thứ tự ngày trong tuần (1-7) sang chuỗi hiển thị tiếng Việt có dấu
    public static String formatDay(Integer dayOfWeek) {
        if (dayOfWeek == null) {
            return "";
        }
        return switch (dayOfWeek) {
            case 1 -> "Thứ 2";
            case 2 -> "Thứ 3";
            case 3 -> "Thứ 4";
            case 4 -> "Thứ 5";
            case 5 -> "Thứ 6";
            case 6 -> "Thứ 7";
            case 7 -> "Chủ nhật";
            default -> "Không rõ";
        };
    }
}
