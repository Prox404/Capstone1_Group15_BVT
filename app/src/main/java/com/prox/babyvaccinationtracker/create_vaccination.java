package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class create_vaccination extends AppCompatActivity {

    EditText edt_price,edt_date_of_entry,edt_unit,edt_dosage,edt_vaccine_name, edt_vac_effectiveness, edt_post_vaccination_reactions,edt_origin,edt_vaccination_target_group,edt_contraindications,edt_quantity;
    Button btn_tt;
    DatePickerDialog datePickerDialog;
    ImageButton img_button;

    String image_url = "https://res.cloudinary.com/du42cexqi/image/upload/v1696504103/nt4cybkx1k25elc2jrng.jpg";
    boolean is_input(String a){
        if(a.length() == 0){
            Toast.makeText(this, "Phải nhập thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // thêm ảnh
    Map config = new HashMap();
    String filepath;
    private void configCloudinary() {
        config.put("cloud_name", "du42cexqi");
        config.put("api_key", "346965553513552");
        config.put("api_secret", "SguEwSEbwQNgOgHRTkyxeuG-478");
        MediaManager.init(this, config);
    }

    private static final int PERMISSION_CODE = 1;
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int PICK_IMAGE = 1;
    public void accessTheGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get the image’s file location
        filepath = getRealPathFromUri(data.getData(), this);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                //set picked image to the mProfile
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        data.getData());
                img_button.setImageBitmap(bitmap);
            } catch (IOException e) {
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
    private void uploadToCloudinary(String filePath) {
        Log.d("A", "sign up uploadToCloudinary- ");

        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.i("upload image", "onStart: ");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.i("upload image", "Uploading... ");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.i("upload image", "image URL: "+resultData.get("url").toString());
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.i("upload image", "error "+ error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("upload image", "Reshedule "+error.getDescription());
            }
        }).dispatch();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vaccination);

        edt_vaccine_name = findViewById(R.id.vaccine_name);
        edt_vac_effectiveness = findViewById(R.id.vac_effectiveness);
        edt_post_vaccination_reactions = findViewById(R.id.post_vaccination_reactions);
        edt_origin = findViewById(R.id.origin);
        edt_vaccination_target_group = findViewById(R.id.vaccination_target_group);
        edt_contraindications = findViewById(R.id.contraindications);
        edt_quantity = findViewById(R.id.quantity);
        edt_dosage = findViewById(R.id.dosage);
        edt_unit = findViewById(R.id.unit);
        edt_date_of_entry = findViewById(R.id.date_of_entry);
        edt_price = findViewById(R.id.price);

        btn_tt = findViewById(R.id.btn_tt);

        configCloudinary();

        Calendar currentDate = Calendar.getInstance();
        // Khởi tạo DatePickerDialog
        datePickerDialog = new DatePickerDialog(
                create_vaccination.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edt_date_of_entry.setText(selectedDate);
                    }
                },
                currentDate.get(Calendar.YEAR), // Năm mặc định
                currentDate.get(Calendar.MONTH), // Tháng mặc định
                currentDate.get(Calendar.DAY_OF_MONTH) // Ngày mặc định
        );
        datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());

        // Ngăn việc nhập trực tiếp vào EditText
        edt_date_of_entry.setFocusable(false);
        edt_date_of_entry.setClickable(true);

        edt_date_of_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        img_button = findViewById(R.id.img_button);

        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccine_name = edt_vaccine_name.getText().toString();
                if(is_input(vaccine_name) == false){
                    edt_vaccine_name.requestFocus();
                    return;
                }
                String vac_effectiveness = edt_vac_effectiveness.getText().toString();
                if(is_input(vac_effectiveness) == false){
                    edt_vac_effectiveness.requestFocus();
                    return;
                }
                String post_vaccination_reactions = edt_post_vaccination_reactions.getText().toString();
                if(is_input(post_vaccination_reactions) == false){
                    edt_post_vaccination_reactions.requestFocus();
                    return;
                }
                String origin = edt_origin.getText().toString();
                if(is_input(origin) == false){
                    edt_origin.requestFocus();
                    return;
                }
                int vaccination_target_group = 0;
                try{
                    vaccination_target_group = Integer.parseInt(edt_vaccination_target_group.getText().toString());
                }catch (Exception e){
                    edt_vaccination_target_group.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                String contraindications = edt_contraindications.getText().toString();
                if(is_input(contraindications)==false){
                    edt_contraindications.requestFocus();
                    return;
                }

                int quantity = 0;
                try {
                    quantity= Integer.parseInt(edt_quantity.getText().toString());
                }catch (Exception e){
                    edt_quantity.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                int dosage = 0;
                try{
                    dosage = Integer.parseInt(edt_dosage.getText().toString());
                }catch (Exception e){
                    edt_dosage.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unit = edt_unit.getText().toString();
                if(is_input(unit) == false){
                    edt_unit.requestFocus();
                    return;
                }
                String date_of_entry = edt_date_of_entry.getText().toString();
                if(date_of_entry.length() == 0){
                    edt_date_of_entry.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải chọn ngày nhập cảnh ", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = 0;
                try{
                    price = Double.parseDouble(edt_price.getText().toString());
                }catch (Exception e){
                    edt_price.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }
                Vaccines vaccines ;

                //uploadToCloudinary(filepath);
                Log.d("A", "sign up uploadToCloudinary- ");

                MediaManager.get().upload(filepath).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.i("upload image", "onStart: ");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.i("upload image", "Uploading... ");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = resultData.get("url").toString();
                        Log.i("upload image", "image URL: "+ url);
                        image_url = url;
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.i("upload image", "error "+ error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.i("upload image", "Reshedule "+error.getDescription());
                    }
                }).dispatch();

                vaccines = new  Vaccines(
                        vaccine_name,
                        vac_effectiveness,
                        post_vaccination_reactions,
                        origin,
                        vaccination_target_group,
                        contraindications,
                        quantity,
                        dosage,
                        unit,
                        date_of_entry,
                        price,
                        image_url
                );

                vaccines.pushDataFisebase();

            }
        });

    }
}