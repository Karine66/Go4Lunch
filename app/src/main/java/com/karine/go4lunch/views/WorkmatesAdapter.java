package com.karine.go4lunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;



import com.karine.go4lunch.R;
import com.karine.go4lunch.models.User;

import java.util.Collections;
import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private List<User> userList;
    private RequestManager glide;

    //Constructor
    public WorkmatesAdapter (List<User> userList, RequestManager glide) {
        this.userList = userList;
        this.glide = glide;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_work_mates_item, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
    holder.updateWithDetails(this.glide, Collections.singletonList(this.userList.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }
}
