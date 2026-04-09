SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE online_ute;

-- 1) SCHEMA DDL
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARBINARY(255) NOT NULL,
    role ENUM('ADMIN','STUDENT','LECTURER') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE lecturer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_lecturer_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    fullname VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_admin_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
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
    CONSTRAINT fk_major_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id)
) ENGINE=InnoDB;

CREATE TABLE `term` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `year_name` VARCHAR(100) NOT NULL,
    `term_name` VARCHAR(100) NOT NULL,
    `is_current` BOOLEAN NOT NULL DEFAULT FALSE
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
    major_id BIGINT NOT NULL,
    CONSTRAINT fk_class_major FOREIGN KEY (major_id) REFERENCES major(id)
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
    CONSTRAINT fk_user_profile_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
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

SET FOREIGN_KEY_CHECKS = 1;
