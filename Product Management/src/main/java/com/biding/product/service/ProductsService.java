package com.biding.product.service;

import com.biding.product.dto.request.PaginationRequest;
import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;

public interface ProductsService {
    APIResponse<Object> createProduct(ProductsRequestDto productsRequestDto);

    APIResponse<Object> updateProduct(Long id, ProductsRequestDto productsRequestDto);

    APIResponse<Object> getProductById(Long id);

    APIResponse<Object> getAllProductsByFiltersAndPagination(PaginationRequest builder);

    APIResponse<Object> deleteProductById(Long id);
}
