package com.biding.vendor.service;

import com.biding.vendor.dtos.requestDtos.VendorUpdateRequest;
import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;

public interface VendorService {
    APIResponseDto<Object> createVendor(VendorRegistrationRequest vendorRegistrationRequest);

    APIResponseDto<Object> updateVendor(Long id, VendorUpdateRequest vendorRegistrationRequest);

    APIResponseDto<Object> getVendorByEmail(String email);

    APIResponseDto<Object> getAllVendors(PaginationRequest paginationRequest);

    APIResponseDto<Object> deleteById(Long id);
}
