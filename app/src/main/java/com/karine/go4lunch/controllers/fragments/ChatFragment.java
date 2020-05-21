package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.karine.go4lunch.API.ChatHelper;
import com.karine.go4lunch.API.MessageHelper;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.Message;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.utils.FirebaseUtils;
import com.karine.go4lunch.views.ChatAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karine.go4lunch.utils.FirebaseUtils.getCurrentUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements ChatAdapter.Listener {


    // FOR DESIGN
    // Getting all views needed
    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chat_text_view_recycler_view_empty)
    TextView textViewRecyclerViewEmpty;
    @BindView(R.id.chat_message_edit_text)
    TextInputEditText editTextMessage;
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
        getActionBar().setTitle("Chat with Workmates");
    }

        // --------------------
        // ACTIONS
        // --------------------

        @OnClick(R.id.chat_send_button)
        public void onClickSendMessage() {
            //Check if text field is not empty and current user properly downloaded from Firestore
            if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
                //Create a new Message to Firestore
                ChatHelper.createMessageForChat(editTextMessage.getText().toString(), this.currentChatName, modelCurrentUser).addOnFailureListener(this.onFailureListener());
                //Reset text field
                this.editTextMessage.setText("");

            }
        }

        @OnClick(R.id.chat_add_file_button)
        public void onClickAddFile() { }

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

