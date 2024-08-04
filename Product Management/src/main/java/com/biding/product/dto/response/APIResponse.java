package com.biding.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class APIResponse<T> {
    private T response;
    private String status;
    private int statusCode;
}
