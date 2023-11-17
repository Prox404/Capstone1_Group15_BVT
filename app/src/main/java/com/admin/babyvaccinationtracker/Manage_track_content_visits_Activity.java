package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.admin.babyvaccinationtracker.Adapter.PostViewPageAdapter;
import com.admin.babyvaccinationtracker.model.Comment;
import com.admin.babyvaccinationtracker.model.Post;
import com.admin.babyvaccinationtracker.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manage_track_content_visits_Activity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    PostViewPageAdapter adapter;

    ArrayList<Post> posts = new ArrayList<>();
    ArrayList<String> post_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_track_content_visits);
        tabLayout = findViewById(R.id.ManagerPostTabLayout);
        viewPager = findViewById(R.id.ManagePostviewPager);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String id = dataSnapshot.getKey();
                    if(!post_id.contains(id)){
                        Post post = new Post();
                        post.setUser(dataSnapshot.child("user").getValue(User.class));
                        post.setContent(dataSnapshot.child("content").getValue(String.class));
                        post.setCreated_at(dataSnapshot.child("created_at").getValue(String.class));
                        ArrayList<String> hashtags = (ArrayList<String>) Objects.requireNonNull(dataSnapshot.child("hashtags").getValue());
                        post.setHashtags(hashtags);
                        ArrayList<String> image_url = (ArrayList<String>) Objects.requireNonNull(dataSnapshot.child("image_url").getValue());
                        post.setImage_url(image_url);
                        post.setPost_id(dataSnapshot.getKey());
                        post.setComments((HashMap<String, Comment>) dataSnapshot.child("comments").getValue());
                        post.setLiked_users((ArrayList<String>) dataSnapshot.child("liked_users").getValue());

                        HashMap<String, Boolean> visitors = (HashMap<String, Boolean>) dataSnapshot.child("Visitors").getValue();
                        if(visitors != null){
                            post.setVisitor(visitors);
                        }else {
                            post.setVisitor(new HashMap<>());
                        }
                        Log.i("VISITORS", visitors+"");
                        posts.add(post);
                    }
                }
                adapter = new PostViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, posts);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}