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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

public class PostVisitsAdapter extends RecyclerView.Adapter<PostVisitsAdapter.viewholder> {
    ArrayList<Post> posts_all;
    ArrayList<Post> posts;
    public PostVisitsAdapter (ArrayList<Post> posts){
        this.posts_all = posts;
        arraylist_all();
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
        int number_comment = 0;
        if (post.getComments() != null){
            number_comment += post.getComments().size();
            for (Map.Entry<String, Comment> entry : post.getComments().entrySet()) {
                Object commentObject = entry.getValue();
                if (commentObject != null && commentObject instanceof HashMap) {
                    number_comment += countComments((HashMap<String, Object>) commentObject);
                }
            }
        }
        post.setNumber_comment(number_comment);
        holder.tv_post_number_comment.setText(number_comment+"");
    }

    public void arrayList_visits(){
        posts = new ArrayList<>(this.posts_all);
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Integer.compare(post2.getVisitor().size(), post1.getVisitor().size());
            }
        });
        ArrayList<Post> post_take_5 = new ArrayList<>();
        if(posts.size() >= 5){
            for(int i = 0 ; i < 5 ; i ++){
                post_take_5.add(posts.get(i));
            }
            posts = new ArrayList<>(post_take_5);
        }
        notifyDataSetChanged();
    }
    public void arrayList_comments(){
        posts = new ArrayList<>(this.posts_all);
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Integer.compare(post2.getNumber_comment(), post1.getNumber_comment());
            }
        });
        ArrayList<Post> post_take_5 = new ArrayList<>();
        if(posts.size() >= 5){
            for(int i = 0 ; i < 5 ; i ++){
                post_take_5.add(posts.get(i));
            }
            posts = new ArrayList<>(post_take_5);
        }
        notifyDataSetChanged();
    }
    public void arraylist_all(){
        posts = new ArrayList<>(this.posts_all);
        notifyDataSetChanged();
    }

    private int countComments(HashMap<String,Object> comments) {
        Object commentObject = comments.get("replies");
        int number_comments = 0;
        if(commentObject != null && commentObject instanceof HashMap){
           number_comments = ((HashMap<String, Object>) commentObject).size();
            for(Map.Entry<String, Object> entry: ((HashMap<String, Object>) commentObject).entrySet()){
                Object object = entry.getValue();
                if(object != null && object instanceof HashMap){
                    number_comments += countComments((HashMap<String, Object>) object);
                }

            }
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
