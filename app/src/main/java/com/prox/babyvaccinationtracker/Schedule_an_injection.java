package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Customer;

import java.util.ArrayList;

public class Schedule_an_injection extends AppCompatActivity {


    TextView schedule_baby_congenital_disease,schedule_bady_birthday,schedule_baby_gender,schedule_bady_name,schedule_tv_cus_name,schedule_tv_cus_email,schedule_tv_cus_phone;
    Button schedule_btn_add;
    LinearLayout schedule_list_btn_babies;
    EditText schedule_edt_date_vaccine,schedule_edt_vaccine_center;
    Spinner schedule_spinner_type_vaccine;


    String id = "IMdLT6gpalXk3aJTDWlndkye2tN2";
    Customer customer = new Customer();
    ArrayList<Baby> babies = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatePickerDialog datePickerDialog;
    ListView listview_vaccine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection);
        // Ánh xạ
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

        schedule_spinner_type_vaccine = findViewById(R.id.schedule_spinner_type_vaccine);

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
                //i.putExtra("cus_address", customer.getCus_address());
                startActivityForResult(i,1);
            }
        });

        // hiển thị thông tin khách hàng và con của họ
        GetDataOnFirebase_customer();


        // Đăng ký vaccine
        schedule_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == RESULT_OK){
                schedule_edt_vaccine_center.setText(data.getStringExtra("center_name")+"");
                ArrayList<String> a = data.getStringArrayListExtra("center_vaccines");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Schedule_an_injection.this, android.R.layout.simple_list_item_1, a);
                schedule_spinner_type_vaccine.setAdapter(adapter);
            }
        }
    }

    private void GetDataOnFirebase_customer(){

        DatabaseReference reference = database.getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean check;
                try {
                    String cus_name = snapshot.child("customers").child(id).child("cus_name").getValue(String.class);
                    String cus_phone = snapshot.child("customers").child(id).child("cus_phone").getValue(String.class);
                    String cus_gender = snapshot.child("customers").child(id).child("cus_gender").getValue(String.class);
                    String cus_ethnicity = snapshot.child("customers").child(id).child("cus_ethnicity").getValue(String.class);
                    String cus_email = snapshot.child("customers").child(id).child("cus_email").getValue(String.class);
                    String cus_birthday = snapshot.child("customers").child(id).child("cus_birthday").getValue(String.class);
                    String cus_avatar = snapshot.child("customers").child(id).child("cus_avatar").getValue(String.class);
                    String cus_address = snapshot.child("customers").child(id).child("cus_address").getValue(String.class);
                    check = false;
                    if (snapshot.child("customers").child(id).child("babies") != null) {
                        for (DataSnapshot customerSnapshot : snapshot.child("customers").child(id).child("babies").getChildren()) {
                            babies.add(customerSnapshot.getValue(Baby.class));
                        }
                        check = true;
                    }

                    customer.setCus_name(cus_name);
                    customer.setCus_birthday(cus_birthday);
                    customer.setCus_address(cus_address);
                    customer.setCus_phone(cus_phone);
                    customer.setCus_email(cus_email);
                    customer.setCus_gender(cus_gender);
                    customer.setCus_ethnicity(cus_ethnicity);
                    customer.setCus_avatar(cus_avatar);

                } catch (Exception e) {
                    Log.i("GENSHINNNNNNNNN", "" + e);
                    return;
                }
                schedule_tv_cus_name.setText(customer.getCus_name());
                schedule_tv_cus_email.setText(customer.getCus_email());
                schedule_tv_cus_phone.setText(customer.getCus_phone());
                if (check) {
                    schedule_bady_name.setText(babies.get(0).getBaby_name());
                    schedule_baby_gender.setText(babies.get(0).getBaby_gender());
                    schedule_bady_birthday.setText(babies.get(0).getBaby_birthday());
                    schedule_baby_congenital_disease.setText(babies.get(0).getBaby_congenital_disease());
                }


                int number_of_babies = babies.size();
                if (number_of_babies > 0) {
                    for (int i = 0; i < number_of_babies; i++) {
                        Baby baby = babies.get(i);
                        Button button = new Button(Schedule_an_injection.this);
                        button.setText(babies.get(i).getBaby_name());

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                schedule_bady_name.setText(baby.getBaby_name());
                                schedule_baby_gender.setText(baby.getBaby_gender());
                                schedule_bady_birthday.setText(baby.getBaby_birthday());
                                schedule_baby_congenital_disease.setText(baby.getBaby_congenital_disease());
                            }
                        });

                        schedule_list_btn_babies.addView(button);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}