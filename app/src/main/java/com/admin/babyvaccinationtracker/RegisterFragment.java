package com.admin.babyvaccinationtracker;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.admin.babyvaccinationtracker.model.Admin;

import java.io.InputStream;

import kotlin.text.Regex;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    Context context;

    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewBack, imageViewAdminAvt;
    private EditText editTextName, editTextEmail, editTextPassword, editTextRePassword, editTextPhone;
    private Button buttonRegister;
    private DatePickerDialog datePickerDialog;
    private Bitmap selectedImage;
    private AdminRegistration adminRegistration;

    String filePath = "";


    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.context = container != null ? container.getContext() : null;
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        imageViewBack = view.findViewById(R.id.imageViewBack);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextRePassword = view.findViewById(R.id.editTextRePassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        imageViewAdminAvt = view.findViewById(R.id.imageViewAdminAvt);

        adminRegistration = new AdminRegistration();


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCustomer();
            }
        });

        imageViewAdminAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = getRealPathFromUri(data.getData(), getActivity());
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(inputStream);
                imageViewAdminAvt.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);
        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void registerCustomer() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editTextRePassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                rePassword.isEmpty() ||
                phone.isEmpty() ||
                selectedImage == null) {
            Toast.makeText(context, "Hãy nhập đầy đủ tất cả các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        Regex passwordRegex = new Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (!passwordRegex.matches(password)){
            Toast.makeText(context, "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(rePassword)){
            Toast.makeText(context, "Mật khẩu nhập lại không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        Admin admin = new Admin();
        admin.setAdmin_name(name);
        admin.setAdmin_email(email);
        admin.setAdmin_phone(phone);
        admin.setAdmin_avatar("https://res.cloudinary.com/daahr9bmg/image/upload/v1696458517/sh3mokiznenwv6eggiqb.png");
        admin.setAdmin_password(password);
        // Đăng ký người dùng và lưu thông tin
        adminRegistration.registerAdmin(context, admin, filePath);
    }
}