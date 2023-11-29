package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_manage_post#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_manage_post extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View buttonThemBaiDang,buttonXemBaiDang,buttonNoidungbaocao;

    public Fragment_manage_post() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_manage_post.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_manage_post newInstance(String param1, String param2) {
        Fragment_manage_post fragment = new Fragment_manage_post();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_post, container, false);
        Context context = container != null ? container.getContext() : null;
        // todo them bài đăng
        buttonThemBaiDang = view.findViewById(R.id.buttonThemBaiDang);
        buttonThemBaiDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Activity_add_post.class));
            }
        });
        // xem bài đăng
        buttonXemBaiDang = view.findViewById(R.id.buttonXemBaiDang);
        buttonXemBaiDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Activity_display_content_post.class));
            }
        });
        // nội dung báo cáo
        buttonNoidungbaocao = view.findViewById(R.id.buttonNoidungbaocao);
        buttonNoidungbaocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Activity_report_comment_and_post.class));
            }
        });

        return view;
    }
}