package com.biding.auction;

import com.biding.auction.controller.SchedulerController;
import com.biding.auction.service.BidingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.AssertionErrors;

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
        verify(bidingService, times(1)).determineAuctionWinners();
    }

    @Test
    public void testFindWinnerException() {
        doThrow(new RuntimeException("Test Exception")).when(bidingService).determineAuctionWinners();

        try {
            schedulerController.findWinner();
            AssertionErrors.fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            // Assert: Verify that the exception is thrown
            Assertions.assertEquals("Test Exception", e.getMessage());
        }

        // Assert: Verify that the determineAuctionWinners method was called
        verify(bidingService, times(1)).determineAuctionWinners();
    }
}