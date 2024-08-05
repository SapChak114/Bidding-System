package com.biding.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsRequestDto {

    private String name;

    private String description;

    private Double basePrice;

    private Long vendorId;
}