package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    TextView schedule_baby_congenital_disease,schedule_bady_birthday,schedule_baby_gender,schedule_bady_name,schedule_tv_cus_name,schedule_tv_cus_email,schedule_tv_cus_phone;
    Button schedule_btn_add;
    LinearLayout schedule_list_btn_babies;
    EditText schedule_edt_date_vaccine,schedule_edt_vaccine_center;
    EditText schedule_edt_tinh,schedule_edt_quan,schedule_edt_phuong;
    EditText schedule_edt_type_vaccine;


    String id = "IMdLT6gpalXk3aJTDWlndkye2tN2";
    Customer customer;
    Baby babyhavebeenchoose = new Baby();

    Vaccines vaccineschoose = new Vaccines();

    Vaccine_center vaccineCenter = new Vaccine_center();


    ArrayList<Baby> babies = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatePickerDialog datePickerDialog;

    String address = "";

    final int VACCINE_CENTER_NAME = 1;
    final int VACCINE_CHOOSE = 5;
    final int VACCINE_PROVINCES = 2;
    final int VACCINE_DISTRICT = 3;
    final int VACCINE_WARD = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection);
        // Ánh xạ
        schedule_edt_tinh = findViewById(R.id.schedule_edt_tinh);
        schedule_edt_tinh.setFocusable(false);
        schedule_edt_tinh.setClickable(true);
        schedule_edt_tinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Schedule_an_injection.this, schedule_an_injection_provinces.class);
                startActivityForResult(i,VACCINE_PROVINCES);
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
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
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
                Intent i = new Intent(Schedule_an_injection.this, Schedule_an_injection_search_vaccine.class);

                i.putExtra("cus_address", address);
                startActivityForResult(i,VACCINE_CENTER_NAME);
            }
        });

        // hiển thị thông tin khách hàng và con của họ
        GetData_customer();


        // Đăng ký vaccine
        schedule_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule_edt_vaccine_center.length()==0){
                    Toast.makeText(Schedule_an_injection.this,"Phải chọn trung tâm tiêm chủng", Toast.LENGTH_LONG).show();
                    return;
                }
                if(schedule_edt_type_vaccine.length()==0){
                    Toast.makeText(Schedule_an_injection.this,"Phải chọn loại vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }
                if(schedule_edt_date_vaccine.length()==0){
                    Toast.makeText(Schedule_an_injection.this,"Phải ngày mong muốn tiêm", Toast.LENGTH_LONG).show();
                    return;
                }
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
                        if(task.isSuccessful())
                            Toast.makeText(Schedule_an_injection.this, "successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Schedule_an_injection.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VACCINE_CENTER_NAME){
            if (resultCode == RESULT_OK){
                schedule_edt_type_vaccine.setText("");
                vaccineCenter = (Vaccine_center) data.getSerializableExtra("selected_vaccine"); // đối tượng vaccine center được chọn
                HashMap<String,Vaccines> vaccines = vaccineCenter.getVaccines();
                vaccineCenter.setVaccines(null);
                schedule_edt_vaccine_center.setText(vaccineCenter.getCenter_name());
                // Todo không hiển thị trung tâm không có vắc-xin
                Log.i("VVAAAAANINEIE", vaccineCenter.toString());
                schedule_edt_type_vaccine.setClickable(false);
                if(vaccineCenter == null){
                    schedule_edt_type_vaccine.setClickable(false);
                }
                else if(vaccineCenter.getVaccines() == null) {
                    schedule_edt_type_vaccine.setClickable(false);
                }
                schedule_edt_type_vaccine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(vaccines != null){
                            Intent intent = new Intent(Schedule_an_injection.this, schedule_an_injection_search_vaccine_2.class);
                            intent.putExtra("Vaccines",vaccines);
                            startActivityForResult(intent,VACCINE_CHOOSE);
                        }
                        else{
                            Toast.makeText(Schedule_an_injection.this,"Bệnh viện này chưa có vắc-xin", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        else if(requestCode == VACCINE_CHOOSE){
            if (resultCode == RESULT_OK){
                vaccineschoose = (Vaccines) data.getSerializableExtra("vaccine_choose");
                schedule_edt_type_vaccine.setText(vaccineschoose.getVaccine_name());
            }
        }
        else if (requestCode == VACCINE_PROVINCES){
            if (resultCode == RESULT_OK){
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
                        int code = data.getIntExtra("provinceCODE",0);
                        Intent i = new Intent(Schedule_an_injection.this,schedule_an_injection_districts.class);
                        i.putExtra("provinceCODE",code);
                        startActivityForResult(i,VACCINE_DISTRICT);
                    }
                });
            }
        }
        else if (requestCode == VACCINE_DISTRICT){
            if (resultCode == RESULT_OK){
                // đặt lại phường
                schedule_edt_phuong.setText("");
                // đặt dữ liệu
                String d = data.getStringExtra("districtNAME");
                schedule_edt_quan.setText(d);
                String ar = schedule_edt_quan.getText()+", "+schedule_edt_tinh.getText();
                address = ar;

                schedule_edt_phuong.setClickable(true);
                schedule_edt_phuong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!schedule_edt_quan.getText().toString().isEmpty()){
                            int code = data.getIntExtra("districtCODE",0);
                            Intent i = new Intent(Schedule_an_injection.this, schedule_an_injection_wards.class);
                            i.putExtra("districtCODE",code);
                            startActivityForResult(i,VACCINE_WARD);
                        }
                    }
                });
            }
        }
        else if (requestCode == VACCINE_WARD){
            if(resultCode == RESULT_OK){
                String w = data.getStringExtra("wardNAME");
                schedule_edt_phuong.setText(w);
                String ar = schedule_edt_phuong.getText()+", "+schedule_edt_quan.getText()+", "+schedule_edt_tinh.getText();
                address = ar;
            }
        }
    }

    private void GetData_customer(){
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
        String cusEthnicity = sharedPreferences.getString("cus_ethnicity","");
        String cus_avatar = sharedPreferences.getString("cus_avatar","");
        customer = new Customer(customerID,cusName,cusBirthday,cusAddress,cusPhone,cusEmail,cusGender,cusEthnicity,cus_avatar);

        String babiesJson = sharedPreferences.getString("babiesList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Baby>>() {}.getType();
        List<Baby> babiesList = gson.fromJson(babiesJson, type);


//        int number_of_babies = babiesList.size();
        for (Baby baby : babiesList) {
            addButtonForBaby(baby);
        }
//        for (int i = 0; i < number_of_babies; i++) {
//            Baby baby = babiesList.get(i);
//            Button button = new Button(Schedule_an_injection.this);
//            button.setText(baby.getBaby_name());
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    babyhavebeenchoose = baby;
//                    schedule_bady_name.setText(baby.getBaby_name());
//                    schedule_baby_gender.setText(baby.getBaby_gender());
//                    schedule_bady_birthday.setText(baby.getBaby_birthday());
//                    schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());
//                }
//            });
//
//            schedule_list_btn_babies.addView(button);
//        }




        //  Todo làm với firebase

//        DatabaseReference reference = database.getReference("users");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean check;
//                try {
//                    String cus_name = snapshot.child("customers").child(id).child("cus_name").getValue(String.class);
//                    String cus_phone = snapshot.child("customers").child(id).child("cus_phone").getValue(String.class);
//                    String cus_gender = snapshot.child("customers").child(id).child("cus_gender").getValue(String.class);
//                    String cus_ethnicity = snapshot.child("customers").child(id).child("cus_ethnicity").getValue(String.class);
//                    String cus_email = snapshot.child("customers").child(id).child("cus_email").getValue(String.class);
//                    String cus_birthday = snapshot.child("customers").child(id).child("cus_birthday").getValue(String.class);
//                    String cus_avatar = snapshot.child("customers").child(id).child("cus_avatar").getValue(String.class);
//                    String cus_address = snapshot.child("customers").child(id).child("cus_address").getValue(String.class);
//                    check = false;
//                    if (snapshot.child("customers").child(id).child("babies") != null) {
//                        for (DataSnapshot customerSnapshot : snapshot.child("customers").child(id).child("babies").getChildren()) {
//                            babies.add(customerSnapshot.getValue(Baby.class));
//                        }
//                        check = true;
//                    }
//
//
//                    babyhavebeenchoose = babies.get(0); // để mặc đinh là bé đầu tiên được hiển thị và được chọn
//
//
//                    String []ar = cus_address.split(", "); // cắt chuỗi đưa ra
//                    schedule_edt_phuong.setText(ar[0]);
//                    schedule_edt_quan.setText(ar[1]);
//                    schedule_edt_tinh.setText(ar[2]);
//
//                    customer.setCustomer_id(id);
//                    customer.setCus_name(cus_name);
//                    customer.setCus_birthday(cus_birthday);
//                    customer.setCus_address(cus_address);
//                    customer.setCus_phone(cus_phone);
//                    customer.setCus_email(cus_email);
//                    customer.setCus_gender(cus_gender);
//                    customer.setCus_ethnicity(cus_ethnicity);
//                    customer.setCus_avatar(cus_avatar);
//
//                    address = customer.getCus_address().toString(); // lấy địa chỉ
//
//                } catch (Exception e) {
//                    Log.i("GENSHINNNNNNNNN", "" + e);
//                    return;
//                }
//                schedule_tv_cus_name.setText(customer.getCus_name());
//                schedule_tv_cus_email.setText(customer.getCus_email());
//                schedule_tv_cus_phone.setText(customer.getCus_phone());
//                if (check) {
//                    schedule_bady_name.setText(babies.get(0).getBaby_name());
//                    schedule_baby_gender.setText(babies.get(0).getBaby_gender());
//                    schedule_bady_birthday.setText(babies.get(0).getBaby_birthday());
//                    schedule_baby_congenital_disease.setText(babies.get(0).getBaby_congenital_disease());
//                }
//
//
//                int number_of_babies = babies.size();
//                if (number_of_babies > 0) {
//                    for (int i = 0; i < number_of_babies; i++) {
//                        Baby baby = babies.get(i);
//                        Button button = new Button(Schedule_an_injection.this);
//                        button.setText(babies.get(i).getBaby_name());
//
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                babyhavebeenchoose = baby;
//                                schedule_bady_name.setText(baby.getBaby_name());
//                                schedule_baby_gender.setText(baby.getBaby_gender());
//                                schedule_bady_birthday.setText(baby.getBaby_birthday());
//                                schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());
//                            }
//                        });
//
//                        schedule_list_btn_babies.addView(button);
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    private void addButtonForBaby(final Baby baby){
        Button button = new Button(this);
        button.setText(baby.getBaby_name());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babyhavebeenchoose = baby;
                schedule_bady_name.setText(baby.getBaby_name());
                schedule_baby_gender.setText(baby.getBaby_gender());
                schedule_bady_birthday.setText(baby.getBaby_birthday());
                schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());
            }
        });

        schedule_list_btn_babies.addView(button);
    }

}