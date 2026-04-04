package com.bangcompany.onlineute.View.features.student;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Student;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.data.ManagementShellPage;

import javax.swing.*;
import java.awt.*;

public class StudentManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final StudentManagementDashboard dashboardPanel;
    private final StudentSearchResultPanel searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";

    public StudentManagementPage() {
        this(new StudentManagementDashboard());
    }

    private StudentManagementPage(StudentManagementDashboard dashboardPanel) {
        super(
                "Tìm theo mã sinh viên, họ tên, email, lớp, khoa",
                null,
                null,
                2,
                dashboardPanel,
                new StudentSearchResultPanel(direction -> {})
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (StudentSearchResultPanel) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        configureCreateAction("Tạo mới", this::openCreateStudentDialog);
    }

    @Override
    protected void onKeywordActivated(String keyword) {
        currentKeyword = keyword;
        currentPage = 1;
        loadCurrentPage();
    }

    @Override
    protected void onKeywordCleared() {
        currentKeyword = "";
        currentPage = 1;
        dashboardPanel.refreshData();
    }

    private void loadCurrentPage() {
        PagedResult<Student> result = AppContext.getStudentController()
                .searchStudents(currentKeyword, currentPage, PAGE_SIZE);
        searchResultPanel.showResults(result, currentKeyword);
        showResults();
    }

    private void changePage(int direction) {
        int nextPage = currentPage + direction;
        if (nextPage < 1) {
            return;
        }
        currentPage = nextPage;
        loadCurrentPage();
    }

    private void openCreateStudentDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tạo mới sinh viên", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Sinh viên"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }
}
