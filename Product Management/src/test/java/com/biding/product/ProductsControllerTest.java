package com.biding.product;

import com.biding.product.controller.ProductsController;
import com.biding.product.dto.request.PaginationRequest;
import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;
import com.biding.product.service.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ProductsControllerTest {

    @Mock
    private ProductsService productsService;

    @InjectMocks
    private ProductsController productsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductsRequestDto requestDto = new ProductsRequestDto();
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .response("Product created successfully")
                .build();

        when(productsService.createProduct(any(ProductsRequestDto.class))).thenReturn(response);

        APIResponse<Object> result = productsController.createProduct(requestDto);

        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode());
        assertEquals("Product created successfully", result.getResponse());
    }

    @Test
    void testUpdateProduct() {
        ProductsRequestDto requestDto = new ProductsRequestDto();
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("Product updated successfully")
                .build();

        when(productsService.updateProduct(anyLong(), any(ProductsRequestDto.class))).thenReturn(response);

        APIResponse<Object> result = productsController.updateProduct(1L, requestDto);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Product updated successfully", result.getResponse());
    }

    @Test
    void testGetProductById() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("Product details")
                .build();

        when(productsService.getProductById(anyLong())).thenReturn(response);

        APIResponse<Object> result = productsController.getProductById(1L);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Product details", result.getResponse());
    }

    @Test
    void testGetProducts() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .response("Product list")
                .build();

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .offset(0)
                .pageSize(10)
                .field("name")
                .sort("asc")
                .build();

        when(productsService.getAllProductsByFiltersAndPagination(any(PaginationRequest.class))).thenReturn(response);

        APIResponse<Object> result = productsController.getProducts(
                0, 10, "name", "asc", null, null, null, null, null);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Product list", result.getResponse());
    }

    @Test
    void testDeleteProduct() {
        APIResponse<Object> response = APIResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .response("Product deleted successfully")
                .build();

        when(productsService.deleteProductById(anyLong())).thenReturn(response);

        APIResponse<Object> result = productsController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCode());
        assertEquals("Product deleted successfully", result.getResponse());
    }
}
