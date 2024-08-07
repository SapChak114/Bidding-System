package com.biding.auction.controller;

import com.biding.auction.dto.request.AuctionRequestDto;
import com.biding.auction.dto.request.PaginationRequest;
import com.biding.auction.dto.response.APIResponse;
import com.biding.auction.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/")
    public ResponseEntity<APIResponse<Object>> createAuction(
            @RequestBody @Valid AuctionRequestDto auctionRequestDto) {

        APIResponse<Object> response = auctionService.saveAuction(auctionRequestDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> updateAuction(
            @PathVariable("id") Long id,
            @RequestBody @Valid AuctionRequestDto auctionRequestDto
            ) {

        APIResponse<Object> response = auctionService.updateAuction(id, auctionRequestDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<APIResponse<Object>> getAuctionByProductId(
            @PathVariable("productId") Long productId) {

        APIResponse<Object> response = auctionService.getAuctionByProductId(productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> deleteAuction(
            @PathVariable("id") Long id) {

        APIResponse<Object> response = auctionService.deleteAuctionById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAuctionByProductId(@RequestParam("productId") Long productId) {
        return auctionService.deleteAuctionByProductId(productId);
    }

    @GetMapping("/filters/{offSet}/{pageSize}/{field}/{sort}")
    public ResponseEntity<APIResponse<Object>> getAuctionsByFilters(
            @PathVariable("offSet") int offSet,
            @PathVariable("pageSize") int pageSize,
            @PathVariable("field") String field,
            @PathVariable("sort") String sort,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "userEmail", required = false) String userEmail,
            @RequestParam(value = "userContact", required = false) String userContact) {

        APIResponse<Object> resp = auctionService.getAuctionsByFilters(PaginationRequest.builder()
                .offset(offSet)
                .pageSize(pageSize)
                .field(field)
                .sort(sort)
                .productName(productName)
                .userEmail(userEmail)
                .userContact(userContact)
                .build());

        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
    //TODO : Find Auctions which are currently in bid and which will start to bid -> currentTime < biddingEndTime

    @GetMapping("/liveORUpcoming")
    public ResponseEntity<APIResponse<Object>> getAllOnGoingAuctions() {
        APIResponse<Object> resp = auctionService.getAllOnGoingAuctions();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
    @GetMapping("/with-winner")
    public ResponseEntity<APIResponse<Object>> getAuctionsWithWinner(
            @RequestParam("offset") int offset,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("field") String field,
            @RequestParam("sort") String sort) {

        APIResponse<Object> resp = auctionService.getAuctionsWithWinner(PaginationRequest.builder()
                            .offset(offset)
                            .pageSize(pageSize)
                            .field(field)
                            .sort(sort)
                            .build());

        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    //TODO: Given user id return the list of auctions where the user has won
}
