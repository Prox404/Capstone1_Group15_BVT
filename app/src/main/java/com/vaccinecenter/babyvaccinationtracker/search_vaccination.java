package com.vaccinecenter.babyvaccinationtracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.Adapter.VaccineAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class search_vaccination extends AppCompatActivity {

    private EditText edt_search_timkiem;
    private TextView vaccineInfoTextView;

    ImageView imge_list_back;
    Button btntimkiem;
    RecyclerView recvaccine;
    VaccineAdapter mVaccineadapter;

    private List<Vaccines> mlistvaccine;

    private void setrcv() {

        edt_search_timkiem = findViewById(R.id.edt_search_timkiem);
        btntimkiem = findViewById(R.id.btntimkiem);

        // Khởi tạo danh sách vaccine
        mlistvaccine = new ArrayList<>();
        recvaccine = findViewById(R.id.rcvvaccine);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recvaccine.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recvaccine.addItemDecoration(dividerItemDecoration);
        mVaccineadapter = new VaccineAdapter(this, mlistvaccine);
        recvaccine.setAdapter(mVaccineadapter);
        vaccineInfoTextView = findViewById(R.id.vaccineInfoTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vaccination);
        setrcv();
        getdatafromrealtimedatabase();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatafromrealtimedatabase();
            }
        });

        imge_list_back = findViewById(R.id.imge_list_back);
        imge_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getdatafromrealtimedatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Context mcontext = search_vaccination.this;
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");
        DatabaseReference myRef = database.getReference("users").child("Vaccine_center").child(id_vaccine_center).child("vaccines");

        String searchTerm = edt_search_timkiem.getText().toString().trim();
        Log.i("Search", "getdatafromrealtimedatabase: " + searchTerm);
        mlistvaccine.clear();
        Log.d("vaccine", "getdatafromrealtimedatabase: call");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Vaccines vaccine = datasnapshot.getValue(Vaccines.class);
                    vaccine.setVaccine_id(datasnapshot.getKey().toString());
                    Log.i("vaccine", "onDataChange: " + vaccine.toString());
                    if (removeDiacritics(vaccine.getVaccine_name().toLowerCase()).contains(removeDiacritics(searchTerm.toLowerCase()))) {
                        mlistvaccine.add(vaccine);
                    }
                }
                mVaccineadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_vaccination.this, "null", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}