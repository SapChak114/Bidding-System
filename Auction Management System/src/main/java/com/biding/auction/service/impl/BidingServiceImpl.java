package com.biding.auction.service.impl;

import com.biding.auction.constants.SQSConstants;
import com.biding.auction.dao.Auction;
import com.biding.auction.dao.Bid;
import com.biding.auction.dao.Product;
import com.biding.auction.dao.User;
import com.biding.auction.dto.request.BidRequestDto;
import com.biding.auction.dto.request.NotificationRequestDto;
import com.biding.auction.repository.AuctionRepository;
import com.biding.auction.repository.BidRepository;
import com.biding.auction.repository.UserRepository;
import com.biding.auction.service.BidingService;
import com.biding.auction.winningStrategy.WinnerDeterminationContext;
import com.biding.auction.winningStrategy.strategies.HighestAmountAlphabeticalUserNameStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountRandomBidStrategy;
import com.biding.auction.winningStrategy.strategies.HighestBidAmountEarliestFirstStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BidingServiceImpl implements BidingService {

    private final BidRepository bidRepository;

    private final AuctionRepository auctionRepository;

    private final UserRepository userRepository;

    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8083/notify/";
    @Autowired
    public BidingServiceImpl(BidRepository bidRepository, AuctionRepository auctionRepository, UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.mapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

    @SqsListener(value = SQSConstants.SQS_QUEUE_NAME)
    public void receiveMessage(String message) {
        try {
            BidRequestDto bidRequestDto = mapper.readValue(message, BidRequestDto.class);

            User user = userRepository.findById(bidRequestDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Auction auction = auctionRepository.findById(bidRequestDto.getAuctionId())
                    .orElseThrow(() -> new RuntimeException("Auction not found"));

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
    public void determineAuctionWinner() {
        List<Auction> auctions = auctionRepository.findAllByBiddingEndTimeBeforeAndWinnerIsNull(new Date());
        log.info("Auction details to fetch winner : "+auctions);
        for (Auction auction : auctions) {
            List<Bid> bids = bidRepository.findByAuctionId(auction.getId());

            if (bids.isEmpty()) {
                log.info("No bids found for auction with ID: {}", auction.getId());
                continue;
            }

            WinnerDeterminationContext context = new WinnerDeterminationContext(new HighestBidAmountEarliestFirstStrategy());
            Optional<Bid> winningBid = context.determineWinner(bids);

            if (winningBid.isPresent()) {
                User winner = winningBid.get().getUser();
                auction.setWinner(winner);
                auctionRepository.save(auction);

                sendNotification(winner, auction);
            } else {
                log.error(" No Participants in Auction for auction id {} ", auction.getId());
            }
        }
    }

    @Override
    public Boolean findByAuctionPresent(Optional<Auction> auction) {
        return auction.map(value -> bidRepository.findByAuction(value).isPresent()).orElse(Boolean.FALSE);
    }

    private void sendNotification(User winner, Auction auction) {
        try {
            Product product = auction.getProduct();
            NotificationRequestDto notificationDto = NotificationRequestDto.builder()
                    .userName(winner.getName())
                    .userEmail(winner.getEmail())
                    .userContact(winner.getContact())
                    .productName(product.getName())
                    .auctionId(auction.getId())
                    .build();

            URI uri = new URI(NOTIFICATION_SERVICE_URL);
            restTemplate.postForEntity(uri, notificationDto, Void.class);
            log.info("Notification sent to user: {}", winner.getName());
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
        }
    }
}