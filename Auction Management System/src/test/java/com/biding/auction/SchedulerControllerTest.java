package com.biding.auction;

import com.biding.auction.controller.SchedulerController;
import com.biding.auction.service.BidingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
    public void testFindWinner() {
        // Trigger the method manually
        schedulerController.findWinner();
        // Verify that determineAuctionWinners was called once
        verify(bidingService, atLeast(1)).determineAuctionWinners();
    }

}