package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.VaccineAdapter;
import com.prox.babyvaccinationtracker.model.Regimen;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class schedule_an_injection_search_vaccine_2_2 extends AppCompatActivity {
    ImageView schedule_image_back_vaccine2;
    EditText schedule_edt_search_vaccine2;
    ListView schedule_list_vaccine;
    String baby_id;
    Button buttonClearFilter;
    VaccineAdapter adapter;
    TextView textViewMessage;

    TextView text_View_take_care_header_scheulde;

    Spinner spinnerOrigin;
    List<String> vaccineOrigin = new ArrayList<>();
    List<Vaccines> vaccines_care = new ArrayList<>();

    String customer_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine2);
        schedule_image_back_vaccine2 = findViewById(R.id.schedule_image_back_vaccine2);
        schedule_edt_search_vaccine2 = findViewById(R.id.schedule_edt_search_vaccine2);
        schedule_list_vaccine = findViewById(R.id.schedule_list_vaccine);
        buttonClearFilter = findViewById(R.id.buttonClearFilter);
        spinnerOrigin = findViewById(R.id.spinnerOrigin);
        textViewMessage = findViewById(R.id.textViewMessage);

        text_View_take_care_header_scheulde = findViewById(R.id.text_View_take_care_header_scheulde);
        text_View_take_care_header_scheulde.setText("Các vắc-xin hiện bạn đang quan tâm");

        buttonClearFilter.setVisibility(View.GONE);

        SharedPreferences.Editor a = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");

        schedule_image_back_vaccine2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        baby_id = intent.getStringExtra("baby_id");


        if (baby_id.isEmpty()) {
            Toast.makeText(this, "Hãy chọn trẻ để chúng tôi có thể gợi ý những loại vaccine phù hợp với bé !!", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this,vaccines_care );
            schedule_list_vaccine.setAdapter(adapter);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s : snapshot.getChildren()){
                        Vaccine_center vaccine_center = s.getValue(Vaccine_center.class);
                        vaccine_center.setCenter_id(s.getKey());

                        HashMap<String, Vaccines> vaccinesAll = vaccine_center.getVaccines();
                        if(vaccinesAll != null){
                            for(Map.Entry<String,Vaccines> vaccine_item : vaccinesAll.entrySet()){
                                if(vaccine_item.getValue().getUser_cares().containsKey(customer_id)){
                                    Vaccines vaccines1 = vaccine_item.getValue();
                                    vaccines1.setVaccine_id(vaccine_item.getKey());
                                    vaccines1.setVaccine_center_owner(vaccine_center);
                                    vaccines_care.add(vaccines1);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        vaccine_origin_fuc(vaccines_care);
                        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<>(schedule_an_injection_search_vaccine_2_2.this, android.R.layout.simple_spinner_item, vaccineOrigin);
                        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerOrigin.setAdapter(adapterOrigin);

                        spinnerOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // Lấy xuất xứ được chọn từ Spinner
                                String selectedOrigin = (String) adapterView.getItemAtPosition(i);
                                // Lọc danh sách vaccine dựa trên xuất xứ
                                filterVaccineByOrigin(selectedOrigin);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        schedule_edt_search_vaccine2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search_vaccines(editable.toString());
            }
        });

        schedule_list_vaccine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccines vaccines1 = (Vaccines) adapterView.getItemAtPosition(i);
                Vaccine_center vaccineCenter = vaccines1.getVaccine_center_owner();

                vaccineCenter.setVaccines(null);
                vaccines1.setVaccine_center_owner(null);
                vaccines1.setUser_cares(null);


                Intent intent1 = new Intent();
                intent1.putExtra("vaccine_choose", vaccines1);
                intent1.putExtra("selected_vaccine" ,vaccineCenter);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }
    void vaccine_origin_fuc(List<Vaccines> vaccines_care){
        vaccineOrigin.clear();
        vaccineOrigin.add("Tất cả");
        for(Vaccines a : vaccines_care){
            vaccineOrigin.add(a.getOrigin());
        }
    }

    private void filterVaccineByOrigin(String selectedOrigin) {
        if (!selectedOrigin.isEmpty() && !selectedOrigin.equals("Tất cả")) {
            List<Vaccines> filteredList_ = new ArrayList<>();
            for (Vaccines vaccine : vaccines_care) {
                if (vaccine.getOrigin().equals(selectedOrigin)) {
                    filteredList_.add(vaccine);
                }
            }

            // Cập nhật Adapter và hiển thị danh sách lọc
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, filteredList_);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            // Nếu không có xuất xứ nào được chọn, hiển thị toàn bộ danh sách
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, vaccines_care);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    void search_vaccines(String searchText) {
        ArrayList<Vaccines> filterVaccine = new ArrayList<>();
        if (!searchText.isEmpty()) {
            for (Vaccines a : vaccines_care) {
                if (removeDiacritics(a.getVaccine_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))) {
                    filterVaccine.add(a);
                }
            }
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            filterVaccine = new ArrayList<>(vaccines_care);
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }
}
