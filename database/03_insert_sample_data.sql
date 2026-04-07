USE online_ute;

-- 1) MASTER DATA: Academic Terms
INSERT INTO `term` (`year_name`, `term_name`, `is_current`) VALUES
('2024-2025', 'HK1', FALSE), ('2024-2025', 'HK2', FALSE), ('2025-2026', 'HK1', TRUE);

SET @term_cur = 3;

-- 2) MASTER DATA: Faculties (based on doc/falcuty.md)
INSERT INTO `faculty` (`faculty_code`, `fullname`) VALUES
('MFG', 'Khoa Cơ khí Chế tạo máy'),
('CHEM', 'Khoa Công nghệ Hóa học và Thực phẩm'),
('IT', 'Khoa Công nghệ Thông tin'),
('EEE', 'Khoa Điện - Điện tử'),
('FDT', 'Khoa Thời trang & Du lịch'),
('ECON', 'Khoa Kinh tế'),
('CIVIL', 'Khoa Xây dựng'),
('APSCI', 'Khoa Khoa học Ứng dụng'),
('LANG', 'Khoa Ngoại ngữ'),
('PRINT', 'Khoa In và Truyền thông'),
('PEDTECH', 'Khoa Sư phạm Công nghệ'),
('AUTO', 'Khoa Cơ khí Động lực');

-- 3) MASTER DATA: Majors (Representative set)
INSERT INTO `major` (`major_code`, `fullname`, `total_credit`, `faculty_id`) VALUES
('110', 'Công nghệ thông tin', 145, 3),
('145', 'Công nghệ kỹ thuật ô tô', 145, 12),
('133', 'Kỹ thuật dữ liệu', 145, 3),
('146', 'Công nghệ kỹ thuật cơ điện tử', 145, 1),
('125', 'Kế toán', 145, 6);

-- 4) MASTER DATA: Classes
INSERT INTO `class` (`class_name`, `major_id`) VALUES
('241101A', 1), ('241101B', 1), ('241451A', 2), ('241251A', 5);

-- 5) ACCOUNTS (2 Admin, 5 Lecturer, 5 Student)
-- Passwords are fixed and hashed per setup.sql logic
INSERT INTO `account` (`username`, `password_hash`, `salt`, `role`) VALUES
-- Admins
('AD001', 'BH4wEHOGUXWnr9CeyNtuPArhbJ61DOmpWzZirw3WzM4=', FROM_BASE64('b0BCNeKjIPAmpx+BKf2Cuw=='), 'ADMIN'),
('AD002', 'r3kjwtbqLOkUzkvELjuwookttAKXuJuiPcxoZMsD0iM=', FROM_BASE64('TpWL9j5djorvmFql0WRqnw=='), 'ADMIN'),
-- Lecturers
('GV001', 'yzl/CfBoRcAM4zJqbBs8toUbBL9BD3XKTtTepjMt1Nk=', FROM_BASE64('lnR7/yv1raqSqjZgkWqKCA=='), 'LECTURER'),
('GV002', 'aQkIy+PN1HD0suPcRDAj6GxGjBN0G9x5GWcQZAe51t8=', FROM_BASE64('QNQcd2im0WGNOfuebcHvug=='), 'LECTURER'),
('GV003', 'iq/Hm0z/S8FL1erw2/qnrG/UZ7lEE0YP6QulFoDkY1s=', FROM_BASE64('JtLhd/ub0r8uaHtfLnm0mA=='), 'LECTURER'),
('GV004', 'srnVENILmMdBfw6/FMdycabf9Ww+CT6X+INTpPDaKDw=', FROM_BASE64('uv4zeBGoZnruVoqs0EBHiw=='), 'LECTURER'),
('GV005', '+YBhLLX4+lvlEVnxjwpFa9HffOVrarvn7an1f67DPQA=', FROM_BASE64('ktVdxGSIZOc7X+pdVqlX7g=='), 'LECTURER'),
-- Students
('24110155', 'DBpfy7ovY65gjdPDFS63jUWbsQtqs0NZQLOPIkSxm6g=', FROM_BASE64('S78u7p+By1O5N4k5zgrjiA=='), 'STUDENT'),
('24110001', 'zqOqSriwYw/scnXWCELTyb47MT6qm1TUHrmsEnFDXMY=', FROM_BASE64('+CpgoxIXbXJWv0n4djokEA=='), 'STUDENT'),
('24110002', 'X8UU0AmksTEgiBvhOpSascM3qYLe5I8F+vnPDL2UjLo=', FROM_BASE64('yOU3jVb5x4UoglH35pDT+A=='), 'STUDENT'),
('24110003', 'PIA+OZPdI64GN1wXbq2o8B6VIVIjNv2wVRekoKvpUvo=', FROM_BASE64('TbXgS3ykL1fKEol2EEVrIw=='), 'STUDENT'),
('24110004', '34zB8+apBcxN/ulrmK781wnagiWllZ0oOlkOe6lBP64=', FROM_BASE64('Rb+ai7FocL2nlCDk8qYRTg=='), 'STUDENT');

-- 6) ENTITY DATA
INSERT INTO `admin` (`code`, `fullname`, `account_id`) VALUES 
('AD001', 'Hệ thống Quản trị', 1), ('AD002', 'Phòng Đào tạo', 2);

INSERT INTO `lecturer` (`code`, `fullname`, `account_id`) VALUES 
('GV001', 'Nguyễn Minh Thái', 3), ('GV002', 'Võ Lê Phúc Hậu', 4), ('GV003', 'Trần Thanh Sơn', 5), ('GV004', 'Phạm Quang Huy', 6), ('GV005', 'Lê Anh Tuấn', 7);

INSERT INTO `student` (`code`, `fullname`, `birth_of_date`, `email`, `avatar_url`, `class_id`, `enrollment_year`, `account_id`) VALUES 
('24110155', 'Phùng Thanh Độ', '2006-11-20', '24110155@student.hcmute.edu.vn', '', 1, 2024, 8),
('24110001', 'Nguyễn Văn Anh', '2006-01-15', '24110001@student.hcmute.edu.vn', '', 1, 2024, 9),
('24110002', 'Trần Thị Bình', '2006-02-20', '24110002@student.hcmute.edu.vn', '', 1, 2024, 10),
('24110003', 'Lê Hoàng Cường', '2006-03-10', '24110003@student.hcmute.edu.vn', '', 1, 2024, 11),
('24110004', 'Phạm Minh Đức', '2006-04-05', '24110004@student.hcmute.edu.vn', '', 1, 2024, 12);

-- 7) PROFILES
INSERT INTO `user_profile` (`account_id`, `profile_code`, `display_name`, `email`, `role_title`, `current_address`, `faculty_name`, `major_name`, `academic_year`, `expected_graduation_year`) VALUES 
(1, 'AD001', 'Quản trị hệ thống', 'admin@hcmute.edu.vn', 'Quản trị viên', 'TP.HCM', NULL, NULL, NULL, NULL),
(2, 'AD002', 'Phòng Đào tạo', 'pdt@hcmute.edu.vn', 'Quản trị viên', 'TP.HCM', NULL, NULL, NULL, NULL),
(3, 'GV001', 'GV Nguyễn Minh Thái', 'gv001@hcmute.edu.vn', 'Giảng viên', 'TP.HCM', 'Khoa Công nghệ Thông tin', NULL, NULL, NULL),
(8, '24110155', 'Phùng Thanh Độ', '24110155@student.hcmute.edu.vn', 'Sinh viên', 'Biên Hòa, Đồng Nai', 'Khoa Công nghệ Thông tin', 'Công nghệ Thông tin', '2024-2028', '2028');

-- 8) COURSES & SECTIONS & SCHEDULES
INSERT INTO `course` (`course_code`, `fullname`, `credit`) VALUES 
('PRG1', 'Lập trình Java', 4), ('DB1', 'Cơ sở dữ liệu', 3), ('AI1', 'Trí tuệ nhân tạo', 3), ('NET1', 'Mạng máy tính', 3);

-- Section 1: Java (GV001)
INSERT INTO `course_section` (`section_code`, `course_id`, `term_id`, `lecturer_id`, `room`, `start_slot`, `end_slot`, `day_of_week`) 
VALUES ('Java01', 1, 3, 1, 'A1-101', 1, 3, 2); -- Monday Slots 1-3

-- Section 2: DB (GV002)
INSERT INTO `course_section` (`section_code`, `course_id`, `term_id`, `lecturer_id`, `room`, `start_slot`, `end_slot`, `day_of_week`) 
VALUES ('DB01', 2, 3, 2, 'C2-202', 4, 6, 4); -- Wednesday Slots 4-6

-- Full Schedule for Section 1 (Weeks 1 to 15)
INSERT INTO `schedule` (`course_section_id`, `day_of_week`, `start_slot`, `end_slot`, `room`, `week_number`, `study_date`)
SELECT 1, 2, 1, 3, 'A1-101', n, DATE_ADD('2025-08-18', INTERVAL (n-1) WEEK)
FROM (WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 15) SELECT n FROM seq) AS s;

-- Full Schedule for Section 2 (Weeks 1 to 15)
INSERT INTO `schedule` (`course_section_id`, `day_of_week`, `start_slot`, `end_slot`, `room`, `week_number`, `study_date`)
SELECT 2, 4, 4, 6, 'C2-202', n, DATE_ADD('2025-08-20', INTERVAL (n-1) WEEK)
FROM (WITH RECURSIVE seq AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 15) SELECT n FROM seq) AS s;

-- 9) REGISTRATION & MARKS (For Phùng Thanh Độ)
INSERT INTO `course_registration` (`student_id`, `section_id`, `status`, `reg_date`) VALUES 
(1, 1, 'APPROVED', '2025-07-21'), (1, 2, 'APPROVED', '2025-07-22');

INSERT INTO `mark` (`registration_id`, `process_score`, `test_score`, `final_score`, `grade_char`, `attendance`) VALUES
(1, 9.0, 8.5, 8.7, 'A', 'Vắng 0 buổi'),
(2, 7.5, 8.0, 7.8, 'B+', 'Vắng 1 buổi');

-- Announcement
INSERT INTO `announcement` (`title`, `content`, `target_type`, `sender_name`) VALUES
('Thông báo bắt đầu học kỳ', 'Sinh viên chú ý thời gian bắt đầu HK1 là 18/08/2025.', 'ALL', 'Hệ thống AD');

SELECT 'Mass data inserted successfully' AS status;
