package com.admin.babyvaccinationtracker;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.admin.babyvaccinationtracker.model.Baby;
import com.admin.babyvaccinationtracker.model.Customer;

import java.util.ArrayList;

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
                            // Đăng nhập thành công, xử lý tại đây
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Ví dụ: Hiển thị thông báo hoặc chuyển đến màn hình chính
                            Log.i("Login", "onComplete: " + user.getEmail() + " - " + user.getUid());
                            //load fragment home
//                            ((AuthActivity) context).loadFragment(HomeFragment.newInstance());
//                            ((AuthActivity) context).changeFragment("home");
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(user.getUid());
                            // get user and set to global variable SharedPreferences
                            databaseReference.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Customer customer = task1.getResult().getValue(Customer.class);
                                    ArrayList<Baby> babiesList;
                                    try {
                                        babiesList = customer.getBabies();
                                    } catch (Exception e) {
                                        babiesList = new ArrayList<>();
                                    }
                                    customer.setCustomer_id(user.getUid());
                                    Log.i("Login", "onComplete: " + customer.getCus_email() + " - " + customer.getCus_password());
                                    // save user to device
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
                                    //get babies
                                    Gson gson = new Gson();
                                    String babiesJson = gson.toJson(babiesList);
                                    editor.putString("babiesList", babiesJson);
                                    editor.apply();
                                    Log.i("sharedPreferences", "onComplete: " + sharedPreferences.getString("babiesList", "null"));
                                    // load fragment home
//                                    ((AuthActivity) context).changeFragment("home");
                                } else {
                                    Log.i("Login", "onComplete: " + task1.getException().getMessage());
                                }
                            });
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
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