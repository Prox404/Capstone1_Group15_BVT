package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Customer;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schedule_an_injection extends AppCompatActivity {


    TextView schedule_baby_congenital_disease, schedule_bady_birthday, schedule_baby_gender, schedule_bady_name, schedule_tv_cus_name, schedule_tv_cus_email, schedule_tv_cus_phone;
    Button schedule_btn_add;
    LinearLayout schedule_list_btn_babies,linear_select_vaccine;
    RadioButton radioButton_center,radioButton_cares;
    EditText schedule_edt_date_vaccine, schedule_edt_vaccine_center;
    EditText schedule_edt_tinh, schedule_edt_quan, schedule_edt_phuong;
    EditText schedule_edt_type_vaccine;
    Customer customer;
    Baby babyhavebeenchoose = new Baby();

    Vaccines vaccineschoose = new Vaccines();

    Vaccine_center vaccineCenter = new Vaccine_center();

    boolean take_care_vaccine = false;

    HashMap<String, Vaccines> vaccines;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatePickerDialog datePickerDialog;
    View loadingLayout;

    String address = "";

    String baby_id = "";

    final int VACCINE_CENTER_NAME = 1;
    final int VACCINE_CHOOSE = 5;
    final int VACCINE_CHOOSE_2 = 6;
    final int VACCINE_PROVINCES = 2;
    final int VACCINE_DISTRICT = 3;
    final int VACCINE_WARD = 4;

    public static ArrayList<String> vaccination_registrations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection);
        // Ánh xạ

        linear_select_vaccine = findViewById(R.id.linear_select_vaccine);
        radioButton_center = findViewById(R.id.radioButton_center);
        radioButton_cares = findViewById(R.id.radioButton_cares);
        loadingLayout = findViewById(R.id.loadingLayout);

        radioButton_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                take_care_vaccine = false;
                linear_select_vaccine.setVisibility(View.VISIBLE);
                schedule_edt_vaccine_center.setText("");
                schedule_edt_type_vaccine.setText("");

                // Tạo TransitionDrawable
                Drawable[] layers_in = new Drawable[2];
                layers_in[0] = getResources().getDrawable(R.drawable.custom_radio_normal);
                layers_in[1] = getResources().getDrawable(R.drawable.custom_radio_selected);
                TransitionDrawable transitionDrawable_in = new TransitionDrawable(layers_in);

                Drawable[] layers_out = new Drawable[2];
                layers_out[1] = getResources().getDrawable(R.drawable.custom_radio_normal);
                layers_out[0] = getResources().getDrawable(R.drawable.custom_radio_selected);
                TransitionDrawable transitionDrawable_out = new TransitionDrawable(layers_out);

                // Set background và chạy transition
                radioButton_cares.setBackground(transitionDrawable_out);
                radioButton_center.setBackground(transitionDrawable_in);
                transitionDrawable_in.startTransition(300); // Thời gian chuyển đổi (milliseconds)
                transitionDrawable_out.startTransition(300);

            }
        });
        radioButton_cares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                take_care_vaccine = true;
                linear_select_vaccine.setVisibility(View.GONE);
                schedule_edt_vaccine_center.setText("");
                schedule_edt_type_vaccine.setText("");

                // Tạo TransitionDrawable
                Drawable[] layers_in = new Drawable[2];
                layers_in[0] = getResources().getDrawable(R.drawable.custom_radio_normal);
                layers_in[1] = getResources().getDrawable(R.drawable.custom_radio_selected);
                TransitionDrawable transitionDrawable_in = new TransitionDrawable(layers_in);

                Drawable[] layers_out = new Drawable[2];
                layers_out[1] = getResources().getDrawable(R.drawable.custom_radio_normal);
                layers_out[0] = getResources().getDrawable(R.drawable.custom_radio_selected);
                TransitionDrawable transitionDrawable_out = new TransitionDrawable(layers_out);

                // Set background và chạy transition
                radioButton_cares.setBackground(transitionDrawable_in);
                radioButton_center.setBackground(transitionDrawable_out);
                transitionDrawable_in.startTransition(300); // Thời gian chuyển đổi (milliseconds)
                transitionDrawable_out.startTransition(300);
            }
        });

        schedule_edt_tinh = findViewById(R.id.schedule_edt_tinh);
        schedule_edt_tinh.setFocusable(false);
        schedule_edt_tinh.setClickable(true);
        schedule_edt_tinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Schedule_an_injection.this, schedule_an_injection_provinces.class);
                startActivityForResult(i, VACCINE_PROVINCES);
            }
        });
        schedule_edt_quan = findViewById(R.id.schedule_edt_quan);
        schedule_edt_quan.setFocusable(false);
        schedule_edt_quan.setClickable(false);
        schedule_edt_phuong = findViewById(R.id.schedule_edt_phuong);
        schedule_edt_phuong.setFocusable(false);
        schedule_edt_phuong.setClickable(false);

        schedule_tv_cus_name = findViewById(R.id.schedule_tv_cus_name);
        schedule_tv_cus_email = findViewById(R.id.schedule_tv_cus_email);
        schedule_tv_cus_phone = findViewById(R.id.schedule_tv_cus_phone);

        schedule_bady_name = findViewById(R.id.schedule_bady_name);
        schedule_baby_gender = findViewById(R.id.schedule_baby_gender);
        schedule_bady_birthday = findViewById(R.id.schedule_bady_birthday);
        schedule_baby_congenital_disease = findViewById(R.id.schedule_baby_congenital_disease);

        schedule_btn_add = findViewById(R.id.schedule_btn_add);
        schedule_list_btn_babies = findViewById(R.id.schedule_list_btn_babies);
        schedule_edt_date_vaccine = findViewById(R.id.schedule_edt_date_vaccine);
        schedule_edt_vaccine_center = findViewById(R.id.schedule_edt_vaccine_center);

        schedule_edt_type_vaccine = findViewById(R.id.schedule_edt_type_vaccine);
        schedule_edt_type_vaccine.setFocusable(false);
        schedule_edt_type_vaccine.setClickable(false);

        schedule_edt_type_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CHECK_BOOL", take_care_vaccine+"");
                if(take_care_vaccine){
                    Intent intent = new Intent(Schedule_an_injection.this, schedule_an_injection_search_vaccine_2_2.class);
                    intent.putExtra("baby_id", baby_id);
                    startActivityForResult(intent, VACCINE_CHOOSE_2);
                }else {
                    if(schedule_edt_vaccine_center.length() != 0){
                        if (vaccines != null) {
                            Intent intent = new Intent(Schedule_an_injection.this, schedule_an_injection_search_vaccine_2.class);
                            intent.putExtra("Vaccines", vaccines);
                            intent.putExtra("baby_id", baby_id);
                            startActivityForResult(intent, VACCINE_CHOOSE);
                        } else {
                            Toast.makeText(Schedule_an_injection.this, "Bệnh viện này chưa có vắc-xin", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        // chọn ngày
        Calendar currentDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                Schedule_an_injection.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        schedule_edt_date_vaccine.setText(selectedDate);
                    }
                },
                currentDate.get(Calendar.YEAR), // Năm mặc định
                currentDate.get(Calendar.MONTH), // Tháng mặc định
                currentDate.get(Calendar.DAY_OF_MONTH) // Ngày mặc định
        );
        // set ngày nhỏ nhất là ngày hiện tại + 1
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis() + 86400000);
        schedule_edt_date_vaccine.setFocusable(false);
        schedule_edt_date_vaccine.setClickable(true);
        schedule_edt_date_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


        schedule_edt_vaccine_center.setFocusable(false);
        schedule_edt_vaccine_center.setClickable(true);
        schedule_edt_vaccine_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baby_id.isEmpty()) {
                    Toast.makeText(Schedule_an_injection.this, "Phải chọn trẻ em", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(Schedule_an_injection.this, Schedule_an_injection_search_vaccine.class);
                i.putExtra("baby_id", baby_id);
                i.putExtra("cus_address", address);
                startActivityForResult(i, VACCINE_CENTER_NAME);
            }
        });

        // hiển thị thông tin khách hàng và con của họ
        GetData_customer();


        // Đăng ký vaccine
        schedule_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule_edt_vaccine_center.length() == 0) {
                    Toast.makeText(Schedule_an_injection.this, "Phải chọn trung tâm tiêm chủng", Toast.LENGTH_LONG).show();
                    return;
                }
                if (schedule_edt_type_vaccine.length() == 0) {
                    Toast.makeText(Schedule_an_injection.this, "Phải chọn loại vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }
                if (schedule_edt_date_vaccine.length() == 0) {
                    Toast.makeText(Schedule_an_injection.this, "Phải ngày mong muốn tiêm", Toast.LENGTH_LONG).show();
                    return;
                }
                loadingLayout.setVisibility(View.VISIBLE);
                DatabaseReference reference = database.getReference("Vaccination_Registration");

                Vaccination_Registration vaccinationRegistration = new Vaccination_Registration();
                vaccinationRegistration.setBaby(babyhavebeenchoose);
                vaccinationRegistration.setCus(customer);
                vaccinationRegistration.setVaccine(vaccineschoose);
                vaccinationRegistration.setCenter(vaccineCenter);
                vaccinationRegistration.setRegist_created_at(schedule_edt_date_vaccine.getText().toString());
                vaccinationRegistration.setStatus(0); // 0 là đang xác nhận, 1 là xác nhận
                reference.push().setValue(vaccinationRegistration).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Schedule_an_injection.this, "Đăng ký thành công, vui lòng đợi trung tâm xác nhận", Toast.LENGTH_LONG).show();
                            loadingLayout.setVisibility(View.GONE);
                            finish();
                        } else {
                            loadingLayout.setVisibility(View.GONE);
                            Toast.makeText(Schedule_an_injection.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void getRegistrationSchedule(String baby_id){
        DatabaseReference reference = database.getReference("Vaccination_Registration");
        Query query = reference.orderByChild("baby/baby_id").equalTo(baby_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccination_registrations.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                    Log.i("Registration", "onDataChange: " + vaccination_registration.toString());
                    if (vaccination_registration.getStatus() < 3 && vaccination_registration.getStatus() >= 0){
                        vaccination_registration.setRegist_id(dataSnapshot.getKey());
                        vaccination_registrations.add(vaccination_registration.getVaccine().getVac_effectiveness());
                        Log.i("Registration", "getRegistrationSchedule: " + vaccination_registrations);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VACCINE_CENTER_NAME) {
            if (resultCode == RESULT_OK) {
                take_care_vaccine = false;
                schedule_edt_type_vaccine.setText("");
                vaccineCenter = (Vaccine_center) data.getSerializableExtra("selected_vaccine"); // đối tượng vaccine center được chọn
                vaccines = vaccineCenter.getVaccines();
                vaccineCenter.setVaccines(null);
                schedule_edt_vaccine_center.setText(vaccineCenter.getCenter_name());
                // Todo không hiển thị trung tâm không có vắc-xin
                Log.i("VVAAAAANINEIE", vaccineCenter.toString());
            }
        } else if (requestCode == VACCINE_CHOOSE) {
            if (resultCode == RESULT_OK) {
                take_care_vaccine = false;
                vaccineschoose = (Vaccines) data.getSerializableExtra("vaccine_choose");
                schedule_edt_type_vaccine.setText(vaccineschoose.getVaccine_name());
            }
        }
        else if(requestCode == VACCINE_CHOOSE_2){
            if(resultCode == RESULT_OK){
                take_care_vaccine = true;
                vaccineCenter = (Vaccine_center) data.getSerializableExtra("selected_vaccine");
                vaccineschoose = (Vaccines) data.getSerializableExtra("vaccine_choose");
                linear_select_vaccine.setVisibility(View.VISIBLE);
                schedule_edt_type_vaccine.setText(vaccineschoose.getVaccine_name());
                schedule_edt_vaccine_center.setText(vaccineCenter.getCenter_name());
            }

        }
        else if (requestCode == VACCINE_PROVINCES) {
            if (resultCode == RESULT_OK) {
                // đặt lại quận phường
                schedule_edt_quan.setText("");
                schedule_edt_phuong.setText("");

                // đặt dữ liệu
                String p = data.getStringExtra("provinceNAME");
                schedule_edt_tinh.setText(p);
                address = schedule_edt_tinh.getText().toString();

                // chọn quận
                schedule_edt_quan.setClickable(true);
                schedule_edt_quan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int code = data.getIntExtra("provinceCODE", 0);
                        Intent i = new Intent(Schedule_an_injection.this, schedule_an_injection_districts.class);
                        i.putExtra("provinceCODE", code);
                        startActivityForResult(i, VACCINE_DISTRICT);
                    }
                });
            }
        } else if (requestCode == VACCINE_DISTRICT) {
            if (resultCode == RESULT_OK) {
                // đặt lại phường
                schedule_edt_phuong.setText("");
                // đặt dữ liệu
                String d = data.getStringExtra("districtNAME");
                schedule_edt_quan.setText(d);
                String ar = schedule_edt_quan.getText() + ", " + schedule_edt_tinh.getText();
                address = ar;

                schedule_edt_phuong.setClickable(true);
                schedule_edt_phuong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!schedule_edt_quan.getText().toString().isEmpty()) {
                            int code = data.getIntExtra("districtCODE", 0);
                            Intent i = new Intent(Schedule_an_injection.this, schedule_an_injection_wards.class);
                            i.putExtra("districtCODE", code);
                            startActivityForResult(i, VACCINE_WARD);
                        }
                    }
                });
            }
        } else if (requestCode == VACCINE_WARD) {
            if (resultCode == RESULT_OK) {
                String w = data.getStringExtra("wardNAME");
                schedule_edt_phuong.setText(w);
                String ar = schedule_edt_phuong.getText() + ", " + schedule_edt_quan.getText() + ", " + schedule_edt_tinh.getText();
                address = ar;
            }
        }
    }

    private void GetData_customer() {
        // todo làm với share preferences
        Context mcontext = Schedule_an_injection.this;
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("user", Context.MODE_PRIVATE);

        String customerID = sharedPreferences.getString("customer_id", "");
        String cusName = sharedPreferences.getString("cus_name", "");
        String cusBirthday = sharedPreferences.getString("cus_birthday", "");
        String cusAddress = sharedPreferences.getString("cus_address", "");
        String cusPhone = sharedPreferences.getString("cus_phone", "");
        String cusEmail = sharedPreferences.getString("cus_email", "");
        String cusGender = sharedPreferences.getString("cus_gender", "");
        String cusEthnicity = sharedPreferences.getString("cus_ethnicity", "");
        String cus_avatar = sharedPreferences.getString("cus_avatar", "");
        customer = new Customer(cusName, cusBirthday, cusAddress, cusPhone, cusEmail, cusGender, cusEthnicity, cus_avatar);
        customer.setCustomer_id(customerID);
        Log.i("TAG", "GetData_customer: " + customer.toString());


        String babiesJson = sharedPreferences.getString("babiesList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Baby>>() {
        }.getType();
        List<Baby> babiesList = gson.fromJson(babiesJson, type);

        for (Baby baby : babiesList) {
            addButtonForBaby(baby);
        }

        //  Todo làm với firebase

        schedule_tv_cus_name.setText(customer.getCus_name());
        schedule_tv_cus_email.setText(customer.getCus_email());
        schedule_tv_cus_phone.setText(customer.getCus_phone());
    }

    private void addButtonForBaby(final Baby baby) {
        Button button = new Button(this);
        button.setText(baby.getBaby_name());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 15, 0);
        button.setLayoutParams(params);
        button.setElevation(0);
        button.setPadding(20, 5, 20, 5);
        button.setHeight(30);
        button.setMinimumHeight(130);
        button.setMinHeight(0);
        button.setStateListAnimator(null);

        // Nếu babyListContainer chưa có Button nào, hoặc Button đầu tiên được thêm vào
        if (schedule_list_btn_babies.getChildCount() == 0) {
            // Thiết lập background cho Button đầu tiên là color/primaryColor
            button.setBackground(getResources().getDrawable(R.drawable.rounded_primary_button_bg));
            button.setTextColor(getResources().getColor(R.color.white));

            babyhavebeenchoose = baby;
            baby_id = baby.getBaby_id();
            schedule_bady_name.setText(baby.getBaby_name());
            schedule_baby_gender.setText(baby.getBaby_gender());
            schedule_bady_birthday.setText(baby.getBaby_birthday());
            schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());
        } else {
            // Thiết lập background mặc định cho tất cả các Button khác
            button.setBackground(getResources().getDrawable(R.drawable.rounded_white_button_bg));
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babyhavebeenchoose = baby;
                baby_id = baby.getBaby_id();
                getRegistrationSchedule(baby_id);
                Log.i("Schedule injection", "onClick: baby id " + baby_id);
                schedule_bady_name.setText(baby.getBaby_name());
                schedule_baby_gender.setText(baby.getBaby_gender());
                schedule_bady_birthday.setText(baby.getBaby_birthday());
                schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());

                resetButtonBackgrounds();

                button.setBackground(getResources().getDrawable(R.drawable.rounded_primary_button_bg));
                button.setTextColor(getResources().getColor(R.color.white));
            }
        });

        schedule_list_btn_babies.addView(button);
    }

    private void resetButtonBackgrounds() {
        // Lặp qua tất cả các Button trong babyListContainer và đặt background về màu trắng
        for (int i = 0; i < schedule_list_btn_babies.getChildCount(); i++) {
            View child = schedule_list_btn_babies.getChildAt(i);
            if (child instanceof Button) {
                ((Button) child).setBackground(getResources().getDrawable(R.drawable.rounded_white_button_bg));
                ((Button) child).setTextColor(getResources().getColor(R.color.textColor));
            }
        }
    }

}