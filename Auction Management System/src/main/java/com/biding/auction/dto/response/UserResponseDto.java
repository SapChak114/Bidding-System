package com.biding.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String contact;

    public UserResponseDto(){}

    private UserResponseDto(Long id, String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

}