package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.BlockDeleteComments;
import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Comment;
import com.admin.babyvaccinationtracker.model.Report;
import com.admin.babyvaccinationtracker.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportCommentAdapter extends RecyclerView.Adapter<ReportCommentAdapter.viewholder> {

    ArrayList<Report> reports;
    Context context;
    public ReportCommentAdapter(ArrayList<Report> reports, Context context){
        this.context = context;
        this.reports =  reports;
    }
    public void setReport(ArrayList<Report> reports){
        this.reports = reports;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportCommentAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_comment, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCommentAdapter.viewholder holder, int position) {
        holder.lv_report_comment.setVisibility(View.GONE);
        holder.image_buttonShowComments.setImageResource(R.drawable.ic_arrow_down);
        Report report = reports.get(position);
        Comment comment_reported = report.getComment();

        String comment_id = comment_reported.getComment_id();
        String url_avatar = report.getComment().getUser().getUser_avatar();
        String post_id = report.getPost_id();

        url_avatar = url_avatar.contains("https") ? url_avatar : url_avatar.replace("http", "https");
        Picasso.get().load(url_avatar).into(holder.imageViewCommentUserAvatar);

        String username = report.getComment().getUser().getUser_name();
        holder.textViewCommentUserName.setText(username);

        String content = report.getComment().getContent();
        holder.textViewCommentContent.setText(content);

        ArrayList<String> reasons = report.getReasons();
//        ArrayAdapter<String> adapter = new
//                ArrayAdapter<>(
//                        holder.lv_report_comment.getContext(),
//                android.R.layout.simple_list_item_1,
//                reasons);
        ReasonsAdapter adapter = new ReasonsAdapter(reasons);
        holder.lv_report_comment.setLayoutManager(new LinearLayoutManager(holder.lv_report_comment.getContext()));
        holder.lv_report_comment.setAdapter(adapter);

        holder.image_buttonShowComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.lv_report_comment.getVisibility() == View.GONE){
                    holder.lv_report_comment.setVisibility(View.VISIBLE);
                    holder.image_buttonShowComments.setImageResource(R.drawable.ic_arrow_up);
                }else {
                    holder.lv_report_comment.setVisibility(View.GONE);
                    holder.image_buttonShowComments.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        holder.imageViewMore_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view,comment_id, post_id, comment_reported.getUser());
            }
        });
    }

    void showMenu(View view, String comment_id, String post_id, User user){
        PopupMenu menu = new PopupMenu(view.getContext(),view);
        menu.getMenuInflater().inflate(R.menu.menu_block_comment_delete, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.item_delete_comment){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa bình luận này!!")
                            .setMessage("Lưu ý: sau khi xóa bình luận này thì những báo cáo này sẽ mất")
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Remove_report(comment_id);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(post_id);
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            HashMap<String, Comment> comments = (HashMap<String, Comment>)
                                                    snapshot.child("comments").getValue();
                                            if(comments != null){
                                                DataSnapshot snapshot_comment = snapshot.child("comments");
                                                if(comments.containsKey(comment_id)){
                                                    snapshot_comment.child(comment_id).getRef().removeValue();

                                                }
                                                else {
                                                    for(Map.Entry<String, Comment> entry : comments.entrySet()){
                                                        Object commentObject = entry.getValue();
                                                        Log.i("REPLIESSSSS", commentObject +"");
                                                        Log.i("KEYYYYYY", entry.getKey());
                                                        if(commentObject != null){
                                                            remove_comment(snapshot_comment, // đường dẫn để xóa comment
                                                                    entry.getKey(), // child cha
                                                                    comment_id, // comment cần xóa
                                                                    (HashMap<String, Object>) commentObject);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                    builder.create().show();
                    return true;
                }
                else if(id == R.id.item_delete_block_comment){
                    if(user.getUser_role().equals("customer")){
                        block_delete_comment(user.getUser_name(), user.getUser_id(), 1, post_id, comment_id);
                    }else if(user.getUser_role().equals("center")){
                        block_delete_comment(user.getUser_name(), user.getUser_id(), 0, post_id, comment_id);
                    }
                    return true;
                }
                return false;
            }
        });
        menu.show();
    }
    void block_delete_comment(String cus_name, String cus_id,int isCus, String post_id, String comment_id){
        Bundle args = new Bundle();
        args.putInt("isCus",isCus);
        args.putString("user_name", cus_name);
        args.putString("user_id", cus_id );
        args.putString("post_id", post_id );
        args.putString("comment_id", comment_id );
        BlockDeleteComments blockUser = new BlockDeleteComments();
        blockUser.setArguments(args);
        blockUser.show(((AppCompatActivity) context).getSupportFragmentManager(), "Chặn người dùng");

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
    void Remove_report(String comment_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
        Query query = reference.orderByChild("comment/comment_id").equalTo(comment_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageViewCommentUserAvatar,imageViewMore_comment;
        TextView textViewCommentUserName,textViewCommentContent;
        RecyclerView lv_report_comment;
        ImageButton image_buttonShowComments;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageViewCommentUserAvatar = itemView.findViewById(R.id.imageViewCommentUserAvatar);
            textViewCommentUserName = itemView.findViewById(R.id.textViewCommentUserName);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
            lv_report_comment = itemView.findViewById(R.id.lv_report_comment);
            imageViewMore_comment = itemView.findViewById(R.id.imageViewmore_comment);
            image_buttonShowComments = itemView.findViewById(R.id.image_buttonShowComments);
        }
    }
}
