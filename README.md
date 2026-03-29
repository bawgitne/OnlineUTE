# OnlineUTE - Hệ Thống Quản Lý Đăng Ký Học Phần

OnlineUTE là một ứng dụng quản lý đào tạo và đăng ký học phần được phát triển dựa trên ngôn ngữ Java, sử dụng Java Swing làm giao diện người dùng và Hibernate/JPA để quản lý tầng dữ liệu.

## Mô phỏng tương đối hệ thống quản lí nhân sự của trường HCMUTE

*   **Quản Lý Tài Khoản**: Đăng nhập phân quyền (Admin, Sinh viên, Giảng viên).
*   **Quản Lý Sinh Viên**: Theo dõi thông tin sinh viên, lớp học, chương trình học.
*   **Quản Lý Học Phần (Course)**: Quản lý danh sách môn học.
*   **Quản Lý Học Kỳ (Term)**: Quản lý các kỳ học (Năm học, Học kỳ hiện tại).
*   **Lớp Học Phần (CourseSection)**: Mở lớp học phần cho từng học kỳ.
*   **Đăng Ký Học Phần**: Sinh viên đăng ký và hủy học phần trực tuyến.
*   **Quản Lý Điểm Số (Mark)**: Tự động tính điểm tổng kết (30% QT + 70% Thi) và xếp loại học lực (A, B, C, D, F).
*   **Lịch Học & Lịch Thi**: Theo dõi thời khóa biểu và lịch thi.

## TechStack

*   **Ngôn ngữ**: Java 17+
*   **Giao diện**: Java Swing (MVC Pattern)
*   **ORM**: Hibernate / Jakarta Persistence (JPA)
*   **Database**: MySQL 8.0
*   **Quản lý phụ thuộc**: Maven
*   **Manual DI**: Centralized `AppContext` container.

## Cách cài

### 1. Chuẩn bị Cơ sở dữ liệu
*   Sử dụng MySQL (XAMPP hoặc MySQL Workbench).
*   Tạo database tên là: `online_ute`.
*   Đảm bảo user/password tương ứng với file cấu hình (Mặc định: `root`/`1234`).

### 2. Cấu hình Persistence (Nếu cần)
Kiểm tra và chỉnh sửa file:
`src/main/resources/META-INF/persistence.xml`
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/online_ute"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="1234"/>
```

### 3. Xây dựng và Chạy ứng dụng
Mở terminal tại thư mục gốc của dự án:

**Dùng Maven Wrapper:**
```bash
./mvnw clean install
./mvnw exec:java -Dexec.mainClass="com.bangcompany.onlineute.OnlineUteApplication"
```

**Hoặc chạy trực tiếp từ IDE:**
Chạy file `src/main/java/com/bangcompany.onlineute/OnlineUteApplication.java`.

## Folder Structure

*   `Config/`: Cấu hình hệ thống (JpaUtil, AppContext).
*   `DAO/`: Tầng giao tiếp dữ liệu (Data Access Objects).
*   `Service/`: Tầng xử lý logic nghiệp vụ.
*   `Controller/`: Tầng điều khiển kết nối giữa View và Service.
*   `Model/Entity/`: Định nghĩa các thực thể (Database Mapping).
*   `View/`: Giao diện Java Swing UI.

##  Tác Giả
Dự án được thực hiện bởi Team BangCompany.
