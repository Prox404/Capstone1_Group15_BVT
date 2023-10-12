package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;

public class Schedule_an_injection_search_vaccine extends AppCompatActivity {
    ImageView schedule_back_vaccine;
    ListView schedule_list_vaccine_search;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText schedule_edt_vaccine_search,schedule_edt_xa,schedule_edt_huyen,schedule_edt_tinh;


    ArrayList<String> vaccine_centers = new ArrayList<>();
    ArrayList<String> vaccine_address = new ArrayList<>();
    ArrayList<String> vaccine_centers_key = new ArrayList<>();
    ArrayList<Vaccines> vaccine_name = new ArrayList<>();

    ArrayAdapter<String> adapter;
//    ArrayList<Vaccine_center> vaccine_centers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine);

        schedule_back_vaccine = findViewById(R.id.schedule_back_vaccine);
        schedule_list_vaccine_search = findViewById(R.id.schedule_list_vaccine_search);
        schedule_edt_vaccine_search = findViewById(R.id.schedule_edt_vaccine_search);
        schedule_edt_xa = findViewById(R.id.schedule_edt_xa);
        schedule_edt_huyen = findViewById(R.id.schedule_edt_huyen);
        schedule_edt_tinh = findViewById(R.id.schedule_edt_tinh);
        schedule_back_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = getIntent();
//                String a = i.getStringExtra("cus_address");
//                Log.i("cus_address",a);
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
        schedule_list_vaccine_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedVaccine = (String) adapterView.getItemAtPosition(i);
                Intent vaccine_choose= new Intent();

                DatabaseReference reference = database.getReference("Vaccine_centers").child(vaccine_centers_key.get(i));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> name = new ArrayList<>();
                        int i = 0;
                        for(DataSnapshot vaccineSnapshot : snapshot.child("vaccines").getChildren()){
                            vaccine_name.add(vaccineSnapshot.getValue(Vaccines.class));
                            name.add(vaccine_name.get(i).getVaccine_name());
                            i++;
                        }
                        vaccine_choose.putStringArrayListExtra("vaccine_name",name);
                        vaccine_choose.putExtra("vaccine_center", selectedVaccine);
                        setResult(RESULT_OK, vaccine_choose);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    private void search_vaccines(String searchText){
        adapter.getFilter().filter(searchText);

    }

    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = database.getReference("Vaccine_centers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot vaccineSnapshot : snapshot.getChildren()){
                    vaccine_centers.add(vaccineSnapshot.child("center_name").getValue(String.class));
                    vaccine_address.add(vaccineSnapshot.child( "center_address").getValue(String.class));
                    vaccine_centers_key.add(vaccineSnapshot.getKey());

                }
                adapter = new ArrayAdapter(Schedule_an_injection_search_vaccine.this, android.R.layout.simple_list_item_1, vaccine_centers);
                schedule_list_vaccine_search.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}