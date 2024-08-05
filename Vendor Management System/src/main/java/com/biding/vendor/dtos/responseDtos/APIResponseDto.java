package com.biding.vendor.dtos.responseDtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class APIResponseDto<T> {
    private T response;
    private String status;
    private int statusCode;
}
