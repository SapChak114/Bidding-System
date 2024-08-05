package com.biding.vendor;

import com.biding.vendor.controller.VendorController;
import com.biding.vendor.dtos.requestDtos.PaginationRequest;
import com.biding.vendor.dtos.requestDtos.VendorRegistrationRequest;
import com.biding.vendor.dtos.requestDtos.VendorUpdateRequest;
import com.biding.vendor.dtos.responseDtos.APIResponseDto;
import com.biding.vendor.dtos.responseDtos.VendorRegistrationResponse;
import com.biding.vendor.service.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendorController.class)
public class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorService;

    @Autowired
    private ObjectMapper objectMapper;

    private VendorRegistrationRequest vendorRegistrationRequest;

    private VendorUpdateRequest vendorUpdateRequest;
    private APIResponseDto<Object> apiResponse;

    @BeforeEach
    void setUp() {
        vendorRegistrationRequest = VendorRegistrationRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .contact("+1234567890")
                .password("securePassword123")
                .build();

        vendorUpdateRequest = VendorUpdateRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .contact("+1234567890")
                .build();

        VendorRegistrationResponse vendorRegistrationResponse = VendorRegistrationResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .contact("+1234567890")
                .build();

        apiResponse = APIResponseDto.<Object>builder()
                .response(vendorRegistrationResponse)
                .status("Success")
                .statusCode(200)
                .build();
    }

    @Test
    void testCreateVendor() throws Exception {
        when(vendorService.createVendor(vendorRegistrationRequest)).thenReturn(apiResponse);

        mockMvc.perform(post("/vendors/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendorRegistrationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateVendor() throws Exception {
        Long vendorId = 1L;
        when(vendorService.updateVendor(vendorId, vendorUpdateRequest)).thenReturn(apiResponse);

        mockMvc.perform(put("/vendors/{id}", vendorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendorRegistrationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVendorByEmail() throws Exception {
        String email = "john.doe@example.com";
        when(vendorService.getVendorByEmail(email)).thenReturn(apiResponse);

        mockMvc.perform(get("/vendors/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVendors() throws Exception {
        int offSet = 0;
        int pageSize = 10;
        String field = "name";
        String sort = "asc";

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .field(field)
                .offset(offSet)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        when(vendorService.getAllVendors(paginationRequest)).thenReturn(apiResponse);

        mockMvc.perform(get("/vendors/{offSet}/{pageSize}/{field}/{sort}", offSet, pageSize, field, sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
