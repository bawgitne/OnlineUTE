# Cong dung cac component

Luu y: Mo ta ngan gon muc dich va noi dung chinh. Viet khong dau de tranh loi UTF-8.

## Components (tong quan)

PrimaryButton
- Nut chinh dung chung.
- Ho tro set mau nen, mau chu, font dam, bo goc, cursor.
- Dung cho cac nut hanh dong chinh (Luu, Tao moi, Dang ky...).
\nPrimaryTable
- Table giao dien moi (khong dung JTable), render bang cac panel.
- Ho tro header bo goc, danh sach row doc, pager < >, row selection.
- Dung cho cac bang display thong tin (Thong bao, Quan ly, Dang ky...).

PaginationPanel
- Panel phan trang co label trang va nut truoc/sau.
- Dung cho cac bang can phan trang neu khong dung PrimaryTable pager.

SearchActionTopbar
- Thanh tim kiem + nut tao moi (neu co).
- Co debounce, hien/ẩn nut xoa, su dung cho cac trang quan ly.

EntityTablePanel
- Bang hien thi danh sach entity (dua tren PrimaryTable).
- Co result label + pager, phu hop cho trang quan ly.

SimpleManagementDashboard
- Card thong ke + guide text cho trang quan ly.
- Dung lam man hinh tong quan khi chua tim kiem.

ManagementShellPage
- Khung trang quan ly (topbar + khu vuc dashboard/ket qua).
- Tu dong chuyen trang theo tu khoa tim kiem.

PageTitleLabel
- The tieu de nho bo goc, nen trang, chu xanh.
- Dung o dau trang (thay cho PageScaffold).

PrimaryCard
- Khung bo goc, nen trang, vien nhe, padding tuy chinh.
- Dung bao boc noi dung (table, form, block thong tin).

FormSectionCard
- Khung bo goc cho nhom field trong form.
- Dung gop nhieu row form co khoang cach.

FormRow
- Tao hang form 1/2/3 cot.
- Dung cho layout input can canh cot.

InputGroup
- TextField co label tren vien (RoundedTitleBorder).
- Dung nhap gia tri text, mat khau.

SelectGroup
- ComboBox co label tren vien (RoundedTitleBorder).
- Dung chon gia tri tu danh sach.

TextAreaGroup
- TextArea co label tren vien (RoundedTitleBorder).
- Dung nhap doan van/ghi chu.

TagChip
- The nho de hien thi thong tin ngan (Email, Lop...).

LabelValuePanel
- Cap label + value cho trang Profile.

EmptyStatePanel
- Panel thong bao khi khong co du lieu.

AppLogoHeader
- Header logo + ten truong (man hinh dang nhap).

TableStyles
- Bo style JTable/JScrollPane (neu con dung JTable).

## Leftbar

NavMenu
- Danh sach menu ben trai.

LeftBarButton
- Nut menu ben trai co icon, trang thai active, hover.

LeftBarTitle
- Tieu de nhom menu ben trai.

UserProfileCard
- Thong tin nguoi dung o dau leftbar.

SidebarItem
- DTO tao menu (tab/title).

## Theme / Paint / Utils

AppTheme
- Tap trung mau sac va font dung chung.

DateUtils
- Chuyen so thu sang text (Thu 2..Chu nhat).

SwingUtils
- Tien ich tao scroll an thanh cuon.

RoundedPanel
- Panel ve nen bo goc.

RoundedOutlineBorder
- Vien bo goc.

RoundedTitleBorder
- Vien co label nam tren border.

RoundedPainter
- Ve nen bo goc cho button.
