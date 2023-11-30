package com.prox.babyvaccinationtracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class search_vaccination extends AppCompatActivity {

    private EditText autoCompleteTextViewTimKiem;
    private TextView vaccineInfoTextView;
    Button btntimkiem;
    vaccineadapter mVaccineadapter;
    String searchTerm = "";
    private List<Vaccines> mlistvaccine = new ArrayList<>();
    GridView gridViewSearchVaccine;

    String customer_id = "";

    @Override
    protected void onRestart() {
        super.onRestart();
        getdatafromrealtimedatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vaccination);

        autoCompleteTextViewTimKiem = findViewById(R.id.edttimkiem);
        btntimkiem = findViewById(R.id.btntimkiem);
        gridViewSearchVaccine = findViewById(R.id.gridViewSearchVaccine);
        vaccineInfoTextView = findViewById(R.id.vaccineInfoTextView);

        mVaccineadapter = new vaccineadapter(this, mlistvaccine);
        gridViewSearchVaccine.setAdapter(mVaccineadapter);
        getdatafromrealtimedatabase();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatafromrealtimedatabase();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
    }

    private void getdatafromrealtimedatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child("Vaccine_center");

        searchTerm = autoCompleteTextViewTimKiem.getText().toString().trim().isEmpty() ? "" : autoCompleteTextViewTimKiem.getText().toString().trim();
        Log.i("Search", "getdatafromrealtimedatabase: " + searchTerm);
        mlistvaccine.clear();
        Log.d("vaccine", "getdatafromrealtimedatabase: call");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Vaccine_center vaccine_center = datasnapshot.getValue(Vaccine_center.class);
                    Log.i("vaccine center", "onDataChange: " + vaccine_center.toString());
                    assert vaccine_center != null;
                    HashMap<String, Vaccines> vaccines = vaccine_center.getVaccines() == null ? new HashMap<String, Vaccines>() : vaccine_center.getVaccines();
                    Log.i("vaccines", "onDataChange: " + vaccines.toString());
                    for (String key : vaccines.keySet()) {
                        Vaccines vaccine = vaccines.get(key);
                        vaccine.setVaccine_id(key);
                        HashMap<String, String> additional_info = new HashMap<>();
                        additional_info.put("center_id", datasnapshot.getKey());
                        additional_info.put("center_name", vaccine_center.getCenter_name());
                        additional_info.put("center_address", vaccine_center.getCenter_address());

                        if(vaccine.getUser_cares().containsKey(customer_id)){
                            additional_info.put("is_user_care", "true" );
                        }
                        else {
                            additional_info.put("is_user_care", "false" );
                        }


                        vaccine.setAdditionInformation(additional_info);
                        if (removeDiacritics(vaccine.getVaccine_name().toLowerCase(Locale.getDefault())).contains(removeDiacritics(searchTerm.toLowerCase(Locale.getDefault()))) && !vaccine.isDeleted()) {
                            mlistvaccine.add(vaccine);
                        }
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
