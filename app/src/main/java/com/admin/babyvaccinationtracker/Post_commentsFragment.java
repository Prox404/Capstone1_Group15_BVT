package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.admin.babyvaccinationtracker.Adapter.DeletePostAdapter;
import com.admin.babyvaccinationtracker.model.Post;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
    EditText edt_search_post_delete_post;

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
        View view = inflater.inflate(R.layout.fragment_post_comments, container, false);
        context = container != null ? container.getContext() : null;
        DeleteP= view.findViewById(R.id.Rcview);
        edt_search_post_delete_post = view.findViewById(R.id.edt_search_post_delete_post);
        DetetePAdapter = new DeletePostAdapter(posts);
        DeleteP.setLayoutManager(new LinearLayoutManager(context));
        DeleteP.setAdapter(DetetePAdapter);

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

        return view;
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