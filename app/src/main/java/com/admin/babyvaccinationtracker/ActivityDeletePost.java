package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.admin.babyvaccinationtracker.Adapter.DeletePostAdapter;
import com.admin.babyvaccinationtracker.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ActivityDeletePost extends AppCompatActivity {
    ArrayList<Post> posts = new ArrayList<>();
    Context context;
    RecyclerView DeleteP;
    DeletePostAdapter DetetePAdapter;
    EditText edt_search_post_delete_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);

        DeleteP= findViewById(R.id.Rcview);
        edt_search_post_delete_post = findViewById(R.id.edt_search_post_delete_post);

        DetetePAdapter = new DeletePostAdapter(posts);
        DeleteP.setLayoutManager(new LinearLayoutManager(context));
        DeleteP.setAdapter(DetetePAdapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    posts.clear();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Post post = snapshot1.getValue(Post.class);
                        posts.add(post);
                    }
                    DetetePAdapter.notifyDataSetChanged();
                }else {
                    posts.clear();
                    DetetePAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        edt_search_post_delete_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                search(name);
            }
        });
        Log.i("POSSTTT", posts +"");
    }
    private void search(String name) {
        ArrayList<Post> posts_filter = new ArrayList<>();
        if(!name.isEmpty()){
            for(Post post : posts){
                if(removeDiacritics(post.getContent().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))
                        || removeDiacritics(post.getUser().getUser_name()).contains(removeDiacritics(name.toLowerCase())))
                {
                    posts_filter.add(post);
                }
            }
            DetetePAdapter.filter_post(posts_filter);
        }else {
            DetetePAdapter.filter_post(posts);
        }
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}