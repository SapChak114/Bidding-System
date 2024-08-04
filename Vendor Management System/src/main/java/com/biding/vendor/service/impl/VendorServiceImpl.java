package com.biding.vendor.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.biding.vendor.dao.Vendors;
import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;
import com.biding.vendor.dtos.responseDtos.PaginationResponse;
import com.biding.vendor.dtos.responseDtos.VendorRegistrationResponse;
import com.biding.vendor.repository.VendorRepository;
import com.biding.vendor.service.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final ObjectMapper mapper;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public APIResponseDto<Object> createVendor(VendorRegistrationRequest vendorRegistrationRequest) {
        log.info("Starting create vendor process for email: {}", vendorRegistrationRequest.getEmail());
        try {
            Vendors vendor = mapper.convertValue(vendorRegistrationRequest, Vendors.class);
            if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getPassword())) {
                vendor.setPassword(BCrypt.hashpw(vendorRegistrationRequest.getPassword(), BCrypt.gensalt()));
            } else {
                return createErrorResponse("Bad Request Password Cannot Be Empty", 400);
            }
            vendor = vendorRepository.save(vendor);
            log.info("Vendor create operation successful for email: {}", vendor.getEmail());
            return APIResponseDto
                    .builder()
                    .status("Success")
                    .statusCode(201)
                    .response(mapper.convertValue(vendor, VendorRegistrationResponse.class))
                    .build();
        } catch (DataAccessException e) {
            log.error("Database error while creating or updating vendor: {}", e.getMessage(), e);
            if (e.getMessage().toLowerCase().contains("duplicate")) {
                return createErrorResponse("Bad Request : Duplicate Entry", 400);
            }
            return createErrorResponse("Unable to create vendor", 503);
        } catch (Exception e) {
            log.error("Exception while creating or updating vendor: {}", e.getMessage(), e);
            return createErrorResponse("An error occurred while processing the request", 500);
        }
    }

    @Override
    @Transactional
    public APIResponseDto<Object> updateVendor(Integer id, VendorRegistrationRequest vendorRegistrationRequest) {
        log.info("Starting update vendor process for id: {}", id);
        Vendors vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

        if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getName())) {
            vendor.setName(vendorRegistrationRequest.getName());
        }
        if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getEmail())) {
            vendor.setEmail(vendorRegistrationRequest.getEmail());
        }
        if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getContact())) {
            vendor.setContact(vendorRegistrationRequest.getContact());
        }
        if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getPassword())) {
            vendor.setPassword(BCrypt.hashpw(vendorRegistrationRequest.getPassword(), BCrypt.gensalt()));
        }
        vendor.setUpdatedAt(new Date());
        vendor = vendorRepository.save(vendor);
        log.info("Vendor update operation successful for id: {}", vendor.getId());
        return APIResponseDto
                .builder()
                .status("Success")
                .statusCode(200)
                .response(mapper.convertValue(vendor, VendorRegistrationResponse.class))
                .build();
    }


    @Override
    public APIResponseDto<Object> getVendorByEmail(String email) {
        log.info("Fetching vendor details for email: {}", email);
        try {
            Vendors vendor = vendorRepository.findByEmail(email);
            if (!Objects.isNull(vendor)) {
                log.info("Vendor found for email: {}", email);
                return APIResponseDto
                        .builder()
                        .status("Success")
                        .response(mapper.convertValue(vendor, VendorRegistrationResponse.class))
                        .statusCode(200)
                        .build();
            } else {
                log.warn("No vendor found for email: {}", email);
                return createErrorResponse("No vendor found with the given email id: " + email, 404);
            }
        } catch (DataAccessException e) {
            log.error("Database error while fetching vendor by email: {}", e.getMessage(), e);
            return createErrorResponse("Error accessing the database", 503);
        } catch (Exception e) {
            log.error("Exception while fetching vendor by email: {}", e.getMessage(), e);
            return createErrorResponse("An error occurred while processing the request", 500);
        }
    }

    @Override
    public APIResponseDto<Object> getAllVendors(PaginationRequest paginationRequest) {
        log.info("Fetching all vendors with pagination: offset={}, pageSize={}, sort={}, field={}",
                paginationRequest.getOffset(), paginationRequest.getPageSize(), paginationRequest.getSort(), paginationRequest.getField());
        try {
            PaginationResponse<Object> filteredData = findByOptions(paginationRequest);
            log.debug("Fetched {} vendors", filteredData.getContent().size());
            return APIResponseDto
                    .builder()
                    .response(filteredData)
                    .status("Success")
                    .statusCode(200)
                    .build();
        } catch (DataAccessException e) {
            log.error("Database error while fetching vendors: {}", e.getMessage(), e);
            return createErrorResponse("Error accessing the database", 503);
        } catch (Exception e) {
            log.error("Exception while filtering data: {}", e.getMessage(), e);
            return createErrorResponse("An error occurred while processing the request", 500);
        }
    }

    private PaginationResponse<Object> findByOptions(PaginationRequest paginationRequest) throws Exception {
        Page<Vendors> page;
        try {
            if (paginationRequest.getSort().startsWith("a")) {
                page = vendorRepository.findByOptions(
                        PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize())
                                .withSort(Sort.by(paginationRequest.getField()).ascending()),
                        paginationRequest.getName(),
                        paginationRequest.getContact(),
                        paginationRequest.getEmail());
            } else {
                page = vendorRepository.findByOptions(
                        PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize())
                                .withSort(Sort.by(paginationRequest.getField()).descending()),
                        paginationRequest.getName(),
                        paginationRequest.getContact(),
                        paginationRequest.getEmail());
            }

            if (!page.isEmpty()) {
                log.debug("Found {} vendors with the given criteria", page.getTotalElements());
                List<VendorRegistrationResponse> response = mapperConvert(page);
                return PaginationResponse.builder()
                        .size(page.getSize())
                        .totalNoPages(page.getTotalPages())
                        .currentPage(page.getNumber()+1)
                        .totalElements(page.getTotalElements())
                        .sortBy(paginationRequest.getField())
                        .sortByType(paginationRequest.getSort())
                        .content(Collections.singletonList(response))
                        .build();
            } else {
                log.warn("No data found for the given criteria");
                throw new Exception("No data found for the given criteria");
            }
        } catch (DataAccessException e) {
            log.error("Database error while finding by options: {}", e.getMessage(), e);
            throw new Exception("Error accessing the database");
        } catch (Exception e) {
            log.error("Exception while finding by options: {}", e.getMessage(), e);
            throw new Exception("Error while filtering data");
        }
    }

    private List<VendorRegistrationResponse> mapperConvert(Page<Vendors> page) {
        return mapper.convertValue(page.getContent(), mapper.getTypeFactory().constructCollectionType(List.class, VendorRegistrationResponse.class));
    }

    private APIResponseDto<Object> createErrorResponse(String message, int statusCode) {
        return APIResponseDto
                .builder()
                .status("Error")
                .statusCode(statusCode)
                .response(message)
                .build();
    }
}
