# Component Tree (chi tiet ca component tai su dung + khung UI thuong)

Luu y:
- "Component tai su dung" la class co san trong View/Components hoac View/features.
- "Khung UI thuong" la JLabel/JPanel/JScrollPane/JTabbedPane/JSplitPane/JTable... va border/fill.
- Viet theo so do cay, dung lai khi khong con component con.

## Phan 1/5 - Auth + Account + Announcement + Attendance

LoginScreen
- Khung UI thuong
  - JPanel (cardHolder)
- Component tai su dung
  - AppLogoHeader
  - LoginForm
    - InputGroup (username)
    - InputGroup (password)
    - PrimaryButton

LoginForm
- Component tai su dung
  - InputGroup
  - PrimaryButton
- Khung UI thuong
  - JLabel (loginTitle)
  - JLabel (subTitle)
  - JSeparator
  - JLabel (forgotPassLabel)
  - Border (CompoundBorder: LineBorder + EmptyBorder)

ChangePasswordPage
- Khung UI thuong
  - JPanel (topPanel)
  - JPanel (centerPanel)
  - JPanel (form) -> the trang mau trang lon nhat
  - Border (centerPanel: LineBorder)
  - Border (form: CompoundBorder LineBorder mau xanh + EmptyBorder)
  - JLabel (title) -> tieu de tren form
  - JLabel (subTitle) -> dong mo ta nho
- Component tai su dung
  - PageTitleLabel (title tren cung trang)
  - InputGroup x3
  - PrimaryButton

AnnouncementPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - AnnouncementTable
      - TableStyles (applyModernTable + styleScrollPane)
- Khung UI thuong
  - JTable
  - JScrollPane

AnnouncementTable
- Component tai su dung
  - TableStyles (applyModernTable + styleScrollPane)
- Khung UI thuong
  - JTable
  - JScrollPane
  - DefaultTableModel

CreateAnnouncementPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - SwingUtils.hiddenScrollPane
      - InputGroup
      - SelectGroup
      - TextAreaGroup
      - PrimaryButton
- Khung UI thuong
  - JPanel (mainPanel)
  - GridBagLayout

AttendancePage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - TableStyles (applyModernTable + centerColumns + styleScrollPane)
- Khung UI thuong
  - JTable
  - JScrollPane
  - DefaultTableModel

## Phan 2/5 - Dashboard shell + Leftbar

DashboardLayout
- Component tai su dung
  - Sidebar
    - UserProfileCard
    - NavMenu
      - LeftBarTitle
      - LeftBarButton
  - TopHeader
  - MainContent
- Khung UI thuong
  - JPanel (headerWrapper)

MainContent
- Component tai su dung
  - Refreshable (interface hook)
- Khung UI thuong
  - CardLayout (JPanel)

Sidebar
- Component tai su dung
  - UserProfileCard
  - NavMenu
    - LeftBarTitle
    - LeftBarButton
- Khung UI thuong
  - JScrollPane (menuScrollPane)
  - JButton (logout)

LeftBarButton
- Component tai su dung
  - RoundedPainter

TopHeader
- Component tai su dung
  - AppTheme
- Khung UI thuong
  - JLabel (title)

## Phan 3/5 - Data management + Student/Lecturer + Generic management

DataManagementPage
- Component tai su dung
  - PageTitleLabel
  - StudentManagementPage
  - LecturerManagementPage
  - SimpleEntityManagementPage<Faculty>
  - SimpleEntityManagementPage<Major>
  - SimpleEntityManagementPage<Class>
  - SimpleEntityManagementPage<Course>
- Khung UI thuong
  - JTabbedPane

StudentManagementPage (extends ManagementShellPage)
- Component tai su dung
  - SearchActionTopbar
    - PrimaryButton (create)
  - SimpleManagementDashboard
  - EntityTablePanel<Student>
    - TableStyles
    - PaginationPanel
- Khung UI thuong
  - JDialog (CreateAccountPage)

LecturerManagementPage (extends ManagementShellPage)
- Component tai su dung
  - SearchActionTopbar
    - PrimaryButton (create)
  - SimpleManagementDashboard
  - EntityTablePanel<Lecturer>
    - TableStyles
    - PaginationPanel

SimpleEntityManagementPage<T> (extends ManagementShellPage)
- Component tai su dung
  - SearchActionTopbar
    - PrimaryButton (optional)
  - SimpleManagementDashboard
  - EntityTablePanel<T>
    - TableStyles
    - PaginationPanel (optional)

ManagementShellPage (base)
- Component tai su dung
  - SearchActionTopbar
  - Refreshable
- Khung UI thuong
  - CardLayout

EntityTablePanel
- Component tai su dung
  - TableStyles
  - PaginationPanel (optional)
- Khung UI thuong
  - JTable
  - JScrollPane
  - JLabel (result)

SimpleManagementDashboard
- Component tai su dung
  - AppTheme
  - RoundedOutlineBorder
- Khung UI thuong
  - JPanel (summary cards)
  - JLabel (title/value)

## Phan 4/5 - Registration + Schedule

CourseRegistrationPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - TableStyles
    - PrimaryButton
- Khung UI thuong
  - JSplitPane
  - JList
  - JTable
  - JScrollPane

CreateRegistrationBatchPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - InputGroup
    - SelectGroup
    - PrimaryButton
    - TableStyles
- Khung UI thuong
  - JTable
  - JScrollPane

CourseSectionDialog
- Component tai su dung
  - PageTitleLabel
  - InputGroup
  - SelectGroup
  - FormRow
  - PrimaryButton
  - TableStyles
- Khung UI thuong
  - JSplitPane
  - JTable
  - JScrollPane

SchedulePage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - PrimaryButton
- Khung UI thuong
  - JScrollPane
  - JPanel (grid)

## Phan 5/5 - Grades + Profile

InputGradesPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - TableStyles
    - PrimaryButton
    - EmptyStatePanel
- Khung UI thuong
  - JTabbedPane
  - JTable
  - JScrollPane

ViewGradesPage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - TableStyles
- Khung UI thuong
  - JTable
  - JScrollPane

ProfilePage
- Component tai su dung
  - PageScaffold
    - PageTitleLabel
    - SwingUtils.hiddenScrollPane
    - TagChip
    - ProfileSectionCard
      - LabelValuePanel
- Khung UI thuong
  - JPanel (summary card)
  - JLabel (name/role)

ProfileSectionCard
- Component tai su dung
  - LabelValuePanel
- Khung UI thuong
  - JLabel (section title)

EmptyStatePanel
- Component tai su dung
  - AppTheme
- Khung UI thuong
  - JLabel

TagChip
- Component tai su dung
  - AppTheme
- Khung UI thuong
  - JLabel
