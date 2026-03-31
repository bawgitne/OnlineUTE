# Tài liệu tầng Service và query database

Tệp này mô tả các hàm chính trong tầng `Service` của dự án `OnlineUTE`, vai trò của từng hàm, và các truy vấn database mà các hàm đó sử dụng thông qua tầng `DAO`.

Mục tiêu của tài liệu:
- Giúp đọc source nhanh hơn
- Biết service nào xử lý nghiệp vụ gì
- Hiểu mỗi hàm service đang chạm vào bảng nào
- Hiểu ý nghĩa của các câu JPQL đang được dùng

## Luồng chung

Luồng chuẩn của dự án:

`View -> Controller -> Service -> DAO -> JPA/Hibernate -> Database`

Trong đó:
- `Service` là nơi xử lý nghiệp vụ
- `DAO` là nơi chứa lệnh truy vấn và thao tác lưu/xóa/sửa dữ liệu

## 1. AccountService

File:
- [AccountService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\AccountService.java)
- [AccountServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\AccountServiceImpl.java)

Vai trò:
- Tạo tài khoản theo từng role
- Gắn `Account` vào profile nghiệp vụ tương ứng

### `createStudentAccount(Account account, Student student)`
- Gắn `account` vào `student`
- Gọi `studentDAO.save(student)`
- Do entity `Student` có quan hệ với `Account`, khi lưu `Student` thì account cũng được lưu theo

DB tác động:
- `student`
- `account`

Query/DAO:
- Không có JPQL thủ công trong service
- Đi qua `StudentDAOImpl.save(...)`
- Bản chất là `persist` hoặc `merge` entity `Student`

### `createLecturerAccount(Account account, Lecturer lecturer)`
- Gắn `account` vào `lecturer`
- Gọi `lecturerDAO.save(lecturer)`

DB tác động:
- `lecturer`
- `account`

### `createAdminAccount(Account account, Admin admin)`
- Gắn `account` vào `admin`
- Gọi `adminDAO.save(admin)`

DB tác động:
- `admin`
- `account`

## 2. AuthService

File:
- [AuthService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\AuthService.java)
- [AuthServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\AuthServiceImpl.java)

Vai trò:
- Đăng nhập
- Đăng xuất
- Nạp session theo role

### `login(String username, String password)`
- Tìm account theo username
- So sánh password hiện tại với `password_hash`
- Nếu đúng thì nạp session

DAO/query đi qua:
- `accountDAO.findByUsername(username)`
- `studentDAO.findByAccountId(accountId)`
- `lecturerDAO.findByAccountId(accountId)`
- `adminDAO.findByAccountId(accountId)`

Ý nghĩa query:
- Tìm đúng account đăng nhập
- Sau đó dựa theo role để lấy profile thật

Các bảng liên quan:
- `account`
- `student`
- `lecturer`
- `admin`

Lưu ý:
- Hiện tại password đang so sánh trực tiếp chuỗi, chưa có cơ chế hash/verify chuẩn production

### `logout()`
- Gọi `SessionManager.logout()`
- Không query DB

## 3. AnnouncementService

File:
- [AnnouncementService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\AnnouncementService.java)
- [AnnouncementServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\AnnouncementServiceImpl.java)

Vai trò:
- Tạo thông báo
- Trả thông báo phù hợp theo role hiện tại

### `createAnnouncement(String title, String content, String targetType, Long courseSectionId, String senderName)`
- Tạo entity `Announcement`
- Gọi `announcementDAO.create(...)`

DB tác động:
- `announcement`

### `getAnnouncementsForCurrentUser()`
- Xét role trong `SessionManager`
- Nếu admin: lấy tất cả
- Nếu lecturer: lấy `ALL_LECTURERS` và `ALL`
- Nếu student: lấy theo logic dành cho sinh viên

DAO/query đi qua:

#### `announcementDAO.findAll()`
JPQL:
```jpql
SELECT a FROM Announcement a ORDER BY a.createdAt DESC
```
Ý nghĩa:
- Lấy toàn bộ thông báo mới nhất lên trước

#### `announcementDAO.findByTargetType(targetType)`
JPQL:
```jpql
SELECT a FROM Announcement a WHERE a.targetType = :tt ORDER BY a.createdAt DESC
```
Ý nghĩa:
- Lọc thông báo đúng nhóm nhận

#### `announcementDAO.findAnnouncementsForStudent(studentId)`
JPQL:
```jpql
SELECT a
FROM Announcement a
WHERE a.targetType = 'ALL'
   OR a.targetType = 'ALL_STUDENTS'
   OR (
        a.targetType = 'COURSE_SECTION'
        AND a.courseSectionId IN (
            SELECT cr.courseSection.id
            FROM CourseRegistration cr
            WHERE cr.student.id = :studentId
        )
      )
ORDER BY a.createdAt DESC
```
Ý nghĩa:
- Sinh viên thấy:
  - thông báo toàn hệ thống
  - thông báo toàn sinh viên
  - thông báo gửi cho lớp học phần mà sinh viên đã đăng ký

Các bảng liên quan:
- `announcement`
- `course_registration`

## 4. ClassService

File:
- [ClassService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\ClassService.java)
- [ClassServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\ClassServiceImpl.java)

### `getAllClasses()`
- Gọi `classDAO.findAll()`
- Trả toàn bộ lớp

DB tác động:
- `class`

Ý nghĩa query:
- Dùng cho combobox khi tạo tài khoản sinh viên

## 5. CourseService

File:
- [CourseService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\CourseService.java)
- [CourseServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\CourseServiceImpl.java)

Các hàm:
- `createCourse(course)` -> `courseDAO.save(course)`
- `updateCourse(course)` -> `courseDAO.update(course)`
- `deleteCourse(course)` -> `courseDAO.delete(course)`
- `findById(id)` -> `courseDAO.findById(id)`
- `getAllCourses()` -> `courseDAO.findAll()`

DB tác động:
- `course`

Ý nghĩa:
- Service CRUD cơ bản cho bảng môn học

## 6. StudentService

File:
- [StudentService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\StudentService.java)
- [StudentServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\StudentServiceImpl.java)

### `createStudent(Student student, Account account)`
- Gắn account vào student
- Gọi `studentDAO.save(student)`

DB tác động:
- `student`
- `account`

Vai trò:
- Tạo sinh viên mới theo kiểu save profile

Lưu ý:
- Hiện tại phần tạo tài khoản chính đang ưu tiên đi qua `AccountService`

## 7. StudyProgramService

File:
- [StudyProgramService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\StudyProgramService.java)
- [StudyProgramServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\StudyProgramServiceImpl.java)

### `getAllStudyPrograms()`
- Gọi `studyProgramDAO.findAll()`

DB tác động:
- `study_program`

Ý nghĩa:
- Nạp danh sách chương trình đào tạo cho form tạo sinh viên và các UI tham chiếu

## 8. TermService

File:
- [TermService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\TermService.java)
- [TermServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\TermServiceImpl.java)

Các hàm:
- `getAllTerms()` -> `termDAO.findAll()`
- `getTermById(id)` -> `termDAO.findById(id)`
- `getCurrentTerm()` -> `termDAO.findCurrentTerm()`
- `createTerm(term)` -> `termDAO.save(term)`
- `updateTerm(term)` -> `termDAO.update(term)`
- `deleteTerm(term)` -> `termDAO.delete(term)`

DB tác động:
- `term`

Query đáng chú ý:
- `findCurrentTerm()` thường dùng để lấy học kỳ đang active, phục vụ tra cứu/lọc dữ liệu hiện hành

## 9. MarkService

File:
- [MarkService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\MarkService.java)
- [MarkServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\MarkServiceImpl.java)

Vai trò:
- Lưu điểm
- Tính điểm tổng kết và điểm chữ

### `saveMark(Mark mark)`
- Gọi `calculateGrade(mark)`
- Sau đó `markDAO.save(mark)`

DB tác động:
- `mark`

### `deleteMark(Mark mark)`
- Gọi `markDAO.delete(mark)`

### `getMarkById(id)`
- Gọi `markDAO.findById(id)`

### `getMarkByRegistration(registrationId)`
- Gọi `markDAO.findByRegistrationId(registrationId)`

Query chính:
- tìm điểm theo `registration_id`

Ý nghĩa:
- mỗi `mark` gắn với một `course_registration`
- đây là điểm của một sinh viên trong một lớp học phần cụ thể

### `calculateGrade(Mark mark)`
- Không query DB
- Tính:
  - `finalScore = 30% process + 70% test`
  - ánh xạ sang `A/B/C/D/F`

## 10. UserProfileService

File:
- [UserProfileService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\UserProfileService.java)
- [UserProfileServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\UserProfileServiceImpl.java)

Vai trò:
- Lấy hồ sơ người dùng hiện tại
- Fallback về thông tin trong session nếu bảng `user_profile` chưa có dữ liệu

### `findByAccountId(accountId)`
- Gọi `userProfileDAO.findByAccountId(accountId)`

DB tác động:
- `user_profile`

### `getCurrentUserProfile()`
- Lấy account hiện tại từ `SessionManager`
- Nếu có `user_profile` trong DB thì dùng DB
- Nếu không có thì dựng profile fallback từ:
  - `Student`
  - `Lecturer`
  - `Admin`

Ý nghĩa:
- giao diện hồ sơ vẫn hiển thị được dù DB chưa seed đủ bảng `user_profile`

## 11. RegistrationBatchService

File:
- [RegistrationBatchService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\RegistrationBatchService.java)
- [RegistrationBatchServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\RegistrationBatchServiceImpl.java)

Vai trò:
- Quản lý đợt đăng ký môn
- Lọc đợt đang mở theo thời gian

### `createBatch(registrationBatch)`
- Validate nghiệp vụ
- Gọi `registrationBatchDAO.save(...)`

### `updateBatch(registrationBatch)`
- Validate rồi `save(...)`

### `getBatchById(id)`
JPQL:
```jpql
SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term WHERE rb.id = :id
```
Ý nghĩa:
- lấy batch theo id
- `JOIN FETCH rb.term` để UI đọc được `term` sau khi entity manager đóng

### `getBatchesByTerm(termId)`
JPQL:
```jpql
SELECT rb FROM RegistrationBatch rb
JOIN FETCH rb.term t
WHERE t.id = :termId
ORDER BY rb.createdAt DESC
```
Ý nghĩa:
- lấy các đợt theo học kỳ
- mới nhất lên trước

### `getAllBatches()`
JPQL:
```jpql
SELECT rb FROM RegistrationBatch rb JOIN FETCH rb.term ORDER BY rb.createdAt DESC
```
Ý nghĩa:
- lấy toàn bộ đợt đăng ký
- fetch luôn `term`

### `getOpenBatches(currentTime)`
JPQL:
```jpql
SELECT rb FROM RegistrationBatch rb
JOIN FETCH rb.term
WHERE rb.openAt <= :currentTime AND rb.closeAt >= :currentTime
ORDER BY rb.openAt ASC
```
Ý nghĩa:
- chỉ lấy các đợt đang mở tại thời điểm hiện tại
- dùng cho màn đăng ký môn học của sinh viên

DB tác động:
- `registration_batch`
- `term`

## 12. CourseSectionService

File:
- [CourseSectionService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\CourseSectionService.java)
- [CourseSectionServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\CourseSectionServiceImpl.java)

Vai trò:
- Tạo lớp học phần
- Gắn lớp học phần vào đợt đăng ký
- Tự tính ngày học đầu/cuối
- Tự sinh lịch học theo tuần
- Kiểm tra trùng lịch giảng viên/phòng

### `createSection(section)`
Luồng:
- validate dữ liệu
- check trùng lịch giảng viên/phòng
- tính `firstStudyDate` và `lastStudyDate`
- `courseSectionDAO.save(section)`
- `scheduleService.regenerateSectionSchedules(...)`

DB tác động:
- `course_section`
- `schedule`

### `createSectionForBatch(registrationBatch, section)`
- Gắn `registrationBatch`
- copy `term` từ batch sang section
- gọi `createSection(section)`

Ý nghĩa:
- section luôn thuộc một đợt đăng ký cụ thể

### `updateSection(section)`
- validate lại
- tính lại ngày
- update section
- regenerate lại toàn bộ `schedule`

### `deleteSection(section)`
- xóa toàn bộ `schedule` trước
- xóa `course_section`

### `getSectionById(id)`
Query:
- `courseSectionDAO.findById(id)`
- DAO dùng `JOIN FETCH` với:
  - `course`
  - `lecturer`
  - `registrationBatch`
  - `term`

Ý nghĩa:
- lấy đủ dữ liệu để UI và service dùng tiếp mà không dính lazy-load

### `getSectionsByTerm(termId)`
- lấy các section của một học kỳ

### `getSectionsByBatch(registrationBatchId)`
JPQL:
```jpql
SELECT cs FROM CourseSection cs
JOIN FETCH cs.course
LEFT JOIN FETCH cs.lecturer
WHERE cs.registrationBatch.id = :registrationBatchId
```
Ý nghĩa:
- lấy các lớp học phần thuộc một đợt đăng ký
- fetch luôn môn học và giảng viên để render UI

### `getAllSections()`
JPQL:
```jpql
SELECT cs FROM CourseSection cs
JOIN FETCH cs.course
LEFT JOIN FETCH cs.lecturer
```
Ý nghĩa:
- lấy toàn bộ section, kèm course và lecturer

### Query kiểm tra trùng lịch
JPQL:
```jpql
SELECT cs FROM CourseSection cs
JOIN FETCH cs.course
LEFT JOIN FETCH cs.lecturer
WHERE cs.term.id = :termId
AND cs.dayOfWeek = :dayOfWeek
AND cs.startSlot <= :endSlot
AND cs.endSlot >= :startSlot
```
Ý nghĩa:
- lấy tất cả section trong cùng học kỳ, cùng thứ, và giao nhau về khoảng tiết
- từ tập này, service kiểm tra:
  - có cùng giảng viên không
  - có cùng phòng không

## 13. ScheduleService

File:
- [ScheduleService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\ScheduleService.java)
- [ScheduleServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\ScheduleServiceImpl.java)

Vai trò:
- Lưu lịch học từng buổi
- Trả lịch theo section
- Trả lịch theo sinh viên
- Trả lịch theo tuần

### `saveSchedule(schedule)`
- lưu một bản ghi `schedule`

### `regenerateSectionSchedules(courseSectionId, schedules)`
- xóa toàn bộ lịch cũ của section
- lưu lại toàn bộ lịch mới

DAO/query:
- `DELETE FROM Schedule s WHERE s.courseSection.id = :courseSectionId`

Ý nghĩa:
- khi admin đổi thông tin section, lịch của section sẽ được sinh lại từ đầu

### `getSectionSchedules(courseSectionId)`
JPQL:
```jpql
SELECT s FROM Schedule s
WHERE s.courseSection.id = :courseSectionId
ORDER BY s.weekNumber
```
Ý nghĩa:
- lấy toàn bộ buổi học của một section theo thứ tự tuần

### `getStudentSchedule(studentId)`
JPQL:
```jpql
SELECT s FROM Schedule s
JOIN FETCH s.courseSection cs
JOIN FETCH cs.course
JOIN FETCH cs.lecturer
JOIN cs.courseRegistrations cr
WHERE cr.student.id = :studentId
ORDER BY s.studyDate, s.startSlot
```
Ý nghĩa:
- lấy toàn bộ lịch của sinh viên
- join qua `course_registration`
- fetch luôn môn và giảng viên để UI render

### `getStudentScheduleByWeek(studentId, weekStart, weekEnd)`
JPQL:
```jpql
SELECT s FROM Schedule s
JOIN FETCH s.courseSection cs
JOIN FETCH cs.course
JOIN FETCH cs.lecturer
JOIN cs.courseRegistrations cr
WHERE cr.student.id = :studentId
AND s.studyDate BETWEEN :startDate AND :endDate
ORDER BY s.studyDate, s.startSlot
```
Ý nghĩa:
- chỉ lấy các buổi học nằm trong một tuần cụ thể
- dùng cho trang thời khóa biểu theo tuần

DB tác động:
- `schedule`
- `course_section`
- `course_registration`
- `course`
- `lecturer`

## 14. CourseRegistrationService

File:
- [CourseRegistrationService.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\CourseRegistrationService.java)
- [CourseRegistrationServiceImpl.java](C:\Users\bangn\Downloads\OnlineUTE\src\main\java\com\bangcompany\onlineute\Service\Impl\CourseRegistrationServiceImpl.java)

Vai trò:
- Đăng ký môn cho sinh viên
- Hủy đăng ký
- Tra cứu danh sách đăng ký
- Chặn trùng lịch khi đăng ký

### `registerToSection(registration)`
- save trực tiếp một entity `CourseRegistration`
- ít business logic

### `registerStudentToSection(studentId, sectionId)`
Đây là hàm nghiệp vụ quan trọng nhất.

Luồng:
1. tìm sinh viên theo id
2. tìm section theo id
3. kiểm tra section có thuộc đợt đăng ký không
4. kiểm tra thời gian hiện tại có nằm trong `openAt -> closeAt` không
5. kiểm tra sinh viên đã đăng ký chưa
6. kiểm tra sĩ số còn chỗ không
7. kiểm tra trùng lịch với các section đã đăng ký
8. tạo `course_registration`
9. tăng `currentCapacity` của `course_section`

DAO/query đi qua:

#### `studentDAO.findById(studentId)`
- lấy thông tin sinh viên

#### `courseSectionDAO.findById(sectionId)`
- lấy lớp học phần cần đăng ký

#### `registrationDAO.isRegistered(studentId, sectionId)`
JPQL:
```jpql
SELECT COUNT(cr)
FROM CourseRegistration cr
WHERE cr.student.id = :studentId
AND cr.courseSection.id = :sectionId
```
Ý nghĩa:
- kiểm tra sinh viên đã có bản ghi đăng ký chưa

#### `registrationDAO.findByStudentId(studentId)`
JPQL:
```jpql
SELECT cr FROM CourseRegistration cr
JOIN FETCH cr.courseSection cs
JOIN FETCH cs.course
LEFT JOIN FETCH cr.mark
WHERE cr.student.id = :studentId
```
Ý nghĩa:
- lấy toàn bộ section sinh viên đã đăng ký để check trùng lịch

#### `registrationDAO.save(registration)`
- insert bản ghi vào `course_registration`

#### `courseSectionDAO.save(section)`
- update lại `current_capacity`

### `cancelRegistration(registration)`
- xóa bản ghi đăng ký

### `getRegistrationById(id)`
- lấy một bản ghi đăng ký theo id

### `getRegistrationsByStudent(studentId)`
- trả toàn bộ đăng ký của sinh viên

### `getRegistrationsBySection(sectionId)`
JPQL:
```jpql
SELECT cr FROM CourseRegistration cr
JOIN FETCH cr.student
LEFT JOIN FETCH cr.mark
WHERE cr.courseSection.id = :sectionId
```
Ý nghĩa:
- lấy danh sách sinh viên trong một lớp học phần
- fetch luôn điểm nếu đã có

### `isStudentRegistered(studentId, sectionId)`
- wrapper cho query đếm số lượng đăng ký

## Ghi chú về query

### 1. Vì sao nhiều query dùng `JOIN FETCH`
- Để lấy luôn entity liên quan ngay trong một query
- Tránh lỗi `LazyInitializationException`
- Rất cần cho Swing UI vì UI thường dùng dữ liệu sau khi `EntityManager` đã đóng

### 2. Vì sao có chỗ service không viết query
- Service chỉ orchestration nghiệp vụ
- Query thực tế nằm ở DAO
- Tài liệu này ghi cả logic service và query DAO mà service sử dụng

### 3. Những nơi query quan trọng nhất hiện tại
- `AnnouncementDAOImpl.findAnnouncementsForStudent`
- `RegistrationBatchDAOImpl.findOpenBatches`
- `CourseSectionDAOImpl.findConflictingSections`
- `ScheduleDAOImpl.findByStudentIdAndDateRange`
- `CourseRegistrationDAOImpl.findByStudentId`

## Kết luận

Tầng service hiện tại có 3 nhóm chính:

1. Service CRUD cơ bản
- `ClassService`
- `CourseService`
- `StudyProgramService`
- `TermService`

2. Service nghiệp vụ người dùng
- `AuthService`
- `AccountService`
- `UserProfileService`
- `AnnouncementService`

3. Service nghiệp vụ học vụ
- `RegistrationBatchService`
- `CourseSectionService`
- `ScheduleService`
- `CourseRegistrationService`
- `MarkService`

Nếu mở rộng tài liệu này sau, nên bổ sung thêm:
- Controller gọi vào service nào
- DAO nào dùng query nào
- bảng DB nào bị tác động khi một use case chạy end-to-end
