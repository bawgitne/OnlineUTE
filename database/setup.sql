-- OnlineUTE - Full setup with massive realistic sample data
-- Target: MySQL 8+

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS online_ute;
CREATE DATABASE online_ute CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE online_ute;

SET FOREIGN_KEY_CHECKS = 1;

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

-- 2) MASTER DATA
INSERT INTO term (year_name, term_name, is_current) VALUES
('2024-2025', 'HK1', FALSE),
('2024-2025', 'HK2', FALSE),
('2025-2026', 'HK1', TRUE);

SET @term_cur = 3;

INSERT INTO registration_batch (name, open_at, close_at, term_id, common_start_date) VALUES
('Đăng ký học phần HK1 2025-2026', '2025-07-20 08:00:00', '2025-08-05 23:59:59', @term_cur, '2025-08-18');

INSERT INTO faculty (faculty_code, fullname) VALUES
('MFG', 'Khoa Cơ khí Chế tạo máy'),
('CHEM', 'Khoa Công nghệ Hóa học và Thực phẩm'),
('FASHION', 'Khoa Thời trang và Du lịch'),
('IT', 'Khoa Công nghệ Thông tin'),
('EEE', 'Khoa Điện - Điện tử'),
('ECON', 'Khoa Kinh tế'),
('CIVIL', 'Khoa Xây dựng'),
('APSCI', 'Khoa Khoa học Ứng dụng'),
('LANG', 'Khoa Ngoại ngữ'),
('PRINT', 'Khoa In và Truyền thông'),
('PED', 'Khoa Sư phạm'),
('PEDTECH', 'Khoa Sư phạm Công nghệ'),
('AUTO', 'Khoa Cơ khí Động lực');

INSERT INTO major (major_code, fullname, total_credit, faculty_id) VALUES
('110', 'Công nghệ thông tin', 145, 4),
('133', 'Kỹ thuật dữ liệu', 145, 4),
('162', 'An toàn thông tin', 145, 4),
('142', 'Công nghệ kỹ thuật điện, điện tử', 145, 5),
('151', 'Công nghệ kỹ thuật điều khiển và tự động hóa', 145, 5),
('145', 'Công nghệ kỹ thuật ô tô', 145, 13);

INSERT INTO class (class_name, faculty_id) VALUES
('241101A', 4), ('241101B', 4), ('241101C', 4),
('241102A', 5), ('241102B', 5),
('241103A', 6), ('241103B', 6);

-- 3) ACCOUNTS
INSERT INTO account (username, password_hash, salt, role) VALUES
('AD001', 'BH4wEHOGUXWnr9CeyNtuPArhbJ61DOmpWzZirw3WzM4=', FROM_BASE64('b0BCNeKjIPAmpx+BKf2Cuw=='), 'ADMIN'),
('AD002', 'r3kjwtbqLOkUzkvELjuwookttAKXuJuiPcxoZMsD0iM=', FROM_BASE64('TpWL9j5djorvmFql0WRqnw=='), 'ADMIN'),
('GV001', 'yzl/CfBoRcAM4zJqbBs8toUbBL9BD3XKTtTepjMt1Nk=', FROM_BASE64('lnR7/yv1raqSqjZgkWqKCA=='), 'LECTURER'),
('GV002', 'aQkIy+PN1HD0suPcRDAj6GxGjBN0G9x5GWcQZAe51t8=', FROM_BASE64('QNQcd2im0WGNOfuebcHvug=='), 'LECTURER'),
('GV003', 'iq/Hm0z/S8FL1erw2/qnrG/UZ7lEE0YP6QulFoDkY1s=', FROM_BASE64('JtLhd/ub0r8uaHtfLnm0mA=='), 'LECTURER'),
('GV004', 'srnVENILmMdBfw6/FMdycabf9Ww+CT6X+INTpPDaKDw=', FROM_BASE64('uv4zeBGoZnruVoqs0EBHiw=='), 'LECTURER'),
('GV005', '+YBhLLX4+lvlEVnxjwpFa9HffOVrarvn7an1f67DPQA=', FROM_BASE64('ktVdxGSIZOc7X+pdVqlX7g=='), 'LECTURER'),
('GV006', 'RMEeRzrXl9iXheUQkAP3YPNFKPC7TR1LMQPK7quhaE0=', FROM_BASE64('G0PrUrj0APpEF7uJtZlI2w=='), 'LECTURER'),
('GV007', 'wVlpxC0lFjvzA8U5+Gs3EtofoTP1v+1Z8DeNV6OHrJA=', FROM_BASE64('t0vW6zrbKugiLd+2Fr41xQ=='), 'LECTURER'),
('GV008', 'NB2uVgcqZhiZrm1GozmyqzHE1L/Y44z+l8XACAtNKnk=', FROM_BASE64('kvB7EIsEzNak1rnYTHbHLQ=='), 'LECTURER'),
('GV009', 'GGbcykP/k+A/OVQqj93S1IxcuaJu70hv4dxd/HQi26M=', FROM_BASE64('3XhlutdwP08JorP5wrQ8LQ=='), 'LECTURER'),
('GV010', 'R/HcpTKbVR2Q5B5Lcv3Yu98/4gTUuCsIJLudv3/aiLw=', FROM_BASE64('fhb4sVVQCzPHT+6EjtI6Aw=='), 'LECTURER'),
('GV011', 'D8q6sLVaBWgv2cB9rf7svUxQgIm7AzaALwLUyzuXVZc=', FROM_BASE64('Wc6lR2B1mQVafW9zVIKKTA=='), 'LECTURER'),
('GV012', 'QYRIU+NLmYe4GsyGRbkTNYby+mtees7GMqXRkShMvoQ=', FROM_BASE64('CF1+IjDDQ5bgTnTGLAVifA=='), 'LECTURER'),
('GV013', 'HxCYau67b1QM+Tgy4/xnXEbhNYwSqSZc+aMcrBS237M=', FROM_BASE64('nqDlLtjeF2MVOoG9hsH0bQ=='), 'LECTURER'),
('GV014', '8XVZJf+tCfCnTsZrGB00DO7rqfixUkrhKbVCn7uVtDA=', FROM_BASE64('KK/VLH1ruN354xZejJw1kw=='), 'LECTURER'),
('GV015', '0sExf4AzgLtVtVmnTEcS/9VxRIpyT+dZh9Si54SKko0=', FROM_BASE64('9lf/t7lXi5Or0/xIgQZYiw=='), 'LECTURER'),
('GV016', 'Vh+dQcsflpCakUp9WbxF3ExapxU6Cdjl0mOIOJOCi2E=', FROM_BASE64('OCGHeOJNtTztvP1wEbuyrA=='), 'LECTURER'),
('GV017', 'AbNUErJa4Mvc1/GLT3zp/O7uIvg5VdMgxr/RQfoPlm4=', FROM_BASE64('ttl9DNY7zvsmj2i4BWPPiA=='), 'LECTURER'),
('GV018', 'fbNN4kBcJn0oF2x8P6UmyS2vh2dMc857BqOVFppPCdM=', FROM_BASE64('WPzeNMubgKTrHcrmGYXi+A=='), 'LECTURER'),
('GV019', '3quL6wVitMA6DT3rMl8iiGU90m7/2gI5vqLIB6l4Ltw=', FROM_BASE64('zUa7PQQVsQz76MGor6nNoQ=='), 'LECTURER'),
('GV020', 'KCnK0P3xXYdiA9K+DFw4jvs5dn64rN46NtVUCj+cnk8=', FROM_BASE64('YVomoXg9NMBFOkOSnL2/ug=='), 'LECTURER'),
('24110001', 'zqOqSriwYw/scnXWCELTyb47MT6qm1TUHrmsEnFDXMY=', FROM_BASE64('+CpgoxIXbXJWv0n4djokEA=='), 'STUDENT'),
('24110002', 'X8UU0AmksTEgiBvhOpSascM3qYLe5I8F+vnPDL2UjLo=', FROM_BASE64('yOU3jVb5x4UoglH35pDT+A=='), 'STUDENT'),
('24110003', 'PIA+OZPdI64GN1wXbq2o8B6VIVIjNv2wVRekoKvpUvo=', FROM_BASE64('TbXgS3ykL1fKEol2EEVrIw=='), 'STUDENT'),
('24110004', '34zB8+apBcxN/ulrmK781wnagiWllZ0oOlkOe6lBP64=', FROM_BASE64('Rb+ai7FocL2nlCDk8qYRTg=='), 'STUDENT'),
('24110005', 'uxw5uLMGvLxbbS6E7vXUVAaXAzOk3Z1Xsyr49dxCUDc=', FROM_BASE64('Rrp1+I/vq0VWztt3DgrRyw=='), 'STUDENT'),
('24110006', 'zW8gSj5aEr9NIXbJwkaNSqy51Wu6sPP6MNWGc/qzwRI=', FROM_BASE64('b1u2imD6AJ7uIfTTSsJxBQ=='), 'STUDENT'),
('24110007', 'R/cKlSmFRul+1229tTxV3l4ZeJLik9TxYnr0lBNljhM=', FROM_BASE64('EjcHXjeDVYtZAM5c5vSoXQ=='), 'STUDENT'),
('24110008', 'dmAXuSdRQgtV+DJJup+X/b/Xal6TK6Sd6o3k3cVKEIU=', FROM_BASE64('d8D66XJtKCXAHqRqxsYFtw=='), 'STUDENT'),
('24110009', 'TksxBCViSXSmhlCgMuMP/ffd+VUtpQqZzycwYtjkRls=', FROM_BASE64('ClnpOf5okcx5s0Dh5HNASQ=='), 'STUDENT'),
('24110010', 'SnLDLCdk/48hP47deXhTG9J2QsexzTbf7LuT42jEc/c=', FROM_BASE64('66QD3HPyoRW98FKny/QGWQ=='), 'STUDENT'),
('24110011', '1mZ7q1n+97mneb/bVb7xlfW9QwiaP9+YVA1ErE6wBxI=', FROM_BASE64('pmNJBb6R5uIHoxwvwVJdWg=='), 'STUDENT'),
('24110012', 'G7QkiGR1A9ce/d4G1rWqZXAhzHTgD3BhHvMQh6gbB2g=', FROM_BASE64('/Cp+q5AjwSbOtSBzHyDZ/Q=='), 'STUDENT'),
('24110013', 'T0v2l/OUK5vSOe9xcCxl263ZZl4pfcah+Sv2dAz0o7k=', FROM_BASE64('JdcKmqtHpPdxx68yhnKvgA=='), 'STUDENT'),
('24110014', '+2NSFrgMLiFBtSrNhjIDkkJnPeaiwvLeREhs1MXymOU=', FROM_BASE64('I2KAzT48210k62y3PoQItw=='), 'STUDENT'),
('24110015', 'hgvO4dt1wB8/mnvfuNTg0ITR4Kzyx6s2bGFqNCVt2Yg=', FROM_BASE64('l+bdLZ/W3UOwqIBPf/eCbQ=='), 'STUDENT'),
('24110016', '9oGht6urN2c6bNh6Yrbm1UIL+TiCy3RejFtvyiyJVuE=', FROM_BASE64('k5CngQSt8spLg+N6kSjohg=='), 'STUDENT'),
('24110017', 'gdkXf7Nl9wx9fivkoa7KK0zWKTUykaXP4+q1rNgN0Ro=', FROM_BASE64('Cy1dPILwnlM1uEpjUWacDA=='), 'STUDENT'),
('24110018', 'ghRg67u1GF5X/DnL4O60QY+zC0d9hDEd85/F8L6cR1I=', FROM_BASE64('YoxZTl7SoFkVCVDoN4U+9w=='), 'STUDENT'),
('24110019', 'dLZPzQla5ohOyQUlyRjjo3amSCoq51kPVmDPwDJpx+w=', FROM_BASE64('BZuO4DgkaOiN/3zh/mDvgg=='), 'STUDENT'),
('24110020', 'kkEv2IhEH2C5PwRiUla30olwrn5SCdU1Q/mBU5/zLcM=', FROM_BASE64('dU9QeuF8XBQkNOA40cEBbw=='), 'STUDENT'),
('24110021', 'S18qJMXOvvDiAYJvdJkTs9FaFSqnx3XGQEy20igE5Os=', FROM_BASE64('x8qwy5geVEqF+I/s6wyIZA=='), 'STUDENT'),
('24110022', '3lg5rCL6kvARUir5aTkJdZFsIF/rQ98sXWKzh3jxXVk=', FROM_BASE64('eB46QGt8o+BqGr1uC6550Q=='), 'STUDENT'),
('24110023', '2/joPY/cQn/DJdUBrD21SD4RW7QvF7vY1dtA0mnSaOs=', FROM_BASE64('6R9mLEE2e3zABpoGCR7T7Q=='), 'STUDENT'),
('24110024', 'G33MORhfVnEjlCAbhNn47MMCBwpepqqDLw2o8MnUg88=', FROM_BASE64('P4lfT01A0Lh99SEYNkpZWQ=='), 'STUDENT'),
('24110025', 'eZtLpN44wmRRk5KzFUw6NiBX/cnyS+di7+TJHAsAiT4=', FROM_BASE64('fARmoVgp07pPEKIKadwrUg=='), 'STUDENT'),
('24110026', 'Y8D3jfmdaWZSviD4Gv8Ap6LuzRs/h0MXs932M5QYeZY=', FROM_BASE64('ui8hUhbY0LpeZUGMQSRFSQ=='), 'STUDENT'),
('24110027', 'SnIHNSo506sr0VTfZALMicl9yooOSTvg5rfZG6PNhU4=', FROM_BASE64('Q3VcqJK3904s6WosnVMuwQ=='), 'STUDENT'),
('24110028', 'QMUk/tWGzBWErHvvKnfscOIC86PnEedRqWN1ang+2Tk=', FROM_BASE64('+7pel4vc66qOSgo8RM9TDQ=='), 'STUDENT'),
('24110029', 'vPVU8CBinCTsYQcHcuHIV26nPlp+CAdxH/qG5JlbsaQ=', FROM_BASE64('w2D41GG0zqZrcBcQYT5qMQ=='), 'STUDENT'),
('24110030', 'RVGDch7zdtn7tTMXIjAYc+Fl2o18Sz3UEgLT7eSaTWs=', FROM_BASE64('YfTId99pPFvauSmmMUaICw=='), 'STUDENT'),
('24110031', 'HNe8OX7Mwm1ux410X7xhbKzUR9pKltKsKx79Q5IMTy8=', FROM_BASE64('iGgef5wrzKcGVu4WMDrpDw=='), 'STUDENT'),
('24110032', 'SPlayVmLPp4biDyz3ez7t+S969+TG9xii1Ku0wHXEfY=', FROM_BASE64('qH8zkAUM+NBINTOS5f+vdA=='), 'STUDENT'),
('24110033', '6X9rqVtS59IRWNiPuurGP2F+SqnnN2wMGM0eLIz8knw=', FROM_BASE64('b9gvVFUTrbjB875LxeVs+Q=='), 'STUDENT'),
('24110034', 'm7M6tWPYK9qoFgh27IakAgEKaxtNo6hiLqgTjfQoW3Q=', FROM_BASE64('yOs3Sx07R7bsX1d3x0qvjg=='), 'STUDENT'),
('24110035', 'D6ONmv2cp/UkRR2NA37VqUoV+Qf1XMcvqL+mlXFvrFM=', FROM_BASE64('6ZKCNrmPuuaWDlNOUZzAlQ=='), 'STUDENT'),
('24110036', 'briw/HSGYPu54ZA/d9KRDPgF0nojkZVIspsQK/p8KM0=', FROM_BASE64('vVxAMWDBjF+Ei3TlnzYJfg=='), 'STUDENT'),
('24110037', 'PfY6897tWpVQuuBQ6BSc6YFgULJ5PY/s6sxeedFGFPE=', FROM_BASE64('okY7Ly5Ti11Wi8Ur6oQRBQ=='), 'STUDENT'),
('24110038', 'HZvBP8FPt4bDPXGFWfOFGgHKg1NFgRQ+z3VDIzqk734=', FROM_BASE64('Pw7QcvEcTCwt3ATn/ekwog=='), 'STUDENT'),
('24110039', 'p9BLzGo3vY+NAJaCQy8ELdR//shM3r/WAeEg8mlTAfY=', FROM_BASE64('8apCkKUEUY0Dcqw0YfXI9A=='), 'STUDENT'),
('24110040', 'GETY8sHir1BQy15lcYliJwlNnUMjmT9Q7fbZ09pg9g8=', FROM_BASE64('vYvjbvzkednFFlMTj+8+2A=='), 'STUDENT'),
('24110041', 'iH24Rw3qRfp60FGI6CezKfl/B2iDdjb67kiZUWqfeLE=', FROM_BASE64('4UPfd6OnrGZhPR7TutWHww=='), 'STUDENT'),
('24110042', '8uz9gOP8WzPvKztia8b12LSmKxPsXqpEUqosHTJzcx8=', FROM_BASE64('5mnilildZ4N/cIEe8DiQaA=='), 'STUDENT'),
('24110043', 'iEwogNPT9wO0jHrkCI8VyL1Gu9LdTX0zmOcs3kjidfQ=', FROM_BASE64('Rb25LHoLofA+FYZiov7VMw=='), 'STUDENT'),
('24110044', 'L6UcdeNh1lDTsDsiW/dnSKYFwCJBK8iYZD33ibPRiag=', FROM_BASE64('ZqbMME7/7xxmIfNc6KCpOw=='), 'STUDENT'),
('24110045', 'fOyCjatBpSCk2cDNB33v6hC9qv4fNI/hfZrZFJ7/+7I=', FROM_BASE64('SQ5fQxgg+yLapbNPhblrow=='), 'STUDENT'),
('24110046', 'HOXwA5VKA/AZbhr3EMM2WR7trv9MvqtYT6kn/11QO+I=', FROM_BASE64('9a4Xf8/+I2/GfayXDlISBg=='), 'STUDENT'),
('24110047', 'oCbdKLEyLM2DwwlEvHOSffFN07r1iarLBCVr73uqzak=', FROM_BASE64('LGQhr/CvIGHc12seTZK7aQ=='), 'STUDENT'),
('24110048', 'oNqHOs+ltjqzLZ3H9p++9bGX63Zewg1V5E9DOwIh0w4=', FROM_BASE64('zlY20K6zIrDlCsCqjB3OqA=='), 'STUDENT'),
('24110049', 'fINrVF7I1NWqoqxl7LPRI2GL3fp/ixn0BArg2KeTI3U=', FROM_BASE64('sjWy6LMH4EEAH85ks4pMcg=='), 'STUDENT'),
('24110050', 'DXD3QBxjrUuWgMXC29YsEjAncGk8Io3NPkb3yeJmDGc=', FROM_BASE64('dPLKegQFmGaRA/Qm/RlEpQ=='), 'STUDENT'),
('24110051', 'gL5hBt8i+tmHrUHxqYq73NNqk1EEnZTvbNG8E53HT2s=', FROM_BASE64('vaVaAGGMWBl6rPcrpU+OfQ=='), 'STUDENT'),
('24110052', 'fV8UVw0VaI2YbQZuTe9WN9E8CawIqDpLyFP66vCNbvI=', FROM_BASE64('nNNo/mLN/K6Jy2mcRNK8dQ=='), 'STUDENT'),
('24110053', 'h864DwRTVJl5ACk2fxrsMLJ0UYk0F5jdcMYCsbaB57U=', FROM_BASE64('bEuGIT/ozZ+Jxzpnhlb0zQ=='), 'STUDENT'),
('24110054', 'EtZcezZo4XyxqbYvIZbKfsQ3b2Mfi9fSdCJghE7aI54=', FROM_BASE64('B7JXDLyXZQi9oE22MlW8rA=='), 'STUDENT'),
('24110055', 'Y5hNsVtbIvesQm9LlWe3r2q8E0F8dlV7//6CRG6vDbA=', FROM_BASE64('apFmhedJsUrF4AQIuMwqkQ=='), 'STUDENT'),
('24110056', 'gx24OxDhyxQ2f/tbCWBqBehWHTUCHRvvONeMBOs+j8I=', FROM_BASE64('ixftN55fXwH4wHh7Wyx2vw=='), 'STUDENT'),
('24110057', 'a/4YOsXd/N4cc5dGXz2tuzbThNJciyBBUwi0j9FNzh4=', FROM_BASE64('BNi3JqFDa999NVU8STKPdQ=='), 'STUDENT'),
('24110058', 'VDGOMtWT78rjuCMhwLQoX7qitf4l8dL0NIyzyWxxXjc=', FROM_BASE64('pCIkZrR+Yq2vTbUaMhc/Jg=='), 'STUDENT'),
('24110059', 'cUZcyWoEfFws9RtGf2x/X30M4IyRlSfO8P5dUvqhCec=', FROM_BASE64('3n9orBbuMiAThd+OYEcGNw=='), 'STUDENT'),
('24110060', '7QLOA3UW0ZJzfncmnkFojw7TbrmfL00Md0PaeplxF68=', FROM_BASE64('Ij0k5BMAVQtqMS7nRAbtmQ=='), 'STUDENT'),
('24110061', 'ZrdsWl0OY/+Is46M0CoUkmtPBSpPDdjnWGMAnUKQVKs=', FROM_BASE64('kfV4VtRmNsjuFJH2QcWxyQ=='), 'STUDENT'),
('24110062', '4M0QVVwL/BKiPr3E8t6HFHHajW8hckGYf8znRNQ509U=', FROM_BASE64('yKLZHF8SVKGaUZjFygXPhA=='), 'STUDENT'),
('24110063', '6vLHsLAz604bW1vGCNHhKt1VpCmElK0ZEd/tp4+1lio=', FROM_BASE64('eQ73x0bMkTWVU8utzRhfVg=='), 'STUDENT'),
('24110064', 'FIEWPKiWVeyJqYPD8a0zcLKnAUITI9O+ydZILVPqpFA=', FROM_BASE64('1YK99ZoDvhRQ8L3fXXbF6w=='), 'STUDENT'),
('24110065', 'HqahDlP9BmrjeYl56gaikT2Py90+xwRwstv4cDCp4fg=', FROM_BASE64('y0gdMEwg554zYDX/8FK4Pw=='), 'STUDENT'),
('24110066', 'epwYLfrfDGtzXE6VVpgmNZ9QTRjna+RfUYqc3q8Tees=', FROM_BASE64('w7zyHxz5SXsLSEZvhFtJ1Q=='), 'STUDENT'),
('24110067', 'wlUfwwqO3AV3+u9B/YtGD4nT8UTBR9rfNxL0/Bw+/Q8=', FROM_BASE64('T7GCldR2JHfGKmBbJRQvMA=='), 'STUDENT'),
('24110068', 'vIkxD45b6veqM+5jsw1oy/jVRoO7AcuVZM2wv3yhyIc=', FROM_BASE64('+D63oHUYJMnOFULawIvs7g=='), 'STUDENT'),
('24110069', '7KTrHrzyCSOiQ4yYz0X/8AimCyrSkKQr/ds5AEciL5Q=', FROM_BASE64('2Rqh+t6umoPljcOAoqQRmA=='), 'STUDENT'),
('24110070', '4Y1tPYlBBCxpkKUvJksLPll4122hhB+vsK8RXcD2E30=', FROM_BASE64('EiRymX1zecQi2X9wUTTQdA=='), 'STUDENT'),
('24110071', 'O69j0FnmhTQEZUo+fZu9fW1cYdzP3mEfY5EbFiBH9LM=', FROM_BASE64('wXRdFjrywDVu7eHGTCbwpQ=='), 'STUDENT'),
('24110072', 'sADLZPwc8cKvp0aAhZVsVxzgFROhfP0HvDlBdklFEvE=', FROM_BASE64('5ocH/R+Kg62dMPkNcPG4tQ=='), 'STUDENT'),
('24110073', 'jBZaq/zpFLQdCzT+1Qx17ba33eC4LXvEV+PU7UooUIw=', FROM_BASE64('G0eVcoXSbE/R3PsNOkPLTA=='), 'STUDENT'),
('24110074', '1LS5+t3AM4ril+w58TlZfIrYYk9TUctpNssMTiu5VJA=', FROM_BASE64('3+7Gmnm2eIfGKdsRyDumVQ=='), 'STUDENT'),
('24110075', 'Sl5Rax/X3G+/rnEE6Nc1h/Gfd3+NorX4vo5EZ4rLqTs=', FROM_BASE64('lPUmqmIAktyFZg1lvtDvtw=='), 'STUDENT'),
('24110076', 'VcU95n2FvqBjCv1l5ZPIceUcEm+Q7YrJngGvlxaAQhU=', FROM_BASE64('aQmIe/qepGt/m4Kj7NU4Eg=='), 'STUDENT'),
('24110077', 'DfhZN1k5ZBFAeSiFrP2ksvYB7B9Zo4jHSpCqYevTnTs=', FROM_BASE64('4KepxmaFxAhRJUStJP11WA=='), 'STUDENT'),
('24110078', 'hmKFVvgj5SBLXzAR7N3ds3ZtcZemh0xfhxCspJU1yPY=', FROM_BASE64('weNTNNg7oEgmMT0uT5fo8A=='), 'STUDENT'),
('24110079', 'lIEPjks9jZFjO3gpisI7QJSyoX9IB3OW2OlixS8Gm78=', FROM_BASE64('pwMiLjnCwLhG2IaUaakLkA=='), 'STUDENT'),
('24110080', 'y87V7vSPT/ElCMpGRlbs0t/NaFqvQ2wdlotBoaZPQIk=', FROM_BASE64('+h0LheC6taoW+70DgcHMuA=='), 'STUDENT'),
('24110081', '0WijMaLkOsL+XyAxfnh5QjiexrbwseL9CwTk8L4q1/Y=', FROM_BASE64('92dFhawj43Tv0bqtl8W4tA=='), 'STUDENT'),
('24110082', 'T3G8S2cQaZYBYgcTTf0rnCTlCMvhrrlLyqyfyf17P5o=', FROM_BASE64('eJzLqKZpp2aTlVpliBpfjg=='), 'STUDENT'),
('24110083', '9zi1Tkuw4Mcz3wuHd6PZqky3e3/XAikFV9fNRyq2ysA=', FROM_BASE64('/AQt0s+txTM91pyZ6AusmQ=='), 'STUDENT'),
('24110084', 'FjI7kWr1vlG5evAn2zddZgek4Do/AE4HTlYAiSsdcDQ=', FROM_BASE64('T0SqiDivBITgxPh3v2XVNg=='), 'STUDENT'),
('24110085', '5cEUo7GAS2jr29vZoUikXbTgaxPqvOaPTXsjxz6U3/c=', FROM_BASE64('+8zx17w8ITJNrHRr1iTvgA=='), 'STUDENT'),
('24110086', '140VQCMeCrUjy+pVPkyulXN8y/Pmsugy6GCVx/OC2do=', FROM_BASE64('glNczbTThd9j0y4KHoSS0A=='), 'STUDENT'),
('24110087', 'xO6lcVL2GOPzWe+GhwmjRXeHzx4sAQnRcnNWwOuT9U0=', FROM_BASE64('8dFtFYR/u/rxxGL2VCkECw=='), 'STUDENT'),
('24110088', 'S0wSSWb+AXUV8+awEzAll71ENh/g9S5fABPN/PFoTko=', FROM_BASE64('/YVoBYwjLop4PHH9++wJ+g=='), 'STUDENT'),
('24110089', 'bzipTTU/EWcM8B3GFFXEXipl3oKz7ghTJZ6cGt4Hvcw=', FROM_BASE64('eXJHePqH6TSnM5q865pfXg=='), 'STUDENT'),
('24110090', '7hpgXWmtXb3ATWHNwT+h2INtqJQrrp5cGq96pWSPaug=', FROM_BASE64('UKU5Z+NLH3/V4NDTjR91hA=='), 'STUDENT'),
('24110091', 'H+Ow+7R5/XhrxSQyqhItSWI2Y4294bvCT7siFrJAU7c=', FROM_BASE64('ifrlb812A1W9xPPp4xWGbQ=='), 'STUDENT'),
('24110092', '4Z8JP5i9PkpZ8B3PpXb6cyCfrAQjo8JFYZYIhfKO184=', FROM_BASE64('ZBREtpjCiU3yak3tfYFSng=='), 'STUDENT'),
('24110093', 'eyIcHUuV+F5+pmUHaxE+N4/lnxbTPpoT2r3XAKJNzGw=', FROM_BASE64('OqWYpQxLzhvjptp2yM8EYQ=='), 'STUDENT'),
('24110094', 'fwIU254fY1PIX3LQZ2ejdDL3Koo2L0zjTgxrRCAKT/Q=', FROM_BASE64('devtO8MljNJ1Af7wdRgHDw=='), 'STUDENT'),
('24110095', 'hpEgTZeOYLf5ZS4amxii43Ej/BA8sfCVX/4o7F3Y1R0=', FROM_BASE64('M8FFEkRbO+dF1uaq2IS4yw=='), 'STUDENT'),
('24110096', 'dFbJLdHjvwLukaXs2Nv1n82WBGObsYvPNR1Dsice+IU=', FROM_BASE64('DxJHGl8hRlDE0zgii6D6NA=='), 'STUDENT'),
('24110097', '26hMvffef4jROufI8ZLjQa48wOhLYiwrfWA2gEZN1bs=', FROM_BASE64('a62zf+GQzn7X1l34L5wjJA=='), 'STUDENT'),
('24110098', 'kfgelRkBnvyUO5Cg4alUyvDRhamJl19vFs9NNH4hFeU=', FROM_BASE64('Wq1uvJOWkx7Pb5hzlyzSZw=='), 'STUDENT'),
('24110099', 'EM2FKuWhGf2zulXxGa1VqACtRM336ZOJ7FcZ9Q4LZxY=', FROM_BASE64('1b4ZbdRVy2O2VA5z9V12gg=='), 'STUDENT'),
('24110100', 'TAkMvzhe0Txw4TUDfpJK6iq4eEpRSw9jme2KzqbnEOM=', FROM_BASE64('JlmpGn1COISOSzbDIwKP2Q=='), 'STUDENT'),
('24110101', 'KKw1QqSyPQkvmp9TFNknjG3L1XVYktmcivC7FUw9Thc=', FROM_BASE64('6IYsnkSZtqKNYIsL3dCgSA=='), 'STUDENT'),
('24110102', 'cQty/9xj9rfE5Mkcv50dxlntBhQRe+SrEE94FxL4zdY=', FROM_BASE64('OSoa+cHbMirT5MaQ1xp75w=='), 'STUDENT'),
('24110103', 'rJBUVxDH+lPhYCSojOm6yYqhDhj3+zEItjz/5J2aWNk=', FROM_BASE64('XiaU5b/XZrhGdBOtOMk+Bw=='), 'STUDENT'),
('24110104', 'pVU/Gu1jquNthUz6twK3hYLhNO30UrSK/MupXJhdwrg=', FROM_BASE64('3/qje9xvJFo9pOVDEdGqgw=='), 'STUDENT'),
('24110105', 'EOpUiWrzMw64poOcIoYnMEtC9U7iQMbpPYEAtkV5n30=', FROM_BASE64('ZP6z/bdTLJU8i66xRVjcVA=='), 'STUDENT'),
('24110106', '3VahkgIpDIPoI3Nah6/hRSAqiEVuCcUR47e7iC+YXFI=', FROM_BASE64('oaAnkrbgEOBPEO045A90YQ=='), 'STUDENT'),
('24110107', '+O/+qfc2sLCGF5A1w4PdcqLHoHNHgc6Xp6KjoWzKLaU=', FROM_BASE64('q8SrZ+9z01KCeaZ0YnUbwQ=='), 'STUDENT'),
('24110108', 'Rsbhkp9zA/VV/hZlRror35xN1ND3Z/M3f1Z2aNAlzYY=', FROM_BASE64('bF5beGcqmLkMCQ92NJWRlA=='), 'STUDENT'),
('24110109', 'QoO8UIG8M/86oH2Ph5nvqS3jjS6lVSfbVWQYk4cbDcI=', FROM_BASE64('Rpc/QPqVICSib1E5amj4eQ=='), 'STUDENT'),
('24110110', 'vsBmR6oHJYC5Fm1YZiwE/MHxmhL2fK6Rx3y6zc9ePfs=', FROM_BASE64('aEYCsmaaFgIaOYbbGYNpWg=='), 'STUDENT'),
('24110111', 'oPBUyQbtDwWfD5HaLrOmwLZ82yolQvGHrV5TI7fbH7Q=', FROM_BASE64('3W1M8vYo0Kdklmg+65+JfA=='), 'STUDENT'),
('24110112', 'jqEJa/uhn61AZBxk3FqJzvymEZKlLzL7sEUwcH3Sd5M=', FROM_BASE64('rqco6X8tial+UpjlBnwRkg=='), 'STUDENT'),
('24110113', 'lxBX9OHFSLRxp5a26SRs9Wj559ZkjCFPhM55z5k+ryU=', FROM_BASE64('z/2P05DIUQL/0mSMfwVD0g=='), 'STUDENT'),
('24110114', 'QcPCjJ/1oX7DwwzgGg9UxHm0n+ZsGwPkg/bVG18kZtM=', FROM_BASE64('xLJL70pDs4mXTBgL0m5pLA=='), 'STUDENT'),
('24110115', 'Phj+/iEhIju5cNuJfT3KdPr5odXh/ixRvtMqPo63t9s=', FROM_BASE64('vxVvUijduD0b20hB5VIvbA=='), 'STUDENT'),
('24110116', 'd2wXO0phanrlvyvWQReCfs8hDujD/CLXET9lR82UGzU=', FROM_BASE64('k8GL6+FRBONCLv8XvXrDxg=='), 'STUDENT'),
('24110117', 'pP4+0hkZ9LZWznuNHB/6dZMUuO1xVcp3B+r4CksKlbM=', FROM_BASE64('vjfNTvjGVJ+AuiZvdHjpQQ=='), 'STUDENT'),
('24110118', 'VIzpLwtxBCINUcOY7nPOVtFBZe6JS6jVtt1ftfY7qH4=', FROM_BASE64('zYP8paLriRxEAo2h2LQUrw=='), 'STUDENT'),
('24110119', 'ZuaCM5/eBxSb07fXt73TwQLRg7z16ESCBm5qFQz1FSI=', FROM_BASE64('vUd3D5S4JKva/Bbtrw9p6w=='), 'STUDENT'),
('24110120', 'LgJCBY/sDEPrMoN9CN82ajb70RMwUw/PUtqUIcMuyuo=', FROM_BASE64('UrT8JFb8un6pABAAjmfGUg=='), 'STUDENT'),
('24110121', 'XrnyCSgBatuwh3Y5N12YZA8Q/CFRDBAVNRmOx+l3jDM=', FROM_BASE64('FoRAzwTMr2j3/qZSHttXEw=='), 'STUDENT'),
('24110122', '4pAgBcVRMN3J8qKA7mUlZ8qfu9z1e4tPLs7ioX2sJx4=', FROM_BASE64('LvOxy5rbdpzGjuz0brqlSw=='), 'STUDENT'),
('24110123', 'N3d41dz7O955P8Z4KgU31nNv0mYO5Hj7SMkrBDLgupk=', FROM_BASE64('QbpHSh5rpbyENXK3WdFt7A=='), 'STUDENT'),
('24110124', 'jWfrN0drF56EmeyF2Kl8L1B4Z4khhi6sqfEl5PE5LKg=', FROM_BASE64('Lc8lSujqkbR4VaOirmIafA=='), 'STUDENT'),
('24110125', 'cQuiKkavwJgzTo8PbAhVZOo8SHqkc/waIhGT3D2+yF8=', FROM_BASE64('gmZujR0oz3M/43h9FV82nw=='), 'STUDENT'),
('24110126', 'ZzGD+zH8+7ReqYQ/lGtU5wft3Bvf2ecIsHF0oeqjp1Q=', FROM_BASE64('uJu00EIH8yDPNeOU7zlGzQ=='), 'STUDENT'),
('24110127', 'WZefxe3OZ5Ildsn4twn0Etq1hvOAuPhemC5uXdCIcIA=', FROM_BASE64('UAt2oBMRvhUnkn6NQZaQkw=='), 'STUDENT'),
('24110128', '+9PGtGwoPhlo0mqDVXymgOO1Uw3Ts7qodUZE0NWrsvE=', FROM_BASE64('qrN6Vq8TsWdsdWbfsEDwOA=='), 'STUDENT'),
('24110129', 'vqQQWm8DvdQZzUZ4/Ql6+zseHzd806Nxulr5AMXWX64=', FROM_BASE64('GzdbGin/9Kd3vwqmcKiAHw=='), 'STUDENT'),
('24110130', 'fea8MVCAUwKyf3kWTArHJxmJPJlabKffE2qkT4ilVfo=', FROM_BASE64('7Qam3/4T935YXxSl/bDTiw=='), 'STUDENT'),
('24110131', '268XIBkkI5EXD6TgaeEww7qpqwl+b/1UH4MpehvEdoo=', FROM_BASE64('USq20dXLGsXRYGw6j8F+Yw=='), 'STUDENT'),
('24110132', '6pmuYDO+YfS8L7iGKr5M4FhWAoVZBgOojbDg1G3z3ts=', FROM_BASE64('xKPG4NMkJWOS8doc+A5Ppg=='), 'STUDENT'),
('24110133', 'Hasou3SJjO3GCVbgda62zqRqxuuaS6G8aoQc9D40wUA=', FROM_BASE64('swQylyY0Lj51RuCk4k8uoA=='), 'STUDENT'),
('24110134', 'v/nAoYEPreKr2xnnDlcKJNKSzi9Jt+lSZaY2rZS4dXU=', FROM_BASE64('Dj3ydyY/3mCoBv1jb1GLVQ=='), 'STUDENT'),
('24110135', 'ucj+yvD3rkjyiB/yZeXIPsXuVE4YSqcRizhRAeY6MI0=', FROM_BASE64('WRxgkX5wco+eIPKvADdBGw=='), 'STUDENT'),
('24110136', 'TG2CYWP5LBuuBy7LQM0Yrixup/PqDY4jq4EDWUZDlkw=', FROM_BASE64('N2AJswfcxbtAkU5QYEl4dg=='), 'STUDENT'),
('24110137', 'kkMz44cVJ+tH0uY6O7PLDoStTuMxoGF7hFKxuNfEnKM=', FROM_BASE64('oGfbNgCfJN1tVoDsMKr7+Q=='), 'STUDENT'),
('24110138', 'H7xLuPr9j+dLc5xf+dB67LnuqwMqQBLDPLYNvpj04t0=', FROM_BASE64('wvDa6xi8LxQyDkkdrJwjoQ=='), 'STUDENT'),
('24110139', 'ocabjdjFiBdIA5rwmBObDArUkD39Nx47YJ40Lc/iCqI=', FROM_BASE64('JQaDGL2HZdJhFbR3z7ZLEw=='), 'STUDENT'),
('24110140', 'o2SvkVWHaK5qK7hGGINHB0ruNbky5YVfsnKqODTcytc=', FROM_BASE64('pj7wVgzg7KM1/wp6uOZ+Ug=='), 'STUDENT'),
('24110141', 'QePVBfWlafL9aEW5n0xAz3crurA5kXemAN92aRZcOzA=', FROM_BASE64('l95JTe7dOguQZ+neip9D/A=='), 'STUDENT'),
('24110142', 'EDIxOnhLIIwkJBveGKjDNLAZ3nEU5qHvFdytoZHHVzs=', FROM_BASE64('aZvL8WEnSeUzkKAIfxMUSA=='), 'STUDENT'),
('24110143', 'ffIWxeQ5hGP/k8ZcTEZQ9xHT16XAHuTALZRybuLBVWM=', FROM_BASE64('fN9eJI4/WkL5gNaO/dCLQQ=='), 'STUDENT'),
('24110144', '27WhrUoK7sHAMxsOjnkXVOZrfpIVDkT9g8akUNvRAxY=', FROM_BASE64('9Av9z9Yy+jfB8aj4KKiQ8w=='), 'STUDENT'),
('24110145', '4eQ10rpSUDz9EQI2HFSE9rthHeG/K8l5xzOtCWk9Ikk=', FROM_BASE64('/Aa+ZAof9jMfZnvO+qDFmA=='), 'STUDENT'),
('24110146', 'cV/pmUiT1wTyuG13Wfi3VPI+6nJuSwRkSr3yOkmGmA8=', FROM_BASE64('cV389IL2slTuL6VSrhYnOw=='), 'STUDENT'),
('24110147', 'GvdsjvfYxiYj/gK4ngB9HgSzBI2ncLylIWWeVz8rz+Y=', FROM_BASE64('98AzShCuAPb/sDp7DTX2Ig=='), 'STUDENT'),
('24110148', 'hy0b380/sEgVPL/zhi3hWixLZtHcULV9vV0jHiAoPzY=', FROM_BASE64('rUiFiThBFQVYW63boof+Sw=='), 'STUDENT'),
('24110149', 'iAs75ZkfJsY7/dLUbNfLsr956zACHqHmv4xanjF3aA8=', FROM_BASE64('+1PNIoD5W5fie1xpE+9Raw=='), 'STUDENT'),
('24110150', 'SAPUyciW4PjRYGsnrh4V3uh0SpHAfYt9mu+rAJNqgOQ=', FROM_BASE64('E4j9hmGi0TlMW9TTpY1aqA=='), 'STUDENT'),
('24110151', 'Ypd21fMkSGLqhpjfUm9TM+xHzoCvxMGy8ieeS5XGe7M=', FROM_BASE64('xi8Hz3h8YsEtTMrCD6AwVA=='), 'STUDENT'),
('24110152', 'HSmt3U1BWi2UKtvl9CHV0WZIUHu9b57GdM00lr/sTG0=', FROM_BASE64('u1VXRpawsY6cV2WfGOVt9g=='), 'STUDENT'),
('24110153', 'SaGIuBxy4nrYYB3C0bn2Yu4+KFY6BRKn5K5eHSmbeiA=', FROM_BASE64('DFiou1G3smNybzO+2Dm11Q=='), 'STUDENT'),
('24110154', 'LG5Bsm3uiBQn42l66Qq5ynxpNtvv6c93SAj+KtuuPCg=', FROM_BASE64('jO/uPUpSKzfFBPM0otkUtg=='), 'STUDENT'),
('24110155', 'DBpfy7ovY65gjdPDFS63jUWbsQtqs0NZQLOPIkSxm6g=', FROM_BASE64('S78u7p+By1O5N4k5zgrjiA=='), 'STUDENT'),
('24110156', 'nks9sU14zpBS3z0YEm3WqQAutVJlsSjzZTmtdKsMrr0=', FROM_BASE64('sYcfC+VIiAUf9IDyjfIx5w=='), 'STUDENT'),
('24110157', '0UfY7m7B20WmWkyFFvVC/gcRihF7QOFDvRF/gEhYoLo=', FROM_BASE64('r1+1mMF8YbJ2aR4w4QJTdw=='), 'STUDENT'),
('24110158', 'QP8w21eTNwByLVwQZBSpYZID/YNQSe5V9kWR2hCOFIg=', FROM_BASE64('3z98azcYdoWx3rTL/BUqLQ=='), 'STUDENT'),
('24110159', 'dFbLMe4Zvy1ijFXFm85S8NYMG+WUGnXT6OVWqgZeNU4=', FROM_BASE64('b7kN7eqosSd5u4uT0N3xeg=='), 'STUDENT'),
('24110160', '/L3CGY82yBzYQYow+xbbDHakmg+CMaHpT++V8v6Pb9U=', FROM_BASE64('z5I6ZOfwX3lZzlPJ79AV6g=='), 'STUDENT'),
('24110161', 'swCvd3Ndir717KHWiHCYUgDv1oyVVraHUJgNamPH3V8=', FROM_BASE64('nUsEyV9jlWRVG42BC1yZ7Q=='), 'STUDENT'),
('24110162', 'nlJLammwy2EhwVDNVLQBYJL+ASDTjX0rvT8N62xPjaA=', FROM_BASE64('5QWWyGuP+qf/DMW1nxNR/Q=='), 'STUDENT'),
('24110163', '2hCbrLRT0R27w9SfoHg+wvx+9ga5SEOKXIgzPPqrf54=', FROM_BASE64('7IGuJydC+pPd2GiQNkuWsg=='), 'STUDENT'),
('24110164', 'MUIAUHOrkuK1ts6Ty7dH+gOfc10ZpzSgVTU8S4pYQLA=', FROM_BASE64('MRZvq92oY42Llba+2LrCGA=='), 'STUDENT'),
('24110165', 'YH3zyFM6LYHVAQrR4FsAf0JH9PMvEhUHYmxKHWGgz8k=', FROM_BASE64('RtgKrOmc2qWWd+QQPCCi4A=='), 'STUDENT'),
('24110166', 'hw9GRCS1kGfhJDSyiRR+glaTagGkM1SiownbaN4u1Cw=', FROM_BASE64('KJ3x+LJGcLdmtgHz8gI1AQ=='), 'STUDENT'),
('24110167', '2DC9i82OSKB+pbrcZaVIqNiTO9awEoFbPuTxZrD0U94=', FROM_BASE64('8pQfZipQV2X+M6SIaEwImg=='), 'STUDENT'),
('24110168', '3BYuGbyHP4+f8oPv9edN37wQD7p7LVaJ1w35mXHNhII=', FROM_BASE64('E+IFv9j/ws9mnE6/QoHiPQ=='), 'STUDENT'),
('24110169', 'unCGWNtyz/jX/waVfYoaqSiyR0W2UssQGyncR8v7AxU=', FROM_BASE64('+9B0e2gHXcZTit8gSgMQtA=='), 'STUDENT'),
('24110170', 'D3+4eyI/MMSKD9eS7U20qdoVmd2SzXwXXx8sXvx+umg=', FROM_BASE64('yHGd8Q4XUz4Ze0CLYaviSQ=='), 'STUDENT'),
('24110171', 'VcK6S2YOTdurACT8JQpGklhOVUJT2Ke7vcd6yUELRKc=', FROM_BASE64('15Onzf0MAE0i97JgEg4tVw=='), 'STUDENT'),
('24110172', 'Qtoqqq9Bazj5jcQNhU4z+PveKkUx1WSgNqgP0dL751Q=', FROM_BASE64('pC/PU7H0YN16UU0xQiQR2g=='), 'STUDENT'),
('24110173', 'kA/+bdRPx6j/3QVClhwZktgTtdug9AkJT+BNBtnN0Kc=', FROM_BASE64('U0xWkV9hF96Ne/cerbpNxQ=='), 'STUDENT'),
('24110174', 'G92mG2l9JAL7TdGnwkbJwe0zugxb8jCdQTsF/4WwcR8=', FROM_BASE64('VgTbKW62A1dFcI/nXLzi7A=='), 'STUDENT'),
('24110175', 'IWCyQ08qC60gWTJLARy0wKOGBnxj2BXUckAwtmSYG5Q=', FROM_BASE64('fT2MnBoh6dE4qKQw0VCVFQ=='), 'STUDENT'),
('24110176', 'wDT49DYdcFEukhCCGoly584lnvSE7lX72YrdzbSV6Lk=', FROM_BASE64('jcsMusHdoxkNnJjhmKxqPw=='), 'STUDENT'),
('24110177', 'Xq5PdTN2ndRW1JHKizPlABkzQuyfIPTKf6iN6QyyBi8=', FROM_BASE64('NKJ3cGrG++c0EeCUBE970g=='), 'STUDENT'),
('24110178', 'yI0L/cIxXa2LKB2qERG+P/Eo9jq9qPgXfY6ZOKEiEKI=', FROM_BASE64('YMT5x4UJrN/gXWl9w4ZDog=='), 'STUDENT'),
('24110179', '+VhD13dkezMfU9rNIJJx/6gXqB3WDxbc/EdDTDSmijc=', FROM_BASE64('mo+v12j7niNuPOCzxVRnog=='), 'STUDENT'),
('24110180', '4VLs9+YrULeWW0nmHD/cp3qT8sntNRXU81RUl+hJ6R8=', FROM_BASE64('qoRBxolEawsNlR4OXXxPoA=='), 'STUDENT'),
('24110181', 'tRc7iwnKO9JbL7v6U8QdrS/sVO0lbN63MnQ2cX7IJMY=', FROM_BASE64('FXnbqCEl0JG464Sjzc9Twg=='), 'STUDENT'),
('24110182', '5/z4IBK0odHZatgANxdj0q5fe15Wqg77lghY3rOxA6I=', FROM_BASE64('+5hl3N1YOBxW43h0A90S5Q=='), 'STUDENT'),
('24110183', 'JAqYSfCTVFarHSVVH/nshT7OCJvrikb0+c1Gc1jxU9g=', FROM_BASE64('YkcoMztbwy3kdBDHETICQA=='), 'STUDENT'),
('24110184', 'Q1MKmgEy3C5Lg3Cfj6jdZIyKsdjgpK6nMjXNbMR4Fq4=', FROM_BASE64('HYgS6s0XpsFYY+yosAItuw=='), 'STUDENT'),
('24110185', 'eLnG+fESJvKQSnobpSUxlc1tqyrPdY6uPIs6us4CsdQ=', FROM_BASE64('FFlsrSpy5xujHDQryXmPHw=='), 'STUDENT'),
('24110186', 'ud8ipcpCvo4pjHnHEYZiKhaQ3Xbl8DPfmOb85rJiKrQ=', FROM_BASE64('/3KG/7rzcfByZmcPqumg2Q=='), 'STUDENT'),
('24110187', 'YuqeGyqepwVUhavm0e9hBCAEjbF+lMJqQkUJ39HYWRg=', FROM_BASE64('DydeBKg0fG9OEBFpl8cPZQ=='), 'STUDENT'),
('24110188', 'TFsXgDk7uXrl9gTMcM+rsGxfDPBsMNqhV5ENlAz3CSk=', FROM_BASE64('Jt7oqUPQ/7oP0f/Hpp5SyQ=='), 'STUDENT'),
('24110189', 'j2uDAT+oTl+uqqZjaWt6IyMwJlKdVugFXRX4Wamtg/s=', FROM_BASE64('N6qn8/DIgG1tWAUScxkWFg=='), 'STUDENT'),
('24110190', 'arJCm8BMwcThvVWDp/r8R1GwsZ7b7iM5suIsYI1hqhs=', FROM_BASE64('yLVjKfesuD0YZA3sIG3vBg=='), 'STUDENT'),
('24110191', '4Iml9sP8v0v9l15Hs/4JnFSg22N+W4T66aQVLe4tzvg=', FROM_BASE64('v6ZaYtVOIbcB5JgJbZPRXQ=='), 'STUDENT'),
('24110192', '4lzWDyiciFXXfKoy12+9UInFDl+Ymdx/yEORPbp/JnY=', FROM_BASE64('7ov0hjX+chwTszMhL26Lug=='), 'STUDENT'),
('24110193', 'WU5JqNj0yzoU1K+94jcAQDc+wiTXr7v/7XAV6Kg9YCg=', FROM_BASE64('4YjnVD+8qcyu14UttOQLTg=='), 'STUDENT'),
('24110194', '6QUCz3GVawIXxZIzeICCExYswZqgYE3niUsv7n7CdpI=', FROM_BASE64('5y3ad63wyQFGriShxbyyWQ=='), 'STUDENT'),
('24110195', 'HU/xKh2pdpZ1vn5GMJZHzPPLwTpl2uXddFYVTqhbkNM=', FROM_BASE64('/YoXRju+yPu9AKhDIx7ZGg=='), 'STUDENT'),
('24110196', '6QO0GgZSXR4LZPhPg1jeHz0UEoqHAc8nppXLZwdRkhQ=', FROM_BASE64('Dp0c0zo4hk3SItCNGeH62Q=='), 'STUDENT'),
('24110197', 'o2RNyWeCV6brCLNG0vESOCfE1+HQ0P9D0h1cGm7N2fk=', FROM_BASE64('hhw+vQzwjkFQVIRcR2bDBw=='), 'STUDENT'),
('24110198', 'Ef/KYuCzdsP+gGzzjKR0IwaxKtZw5Kj31wDOqgbYJ4U=', FROM_BASE64('kQ/zMq8TAJhn7JgqVwDfeQ=='), 'STUDENT'),
('24110199', 'QhUpDM27rFflvBGvO3FcjXAlieErX9AYDmsWugBdqsA=', FROM_BASE64('0dw10s/a3Dcf9DjAI2+6DQ=='), 'STUDENT'),
('24110200', 'x2O6dR/N5YckDcO243nJHsUWB6HVinwKwahr11CjJho=', FROM_BASE64('TAj1ihqhD7AYubp9Eaiv0Q=='), 'STUDENT');

INSERT INTO admin (code, fullname, account_id) VALUES
('AD001', 'Hệ thống Quản trị', 1),
('AD002', 'Phòng Đào tạo', 2);

INSERT INTO lecturer (code, fullname, account_id) VALUES
('GV001', 'Nguyễn Minh Đạo', 3),
('GV002', 'Võ Lê Phúc Hậu', 4),
('GV003', 'Trần Thanh Sơn', 5),
('GV004', 'Phạm Quang Huy', 6),
('GV005', 'Lê Anh Tuấn', 7),
('GV006', 'Đỗ Thị Thu Hà', 8),
('GV007', 'Nguyễn Thị Bích Ngọc', 9),
('GV008', 'Trần Văn Khoa', 10),
('GV009', 'Bùi Gia Bảo', 11),
('GV010', 'Phạm Minh Châu', 12),
('GV011', 'Đặng Quốc Đạt', 13),
('GV012', 'Ngô Thanh Long', 14),
('GV013', 'Hồ Vĩnh Hoàng', 15),
('GV014', 'Lý Phương Thảo', 16),
('GV015', 'Trịnh Hữu Thọ', 17),
('GV016', 'Đinh Công Chứ', 18),
('GV017', 'Lương Tấn Tài', 19),
('GV018', 'Mai Quỳnh Hương', 20),
('GV019', 'Vũ Anh Kiệt', 21),
('GV020', 'Cao Tiến Đạt', 22);

INSERT INTO student (code, fullname, birth_of_date, email, avatar_url, class_id, enrollment_year, account_id) 
SELECT CONCAT('24110', LPAD(n, 3, '0')), 'Sinh viên OnlineUTE', '2004-01-01', CONCAT('st', LPAD(n, 3, '0'), '@student.hcmute.edu.vn'), '', MOD(n, 7)+1, 2024, n + 22 FROM 
(WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 200) SELECT n FROM seq) AS s;

INSERT INTO course (course_code, fullname, credit) VALUES
('MATH1', 'Toán Cao Cấp 1', 3), ('IT1', 'Lập trình Java', 4), ('IT2', 'Cơ sở dữ liệu', 3), ('ENG1', 'Tiếng Anh 1', 2);

INSERT INTO course_section (section_code, course_id, term_id, lecturer_id, room, start_slot, end_slot) VALUES
('SEC01', 1, 3, 1, 'A1-101', 1, 3), ('SEC02', 2, 3, 2, 'C2-202', 4, 6);

INSERT INTO announcement (title, content, target_type, sender_name) VALUES
('Chào mừng năm học mới', 'Chào mừng các bạn sinh viên đến với OnlineUTE.', 'ALL', 'Ban Giám Hiệu');

SELECT 'Setup complete' AS status;
