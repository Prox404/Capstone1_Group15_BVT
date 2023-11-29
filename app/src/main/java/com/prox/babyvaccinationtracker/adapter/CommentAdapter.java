package com.prox.babyvaccinationtracker.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    Post post;
    User user;
    DatabaseReference commentReference;

    public CommentAdapter(List<Comment> comments, DatabaseReference commentReference, User user, Post post) {
        this.comments = comments;
        this.commentReference = commentReference;
        this.user = user;
        this.post = post;
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
                if (comment.getReplies() != null){
                    HashMap<String, Comment> replies = comment.getReplies();
                    Comment newComment = new Comment();
                    newComment.setContent(content);
                    newComment.setUser(user);
                    String commentKey = commentReference.push().getKey();
                    newComment.setComment_id(commentKey);
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
                holder.editTextCommentContent.setText("");
                
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
            CommentAdapter replyAdapter = new CommentAdapter(replyList, commentReference.child(comment.getComment_id()).child("replies"), user, post);
            holder.recyclerViewReplies.setLayoutManager(new LinearLayoutManager(holder.recyclerViewReplies.getContext()));
            holder.recyclerViewReplies.setAdapter(replyAdapter);
        }
        holder.imageEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment.getUser().getUser_id().equals(user.getUser_id())){
                    editComment(view,comment);
                }else {
                    reportComment(view, comment);
                }

            }
        });
    }

    private void reportComment(View view, Comment comment) {
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.report_menu, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.i("IDDDD", id+" "+menuItem.getItemId());
                if (id == R.id.menu_report_item) {
                    // todo xóa comment
                    View view_report = LayoutInflater.from(view.getContext()).inflate(R.layout.report_fragment,null, false);
                    TextView textView = view_report.findViewById(R.id.edt_report_reason);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(view_report).setPositiveButton("Báo cáo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String reason = textView.getText().toString();
                            if(!reason.trim().isEmpty()){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");
                                Report report = new Report();
                                report.setComment(comment);
                                report.setType_report(0);
                                report.setReason(reason);
                                post.setComments(null);
                                report.setPost_id(post.getPost_id());
                                databaseReference.push().setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(),"Đã báo cáo bài viết này",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
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

    private void editComment(View view, Comment comment) {
        PopupMenu MENU = new PopupMenu(view.getContext(), view);
        MENU.getMenuInflater().inflate(R.menu.editcomment_menu, MENU.getMenu());
        MENU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.i("IDDDD", id+" "+menuItem.getItemId());
                if (id == R.id.itemdeletecomment) {
                    // todo xóa comment
                    DatabaseReference reference_delete = FirebaseDatabase.getInstance()
                            .getReference("posts").child(post.getPost_id());
                    reference_delete.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Comment> comments = (HashMap<String, Comment>)
                                    snapshot.child("comments").getValue();
                            if(comments != null){
                                DataSnapshot snapshot_comment = snapshot.child("comments");
                                if(comments.containsKey(comment.getComment_id())){
                                    snapshot_comment.child(comment.getComment_id()).getRef().removeValue();

                                }
                                else {
                                    for(Map.Entry<String, Comment> entry : comments.entrySet()){
                                        Object commentObject = entry.getValue();
                                        Log.i("REPLIESSSSS", commentObject +"");
                                        Log.i("KEYYYYYY", entry.getKey());
                                        if(commentObject != null){
                                            if(remove_comment(snapshot_comment, // đường dẫn để xóa comment
                                                    entry.getKey(), // child cha
                                                    comment.getComment_id(), // comment cần xóa
                                                    (HashMap<String, Object>) commentObject)){
                                                break;
                                            }

                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(view.getContext(), " Đã xóa bình luận thành công", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        MENU.show();
    }

    boolean remove_comment(DataSnapshot snapshot_comment_child,
                           String comment_id,
                           String comment_delete,
                           HashMap<String, Object> Object){
        DataSnapshot snapshot_comment = snapshot_comment_child.child(comment_id);
        Object replies =  Object.get("replies");
        Log.i("replies_1", replies+"");
        if(replies instanceof HashMap && replies != null){
            if(((HashMap<String, Object>) replies).containsKey(comment_delete)){
                Log.i("replies_2", comment_delete + " "+ comment_id);
                Log.i("snapshot_comment", snapshot_comment+"");
                snapshot_comment.child("replies").child(comment_delete).getRef().removeValue();
                return true;
            }
            else {
                for(Map.Entry<String, Object> entry: ((HashMap<String, Object>) replies).entrySet()){
                    Object object = entry.getValue();
                    Log.i("snapshot_replies", object+"");
                    if(object != null && object instanceof HashMap){
                        DataSnapshot snapshot_comment2 = snapshot_comment_child
                                .child(comment_id).child("replies");
                        if(remove_comment(snapshot_comment2,
                                entry.getKey(),
                                comment_delete,
                                (HashMap<String, java.lang.Object>) object)){
                            return true;
                        };
                    }
                }
            }
        }
        return false;
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
        ImageView imageViewUserAvatar,imageEditComment;
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
            imageEditComment = itemView.findViewById(R.id.imageEditComment);

        }
    }

}