package com.biding.vendor.dtos.responseDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(builderClassName = "builder")
@ToString
public class VendorRegistrationResponse {
    private Long id;
    private String name;
    private String email;
    private String contact;
}
