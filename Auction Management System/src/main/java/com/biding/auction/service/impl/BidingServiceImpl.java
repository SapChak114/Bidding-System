package com.biding.auction.service.impl;

import com.biding.auction.dao.Auction;
import com.biding.auction.dao.Bid;
import com.biding.auction.dao.User;
import com.biding.auction.dto.request.BidRequestDto;
import com.biding.auction.dto.request.NotificationRequestDto;
import com.biding.auction.enums.BiddingStrategy;
import com.biding.auction.repository.AuctionRepository;
import com.biding.auction.repository.BidRepository;
import com.biding.auction.repository.UserRepository;
import com.biding.auction.service.BidingService;
import com.biding.auction.service.NotificationService;
import com.biding.auction.winningStrategy.WinnerDeterminationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.biding.auction.constants.AuctionConstant.*;
import static com.biding.auction.constants.SQSConstants.SQS_QUEUE_NAME;

@Service
@Slf4j
public class BidingServiceImpl implements BidingService {

    private final BidRepository bidRepository;

    private final AuctionRepository auctionRepository;

    private final UserRepository userRepository;

    private final ObjectMapper mapper;

    private final NotificationService notificationService;

    @Autowired
    public BidingServiceImpl(BidRepository bidRepository, AuctionRepository auctionRepository, UserRepository userRepository, NotificationService notificationService) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.mapper = new ObjectMapper();
        this.notificationService = notificationService;
    }

    @SqsListener(value = SQS_QUEUE_NAME)
    public void receiveMessage(String message) {
        try {
            BidRequestDto bidRequestDto = mapper.readValue(message, BidRequestDto.class);

            User user = userRepository.findById(bidRequestDto.getUserId())
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            Auction auction = auctionRepository.findById(bidRequestDto.getAuctionId())
                    .orElseThrow(() -> new RuntimeException(AUCTION_NOT_FOUND));

            if (bidRequestDto.getAmount() < auction.getProduct().getBasePrice()) {
                throw new RuntimeException(INVALID_BIDDING_PRICE);
            }
            Bid bid = new Bid();
            bid.setUser(user);
            bid.setAuction(auction);
            bid.setAmount(bidRequestDto.getAmount());

            bidRepository.save(bid);

            log.info("Successfully processed bid: {}", bid);
        } catch (IOException e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void determineAuctionWinners() {
        List<Auction> auctions = auctionRepository.findAllByBiddingEndTimeBeforeAndWinnerIsNull(new Date());
        log.debug("Auction details to fetch winner : "+auctions);
        for (Auction auction : auctions) {
            List<Bid> bids = bidRepository.findByAuctionId(auction.getId());

            if (bids.isEmpty()) {
                log.info("No bids found for auction with ID: {}", auction.getId());
                continue;
            }
            BiddingStrategy strategy = auction.getBiddingStrategy();
            WinnerDeterminationContext context = new WinnerDeterminationContext(strategy);
            Optional<Bid> winningBid = context.determineWinner(bids);

            if (winningBid.isPresent()) {
                User winner = winningBid.get().getUser();
                log.debug("winner name : {}",winner);
                auction.setWinner(winner);
                auction = auctionRepository.save(auction);
                log.debug("Updated auction {}",auction);
                notificationService.sendNotification(winner, auction);
            } else {
                log.error(" No Participants in Auction for auction id {} ", auction.getId());
            }
        }
    }

    @Override
    public Boolean findByAuctionPresent(Optional<Auction> auction) {
        return auction.map(value -> bidRepository.findByAuction(value).isPresent()).orElse(Boolean.FALSE);
    }

}