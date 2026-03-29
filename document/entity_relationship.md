# 📊 Cấu trúc Quan hệ Thực thể (Entity Relationships)

Hệ thống được thiết kế dựa trên mô hình cơ sở dữ liệu quan hệ, sử dụng Hibernate/JPA để ánh xạ vào Java Entity.

## 1. Mối quan hệ Trọng tâm (Auth & Profile)
Đây là quan hệ 1-1 (One-to-One) quan trọng nhất:

*   **Account ↔ Student**: Một tài khoản chỉ thuộc về một sinh viên và ngược lại.
    *   `Student` sở hữu cột `account_id` (Owning Side).
    *   `Account` sử dụng `mappedBy="account"`.
*   **Account ↔ Lecturer**: Một tài khoản chỉ thuộc về một giảng viên.
    *   `Lecturer` sở hữu cột `account_id`.

## 2. Quy tắc Cascade
Tất cả quan hệ từ `Account` đến `Student`/`Lecturer` đều sử dụng **`CascadeType.ALL`**.
*   **Hành động**: Khi bạn lưu (save) hoặc xóa (delete) một `Account`, thông tin `Student` hoặc `Lecturer` tương ứng sẽ tự động được xử lý theo. Điều này giúp đồng bộ hóa dữ liệu giữa bảng tài khoản và bảng hồ sơ cá nhân.
