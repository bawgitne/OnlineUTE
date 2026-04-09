# OnlineUTE - Hệ thống Quản lý Đăng ký Học phần Đại học

Ứng dụng desktop hỗ trợ toàn bộ quy trình đăng ký học phần, quản lý thời khóa biểu và nhập điểm cho sinh viên, giảng viên và quản trị viên.

---

## Tính năng chính

### Sinh viên
- Xem thời khóa biểu theo tuần (có thể chuyển tuần trước/sau)
- Đăng ký môn học theo các đợt mở đăng ký
- Xem và hủy môn đã đăng ký
- Xem kết quả học tập chi tiết (điểm hệ 10, hệ 4, điểm chữ, kết quả)

### Giảng viên
- Nhập điểm và điểm danh cho lớp học phần mình phụ trách
- Xem thời khóa biểu giảng dạy theo tuần
- Nhận thông báo từ hệ thống

### Quản trị viên (Admin)
- Quản lý các đợt đăng ký môn học
- Tạo và quản lý lớp học phần
- Quản lý tài khoản sinh viên, giảng viên, admin
- Quản lý môn học, khoa, ngành, lớp, học kỳ
- Gửi thông báo hệ thống

---

## Công nghệ & Kiến trúc

- Ngôn ngữ: Java 17 + Swing
- Cơ sở dữ liệu: MySQL + JPA/Hibernate
- Kiến trúc: MVC thuần
- Quản lý dependency: AppContext instance-based
- Xử lý transaction: JpaUtil.doInTransaction
- Xử lý lỗi: ExceptionHandler + BusinessException

### Các cải tiến kỹ thuật đã áp dụng
- Viết AbstractDAO generic để tránh lặp code CRUD ở tất cả các DAOImpl
- Chuyển AppContext từ static sang instance để dependency rõ ràng và dễ test hơn
- Tạo ViewContext làm lớp trung gian, giúp View không gọi trực tiếp AppContext
- Triển khai ExceptionHandler thống nhất để hiển thị popup lỗi và thông báo thành công
- Thêm BusinessException riêng để phân biệt lỗi nghiệp vụ với lỗi kỹ thuật
- Tối ưu query bằng JOIN FETCH để giảm nguy cơ N+1
- Thiết kế giao diện với Card, RoundedBorders, Tabs và AppTheme riêng biệt

---

## Hướng dẫn cài đặt & chạy

### 1. Chuẩn bị Database
Chạy lần lượt các file SQL trong thư mục `database/`:
1. `01_create_database.sql`
2. `02_create_tables.sql`
3. `03_insert_sample_data.sql`

### 2. Thông tin tài khoản tạo sẵn
#### 1. Role ADMIN:
- Username: AD001
- Password: admin123
#### 2. Role GIANGVIEN:
- Username: GV001 - GV011
- Password: 123456
#### 3. Role SINHVIEC:
- Username: 24110001 - 24110200
- Password: 123456
