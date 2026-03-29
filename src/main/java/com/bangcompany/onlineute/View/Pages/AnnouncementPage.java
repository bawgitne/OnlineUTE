package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.View.Containers.AnnouncementTable;
import com.bangcompany.onlineute.View.shared.components.PageScaffold;

import javax.swing.*;

public class AnnouncementPage extends JPanel implements Refreshable {
    private final AnnouncementTable announcementTable;

    public AnnouncementPage() {
        setLayout(new java.awt.BorderLayout());
        announcementTable = new AnnouncementTable();

        PageScaffold scaffold = new PageScaffold("Thong Bao");
        scaffold.setBody(announcementTable);
        add(scaffold, java.awt.BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        announcementTable.refreshData();
    }
}
