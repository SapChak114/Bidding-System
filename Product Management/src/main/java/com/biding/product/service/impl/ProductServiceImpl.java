package com.biding.product.service.impl;

import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;
import com.biding.product.service.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductsService {

    @Override
    public APIResponse<Object> createProduct(ProductsRequestDto productsRequestDto) {
        return null;
    }

    @Override
    public APIResponse<Object> updateProduct(Integer id, ProductsRequestDto productsRequestDto) {
        return null;
    }

    @Override
    public APIResponse<Object> getProductById(Integer id) {
        return null;
    }
}
