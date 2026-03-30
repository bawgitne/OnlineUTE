# Cau truc View hien tai

Phan giao dien dang duoc dong bo theo namespace `com.bangcompany.onlineute.View.*`.

Luong chinh hien tai:

1. `View.navigation.MainNavigator`
2. `View.features.auth.LoginScreen`
3. `View.features.dashboard.DashboardLayout`
4. `View.features.dashboard.MainContent`

Trang thai to chuc hien tai:

- `View.features`
  - chua code that cua login, dashboard, announcement, account, schedule, grade, attendance, profile
  - rieng `View.features.dashboard` giu toan bo ha tang dashboard:
    - `DashboardLayout`
    - `MainContent`
    - `Sidebar`
    - `TopHeader`
    - `PageScaffold`
  - `View.features.profile` hien co:
    - `ProfilePage`
    - `ProfileSectionCard`
    - `ProfileFieldPanel`
- `View.navigation`
  - giu dieu huong va `Refreshable`
- `View.Components`
  - giu component UI nho va UI helper tai su dung, bao gom `TableStyles`
  - trong do `View.Components.leftbar` giu cac component chi phuc vu menu ben trai:
    - `LeftBarButton`
    - `LeftBarTitle`
    - `NavMenu`
    - `SidebarItem`
    - `UserProfileCard`

Da xu ly:

1. Dong bo namespace ve `View`
2. Gop con 1 `View.WindowManager`
3. Xoa nhom legacy khong con nam trong luong chinh:
   - `MainView`
   - `PageFactory`
   - `PageRegistry`
   - `LoginFrame`
   - `DashboardFrame`
4. Chuyen toan bo ha tang dashboard vao `View.features.dashboard`
5. Chuyen code that tu `View.Pages` sang `View.features`
6. Xoa toan bo `View.Pages`
7. Dua `AnnouncementTable` vao `View.features.announcement` va xoa `View.Panels`
8. Dua `TableStyles` vao `View.Components`
9. Tach nhom component chi dung cho leftbar vao `View.Components.leftbar`

Buoc tiep theo hop ly:

1. Build va sua loi compile sau dot refactor
