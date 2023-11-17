package com.admin.babyvaccinationtracker.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Comment;
import com.admin.babyvaccinationtracker.model.Post;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

public class PostVisitsAdapter extends RecyclerView.Adapter<PostVisitsAdapter.viewholder> {

    ArrayList<Post> posts;

    public PostVisitsAdapter (ArrayList<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostVisitsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_content_visits_item,parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostVisitsAdapter.viewholder holder, int position) {
        Post post = posts.get(position);
        Log.i("POSTTTSSS", post.getComments()+"");
        holder.tv_number.setText((position+1)+"");
        holder.tv_post_user.setText(post.getUser().getUser_name());
        holder.tv_post_content.setText(post.getContent());
        holder.tv_post_number_visit.setText(post.getVisitor().size()+"");
        Long number_comment = 0L;
        if (post.getComments() != null){
            number_comment += post.getComments().size();
//            for (Map.Entry<String, Comment> entry : post.getComments().entrySet()) {
//                Object commentObject = entry.getValue();
//                if (commentObject != null && commentObject instanceof HashMap) {
//                    number_comment += countComments((HashMap<String, Object>) commentObject);
//                }
//            }
            number_comment += countComments(post.getComments());

        }
        holder.tv_post_number_comment.setText(number_comment+"");
    }

    private Long countComments(HashMap<String,Comment> comments) {
        Object commentObject = comments.get("replies");
        Long number_comments = 0L;
        if(commentObject != null){
//           number_comments = (long) commentObject.size();
//            for(Map.Entry<String, Object> entry: commentObject.entrySet()){
//                number_comments += countComments((HashMap<String, Object>) entry);
//            }
        }
        return number_comments;
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_number;
        TextView tv_post_user;
        TextView tv_post_content;
        TextView tv_post_number_visit;
        TextView tv_post_number_comment;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_number = itemView.findViewById(R.id.tv_post_number);
            tv_post_user = itemView.findViewById(R.id.tv_post_user);
            tv_post_content = itemView.findViewById(R.id.tv_post_content);
            tv_post_number_visit = itemView.findViewById(R.id.tv_post_number_visits);
            tv_post_number_comment = itemView.findViewById(R.id.tv_post_number_comment);
        }
    }
}
