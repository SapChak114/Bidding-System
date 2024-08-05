package com.biding.auction.service.impl;

import com.biding.auction.dao.Auction;
import com.biding.auction.dao.Product;
import com.biding.auction.dao.User;
import com.biding.auction.dto.request.AuctionRequestDto;
import com.biding.auction.dto.request.PaginationRequest;
import com.biding.auction.dto.response.*;
import com.biding.auction.repository.AuctionRepository;
import com.biding.auction.repository.ProductRepository;
import com.biding.auction.repository.UserRepository;
import com.biding.auction.service.AuctionService;
import com.biding.auction.service.BidingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.biding.auction.constants.AuctionConstant.BID_IS_PRESENT;

@Service
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final ObjectMapper mapper;

    private final BidingService bidingService;

    @Autowired
    public AuctionServiceImpl(ProductRepository productRepository, UserRepository userRepository, AuctionRepository auctionRepository, BidingService bidingService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidingService = bidingService;
        this.mapper = new ObjectMapper();
    }
    @Transactional
    public APIResponse<Object> saveAuction(AuctionRequestDto auctionRequestDto) {
        try {
            Product product = productRepository.findById(auctionRequestDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            User winner = null;
            if (auctionRequestDto.getWinnerId() != null) {
                winner = userRepository.findById(auctionRequestDto.getWinnerId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
            }

            Auction auction = new Auction();
            auction.setProduct(product);
            auction.setBiddingStartTime(auctionRequestDto.getBiddingStartTime());
            auction.setBiddingEndTime(auctionRequestDto.getBiddingEndTime());
            auction.setWinner(winner);

            auction = auctionRepository.save(auction);

            ProductResponseDto productDto = mapper.convertValue(product, ProductResponseDto.class);
            UserResponseDto userDto = winner != null ? mapper.convertValue(winner, UserResponseDto.class) : null;

            AuctionResponseDto responseDto = AuctionResponseDto.builder()
                    .id(auction.getId())
                    .product(productDto)
                    .biddingStartTime(auction.getBiddingStartTime())
                    .biddingEndTime(auction.getBiddingEndTime())
                    .winner(userDto)
                    .build();

            return createResponse(responseDto, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            log.error("Database error while creating auction: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while creating auction: {}", e.getMessage(), e);
            return createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception while creating auction: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> updateAuction(Long id, AuctionRequestDto auctionRequestDto) {
        try {
            Auction existingAuction = auctionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

            if (auctionRequestDto.getProductId() != null) {
                Product product = productRepository.findById(auctionRequestDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                existingAuction.setProduct(product);
            }
            existingAuction.setBiddingStartTime(auctionRequestDto.getBiddingStartTime());
            existingAuction.setBiddingEndTime(auctionRequestDto.getBiddingEndTime());

            if (auctionRequestDto.getWinnerId() != null) {
                User winner = userRepository.findById(auctionRequestDto.getWinnerId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                existingAuction.setWinner(winner);
            } else {
                existingAuction.setWinner(null);
            }

            auctionRepository.save(existingAuction);

            ProductResponseDto productDto = mapper.convertValue(existingAuction.getProduct(), ProductResponseDto.class);
            UserResponseDto userDto = existingAuction.getWinner() != null ?
                    mapper.convertValue(existingAuction.getWinner(), UserResponseDto.class) : null;

            AuctionResponseDto responseDto = AuctionResponseDto.builder()
                    .id(existingAuction.getId())
                    .product(productDto)
                    .biddingStartTime(existingAuction.getBiddingStartTime())
                    .biddingEndTime(existingAuction.getBiddingEndTime())
                    .winner(userDto)
                    .build();

            return createResponse(responseDto, HttpStatus.OK);
        } catch (DataAccessException e) {
            log.error("Database error while updating auction: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while updating auction: {}", e.getMessage(), e);
            return createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception while updating auction: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getAuctionByProductId(Long productId) {
        try {
            Auction auction = auctionRepository.findAuctionByProductId(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Auction not found for product ID"));


            ProductResponseDto productDto = mapper.convertValue(auction.getProduct(), ProductResponseDto.class);
            UserResponseDto userDto = auction.getWinner() != null ?
                    mapper.convertValue(auction.getWinner(), UserResponseDto.class) : null;

            AuctionResponseDto responseDto = AuctionResponseDto.builder()
                    .id(auction.getId())
                    .product(productDto)
                    .biddingStartTime(auction.getBiddingStartTime())
                    .biddingEndTime(auction.getBiddingEndTime())
                    .winner(userDto)
                    .build();

            return createResponse(responseDto, HttpStatus.OK);
        } catch (DataAccessException e) {
            log.error("Database error while fetching auction: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while fetching auction: {}", e.getMessage(), e);
            return createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception while fetching auction: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> deleteAuctionById(Long id) {
        try {
            Auction auction = auctionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

            auctionRepository.delete(auction);

            return createResponse("Auction successfully deleted", HttpStatus.NO_CONTENT);
        } catch (DataAccessException e) {
            log.error("Database error while deleting auction: {}", e.getMessage(), e);
            return createResponse("Error accessing the database", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while deleting auction: {}", e.getMessage(), e);
            return createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception while deleting auction: {}", e.getMessage(), e);
            return createResponse("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getAuctionsByFilters(PaginationRequest paginationRequest) {
        try {
            log.info("Fetching auctions with filters: {}", paginationRequest);

            Pageable pageable = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getPageSize(),
                    paginationRequest.getSort().toLowerCase().startsWith("a")
                            ? Sort.by(paginationRequest.getField()).ascending()
                            : Sort.by(paginationRequest.getField()).descending());

            Page<Auction> page = auctionRepository.findByOptions(pageable,
                    paginationRequest.getProductName(),
                    paginationRequest.getUserEmail(),
                    paginationRequest.getUserContact());

            List<AuctionResponseDto> response = mapper.convertValue(page.getContent(),
                    mapper.getTypeFactory().constructCollectionType(List.class, AuctionResponseDto.class));

            PaginationResponse<AuctionResponseDto> paginationResponse = PaginationResponse.<AuctionResponseDto>builder()
                    .size(page.getSize())
                    .totalNoPages(page.getTotalPages())
                    .currentPage(page.getNumber() + 1)
                    .totalElements(page.getTotalElements())
                    .sortBy(paginationRequest.getField())
                    .sortByType(paginationRequest.getSort())
                    .content(response)
                    .build();

            return createResponse(paginationResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error fetching auctions with filters: {}", e.getMessage(), e);
            return createResponse("Error fetching auctions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getAuctionsWithWinner(PaginationRequest paginationRequest) {
        log.info("Fetching auctions with winner with pagination request: {}", paginationRequest);

        try {
            Pageable pageable = PageRequest.of(
                    paginationRequest.getOffset() - 1,
                    paginationRequest.getPageSize(),
                    paginationRequest.getSort().equalsIgnoreCase("asc") ?
                            Sort.by(paginationRequest.getField()).ascending() :
                            Sort.by(paginationRequest.getField()).descending()
            );

            Page<Auction> page = auctionRepository.findByWinnerIsNotNull(pageable);

            List<AuctionResponseDto> response = mapper.convertValue(page.getContent(),
                    mapper.getTypeFactory().constructCollectionType(List.class, AuctionResponseDto.class));

            PaginationResponse<AuctionResponseDto> paginationResponse = PaginationResponse.<AuctionResponseDto>builder()
                    .size(page.getSize())
                    .totalNoPages(page.getTotalPages())
                    .currentPage(page.getNumber() + 1)
                    .totalElements(page.getTotalElements())
                    .sortBy(paginationRequest.getField())
                    .sortByType(paginationRequest.getSort())
                    .content(response)
                    .build();

            return createResponse(paginationResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching auctions with filters: {}", e.getMessage(), e);
            return createResponse("Error fetching auctions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> deleteAuctionByProductId(Long productId) {
        Optional<Auction> auction = auctionRepository.findAuctionByProductId(productId);

        Boolean isBidPresent = bidingService.findByAuctionPresent(auction);

        if (isBidPresent) {
            return new ResponseEntity<>(BID_IS_PRESENT, HttpStatus.OK);
        } else {
            auction.ifPresent(auctionRepository::delete);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private APIResponse<Object> createResponse(Object content, HttpStatus status) {
        if (status.is4xxClientError() || status.is5xxServerError()) {
            if (content instanceof String) {
                throw new ResponseStatusException(status, (String) content);
            } else {
                throw new ResponseStatusException(status, "Error");
            }
        }
        return APIResponse.builder()
                .statusCode(status.value())
                .response(content)
                .build();
    }
}
