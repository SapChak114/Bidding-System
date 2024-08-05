package com.biding.auction.service;

import com.biding.auction.dto.request.AuctionRequestDto;
import com.biding.auction.dto.request.PaginationRequest;
import com.biding.auction.dto.response.APIResponse;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;

public interface AuctionService {
    APIResponse<Object> saveAuction(AuctionRequestDto auctionRequestDto);

    APIResponse<Object> updateAuction(Long id, AuctionRequestDto auctionRequestDto);

    APIResponse<Object> getAuctionByProductId(Long productId);

    APIResponse<Object> deleteAuctionById(Long id);

    APIResponse<Object> getAuctionsByFilters(PaginationRequest build);

    APIResponse<Object> getAuctionsWithWinner(PaginationRequest paginationRequest);

    ResponseEntity<?> deleteAuctionByProductId(Long productId);
}
