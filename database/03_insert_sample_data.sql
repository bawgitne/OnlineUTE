USE online_ute;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ
TRUNCATE TABLE `mark`;
TRUNCATE TABLE `schedule`;
TRUNCATE TABLE `course_registration`;
TRUNCATE TABLE `course_section`;
TRUNCATE TABLE `registration_batch`;
TRUNCATE TABLE `class`;
TRUNCATE TABLE `major`;
TRUNCATE TABLE `faculty`;
TRUNCATE TABLE `term`;
TRUNCATE TABLE `user_profile`;
TRUNCATE TABLE `student`;
TRUNCATE TABLE `lecturer`;
TRUNCATE TABLE `admin`;
TRUNCATE TABLE `account`;
TRUNCATE TABLE `course`;
TRUNCATE TABLE `announcement`;

SET FOREIGN_KEY_CHECKS = 1;

-- 1) ACADEMIC TERMS & BATCH
INSERT INTO `term` (`id`, `year_name`, `term_name`, `is_current`) VALUES (1, '2025-2026', 'HK2', TRUE);
INSERT INTO `registration_batch` (`id`, `name`, `open_at`, `close_at`, `term_id`, `common_start_date`) VALUES
(1, 'Đợt đăng ký HK2 (2025-2026)', '2025-12-01 00:00:00', '2026-07-01 23:59:59', 1, '2026-02-16');

-- 2) MASTER DATA
INSERT INTO `faculty` (`id`, `faculty_code`, `fullname`) VALUES 
(1, 'IT', 'Khoa Công nghệ Thông tin'), (2, 'ECON', 'Khoa Kinh tế'), (3, 'FL', 'Trung tâm Phát triển ngôn ngữ'),
(4, 'POLI', 'Chính trị và Luật'), (5, 'PHED', 'Trung tâm Giáo dục thể chất'), (6, 'APPS', 'Khoa học ứng dụng'),
(7, 'ELEC', 'Điện - Điện tử'), (8, 'GDQP', 'Trung tâm GDQP và An ninh'), (9, 'EDU', 'Viện Sư phạm kỹ thuật');

INSERT INTO `major` (`id`, `major_code`, `fullname`, `total_credit`, `faculty_id`) VALUES (1, '110', 'Công nghệ thông tin', 145, 1);
INSERT INTO `class` (`id`, `class_name`, `major_id`) VALUES (1, '251101A', 1), (2, '251101B', 1);

-- 3) TÊN TIẾNG VIỆT NGẪU NHIÊN
DROP TABLE IF EXISTS `vn_names_sample`;
CREATE TABLE `vn_names_sample` (id INT AUTO_INCREMENT PRIMARY KEY, ho VARCHAR(50), dem VARCHAR(50), ten VARCHAR(50));
INSERT INTO `vn_names_sample` (ho, dem, ten) VALUES 
('Nguyễn', 'Văn', 'Nam'), ('Trần', 'Thị', 'Lan'), ('Lê', 'Minh', 'Hùng'), ('Phạm', 'Anh', 'Hương'), 
('Hoàng', 'Ngọc', 'Tuấn'), ('Phan', 'Hoàng', 'Tuyết'), ('Vũ', 'Đức', 'Trang'), ('Đặng', 'Thành', 'Dũng'), 
('Bùi', 'Thu', 'Hoa'), ('Đỗ', 'Kim', 'Bình'), ('Ngô', 'Thanh', 'Tùng'), ('Dương', 'Hữu', 'Sơn'),
('Lý', 'Quốc', 'Khánh'), ('Trịnh', 'Bảo', 'Long'), ('Đinh', 'Phương', 'Linh'), ('Lương', 'Gia', 'Bảo');

-- 4) GIẢNG VIÊN
INSERT INTO `account` (`id`, `username`, `password_hash`, `salt`, `role`) VALUES 
(1, 'ad001', 'BH4wEHOGUXWnr9CeyNtuPArhbJ61DOmpWzZirw3WzM4=', FROM_BASE64('b0BCNeKjIPAmpx+BKf2Cuw=='), 'ADMIN'),
(2, 'gv001', 'yzl/CfBoRcAM4zJqbBs8toUbBL9BD3XKTtTepjMt1Nk=', FROM_BASE64('lnR7/yv1raqSqjZgkWqKCA=='), 'LECTURER');

INSERT INTO `account` (`id`, `username`, `password_hash`, `salt`, `role`)
SELECT n + 2, CONCAT('gv', LPAD(n + 1, 3, '0')), 'yzl/CfBoRcAM4zJqbBs8toUbBL9BD3XKTtTepjMt1Nk=', FROM_BASE64('lnR7/yv1raqSqjZgkWqKCA=='), 'LECTURER'
FROM (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS nums;

INSERT INTO `admin` (`code`, `fullname`, `account_id`) VALUES ('ad001', 'Quản trị viên Hệ thống', 1);
INSERT INTO `lecturer` (`id`, `code`, `fullname`, `account_id`) VALUES (11, 'gv001', 'TS. Nguyễn Minh Thái', 2);
INSERT INTO `lecturer` (`id`, `code`, `fullname`, `account_id`)
SELECT n, CONCAT('gv', LPAD(n + 1, 3, '0')), 
       (SELECT CONCAT(IF(RAND()>0.5,'TS. ','ThS. '), h.ho, ' ', d.dem, ' ', t.ten) FROM vn_names_sample h, vn_names_sample d, vn_names_sample t ORDER BY RAND() LIMIT 1),
       n + 2
FROM (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS nums;

-- 5) 200 SINH VIÊN (24110001 - 24110200)
INSERT INTO `account` (`id`, `username`, `password_hash`, `salt`, `role`)
SELECT n + 12, CONCAT('2411', LPAD(n, 4, '0')), 'DBpfy7ovY65gjdPDFS63jUWbsQtqs0NZQLOPIkSxm6g=', FROM_BASE64('S78u7p+By1O5N4k5zgrjiA=='), 'STUDENT'
FROM (SELECT ROW_NUMBER() OVER () as n FROM vn_names_sample t1, vn_names_sample t2, vn_names_sample t3 LIMIT 200) AS nums;

INSERT INTO `student` (`id`, `code`, `fullname`, `email`, `class_id`, `enrollment_year`, `account_id`)
SELECT n, CONCAT('2411', LPAD(n, 4, '0')), 
       (SELECT CONCAT(h.ho, ' ', d.dem, ' ', t.ten) FROM vn_names_sample h, vn_names_sample d, vn_names_sample t ORDER BY RAND() LIMIT 1),
       CONCAT('2411', LPAD(n, 4, '0'), '@student.hcmute.edu.vn'),
       IF(RAND() > 0.5, 1, 2), 2025, n + 12
FROM (SELECT ROW_NUMBER() OVER () as n FROM vn_names_sample t1, vn_names_sample t2, vn_names_sample t3 LIMIT 200) AS nums;

-- 6) USER PROFILES
INSERT INTO `user_profile` (`account_id`, `profile_code`, `display_name`, `email`, `role_title`)
SELECT id, username, 
       (SELECT fullname FROM student WHERE account_id = a.id UNION SELECT fullname FROM lecturer WHERE account_id = a.id UNION SELECT fullname FROM admin WHERE account_id = a.id),
       CONCAT(username, '@hcmute.edu.vn'), 
       CASE WHEN role='STUDENT' THEN 'Sinh viên' WHEN role='LECTURER' THEN 'Giảng viên' ELSE 'Quản trị viên' END
FROM `account` a;

-- 7) COURSES
INSERT INTO `course` (`id`, `course_code`, `fullname`, `credit`) VALUES 
(1, 'ENCS040026', 'Kỹ năng Giao tiếp Tiếng Anh Cơ bản', 4), (2, 'ENCS140026', 'Kỹ năng Giao tiếp Tiếng Anh 1', 4),
(3, 'ENCS240026', 'Kỹ năng Giao tiếp Tiếng Anh 2', 4), (4, 'LLCT120205', 'Kinh tế chính trị Mác - Lênin', 2),
(5, 'LLCT120314', 'Tư tưởng Hồ Chí Minh', 2), (29, 'INIT130185', 'Nhập môn ngành CNTT', 3),
(30, 'INPR140285', 'Nhập môn lập trình', 4), (35, 'DIGR240485', 'Toán rời rạc và lý thuyết đồ thị', 4),
(42, 'PRTE230385', 'Kỹ thuật lập trình', 3), (46, 'DASA230179', 'Cấu trúc dữ liệu và giải thuật', 3),
(49, 'OOPR230279', 'Lập trình hướng đối tượng', 3), (55, 'DBSY240184', 'Cơ sở dữ liệu', 4),
(62, 'MALE431984', 'Học máy', 3), (63, 'SOEN330679', 'Công nghệ phần mềm', 3), (64, 'WEPR330479', 'Lập trình Web', 3);
INSERT IGNORE INTO `course` (`id`, `course_code`, `fullname`, `credit`) VALUES 
(6, 'LLCT120405', 'Chủ nghĩa xã hội khoa học', 2), (7, 'LLCT220514', 'Lịch sử Đảng CSVN', 2), (8, 'BADM112330', 'Cầu lông (*)', 1), (9, 'BASK112330', 'Bóng rổ (*)', 1), (10, 'CHES112330', 'Cờ vua (*)', 1),
(11, 'CHIN112330', 'Cờ tướng (*)', 1), (12, 'FOOT112330', 'Bóng đá (*)', 1), (13, 'GEFC220105', 'Kinh tế học đại cương', 2), (14, 'INLO220405', 'Nhập môn logic học', 2), (15, 'INMA220305', 'Nhập môn quản trị học', 2);
INSERT IGNORE INTO `course` (`id`, `course_code`, `fullname`, `credit`) VALUES 
(16, 'INSO321005', 'Nhập môn xã hội học', 2), (17, 'IQMA220205', 'Nhập môn quản trị chất lượng', 2), (18, 'IVNC320905', 'Cơ sở văn hóa Việt Nam', 2), (19, 'KARA112330', 'Không thủ đạo (*)', 1), (20, 'LESK120190', 'Kỹ năng học tập đại học', 2),
(31, 'MATH132401', 'Toán 1', 3), (32, 'MATH143001', 'Đại số tuyến tính và cấu trúc đại số', 4), (33, 'PHED110130', 'Giáo dục thể chất 1 (Điền kinh) (*)', 1), (34, 'PHYS130902', 'Vật lý 1', 3), (36, 'EEEN234162', 'Điện tử căn bản (CTT)', 3),
(37, 'GDQP110131', 'Giáo dục quốc phòng 1 (*)', 1), (38, 'GDQP110231', 'Giáo dục quốc phòng 2 (*)', 1), (39, 'LLCT130105', 'Triết học Mác - Lênin', 3), (40, 'MATH132501', 'Toán 2', 3), (41, 'MATH132901', 'Xác suất thống kê ứng dụng', 3),
(43, 'GDQP110331', 'Giáo dục quốc phòng 3 (*)', 1), (44, 'GDQP110431', 'Giáo dục quốc phòng 4 (*)', 1), (45, 'CAAL230180', 'Kiến trúc máy tính và hợp ngữ', 3), (47, 'IPPA233277', 'Lập Trình Python', 3), (48, 'NEES330380', 'Mạng máy tính căn bản', 3);
INSERT IGNORE INTO `course` (`id`, `course_code`, `fullname`, `credit`) VALUES 
(50, 'PHYS111202', 'Thí nghiệm Vật lý 1', 1), (51, 'PRBE214262', 'Thực tập điện tử căn bản', 1), (52, 'ENPS220591', 'Tâm lý học kỹ sư', 2), (53, 'WOPS120390', 'Kỹ năng làm việc trong môi trường kỹ thuật', 2), (54, 'ARIN330585', 'Trí tuệ nhân tạo', 3),
(56, 'ENCS330537', 'Tiếng Anh Chuyên ngành Khoa học Máy tính', 3), (57, 'INOT231780', 'Vạn Vật Kết Nối', 3), (58, 'INSE330380', 'An toàn thông tin', 3), (59, 'OPSY330280', 'Hệ điều hành', 3), (60, 'WIPR230579', 'Lập trình trên Windows', 3),
(61, 'DBMS330284', 'Hệ quản trị cơ sở dữ liệu', 3), (65, 'CLCO332779', 'Điện toán đám mây', 3), (66, 'DIPR430685', 'Xử lý ảnh số', 3), (67, 'ECOM430984', 'Thương mại điện tử', 3), (68, 'ESYS431080', 'Hệ thống nhúng', 3),
(69, 'FOIT331380', 'Lý thuyết thông tin', 3), (70, 'SPPR330885', 'Xử lý tiếng nói', 3), (71, 'MOPR331279', 'Lập trình di động', 3), (72, 'ITIN421085', 'Thực tập tốt nghiệp', 2), (73, 'SEMI310026', 'Chuyên đề doanh nghiệp', 1), (74, 'GRPR411979', 'Khoá luận tốt nghiệp', 11);

-- 8) SECTIONS & SCHEDULES
SET @thai_id = 11;
INSERT INTO `course_section` (`id`, `section_code`, `registration_batch_id`, `course_id`, `term_id`, `lecturer_id`, `room`, `day_of_week`, `start_slot`, `end_slot`, `total_weeks`, `first_study_date`, `max_capacity`, `current_capacity`) VALUES
(1, 'OOP-THAI-01', 1, 49, 1, @thai_id, 'E1-201', 2, 1, 3, 15, '2026-02-16', 50, 0),
(2, 'DASA-THAI-01', 1, 46, 1, @thai_id, 'E1-202', 3, 4, 6, 15, '2026-02-17', 50, 0),
(3, 'DBSY-THAI-01', 1, 55, 1, @thai_id, 'E1-203', 4, 7, 9, 15, '2026-02-18', 50, 0);

INSERT INTO `course_section` (`section_code`, `registration_batch_id`, `course_id`, `term_id`, `lecturer_id`, `room`, `day_of_week`, `start_slot`, `end_slot`, `total_weeks`, `first_study_date`, `max_capacity`, `current_capacity`)
SELECT CONCAT(c.course_code, '-', l.id), 1, c.id, 1, l.id, CONCAT('A1-', (l.id * 10 + c.id % 20)), (c.id % 6) + 2, IF(c.id % 2 = 0, 1, 7), IF(c.id % 2 = 0, 3, 9), 15, '2026-02-16', 60, 0
FROM `lecturer` l JOIN `course` c ON (l.id + c.id) % 10 = 0 WHERE l.id <= 10;

INSERT INTO `schedule` (`course_section_id`, `day_of_week`, `start_slot`, `end_slot`, `room`, `week_number`, `study_date`)
SELECT cs.id, cs.day_of_week, cs.start_slot, cs.end_slot, cs.room, seq.n, DATE_ADD(cs.first_study_date, INTERVAL (seq.n-1) WEEK)
FROM `course_section` cs CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15) AS seq;

-- 9) REGISTRATION & UPDATE
INSERT INTO `course_registration` (`student_id`, `section_id`, `status`, `reg_date`)
SELECT s.id, cs.id, 'APPROVED', '2026-01-20'
FROM `student` s JOIN `course_section` cs ON (s.id + cs.id) % 15 IN (1, 3, 5) LIMIT 1000;

INSERT INTO `mark` (`registration_id`, `attendance`, `process_score`, `test_score`, `final_score`, `grade_char`)
SELECT id, 'Đầy đủ', ROUND(RAND()*3+7, 1), ROUND(RAND()*5+5, 1), 0, 'B' FROM `course_registration`;
UPDATE `mark` SET final_score = ROUND(process_score * 0.4 + test_score * 0.6, 1), grade_char = CASE WHEN (process_score * 0.4 + test_score * 0.6) >= 8.5 THEN 'A' WHEN (process_score * 0.4 + test_score * 0.6) >= 7.0 THEN 'B' WHEN (process_score * 0.4 + test_score * 0.6) >= 5.5 THEN 'C' WHEN (process_score * 0.4 + test_score * 0.6) >= 4.0 THEN 'D' ELSE 'F' END;
UPDATE `course_section` cs SET current_capacity = (SELECT COUNT(*) FROM `course_registration` cr WHERE cr.section_id = cs.id);

-- 10) ANNOUNCEMENTS (Thông báo mẫu)
INSERT INTO `announcement` (`title`, `content`, `target_type`, `sender_name`, `created_at`) VALUES
('Thông báo nghỉ lễ 30/4 - 1/5', 'Toàn trường nghỉ lễ từ ngày 30/04 đến hết ngày 01/05/2026. Các lớp học bù sẽ được thông báo sau.', 'ALL', 'Phòng Đào tạo', '2026-04-20 08:30:00'),
('Nhắc nhở nộp học phí HK2', 'Hạn cuối nộp học phí học kỳ 2 là ngày 15/05/2026. Sinh viên vui lòng hoàn tất đúng hạn.', 'STUDENT', 'Phòng Kế hoạch Tài chính', '2026-04-15 10:00:00'),
('Họp Khoa CNTT đột xuất', 'Kính mời quý thầy cô Khoa CNTT họp vào lúc 14h00 chiều nay tại văn phòng Khoa.', 'LECTURER', 'Trưởng khoa CNTT', '2026-04-10 09:00:00'),
('Thông báo về việc cấp học bổng khuyến khích', 'Danh sách sinh viên nhận học bổng khuyến khích học tập HK1 đã được công bố trên website trường.', 'STUDENT', 'Phòng CTSV', '2026-04-12 14:00:00'),
('Triển khai nghiên cứu khoa học sinh viên', 'Khởi động cuộc thi NCKH sinh viên cấp trường năm 2026. Các nhóm đăng ký trước ngày 30/05.', 'ALL', 'Phòng Quản lý Khoa học', '2026-04-05 11:00:00');

-- Thông báo riêng cho các lớp của thầy Thái
INSERT INTO `announcement` (`title`, `content`, `target_type`, `course_section_id`, `sender_name`, `created_at`) VALUES
('Đổi phòng học lớp OOP-THAI-01', 'Lớp Lập trình hướng đối tượng sáng thứ 2 tuần sau sẽ chuyển từ phòng E1-201 sang Lab-05.', 'CLASS', 1, 'TS. Nguyễn Minh Thái', '2026-04-09 17:00:00'),
('Tài liệu tham khảo môn Cấu trúc dữ liệu', 'Thầy đã upload slide chương 4 và bài tập thực hành lên hệ thống. Các em tải về xem trước.', 'CLASS', 2, 'TS. Nguyễn Minh Thái', '2026-04-08 20:00:00'),
('Nhắc nhở nộp bài tập lớn Cơ sở dữ liệu', 'Hạn cuối nộp báo cáo giai đoạn 1 là Chủ nhật tuần này. Các nhóm nộp đúng hạn qua link driver.', 'CLASS', 3, 'TS. Nguyễn Minh Thái', '2026-04-07 15:30:00');

DROP TABLE IF EXISTS `vn_names_sample`;

