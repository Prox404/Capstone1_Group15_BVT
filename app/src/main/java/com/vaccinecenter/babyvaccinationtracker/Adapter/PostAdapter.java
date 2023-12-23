package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.Activity_Edit_post;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.Comment;
import com.vaccinecenter.babyvaccinationtracker.model.Post;
import com.vaccinecenter.babyvaccinationtracker.model.Report;
import com.vaccinecenter.babyvaccinationtracker.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postItemList;
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts");;
    String user_id;
    User user;

    List<Comment> commentList = new ArrayList<>();
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

        if (postItem.getImage_url().size() == 0)
            holder.viewPagerImage.setVisibility(View.GONE);
        else
            holder.viewPagerImage.setVisibility(View.VISIBLE);

        if (postItem.getLiked_users() != null){
            ArrayList<String> liked_users = postItem.getLiked_users();
            if (liked_users.contains(user_id)){
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart_solid);
            }else {
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart);
            }
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
            holder.recylerViewComments.setLayoutManager(new LinearLayoutManager(holder.recylerViewComments.getContext()));
            commentAdapter = new CommentAdapter(commentList , commentReference, postItem.getPost_id());
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
                    DatabaseReference commentRef = postReference.child(postItem.getPost_id()).child("comments").push();
                    commentRef.setValue(comment).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Log.i("Comment", "onClick: Comment success");
                        }else {
                            Log.i("Comment", "onClick: Comment failed");
                        }
                    });
                    holder.editTextCommentContent.setText("");
                }
            }
        });
        holder.imageButtonPostMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postItem.getUser().getUser_id().equals(user_id)){
                    Showmenuedit(view, postItem);
                }
                else {
                    ReportMenu(view, postItem);

                }
            }
        });
    }
    private void ReportMenu(View view, Post post){
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.report_menu, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.menu_report_item){
                    View report_view = LayoutInflater.from
                                    (view.getContext())
                            .inflate(R.layout.report_fragment,null, false );
                    TextView edt_report_reason = report_view.findViewById(R.id.edt_report_reason);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(report_view)
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setPositiveButton("Báo cáo", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference ReportContent = FirebaseDatabase
                                            .getInstance().getReference("Report");
                                    String str_report = edt_report_reason.getText().toString().trim();

                                    if(!str_report.isEmpty()){
                                        Report report = new Report();
                                        report.setType_report(1);
                                        post.setComments(null); // đặt lại comment
                                        report.setPost(post);
                                        report.setReason(str_report);
                                        ReportContent.push()
                                                .setValue(report)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(view.getContext(),
                                                                "Báo cáo thành công",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                }
                            });
                    builder.create().show();
                    return true;
                }
                return false;
            }
        });
        MENU.show();
    }
    private void Showmenuedit(View view, Post post_edit){
        String id_post = post_edit.getPost_id();
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.edit_delete, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.itemdeletepost) {
                    DatabaseReference EditContent = FirebaseDatabase.getInstance().getReference("posts");
                    EditContent.child(id_post)
                            .setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(view.getContext(),
                                            " Đã xóa bài viết thành công",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    return true;
                } else if (id == R.id.itemeditpost_edit) {
                    //todo cập nhập bài viết
                    Intent intent = new Intent(view.getContext(), Activity_Edit_post.class);
                    intent.putExtra("Post", post_edit);
                    view.getContext().startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        MENU.show();
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
        ImageButton imageButtonPostMenu;

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
            imageButtonPostMenu = itemView.findViewById(R.id.imageButtonPostMenu);

            user_id = sharedPreferences.getString("center_id", "");
        }
    }
}
