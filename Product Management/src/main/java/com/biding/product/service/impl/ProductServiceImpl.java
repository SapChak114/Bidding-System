package com.biding.product.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.biding.product.dao.Products;
import com.biding.product.dao.Vendors;
import com.biding.product.dto.request.PaginationRequest;
import com.biding.product.dto.request.ProductsRequestDto;
import com.biding.product.dto.response.APIResponse;
import com.biding.product.dto.response.PaginationResponse;
import com.biding.product.dto.response.ProductsResponseDto;
import com.biding.product.repository.ProductsRepository;
import com.biding.product.repository.VendorsRepository;
import com.biding.product.service.ProductsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    private final VendorsRepository vendorsRepository;

    private final ObjectMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository, VendorsRepository vendorsRepository, ObjectMapper mapper) {
        this.productsRepository = productsRepository;
        this.vendorsRepository = vendorsRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public APIResponse<Object> createProduct(ProductsRequestDto productsRequestDto) {
        log.info("Starting createProduct process with request: {}", productsRequestDto);

        try {
            Products products = mapper.convertValue(productsRequestDto, Products.class);
            log.debug("Converted DTO to Entity: {}", products);

            Vendors vendors = vendorsRepository.findById(productsRequestDto.getVendorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
            log.debug("Found Vendor: {}", vendors);

            products.setVendors(vendors);

            products = productsRepository.save(products);
            log.debug("Saved Product: {}", products);

            ProductsResponseDto productsResponseDto = mapper.convertValue(products, ProductsResponseDto.class);
            log.info("Product created successfully with ID: {}", products.getId());

            return createResponse(productsResponseDto, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            log.error("Data Integrity Violation Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse("Data integrity violation", HttpStatus.BAD_REQUEST);

        } catch (ConstraintViolationException e) {
            log.error("Constraint Violation Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse("Constraint violation", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public APIResponse<Object> updateProduct(Long id, ProductsRequestDto productsRequestDto) {
        log.info("Starting update process for product with id: {}", id);

        Products existingProduct;
        try {
            existingProduct = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        } catch (ResponseStatusException e) {
            log.error("Error retrieving product with id: {}", id, e);
            return createResponse(e.getReason(), HttpStatus.NOT_FOUND);
        }

        try {
            if (StringUtil.notNullNorEmpty(productsRequestDto.getName())) {
                existingProduct.setName(productsRequestDto.getName());
            }
            if (StringUtil.notNullNorEmpty(productsRequestDto.getDescription())) {
                existingProduct.setDescription(productsRequestDto.getDescription());
            }
            if (productsRequestDto.getBasePrice() != null) {
                existingProduct.setBasePrice(productsRequestDto.getBasePrice());
            }

            Products updatedProduct = productsRepository.save(existingProduct);
            ProductsResponseDto productsResponseDto = mapper.convertValue(updatedProduct, ProductsResponseDto.class);

            log.info("Successfully updated product with id: {}", id);

            return createResponse(productsResponseDto, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating product with id: {}", id, e);
            return createResponse("Data integrity violation", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error while updating product with id: {}", id, e);
            return createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIResponse<Object> createResponse(Object content, HttpStatus status) {
        if (status.value() > 399) {
            if (content instanceof String) {
                throw new ResponseStatusException(status, (String) content);
            } else {
                throw new ResponseStatusException(status, "Error");
            }
        }
        return APIResponse.builder()
                .statusCode(status.value())
                .response(content)
                .build();
    }

    @Override
    public APIResponse<Object> getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        try {
            Products product = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            ProductsResponseDto productResponseDto = mapper.convertValue(product, ProductsResponseDto.class);

            log.info("Successfully retrieved product with id: {}", id);

            return createResponse(productResponseDto, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            log.error("Error while fetching product with id: {}", id, e);
            return createResponse(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while fetching product with id: {}", id, e);
            return createResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getAllProductsByFiltersAndPagination(PaginationRequest paginationRequest) {
        log.info("Fetching products with filters and pagination: {}", paginationRequest);

        try {
            Page<Products> page;
            if (paginationRequest.getSort().startsWith("a")) {
                page = productsRepository.findByFilters(
                        PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize())
                                .withSort(Sort.by(paginationRequest.getField()).ascending()),
                        paginationRequest.getName(),
                        paginationRequest.getDescription(),
                        paginationRequest.getBasePriceMin(),
                        paginationRequest.getBasePriceMax(),
                        paginationRequest.getVendorId());
            } else {
                page = productsRepository.findByFilters(
                        PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize())
                                .withSort(Sort.by(paginationRequest.getField()).descending()),
                        paginationRequest.getName(),
                        paginationRequest.getDescription(),
                        paginationRequest.getBasePriceMin(),
                        paginationRequest.getBasePriceMax(),
                        paginationRequest.getVendorId());
            }

            if (!page.isEmpty()) {
                log.debug("Found {} products with the given criteria", page.getTotalElements());
                List<ProductsResponseDto> response = mapperConvert(page);
                return createResponse(PaginationResponse.builder()
                        .size(page.getSize())
                        .totalNoPages(page.getTotalPages())
                        .currentPage(page.getNumber() + 1)
                        .totalElements(page.getTotalElements())
                        .sortBy(paginationRequest.getField())
                        .sortByType(paginationRequest.getSort())
                        .content(Collections.singletonList(response))
                        .build(), HttpStatus.OK);
            } else {
                log.warn("No products found for the given criteria");
                return createResponse("No products found for the given criteria", HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            log.error("Database error while fetching products: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Exception while fetching products: {}", e.getMessage(), e);
            return createResponse("Error while fetching products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<ProductsResponseDto> mapperConvert(Page<Products> page) {
        // Convert Products entities to ProductsResponseDto
        return page.getContent().stream()
                .map(product -> mapper.convertValue(product, ProductsResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public APIResponse<Object> deleteProductById(Long id) {
        log.info("Attempting to delete product with id: {}", id);

        try {
            Products product = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            productsRepository.delete(product);

            log.info("Successfully deleted product with id: {}", id);

            return createResponse("Product successfully deleted", HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            log.error("Error while deleting product with id: {}", id, e);
            return createResponse(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while deleting product with id: {}", id, e);
            return createResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
