package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.admin.babyvaccinationtracker.Adapter.ReportCommentAdapter;
import com.admin.babyvaccinationtracker.model.Comment;
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

public class ActivityReport_comment extends AppCompatActivity {

    ImageView imageView_icBack_commentR;
    RecyclerView rv_report_comment;

    HashMap<String, Report> reportHashMap = new HashMap<>();
    ArrayList<Report> reports = new ArrayList<>();
    ReportCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comment);
        imageView_icBack_commentR = findViewById(R.id.imageView_icBack_commentR);
        rv_report_comment = findViewById(R.id.rv_report_comment);
        imageView_icBack_commentR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new ReportCommentAdapter(reports, ActivityReport_comment.this);
        rv_report_comment.setLayoutManager(new LinearLayoutManager(ActivityReport_comment.this));
        rv_report_comment.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
        Query query = reference.orderByChild("type_report").equalTo(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reportHashMap.clear();
                    for (DataSnapshot s : snapshot.getChildren()){
                        Report report = new Report();
                        Comment comment = s.child("comment").getValue(Comment.class);
                        String id_comment = comment.getComment_id();
                        if(reportHashMap.containsKey(id_comment)){
                            ArrayList<String> reasons = reportHashMap.get(id_comment).getReasons();
                            reasons.add(s.child("reason").getValue(String.class));
                            reportHashMap.get(id_comment).setReasons(reasons);
                        }
                        else {
                            ArrayList<String> reasons = new ArrayList<>();
                            reasons.add(s.child("reason").getValue(String.class));
                            report.setComment(comment);
                            report.setReport_id(s.getKey());
                            report.setReasons(reasons);
                            report.setPost_id(s.child("post_id").getValue(String.class));
                            reportHashMap.put(id_comment,report);
                        }
                    }
                    reports = new ArrayList<>(reportHashMap.values());
                    Collections.sort(reports, new Comparator<Report>() {
                        @Override
                        public int compare(Report report, Report t1) {
                            return Integer.compare(t1.getReasons().size(), report.getReasons().size()) ;
                        }
                    });
                    adapter.setReport(reports);
                }else {
                    reportHashMap.clear();
                    reports.clear();
                    adapter.setReport(reports);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}