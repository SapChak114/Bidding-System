package com.biding.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class ProductsRequestDto {

    private String name;

    private String description;

    private Double basePrice;

    private VendorRequestDto vendors;
}