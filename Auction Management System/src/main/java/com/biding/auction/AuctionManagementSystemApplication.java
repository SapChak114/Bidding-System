package com.biding.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class AuctionManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionManagementSystemApplication.class, args);
    }

}
