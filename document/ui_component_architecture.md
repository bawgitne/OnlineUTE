# 🏛️ Kiến trúc Thành phần Giao diện (UI Component Architecture)

Dự án OnlineUTE sử dụng mô hình **Smart & Dumb Components** kết hợp với **Atomic Design** để phân tách trách nhiệm giữa logic và hiển thị, giúp mã nguồn dễ bảo trì và mở rộng.

---

## 🏗️ Cấu trúc Phân lớp (Atomic Layers)

### 1. ⚛️ Atoms (Nguyên tử) - `View/Components`
Đây là các thành phần cơ bản nhất, không chứa logic nghiệp vụ, có thể tái sử dụng ở bất cứ đâu.
- **LeftBarButton.java**: Nút bấm trên thanh sidebar. Tự động xử lý màu icon (tinting) và hiển thị trạng thái `Active`.
- **LeftBarTitle.java**: Tiêu đề phân đoạn trong menu (ví dụ: "TRANG CÁ NHÂN"). Theo phong cách HCMUTE portal.

### 2. 🧬 Molecules (Phân tử) - `View/Containers`
Các thành phần phức tạp hơn, kết hợp nhiều Atoms lại với nhau.
- **LeftBar.java**: Thanh Sidebar bên trái. Tự động hiển thị Menu khác nhau dựa trên **Role (STUDENT, LECTURER, ADMIN)**.
- **TopHeader.java**: Thanh tiêu đề phía trên chứa Logo trường và thông tin chung.
- **MainView.java**: Bộ quản lý các trang (Page Manager). Sử dụng `CardLayout` để tráo đổi nội dung giữa các tab cực nhanh mà không phải load lại.
- **AnnouncementTable.java**: Bảng hiển thị danh sách thông báo.

### 3. 📄 Pages (Trang) - `View/Pages`
Nội dung cụ thể sẽ được hiển thị trong khu vực chính của ứng dụng.
- **AnnouncementPage.java**: Trang chứa bảng thông báo.
- **ProfilePage.java**: Trang thông tin sinh viên/giảng viên.
- **SchedulePage.java**: Trang xem thời khóa biểu.

### 4. 🖼️ Organisms/Screens (Màn hình chính) - `View/Screens`
Nơi lắp ráp tất cả các thành phần trên thành một cửa sổ hoàn chỉnh.
- **DashboardFrame.java**: Màn hình chính sau khi đăng nhập. Kết nối `LeftBar` với `MainView` để điều hướng.

---

## 🔁 Cơ chế Hoạt động (Workflow)

### 1. Luồng Điều hướng (Navigation Flow)
1. Người dùng nhấn vào một **LeftBarButton**.
2. `LeftBar` nhận sự kiện click và kích hoạt một hàm **Callback** (Consumer).
3. `DashboardFrame` (Smart Component) nhận tín hiệu và yêu cầu `MainView` hiển thị trang tương ứng bằng **Key** (ví dụ: "Profile", "Schedule").
4. `MainView` tráo đổi Card và hiển thị nội dung ngay lập tức.

### 2. Phân quyền Hiển thị (Role-based UI)
Thanh `LeftBar` nhận tham số `Role` khi khởi tạo:
- **STUDENT**: Hiện menu Học tập, Tài chính.
- **LECTURER**: Hiện menu Giảng dạy, Nhập điểm.
- **ADMIN**: Hiện menu Quản trị hệ thống.

---

## 🎨 Phong cách Thiết kế (Design Tokens)

- **Màu nền chính (Primary Blue)**: `#00558D` (HCMUTE Blue).
- **Màu tiêu đề (Secondary Yellow)**: `#D1CD14`.
- **Phông chữ chính (Primary Font)**: `Segoe UI` (cho cảm giác hiện đại, native trên Windows).
- **Phản hồi người dùng (UX)**: Các nút bấm có hiệu ứng hover và thay đổi màu khi đang được chọn (Active state).
