package com.biding.notification.constants;

public class NotificationConstants {

    public static String SMS_BODY = "Hi %s, you have won the auction! Auction ID: %s, Product: %s";

    public static String EMAIL_BODY = "<html>" +
            "<body>" +
            "<h1>Congratulations %s!</h1>" +
            "<p>You have won the auction for the product: <strong>%s</strong>.</p>" +
            "<p><strong>Auction ID:</strong> %s</p>" +
            "<p>Thank you for participating in the auction. We hope you enjoy your new purchase!</p>" +
            "<p>Best regards,<br/>The Auction Team</p>" +
            "</body>" +
            "</html>";

    public static String EMAIL_SUBJECT = "Congratulations! You have won the auction";
}
