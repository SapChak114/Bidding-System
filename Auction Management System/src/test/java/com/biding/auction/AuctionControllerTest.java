package com.biding.auction;

import com.biding.auction.controller.AuctionController;
import com.biding.auction.dto.request.AuctionRequestDto;
import com.biding.auction.dto.request.PaginationRequest;
import com.biding.auction.dto.response.APIResponse;
import com.biding.auction.service.AuctionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuctionControllerTest {

    @Mock
    private AuctionService auctionService;

    @InjectMocks
    private AuctionController auctionController;

    public AuctionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAuction() {
        AuctionRequestDto requestDto = new AuctionRequestDto();
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.CREATED.value()).response(new Object()).build();

        when(auctionService.saveAuction(any(AuctionRequestDto.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.createAuction(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).saveAuction(any(AuctionRequestDto.class));
    }

    @Test
    void updateAuction() {
        AuctionRequestDto requestDto = new AuctionRequestDto();
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.OK.value()).response(new Object()).build();

        when(auctionService.updateAuction(anyLong(), any(AuctionRequestDto.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.updateAuction(1L, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).updateAuction(anyLong(), any(AuctionRequestDto.class));
    }

    @Test
    void getAuctionByProductId() {
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.OK.value()).response(new Object()).build();

        when(auctionService.getAuctionByProductId(anyLong())).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.getAuctionByProductId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).getAuctionByProductId(anyLong());
    }

    @Test
    void deleteAuction() {
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.NO_CONTENT.value()).response(null).build();

        when(auctionService.deleteAuctionById(anyLong())).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.deleteAuction(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).deleteAuctionById(anyLong());
    }

    @Test
    void getAuctionsByFilters() {
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.OK.value()).response(new Object()).build();
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .offset(1)
                .pageSize(10)
                .field("name")
                .sort("asc")
                .productName("Product")
                .userEmail("user@example.com")
                .userContact("1234567890")
                .build();

        when(auctionService.getAuctionsByFilters(any(PaginationRequest.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.getAuctionsByFilters(
                paginationRequest.getOffset(),
                paginationRequest.getPageSize(),
                paginationRequest.getField(),
                paginationRequest.getSort(),
                paginationRequest.getProductName(),
                paginationRequest.getUserEmail(),
                paginationRequest.getUserContact()
        );

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).getAuctionsByFilters(any(PaginationRequest.class));
    }

    @Test
    void getAuctionsWithWinner() {
        APIResponse<Object> response = APIResponse.<Object>builder().statusCode(HttpStatus.OK.value()).response(new Object()).build();
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .offset(1)
                .pageSize(10)
                .field("name")
                .sort("asc")
                .build();

        when(auctionService.getAuctionsWithWinner(any(PaginationRequest.class))).thenReturn(response);

        ResponseEntity<APIResponse<Object>> result = auctionController.getAuctionsWithWinner(
                paginationRequest.getOffset(),
                paginationRequest.getPageSize(),
                paginationRequest.getField(),
                paginationRequest.getSort()
        );

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(auctionService, times(1)).getAuctionsWithWinner(any(PaginationRequest.class));
    }
}
