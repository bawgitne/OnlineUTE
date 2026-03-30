package com.bangcompany.onlineute.View.Components;

public class SidebarItem {
    private final String key;
    private final String label;
    private final String icon;

    public SidebarItem(String key, String label, String icon) {
        this.key = key;
        this.label = label;
        this.icon = icon;
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
