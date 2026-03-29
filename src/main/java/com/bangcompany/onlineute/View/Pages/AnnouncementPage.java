package com.bangcompany.onlineute.View.Pages;

import com.bangcompany.onlineute.View.Containers.AnnouncementTable;
import javax.swing.*;
import java.awt.*;

public class AnnouncementPage extends JPanel {
    public AnnouncementPage() {
        setLayout(new BorderLayout());
        add(new AnnouncementTable(), BorderLayout.CENTER);
    }
}
