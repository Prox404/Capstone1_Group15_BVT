package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.admin.babyvaccinationtracker.Adapter.postAdapter;
import com.admin.babyvaccinationtracker.model.Post;
import com.admin.babyvaccinationtracker.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_display_content_post extends AppCompatActivity {

    RecyclerView recyler_view_post;
    postAdapter adapter;
    ArrayList<Post> posts = new ArrayList<>();
    ArrayList<Post> posts_report = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content_post);
        recyler_view_post = findViewById(R.id.recyler_view_post);

        adapter = new postAdapter(posts);
        recyler_view_post.setLayoutManager(new LinearLayoutManager(Activity_display_content_post.this));
        recyler_view_post.setAdapter(adapter);

        reference = firebaseDatabase.getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for(DataSnapshot post : snapshot.getChildren()){
                    Post a = new Post();
                    a.setUser(post.child("user").getValue(User.class));
                    a.setCreated_at(post.child("created_at").getValue(String.class));
                    ArrayList<String> hashtags = (ArrayList<String>) post.child("hashtags").getValue();
                    if(hashtags != null){
                        a.setHashtags(hashtags);
                    }
                    a.setContent(post.child("content").getValue(String.class));
                    ArrayList<String> image_url =(ArrayList<String>) post.child("image_url").getValue();
                    if(image_url != null){
                        a.setImage_url(image_url);
                    }
                    posts.add(a);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}