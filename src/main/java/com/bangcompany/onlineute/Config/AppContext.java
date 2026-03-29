package com.bangcompany.onlineute.Config;

// DAOs
import com.bangcompany.onlineute.DAO.*;
import com.bangcompany.onlineute.DAO.Impl.*;

// Services
import com.bangcompany.onlineute.Service.*;
import com.bangcompany.onlineute.Service.Impl.*;

// Controllers
import com.bangcompany.onlineute.Controller.*;

/**
 * AppContext - A manual Dependency Injection (DI) Container.
 */
public final class AppContext {
    
    // --- Bean Container ---
    
    // DAOs
    public static AccountDAO accountDAO;
    public static StudentDAO studentDAO;
    public static LecturerDAO lecturerDAO;
    public static AdminDAO adminDAO;
    public static ScheduleDAO scheduleDAO;
    public static TermDAO termDAO;
    public static CourseSectionDAO courseSectionDAO;
    public static CourseRegistrationDAO courseRegistrationDAO;
    public static MarkDAO markDAO;

    // Services
    public static AuthService authService;
    public static StudentService studentService;
    public static ScheduleService scheduleService;
    public static TermService termService;
    public static CourseSectionService courseSectionService;
    public static CourseRegistrationService courseRegistrationService;
    public static MarkService markService;

    // Controllers
    public static AuthController authController;
    public static TermController termController;
    public static StudentController studentController;
    public static CourseSectionController courseSectionController;
    public static CourseRegistrationController courseRegistrationController;
    public static MarkController markController;

    private AppContext() {}

    public static void init() {
        System.out.println("Building App Context (Singleton Container)...");

        // 1. Instantiate DAOs
        accountDAO = new AccountDAOImpl();
        studentDAO = new StudentDAOImpl();
        lecturerDAO = new LecturerDAOImpl();
        adminDAO = new AdminDAOImpl();
        scheduleDAO = new ScheduleDAOImpl();
        termDAO = new TermDAOImpl();
        courseSectionDAO = new CourseSectionDAOImpl();
        courseRegistrationDAO = new CourseRegistrationDAOImpl();
        markDAO = new MarkDAOImpl();

        // 2. Instantiate Services (Injecting DAOs)
        authService = new AuthServiceImpl(accountDAO, studentDAO, lecturerDAO, adminDAO);
        studentService = new StudentServiceImpl(studentDAO);
        scheduleService = new ScheduleServiceImpl(scheduleDAO);
        termService = new TermServiceImpl(termDAO);
        courseSectionService = new CourseSectionServiceImpl(courseSectionDAO);
        courseRegistrationService = new CourseRegistrationServiceImpl(courseRegistrationDAO);
        markService = new MarkServiceImpl(markDAO);

        // 3. Instantiate Controllers (Injecting Services)
        authController = new AuthController(authService);
        termController = new TermController(termService);
        studentController = new StudentController(studentService);
        courseSectionController = new CourseSectionController(courseSectionService);
        courseRegistrationController = new CourseRegistrationController(courseRegistrationService);
        markController = new MarkController(markService);

        System.out.println("Bean initialization completed successfully.");
    }
    
    // Getters for convenience (or direct access)
    public static ScheduleService getScheduleService() { return scheduleService; }
    public static CourseSectionService getCourseSectionService() { return courseSectionService; }
    public static CourseRegistrationService getCourseRegistrationService() { return courseRegistrationService; }
    public static MarkService getMarkService() { return markService; }
}
