package com.biding.product.controller;

import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;
import com.biding.product.service.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductsController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService){
        this.productsService = productsService;
    }

    @PostMapping("/")
    public APIResponse<Object> createProduct(@RequestBody ProductsRequestDto productsRequestDto) {
        return productsService.createProduct(productsRequestDto);
    }

    @PutMapping("/{id}")
    public APIResponse<Object> updateProduct(@PathVariable Integer id, @RequestBody ProductsRequestDto productsRequestDto) {
        return productsService.updateProduct(id, productsRequestDto);
    }

    @GetMapping("/{id}")
    public APIResponse<Object> getProductById(@PathVariable Integer id) {
        return productsService.getProductById(id);
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    public APIResponse<Object> getProducts(
                            @PathVariable("offSet") int offSet,
                            @PathVariable("pageSize") int pageSize,
                            @PathVariable("field") String field,
                            @PathVariable("sort") String sort,
                            @RequestParam("name") String name,
                            @RequestParam("description") String description,
                            @RequestParam("basePrice") Double basePrice) {

    }
}
