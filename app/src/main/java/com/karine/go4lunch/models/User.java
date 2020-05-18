package com.karine.go4lunch.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    private boolean userChat;
    private String reference;
    private String uid;
        private String username;
        @Nullable
        private String urlPicture;
        private String placeId;

    private ArrayList<String> like;

    public User() { }

    /**
     * Constructor
     * @param uid
     * @param username
     * @param urlPicture
     */
    public User(String uid, String username, @Nullable String urlPicture, String placeId, ArrayList<String> like) {
            this.uid = uid;
            this.username = username;
            this.urlPicture = urlPicture;
            this.placeId = placeId;
            this.like = like;
            this.userChat = false;


        }

    /**
     * Getters
     * @return
     */
    public String getUid() { return uid; }
        public String getUsername() { return username; }
        public String getUrlPicture() { return urlPicture; }
        public String getPlaceId(){
        return placeId;
        }
        public ArrayList<String> getLike() {
        return like;
        }
        public Boolean getUserChat() { return userChat; }

    /**
     * Setters
     * @param username
     */
        public void setUsername(String username) { this.username = username; }
        public void setUid(String uid) { this.uid = uid; }
        public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }
        public void setLike(ArrayList<String> like) {
            this.like = like;
        }
        public void setUserChat(Boolean chatUser) { userChat = chatUser; }
}

