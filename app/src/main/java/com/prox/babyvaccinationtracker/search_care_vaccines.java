package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class search_care_vaccines  extends AppCompatActivity {
    private EditText autoCompleteTextViewTimKiem;
    private TextView vaccineInfoTextView,textViewVaccine_care_header;
    Button btntimkiem;
    vaccineadapter mVaccineadapter;
    String searchTerm = "";
    private List<Vaccines> mlistvaccine = new ArrayList<>();
    GridView gridViewSearchVaccine;

    View loadingLayout;

    String customer_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vaccination);

        autoCompleteTextViewTimKiem = findViewById(R.id.edttimkiem);
        btntimkiem = findViewById(R.id.btntimkiem);
        gridViewSearchVaccine = findViewById(R.id.gridViewSearchVaccine);
        vaccineInfoTextView = findViewById(R.id.vaccineInfoTextView);
        textViewVaccine_care_header = findViewById(R.id.textViewVaccine_care_header);
        loadingLayout = findViewById(R.id.loadingLayout);

        textViewVaccine_care_header.setText("Giỏ hàng");


        mVaccineadapter = new vaccineadapter(this, mlistvaccine);
        gridViewSearchVaccine.setAdapter(mVaccineadapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        getdatafromrealtimedatabase();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatafromrealtimedatabase();
            }
        });


    }

    private void getdatafromrealtimedatabase() {
        loadingLayout.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Favorite").child(customer_id);
        Log.i("customerId", customer_id);

        searchTerm = autoCompleteTextViewTimKiem.getText().toString().trim().isEmpty() ? "" : autoCompleteTextViewTimKiem.getText().toString().trim();
        mlistvaccine.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Vaccines vaccine = dataSnapshot.child("vaccines").getValue(Vaccines.class);
                        if (removeDiacritics(vaccine.getVaccine_name().toLowerCase(Locale.getDefault())).contains(removeDiacritics(searchTerm.toLowerCase(Locale.getDefault()))) && !vaccine.isDeleted()) {
                            mlistvaccine.add(vaccine);
                        }
                    }
                    mVaccineadapter.notifyDataSetChanged();
                }
                else {
                    mVaccineadapter.notifyDataSetChanged();
                }

                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(search_care_vaccines.this, "Đã xảy ra lỗi !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
