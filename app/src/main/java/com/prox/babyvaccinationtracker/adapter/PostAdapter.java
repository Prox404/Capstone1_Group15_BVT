package com.prox.babyvaccinationtracker.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Comment;
import com.prox.babyvaccinationtracker.model.Post;
import com.prox.babyvaccinationtracker.model.Report;
import com.prox.babyvaccinationtracker.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postItemList;
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts");
    String user_id;
    User user;

    List<Comment> commentList;
    CommentAdapter commentAdapter;


    public PostAdapter(List<Post> postItemList, User user) {
        this.postItemList = postItemList;
        this.user = user;
        Log.i("Post", "PostAdapter: " + "render");
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
        HashMap<String, Boolean> visitors;
        if (postItem.getVisitor() != null) {
            visitors = new HashMap<String, Boolean>(postItem.getVisitor());
        } else {
            visitors = new HashMap<String, Boolean>();
        }
        Log.i("Post", "onBindViewHolder: " + postItem);
        holder.userName.setText(postItem.getUser().getUser_name());
        holder.postTime.setText(postItem.getCreated_at());
        holder.postContent.setText(postItem.getContent());
        if (postItem.getHashtags() != null)
            holder.textViewHashtag.setText(String.join(" ", postItem.getHashtags()));
        if (postItem.getUser().getUser_avatar() != null)
            Picasso.get().load(postItem.getUser().getUser_avatar()).into(holder.userAvatar);
        if (postItem.getImage_url() != null) {
            ImageCarouselAdapter postImageAdapter = new ImageCarouselAdapter(holder.viewPagerImage.getContext(), postItem.getImage_url());
            holder.viewPagerImage.setAdapter(postImageAdapter);
        }
        if (postItem.getLiked_users() != null) {
            ArrayList<String> liked_users = postItem.getLiked_users();

            if (liked_users.contains(user_id)) {
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart_solid);
            } else {
                holder.imageViewHeart.setImageResource(R.drawable.ic_heart);
            }
            Log.i("LIKED_USERS", liked_users + " \n " + user_id);
        }
        if (postItem.getComments() != null) {
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
            commentAdapter = new CommentAdapter(commentList, commentReference, user, postItem);
            holder.recylerViewComments.setLayoutManager(new LinearLayoutManager(holder.recylerViewComments.getContext()));
            holder.recylerViewComments.setAdapter(commentAdapter);
        }

        if (postItem.getImage_url() != null && postItem.getImage_url().size() >0){
            holder.viewPagerImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    if (!visitors.containsKey(user_id)) {
                        Log.i("VISITORS", visitors + "");
                        postReference.child(postItem.getPost_id()).child("Visitors").child(user_id).setValue(true);
                    }
                }
            });
        }else {
            holder.viewPagerImage.setVisibility(View.GONE);
        }

        holder.likeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> liked_users = new ArrayList<>();
                if (postItem.getLiked_users() != null)
                    liked_users = postItem.getLiked_users();
                if (liked_users.contains(user_id)) {
                    holder.imageViewHeart.setImageResource(R.drawable.ic_heart);
                    liked_users.remove(user_id);
                } else {
                    holder.imageViewHeart.setImageResource(R.drawable.ic_heart_solid);
                    liked_users.add(user_id);
                }
                postItem.setLiked_users(liked_users);
                postReference.child(postItem.getPost_id()).child("liked_users").setValue(liked_users);
                Log.i("VISITORS", visitors + "");
                if (!visitors.containsKey(user_id)) {
                    Log.i("VISITORS", visitors + "");
                    postReference.child(postItem.getPost_id()).child("Visitors").child(user_id).setValue(true);
                }
            }
        });


        holder.buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = holder.editTextCommentContent.getText().toString();
                if (!commentContent.isEmpty()) {
                    Comment comment = new Comment();
                    comment.setContent(commentContent);
                    comment.setUser(user);
                    DatabaseReference commentRef = postReference.child(postItem.getPost_id()).child("comments").push();
                    commentRef.setValue(comment).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("Comment", "onClick: Comment success");
                        } else {
                            Log.i("Comment", "onClick: Comment failed");
                        }
                    });
                    holder.editTextCommentContent.setText("");
                    if (!visitors.containsKey(user_id)) {
                        postReference.child(postItem.getPost_id()).child("Visitors").child(user_id).setValue(true);
                    }
                }

            }
        });
        holder.imageEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_post = postItem.getPost_id();
                if(postItem.getUser().getUser_id().equals(user_id)){
                    Showmenuedit(view,id_post);
                }
                else {
                    report_menu(view,postItem);
                }
            }
        });
    }

    private void Showmenuedit(View view, String id_post){
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.editpost_menu, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.i("IDDDD", id+" "+menuItem.getItemId());
                if (id == R.id.itemdeletepost) {
                    // thêm xóa hồi nãy làm bên admin
                    DatabaseReference EditContent = FirebaseDatabase.getInstance().getReference("posts");
                    EditContent.child(id_post).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    Toast.makeText(view.getContext(), " Đã xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.itemeditpost_edit) {
                    //todo cập nhập bài viết
                    Toast.makeText(view.getContext(), " Đã cập nhật bài viết thành công", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        MENU.show();
    }
    private void report_menu(View view, Post post){
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.report_menu, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.menu_report_item){
                    View report_view = LayoutInflater.from(view.getContext()).inflate(R.layout.report_fragment,null, false );
                    TextView edt_report_reason = report_view.findViewById(R.id.edt_report_reason);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(report_view).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).setPositiveButton("Báo cáo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference ReportContent = FirebaseDatabase.getInstance().getReference("Report");
                            String str_report = edt_report_reason.getText().toString();
                            if(!str_report.isEmpty()){
                                Report report = new Report();
                                report.setType_report(1);
                                report.setPost(post);
                                report.setReason(str_report);
                                ReportContent.push().setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(), "Báo cáo thành công", Toast.LENGTH_LONG).show();
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
            Log.i("Alooo", "convertMapToComment: " + comment);
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
        ImageView userAvatar, imageViewHeart,imageViewComment,imageEditPost;
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
            imageViewComment = itemView.findViewById(R.id.imageViewComment);
            imageEditPost = itemView.findViewById(R.id.imageEditPost);
            user_id = sharedPreferences.getString("customer_id", "");

        }
    }
}
