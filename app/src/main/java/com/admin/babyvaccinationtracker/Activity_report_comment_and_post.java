package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity_report_comment_and_post extends AppCompatActivity {

    ImageView Iv_back;
    TextView number_report_post,number_report_comment;
    LinearLayout LLreport_post, LLreport_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comment_and_post);
        number_report_post = findViewById(R.id.number_report_post);
        number_report_comment = findViewById(R.id.number_report_comment);
        LLreport_post = findViewById(R.id.LLreport_post);
        LLreport_comment = findViewById(R.id.LLreport_comment);
        Iv_back = findViewById(R.id.Iv_back);

        Iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LLreport_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Activity_report_comment_and_post.this,
                        ActivityReport_post.class));
            }
        });
        LLreport_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Activity_report_comment_and_post.this,
                        ActivityReport_comment.class));
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
        Query query_comment = reference.orderByChild("type_report").equalTo(0);
        query_comment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    number_report_comment.setText(snapshot.getChildrenCount()+"");
                }
                else {
                    number_report_comment.setText(0+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query_post = reference.orderByChild("type_report").equalTo(1);
        query_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    number_report_post.setText(snapshot.getChildrenCount()+"");
                }
                else {
                    number_report_post.setText(0+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}