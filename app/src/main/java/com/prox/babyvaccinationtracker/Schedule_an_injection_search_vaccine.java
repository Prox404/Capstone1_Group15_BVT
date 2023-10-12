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

    ArrayList<Vaccine_center> vaccine_centers = new ArrayList<>();

    VaccineCenterAdapter adapter;

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
                Vaccine_center selectedVaccine = (Vaccine_center) adapterView.getItemAtPosition(i);

                ArrayList<String> center_vaccine = new ArrayList<>();

                for (Vaccines vaccine : selectedVaccine.getVaccines().values()) {
                    String vaccineName = vaccine.getVaccine_name();
                    center_vaccine.add(vaccineName);
                }


                Intent vaccine_choose = new Intent();
                vaccine_choose.putExtra("center_name", selectedVaccine.getCenter_name());
                vaccine_choose.putStringArrayListExtra("center_vaccines", center_vaccine);
                setResult(RESULT_OK, vaccine_choose);
                finish();

            }
        });

    }
    private void search_vaccines(String searchText){
        ArrayList<Vaccine_center> filteredVaccines = new ArrayList<>();

        for (Vaccine_center vaccine : vaccine_centers) {
            if (vaccine.getCenter_name().toLowerCase().contains(searchText.toLowerCase())) {
                filteredVaccines.add(vaccine);
            }
        }


        adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, filteredVaccines);
        schedule_list_vaccine_search.setAdapter(adapter);
    }

    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = database.getReference("Vaccine_centers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String customerAddress = getIntent().getStringExtra("cus_address");

                ArrayList<Vaccine_center> matchingCenters = new ArrayList<>();

                for (DataSnapshot vaccineSnapshot : snapshot.getChildren()) {
                    Vaccine_center center = vaccineSnapshot.getValue(Vaccine_center.class);
                    String centerAddress = center.getCenter_address();

                    if (isAddressNearCustomer(customerAddress, centerAddress)) {
                        matchingCenters.add(center);
                    }
                }

                adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, matchingCenters);
                schedule_list_vaccine_search.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private boolean isAddressNearCustomer(String customerAddress, String centerAddress) {
        String[] customerAddressParts = customerAddress.split(", ");
        String[] centerAddressParts = centerAddress.split(", ");

        if (customerAddressParts.length < centerAddressParts.length) {
            return false;
        }

        for (int i = 0; i < centerAddressParts.length; i++) {
            if (!customerAddressParts[i].equals(centerAddressParts[i])) {
                return false;
            }
        }

        return true;
    }
}