package com.biding.vendor.dtos.responseDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorRegistrationResponse {
    private Long id;
    private String name;
    private String email;
    private String contact;
}
