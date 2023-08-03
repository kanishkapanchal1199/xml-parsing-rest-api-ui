package com.tatva.task.xmlparsingrestapi.dto;

import org.junit.Test;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.assertEquals;

public class PaginationRequestDTOTest {

    @Test
    public void testPaginationRequestDTOGetterSetter() {
        int page = 2;
        int size = 20;
        String sortBy = "id,username,email";

        PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(page, size, sortBy);


        assertEquals(page, paginationRequestDTO.getPage());
        assertEquals(size, paginationRequestDTO.getSize());
        assertEquals(sortBy, paginationRequestDTO.getSortBy());
    }

    @Test
    public void testCreateSortFromSortBy() {
        String sortBy = "id,username,email";
        Sort expectedSort = Sort.by(
                Sort.Order.by("id"),
                Sort.Order.by("username"),
                Sort.Order.by("email")
        );

        Sort resultSort = PaginationRequestDTO.createSortFromSortBy(sortBy);

        assertEquals(expectedSort, resultSort);
    }

    @Test
    public void testCreateSortFromSortByWithEmptyString() {
        String sortBy = "";
        Sort expectedSort = Sort.by(Sort.Order.by("id"));

        Sort resultSort = PaginationRequestDTO.createSortFromSortBy(sortBy);

        assertEquals(expectedSort, resultSort);
    }

    @Test
    public void testCreateSortFromSortByWithSingleProperty() {
        String sortBy = "username";
        Sort expectedSort = Sort.by(Sort.Order.by("username"));

        Sort resultSort = PaginationRequestDTO.createSortFromSortBy(sortBy);

        assertEquals(expectedSort, resultSort);
    }

    @Test
    public void testPaginationRequestDTOSetterMethods() {
        int page = 2;
        int size = 20;
        String sortBy = "id,username,email";

        PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(0, 10, "id");


        paginationRequestDTO.setPage(page);
        paginationRequestDTO.setSize(size);
        paginationRequestDTO.setSortBy(sortBy);


        assertEquals(page, paginationRequestDTO.getPage());
        assertEquals(size, paginationRequestDTO.getSize());
        assertEquals(sortBy, paginationRequestDTO.getSortBy());
    }
}
