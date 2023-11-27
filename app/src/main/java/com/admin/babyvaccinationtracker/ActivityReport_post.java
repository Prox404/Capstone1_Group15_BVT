package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.admin.babyvaccinationtracker.Adapter.ReportPostAdapter;
import com.admin.babyvaccinationtracker.model.Post;
import com.admin.babyvaccinationtracker.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ActivityReport_post extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView rv_report_post;
    ArrayList<Report> reports;
    ReportPostAdapter adapter;
    Map<String , Report> report_port = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_post);
        iv_back = findViewById(R.id.iv_back);
        rv_report_post = findViewById(R.id.rv_report_post);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reports = new ArrayList<>();
        adapter = new ReportPostAdapter(ActivityReport_post.this,reports);
        rv_report_post.setLayoutManager(new LinearLayoutManager(ActivityReport_post.this));
        rv_report_post.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
        Query query = reference.orderByChild("type_report").equalTo(1); // bài đăng : 0 || comment : 1

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_big) {
                if (snapshot_big.exists()) {
                    reports.clear(); // Clear the previous data
                    report_port.clear();
                    for (DataSnapshot snapshot : snapshot_big.getChildren()) {
                        Post post = snapshot.child("post").getValue(Post.class);
                        String reason = snapshot.child("reason").getValue(String.class);
                        if(report_port.containsKey(post.getPost_id())){
                            ArrayList<String> reasons = report_port.get(post.getPost_id()).getReasons();
                            reasons.add(reason);
                            report_port.get(post.getPost_id()).setReasons(reasons);
                        }else {
                            Report report = new Report();
                            report.setPost(post);
                            report.setReport_id(snapshot.getKey());
                            ArrayList<String> reasons = new ArrayList<>();
                            reasons.add(reason);
                            report.setReasons(reasons);
                            report_port.put(post.getPost_id(), report);
                        }
                    }
                    reports = new ArrayList<>(report_port.values());
                    Collections.sort(reports, new Comparator<Report>() {
                        @Override
                        public int compare(Report report, Report t1) {
                            return Integer.compare(t1.getReasons().size(),report.getReasons().size() );
                        }
                    });
                    adapter.setReports(reports);

                } else {
                    reports.clear();
                    report_port.clear();
                    adapter.setReports(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }
}