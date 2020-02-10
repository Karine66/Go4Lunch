package com.karine.go4lunch.API;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatHelper {

    private static final String COLLECTION_NAME = "chats";

    /**
     * Collection reference
     * @return
     */

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
}
