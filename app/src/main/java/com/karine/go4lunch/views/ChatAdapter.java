package com.karine.go4lunch.views;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.Message;

public class ChatAdapter extends FirestoreRecyclerAdapter<Message, ChatViewHolder> {

        public interface Listener {
            void onDataChanged();
        }

        //FOR DATA
        private final RequestManager glide;
        private final String idCurrentUser;

        //FOR COMMUNICATION
        private Listener callback;

    /**
     * constructor
     * @param options
     * @param glide
     * @param callback
     * @param idCurrentUser
     */
        public ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide, Listener callback, String idCurrentUser) {
            super(options);
            this.glide = glide;
            this.callback = callback;
            this.idCurrentUser = idCurrentUser;
        }

    /**
     * create viewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_chat_item, parent, false));
        }

    /**
     * Update viewHolder
     * @param holder
     * @param position
     * @param model
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Message model) {
        holder.updateWithMessage(model, this.idCurrentUser, this.glide);
    }

        @Override
        public void onDataChanged() {
            super.onDataChanged();
            this.callback.onDataChanged();
        }
}
