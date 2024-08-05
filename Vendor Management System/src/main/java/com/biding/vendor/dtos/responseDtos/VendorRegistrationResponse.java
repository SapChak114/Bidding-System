package com.biding.vendor.dtos.responseDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


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

    public VendorRegistrationResponse() {}
    public VendorRegistrationResponse(Long id, String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }
}
