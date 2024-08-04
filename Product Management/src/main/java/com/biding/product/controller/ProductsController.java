package com.biding.product.controller;

import com.biding.product.dto.request.PaginationRequest;
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
    public APIResponse<Object> updateProduct(@PathVariable("id") Long id, @RequestBody ProductsRequestDto productsRequestDto) {
        return productsService.updateProduct(id, productsRequestDto);
    }

    @GetMapping("/{id}")
    public APIResponse<Object> getProductById(@PathVariable("id") Long id) {
        return productsService.getProductById(id);
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    public APIResponse<Object> getProducts(
                            @PathVariable("offSet") int offSet,
                            @PathVariable("pageSize") int pageSize,
                            @PathVariable("field") String field,
                            @PathVariable("sort") String sort,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "basePriceMin", required = false) Double basePriceMin,
                            @RequestParam(value = "basePriceMax", required = false) Double basePriceMax,
                            @RequestParam(value = "vendorId", required = false) Long vendorId) {
        return productsService.getAllProductsByFiltersAndPagination(PaginationRequest
                        .builder()
                        .offset(offSet)
                        .pageSize(pageSize)
                        .field(field)
                        .sort(sort)
                        .name(name)
                        .description(description)
                        .vendorId(vendorId)
                        .basePriceMin(basePriceMin)
                        .basePriceMax(basePriceMax)
                        .build());
    }

    @DeleteMapping("/{id}")
    public APIResponse<Object> deleteProduct(@PathVariable("id") Long id) {
        return productsService.deleteProductById(id);
    }
}
