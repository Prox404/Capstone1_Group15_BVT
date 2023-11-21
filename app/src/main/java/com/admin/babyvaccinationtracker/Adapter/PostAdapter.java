package com.admin.babyvaccinationtracker.Adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postItemList;
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts");;
    public PostAdapter(List<Post> postItemList) {
        this.postItemList = postItemList;

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post postItem = postItemList.get(position);
        Log.i("Post", "onBindViewHolder: " + postItem.toString());
        holder.postTime.setText(postItem.getCreated_at());
        holder.postContent.setText(postItem.getContent());
        if (postItem.getImage_url() != null){
            ImageCarouselAdapter postImageAdapter = new ImageCarouselAdapter(holder.viewPagerImage.getContext(), postItem.getImage_url());
            holder.viewPagerImage.setAdapter(postImageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return postItemList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTime;
        TextView postContent;
        ViewPager2 viewPagerImage;
        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user", itemView.getContext().MODE_PRIVATE);

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your views here
            postTime = itemView.findViewById(R.id.textViewTime);
            postContent = itemView.findViewById(R.id.textViewPostContent);
            viewPagerImage = itemView.findViewById(R.id.viewPagerImage);

        }
    }
}

