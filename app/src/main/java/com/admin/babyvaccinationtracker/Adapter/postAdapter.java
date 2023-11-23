package com.admin.babyvaccinationtracker.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.viewholder> {
    ArrayList<Post> posts;
    public  postAdapter (ArrayList<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public postAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent, false);
        return new viewholder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull postAdapter.viewholder holder, int position) {
        Post post = posts.get(position);
        holder.textViewUsername.setText(post.getUser().getUser_name());
        holder.textViewTime.setText(post.getCreated_at());
        holder.textViewPostContent.setText(post.getContent());
        String hashtag = "";
        for(String a : post.getHashtags()){
            hashtag = " "+ a;
        }

        holder.textViewHashtag.setText(hashtag);
        String image_user = post.getUser().getUser_avatar();
//        image_user = image_user.contains("http") ? image_user.replace("http", "https") : image_user;
        Picasso.get().load(image_user).into(holder.imageViewUserAvatar);

        if (post.getImage_url() != null) {
            ImageCarouselAdapter postImageAdapter = new ImageCarouselAdapter(holder.viewPagerImage.getContext(), post.getImage_url());
            holder.viewPagerImage.setAdapter(postImageAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageViewUserAvatar;
        TextView textViewUsername,textViewPostContent,textViewHashtag;
        TextView textViewTime;
        ViewPager2 viewPagerImage;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageViewUserAvatar = itemView.findViewById(R.id.imageViewUserAvatar);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewPostContent = itemView.findViewById(R.id.textViewPostContent);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewHashtag = itemView.findViewById(R.id.textViewHashtag);
            viewPagerImage = itemView.findViewById(R.id.viewPagerImage);
        }
    }
}
