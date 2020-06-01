package com.karine.go4lunch.controllers.fragments;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karine.go4lunch.API.ChatHelper;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.Message;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.views.ChatAdapter;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;


import static com.karine.go4lunch.utils.FirebaseUtils.getCurrentUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements ChatAdapter.Listener {


    private static final int RESULT_OK = 300;
    // FOR DESIGN
    // Getting all views needed
    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chat_text_view_recycler_view_empty)
    TextView textViewRecyclerViewEmpty;
    @BindView(R.id.chat_message_edit_text)
    EditText editTextMessage;
    @BindView(R.id.chat_image_chosen_preview)
    ImageView imageViewPreview;

    // FOR DATA
//    Declaring Adapter and data
    private ChatAdapter chatAdapter;
    @Nullable
    private User modelCurrentUser;
    private String currentChatName;
    private String chatName;
    private String chat;
    private Uri uriImageSelected;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActionBar().setTitle(R.string.chat_workmates);
    }
    //For permission images
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //For permission images
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }


    // --------------------
        // ACTIONS
        // --------------------

    @OnClick(R.id.chat_send_button)
    public void onClickSendMessage() {
        // 1 - Check if text field is not empty and current user properly downloaded from Firestore
        if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
            if (this.imageViewPreview.getDrawable() == null) {
                // SEND A TEXT MESSAGE
                ChatHelper.createMessageForChat(editTextMessage.getText().toString(), modelCurrentUser).addOnFailureListener(this.onFailureListener());
                this.editTextMessage.setText("");
            } else {
                // SEND A IMAGE + TEXT IMAGE
                this.uploadPhotoInFirebaseAndSendMessage(editTextMessage.getText().toString());
                this.editTextMessage.setText("");
                this.imageViewPreview.setImageDrawable(null);
            }
        }
//            // 2 - Create a new Message to Firestore
//            ChatHelper.createMessageForChat(editTextMessage.getText().toString(), modelCurrentUser).addOnFailureListener(this.onFailureListener());
//            // 3 - Reset text field
//            this.editTextMessage.setText("");
//        }
    }
        //For insert image
        @OnClick(R.id.chat_add_file_button)
        public void onClickAddFile() {
        this.chooseImageFromPhone();
        }

        // --------------------
        // REST REQUESTS
        // --------------------
        // Get Current User from Firestore
        private void getCurrentUserFromFirestore(){
            UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    modelCurrentUser = documentSnapshot.toObject(User.class);
                }
            });
        }

//    private void uploadPhotoInFirebaseAndSendMessage(final String message) {
//        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
////       UPLOAD TO GCS
//        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
//        mImageRef.putFile(this.uriImageSelected)
//                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        String pathImageSavedInFirebase = taskSnapshot.getMetadata().getDownloadUrl().toString();
//                        // SAVE MESSAGE IN FIRESTORE
//                        ChatHelper.createMessageWithImageForChat(pathImageSavedInFirebase, message, modelCurrentUser).addOnFailureListener(onFailureListener());
//                    }
//                })
//                .addOnFailureListener(this.onFailureListener());
//    }
    //  Upload a picture in Firebase and send a message
    private void uploadPhotoInFirebaseAndSendMessage(final String message) {
        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        //  UPLOAD TO GCS
        final StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        UploadTask uploadTask = mImageRef.putFile(this.uriImageSelected);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e("UploadPhotoChat", "Error TASK_URI : " + task.getException());
                    throw Objects.requireNonNull(task.getException());
                }
                // Continue with the task to get the download URL
                return mImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    ChatHelper.createMessageWithImageForChat(Objects.requireNonNull(downloadUri).toString(), message, modelCurrentUser).addOnFailureListener(onFailureListener());
                } else {
                    Log.e("UploadPhotoChat", "Error ON_COMPLETE : " + task.getException());
                }
            }
        });
    }
    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewPreview);
            } else {
                StyleableToast.makeText(Objects.requireNonNull(getContext()),getString(R.string.toast_title_no_image_chosen), R.style.personalizedToast).show();
            }
        }
    }



    // --------------------
        // UI
        // --------------------
        //Configure RecyclerView with a Query
        private void configureRecyclerView(){
            //Track current chat name
//            this.currentChatName = chatName;
            //Configure Adapter & RecyclerView
            this.chatAdapter = new ChatAdapter(generateOptionsForAdapter(ChatHelper.getAllMessageForChat(chat)), Glide.with(this), this, Objects.requireNonNull(getCurrentUser()).getUid());
            chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()); // Scroll to bottom on new messages
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(this.chatAdapter);
        }

        // Create options for RecyclerView from a Query
        private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
            return new FirestoreRecyclerOptions.Builder<Message>()
                    .setQuery(query, Message.class)
                    .setLifecycleOwner(this)
                    .build();
        }

        // --------------------
        // CALLBACK
        // --------------------

        @Override
        public void onDataChanged() {
            // 7 - Show TextView in case RecyclerView is empty
            textViewRecyclerViewEmpty.setVisibility(this.chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }


    }

