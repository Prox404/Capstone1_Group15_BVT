package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    Context context;
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotPasswordTextView;

    public LoginFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container != null ? container.getContext() : null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        loginButton = view.findViewById(R.id.buttonLogin);
        registerTextView = view.findViewById(R.id.textViewRegister);
        forgotPasswordTextView = view.findViewById(R.id.textViewForgotPassword);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity) context).changeFragment("register");
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity) context).changeFragment("forgotPassword");
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = mAuth.getCurrentUser().getUid();
                            Log.i("LOGINGGGGGGG", id);
                            DatabaseReference checkblacklist = FirebaseDatabase.getInstance().getReference("BlackList").child(id);
                            checkblacklist.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        // Đăng nhập thành công, xử lý tại đây
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Ví dụ: Hiển thị thông báo hoặc chuyển đến màn hình chính
                                        Log.i("Login", "onComplete: " + user.getEmail() + " - " + user.getUid());
                                        //load fragment home
//                            ((AuthActivity) context).loadFragment(HomeFragment.newInstance());
//                            ((AuthActivity) context).changeFragment("home");
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(user.getUid());
                                        // get user and set to global variable SharedPreferences
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Dữ liệu tồn tại, giải phân tích dữ liệu và tạo đối tượng Customer
                                                    Customer customer = new Customer();
                                                    customer.setCustomer_id(user.getUid());
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
                                                        baby.setBaby_id(babySnapshot.getKey());
                                                        if (baby != null) {
                                                            babyList.add(baby);
                                                        }
                                                    }
                                                    customer.setBabies(babyList);
                                                    if (customer != null) {

                                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("customer_id", customer.getCustomer_id());
                                                        editor.putString("cus_name", customer.getCus_name());
                                                        editor.putString("cus_birthday", customer.getCus_birthday());
                                                        editor.putString("cus_address", customer.getCus_address());
                                                        editor.putString("cus_phone", customer.getCus_phone());
                                                        editor.putString("cus_email", customer.getCus_email());
                                                        editor.putString("cus_gender", customer.getCus_gender());
                                                        editor.putString("cus_gender", customer.getCus_gender());
                                                        editor.putString("cus_ethnicity", customer.getCus_ethnicity());
                                                        editor.putString("cus_avatar", customer.getCus_avatar());
                                                        Gson gson = new Gson();
                                                        String babiesJson = gson.toJson(customer.getBabies());
                                                        Log.i("babiesJson", "onDataChange: " + babiesJson);
                                                        editor.putString("babiesList", babiesJson);
                                                        editor.apply();
//                                            Log.i("sharedPreferences", "onComplete: " + sharedPreferences.getString("babiesList", "null"));
//                                            // load fragment home
                                                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                        if (babyList == null || babyList.isEmpty()){
                                                            ((AuthActivity) context).changeFragment("getStarted");
                                                        }else {
                                                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Không tìm thấy dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    // Dữ liệu không tồn tại
                                                    Toast.makeText(context, "Không tìm thấy dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Đã xảy ra lỗi, không thể lấy dữ liệu
                                                Toast.makeText(context, "Đã xảy ra lỗi, không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(context, "Tài khoản này đã bị chặn",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        } else {
                            // Đăng nhập thất bại, xử lý tại đây
                            String errorCode = task.getException().getMessage();
                            Log.i("Login", "Fail: " + errorCode);
                            Toast.makeText(context, "Đăng nhập thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}