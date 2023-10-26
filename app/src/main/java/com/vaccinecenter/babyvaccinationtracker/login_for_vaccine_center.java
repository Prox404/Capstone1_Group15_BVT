package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccine_center;


public class login_for_vaccine_center extends AppCompatActivity {
    EditText login_for_edt_email_vaccine_center,login_for_edt_password_vaccine_center;
    Button login_btn_login_for_vaccine_center;

    TextView tv_registration,login_textview_reset_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_vaccine_center);
        // check người dùng đã lưu trước đó
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");
        if(!id_vaccine_center.isEmpty()){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        login_for_edt_password_vaccine_center = findViewById(R.id.edt_login_password);
        login_for_edt_email_vaccine_center = findViewById(R.id.edt_login_email);
        login_btn_login_for_vaccine_center = findViewById(R.id.login_btn_login_for_vaccine_center);
        login_textview_reset_password = findViewById(R.id.login_textview_reset_password);
        login_textview_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_for_vaccine_center.this, reset_password.class);
                startActivity(intent);
            }
        });
        tv_registration = findViewById(R.id.tv_registration);

        tv_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login_for_vaccine_center.this, Register_for_vaccine_center.class);
                startActivityForResult(i,1);
            }
        });

        login_btn_login_for_vaccine_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = login_for_edt_email_vaccine_center.getText().toString();
                String password = login_for_edt_password_vaccine_center.getText().toString();
                if(email.length() == 0||password.length()==0){
                    Toast.makeText(login_for_vaccine_center.this,"Phải nhập tài khoản đăng nhập", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("Login", "onComplete: " + user.getEmail() + " - " + user.getUid());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center").child(user.getUid());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Context context = login_for_vaccine_center.this;
                                    if (dataSnapshot.exists()) {
                                        // Dữ liệu tồn tại, giải phân tích dữ liệu và tạo đối tượng Customer
                                        Vaccine_center center = new Vaccine_center();
                                        center.setCenter_id(user.getUid());
                                        center.setCenter_name(dataSnapshot.child("center_name").getValue().toString());
                                        center.setCenter_address(dataSnapshot.child("center_address").getValue().toString());
                                        center.setActivity_certificate(dataSnapshot.child("activity_certificate").getValue().toString());
                                        center.setCenter_email(dataSnapshot.child("center_email").getValue().toString());
                                        center.setCenter_image(dataSnapshot.child("center_image").getValue().toString());
                                        center.setHotline(dataSnapshot.child("hotline").getValue().toString());
                                        center.setWork_time(dataSnapshot.child("work_time").getValue().toString());

                                        if (center != null) {

                                            SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("center_id", center.getCenter_id());
                                            editor.putString("center_address", center.getCenter_address());
                                            editor.putString("activity_certificate", center.getActivity_certificate());
                                            editor.putString("center_email", center.getCenter_email());
                                            editor.putString("center_image", center.getCenter_image());
                                            editor.putString("hotline", center.getHotline());
                                            editor.putString("work_time", center.getWork_time());

                                            editor.apply();
//
                                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, HomeActivity.class);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(context, "Không tìm thấy dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Không tìm thấy dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            String errorCode = task.getException().getMessage();
                            Log.i("Login", "Fail: " + errorCode);
                            Toast.makeText(login_for_vaccine_center.this, "Đăng nhập thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Toast.makeText(login_for_vaccine_center.this,"Đăng ký thành công, vui lòng chờ xác nhận!",Toast.LENGTH_LONG).show();
        }
    }
}