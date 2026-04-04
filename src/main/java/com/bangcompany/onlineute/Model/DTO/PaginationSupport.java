package com.bangcompany.onlineute.Model.DTO;

import java.util.List;

public final class PaginationSupport {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private PaginationSupport() {
    }

    public static PageRequest normalize(int page, int pageSize) {
        int safePage = Math.max(page, DEFAULT_PAGE);
        int safePageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        return new PageRequest(safePage, safePageSize);
    }

    public static int offset(PageRequest pageRequest) {
        return Math.max(0, (pageRequest.getPage() - 1) * pageRequest.getPageSize());
    }

    public static <T> PagedResult<T> empty(PageRequest pageRequest) {
        return new PagedResult<>(List.of(), pageRequest.getPage(), pageRequest.getPageSize(), 0);
    }

    public static <T> PagedResult<T> of(List<T> items, PageRequest pageRequest, long totalItems) {
        return new PagedResult<>(items, pageRequest.getPage(), pageRequest.getPageSize(), totalItems);
    }
}
