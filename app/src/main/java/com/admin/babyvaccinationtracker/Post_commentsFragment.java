package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.babyvaccinationtracker.Adapter.DeletePostAdapter;
import com.admin.babyvaccinationtracker.model.Post;

import java.util.ArrayList;

import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Post_commentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Post_commentsFragment extends Fragment {

    public Post_commentsFragment() {
        // Required empty public constructor
    }

    ArrayList<Post> posts;
    Context context;
    RecyclerView DeleteP;
    DeletePostAdapter DetetePAdapter;

    public static Post_commentsFragment newInstance(ArrayList<Post> posts) {
        Post_commentsFragment fragment = new Post_commentsFragment();
        Bundle args = new Bundle();
        args.putSerializable("post",posts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            posts = (ArrayList<Post>) getArguments().getSerializable("post");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_post_comments, container, false);
        context = container != null ? container.getContext() : null;
        DeleteP= view.findViewById(R.id.Rcview);
        DetetePAdapter = new DeletePostAdapter(posts);
        DeleteP.setLayoutManager(new LinearLayoutManager(context));
        DeleteP.setAdapter(DetetePAdapter);
        Log.i("POSSTTT", posts +"");

        return view;
    }
}