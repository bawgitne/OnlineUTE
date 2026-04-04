package com.bangcompany.onlineute.Model.DTO;

import java.util.List;

public class PagedResult<T> {
    private final List<T> items;
    private final int page;
    private final int pageSize;
    private final long totalItems;

    public PagedResult(List<T> items, int page, int pageSize, long totalItems) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        if (pageSize <= 0 || totalItems <= 0) {
            return totalItems > 0 ? 1 : 0;
        }
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public boolean hasPrevious() {
        return page > 1;
    }

    public boolean hasNext() {
        return page < getTotalPages();
    }
}
