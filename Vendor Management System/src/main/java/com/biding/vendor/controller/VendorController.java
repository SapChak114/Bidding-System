package com.biding.vendor.controller;

import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;
import com.biding.vendor.service.VendorService;
import com.biding.vendor.service.impl.VendorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vendors")
@Slf4j
public class VendorController {

    private final VendorService vendorService;

    @Autowired
    public VendorController(VendorServiceImpl vendorService) {
        this.vendorService = vendorService;
    }


    @PostMapping("/")
    public APIResponseDto<Object> createVendor(@RequestBody VendorRegistrationRequest vendorRegistrationRequest) {
        return vendorService.createVendor(vendorRegistrationRequest);
    }

    @PutMapping("/{id}")
    public APIResponseDto<Object> updateVendor(@PathVariable("id") Integer id, @RequestBody VendorRegistrationRequest vendorRegistrationRequest) {
        return vendorService.updateVendor(id, vendorRegistrationRequest);
    }

    @GetMapping("/{email}")
    public APIResponseDto<Object> getVendorByEmail(@PathVariable("email") String email) {
        return vendorService.getVendorByEmail(email);
    }

    @GetMapping("/{offSet}/{pageSize}/{field}/{sort}")
    public APIResponseDto<Object> getVendors(
                                        @PathVariable("offSet") int offSet,
                                        @PathVariable("pageSize") int pageSize,
                                        @PathVariable("field") String field,
                                        @PathVariable("sort") String sort,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String contact) {
        return vendorService.getAllVendors(PaginationRequest.builder()
                        .field(field)
                        .offset(offSet)
                        .pageSize(pageSize)
                        .sort(sort)
                        .name(name)
                        .email(email)
                        .contact(contact)
                        .build());
    }

}
