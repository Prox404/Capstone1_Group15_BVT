package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView imageView_Account;

    TextView textViewNumOfUser,textViewNumOfRegisterInjection;

    LinearLayout send_notification,user_management,report, viewHealthContainer, managePostContainer;

    FirebaseDatabase firebaseDatabase;


    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = container != null ? container.getContext() : null;
        // gửi thông báo
        send_notification = view.findViewById(R.id.send_notification);
        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SendNotificationActivity.class));
            }
        });
        // quản lý người dùng
        user_management = view.findViewById(R.id.user_management);
        user_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Admin_management_of_user.class));
//                loadFragment(new Fragment_manage_user_upgrade());
            }
        });
        // báo cáo
        report = view.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Manage_track_content_visits_Activity.class));
            }
        });
        // số lượng người đăng ký tiêm
        textViewNumOfRegisterInjection = view.findViewById(R.id.textViewNumOfRegisterInjection);
        DatabaseReference firebaseDatabase_number_RegisterInjection = firebaseDatabase.getReference("Vaccination_Registration");
        firebaseDatabase_number_RegisterInjection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewNumOfRegisterInjection.setText(snapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // số lượng người dùng
        textViewNumOfUser = view.findViewById(R.id.textViewNumOfUser);
        DatabaseReference firebaseDatabase_number_User =  firebaseDatabase.getReference("users").child("customers");
        firebaseDatabase_number_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewNumOfUser.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // load ảnh
        imageView_Account = view.findViewById(R.id.imageView_Account);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String image_admin = sharedPreferences.getString("admin_avatar","");
        if(!image_admin.isEmpty()){
            image_admin = image_admin.contains("https") ? image_admin : image_admin.replace("http", "https");
            Picasso.get().load(image_admin).into(imageView_Account);
        }
        imageView_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, UserActivity.class));
            }
        });

        viewHealthContainer = view.findViewById(R.id.viewHealthContainer);
        viewHealthContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BabyManagement.class));
            }
        });

        managePostContainer = view.findViewById(R.id.managePostContainer);
        managePostContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Fragment_manage_post());
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }


}