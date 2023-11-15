package com.vaccinecenter.babyvaccinationtracker.Adapter;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.Comment;
import com.vaccinecenter.babyvaccinationtracker.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    String post_id;

    DatabaseReference commentReference;

    public CommentAdapter(List<Comment> comments, DatabaseReference commentReference) {
        this.comments = comments;
        this.commentReference = commentReference;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Log.i("Siuuuuuuuu", "onBindViewHolder: " + comment.toString());
        holder.textViewUserName.setText(comment.getUser().getUser_name());
        holder.textViewComment.setText(comment.getContent());
        Picasso.get().load(comment.getUser().getUser_avatar()).into(holder.imageViewUserAvatar);

        holder.textViewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commentContainer.setVisibility(View.VISIBLE);
                holder.textViewReply.setVisibility(View.GONE);
            }
        });

        holder.textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commentContainer.setVisibility(View.GONE);
                holder.textViewReply.setVisibility(View.VISIBLE);
            }
        });

        holder.buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = commentReference.push().getKey();
                String content = holder.editTextCommentContent.getText().toString();

                User user = new User();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("user", view.getContext().MODE_PRIVATE);
                user.setUser_name(sharedPreferences.getString("center_name", ""));
                user.setUser_avatar(sharedPreferences.getString("center_image", ""));
                user.setUser_id(sharedPreferences.getString("center_id", ""));
                user.setUser_role("center");

                if (comment.getReplies() != null){
                    HashMap<String, Comment> replies = comment.getReplies();
                    Comment newComment = new Comment();
                    newComment.setContent(content);
                    newComment.setUser(user);
                    String commentKey = commentReference.push().getKey();
                    replies.put(commentKey, newComment);
                    commentReference.child(comment.getComment_id()).child("replies").setValue(replies);
                    comment.setReplies(replies);
                }else {
                    HashMap<String, Comment> replies = new HashMap<>();
                    Comment newComment = new Comment();
                    newComment.setContent(content);
                    newComment.setUser(user);
                    String commentKey = commentReference.push().getKey();
                    newComment.setComment_id(commentKey);
                    replies.put(commentKey, newComment);
                    commentReference.child(comment.getComment_id()).child("replies").setValue(replies);
                    comment.setReplies(replies);
                }
                holder.commentContainer.setVisibility(View.GONE);
                holder.textViewReply.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        if(comment.getReplies() != null && comment.getReplies().size() > 0){
            HashMap<String, Comment> replies = comment.getReplies();
            List<Comment> replyList = new ArrayList<>();
            // Lấy danh sách các câu trả lời (replies) của bình luận (comment)
            for (Map.Entry<String, Comment> entry : comment.getReplies().entrySet()) {
                Object commentObject = entry.getValue();
                if (commentObject instanceof HashMap) {
                    // Nếu đó là một HashMap, thử chuyển đổi nó thành một Comment
                    Comment reply = convertMapToComment((HashMap<String, Object>) commentObject);
                    reply.setComment_id(entry.getKey());
                    if (reply != null) {
                        replyList.add(reply);
                    }
                } else if (commentObject instanceof Comment) {
                    // Nếu đó đã là một Comment, chỉ cần thêm nó vào danh sách
                    replyList.add((Comment) commentObject);
                }
            }
            CommentAdapter replyAdapter = new CommentAdapter(replyList, commentReference.child(comment.getComment_id()).child("replies"));
            holder.recyclerViewReplies.setLayoutManager(new LinearLayoutManager(holder.recyclerViewReplies.getContext()));
            holder.recyclerViewReplies.setAdapter(replyAdapter);
        }
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
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComment,textViewUserName, textViewReply, textViewCancel;
        ImageView imageViewUserAvatar;
        LinearLayout commentContainer;
        EditText editTextCommentContent;
        Button buttonSendComment;

        RecyclerView recyclerViewReplies;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewCommentUserName);
            textViewComment = itemView.findViewById(R.id.textViewCommentContent);
            imageViewUserAvatar = itemView.findViewById(R.id.imageViewCommentUserAvatar);
            textViewReply = itemView.findViewById(R.id.textViewReply);
            commentContainer = itemView.findViewById(R.id.commentContainer);
            textViewCancel = itemView.findViewById(R.id.textViewCancel);
            editTextCommentContent = itemView.findViewById(R.id.editTextCommentContent);
            buttonSendComment = itemView.findViewById(R.id.buttonSendComment);
            recyclerViewReplies = itemView.findViewById(R.id.recyclerViewReplies);
        }
    }

}