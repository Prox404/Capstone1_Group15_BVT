package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class search_vaccine_center extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextViewTimKiem;
    private TextView centerInfoTextView;
    LinearLayout btntimkiem;
    RecyclerView rcvcenter;
    vaccine_center_adapter mvaccinecenteradapter;

    private List<Vaccine_center> mlistvaccinecenter;
    private void setrcv() {
        autoCompleteTextViewTimKiem = findViewById(R.id.edttimkiem);
        btntimkiem = findViewById(R.id.btntimkiem);

        // Khởi tạo danh sách vaccine
        mlistvaccinecenter = new ArrayList<>();
        rcvcenter = findViewById(R.id.rcvcenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvcenter.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rcvcenter.addItemDecoration(dividerItemDecoration);
        mvaccinecenteradapter = new vaccine_center_adapter(this, mlistvaccinecenter);
        rcvcenter.setAdapter(mvaccinecenteradapter);
        centerInfoTextView = findViewById(R.id.centerInfoTextView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vaccine_center);
        setrcv();
        getdatafromrealtimedatabase();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatafromrealtimedatabase();
            }
        });

    }
    private void getdatafromrealtimedatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child("Vaccine_center");

        String searchTerm = autoCompleteTextViewTimKiem.getText().toString().trim();
        Log.i("Search", "getdatafromrealtimedatabase: " + searchTerm);
        mlistvaccinecenter.clear();
        Log.d("vaccine", "getdatafromrealtimedatabase: call");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Vaccine_center vaccineCenter = datasnapshot.getValue(Vaccine_center.class);
                    vaccineCenter.setCenter_id(datasnapshot.getKey());
                    Log.i("vaccine_center", "onDataChange: " + vaccineCenter.toString());
                    if (removeDiacritics(vaccineCenter.getCenter_name().toLowerCase()).contains(removeDiacritics(searchTerm.toLowerCase()))) {
                        mlistvaccinecenter.add(vaccineCenter);
                    }
                }
                mvaccinecenteradapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_vaccine_center.this, "null", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}