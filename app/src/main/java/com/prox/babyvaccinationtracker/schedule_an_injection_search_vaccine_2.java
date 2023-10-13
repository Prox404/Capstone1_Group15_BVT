package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.prox.babyvaccinationtracker.adapter.VaccineAdapter;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class schedule_an_injection_search_vaccine_2 extends AppCompatActivity {
    ImageView schedule_image_back_vaccine2;
    EditText schedule_edt_search_vaccine2;
    ListView schedule_list_vaccine;
    ArrayList<Vaccines> vaccines;
    ArrayList<Vaccines> filterVaccine;

    VaccineAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_search_vaccine2);
        schedule_image_back_vaccine2 = findViewById(R.id.schedule_image_back_vaccine2);
        schedule_edt_search_vaccine2 = findViewById(R.id.schedule_edt_search_vaccine2);
        schedule_list_vaccine = findViewById(R.id.schedule_list_vaccine);
        schedule_image_back_vaccine2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        HashMap<String, Vaccines> vaccinesHashMap = (HashMap<String, Vaccines>) intent.getSerializableExtra("Vaccines"); // hashmap
        vaccines = new ArrayList<>(vaccinesHashMap.values());
        filterVaccine = new ArrayList<>(vaccines);
        adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, vaccines);
        schedule_list_vaccine.setAdapter(adapter);
        schedule_list_vaccine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vaccines vaccines1 = (Vaccines) adapterView.getItemAtPosition(i);
                Intent intent1 = new Intent();
                intent1.putExtra("vaccine_choose",vaccines1);
                setResult(RESULT_OK,intent1);
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


    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    void search_vaccines(String searchText){
        if(!searchText.isEmpty()){
            filterVaccine.clear();
            for(Vaccines a : vaccines){
                if(removeDiacritics(a.getVaccine_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))){
                    filterVaccine.add(a);
                }
            }
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        }
        else{
            filterVaccine = new ArrayList<>(vaccines);
            adapter = new VaccineAdapter(schedule_an_injection_search_vaccine_2.this, filterVaccine);
            schedule_list_vaccine.setAdapter(adapter);
        }
    }
}