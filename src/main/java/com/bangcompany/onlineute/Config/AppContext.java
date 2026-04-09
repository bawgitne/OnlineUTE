package com.bangcompany.onlineute.Config;

import com.bangcompany.onlineute.Controller.*;
import com.bangcompany.onlineute.DAO.*;
import com.bangcompany.onlineute.DAO.Impl.*;
import com.bangcompany.onlineute.Service.*;
import com.bangcompany.onlineute.Service.Impl.*;

public final class AppContext {

    public final AccountController accountController;
    public final AuthController authController;
    public final TermController termController;
    public final StudentController studentController;
    public final LecturerController lecturerController;
    public final CourseController courseController;
    public final CourseSectionController courseSectionController;
    public final CourseRegistrationController courseRegistrationController;
    public final MarkController markController;
    public final NotificationController notificationController;
    public final ScheduleController scheduleController;
    public final ClassController classController;
    public final FacultyController facultyController;
    public final MajorController majorController;
    public final UserProfileController userProfileController;
    public final RegistrationBatchController registrationBatchController;

    private AppContext(
            AccountController accountController,
            AuthController authController,
            TermController termController,
            StudentController studentController,
            LecturerController lecturerController,
            CourseController courseController,
            CourseSectionController courseSectionController,
            CourseRegistrationController courseRegistrationController,
            MarkController markController,
            NotificationController notificationController,
            ScheduleController scheduleController,
            ClassController classController,
            FacultyController facultyController,
            MajorController majorController,
            UserProfileController userProfileController,
            RegistrationBatchController registrationBatchController
    ) {
        this.accountController = accountController;
        this.authController = authController;
        this.termController = termController;
        this.studentController = studentController;
        this.lecturerController = lecturerController;
        this.courseController = courseController;
        this.courseSectionController = courseSectionController;
        this.courseRegistrationController = courseRegistrationController;
        this.markController = markController;
        this.notificationController = notificationController;
        this.scheduleController = scheduleController;
        this.classController = classController;
        this.facultyController = facultyController;
        this.majorController = majorController;
        this.userProfileController = userProfileController;
        this.registrationBatchController = registrationBatchController;
    }

    public static AppContext init() {
        System.out.println("Building App Context (Container)...");

        AccountDAO accountDAO = new AccountDAOImpl();
        StudentDAO studentDAO = new StudentDAOImpl();
        LecturerDAO lecturerDAO = new LecturerDAOImpl();
        AdminDAO adminDAO = new AdminDAOImpl();
        ScheduleDAO scheduleDAO = new ScheduleDAOImpl();
        TermDAO termDAO = new TermDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();
        CourseSectionDAO courseSectionDAO = new CourseSectionDAOImpl();
        CourseRegistrationDAO courseRegistrationDAO = new CourseRegistrationDAOImpl();
        MarkDAO markDAO = new MarkDAOImpl();
        AnnouncementDAO announcementDAO = new AnnouncementDAOImpl();
        ClassDAO classDAO = new ClassDAOImpl();
        FacultyDAO facultyDAO = new FacultyDAOImpl();
        MajorDAO majorDAO = new MajorDAOImpl();
        UserProfileDAO userProfileDAO = new UserProfileDAOImpl();
        RegistrationBatchDAO registrationBatchDAO = new RegistrationBatchDAOImpl();

        AccountService accountService = new AccountServiceImpl(accountDAO, studentDAO, lecturerDAO, adminDAO);
        AuthService authService = new AuthServiceImpl(accountDAO, studentDAO, lecturerDAO, adminDAO);
        StudentService studentService = new StudentServiceImpl(studentDAO);
        LecturerService lecturerService = new LecturerServiceImpl(lecturerDAO);
        ScheduleService scheduleService = new ScheduleServiceImpl(scheduleDAO);
        TermService termService = new TermServiceImpl(termDAO);
        CourseService courseService = new CourseServiceImpl(courseDAO);
        AnnouncementService announcementService = new AnnouncementServiceImpl(announcementDAO);
        CourseSectionService courseSectionService = new CourseSectionServiceImpl(courseSectionDAO, scheduleService, courseRegistrationDAO, announcementService);
        CourseRegistrationService courseRegistrationService = new CourseRegistrationServiceImpl(courseRegistrationDAO, studentDAO, courseSectionDAO, markDAO);
        MarkService markService = new MarkServiceImpl(markDAO);
        ClassService classService = new ClassServiceImpl(classDAO);
        FacultyService facultyService = new FacultyServiceImpl(facultyDAO);
        MajorService majorService = new MajorServiceImpl(majorDAO);
        UserProfileService userProfileService = new UserProfileServiceImpl(userProfileDAO);
        RegistrationBatchService registrationBatchService = new RegistrationBatchServiceImpl(registrationBatchDAO);

        AccountController accountController = new AccountController(accountService);
        AuthController authController = new AuthController(authService);
        TermController termController = new TermController(termService);
        StudentController studentController = new StudentController(studentService);
        LecturerController lecturerController = new LecturerController(lecturerService);
        CourseController courseController = new CourseController(courseService);
        CourseSectionController courseSectionController = new CourseSectionController(courseSectionService);
        CourseRegistrationController courseRegistrationController = new CourseRegistrationController(courseRegistrationService);
        MarkController markController = new MarkController(markService);
        NotificationController notificationController = new NotificationController(announcementService, courseSectionService);
        ScheduleController scheduleController = new ScheduleController(scheduleService);
        ClassController classController = new ClassController(classService);
        FacultyController facultyController = new FacultyController(facultyService);
        MajorController majorController = new MajorController(majorService);
        UserProfileController userProfileController = new UserProfileController(userProfileService);
        RegistrationBatchController registrationBatchController = new RegistrationBatchController(registrationBatchService);

        System.out.println("Bean initialization completed successfully.");
        return new AppContext(
                accountController,
                authController,
                termController,
                studentController,
                lecturerController,
                courseController,
                courseSectionController,
                courseRegistrationController,
                markController,
                notificationController,
                scheduleController,
                classController,
                facultyController,
                majorController,
                userProfileController,
                registrationBatchController
        );
    }
}
