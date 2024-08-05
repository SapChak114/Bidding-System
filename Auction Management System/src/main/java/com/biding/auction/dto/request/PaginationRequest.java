package com.biding.auction.dto.request;

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
    private String productName;
    private String userEmail;
    private String userContact;
}