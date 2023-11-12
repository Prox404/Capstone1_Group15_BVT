package com.admin.babyvaccinationtracker.model;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customer_id;
    private String cus_name;
    private String cus_birthday;
    private String cus_address;
    private String cus_phone;
    private String cus_email;
    private String cus_gender;
    private String cus_ethnicity;
    private String cus_password;
    private String cus_avatar;
    private List<Baby> babies = new ArrayList<>();

    private Boolean blocked;

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public List<Baby> getBabies() {
        return babies;
    }

    public void setBabies(List<Baby> babies) {
        this.babies = babies;
    }


    public Customer(String cus_name, String cus_birthday, String cus_address, String cus_phone, String cus_email, String cus_gender, String cus_ethnicity, String cus_password, String cus_avatar) {
        this.cus_name = cus_name;
        this.cus_birthday = cus_birthday;
        this.cus_address = cus_address;
        this.cus_phone = cus_phone;
        this.cus_email = cus_email;
        this.cus_gender = cus_gender;
        this.cus_ethnicity = cus_ethnicity;
        this.cus_password = cus_password;
        this.cus_avatar = cus_avatar;
    }

    public Customer() {
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_birthday() {
        return cus_birthday;
    }

    public void setCus_birthday(String cus_birthday) {
        this.cus_birthday = cus_birthday;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getCus_gender() {
        return cus_gender;
    }

    public void setCus_gender(String cus_gender) {
        this.cus_gender = cus_gender;
    }

    public String getCus_ethnicity() {
        return cus_ethnicity;
    }

    public void setCus_ethnicity(String cus_ethnicity) {
        this.cus_ethnicity = cus_ethnicity;
    }

    public String getCus_password() {
        return cus_password;
    }

    public void setCus_password(String cus_password) {
        this.cus_password = cus_password;
    }

    public String getCus_avatar() {
        return cus_avatar;
    }

    public void setCus_avatar(String cus_avatar) {
        this.cus_avatar = cus_avatar;
    }

    public void uploadUserData(Activity activity, String user_id){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user", MODE_PRIVATE);
        String customer_id = user_id;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(customer_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = new Customer();
                customer.setCustomer_id(customer_id);
                customer.setCus_avatar(dataSnapshot.child("cus_avatar").getValue().toString());
                customer.setCus_name(dataSnapshot.child("cus_name").getValue().toString());
                customer.setCus_birthday(dataSnapshot.child("cus_birthday").getValue().toString());
                customer.setCus_address(dataSnapshot.child("cus_address").getValue().toString());
                customer.setCus_phone(dataSnapshot.child("cus_phone").getValue().toString());
                customer.setCus_email(dataSnapshot.child("cus_email").getValue().toString());
                customer.setCus_gender(dataSnapshot.child("cus_gender").getValue().toString());
                customer.setCus_ethnicity(dataSnapshot.child("cus_ethnicity").getValue().toString());
                List<Baby> babyList = new ArrayList<>();
                DataSnapshot babiesSnapshot = dataSnapshot.child("babies");
                for (DataSnapshot babySnapshot : babiesSnapshot.getChildren()) {
                    Baby baby = babySnapshot.getValue(Baby.class);
                    if (baby != null) {
                        babyList.add(baby);
                    }
                }
                customer.setBabies(babyList);
                if (customer != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("customer_id", customer.getCustomer_id());
                    editor.putString("cus_name", customer.getCus_name());
                    editor.putString("cus_birthday", customer.getCus_birthday());
                    editor.putString("cus_address", customer.getCus_address());
                    editor.putString("cus_phone", customer.getCus_phone());
                    editor.putString("cus_email", customer.getCus_email());
                    editor.putString("cus_gender", customer.getCus_gender());
                    editor.putString("cus_gender", customer.getCus_gender());

                    Gson gson = new Gson();
                    String babiesJson = gson.toJson(customer.getBabies());
                    Log.i("babiesJson", "onDataChange: " + babiesJson);
                    editor.putString("babiesList", babiesJson);
                    editor.apply();
                } else {
                    Log.i("Update Customer", "onDataChange: null"  );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id='" + customer_id + '\'' +
                ", cus_name='" + cus_name + '\'' +
                ", cus_birthday='" + cus_birthday + '\'' +
                ", cus_address='" + cus_address + '\'' +
                ", cus_phone='" + cus_phone + '\'' +
                ", cus_email='" + cus_email + '\'' +
                ", cus_gender='" + cus_gender + '\'' +
                ", cus_ethnicity='" + cus_ethnicity + '\'' +
                ", cus_password='" + cus_password + '\'' +
                ", cus_avatar='" + cus_avatar + '\'' +
                '}';
    }
}
