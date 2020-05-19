package com.karine.go4lunch.API;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatHelper {

    private static final String COLLECTION_NAME = "chats";



    public static Query getAllMessageForChat(String chat){
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);     }
}
