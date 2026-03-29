-- OnlineUTE - Full setup with massive realistic sample data
-- Target: MySQL 8+

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS online_ute;
CREATE DATABASE online_ute CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE online_ute;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================
-- 1) SCHEMA DDL
-- =========================================================

CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','STUDENT','LECTURER') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE lecturer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_lecturer_account
        FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_admin_account
        FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE study_program (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    program_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(255) NOT NULL,
    total_credit INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE term (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year_name VARCHAR(100) NOT NULL,
    term_name VARCHAR(100) NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

CREATE TABLE `class` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    birth_of_date DATE,
    email VARCHAR(150) NOT NULL UNIQUE,
    avatar_url VARCHAR(255),
    class_id BIGINT NOT NULL,
    study_program_id BIGINT NOT NULL,
    academic_term_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES `class`(id),
    CONSTRAINT fk_student_program FOREIGN KEY (study_program_id) REFERENCES study_program(id),
    CONSTRAINT fk_student_term FOREIGN KEY (academic_term_id) REFERENCES term(id),
    CONSTRAINT fk_student_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    credit INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE course_section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_code VARCHAR(50) NOT NULL,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    room VARCHAR(50) NOT NULL,
    CONSTRAINT uk_section_term UNIQUE (section_code, term_id),
    CONSTRAINT fk_section_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_section_term FOREIGN KEY (term_id) REFERENCES term(id),
    CONSTRAINT fk_section_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturer(id)
) ENGINE=InnoDB;

CREATE TABLE course_registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'APPROVED',
    reg_date DATE NOT NULL,
    UNIQUE KEY idx_student_section (student_id, section_id),
    CONSTRAINT fk_reg_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_reg_section FOREIGN KEY (section_id) REFERENCES course_section(id)
) ENGINE=InnoDB;

CREATE TABLE mark (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_id BIGINT NOT NULL UNIQUE,
    process_score DECIMAL(5,2),
    test_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    grade_char VARCHAR(5),
    attendance VARCHAR(30),
    CONSTRAINT fk_mark_registration FOREIGN KEY (registration_id) REFERENCES course_registration(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_section_id BIGINT NOT NULL,
    day_of_week INT NOT NULL,
    start_slot INT NOT NULL,
    end_slot INT NOT NULL,
    room VARCHAR(50) NOT NULL,
    week_number INT NOT NULL,
    CONSTRAINT fk_schedule_section FOREIGN KEY (course_section_id) REFERENCES course_section(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE exam_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id BIGINT NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    room_code VARCHAR(50) NOT NULL,
    seat_no INT NOT NULL,
    CONSTRAINT fk_exam_section FOREIGN KEY (section_id) REFERENCES course_section(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    course_section_id BIGINT NULL,
    sender_name VARCHAR(150) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


-- =========================================================
-- 2) MASTER DATA (Terms, Programs, Classes, Admins)
-- =========================================================

INSERT INTO term (year_name, term_name, is_current) VALUES
('2024-2025', 'HK1', FALSE),
('2024-2025', 'HK2', FALSE),
('2025-2026', 'HK1', TRUE);

SET @term_old = 1;
SET @term_prev = 2;
SET @term_cur = 3;

INSERT INTO study_program (program_code, fullname, total_credit) VALUES
('CNTT', 'Công nghệ thông tin', 145),
('KTPM', 'Kỹ thuật phần mềm', 145),
('HTTT', 'Hệ thống thông tin', 140),
('ATTT', 'An toàn thông tin', 145),
('DTVT', 'Điện tử viễn thông', 142),
('CKM', 'Cơ khí máy', 140),
('NNA', 'Ngôn ngữ Anh', 135),
('QTKD', 'Quản trị kinh doanh', 130),
('KTDN', 'Kế toán doanh nghiệp', 130),
('TCNH', 'Tài chính ngân hàng', 135);

INSERT INTO `class` (class_name) VALUES
('241101A'), ('241101B'), ('241101C'),
('241102A'), ('241102B'),
('241103A'), ('241103B'),
('231101A'), ('231102A'), ('231103A'),
('221101A'), ('221102A'), ('221103A');

-- Admin accounts
INSERT INTO account (username, password_hash, role) VALUES
('admin', 'admin123', 'ADMIN'),
('pdt01', 'pdt123', 'ADMIN');

INSERT INTO admin (code, fullname, account_id) VALUES
('AD001', 'Hệ thống Quản trị', 1),
('AD002', 'Phòng Đào tạo', 2);

-- =========================================================
-- 3) LECTURERS (20)
-- =========================================================
-- Generates 20 lecturers

INSERT INTO account (username, password_hash, role) VALUES
('gv001', '123456', 'LECTURER'), ('gv002', '123456', 'LECTURER'),
('gv003', '123456', 'LECTURER'), ('gv004', '123456', 'LECTURER'),
('gv005', '123456', 'LECTURER'), ('gv006', '123456', 'LECTURER'),
('gv007', '123456', 'LECTURER'), ('gv008', '123456', 'LECTURER'),
('gv009', '123456', 'LECTURER'), ('gv010', '123456', 'LECTURER'),
('gv011', '123456', 'LECTURER'), ('gv012', '123456', 'LECTURER'),
('gv013', '123456', 'LECTURER'), ('gv014', '123456', 'LECTURER'),
('gv015', '123456', 'LECTURER'), ('gv016', '123456', 'LECTURER'),
('gv017', '123456', 'LECTURER'), ('gv018', '123456', 'LECTURER'),
('gv019', '123456', 'LECTURER'), ('gv020', '123456', 'LECTURER');

INSERT INTO lecturer (code, fullname, account_id) VALUES
('GV001', 'Nguyễn Minh Đạo', 3), ('GV002', 'Võ Lê Phúc Hậu', 4),
('GV003', 'Trần Thanh Sơn', 5), ('GV004', 'Phạm Quang Huy', 6),
('GV005', 'Lê Anh Tuấn', 7), ('GV006', 'Đỗ Thị Thu Hà', 8),
('GV007', 'Nguyễn Thị Bích Ngọc', 9), ('GV008', 'Trần Văn Khoa', 10),
('GV009', 'Bùi Gia Bảo', 11), ('GV010', 'Phạm Minh Châu', 12),
('GV011', 'Đặng Quốc Đạt', 13), ('GV012', 'Ngô Thanh Long', 14),
('GV013', 'Hồ Vĩnh Hoàng', 15), ('GV014', 'Lý Phương Thảo', 16),
('GV015', 'Trịnh Hữu Thọ', 17), ('GV016', 'Đinh Công Trứ', 18),
('GV017', 'Lương Tấn Tài', 19), ('GV018', 'Mai Quỳnh Hương', 20),
('GV019', 'Vũ Anh Kiệt', 21), ('GV020', 'Cao Tiến Đạt', 22);

-- =========================================================
-- 4) COURSES (30)
-- =========================================================

INSERT INTO course (course_code, fullname, credit) VALUES
('MATH132401', 'Toán 1', 3),
('MATH132501', 'Toán 2', 3),
('PHYS130902', 'Vật lý 1', 3),
('PHYS131002', 'Vật lý 2', 3),
('LLCT130105', 'Triết học Mác - Lênin', 3),
('LLCT220405', 'Kinh tế chính trị Mác - Lênin', 2),
('ENG120100', 'Tiếng Anh 1', 2),
('ENG120200', 'Tiếng Anh 2', 2),
('ENG120300', 'Tiếng Anh 3', 2),
('GDTC110130', 'Giáo dục thể chất 1', 1),
('GDQP110131', 'Giáo dục quốc phòng 1', 1),
('PRTE230385', 'Kỹ thuật lập trình', 3),
('IT001', 'Nhập môn lập trình', 4),
('IT002', 'Lập trình hướng đối tượng', 3),
('IT003', 'Cấu trúc dữ liệu và giải thuật', 3),
('IT004', 'Cơ sở dữ liệu', 3),
('IT005', 'Hệ quản trị CSDL', 3),
('IT006', 'Phân tích thiết kế hệ thống', 3),
('IT007', 'Mạng máy tính', 3),
('IT008', 'Hệ điều hành', 3),
('IT009', 'Lập trình Web', 3),
('IT010', 'Lập trình Windows', 3),
('IT011', 'Lập trình thiết bị di động', 3),
('IT012', 'Trí tuệ nhân tạo', 3),
('IT013', 'Học máy', 3),
('IT014', 'Khai thác dữ liệu', 3),
('IT015', 'Điện toán đám mây', 3),
('IT016', 'Bảo mật thông tin', 3),
('IT017', 'Đồ án cơ sở', 2),
('IT018', 'Đồ án chuyên ngành', 3);

-- =========================================================
-- 5) GENERATE STUDENTS (200 Students via CTE)
-- =========================================================
-- Account IDs 23 -> 222
INSERT INTO account (username, password_hash, role)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 200)
SELECT CONCAT('24110', LPAD(n, 3, '0')), '123456', 'STUDENT' FROM seq;

INSERT INTO student (code, fullname, birth_of_date, email, avatar_url, class_id, study_program_id, academic_term_id, account_id)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 200)
SELECT
    CONCAT('24110', LPAD(n, 3, '0')),
    CONCAT(
        ELT(MOD(n, 7) + 1, 'Nguyễn ', 'Trần ', 'Lê ', 'Phạm ', 'Hoàng ', 'Huỳnh ', 'Võ '),
        ELT(MOD(n, 5) + 1, 'Viết ', 'Thị ', 'Xuân ', 'Minh ', 'Thanh '),
        ELT(MOD(n, 11) + 1, 'Hùng', 'Dũng', 'Lan', 'Hoa', 'Ngọc', 'Tâm', 'Bảo', 'Vy', 'Vinh', 'Luân', 'Trang')
    ),
    DATE_ADD('2004-01-01', INTERVAL MOD(n, 1000) DAY),
    CONCAT('24110', LPAD(n, 3, '0'), '@student.hcmute.edu.vn'),
    CONCAT('https://i.pravatar.cc/150?img=', MOD(n, 70)+1),
    MOD(n, 13) + 1,
    MOD(n, 10) + 1,
    @term_cur,
    n + 22
FROM seq;

-- =========================================================
-- 6) COURSE SECTIONS (60 Sections per term)
-- =========================================================

-- HK1 (Past)
INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 40)
SELECT CONCAT('SEC_HK1_', LPAD(n, 3, '0')), MOD(n, 30)+1, 1, MOD(n, 20)+1, CONCAT('A', MOD(n,5)+1, '-', (MOD(n,5) + 1) * 100 + MOD(n, 15) + 1)
FROM seq;

-- HK2 (Past)
INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 40)
SELECT CONCAT('SEC_HK2_', LPAD(n, 3, '0')), MOD(n, 30)+1, 2, MOD(n, 20)+1, CONCAT('B', MOD(n,3)+1, '-', (MOD(n,3) + 1) * 100 + MOD(n, 15) + 1)
FROM seq;

-- HK3 (Current)
INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 60)
SELECT CONCAT('SEC_CUR_', LPAD(n, 3, '0')), MOD(n, 30)+1, 3, MOD(n, 20)+1, CONCAT('A', MOD(n,5)+1, '-', (MOD(n,5) + 1) * 100 + MOD(n, 15) + 1)
FROM seq;

-- =========================================================
-- 7) COURSE REGISTRATIONS (Massive cross join)
-- =========================================================
-- Generate ~6 courses per student for past HK1 (1..60)
INSERT INTO course_registration (student_id, section_id, status, reg_date)
SELECT s.id, cs.id, 'APPROVED', '2024-08-15'
FROM student s
JOIN course_section cs ON cs.term_id = 1
WHERE MOD(s.id + cs.id, 9) = 0;

-- Generate ~6 courses per student for past HK2 (61..120)
INSERT INTO course_registration (student_id, section_id, status, reg_date)
SELECT s.id, cs.id, 'APPROVED', '2025-01-10'
FROM student s
JOIN course_section cs ON cs.term_id = 2
WHERE MOD(s.id + cs.id, 10) = 0;

-- Generate ~6 courses per student for CURRENT term (121..220)
-- Mix of Approved, Pending, Rejected
INSERT INTO course_registration (student_id, section_id, status, reg_date)
SELECT s.id, cs.id, 
    CASE 
        WHEN MOD(s.id + cs.id, 15) = 0 THEN 'PENDING'
        WHEN MOD(s.id + cs.id, 25) = 0 THEN 'REJECTED'
        ELSE 'APPROVED'
    END, 
    '2025-08-01'
FROM student s
JOIN course_section cs ON cs.term_id = 3
WHERE MOD(s.id + cs.id, 13) = 0;

-- =========================================================
-- 8) MARKS (Past courses get random realistic marks)
-- =========================================================
-- Fill marks for ONLY Approved Past Registrations
INSERT INTO mark (registration_id, process_score, test_score, final_score, grade_char, attendance)
SELECT 
    cr.id,
    ROUND(5 + MOD(cr.id, 51)/10, 2), -- 5.0 to 10.0
    ROUND(4 + MOD(cr.id * 7, 61)/10, 2), -- 4.0 to 10.0
    ROUND((5 + MOD(cr.id, 51)/10)*0.3 + (4 + MOD(cr.id * 7, 61)/10)*0.7, 2), -- 30% process, 70% test
    CASE 
        WHEN ROUND((5 + MOD(cr.id, 51)/10)*0.3 + (4 + MOD(cr.id * 7, 61)/10)*0.7, 2) >= 8.5 THEN 'A'
        WHEN ROUND((5 + MOD(cr.id, 51)/10)*0.3 + (4 + MOD(cr.id * 7, 61)/10)*0.7, 2) >= 7.0 THEN 'B'
        WHEN ROUND((5 + MOD(cr.id, 51)/10)*0.3 + (4 + MOD(cr.id * 7, 61)/10)*0.7, 2) >= 5.5 THEN 'C'
        WHEN ROUND((5 + MOD(cr.id, 51)/10)*0.3 + (4 + MOD(cr.id * 7, 61)/10)*0.7, 2) >= 4.0 THEN 'D'
        ELSE 'F'
    END,
    CONCAT(LPAD('', 12 + MOD(cr.id, 4), '1'), LPAD('', 3 - MOD(cr.id, 4), '0')) -- Like '111111111111100'
FROM course_registration cr
JOIN course_section cs ON cr.section_id = cs.id
WHERE cs.term_id < 3 AND cr.status = 'APPROVED';

-- Fill marks for CURRENT courses (Empty grades but existing attendance)
INSERT INTO mark (registration_id, process_score, test_score, final_score, grade_char, attendance)
SELECT 
    cr.id,
    NULL, NULL, NULL, NULL,
    '111000000000000' -- 3 weeks in
FROM course_registration cr
JOIN course_section cs ON cr.section_id = cs.id
WHERE cs.term_id = 3 AND cr.status = 'APPROVED';

-- =========================================================
-- 9) SCHEDULE
-- =========================================================
-- Generate schedules for CURRENT term sections ONLY
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number)
SELECT 
    id, 
    MOD(id, 6) + 2, -- Monday (2) to Saturday (7)
    CASE MOD(id, 3) WHEN 0 THEN 1 WHEN 1 THEN 4 ELSE 7 END, -- start slots 1, 4, 7
    CASE MOD(id, 3) WHEN 0 THEN 3 WHEN 1 THEN 6 ELSE 9 END, -- end slots 3, 6, 9
    room,
    1 -- just week 1 as example
FROM course_section 
WHERE term_id = 3;

-- =========================================================
-- 10) EXAM SCHEDULE
-- =========================================================
INSERT INTO exam_schedule (section_id, exam_date, start_time, room_code, seat_no)
SELECT 
    id, 
    DATE_ADD('2025-12-15', INTERVAL MOD(id, 14) DAY),
    '07:30:00',
    room,
    MOD(id, 40) + 1
FROM course_section
WHERE term_id = 3
LIMIT 50;

-- =========================================================
-- 11) ANNOUNCEMENTS
-- =========================================================
INSERT INTO announcement (title, content, target_type, course_section_id, sender_name, created_at) VALUES
('Thông báo kế hoạch nghỉ Tết Dương lịch 2026', 'Trường Đại học Sư phạm Kỹ thuật TP.HCM thông báo kế hoạch nghỉ Tết Dương lịch cho toàn thể CBVC và sinh viên nghỉ từ ngày 01/01/2026.', 'ALL', NULL, 'Phòng Đào Tạo', '2025-12-01 08:00:00'),
('Thông báo mở đăng ký học phần Học kỳ 1 năm học 2026-2027', 'Sinh viên các khóa lưu ý thời gian đăng ký học phần sẽ bắt đầu từ 8h00 ngày 25/08.', 'ALL_STUDENTS', NULL, 'Phòng Đào Tạo', '2025-12-05 09:30:00'),
('Nhắc nhở đóng học phí', 'Đề nghị toàn thể sinh viên hoàn thành nghĩa vụ học phí trước ngày 15/12 để tránh bị hủy học phần.', 'ALL_STUDENTS', NULL, 'Phòng Tài Chính', '2025-12-10 14:00:00'),
('Dời lịch học môn Nhập môn lập trình', 'Lớp học bị dời sang chiều thứ 5 tại phòng A3-104 do giảng viên đi công tác.', 'COURSE_SECTION', 121, 'GV. Nguyễn Minh Đạo', '2025-12-11 16:30:00'),
('Nộp tài liệu Đồ án Cơ sở', 'Các em lưu ý nộp bản báo cáo định dạng PDF lên hệ thống LMS trước thứ bảy tuần này.', 'COURSE_SECTION', 150, 'GV. Vũ Anh Kiệt', '2025-12-12 10:15:00');

-- =========================================================
-- 12) AUDIT & METRICS 
-- =========================================================
SELECT 'accounts' AS metric, COUNT(*) AS total FROM account
UNION ALL SELECT 'lecturers', COUNT(*) FROM lecturer
UNION ALL SELECT 'students', COUNT(*) FROM student
UNION ALL SELECT 'courses', COUNT(*) FROM course
UNION ALL SELECT 'course_sections', COUNT(*) FROM course_section
UNION ALL SELECT 'registrations', COUNT(*) FROM course_registration
UNION ALL SELECT 'marks', COUNT(*) FROM mark
UNION ALL SELECT 'schedules', COUNT(*) FROM schedule
UNION ALL SELECT 'announcements', COUNT(*) FROM announcement;
