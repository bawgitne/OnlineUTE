package com.bangcompany.onlineute.view.layout;

import com.bangcompany.onlineute.Model.EnumType.MenuItem;

import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends com.bangcompany.onlineute.View.Containers.Sidebar {
    public Sidebar(String userName, String userId, String roleDisplayName, List<MenuItem> menuItems, Consumer<String> onNavigate) {
        super(userName, userId, roleDisplayName, menuItems, onNavigate);
    }
}
