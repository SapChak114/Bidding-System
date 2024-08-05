package com.biding.product.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsResponseDto {
    private Long id;

    private String name;

    private String description;

    private Double basePrice;

    private Long vendorId;

    public ProductsResponseDto(){}

    public ProductsResponseDto(Long id, String name, String description, Double basePrice, Long vendorId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.vendorId = vendorId;
    }
}
