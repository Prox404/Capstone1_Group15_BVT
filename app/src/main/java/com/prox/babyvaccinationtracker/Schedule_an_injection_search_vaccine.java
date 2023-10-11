package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Schedule_an_injection_search_vaccine extends AppCompatActivity {
    ImageView schedule_back_vaccine;
    ListView schedule_list_vaccine_search;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText schedule_edt_vaccine_search;

    ArrayList<String> vaccines = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine);

        schedule_back_vaccine = findViewById(R.id.schedule_back_vaccine);
        schedule_list_vaccine_search = findViewById(R.id.schedule_list_vaccine_search);
        schedule_edt_vaccine_search = findViewById(R.id.schedule_edt_vaccine_search);
        schedule_back_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GetDataOnFireBase_vaccine();

        schedule_edt_vaccine_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().toLowerCase();
                search_vaccines(searchText);

            }
        });


    }
    private void search_vaccines(String searchText){
        ArrayList<String> filteredVaccines = new ArrayList<>();

        for (String vaccine : vaccines) {
            if (vaccine.toLowerCase().contains(searchText)) {
                filteredVaccines.add(vaccine);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Schedule_an_injection_search_vaccine.this, android.R.layout.simple_list_item_1, filteredVaccines);
        schedule_list_vaccine_search.setAdapter(adapter);
    }

    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = database.getReference("Vaccines");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot vaccineSnapshot : snapshot.getChildren()){
                    vaccines.add(vaccineSnapshot.child("vaccine_name").getValue(String.class));
                }
                ArrayAdapter adapter = new ArrayAdapter(Schedule_an_injection_search_vaccine.this, android.R.layout.simple_list_item_1, vaccines);
                schedule_list_vaccine_search.setAdapter(adapter);

                schedule_list_vaccine_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent vaccine_choose= new Intent();
                        vaccine_choose.putExtra("vaccine_name",vaccines.get(i));
                        setResult(RESULT_OK, vaccine_choose);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}