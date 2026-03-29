# OnlineUTE - Tài liệu luồng API

Tài liệu này mô tả luồng gọi hàm theo đúng mã nguồn hiện tại:

`Controller -> Service -> DAO`

Mục tiêu là giúp người đọc nắm nhanh một chức năng đi qua những lớp nào, gọi hàm nào.

## 1. Phạm vi

- Đây là API ở tầng `Controller` trong ứng dụng desktop Swing, không phải REST endpoint HTTP.
- Tên hàm được giữ nguyên theo source hiện tại.

## 2. Wiring thực tế trong AppContext

Đã được khởi tạo trong `AppContext.init()`:

- `AuthController -> AuthServiceImpl -> AccountDAO, StudentDAO, LecturerDAO, AdminDAO`
- `TermController -> TermServiceImpl -> TermDAO`
- `StudentController -> StudentServiceImpl -> StudentDAO`
- `CourseSectionController -> CourseSectionServiceImpl -> CourseSectionDAO`
- `CourseRegistrationController -> CourseRegistrationServiceImpl -> CourseRegistrationDAO`
- `MarkController -> MarkServiceImpl -> MarkDAO`
- `NotificationController -> AnnouncementServiceImpl, CourseSectionServiceImpl -> AnnouncementDAO, CourseSectionDAO`
- `ScheduleServiceImpl -> ScheduleDAO` (hiện tại View gọi trực tiếp qua `AppContext.getScheduleService()`)

Chưa thấy được wire vào `AppContext`:

- `CourseController`
- `CourseServiceImpl`
- `CourseDAO` trong luồng chính

## 3. Module xác thực (Auth)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/AuthController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/AuthService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/AuthServiceImpl.java`
- `DAO`: `AccountDAO`, `StudentDAO`, `LecturerDAO`, `AdminDAO`

Luồng theo từng hàm:

- `AuthController.Login(username, password)`
  - Gọi `AuthService.login(username, password)`
  - Service gọi `AccountDAO.findByUsername(username)`
  - Nếu đúng mật khẩu, service nạp profile bằng:
    - `StudentDAO.findByAccountId(...)` hoặc
    - `LecturerDAO.findByAccountId(...)` hoặc
    - `AdminDAO.findByAccountId(...)`
  - Kết quả: trả `Optional<Account>` và cập nhật `SessionManager`

- `AuthController.Logout()`
  - Gọi `AuthService.logout()`
  - Không gọi DAO
  - Kết quả: xóa session hiện tại

- `AuthController.RegisterStudent(account, student)`
  - Gọi `AuthService.registerStudent(...)`
  - Service liên kết account vào student
  - Gọi `StudentDAO.save(student)`

- `AuthController.RegisterLecturer(account, lecturer)`
  - Gọi `AuthService.registerLecturer(...)`
  - Service liên kết account vào lecturer
  - Gọi `LecturerDAO.save(lecturer)`

- `AuthController.RegisterAdmin(account, admin)`
  - Gọi `AuthService.registerAdmin(...)`
  - Service liên kết account vào admin
  - Gọi `AdminDAO.save(admin)`

## 4. Module thông báo (Notification/Announcement)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/NotificationController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/AnnouncementService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/AnnouncementServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/AnnouncementDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/AnnouncementDAOImpl.java`

Luồng theo từng hàm:

- `NotificationController.createAnnouncement(title, content, targetType, courseSectionId, senderName)`
  - Gọi `AnnouncementService.createAnnouncement(...)`
  - Service tạo entity `Announcement`
  - Gọi `AnnouncementDAO.create(announcement)`

- `NotificationController.getAnnouncementsForCurrentUser()`
  - Gọi `AnnouncementService.getAnnouncementsForCurrentUser()`
  - Service phân nhánh theo role:
    - `ADMIN`: gọi `AnnouncementDAO.findAll()`
    - `LECTURER`: gọi `AnnouncementDAO.findByTargetType("ALL_LECTURERS")` và `findByTargetType("ALL")`
    - `STUDENT`: gọi `AnnouncementDAO.findAnnouncementsForStudent(studentId)`

- `NotificationController.getCourseSectionsByLecturerId(lecturerId)`
  - Gọi `CourseSectionService.getAllSections()`
  - Service gọi `CourseSectionDAO.findAll()`
  - Controller lọc danh sách theo `lecturerId`

Chi tiết quan trọng cho sinh viên:

- `AnnouncementDAOImpl.findAnnouncementsForStudent(studentId)` lấy:
  - Thông báo `ALL`
  - Thông báo `ALL_STUDENTS`
  - Thông báo `COURSE_SECTION` nếu `courseSectionId` nằm trong các lớp sinh viên đã đăng ký

## 5. Module học kỳ (Term)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/TermController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/TermService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/TermServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/TermDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/TermDAOImpl.java`

Luồng theo từng hàm:

- `TermController.getAllTerms()`
  - `TermService.getAllTerms()`
  - `TermDAO.findAll()`

- `TermController.getTermById(id)`
  - `TermService.getTermById(id)`
  - `TermDAO.findById(id)`

- `TermController.getCurrentTerm()`
  - `TermService.getCurrentTerm()`
  - `TermDAO.findCurrentTerm()`

- `TermController.createTerm(term)`
  - `TermService.createTerm(term)`
  - `TermDAO.save(term)`

- `TermController.updateTerm(term)`
  - `TermService.updateTerm(term)`
  - `TermDAO.update(term)`

- `TermController.deleteTerm(term)`
  - `TermService.deleteTerm(term)`
  - `TermDAO.delete(term)`

## 6. Module lớp học phần (Course Section)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/CourseSectionController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/CourseSectionService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/CourseSectionServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/CourseSectionDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/CourseSectionDAOImpl.java`

Luồng theo từng hàm:

- `CourseSectionController.getSectionsByTerm(termId)`
  - `CourseSectionService.getSectionsByTerm(termId)`
  - `CourseSectionDAO.findByTermId(termId)`

- `CourseSectionController.getAllSections()`
  - `CourseSectionService.getAllSections()`
  - `CourseSectionDAO.findAll()`

- `CourseSectionController.getSectionById(id)`
  - `CourseSectionService.getSectionById(id)`
  - `CourseSectionDAO.findById(id)`

- `CourseSectionController.createSection(section)`
  - `CourseSectionService.createSection(section)`
  - `CourseSectionDAO.save(section)`

## 7. Module đăng ký học phần (Course Registration)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/CourseRegistrationController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/CourseRegistrationService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/CourseRegistrationServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/CourseRegistrationDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/CourseRegistrationDAOImpl.java`

Luồng theo từng hàm:

- `CourseRegistrationController.register(registration)`
  - `CourseRegistrationService.registerToSection(registration)`
  - `CourseRegistrationDAO.save(registration)`

- `CourseRegistrationController.cancel(registration)`
  - `CourseRegistrationService.cancelRegistration(registration)`
  - `CourseRegistrationDAO.delete(registration)`

- `CourseRegistrationController.getStudentRegistrations(studentId)`
  - `CourseRegistrationService.getRegistrationsByStudent(studentId)`
  - `CourseRegistrationDAO.findByStudentId(studentId)`

- `CourseRegistrationController.isStudentInCourse(studentId, sectionId)`
  - `CourseRegistrationService.isStudentRegistered(studentId, sectionId)`
  - `CourseRegistrationDAO.isRegistered(studentId, sectionId)`

## 8. Module điểm (Mark)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/MarkController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/MarkService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/MarkServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/MarkDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/MarkDAOImpl.java`

Luồng theo từng hàm:

- `MarkController.updateMark(mark)`
  - `MarkService.saveMark(mark)`
  - Service tự tính `finalScore` và `gradeChar`
  - `MarkDAO.save(mark)`

- `MarkController.getMarkByRegistration(registrationId)`
  - `MarkService.getMarkByRegistration(registrationId)`
  - `MarkDAO.findByRegistrationId(registrationId)`

- `MarkController.getMarkById(id)`
  - `MarkService.getMarkById(id)`
  - `MarkDAO.findById(id)`

## 9. Module sinh viên (Student)

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/StudentController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/StudentService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/StudentServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/StudentDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/StudentDAOImpl.java`

Luồng theo từng hàm:

- `StudentController.createStudent(student, account)`
  - `StudentService.createStudent(student, account)`
  - Service gắn `student.setAccount(account)`
  - `StudentDAO.save(student)`

## 10. Module thời khóa biểu (Schedule) - hiện chưa qua Controller

Files:

- `Service`: `src/main/java/com/bangcompany/onlineute/Service/ScheduleService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/ScheduleServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/ScheduleDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/ScheduleDAOImpl.java`

Luồng hiện tại trong View:

- `SchedulePage` gọi `AppContext.getScheduleService().getStudentSchedule(studentId)`
- Service gọi `ScheduleDAO.findByStudentId(studentId)`

Ghi chú:

- DAO dùng `JOIN FETCH` để lấy luôn `CourseSection`, `Course`, `Lecturer`, tránh lỗi lazy loading khi render UI.

## 11. Module môn học (Course) - có code nhưng chưa wire AppContext

Files:

- `Controller`: `src/main/java/com/bangcompany/onlineute/Controller/CourseController.java`
- `Service`: `src/main/java/com/bangcompany/onlineute/Service/CourseService.java`
- `ServiceImpl`: `src/main/java/com/bangcompany/onlineute/Service/Impl/CourseServiceImpl.java`
- `DAO`: `src/main/java/com/bangcompany/onlineute/DAO/CourseDAO.java`
- `DAOImpl`: `src/main/java/com/bangcompany/onlineute/DAO/Impl/CourseDAOImpl.java`

Luồng nếu được wire:

- `CourseController.createCourse(course)`
  - `CourseService.createCourse(course)`
  - `CourseDAO.save(course)`

- `CourseController.updateCourse(course)`
  - `CourseService.updateCourse(course)`
  - `CourseDAO.update(course)`

- `CourseController.deleteCourse(course)`
  - `CourseService.deleteCourse(course)`
  - `CourseDAO.delete(course)`

- `CourseController.findById(id)`
  - `CourseService.findById(id)`
  - `CourseDAO.findById(id)`

## 12. Ghi chú kiến trúc

- Vẫn còn chỗ View gọi service trực tiếp (ví dụ `SchedulePage`). Nếu muốn đồng nhất kiến trúc, nên thêm `ScheduleController`.
- `AuthController` đang bắt exception và trả `null` cho các hàm register; có thể chuyển sang trả `Optional` hoặc ném custom exception để dễ debug hơn.
- Tên hàm controller hiện chưa đồng nhất kiểu đặt tên (`Login`, `Logout`, `createStudent`).
