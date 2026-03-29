-- 1. Create Database
DROP DATABASE IF EXISTS online_ute;
CREATE DATABASE online_ute CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE online_ute;

-- 2. Create Account Table
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- 3. Create Lecturer Table
CREATE TABLE lecturer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_lecturer_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    INDEX idx_lecturer_fullname (fullname)
) ENGINE=InnoDB;

-- 4. Create Admin Table
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_admin_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Create Study Program Table
CREATE TABLE study_program (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    program_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(255) NOT NULL,
    total_credit INT NOT NULL
) ENGINE=InnoDB;

-- 6. Create Term Table
CREATE TABLE term (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year_name VARCHAR(100) NOT NULL,
    term_name VARCHAR(100) NOT NULL,
    is_current BOOLEAN DEFAULT FALSE NOT NULL
) ENGINE=InnoDB;

-- 7. Create Class Table
CREATE TABLE class (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- 8. Create Student Table
CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    birth_of_date DATE,
    email VARCHAR(150) NOT NULL,
    avatar_url VARCHAR(255),
    class_id BIGINT NOT NULL,
    study_program_id BIGINT NOT NULL,
    academic_term_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES class(id),
    CONSTRAINT fk_student_program FOREIGN KEY (study_program_id) REFERENCES study_program(id),
    CONSTRAINT fk_student_term FOREIGN KEY (academic_term_id) REFERENCES term(id),
    CONSTRAINT fk_student_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    INDEX idx_student_fullname (fullname)
) ENGINE=InnoDB;

-- 9. Create Course Table
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    credit INT NOT NULL
) ENGINE=InnoDB;

-- 10. Create Course Section Table
CREATE TABLE course_section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_code VARCHAR(50) NOT NULL,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    room VARCHAR(50),
    CONSTRAINT fk_section_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_section_term FOREIGN KEY (term_id) REFERENCES term(id),
    CONSTRAINT fk_section_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturer(id),
    INDEX idx_section_code (section_code)
) ENGINE=InnoDB;

-- 11. Create Course Registration Table
CREATE TABLE course_registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    reg_date DATE NOT NULL,
    UNIQUE KEY idx_student_section (student_id, section_id),
    CONSTRAINT fk_reg_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_reg_section FOREIGN KEY (section_id) REFERENCES course_section(id)
) ENGINE=InnoDB;

-- 12. Create Mark Table
CREATE TABLE mark (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_id BIGINT NOT NULL UNIQUE,
    process_score DECIMAL(5,2),
    test_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    grade_char VARCHAR(5),
    CONSTRAINT fk_mark_registration FOREIGN KEY (registration_id) REFERENCES course_registration(id)
) ENGINE=InnoDB;

-- 13. Create Schedule Table
CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_section_id BIGINT NOT NULL,
    day_of_week INT NOT NULL,
    start_slot INT NOT NULL,
    end_slot INT NOT NULL,
    room VARCHAR(50) NOT NULL,
    week_number INT NOT NULL,
    CONSTRAINT fk_schedule_section FOREIGN KEY (course_section_id) REFERENCES course_section(id)
) ENGINE=InnoDB;

-- 14. Create Exam Schedule Table
CREATE TABLE exam_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id BIGINT NOT NULL,
    exam_date DATE NOT NULL,
    start_time TIME NOT NULL,
    room_code VARCHAR(50) NOT NULL,
    seat_no INT NOT NULL,
    CONSTRAINT fk_exam_section FOREIGN KEY (section_id) REFERENCES course_section(id)
) ENGINE=InnoDB;

-- ==========================================
-- 15. SAMPLE DATA INJECTION
-- ==========================================

-- 15.1 Term
INSERT INTO term (year_name, term_name, is_current) VALUES ('2024-2025', 'HK2', TRUE);
SET @current_term_id = LAST_INSERT_ID();

-- 15.2 Lecturer
INSERT INTO account (username, password_hash, role) VALUES ('phuchau', '123', 'LECTURER');
SET @gv01_acc = LAST_INSERT_ID();
INSERT INTO lecturer (code, fullname, account_id) VALUES ('GV01', 'Võ Lê Phúc Hậu', @gv01_acc);
SET @gv01_id = LAST_INSERT_ID();

INSERT INTO account (username, password_hash, role) VALUES ('minhdao', '123', 'LECTURER');
SET @gv02_acc = LAST_INSERT_ID();
INSERT INTO lecturer (code, fullname, account_id) VALUES ('GV02', 'Nguyễn Minh Đạo', @gv02_acc);
SET @gv02_id = LAST_INSERT_ID();

INSERT INTO account (username, password_hash, role) VALUES ('thanhson', '123', 'LECTURER');
SET @gv03_acc = LAST_INSERT_ID();
INSERT INTO lecturer (code, fullname, account_id) VALUES ('GV03', 'Nguyễn Thành Sơn', @gv03_acc);
SET @gv03_id = LAST_INSERT_ID();

-- 15.3 Admin
INSERT INTO account (username, password_hash, role) VALUES ('admin', 'admin', 'ADMIN');
SET @admin_acc = LAST_INSERT_ID();
INSERT INTO admin (code, fullname, account_id) VALUES ('AD01', 'Hệ thống Quản trị', @admin_acc);

-- 15.3.5 Study Program
INSERT INTO study_program (program_code, fullname, total_credit) VALUES ('CNTT', 'Công nghệ thông tin', 150);
SET @stu_prog_id = LAST_INSERT_ID();

-- 15.4 Courses
INSERT INTO course (course_code, fullname, credit) VALUES ('OPSY330280', 'Hệ điều hành', 3);
SET @c_os = LAST_INSERT_ID();
INSERT INTO course (course_code, fullname, credit) VALUES ('WIPR230579', 'Lập trình trên Windows', 3);
SET @c_win = LAST_INSERT_ID();
INSERT INTO course (course_code, fullname, credit) VALUES ('DBSY240184', 'Cơ sở dữ liệu', 3);
SET @c_db = LAST_INSERT_ID();
INSERT INTO course (course_code, fullname, credit) VALUES ('MATH143001', 'Đại số tuyến tính', 3);
SET @c_math = LAST_INSERT_ID();

-- 15.5 Course Sections (Classes)
INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room) 
VALUES ('OPSY_L01', @c_os, @current_term_id, @gv01_id, 'A4-503');
SET @cs_os = LAST_INSERT_ID();

INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room) 
VALUES ('WIPR_L02', @c_win, @current_term_id, @gv02_id, 'A301');
SET @cs_win = LAST_INSERT_ID();

INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room) 
VALUES ('DBSY_L03', @c_db, @current_term_id, @gv03_id, 'A122');
SET @cs_db = LAST_INSERT_ID();

-- 15.6 Student
INSERT INTO account (username, password_hash, role) VALUES ('24110166', '123', 'STUDENT');
SET @stu_acc = LAST_INSERT_ID();
INSERT INTO class (class_name) VALUES ('CNTT-K24');
SET @stu_class = LAST_INSERT_ID();
INSERT INTO student (code, fullname, email, class_id, study_program_id, academic_term_id, account_id) 
VALUES ('24110166', 'Ngô Anh Bằng', 'ngoa@student.hcmute.edu.vn', @stu_class, @stu_prog_id, @current_term_id, @stu_acc);
SET @stu_id = LAST_INSERT_ID();

-- 15.7 Course Registrations (Assigning student to classes)
INSERT INTO course_registration (student_id, section_id, status, reg_date) VALUES (@stu_id, @cs_os, 'APPROVED', '2026-03-20');
INSERT INTO course_registration (student_id, section_id, status, reg_date) VALUES (@stu_id, @cs_win, 'APPROVED', '2026-03-20');
INSERT INTO course_registration (student_id, section_id, status, reg_date) VALUES (@stu_id, @cs_db, 'APPROVED', '2026-03-20');

-- 15.8 Schedule (Visible on the UI)
-- Mon, Sáng: OS
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number) 
VALUES (@cs_os, 1, 1, 4, 'A4-503', 1);

-- Tue, Sáng: Windows
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number) 
VALUES (@cs_win, 2, 1, 4, 'A301', 1);

-- Wed, Sáng: OS
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number) 
VALUES (@cs_os, 3, 1, 4, 'A307', 1);

-- Wed, Chiều: Database
INSERT INTO schedule (course_section_id, day_of_week, start_slot, end_slot, room, week_number) 
VALUES (@cs_db, 3, 7, 10, 'A122', 1);
