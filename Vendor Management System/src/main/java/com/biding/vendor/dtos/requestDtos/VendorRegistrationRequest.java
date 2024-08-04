package com.biding.vendor.dtos.requestDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorRegistrationRequest {
    private String name;
    private String email;
    private String contact;
    private String password;
}
