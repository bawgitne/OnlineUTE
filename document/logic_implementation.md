# 🛠️ Quy trình triển khai Logic (Service & Controller)

Tài liệu này mô tả cách các thành phần trong hệ thống phối hợp với nhau để phục vụ ứng dụng Java Swing.

## 1. Tầng Dịch vụ (Service Layer)
Lớp tiêu biểu: `AuthServiceImpl`

### Chức năng chính:
*   **Đăng ký đồng bộ**: Phương thức `registerStudent` và `registerLecturer` thực hiện kết nối hai đối tượng (`Account` và `Profile`) trước khi lưu.
*   **Tính toàn vẹn**: Đảm bảo tài khoản và hồ sơ luôn đi đôi với nhau.

## 2. Tầng Điều phối (Controller Layer)
Lớp tiêu biểu: `AuthController`

### Cách thức hoạt động cho Java Swing:
*   **Manual Dependency Injection**: Vì không dùng Spring, Controller tự khởi tạo các DAO Impl và truyền vào Service Impl qua Constructor.
*   **Cầu nối UI-Service**: Controller tiếp nhận dữ liệu từ các form Swing (JFrame), xử lý lỗi qua khối `try-catch` và trả kết quả về cho giao diện người dùng hiển thị thông báo (`JOptionPane`).

## 3. Luồng dữ liệu (Data Flow)
1.  **View (Swing)**: Nhận input từ người dùng -> Tạo đối tượng Entity.
2.  **Controller**: Nhận đối tượng -> Gọi Service xử lý.
3.  **Service**: Thực hiện logic nghiệp vụ -> Gọi DAO để ghi xuống DB.
4.  **DAO**: Thực hiện các thao tác JPA/Hibernate.
