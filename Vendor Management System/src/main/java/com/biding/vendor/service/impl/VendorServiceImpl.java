package com.biding.vendor.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.biding.vendor.dao.Vendor;
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
import org.springframework.data.domain.Pageable;
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
            Vendor vendor = mapper.convertValue(vendorRegistrationRequest, Vendor.class);
            if (StringUtil.notNullNorEmpty(vendorRegistrationRequest.getPassword())) {
                vendor.setPassword(BCrypt.hashpw(vendorRegistrationRequest.getPassword(), BCrypt.gensalt()));
            } else {
                return createResponse("Password cannot be empty", "Error", HttpStatus.BAD_REQUEST);
            }
            vendor = vendorRepository.save(vendor);
            log.info("Vendor create operation successful for email: {}", vendor.getEmail());
            VendorRegistrationResponse response = mapper.convertValue(vendor, VendorRegistrationResponse.class);
            return createResponse(response, "Success", HttpStatus.CREATED);
        } catch (DataAccessException e) {
            log.error("Database error while creating vendor: {}", e.getMessage(), e);
            if (e.getMessage().toLowerCase().contains("duplicate")) {
                return createResponse("Duplicate entry", "Error", HttpStatus.BAD_REQUEST);
            }
            return createResponse("Unable to create vendor", "Error", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            log.error("Exception while creating vendor: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", "Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public APIResponseDto<Object> updateVendor(Integer id, VendorRegistrationRequest vendorRegistrationRequest) {
        log.info("Starting update vendor process for id: {}", id);

        try {
            Vendor vendor = vendorRepository.findById(id)
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
        } catch (DataAccessException e) {
            log.error("Database error while updating vendor: {}", e.getMessage(), e);
            if (e.getMessage().toLowerCase().contains("duplicate")) {
                return createResponse("Duplicate entry", "Error",HttpStatus.BAD_REQUEST);
            }
            return createResponse("Unable to update vendor", "Error", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            log.error("Exception while updating vendor: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", "Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public APIResponseDto<Object> getVendorByEmail(String email) {
        log.info("Fetching vendor details for email: {}", email);
        try {
            Vendor vendor = vendorRepository.findByEmail(email);
            if (!Objects.isNull(vendor)) {
                log.info("Vendor found for email: {}", email);
                return createResponse(mapper.convertValue(vendor, VendorRegistrationResponse.class), "Success", HttpStatus.valueOf(200));
            } else {
                log.warn("No vendor found for email: {}", email);
                return createResponse("No vendor found with the given email id: " + email,"Error", HttpStatus.valueOf(404));
            }
        } catch (DataAccessException e) {
            log.error("Database error while fetching vendor by email: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", "Error",HttpStatus.valueOf(503));
        } catch (Exception e) {
            log.error("Exception while fetching vendor by email: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request","Error",HttpStatus.valueOf( 500));
        }
    }

    @Override
    public APIResponseDto<Object> getAllVendors(PaginationRequest paginationRequest) {
        log.info("Fetching all vendors with pagination: offset={}, pageSize={}, sort={}, field={}",
                paginationRequest.getOffset(), paginationRequest.getPageSize(), paginationRequest.getSort(), paginationRequest.getField());
        try {
            PaginationResponse<Object> filteredData = findByOptions(paginationRequest);
            log.debug("Fetched {} vendors", filteredData.getContent().size());
            return createResponse(filteredData, "Success", HttpStatus.valueOf(200));
        } catch (DataAccessException e) {
            log.error("Database error while fetching vendors: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", "Error" , HttpStatus.valueOf(503));
        } catch (Exception e) {
            log.error("Exception while filtering data: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request","Error" , HttpStatus.valueOf(500));
        }
    }

    private PaginationResponse<Object> findByOptions(PaginationRequest paginationRequest) throws Exception {
        Page<Vendor> page;
        try {

            Pageable pageable = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize(),
                    paginationRequest.getSort().toLowerCase().startsWith("a")
                            ? Sort.by(paginationRequest.getField()).ascending()
                            : Sort.by(paginationRequest.getField()).descending());
                page = vendorRepository.findByOptions(
                        pageable,
                        paginationRequest.getName(),
                        paginationRequest.getContact(),
                        paginationRequest.getEmail());

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

    private List<VendorRegistrationResponse> mapperConvert(Page<Vendor> page) {
        return mapper.convertValue(page.getContent(), mapper.getTypeFactory().constructCollectionType(List.class, VendorRegistrationResponse.class));
    }

    private APIResponseDto<Object> createResponse(Object content, String message, HttpStatus status) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            if (content instanceof String) {
                throw new ResponseStatusException(status, (String) content);
            } else {
                throw new ResponseStatusException(status, "Error");
            }
        }
        return APIResponseDto.builder()
                .statusCode(status.value())
                .status(message)
                .response(content)
                .build();
    }

}
