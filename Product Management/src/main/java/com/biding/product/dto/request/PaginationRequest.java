package com.biding.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationRequest {
    private int offset;
    private int pageSize;
    private String field;
    private String sort;
    private String name;
    private String description;
    private Double basePriceMin;
    private Double basePriceMax;
    private Long vendorId;
}
