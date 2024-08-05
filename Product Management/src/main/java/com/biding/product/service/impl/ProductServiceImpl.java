package com.biding.product.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.biding.product.dao.Product;
import com.biding.product.dao.Vendor;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.biding.product.constants.RestTemplateConstants.BASE_URL;
import static com.biding.product.constants.RestTemplateConstants.PRODUCT_ID;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.biding.product.constants.RestTemplateConstants.BID_IS_PRESENT;
import static com.biding.product.constants.ProductManagementConstants.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    private final VendorsRepository vendorsRepository;

    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository, VendorsRepository vendorsRepository, ObjectMapper mapper) {
        this.productsRepository = productsRepository;
        this.vendorsRepository = vendorsRepository;
        this.mapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

    @Override
    public APIResponse<Object> createProduct(ProductsRequestDto productsRequestDto) {
        log.info("Starting createProduct process with request: {}", productsRequestDto);

        try {
            Product product = mapper.convertValue(productsRequestDto, Product.class);
            log.debug("Converted DTO to Entity: {}", product);

            Vendor vendor = vendorsRepository.findById(productsRequestDto.getVendorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, VENDOR_NOT_FOUND));
            log.debug("Found Vendor: {}", vendor);

            product.setVendor(vendor);

            product.setUpdatedAt(new Date());
            product.setCreatedAt(new Date());
            product = productsRepository.save(product);
            log.debug("Saved Product: {}", product);

            ProductsResponseDto productsResponseDto = mapper.convertValue(product, ProductsResponseDto.class);
            log.info("Product created successfully with ID: {}", product.getId());

            return createResponse(productsResponseDto, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            log.error("Data Integrity Violation Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse(DATA_INTEGRITY_VIOLATION, HttpStatus.BAD_REQUEST);

        } catch (ConstraintViolationException e) {
            log.error("Constraint Violation Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse(CONSTRAINT_VIOLATION, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception occurred while creating product: {}", e.getMessage(), e);
            return createResponse(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public APIResponse<Object> updateProduct(Long id, ProductsRequestDto productsRequestDto) {
        log.info("Starting update process for product with id: {}", id);

        Product existingProduct;
        try {
            existingProduct = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NO_PRODUCT_FOUND));
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

            existingProduct.setUpdatedAt(new Date());
            Product updatedProduct = productsRepository.save(existingProduct);
            ProductsResponseDto productsResponseDto = mapper.convertValue(updatedProduct, ProductsResponseDto.class);

            log.info("Successfully updated product with id: {}", id);

            return createResponse(productsResponseDto, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating product with id: {}", id, e);
            return createResponse(DATA_INTEGRITY_VIOLATION, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error while updating product with id: {}", id, e);
            return createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIResponse<Object> createResponse(Object content, HttpStatus status) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            if (content instanceof String) {
                throw new ResponseStatusException(status, (String) content);
            } else {
                throw new ResponseStatusException(status, "Error");
            }
        }
        return APIResponse.builder()
                .statusCode(status.value())
                .response(content)
                .status(SUCCESS)
                .build();
    }

    @Override
    public APIResponse<Object> getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        try {
            Product product = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            ProductsResponseDto productResponseDto = mapper.convertValue(product, ProductsResponseDto.class);

            log.info("Successfully retrieved product with id: {}", id);

            return createResponse(productResponseDto, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            log.error("Error while fetching product with id: {}", id, e);
            return createResponse(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while fetching product with id: {}", id, e);
            return createResponse(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getAllProductsByFiltersAndPagination(PaginationRequest paginationRequest) {
        log.info("Fetching products with filters and pagination: {}", paginationRequest);

        try {
            Pageable pageable = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize(),
                    paginationRequest.getSort().toLowerCase().startsWith("a")
                            ? Sort.by(paginationRequest.getField()).ascending()
                            : Sort.by(paginationRequest.getField()).descending());

            Page<Product> page = productsRepository.findByFilters(
                        pageable,
                        paginationRequest.getName(),
                        paginationRequest.getDescription(),
                        paginationRequest.getBasePriceMin(),
                        paginationRequest.getBasePriceMax(),
                        paginationRequest.getVendorId());

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
                return createResponse(NO_PRODUCT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            log.error("Database error while fetching products: {}", e.getMessage(), e);
            return createResponse(DATABASE_ACCESS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Exception while fetching products: {}", e.getMessage(), e);
            return createResponse(FETCHING_PRODUCT_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<ProductsResponseDto> mapperConvert(Page<Product> page) {
        return page.getContent().stream()
                .map(product -> mapper.convertValue(product, ProductsResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public APIResponse<Object> deleteProductById(Long id) {
        log.info("Attempting to delete product with id: {}", id);

        try {
            Product product = productsRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NO_PRODUCT_FOUND));

            ResponseEntity<?> response = deleteAuctionByProductId(id);
            if (response.getBody() instanceof String
                    && StringUtil.notNullNorEmpty(((String) response.getBody()))
                    && ((String) response.getBody()).equals(BID_IS_PRESENT)) {
                return createResponse(DENY_PRODUCT_DELETE, HttpStatus.BAD_REQUEST);
            }
            productsRepository.delete(product);

            log.info("Successfully deleted product with id: {}", id);
            return createResponse(DELETED_SUCCESSFULLY, HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            log.error("Error while deleting product with id: {}", id, e);
            return createResponse(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while deleting product with id: {}", id, e);
            return createResponse(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteAuctionByProductId(Long productId) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(PRODUCT_ID, productId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class);
        } catch (HttpClientErrorException e) {
            log.error("HTTP Client Error: Status Code = {}, Response Body = {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;  // or handle the exception as per your requirement
        } catch (Exception e) {
            log.error("Unexpected error occurred while making the HTTP request", e);
            throw e;
        }
    }

}
