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
    // Services
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
    // Controllers
    public static AuthController authController;
    public static TermController termController;
    public static StudentController studentController;
    public static CourseController courseController;
    public static CourseSectionController courseSectionController;
    public static CourseRegistrationController courseRegistrationController;
    public static MarkController markController;
    public static NotificationController notificationController;

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
        // 2. Services
        authService = new AuthServiceImpl(accountDAO, studentDAO, lecturerDAO, adminDAO);
        studentService = new StudentServiceImpl(studentDAO);
        scheduleService = new ScheduleServiceImpl(scheduleDAO);
        termService = new TermServiceImpl(termDAO);
        courseService = new CourseServiceImpl(courseDAO);
        courseSectionService = new CourseSectionServiceImpl(courseSectionDAO);
        courseRegistrationService = new CourseRegistrationServiceImpl(courseRegistrationDAO);
        markService = new MarkServiceImpl(markDAO);
        announcementService = new AnnouncementServiceImpl(announcementDAO);
        classService = new ClassServiceImpl(classDAO);
        studyProgramService = new StudyProgramServiceImpl(studyProgramDAO);
        // 3. Controllers
        authController = new AuthController(authService);
        termController = new TermController(termService);
        studentController = new StudentController(studentService);
        courseController = new CourseController(courseService);
        courseSectionController = new CourseSectionController(courseSectionService);
        courseRegistrationController = new CourseRegistrationController(courseRegistrationService);
        markController = new MarkController(markService);
        notificationController = new NotificationController(announcementService, courseSectionService);

        System.out.println("Bean initialization completed successfully.");
    }

    public static ScheduleService getScheduleService() { return scheduleService; }
    public static CourseService getCourseService() { return courseService; }
    public static CourseSectionService getCourseSectionService() { return courseSectionService; }
    public static CourseRegistrationService getCourseRegistrationService() { return courseRegistrationService; }
    public static MarkService getMarkService() { return markService; }
    public static AnnouncementService getAnnouncementService() { return announcementService; }
    public static NotificationController getNotificationController() { return notificationController; }
    public static TermService getTermService() { return termService; }
    public static StudentService getStudentService() { return studentService; }
    public static AuthService getAuthService() { return authService;}
    public static ClassService getClassService() { return classService; }
    public static StudyProgramService getStudyProgramService() { return studyProgramService; }
}
