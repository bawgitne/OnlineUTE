package com.bangcompany.onlineute.View.Pages;

/**
 * Refreshable - Interface for pages that need to reload data when displayed.
 */
public interface Refreshable {
    /**
     * Called by MainView whenever this page is shown.
     */
    void onEnter();
}
