package com.bidding.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class PaginationResponse<T> {
    private int size;
    private int totalNoPages;
    private int currentPage;
    private long totalElements;
    private String sortBy;
    private String sortByType;
    private List<T> content;
}