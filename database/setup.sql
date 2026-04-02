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

CREATE TABLE faculty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    faculty_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE major (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    major_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(255) NOT NULL,
    total_credit INT NOT NULL,
    faculty_id BIGINT NOT NULL,
    CONSTRAINT fk_major_faculty
        FOREIGN KEY (faculty_id) REFERENCES faculty(id)
) ENGINE=InnoDB;

CREATE TABLE term (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year_name VARCHAR(100) NOT NULL,
    term_name VARCHAR(100) NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

CREATE TABLE registration_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    open_at DATETIME NOT NULL,
    close_at DATETIME NOT NULL,
    term_id BIGINT NOT NULL,
    common_start_date DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registration_batch_term FOREIGN KEY (term_id) REFERENCES term(id)
) ENGINE=InnoDB;

CREATE TABLE `class` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL UNIQUE,
    faculty_id BIGINT NOT NULL,
    CONSTRAINT fk_class_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id)
) ENGINE=InnoDB;

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    birth_of_date DATE,
    email VARCHAR(150) NOT NULL UNIQUE,
    avatar_url VARCHAR(255),
    class_id BIGINT NOT NULL,
    enrollment_year INT NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES `class`(id),
    CONSTRAINT fk_student_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE user_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL UNIQUE,
    profile_code VARCHAR(50),
    display_name VARCHAR(150),
    role_title VARCHAR(100),
    avatar_url VARCHAR(255),
    email VARCHAR(150),
    phone_number VARCHAR(20),
    birth_date DATE,
    gender VARCHAR(20),
    place_of_birth VARCHAR(150),
    nationality VARCHAR(100),
    ethnicity VARCHAR(100),
    religion VARCHAR(100),
    citizen_id_number VARCHAR(50),
    citizen_id_issue_place VARCHAR(150),
    citizen_id_issue_date DATE,
    current_address VARCHAR(500),
    permanent_address VARCHAR(500),
    faculty_name VARCHAR(150),
    class_name VARCHAR(100),
    major_name VARCHAR(255),
    academic_year VARCHAR(100),
    expected_graduation_year VARCHAR(100),
    contact_name VARCHAR(150),
    contact_phone VARCHAR(20),
    contact_address VARCHAR(500),
    father_name VARCHAR(150),
    father_phone VARCHAR(20),
    mother_name VARCHAR(150),
    mother_phone VARCHAR(20),
    CONSTRAINT fk_user_profile_account
        FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
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
    registration_batch_id BIGINT NULL,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    room VARCHAR(50) NOT NULL,
    max_capacity INT NOT NULL DEFAULT 70,
    current_capacity INT NOT NULL DEFAULT 0,
    day_of_week INT NOT NULL DEFAULT 2,
    start_slot INT NOT NULL DEFAULT 1,
    end_slot INT NOT NULL DEFAULT 3,
    total_weeks INT NOT NULL DEFAULT 15,
    first_study_date DATE NULL,
    last_study_date DATE NULL,
    CONSTRAINT uk_section_term UNIQUE (section_code, term_id),
    CONSTRAINT fk_section_batch FOREIGN KEY (registration_batch_id) REFERENCES registration_batch(id),
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
    study_date DATE NOT NULL,
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

INSERT INTO registration_batch (name, open_at, close_at, term_id, common_start_date) VALUES
('ÄÄƒng kÃ½ há»c pháº§n HK1 2025-2026', '2025-07-20 08:00:00', '2025-08-05 23:59:59', @term_cur, '2025-08-18');

INSERT INTO faculty (faculty_code, fullname) VALUES
('MFG', 'Khoa Co khi Che tao may'),
('CHEM', 'Khoa Cong nghe Hoa hoc va Thuc pham'),
('FASHION', 'Khoa Thoi trang va Du lich'),
('IT', 'Khoa Cong nghe Thong tin'),
('EEE', 'Khoa Dien - Dien tu'),
('ECON', 'Khoa Kinh te'),
('CIVIL', 'Khoa Xay dung'),
('APSCI', 'Khoa Khoa hoc Ung dung'),
('LANG', 'Khoa Ngoai ngu'),
('PRINT', 'Khoa In va Truyen thong'),
('PED', 'Khoa Su pham'),
('PEDTECH', 'Khoa Su pham Cong nghe'),
('AUTO', 'Khoa Co khi Dong luc');

SET @faculty_mfg = 1;
SET @faculty_chem = 2;
SET @faculty_fashion = 3;
SET @faculty_it = 4;
SET @faculty_eee = 5;
SET @faculty_econ = 6;
SET @faculty_civil = 7;
SET @faculty_apsci = 8;
SET @faculty_lang = 9;
SET @faculty_print = 10;
SET @faculty_ped = 11;
SET @faculty_pedtech = 12;
SET @faculty_auto = 13;

INSERT INTO major (major_code, fullname, total_credit, faculty_id) VALUES
('104', 'Ky thuat cong nghiep', 140, @faculty_mfg),
('109', 'Cong nghe may', 140, @faculty_chem),
('110', 'Cong nghe thong tin', 145, @faculty_it),
('116', 'Cong nghe thuc pham', 140, @faculty_chem),
('119', 'Cong nghe ky thuat may tinh', 145, @faculty_eee),
('123', 'Thiet ke thoi trang', 140, @faculty_fashion),
('124', 'Quan ly cong nghiep', 130, @faculty_econ),
('125', 'Ke toan', 130, @faculty_econ),
('126', 'Thuong mai dien tu', 130, @faculty_econ),
('127', 'Ky thuat xay dung cong trinh giao thong', 140, @faculty_civil),
('128', 'Cong nghe ky thuat hoa hoc', 140, @faculty_chem),
('129', 'Ky thuat y sinh', 140, @faculty_eee),
('130', 'Cong nghe vat lieu', 135, @faculty_apsci),
('131', 'Ngon ngu Anh', 135, @faculty_lang),
('132', 'Logistics va quan ly chuoi cung ung', 130, @faculty_econ),
('133', 'Ky thuat du lieu', 145, @faculty_it),
('134', 'Robot va tri tue nhan tao', 145, @faculty_mfg),
('135', 'He thong ky thuat cong trinh xay dung', 140, @faculty_civil),
('136', 'Kinh doanh quoc te', 130, @faculty_econ),
('138', 'Ky nghe go va noi that', 140, @faculty_mfg),
('139', 'He thong nhung va IoT', 145, @faculty_eee),
('140', 'Kien truc noi that', 140, @faculty_civil),
('142', 'Cong nghe ky thuat dien, dien tu', 145, @faculty_eee),
('143', 'Cong nghe che tao may', 140, @faculty_mfg),
('144', 'Cong nghe ky thuat co khi', 140, @faculty_mfg),
('145', 'Cong nghe ky thuat o to', 145, @faculty_auto),
('146', 'Cong nghe ky thuat co dien tu', 145, @faculty_mfg),
('147', 'Cong nghe ky thuat nhiet', 140, @faculty_auto),
('149', 'Cong nghe ky thuat cong trinh xay dung', 140, @faculty_civil),
('150', 'Cong nghe ky thuat moi truong', 140, @faculty_chem),
('151', 'Cong nghe ky thuat dieu khien va tu dong hoa', 145, @faculty_eee),
('154', 'Nang luong tai tao', 140, @faculty_auto),
('155', 'Quan ly xay dung', 130, @faculty_civil),
('156', 'Thiet ke do hoa', 135, @faculty_print),
('157', 'Kien truc', 140, @faculty_civil),
('158', 'Cong nghe ky thuat in', 135, @faculty_print),
('159', 'Quan tri nha hang va dich vu an uong', 130, @faculty_fashion),
('160', 'Quan ly va van hanh ha tang', 130, @faculty_civil),
('161', 'Cong nghe ky thuat dien tu - vien thong', 145, @faculty_eee),
('162', 'An toan thong tin', 145, @faculty_it),
('163', 'Luat', 130, @faculty_econ),
('164', 'Tam ly hoc giao duc', 130, @faculty_ped),
('165', 'Cong nghe truyen thong', 135, @faculty_print),
('166', 'Quan tri kinh doanh', 130, @faculty_econ),
('167', 'Cong nghe tai chinh', 130, @faculty_econ),
('168', 'Vat ly ky thuat', 135, @faculty_apsci),
('169', 'Moi truong va Phat trien ben vung', 135, @faculty_chem),
('950', 'Su pham tieng Anh', 135, @faculty_lang),
('951', 'Su pham Cong nghe', 135, @faculty_pedtech);

INSERT INTO class (class_name, faculty_id) VALUES
('241101A', @faculty_it), ('241101B', @faculty_it), ('241101C', @faculty_it),
('241102A', @faculty_eee), ('241102B', @faculty_eee),
('241103A', @faculty_econ), ('241103B', @faculty_econ),
('231101A', @faculty_mfg), ('231102A', @faculty_civil), ('231103A', @faculty_chem),
('221101A', @faculty_lang), ('221102A', @faculty_print), ('221103A', @faculty_auto);
-- Admin accounts
INSERT INTO account (password_hash, role) VALUES
('admin123', 'ADMIN'),
('pdt123', 'ADMIN');

INSERT INTO admin (code, fullname, account_id) VALUES
('AD001', 'Há»‡ thá»‘ng Quáº£n trá»‹', 1),
('AD002', 'PhÃ²ng ÄÃ o táº¡o', 2);

-- =========================================================
-- 3) LECTURERS (20)
-- =========================================================
-- Generates 20 lecturers

INSERT INTO account (password_hash, role) VALUES
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER'),
('123456', 'LECTURER'), ('123456', 'LECTURER');

INSERT INTO lecturer (code, fullname, account_id) VALUES
('GV001', 'Nguyá»…n Minh Äáº¡o', 3), ('GV002', 'VÃµ LÃª PhÃºc Háº­u', 4),
('GV003', 'Tráº§n Thanh SÆ¡n', 5), ('GV004', 'Pháº¡m Quang Huy', 6),
('GV005', 'LÃª Anh Tuáº¥n', 7), ('GV006', 'Äá»— Thá»‹ Thu HÃ ', 8),
('GV007', 'Nguyá»…n Thá»‹ BÃ­ch Ngá»c', 9), ('GV008', 'Tráº§n VÄƒn Khoa', 10),
('GV009', 'BÃ¹i Gia Báº£o', 11), ('GV010', 'Pháº¡m Minh ChÃ¢u', 12),
('GV011', 'Äáº·ng Quá»‘c Äáº¡t', 13), ('GV012', 'NgÃ´ Thanh Long', 14),
('GV013', 'Há»“ VÄ©nh HoÃ ng', 15), ('GV014', 'LÃ½ PhÆ°Æ¡ng Tháº£o', 16),
('GV015', 'Trá»‹nh Há»¯u Thá»', 17), ('GV016', 'Äinh CÃ´ng Trá»©', 18),
('GV017', 'LÆ°Æ¡ng Táº¥n TÃ i', 19), ('GV018', 'Mai Quá»³nh HÆ°Æ¡ng', 20),
('GV019', 'VÅ© Anh Kiá»‡t', 21), ('GV020', 'Cao Tiáº¿n Äáº¡t', 22);

-- =========================================================
-- 4) COURSES (30)
-- =========================================================

INSERT INTO course (course_code, fullname, credit) VALUES
('MATH132401', 'ToÃ¡n 1', 3),
('MATH132501', 'ToÃ¡n 2', 3),
('PHYS130902', 'Váº­t lÃ½ 1', 3),
('PHYS131002', 'Váº­t lÃ½ 2', 3),
('LLCT130105', 'Triáº¿t há»c MÃ¡c - LÃªnin', 3),
('LLCT220405', 'Kinh táº¿ chÃ­nh trá»‹ MÃ¡c - LÃªnin', 2),
('ENG120100', 'Tiáº¿ng Anh 1', 2),
('ENG120200', 'Tiáº¿ng Anh 2', 2),
('ENG120300', 'Tiáº¿ng Anh 3', 2),
('GDTC110130', 'GiÃ¡o dá»¥c thá»ƒ cháº¥t 1', 1),
('GDQP110131', 'GiÃ¡o dá»¥c quá»‘c phÃ²ng 1', 1),
('PRTE230385', 'Ká»¹ thuáº­t láº­p trÃ¬nh', 3),
('IT001', 'Nháº­p mÃ´n láº­p trÃ¬nh', 4),
('IT002', 'Láº­p trÃ¬nh hÆ°á»›ng Ä‘á»‘i tÆ°á»£ng', 3),
('IT003', 'Cáº¥u trÃºc dá»¯ liá»‡u vÃ  giáº£i thuáº­t', 3),
('IT004', 'CÆ¡ sá»Ÿ dá»¯ liá»‡u', 3),
('IT005', 'Há»‡ quáº£n trá»‹ CSDL', 3),
('IT006', 'PhÃ¢n tÃ­ch thiáº¿t káº¿ há»‡ thá»‘ng', 3),
('IT007', 'Máº¡ng mÃ¡y tÃ­nh', 3),
('IT008', 'Há»‡ Ä‘iá»u hÃ nh', 3),
('IT009', 'Láº­p trÃ¬nh Web', 3),
('IT010', 'Láº­p trÃ¬nh Windows', 3),
('IT011', 'Láº­p trÃ¬nh thiáº¿t bá»‹ di Ä‘á»™ng', 3),
('IT012', 'TrÃ­ tuá»‡ nhÃ¢n táº¡o', 3),
('IT013', 'Há»c mÃ¡y', 3),
('IT014', 'Khai thÃ¡c dá»¯ liá»‡u', 3),
('IT015', 'Äiá»‡n toÃ¡n Ä‘Ã¡m mÃ¢y', 3),
('IT016', 'Báº£o máº­t thÃ´ng tin', 3),
('IT017', 'Äá»“ Ã¡n cÆ¡ sá»Ÿ', 2),
('IT018', 'Äá»“ Ã¡n chuyÃªn ngÃ nh', 3);

-- =========================================================
-- 5) GENERATE STUDENTS (200 Students via CTE)
-- =========================================================
-- Account IDs 23 -> 222
INSERT INTO account (password_hash, role)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 200)
SELECT '123456', 'STUDENT' FROM seq;

INSERT INTO student (code, fullname, birth_of_date, email, avatar_url, class_id, enrollment_year, account_id)
WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 200)
SELECT
    CONCAT('24110', LPAD(n, 3, '0')),
    CONCAT(
        ELT(MOD(n, 7) + 1, 'Nguyá»…n ', 'Tráº§n ', 'LÃª ', 'Pháº¡m ', 'HoÃ ng ', 'Huá»³nh ', 'VÃµ '),
        ELT(MOD(n, 5) + 1, 'Viáº¿t ', 'Thá»‹ ', 'XuÃ¢n ', 'Minh ', 'Thanh '),
        ELT(MOD(n, 11) + 1, 'HÃ¹ng', 'DÅ©ng', 'Lan', 'Hoa', 'Ngá»c', 'TÃ¢m', 'Báº£o', 'Vy', 'Vinh', 'LuÃ¢n', 'Trang')
    ),
    DATE_ADD('2004-01-01', INTERVAL MOD(n, 1000) DAY),
    CONCAT('24110', LPAD(n, 3, '0'), '@student.hcmute.edu.vn'),
    CONCAT('https://i.pravatar.cc/150?img=', MOD(n, 70)+1),
    MOD(n, 13) + 1,
    2024,
    n + 22
FROM seq;

INSERT INTO user_profile (
    account_id, profile_code, display_name, role_title, avatar_url, email, phone_number,
    birth_date, gender, place_of_birth, nationality, ethnicity, religion, citizen_id_number,
    citizen_id_issue_place, citizen_id_issue_date, current_address, permanent_address,
    faculty_name, class_name, major_name, academic_year, expected_graduation_year,
    contact_name, contact_phone, contact_address, father_name, father_phone, mother_name, mother_phone
)
SELECT
    s.account_id,
    s.code,
    s.fullname,
    'Sinh viÃªn',
    s.avatar_url,
    s.email,
    CONCAT('09', LPAD(MOD(s.id * 37, 100000000), 8, '0')),
    s.birth_of_date,
    CASE WHEN MOD(s.id, 2) = 0 THEN 'Ná»¯' ELSE 'Nam' END,
    'TP. Há»“ ChÃ­ Minh',
    'Viá»‡t Nam',
    'Kinh',
    'KhÃ´ng',
    CONCAT('079', LPAD(s.id, 9, '0')),
    'CÃ´ng an TP. Há»“ ChÃ­ Minh',
    DATE_ADD('2022-01-01', INTERVAL MOD(s.id, 500) DAY),
    CONCAT('Sá»‘ ', MOD(s.id, 200) + 1, ', TP. Thá»§ Äá»©c, TP. Há»“ ChÃ­ Minh'),
    CONCAT('Sá»‘ ', MOD(s.id, 300) + 10, ', Quáº­n ', MOD(s.id, 12) + 1, ', TP. Há»“ ChÃ­ Minh'),
    f.fullname,
    c.class_name,
    NULL,
    '2024 - 2028',
    '2028',
    CONCAT('NgÆ°á»i liÃªn há»‡ ', s.fullname),
    CONCAT('08', LPAD(MOD(s.id * 41, 100000000), 8, '0')),
    CONCAT('Sá»‘ ', MOD(s.id, 90) + 1, ', Khu phá»‘ 1, TP. Thá»§ Äá»©c'),
    CONCAT('Cha ', s.fullname),
    CONCAT('07', LPAD(MOD(s.id * 43, 100000000), 8, '0')),
    CONCAT('Máº¹ ', s.fullname),
    CONCAT('07', LPAD(MOD(s.id * 47, 100000000), 8, '0'))
FROM student s
JOIN `class` c ON c.id = s.class_id
JOIN faculty f ON f.id = c.faculty_id;

INSERT INTO user_profile (
    account_id, profile_code, display_name, role_title, email, phone_number,
    nationality, faculty_name, academic_year, current_address
)
SELECT
    l.account_id,
    l.code,
    l.fullname,
    'Giáº£ng viÃªn',
    CONCAT(LOWER(l.code), '@hcmute.edu.vn'),
    CONCAT('09', LPAD(MOD(l.id * 53, 100000000), 8, '0')),
    'Viá»‡t Nam',
    'Khoa CÃ´ng nghá»‡ thÃ´ng tin',
    'CÃ´ng tÃ¡c hiá»‡n táº¡i',
    CONCAT('Sá»‘ ', MOD(l.id, 50) + 1, ', TP. Há»“ ChÃ­ Minh')
FROM lecturer l;

INSERT INTO user_profile (
    account_id, profile_code, display_name, role_title, email, phone_number,
    nationality, faculty_name, academic_year, current_address
)
SELECT
    a.account_id,
    a.code,
    a.fullname,
    'Quáº£n trá»‹ viÃªn',
    CONCAT(LOWER(a.code), '@hcmute.edu.vn'),
    CONCAT('09', LPAD(MOD(a.id * 59, 100000000), 8, '0')),
    'Viá»‡t Nam',
    'PhÃ²ng ÄÃ o táº¡o',
    'NhÃ¢n sá»± hiá»‡n táº¡i',
    'CÆ¡ sá»Ÿ 1 - TP. Thá»§ Äá»©c'
FROM admin a;

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
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number, study_date)
SELECT 
    id, 
    MOD(id, 6) + 2, -- Monday (2) to Saturday (7)
    CASE MOD(id, 3) WHEN 0 THEN 1 WHEN 1 THEN 4 ELSE 7 END, -- start slots 1, 4, 7
    CASE MOD(id, 3) WHEN 0 THEN 3 WHEN 1 THEN 6 ELSE 9 END, -- end slots 3, 6, 9
    room,
    1, -- just week 1 as example
    DATE_ADD('2025-08-18', INTERVAL MOD(id, 6) + 1 DAY)
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
('ThÃ´ng bÃ¡o káº¿ hoáº¡ch nghá»‰ Táº¿t DÆ°Æ¡ng lá»‹ch 2026', 'TrÆ°á»ng Äáº¡i há»c SÆ° pháº¡m Ká»¹ thuáº­t TP.HCM thÃ´ng bÃ¡o káº¿ hoáº¡ch nghá»‰ Táº¿t DÆ°Æ¡ng lá»‹ch cho toÃ n thá»ƒ CBVC vÃ  sinh viÃªn nghá»‰ tá»« ngÃ y 01/01/2026.', 'ALL', NULL, 'PhÃ²ng ÄÃ o Táº¡o', '2025-12-01 08:00:00'),
('ThÃ´ng bÃ¡o má»Ÿ Ä‘Äƒng kÃ½ há»c pháº§n Há»c ká»³ 1 nÄƒm há»c 2026-2027', 'Sinh viÃªn cÃ¡c khÃ³a lÆ°u Ã½ thá»i gian Ä‘Äƒng kÃ½ há»c pháº§n sáº½ báº¯t Ä‘áº§u tá»« 8h00 ngÃ y 25/08.', 'ALL_STUDENTS', NULL, 'PhÃ²ng ÄÃ o Táº¡o', '2025-12-05 09:30:00'),
('Nháº¯c nhá»Ÿ Ä‘Ã³ng há»c phÃ­', 'Äá» nghá»‹ toÃ n thá»ƒ sinh viÃªn hoÃ n thÃ nh nghÄ©a vá»¥ há»c phÃ­ trÆ°á»›c ngÃ y 15/12 Ä‘á»ƒ trÃ¡nh bá»‹ há»§y há»c pháº§n.', 'ALL_STUDENTS', NULL, 'PhÃ²ng TÃ i ChÃ­nh', '2025-12-10 14:00:00'),
('Dá»i lá»‹ch há»c mÃ´n Nháº­p mÃ´n láº­p trÃ¬nh', 'Lá»›p há»c bá»‹ dá»i sang chiá»u thá»© 5 táº¡i phÃ²ng A3-104 do giáº£ng viÃªn Ä‘i cÃ´ng tÃ¡c.', 'COURSE_SECTION', 121, 'GV. Nguyá»…n Minh Äáº¡o', '2025-12-11 16:30:00'),
('Ná»™p tÃ i liá»‡u Äá»“ Ã¡n CÆ¡ sá»Ÿ', 'CÃ¡c em lÆ°u Ã½ ná»™p báº£n bÃ¡o cÃ¡o Ä‘á»‹nh dáº¡ng PDF lÃªn há»‡ thá»‘ng LMS trÆ°á»›c thá»© báº£y tuáº§n nÃ y.', 'COURSE_SECTION', 150, 'GV. VÅ© Anh Kiá»‡t', '2025-12-12 10:15:00');

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
