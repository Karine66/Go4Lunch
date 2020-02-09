package com.karine.go4lunch.models;

import androidx.annotation.Nullable;

public class User {

        private String uid;
        private String username;
        @Nullable
        private String urlPicture;

        public User() { }

    /**
     * Constructor
     * @param uid
     * @param username
     * @param urlPicture
     */
    public User(String uid, String username, @Nullable String urlPicture) {
            this.uid = uid;
            this.username = username;
            this.urlPicture = urlPicture;

        }

    /**
     * Getters
     * @return
     */
    public String getUid() { return uid; }
        public String getUsername() { return username; }
        public String getUrlPicture() { return urlPicture; }

    /**
     * Setters
     * @param username
     */
        public void setUsername(String username) { this.username = username; }
        public void setUid(String uid) { this.uid = uid; }
        public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }

    }

