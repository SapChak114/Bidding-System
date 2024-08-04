package com.biding.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class PaginationRequest {
    private int offset;
    private int pageSize;
    private String field;
    private String sort;
    private String name;
    private String email;
    private String contact;
}
