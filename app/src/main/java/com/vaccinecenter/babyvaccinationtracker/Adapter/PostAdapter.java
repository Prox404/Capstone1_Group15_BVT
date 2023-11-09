package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postItemList;
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts");;
    String user_id;

    public PostAdapter(List<Post> postItemList) {
        this.postItemList = postItemList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post postItem = postItemList.get(position);

        holder.userName.setText(postItem.getUser().getUser_name());
        holder.postTime.setText(postItem.getCreated_at());
        holder.postContent.setText(postItem.getContent());
        if(postItem.getHashtags() != null)
            holder.textViewHashtag.setText(String.join(" ", postItem.getHashtags()));
        if (postItem.getUser().getUser_avatar() != null)
            Picasso.get().load(postItem.getUser().getUser_avatar()).into(holder.userAvatar);
        if (postItem.getImage_url() != null){
            ImageCarouselAdapter postImageAdapter = new ImageCarouselAdapter(holder.viewPagerImage.getContext(), postItem.getImage_url());
            holder.viewPagerImage.setAdapter(postImageAdapter);
        }
        if (postItem.getLiked_users() != null){
            ArrayList<String> liked_users = postItem.getLiked_users();
            if (liked_users.contains(user_id)){
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart_solid);
            }else {
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart);
            }
        }

        holder.likeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> liked_users = new ArrayList<>();
                if (postItem.getLiked_users() != null)
                    liked_users = postItem.getLiked_users();
                if (liked_users.contains(user_id)){
                    holder.imageViewHeart.setImageResource(R.drawable.ic_heart);
                    liked_users.remove(user_id);
                }else {
                    holder.imageViewHeart.setImageResource(R.drawable.ic_heart_solid);
                    liked_users.add(user_id);
                }
                postItem.setLiked_users(liked_users);
                postReference.child(postItem.getPost_id()).child("liked_users").setValue(liked_users);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postItemList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView postTime;
        TextView postContent;
        TextView textViewHashtag;
        ImageView userAvatar, imageViewHeart;
        ViewPager2 viewPagerImage;
        LinearLayout likeContainer, commentContainer;
        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user", itemView.getContext().MODE_PRIVATE);

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            userName = itemView.findViewById(R.id.textViewUsername);
            postTime = itemView.findViewById(R.id.textViewTime);
            postContent = itemView.findViewById(R.id.textViewPostContent);
            textViewHashtag = itemView.findViewById(R.id.textViewHashtag);
            userAvatar = itemView.findViewById(R.id.imageViewUserAvatar);
            viewPagerImage = itemView.findViewById(R.id.viewPagerImage);
            likeContainer = itemView.findViewById(R.id.likeContainer);
            commentContainer = itemView.findViewById(R.id.commentContainer);
            imageViewHeart = itemView.findViewById(R.id.imageViewHeart);

            user_id = sharedPreferences.getString("center_id", "");
        }
    }
}
