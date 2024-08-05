package com.bidding.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder(builderClassName = "builder")
@ToString
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String contact;
}
