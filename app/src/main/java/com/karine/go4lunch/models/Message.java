package com.karine.go4lunch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String textMessage;
    private String message;
    private Date dateCreated;
    private User userSender;
    private String urlImage;
    private User user;

    public Message() { }

    /**
     * Constructor
     * @param message
     * @param userSender
     */
    public Message(String message, User userSender, User user) {
        this.message = message;
        this.userSender = userSender;
        this.user = user;
    }

    public Message(String message, String urlImage, User userSender, User user) {
        this.message = message;
        this.urlImage = urlImage;
        this.userSender = userSender;
        this.user = user;
    }

    public Message(String textMessage, User userSender) {
        this.textMessage = textMessage;
        this.userSender = userSender;
    }

    /**
     * Getters
     * @return
     */
    public String getMessage() { return message; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public User getUserSender() { return userSender; }
    public String getUrlImage() { return urlImage; }

    /**
     * Setters
     * @param message
     */
    public void setMessage(String message) { this.message = message; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUserSender(User userSender) { this.userSender = userSender; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
}

