package com.bangcompany.onlineute.View.features.lecturer;

import com.bangcompany.onlineute.Config.AppContext;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Lecturer;
import com.bangcompany.onlineute.View.features.account.CreateAccountPage;
import com.bangcompany.onlineute.View.features.data.ManagementShellPage;

import javax.swing.*;
import java.awt.*;

public class LecturerManagementPage extends ManagementShellPage {
    private static final int PAGE_SIZE = 20;

    private final LecturerManagementDashboard dashboardPanel;
    private final LecturerSearchResultPanel searchResultPanel;

    private int currentPage = 1;
    private String currentKeyword = "";

    public LecturerManagementPage() {
        this(new LecturerManagementDashboard());
    }

    private LecturerManagementPage(LecturerManagementDashboard dashboardPanel) {
        super(
                "Tìm theo mã giảng viên, họ tên",
                null,
                null,
                2,
                dashboardPanel,
                new LecturerSearchResultPanel(direction -> {})
        );
        this.dashboardPanel = dashboardPanel;
        this.searchResultPanel = (LecturerSearchResultPanel) getResultComponent();
        this.searchResultPanel.setPageHandler(this::changePage);
        configureCreateAction("Tạo mới", this::openCreateLecturerDialog);
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
        PagedResult<Lecturer> result = AppContext.getLecturerController()
                .searchLecturers(currentKeyword, currentPage, PAGE_SIZE);
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

    private void openCreateLecturerDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Tạo mới giảng viên", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(new CreateAccountPage("Giảng viên"));
        dialog.setSize(1100, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        onEnter();
    }
}
