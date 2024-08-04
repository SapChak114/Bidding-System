package com.biding.vendor.service;

import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;

public interface VendorService {
    APIResponseDto<Object> createVendor(VendorRegistrationRequest vendorRegistrationRequest);

    public APIResponseDto<Object> updateVendor(Integer id, VendorRegistrationRequest vendorRegistrationRequest);

    APIResponseDto<Object> getVendorByEmail(String id);

    APIResponseDto<Object> getAllVendors(PaginationRequest paginationRequest);
}
