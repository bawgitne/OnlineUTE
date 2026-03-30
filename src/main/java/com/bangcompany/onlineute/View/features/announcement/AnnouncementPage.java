package com.bangcompany.onlineute.View.features.announcement;

import com.bangcompany.onlineute.View.features.dashboard.PageScaffold;
import com.bangcompany.onlineute.View.navigation.Refreshable;

import javax.swing.*;
import java.awt.*;

public class AnnouncementPage extends JPanel implements Refreshable {
    private final AnnouncementTable announcementTable;

    public AnnouncementPage() {
        setLayout(new BorderLayout());
        announcementTable = new AnnouncementTable();

        PageScaffold scaffold = new PageScaffold("Thông báo");
        scaffold.setBody(announcementTable);
        add(scaffold, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        announcementTable.refreshData();
    }
}

