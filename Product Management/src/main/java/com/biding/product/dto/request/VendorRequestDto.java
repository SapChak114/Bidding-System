package com.biding.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorRequestDto {
    private Long id;
    private String name;
    private String email;
    private String contact;
}
