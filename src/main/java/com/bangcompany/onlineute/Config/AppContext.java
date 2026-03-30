package com.bangcompany.onlineute.Config;

import com.bangcompany.onlineute.Controller.*;
import com.bangcompany.onlineute.DAO.*;
import com.bangcompany.onlineute.DAO.Impl.*;
import com.bangcompany.onlineute.Service.*;
import com.bangcompany.onlineute.Service.Impl.*;

public final class AppContext {

    // DAOs
    public static AccountDAO accountDAO;
    public static StudentDAO studentDAO;
    public static LecturerDAO lecturerDAO;
    public static AdminDAO adminDAO;
    public static ScheduleDAO scheduleDAO;
    public static TermDAO termDAO;
    public static CourseDAO courseDAO;
    public static CourseSectionDAO courseSectionDAO;
    public static CourseRegistrationDAO courseRegistrationDAO;
    public static MarkDAO markDAO;
    public static AnnouncementDAO announcementDAO;
    public static ClassDAO classDAO;
    public static StudyProgramDAO studyProgramDAO;
    public static UserProfileDAO userProfileDAO;
    public static RegistrationBatchDAO registrationBatchDAO;
    // Services
    public static AccountService accountService;
    public static AuthService authService;
    public static StudentService studentService;
    public static ScheduleService scheduleService;
    public static TermService termService;
    public static CourseService courseService;
    public static CourseSectionService courseSectionService;
    public static CourseRegistrationService courseRegistrationService;
    public static MarkService markService;
    public static AnnouncementService announcementService;
    public static ClassService classService;
    public static StudyProgramService studyProgramService;
    public static UserProfileService userProfileService;
    public static RegistrationBatchService registrationBatchService;
    // Controllers
    public static AccountController accountController;
    public static AuthController authController;
    public static TermController termController;
    public static StudentController studentController;
    public static CourseController courseController;
    public static CourseSectionController courseSectionController;
    public static CourseRegistrationController courseRegistrationController;
    public static MarkController markController;
    public static NotificationController notificationController;
    public static UserProfileController userProfileController;
    public static RegistrationBatchController registrationBatchController;

    private AppContext() {}

    public static void init() {
        System.out.println("Building App Context (Singleton Container)...");

        // 1. DAOs
        accountDAO = new AccountDAOImpl();
        studentDAO = new StudentDAOImpl();
        lecturerDAO = new LecturerDAOImpl();
        adminDAO = new AdminDAOImpl();
        scheduleDAO = new ScheduleDAOImpl();
        termDAO = new TermDAOImpl();
        courseDAO = new CourseDAOImpl();
        courseSectionDAO = new CourseSectionDAOImpl();
        courseRegistrationDAO = new CourseRegistrationDAOImpl();
        markDAO = new MarkDAOImpl();
        announcementDAO = new AnnouncementDAOImpl();
        classDAO = new ClassDAOImpl();
        studyProgramDAO = new StudyProgramDAOImpl();
        userProfileDAO = new UserProfileDAOImpl();
        registrationBatchDAO = new RegistrationBatchDAOImpl();
        // 2. Services
        accountService = new AccountServiceImpl(studentDAO, lecturerDAO, adminDAO);
        authService = new AuthServiceImpl(accountDAO, studentDAO, lecturerDAO, adminDAO);
        studentService = new StudentServiceImpl(studentDAO);
        scheduleService = new ScheduleServiceImpl(scheduleDAO);
        termService = new TermServiceImpl(termDAO);
        courseService = new CourseServiceImpl(courseDAO);
        courseSectionService = new CourseSectionServiceImpl(courseSectionDAO, scheduleService);
        courseRegistrationService = new CourseRegistrationServiceImpl(courseRegistrationDAO, studentDAO, courseSectionDAO);
        markService = new MarkServiceImpl(markDAO);
        announcementService = new AnnouncementServiceImpl(announcementDAO);
        classService = new ClassServiceImpl(classDAO);
        studyProgramService = new StudyProgramServiceImpl(studyProgramDAO);
        userProfileService = new UserProfileServiceImpl(userProfileDAO);
        registrationBatchService = new RegistrationBatchServiceImpl(registrationBatchDAO);
        // 3. Controllers
        accountController = new AccountController(accountService);
        authController = new AuthController(authService);
        termController = new TermController(termService);
        studentController = new StudentController(studentService);
        courseController = new CourseController(courseService);
        courseSectionController = new CourseSectionController(courseSectionService);
        courseRegistrationController = new CourseRegistrationController(courseRegistrationService);
        markController = new MarkController(markService);
        notificationController = new NotificationController(announcementService, courseSectionService);
        userProfileController = new UserProfileController(userProfileService);
        registrationBatchController = new RegistrationBatchController(registrationBatchService);

        System.out.println("Bean initialization completed successfully.");
    }

    public static ScheduleService getScheduleService() { return scheduleService; }
    public static CourseService getCourseService() { return courseService; }
    public static CourseSectionService getCourseSectionService() { return courseSectionService; }
    public static CourseRegistrationService getCourseRegistrationService() { return courseRegistrationService; }
    public static CourseSectionController getCourseSectionController() { return courseSectionController; }
    public static CourseRegistrationController getCourseRegistrationController() { return courseRegistrationController; }
    public static LecturerDAO getLecturerDAO() { return lecturerDAO; }
    public static MarkService getMarkService() { return markService; }
    public static AnnouncementService getAnnouncementService() { return announcementService; }
    public static NotificationController getNotificationController() { return notificationController; }
    public static TermService getTermService() { return termService; }
    public static StudentService getStudentService() { return studentService; }
    public static AuthService getAuthService() { return authService;}
    public static AccountService getAccountService() { return accountService; }
    public static AccountController getAccountController() { return accountController; }
    public static ClassService getClassService() { return classService; }
    public static StudyProgramService getStudyProgramService() { return studyProgramService; }
    public static UserProfileController getUserProfileController() { return userProfileController; }
    public static RegistrationBatchService getRegistrationBatchService() { return registrationBatchService; }
    public static RegistrationBatchController getRegistrationBatchController() { return registrationBatchController; }
}
