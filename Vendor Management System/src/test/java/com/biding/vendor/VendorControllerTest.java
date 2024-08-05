package com.biding.vendor;

import com.biding.vendor.controller.VendorController;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;
import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class VendorControllerTest {

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVendor() {
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
                .name("abc")
                .contact("918420595035")
                .email("abc@abc.com")
                .password("password")
                .build();
        APIResponseDto<Object> response = APIResponseDto.builder()
                .response("Vendor created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .build();

        when(vendorService.createVendor(any(VendorRegistrationRequest.class))).thenReturn(response);

        APIResponseDto<Object> result = vendorController.createVendor(request);

        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode());
        assertEquals("Success", result.getStatus());
    }

    @Test
    void testUpdateVendor() {
        Integer id = 1;
        VendorRegistrationRequest request = VendorRegistrationRequest.builder()
                .name("abc")
                .build();
        APIResponseDto<Object> response = APIResponseDto.builder()
                .response("Vendor updated successfully")
                .statusCode(HttpStatus.OK.value())
                .build();

        when(vendorService.updateVendor(eq(id), any(VendorRegistrationRequest.class))).thenReturn(response);

        APIResponseDto<Object> result = vendorController.updateVendor(id, request);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Vendor updated successfully", result.getResponse());
    }

    @Test
    void testGetVendorByEmail() {
        String email = "vendor@example.com";
        APIResponseDto<Object> response = APIResponseDto.builder()
                .response("Vendor details")
                .statusCode(HttpStatus.OK.value())
                .build();

        when(vendorService.getVendorByEmail(eq(email))).thenReturn(response);

        APIResponseDto<Object> result = vendorController.getVendorByEmail(email);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Vendor details", result.getResponse());
    }

    @Test
    void testGetVendors() {
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .offset(0)
                .pageSize(10)
                .field("name")
                .sort("asc")
                .name("VendorName")
                .email("vendor@example.com")
                .contact("1234567890")
                .build();

        APIResponseDto<Object> response = APIResponseDto.builder()
                .response("Vendors list")
                .statusCode(HttpStatus.OK.value())
                .build();

        when(vendorService.getAllVendors(any(PaginationRequest.class))).thenReturn(response);

        APIResponseDto<Object> result = vendorController.getVendors(
                paginationRequest.getOffset(),
                paginationRequest.getPageSize(),
                paginationRequest.getField(),
                paginationRequest.getSort(),
                paginationRequest.getName(),
                paginationRequest.getEmail(),
                paginationRequest.getContact()
        );

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertEquals("Vendors list", result.getResponse());
    }
}
