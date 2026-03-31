package com.bangcompany.onlineute.View.Components.leftbar;

public class SidebarItem {
    private final boolean title;
    private final String key;
    private final String label;
    private final String icon;

    private SidebarItem(boolean title, String key, String label, String icon) {
        this.title = title;
        this.key = key;
        this.label = label;
        this.icon = icon;
    }

    public static SidebarItem title(String label) {
        return new SidebarItem(true, null, label, null);
    }

    public static SidebarItem tab(String key, String label, String icon) {
        return new SidebarItem(false, key, label, icon);
    }

    public boolean isTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }
}
