package com.example.palianytsia.controller;

public interface EmailConstants {
    String HELLO = "Hello %s,<br>";
    String SPACE = "<br><br>";
    String HREF = "<a href=";
    String SIGNATURE = "Best regards,<br>Palianytsia team";
    String SBJ_REG="Successful Registration!";
    String SBJ_CHANGES="Changes in your account";
    String MESSAGE_REGISTER = HELLO +
            "Thank you for choosing us!<br>" +
            "Welcome to Palianytsia website. When you log into your account, the following features will be available to you:<br>" +
            "– Place orders<br>" +
            "– View order history<br>" +
            "– Browse our delicious bakes for order<br>" +
            "– Change account information" +
            SPACE +
            "Check " + HREF + "%2$s" + ">our tasty bakery</a>, " +
            " right now and enjoy your driving immediately!" +
            SPACE +
            SIGNATURE;
    String MESSAGE_ADD = HELLO +
            "You just now added new address to your profile!" +
            SPACE +
            SIGNATURE;
    String MESSAGE_EDIT = HELLO +
            "You just now successfully edit your profile data!" +
            SPACE +
            SIGNATURE;
    String MESSAGE_REMOVE = HELLO +
            "Your address was successfully deleted from your profile!" +
            SPACE +
            SIGNATURE;
    String MESSAGE_CHANGE = HELLO +
            "You just now changed your password to a new one!" +
            SPACE +
            SIGNATURE;
}
