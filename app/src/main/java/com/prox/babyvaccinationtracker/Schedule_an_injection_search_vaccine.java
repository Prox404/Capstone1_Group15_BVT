package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.VaccineCenterAdapter;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Schedule_an_injection_search_vaccine extends AppCompatActivity {
    ImageView schedule_back_vaccine;
    ListView schedule_list_vaccine_search;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText schedule_edt_vaccine_search;
    TextView schedule_tv_cus_address,schedule_tv_display_all_vaccine;

    VaccineCenterAdapter adapter;


    ArrayList<Vaccine_center> vaccine_centers = new ArrayList<>(); // tất cả trung tâm tiêm chủng
    ArrayList<Vaccine_center> filterAddressCenter = new ArrayList<>(); // trung tâm lọc theo địa chỉ khách hàng
    ArrayList<Vaccine_center> matchingCenters = new ArrayList<>(vaccine_centers); // tìm trung tâm vắc-xin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine);

        schedule_back_vaccine = findViewById(R.id.schedule_back_vaccine);
        schedule_list_vaccine_search = findViewById(R.id.schedule_list_vaccine_search);
        schedule_edt_vaccine_search = findViewById(R.id.schedule_edt_vaccine_search);
        schedule_tv_cus_address = findViewById(R.id.schedule_tv_cus_address);
        schedule_tv_display_all_vaccine = findViewById(R.id.schedule_tv_display_all_vaccine);

        // quay lại
        schedule_back_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // hiển thị những vaccine center gần khách hàng
        GetDataOnFireBase_vaccine();

        // hiển thị tất cả vaccine center
        schedule_tv_display_all_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, vaccine_centers);
                schedule_list_vaccine_search.setAdapter(adapter);
            }

        });


        // tìm kiếm
        schedule_edt_vaccine_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString();
                search_vaccines(searchText);

            }
        });
        schedule_list_vaccine_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccine_center selectedVaccine = (Vaccine_center) adapterView.getItemAtPosition(i);
                Intent vaccine_choose = new Intent();
                // Gửi selectedVaccine
                if(selectedVaccine != null){
                    vaccine_choose.putExtra("selected_vaccine", selectedVaccine);
                    setResult(RESULT_OK, vaccine_choose);
                    finish();
                }
            }
        });

    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    private void search_vaccines(String searchText){
        if(!searchText.isEmpty()){
            matchingCenters.clear();
            for(Vaccine_center center : vaccine_centers){
                if(removeDiacritics(center.getCenter_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))){
                    matchingCenters.add(center);
                }
            }
            adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, matchingCenters);
            schedule_list_vaccine_search.setAdapter(adapter);
        }
        else{
            matchingCenters = new ArrayList<>(filterAddressCenter);
            adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, matchingCenters);
            schedule_list_vaccine_search.setAdapter(adapter);
        }
    }

    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = database.getReference("users").child("Vaccine_center");;
        String address = getIntent().getStringExtra("cus_address");
        schedule_tv_cus_address.setText(address);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String customerAddress = address;
                vaccine_centers = new ArrayList<>();
                for (DataSnapshot vaccineSnapshot : snapshot.getChildren()) {
                    Vaccine_center center = vaccineSnapshot.getValue(Vaccine_center.class);
                    center.setCenter_id(vaccineSnapshot.getKey());
                    String centerAddress = center.getCenter_address();
                    vaccine_centers.add(center);
                    if (isAddressNearCustomer(customerAddress, centerAddress)) {
                        filterAddressCenter.add(center);
                    }
                }

                adapter = new VaccineCenterAdapter(Schedule_an_injection_search_vaccine.this, filterAddressCenter);
                schedule_list_vaccine_search.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private boolean isAddressNearCustomer(String customerAddress, String centerAddress) {
        String[] customerAddressParts = customerAddress.split(", "); // 0 1
        String[] centerAddressParts = centerAddress.split(", "); // 0 1 2

        int customerSizeAddress = customerAddressParts.length;

        if(customerSizeAddress == 1){
            if(!customerAddressParts[0].equals(centerAddressParts[2])){
                return false;
            }
        }
        else if(customerSizeAddress == 2){
            if(customerAddressParts[1].equals(centerAddressParts[2]))
                if(!customerAddressParts[0].equals(centerAddressParts[1])){
                    return false;
                }
                else
                    return true;
            else
                return false;
        } else if (customerSizeAddress == 3){
            for (int i = 0; i < customerSizeAddress; i++) {
                if (!customerAddressParts[i].equals(centerAddressParts[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}