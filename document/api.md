# Controller API Documentation

Tai lieu nay tong hop cac API public hien co trong cac controller cua du an.

## 1. AuthController
File: `src/main/java/com/bangcompany/onlineute/Controller/AuthController.java`

### 1.1 Login
- Signature: `Optional<Account> Login(String username, String password)`
- Mo ta: Dang nhap bang username va password.
- Dau vao:
  - `username`: ten dang nhap
  - `password`: mat khau
- Dau ra:
  - `Optional<Account>`: co gia tri neu dang nhap thanh cong, rong neu that bai.

### 1.2 Logout
- Signature: `void Logout()`
- Mo ta: Dang xuat tai khoan hien tai (clear session).
- Dau vao: khong co
- Dau ra: khong co

### 1.3 RegisterStudent
- Signature: `Account RegisterStudent(Account account, Student student)`
- Mo ta: Dang ky tai khoan sinh vien.
- Dau vao:
  - `account`: thong tin tai khoan
  - `student`: thong tin ho so sinh vien
- Dau ra:
  - `Account`: tai khoan vua dang ky
  - `null`: neu xay ra loi trong qua trinh dang ky

### 1.4 RegisterLecturer
- Signature: `Account RegisterLecturer(Account account, Lecturer lecturer)`
- Mo ta: Dang ky tai khoan giang vien.
- Dau vao:
  - `account`: thong tin tai khoan
  - `lecturer`: thong tin ho so giang vien
- Dau ra:
  - `Account`: tai khoan vua dang ky
  - `null`: neu xay ra loi trong qua trinh dang ky

### 1.5 RegisterAdmin
- Signature: `Account RegisterAdmin(Account account, Admin admin)`
- Mo ta: Dang ky tai khoan quan tri.
- Dau vao:
  - `account`: thong tin tai khoan
  - `admin`: thong tin ho so quan tri
- Dau ra:
  - `Account`: tai khoan vua dang ky
  - `null`: neu xay ra loi trong qua trinh dang ky

## 2. StudentController
File: `src/main/java/com/bangcompany/onlineute/Controller/StudentController.java`

### 2.1 createStudent
- Signature: `void createStudent(Student student, Account account)`
- Mo ta: Tao sinh vien moi va lien ket voi account.
- Dau vao:
  - `student`: thong tin sinh vien
  - `account`: thong tin tai khoan
- Dau ra: khong co

## 3. NotificationController
File: `src/main/java/com/bangcompany/onlineute/Controller/NotificationController.java`

Hien tai chua dinh nghia API public nao.

## 4. Ghi chu
- Day la API o tang controller trong ung dung desktop (Swing), khong phai REST endpoint HTTP.
- Method naming hien tai dang dung PascalCase va camelCase xen ke (`Login`, `Logout`, `createStudent`).
- Co the chuan hoa dat ten method ve camelCase dong nhat trong cac controller.
