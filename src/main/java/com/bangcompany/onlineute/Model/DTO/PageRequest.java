package com.bangcompany.onlineute.Model.DTO;

public class PageRequest {
    private final int page;
    private final int pageSize;

    public PageRequest(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
