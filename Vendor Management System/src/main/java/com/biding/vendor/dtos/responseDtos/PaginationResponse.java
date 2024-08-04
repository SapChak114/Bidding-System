package com.biding.vendor.dtos.responseDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(builderClassName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationResponse<T> {
    int size;

    int totalNoPages;

    int currentPage;

    String sortBy;

    String sortByType;

    Long totalElements;

    List<T> content;
}
