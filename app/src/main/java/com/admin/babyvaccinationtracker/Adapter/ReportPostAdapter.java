package com.admin.babyvaccinationtracker.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.admin.babyvaccinationtracker.BlockDeletePost;
import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Post;
import com.admin.babyvaccinationtracker.model.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReportPostAdapter extends RecyclerView.Adapter<ReportPostAdapter.viewholder> {

    ArrayList<Report> reports;
    Context context;
    public ReportPostAdapter(Context context, ArrayList<Report> reports){
        this.reports = reports;
        this.context = context;
    }
    @NonNull
    @Override
    public ReportPostAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_report_post, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportPostAdapter.viewholder holder, int position) {
        Post post = reports.get(position).getPost();
        String post_id = post.getPost_id();
        holder.textViewUsername2.setText(post.getUser().getUser_name());
        holder.textViewTime.setText(post.getCreated_at());
        String url_avatar = post.getUser().getUser_avatar();
        url_avatar = url_avatar.contains("https") ?
                url_avatar : url_avatar.replace("http", "https");
        Picasso.get().load(url_avatar).into(holder.imageViewUserAvatar);
        holder.textViewPostContent.setText(post.getContent());
        if(post.getHashtags()!= null){
            holder.textViewHashtag.setText(String.join(" ", post.getHashtags()));
        }
        if (post.getImage_url() != null) {
            ImageCarouselAdapter postImageAdapter =
                    new ImageCarouselAdapter(holder.viewPagerImage.getContext(), post.getImage_url());
            holder.viewPagerImage.setAdapter(postImageAdapter);
        }
        else {
            holder.viewPagerImage.setVisibility(View.GONE);
        }
        ArrayList<String> reasons = reports.get(position).getReasons();
        Log.i("REASONSSSS",reasons +"");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(holder.ListView_reason_report_post.getContext(),
                        android.R.layout.simple_list_item_1, reasons );
        holder.ListView_reason_report_post.setAdapter(adapter);
        holder.imageVIew_delete_block2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, post,post_id);
            }
        });
    }

    private void showMenu(View view, Post post, String post_id) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.menu_block_delete, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.item_delete_post){
                    DatabaseReference reference_deletePost = FirebaseDatabase
                            .getInstance()
                            .getReference("posts");
                    reference_deletePost.child(post_id)
                            .setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference reference_report = FirebaseDatabase
                                    .getInstance().getReference("Report");
                            Query query = reference_report
                                    .orderByChild("post/post_id").equalTo(post_id);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot s : snapshot.getChildren()){
                                            s.getRef().removeValue();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    return true;
                }
                else if (id == R.id.item_delete_block) {
                    String cus_name = post.getUser().getUser_name();
                    String cus_id = post.getUser().getUser_id();
                    if(post.getUser().getUser_role().equals("customer")){
                        Block_Delete_Post(cus_name,cus_id,1,post_id);
                    }else if(post.getUser().getUser_role().equals("center")){
                        Block_Delete_Post(cus_name,cus_id,0,post_id);
                    }
                    return true;
                }
                return false;
            }
        });
        menu.show();
    }
    void Block_Delete_Post(String cus_name, String cus_id,int isCus, String post_id){
        Bundle args = new Bundle();
        args.putInt("isCus",isCus);
        args.putString("user_name", cus_name);
        args.putString("user_id", cus_id );
        args.putString("post_id", post_id );
        BlockDeletePost blockUser = new BlockDeletePost();
        blockUser.setArguments(args);
        blockUser.show(((AppCompatActivity) context).getSupportFragmentManager(), "Chặn người dùng");
    }

    public void setReports(ArrayList<Report> reports){
        this.reports = reports;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageViewUserAvatar,imageVIew_delete_block2;
        TextView textViewUsername2,textViewTime,textViewPostContent,textViewHashtag;
        ViewPager2 viewPagerImage;
        ListView ListView_reason_report_post;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageViewUserAvatar = itemView.findViewById(R.id.imageViewUserAvatar2);
            textViewUsername2 = itemView.findViewById(R.id.textViewUsername2);
            textViewTime = itemView.findViewById(R.id.textViewTime2);
            textViewPostContent = itemView.findViewById(R.id.textViewPostContent2);
            textViewHashtag = itemView.findViewById(R.id.textViewHashtag2);
            viewPagerImage = itemView.findViewById(R.id.viewPagerImage2);
            ListView_reason_report_post = itemView.findViewById(R.id.ListView_reason_report_post);
            imageVIew_delete_block2 = itemView.findViewById(R.id.imageVIew_delete_block2);
        }
    }
}
