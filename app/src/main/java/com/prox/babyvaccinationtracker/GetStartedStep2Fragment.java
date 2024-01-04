package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.prox.babyvaccinationtracker.model.BabyCheckList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetStartedStep2Fragment extends Fragment {

    Context context;

    BabyCheckList babyCheckList = new BabyCheckList();
    private CheckBox checkBoxTuberculosis, checkBoxHepatitisB, checkBoxDiphtheria, checkBoxParalysis,
            checkBoxPneumoniaHIBMeningitis, checkBoxRotavirusDiarrhea, checkBoxPneumoniaMeningitisOtitisMedia,
            checkBoxMeningitisSepsisPneumoniaACWY, checkBoxInfluenza, checkBoxMeasles, checkBoxMeaslesMumpsRubella,
            checkBoxChickenpox, checkBoxHepatitisA, checkBoxHepatitisAandB, checkBoxTetanus, checkBoxAnthrax, checkBoxMeningitisSepsisPneumoniaBC, checkBoxJapaneseEncephalitis;

    private EditText editCongenitalDisease;

    public GetStartedStep2Fragment() {
        // Required empty public constructor
    }

    public static GetStartedStep2Fragment newInstance(String param1, String param2) {
        GetStartedStep2Fragment fragment = new GetStartedStep2Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext() != null ? container.getContext() : null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_started_step2, container, false);
        checkBoxTuberculosis = view.findViewById(R.id.checkBoxTuberculosis);
        checkBoxHepatitisB = view.findViewById(R.id.checkBoxHepatitisB);
        checkBoxDiphtheria = view.findViewById(R.id.checkBoxDiphtheria);
        checkBoxParalysis = view.findViewById(R.id.checkBoxParalysis);
        checkBoxPneumoniaHIBMeningitis = view.findViewById(R.id.checkBoxPneumoniaHIBMeningitis);
        checkBoxRotavirusDiarrhea = view.findViewById(R.id.checkBoxRotavirusDiarrhea);
        checkBoxPneumoniaMeningitisOtitisMedia = view.findViewById(R.id.checkBoxPneumoniaMeningitisOtitisMedia);
        checkBoxMeningitisSepsisPneumoniaACWY = view.findViewById(R.id.checkBoxMeningitisSepsisPneumoniaACWY);
        checkBoxInfluenza = view.findViewById(R.id.checkBoxInfluenza);
        checkBoxMeasles = view.findViewById(R.id.checkBoxMeasles);
        checkBoxMeaslesMumpsRubella = view.findViewById(R.id.checkBoxMeaslesMumpsRubella);
        checkBoxChickenpox = view.findViewById(R.id.checkBoxChickenpox);
        checkBoxHepatitisA = view.findViewById(R.id.checkBoxHepatitisA);
        checkBoxHepatitisAandB = view.findViewById(R.id.checkBoxHepatitisAandB);
        checkBoxTetanus = view.findViewById(R.id.checkBoxTetanus);
        checkBoxAnthrax = view.findViewById(R.id.checkBoxAnthrax);
        editCongenitalDisease = view.findViewById(R.id.editCongenitalDisease);
        checkBoxMeningitisSepsisPneumoniaBC = view.findViewById(R.id.checkBoxMeningitisSepsisPneumoniaBC);
        checkBoxJapaneseEncephalitis = view.findViewById(R.id.checkBoxJapaneseEncephalitis);

        String birthDate = GetStartedActivity.baby.getBaby_birthday();
        Log.i("GetStarted", "onCreateView: " + birthDate);

        // Định dạng ngày tháng từ chuỗi ngày sinh
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = null;
        try {
            dateOfBirth = sdf.parse(birthDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dateOfBirth != null) {
            Calendar calendar = Calendar.getInstance();
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(dateOfBirth);

            int month = 0;
            if (calendar.get(Calendar.YEAR) > birthCalendar.get(Calendar.YEAR) ) {
                month = 12 * (calendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)) +
                        (calendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH));
                Log.i("GetStarted", "onCreateView: " + month);
            } else {
                month = calendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH);
                Log.i("GetStarted", "onCreateView: " + month);
            }
            Log.i("GetStarted", "dateOfBirthNotNull Month: " + month);
            int age = Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
            Log.i("GetStarted", "dateOfBirthNotNull Year: " + age);

            // Kiểm tra và đánh dấu các ô kiểm tra tương ứng với ngày sinh và tuổi

            if (month >= 0) {
                checkBoxTuberculosis.setChecked(true);
                GetStartedActivity.babyCheckList.setTuberculosis(true);
                checkBoxHepatitisB.setChecked(true);
                GetStartedActivity.babyCheckList.setHepatitis_b(true);
            }
            if (month >= 2) {
                checkBoxDiphtheria.setChecked(true);
                GetStartedActivity.babyCheckList.setDiphtheria_whooping_cough_poliomyelitis(true);
                checkBoxParalysis.setChecked(true);
                GetStartedActivity.babyCheckList.setParalysis(true);
                checkBoxPneumoniaHIBMeningitis.setChecked(true);
                GetStartedActivity.babyCheckList.setPneumonia_hib_meningitis(true);
                checkBoxRotavirusDiarrhea.setChecked(true);
                GetStartedActivity.babyCheckList.setRotavirus_diarrhea(true);
                checkBoxPneumoniaMeningitisOtitisMedia.setChecked(true);
                GetStartedActivity.babyCheckList.setPneumonia_meningitis_otitis_media_caused_by_streptococcus(true);
            }
            if (month >= 6) {
                checkBoxInfluenza.setChecked(true);
                GetStartedActivity.babyCheckList.setInfluenza(true);
                checkBoxMeningitisSepsisPneumoniaBC.setChecked(true);
                GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c(true);
            }
            if (month >= 9) {
                GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y(true);
                checkBoxMeningitisSepsisPneumoniaACWY.setChecked(true);
                GetStartedActivity.babyCheckList.setMeasles(true);
                checkBoxMeasles.setChecked(true);
                GetStartedActivity.babyCheckList.setMeasles_mumps_rubella(true);
                checkBoxJapaneseEncephalitis.setChecked(true);
checkBoxMeaslesMumpsRubella.setChecked(true);
                checkBoxMeaslesMumpsRubella.setChecked(true);
                GetStartedActivity.babyCheckList.setChickenpox(true);
                checkBoxChickenpox.setChecked(true);
            }
            if (month >= 12) {
                checkBoxMeaslesMumpsRubella.setChecked(true);
                GetStartedActivity.babyCheckList.setHepatitis_a(true);
                checkBoxHepatitisA.setChecked(true);
                GetStartedActivity.babyCheckList.setHepatitis_a_b(true);
                checkBoxHepatitisAandB.setChecked(true);
                GetStartedActivity.babyCheckList.setTetanus(true);
            }
            if (age >= 2) {
                checkBoxTetanus.setChecked(true);
                GetStartedActivity.babyCheckList.setAnthrax(true);
                checkBoxAnthrax.setChecked(true);
                GetStartedActivity.babyCheckList.setJapanese_encephalitis(true);
            }
        }

        if (GetStartedActivity.baby.getBaby_congenital_disease() != null) {
            editCongenitalDisease.setText(GetStartedActivity.baby.getBaby_congenital_disease());
        }

        checkBoxTuberculosis.setOnCheckedChangeListener(createCheckedChangeListener("tuberculosis"));
        checkBoxHepatitisB.setOnCheckedChangeListener(createCheckedChangeListener("hepatitis_b"));
        checkBoxDiphtheria.setOnCheckedChangeListener(createCheckedChangeListener("diphtheria_whooping_cough_poliomyelitis"));
        checkBoxParalysis.setOnCheckedChangeListener(createCheckedChangeListener("paralysis"));
        checkBoxPneumoniaHIBMeningitis.setOnCheckedChangeListener(createCheckedChangeListener("pneumonia_hib_meningitis"));
        checkBoxRotavirusDiarrhea.setOnCheckedChangeListener(createCheckedChangeListener("rotavirus_diarrhea"));
        checkBoxPneumoniaMeningitisOtitisMedia.setOnCheckedChangeListener(createCheckedChangeListener("pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        checkBoxMeningitisSepsisPneumoniaACWY.setOnCheckedChangeListener(createCheckedChangeListener("meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        checkBoxInfluenza.setOnCheckedChangeListener(createCheckedChangeListener("influenza"));
        checkBoxMeasles.setOnCheckedChangeListener(createCheckedChangeListener("measles"));
        checkBoxMeaslesMumpsRubella.setOnCheckedChangeListener(createCheckedChangeListener("measles_mumps_rubella"));
        checkBoxChickenpox.setOnCheckedChangeListener(createCheckedChangeListener("chickenpox"));
        checkBoxHepatitisA.setOnCheckedChangeListener(createCheckedChangeListener("hepatitis_a"));
        checkBoxHepatitisAandB.setOnCheckedChangeListener(createCheckedChangeListener("hepatitis_a_b"));
        checkBoxTetanus.setOnCheckedChangeListener(createCheckedChangeListener("tetanus"));
        checkBoxAnthrax.setOnCheckedChangeListener(createCheckedChangeListener("anthrax"));
        checkBoxMeningitisSepsisPneumoniaBC.setOnCheckedChangeListener(createCheckedChangeListener("meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        checkBoxJapaneseEncephalitis.setOnCheckedChangeListener(createCheckedChangeListener("japanese_encephalitis"));
        editCongenitalDisease.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String congenitalDisease = s.toString() != null ? s.toString() : "Không có bệnh lý bẩm sinh";
                Log.i("GetStarted2", "afterTextChanged: " + congenitalDisease);
                GetStartedActivity.baby.setBaby_congenital_disease(congenitalDisease);
            }
        });

        return view;
    }

    private CompoundButton.OnCheckedChangeListener createCheckedChangeListener(final String diseaseName) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu checkbox được chọn, xử lý tại đây (ví dụ: hiển thị thông báo)
                    String message = "Bạn đã chọn " + diseaseName;
                    switch (diseaseName) {
                        case "tuberculosis":
                            GetStartedActivity.babyCheckList.setTuberculosis(true);
                            break;
                        case "hepatitis_b":
                            GetStartedActivity.babyCheckList.setHepatitis_b(true);
                            break;
                        case "diphtheria_whooping_cough_poliomyelitis":
                            GetStartedActivity.babyCheckList.setDiphtheria_whooping_cough_poliomyelitis(true);
                            break;
                        case "paralysis":
                            GetStartedActivity.babyCheckList.setParalysis(true);
                            break;
                        case "pneumonia_hib_meningitis":
                            GetStartedActivity.babyCheckList.setPneumonia_hib_meningitis(true);
                            break;
                        case "rotavirus_diarrhea":
                            GetStartedActivity.babyCheckList.setRotavirus_diarrhea(true);
                            break;
                        case "pneumonia_meningitis_otitis_media_caused_by_streptococcus":
                            GetStartedActivity.babyCheckList.setPneumonia_meningitis_otitis_media_caused_by_streptococcus(true);
                            break;
                        case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c":
                            GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c(true);
                            break;
                        case "influenza":
                            GetStartedActivity.babyCheckList.setInfluenza(true);
                            break;
                        case "measles":
                            GetStartedActivity.babyCheckList.setMeasles(true);
                            break;
                        case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y":
                            GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y(true);
                            break;
                        case "japanese_encephalitis":
                            GetStartedActivity.babyCheckList.setJapanese_encephalitis(true);
                            break;
                        case "measles_mumps_rubella":
                            GetStartedActivity.babyCheckList.setMeasles_mumps_rubella(true);
                            break;
                        case "chickenpox":
                            GetStartedActivity.babyCheckList.setChickenpox(true);
                            break;
                        case "hepatitis_a":
                            GetStartedActivity.babyCheckList.setHepatitis_a(true);
                            break;
                        case "hepatitis_a_b":
                            GetStartedActivity.babyCheckList.setHepatitis_a_b(true);
                            break;
                        case "tetanus":
                            GetStartedActivity.babyCheckList.setTetanus(true);
                            break;
                        case "anthrax":
                            GetStartedActivity.babyCheckList.setAnthrax(true);
                            break;
                    }
                    Log.i("getStarted2", "onCheckedChanged: checked " + diseaseName);
                } else {
                    switch (diseaseName) {
                        case "tuberculosis":
                            GetStartedActivity.babyCheckList.setTuberculosis(false);
                            break;
                        case "hepatitis_b":
                            GetStartedActivity.babyCheckList.setHepatitis_b(false);
                            break;
                        case "diphtheria_whooping_cough_poliomyelitis":
                            GetStartedActivity.babyCheckList.setDiphtheria_whooping_cough_poliomyelitis(false);
                            break;
                        case "paralysis":
                            GetStartedActivity.babyCheckList.setParalysis(false);
                            break;
                        case "pneumonia_hib_meningitis":
                            GetStartedActivity.babyCheckList.setPneumonia_hib_meningitis(false);
                            break;
                        case "rotavirus_diarrhea":
                            GetStartedActivity.babyCheckList.setRotavirus_diarrhea(false);
                            break;
                        case "pneumonia_meningitis_otitis_media_caused_by_streptococcus":
                            GetStartedActivity.babyCheckList.setPneumonia_meningitis_otitis_media_caused_by_streptococcus(false);
                            break;
                        case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c":
                            GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c(false);
                            break;
                        case "influenza":
                            GetStartedActivity.babyCheckList.setInfluenza(false);
                            break;
                        case "measles":
                            GetStartedActivity.babyCheckList.setMeasles(false);
                            break;
                        case "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y":
                            GetStartedActivity.babyCheckList.setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y(false);
                            break;
                        case "japanese_encephalitis":
                            GetStartedActivity.babyCheckList.setJapanese_encephalitis(false);
                            break;
                        case "measles_mumps_rubella":
                            GetStartedActivity.babyCheckList.setMeasles_mumps_rubella(false);
                            break;
                        case "chickenpox":
                            GetStartedActivity.babyCheckList.setChickenpox(false);
                            break;
                        case "hepatitis_a":
                            GetStartedActivity.babyCheckList.setHepatitis_a(false);
                            break;
                        case "hepatitis_a_b":
                            GetStartedActivity.babyCheckList.setHepatitis_a_b(false);
                            break;
                        case "tetanus":
                            GetStartedActivity.babyCheckList.setTetanus(false);
                            break;
                        case "anthrax":
                            GetStartedActivity.babyCheckList.setAnthrax(false);
                            break;
                    }
                    Log.i("getStarted2", "onCheckedChanged: unchecked " + diseaseName);
                }
            }
        };
    }
}