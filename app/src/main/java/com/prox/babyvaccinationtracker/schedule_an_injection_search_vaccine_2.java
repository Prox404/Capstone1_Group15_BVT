package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.TimeLineAdapter;
import com.prox.babyvaccinationtracker.adapter.VaccineAdapter;
import com.prox.babyvaccinationtracker.model.Regimen;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class schedule_an_injection_search_vaccine_2 extends AppCompatActivity {
    ImageView schedule_image_back_vaccine2;
    EditText schedule_edt_search_vaccine2;
    ListView schedule_list_vaccine;
    ArrayList<Vaccines> vaccines;
    ArrayList<Vaccines> filterVaccine = new ArrayList<>();
    List<Regimen> regimenList = new ArrayList<>();
    String baby_id;
    Date closestDate = null;
    String closestVaccineType = "";
    Button buttonClearFilter;
    VaccineAdapter adapter;
    TextView textViewMessage;

    Spinner spinnerOrigin;

    List<String> vaccineOrigin = new ArrayList<>();
    HashMap<String, Vaccines> vaccinesHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine2);
        schedule_image_back_vaccine2 = findViewById(R.id.schedule_image_back_vaccine2);
        schedule_edt_search_vaccine2 = findViewById(R.id.schedule_edt_search_vaccine2);
        schedule_list_vaccine = findViewById(R.id.schedule_list_vaccine);
        buttonClearFilter = findViewById(R.id.buttonClearFilter);
        spinnerOrigin = findViewById(R.id.spinnerOrigin);
        schedule_image_back_vaccine2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        vaccinesHashMap = (HashMap<String, Vaccines>) intent.getSerializableExtra("Vaccines"); // hashmap
        baby_id = intent.getStringExtra("baby_id");
        if (baby_id.isEmpty()) {
            Toast.makeText(this, "Hãy chọn trẻ để chúng tôi có thể gợi ý những loại vaccine phù hợp với bé !!", Toast.LENGTH_SHORT).show();
        } else {
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
                                if (!regimen.isVaccinated()) {
                                    closestDate = regimenDate;
                                    closestVaccineType = regimen.getVaccination_type();
                                }
                            }
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
                        ArrayAdapter<String> adapterOrigin = new ArrayAdapter<String>(schedule_an_injection_search_vaccine_2.this, android.R.layout.simple_spinner_item, vaccineOrigin);
                        adapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerOrigin.setAdapter(adapterOrigin);
                    }
                    Log.i("Home", "onDataChange: " + regimenList.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Log.i("select vaccine", "onCreate: " + vaccineOrigin.toString());


        spinnerOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Log.i("select vaccine", "onItemSelected");
                String selectedOrigin = (String) parentView.getItemAtPosition(position);

                filterVaccineByOrigin(selectedOrigin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i("select vaccine", "onNothingSelected");
            }
        });

        schedule_list_vaccine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccines vaccines1 = (Vaccines) adapterView.getItemAtPosition(i);
                Intent intent1 = new Intent();
                intent1.putExtra("vaccine_choose", vaccines1);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });

        schedule_edt_search_vaccine2.addTextChangedListener(new TextWatcher() {
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

        buttonClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, vaccines);
                schedule_list_vaccine.setAdapter(adapter);
                schedule_edt_search_vaccine2.setText("");
                textViewMessage.setVisibility(View.GONE);
            }
        });

    }

    private void filterVaccines() {
        vaccines = new ArrayList<>();
        vaccineOrigin.clear();
        vaccineOrigin.add("Tất cả");
        for (Map.Entry<String, Vaccines> entry : vaccinesHashMap.entrySet()) {
            Vaccines vaccine = entry.getValue();
            vaccine.setVaccine_id(entry.getKey());
            vaccines.add(vaccine);
            if (!vaccineOrigin.contains(vaccine.getOrigin())) {
                vaccineOrigin.add(vaccine.getOrigin());
            }
            Log.i("select vaccine", "onCreate ohhhh: " + vaccine.getVac_effectiveness() + " " + closestVaccineType);
            if (vaccine.getVac_effectiveness().equals(closestVaccineType)) {
                filterVaccine.add(vaccine);
            }
        }
        adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
        schedule_list_vaccine.setAdapter(adapter);
    }

    private void filterVaccineByOrigin(String selectedOrigin) {
        Log.i("select vaccine", "filterVaccineByOrigin: " + selectedOrigin);
        if (!selectedOrigin.isEmpty() && !selectedOrigin.equals("Tất cả")) {
            List<Vaccines> filteredList_ = new ArrayList<>();
            for (Vaccines vaccine : filterVaccine) {
                if (vaccine.getOrigin().equals(selectedOrigin)) {
                    filteredList_.add(vaccine);
                }
            }

            // Cập nhật Adapter và hiển thị danh sách lọc
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filteredList_);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            // Nếu không có xuất xứ nào được chọn, hiển thị toàn bộ danh sách
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    void search_vaccines(String searchText) {
        if (!searchText.isEmpty()) {
            filterVaccine.clear();
            for (Vaccines a : vaccines) {
                if (removeDiacritics(a.getVaccine_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))) {
                    filterVaccine.add(a);
                }
            }
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        } else {
            filterVaccine = new ArrayList<>(vaccines);
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }
}