package com.tatva.task.xmlparsingrestapi.dto;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/* Pagination DTO for specific default paging values.*/
public class PaginationRequestDTO
{
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";

    public PaginationRequestDTO(int page, int size, String sortBy) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public static Sort createSortFromSortBy(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            // If sortBy is null or empty, return the default Sort by id
            return Sort.by("id");
        }

        // Split the comma-separated string by commas
        String[] properties = sortBy.split(",");

        // Create a list of Sort.Order objects based on the properties
        List<Sort.Order> orders = new ArrayList<>();
        for (String property : properties) {
            Sort.Order order = Sort.Order.by(property.trim()); // Trim each property to remove leading/trailing spaces
            orders.add(order);
        }

        // Create a Sort object from the list of Sort.Order objects
        return Sort.by(orders);
    }

}
