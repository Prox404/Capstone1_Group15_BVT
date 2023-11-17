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

import com.admin.babyvaccinationtracker.Adapter.PostVisitsAdapter;
import com.admin.babyvaccinationtracker.model.Post;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Post_VisitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Post_VisitsFragment extends Fragment {
    ArrayList<Post> posts;
    Context context;
    RecyclerView recyclerViewVisits;
    PostVisitsAdapter adapter;
    public Post_VisitsFragment() {
        // Required empty public constructor
    }

    public static Post_VisitsFragment newInstance(ArrayList<Post> posts) {
        Post_VisitsFragment fragment = new Post_VisitsFragment();
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
        View view = inflater.inflate(R.layout.fragment_post__visits, container, false);
        context = container != null ? container.getContext() : null;
        recyclerViewVisits = view.findViewById(R.id.recyclerViewVisits);

        adapter = new PostVisitsAdapter(posts);
        Log.i("POSTTTSASSAC", posts+"");
        recyclerViewVisits.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewVisits.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}