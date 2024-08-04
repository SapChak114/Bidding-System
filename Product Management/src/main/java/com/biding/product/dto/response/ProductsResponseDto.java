package com.biding.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class ProductsResponseDto {
    private Long id;

    private String name;

    private String description;

    private Double basePrice;

    private Long vendorId;
}
