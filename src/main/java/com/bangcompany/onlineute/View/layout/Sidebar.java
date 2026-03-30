package com.bangcompany.onlineute.view.layout;

import com.bangcompany.onlineute.View.Components.SidebarItem;

import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends com.bangcompany.onlineute.View.Containers.Sidebar {
    public Sidebar(String userName, String userId, String roleDisplayName, List<SidebarItem> menuItems, Consumer<String> onNavigate) {
        super(userName, userId, roleDisplayName, menuItems, onNavigate);
    }
}
