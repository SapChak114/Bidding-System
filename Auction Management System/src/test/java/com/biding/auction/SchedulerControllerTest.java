package com.biding.auction;

import com.biding.auction.controller.SchedulerController;
import com.biding.auction.service.BidingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class SchedulerControllerTest {

    @Mock
    private BidingService bidingService;

    @InjectMocks
    private SchedulerController schedulerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindWinner() {
        // Trigger the method manually
        schedulerController.findWinner();

        // Verify that determineAuctionWinners was called once
        verify(bidingService, times(1)).determineAuctionWinner();
    }

    @Test
    void testFindWinnerException() {
        // Simulate an exception in determineAuctionWinners
        doThrow(new RuntimeException("Test Exception")).when(bidingService).determineAuctionWinner();

        // Trigger the method manually
        schedulerController.findWinner();

        // Verify that determineAuctionWinners was called once
        verify(bidingService, times(1)).determineAuctionWinner();
    }
}