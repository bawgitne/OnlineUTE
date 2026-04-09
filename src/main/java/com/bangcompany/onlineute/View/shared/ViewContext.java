/**
 * nơi chứa các controller để bên view gọi dùng
 */
package com.bangcompany.onlineute.View.shared;

import com.bangcompany.onlineute.Controller.*;

public class ViewContext {
    private final AccountController accountController;
    private final AuthController authController;
    private final StudentController studentController;
    private final LecturerController lecturerController;
    private final FacultyController facultyController;
    private final MajorController majorController;
    private final ClassController classController;
    private final CourseController courseController;
    private final TermController termController;
    private final RegistrationBatchController registrationBatchController;
    private final CourseSectionController courseSectionController;
    private final CourseRegistrationController courseRegistrationController;
    private final NotificationController notificationController;
    private final MarkController markController;
    private final UserProfileController userProfileController;
    private final ScheduleController scheduleController;

    public ViewContext(
            AccountController accountController,
            AuthController authController,
            StudentController studentController,
            LecturerController lecturerController,
            FacultyController facultyController,
            MajorController majorController,
            ClassController classController,
            CourseController courseController,
            TermController termController,
            RegistrationBatchController registrationBatchController,
            CourseSectionController courseSectionController,
            CourseRegistrationController courseRegistrationController,
            NotificationController notificationController,
            MarkController markController,
            UserProfileController userProfileController,
            ScheduleController scheduleController
    ) {
        this.accountController = accountController;
        this.authController = authController;
        this.studentController = studentController;
        this.lecturerController = lecturerController;
        this.facultyController = facultyController;
        this.majorController = majorController;
        this.classController = classController;
        this.courseController = courseController;
        this.termController = termController;
        this.registrationBatchController = registrationBatchController;
        this.courseSectionController = courseSectionController;
        this.courseRegistrationController = courseRegistrationController;
        this.notificationController = notificationController;
        this.markController = markController;
        this.userProfileController = userProfileController;
        this.scheduleController = scheduleController;
    }

    public AccountController getAccountController() { return accountController; }
    public AuthController getAuthController() { return authController; }
    public StudentController getStudentController() { return studentController; }
    public LecturerController getLecturerController() { return lecturerController; }
    public FacultyController getFacultyController() { return facultyController; }
    public MajorController getMajorController() { return majorController; }
    public ClassController getClassController() { return classController; }
    public CourseController getCourseController() { return courseController; }
    public TermController getTermController() { return termController; }
    public RegistrationBatchController getRegistrationBatchController() { return registrationBatchController; }
    public CourseSectionController getCourseSectionController() { return courseSectionController; }
    public CourseRegistrationController getCourseRegistrationController() { return courseRegistrationController; }
    public NotificationController getNotificationController() { return notificationController; }
    public MarkController getMarkController() { return markController; }
    public UserProfileController getUserProfileController() { return userProfileController; }
    public ScheduleController getScheduleController() { return scheduleController; }
}
