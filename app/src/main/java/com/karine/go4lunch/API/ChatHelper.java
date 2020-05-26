package com.karine.go4lunch.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.karine.go4lunch.models.Message;
import com.karine.go4lunch.models.User;

public class ChatHelper {

    private static final String COLLECTION_NAME = "chats";

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Query getAllMessageForChat(String chat){
        return FirebaseFirestore.getInstance()

                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);     }

    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender){

        // Create the Message object
        Message message = new Message(textMessage, userSender);

        // Store Message to Firestore
        return ChatHelper.getChatCollection()
//                .document(chat)
//                .collection(COLLECTION_NAME)
                .add(message);
    }


    public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, User userSender){
        Message message = new Message(urlImage,textMessage, userSender);
        return ChatHelper.getChatCollection()
//                .document(chat)
//                .collection(COLLECTION_NAME)
                .add(message);
    }
}

