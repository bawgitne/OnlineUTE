package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.View.Containers.AnnouncementTable;
import javax.swing.*;
import java.awt.*;

public class AnnouncementPage extends JPanel implements Refreshable {
    private final AnnouncementTable announcementTable;

    public AnnouncementPage() {
        setLayout(new BorderLayout());
        announcementTable = new AnnouncementTable();
        add(announcementTable, BorderLayout.CENTER);
    }

    @Override
    public void onEnter() {
        announcementTable.refreshData();
    }
}
