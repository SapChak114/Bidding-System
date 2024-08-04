package com.biding.product.service;

import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;

public interface ProductsService {
    APIResponse<Object> createProduct(ProductsRequestDto productsRequestDto);

    APIResponse<Object> updateProduct(Integer id, ProductsRequestDto productsRequestDto);

    APIResponse<Object> getProductById(Integer id);
}
