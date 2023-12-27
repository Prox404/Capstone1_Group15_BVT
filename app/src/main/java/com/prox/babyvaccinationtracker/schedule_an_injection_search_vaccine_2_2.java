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
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
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
    List<Vaccines> vaccines_filter = new ArrayList<>();
    List<Vaccines> vaccines = new ArrayList<>();

    List<Regimen> regimenList = new ArrayList<>();
    Date closestDate = null;
    String closestVaccineType = "";

    String customer_id = "";

    private ArrayList<String> vaccination_registrations = new ArrayList<>();

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
        text_View_take_care_header_scheulde.setText("Các vắc-xin đang trong giỏ hàng");

        vaccination_registrations = Schedule_an_injection.vaccination_registrations;
        Log.i("Registration", "onCreate: " + vaccination_registrations.size());


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

        DatabaseReference reference_Favorite = FirebaseDatabase.getInstance().getReference("Favorite").child(customer_id);
        reference_Favorite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Vaccines vaccines = snapshot1.child("vaccines").getValue(Vaccines.class);
                    vaccines_care.add(vaccines);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (baby_id.isEmpty()) {
            Toast.makeText(this, "Hãy chọn trẻ để chúng tôi có thể gợi ý những loại vaccine phù hợp với bé !!", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this,vaccines_care );
            schedule_list_vaccine.setAdapter(adapter);

            Log.i("select vaccine", "onCreate: baby_id: " + baby_id);
            DatabaseReference vaccinationRegimenReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen").child(baby_id);
            vaccinationRegimenReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    regimenList.clear();
                    Date currentDate = new Date();
                    closestDate = null;
                    for (DataSnapshot regimenSnapshot : snapshot.getChildren()) {
                        Regimen regimen = regimenSnapshot.getValue(Regimen.class);
                        Date regimenDate = regimen.getDate();
                        if (regimenDate.before(currentDate) || regimenDate.equals(currentDate)) {
                            if (closestDate == null || closestDate.before(regimenDate)) {
                                if (!regimen.isVaccinated() && !isOnVaccineList(regimen.getVaccination_type())) {
                                    closestDate = regimenDate;
                                    closestVaccineType = regimen.getVaccination_type();
                                }
                            }
                        }
                        if (regimenDate.after(currentDate) && closestDate == null){
                            closestDate = regimenDate;
                            closestVaccineType = regimen.getVaccination_type();
                        }
                        regimenList.add(regimen);
                    }
                    Log.i("select vaccine", "onDataChange: " + closestVaccineType);
                    if (!closestVaccineType.isEmpty()) {
                        textViewMessage = findViewById(R.id.textViewMessage);
                        textViewMessage.setVisibility(View.VISIBLE);
                        String vaccineName = "";
                        switch (closestVaccineType) {
                            case "tuberculosis":
                                vaccineName = "Lao";
                                break;
                            case "hepatitis_b":
                                vaccineName = "Viêm gan B";
                                break;
                            case "diphtheria_whooping_cough_poliomyelitis":
                                vaccineName = "Bạch hầu, ho gà, uốn ván";
                                break;
                            case "paralysis":
                                vaccineName = "Bại liệt";
                                break;
                            case "pneumonia_hib_meningitis":
                                vaccineName = "Viêm phổi, viêm màng não mủ do Hib";
                                break;
                            case "pneumonia_meningitis_otitis_media_caused_by_streptococcus":
                                vaccineName = "Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn";
                                break;
                            case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c":
                                vaccineName = "Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C";
                                break;
                            case "influenza":
                                vaccineName = "Cúm";
                                break;
                            case "measles":
                                vaccineName = "Sởi";
                                break;
                            case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y":
                                vaccineName = "Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y";
                                break;
                            case "japanese_encephalitis":
                                vaccineName = "Viêm não Nhật Bản";
                                break;
                            case "measles_mumps_rubella":
                                vaccineName = "Sởi, Quai bị, Rubella";
                                break;
                            case "chickenpox":
                                vaccineName = "Thủy đậu";
                                break;
                            case "hepatitis_a":
                                vaccineName = "Viêm gan A";
                                break;
                            case "anthrax":
                                vaccineName = "Bệnh tả ";
                                break;
                            case "hepatitis_a_b":
                                vaccineName = "Viêm gan A + B";
                                break;
                            case "tetanus":
                                vaccineName = "Thương hàn";
                                break;
                        }
                        textViewMessage.setText("Gợi ý những loại vaccine phù hợp với bé: " + vaccineName);
                        filterVaccines();
                        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<String>(schedule_an_injection_search_vaccine_2_2.this, android.R.layout.simple_spinner_item, vaccineOrigin);
                        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerOrigin.setAdapter(adapterOrigin);
                    }
                    Log.i("Home", "onDataChange: " + regimenList.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            spinnerOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Lấy xuất xứ được chọn từ Spinner
                    String selectedOrigin = (String) adapterView.getItemAtPosition(i);
                    Log.i("selectedOrigin", ""+selectedOrigin);
                    // Lọc danh sách vaccine dựa trên xuất xứ
                    filterVaccineByOrigin(selectedOrigin);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

        buttonClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, vaccines_care);
                schedule_list_vaccine.setAdapter(adapter);
                textViewMessage.setVisibility(View.GONE);
                vaccines = new ArrayList<>(vaccines_care);
                schedule_edt_search_vaccine2.setText("");
            }
        });

        schedule_list_vaccine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccines vaccines1 = (Vaccines) adapterView.getItemAtPosition(i);
                Vaccine_center vaccineCenter = vaccines1.getVaccine_center_owner();

                vaccineCenter.setVaccines(null);
                vaccines1.setVaccine_center_owner(null);

                Intent intent1 = new Intent();
                intent1.putExtra("vaccine_choose", vaccines1);
                intent1.putExtra("selected_vaccine" ,vaccineCenter);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }

    public Boolean isOnVaccineList(String vaccinationType){
        Log.i("Registration", "isOnVaccineList: " + vaccination_registrations);
        return vaccination_registrations.contains(vaccinationType);
    }

    void filterVaccines(){
        vaccines_filter = new ArrayList<>();
        vaccineOrigin.add("Tất cả");
        for (Vaccines a  : vaccines_care) {
            if (!vaccineOrigin.contains(a.getOrigin())) {
                vaccineOrigin.add(a.getOrigin());
            }
            if (a.getVac_effectiveness().equals(closestVaccineType)) {
                vaccines_filter.add(a);
            }
        }
        vaccines = new ArrayList<>(vaccines_filter);
        adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, vaccines_filter);
        schedule_list_vaccine.setAdapter(adapter);
    }


    private void filterVaccineByOrigin(String selectedOrigin) {
        if (!selectedOrigin.isEmpty() && !selectedOrigin.equals("Tất cả")) {
            List<Vaccines> filteredList_ = new ArrayList<>();
            for (Vaccines vaccine : vaccines) {
                if (vaccine.getOrigin().equals(selectedOrigin)) {
                    filteredList_.add(vaccine);
                }
            }

            // Cập nhật Adapter và hiển thị danh sách lọc
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, filteredList_);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            // Nếu không có xuất xứ nào được chọn, hiển thị toàn bộ danh sách
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, vaccines);
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
            for (Vaccines a : vaccines) {
                if (removeDiacritics(a.getVaccine_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))) {
                    filterVaccine.add(a);
                }
            }
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2_2.this, vaccines);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }
}
