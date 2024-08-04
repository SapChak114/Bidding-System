package com.bidding.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String contact;
}
