package com.bidding.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class APIResponse<T> {
    private T response;
    private int statusCode;
}
