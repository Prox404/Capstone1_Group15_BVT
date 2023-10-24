package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.admin.babyvaccinationtracker.model.Vaccine_center;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class VaccineCenterRegistration {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceCenter;
    private DatabaseReference databaseReferenceRegistration;

    public  VaccineCenterRegistration(){
        // Khởi tạo Firebase Auth và Realtime Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceCenter = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center");
        databaseReferenceRegistration = FirebaseDatabase.getInstance().getReference("Vaccine_center_registration");
    }
    public void registerCenter(Context context, Vaccine_center center,String registration_id){
        if(context == null){
            Log.i("Contentttttttt",""+context);
            return;
        }
        if(center == null){
            Log.i("Centerttttttt",""+center);
            return;
        }
        byte[] decenter_passord = Base64.getDecoder().decode(center.getCenter_password());
        String password = new String(decenter_passord, StandardCharsets.UTF_8);
        Log.i("Password Decoding",""+password);
        firebaseAuth.createUserWithEmailAndPassword(center.getCenter_email(),password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
                        center.setCenter_password(null);
                        databaseReferenceCenter.child(uid).setValue(center);
                        databaseReferenceRegistration.child(registration_id).child("status").setValue(1, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Log.e("FirebaseError", "Data could not be saved: " + databaseError.getMessage());
                            } else {
                                Log.d("FirebaseSuccess", "Data saved successfully.");
                            }
                        });
                        Toast.makeText(context, "Xác nhận thành công !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // Đã tồn tại người dùng với cùng email, xử lý tùy ý
                            Log.i("CenterRegistration", "Duplicated center email");
                            Toast.makeText(context, "Đã tồn tại người dùng với cùng email !", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Đăng ký thất bại, xử lý tùy ý
                            Log.i("CenterRegistration", "Failed Register Center");
                            Toast.makeText(context, "Xác nhận thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}
