package io.github.fermarcgom.dto;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class CardPageResponse {

    private List<CardResponse> content;
    private int size;
    private long totalSize;
    private int page;
    private int totalPages;

    public List<CardResponse> getContent() {
        return content;
    }

    public void setContent(List<CardResponse> content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

