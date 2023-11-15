package com.prox.babyvaccinationtracker.adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Comment;
import com.prox.babyvaccinationtracker.model.Post;
import com.prox.babyvaccinationtracker.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postItemList;
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts");;
    String user_id;
    User user;

    List<Comment> commentList;
    CommentAdapter commentAdapter;

    public PostAdapter(List<Post> postItemList, User user) {
        this.postItemList = postItemList;
        this.user = user;
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

        Log.i("Post", "onBindViewHolder: " + postItem.toString());
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
            Log.i("LIKED_USERS", liked_users+" \n "+user_id);
        }
        if (postItem.getComments() != null){
            commentList = new ArrayList<>();

            for (Map.Entry<String, Comment> entry : postItem.getComments().entrySet()) {
                Object commentObject = entry.getValue();
                Log.i("Comment", "onBindViewHolder: " + commentObject.getClass().getName());
                if (commentObject instanceof HashMap) {
                    // If it's a Map, try to convert it to a Comment
                    Comment comment = convertMapToComment((HashMap<String, Object>) commentObject);
                    comment.setComment_id(entry.getKey());
                    if (comment != null) {
                        commentList.add(comment);
                    }
                } else if (commentObject instanceof Comment) {
                    // If it's already a Comment, just add it to the list
                    commentList.add((Comment) commentObject);
                }
            }
            DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference("posts").child(postItem.getPost_id()).child("comments");
            commentAdapter = new CommentAdapter(commentList , commentReference, user);
            holder.recylerViewComments.setLayoutManager(new LinearLayoutManager(holder.recylerViewComments.getContext()));
            holder.recylerViewComments.setAdapter(commentAdapter);
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

        holder.buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = holder.editTextCommentContent.getText().toString();
                if (!commentContent.isEmpty()){
                    Comment comment = new Comment();
                    comment.setContent(commentContent);
                    comment.setUser(user);
                    postReference.child(postItem.getPost_id()).child("comments").push().setValue(comment).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }else {
                            Log.i("Comment", "onClick: Comment failed");
                        }
                    });
                    holder.editTextCommentContent.setText("");
                }
            }
        });

    }

    private Comment convertMapToComment(HashMap<String, Object> commentMap) {
        try {
            Comment comment = new Comment();
            comment.setContent((String) commentMap.get("content"));
            comment.setCreated_at((String) commentMap.get("created_at"));
            comment.setReplies((HashMap<String, Comment>) commentMap.get("replies"));
            if (commentMap.get("user") instanceof HashMap){
                HashMap<String, Object> userMap = (HashMap<String, Object>) commentMap.get("user");
                User user = new User();
                user.setUser_name((String) userMap.get("user_name"));
                user.setUser_avatar((String) userMap.get("user_avatar"));
                user.setUser_id((String) userMap.get("user_id"));
                user.setUser_role((String) userMap.get("user_role"));
                comment.setUser(user);
            }else if (commentMap.get("user") instanceof User){
                comment.setUser((User) commentMap.get("user"));
            }
            Log.i("Alooo", "convertMapToComment: " + comment.toString());
            // Set other fields accordingly
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        EditText editTextCommentContent;
        Button buttonSendComment;

        RecyclerView recylerViewComments;

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
            recylerViewComments = itemView.findViewById(R.id.recyclerViewComments);
            buttonSendComment = itemView.findViewById(R.id.buttonSendComment);
            editTextCommentContent = itemView.findViewById(R.id.editTextCommentContent);

            user_id = sharedPreferences.getString("customer_id", "");

        }
    }
}
