package com.biding.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private Double basePrice;

    public ProductResponseDto(){}

    public ProductResponseDto(Long id, String name, String description, Double basePrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
    }
}