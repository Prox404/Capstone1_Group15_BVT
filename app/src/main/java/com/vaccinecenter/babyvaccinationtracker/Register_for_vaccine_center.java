package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccine_center;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccine_center_registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import kotlin.text.Regex;

public class Register_for_vaccine_center extends AppCompatActivity {

    EditText register_edt_center_name,register_edt_hotline,register_edt_center_email,register_edt_center_password,register_edt_center_re_password;
    Button register_btn_register;
    ImageButton register_img_btn_activity_certificate,register_img_btn_center_image;

    RadioGroup register_work_time;
    RadioButton register_radio_all_day,register_radio_time;

    LinearLayout register_selected_hour_minute;

    TextView tv_center_image,tv_center_chungchi;

    ImageView image_back_register_account_center;



    TextInputEditText register_tv_time_begin,register_tv_time_end;

    Spinner Register_spn_provinces,Register_spn_districts,Register_spn_wards;
    String filePath_center_image = "";
    String filePath_center_certificate = "";

    String jsonAddressDataString = "";
    JSONArray provincesArray;
    ArrayList<String> provinceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_vaccine_center);
        //imagview
        image_back_register_account_center = findViewById(R.id.image_back_register_account_center);
        image_back_register_account_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // textview
        tv_center_image = findViewById(R.id.tv_center_image);
        tv_center_chungchi = findViewById(R.id.tv_center_chungchi);
        // TextInputEditText
        register_tv_time_begin = findViewById(R.id.register_tv_time_begin);
        register_tv_time_end = findViewById(R.id.register_tv_time_end);
        // edit text
        register_edt_center_name = findViewById(R.id.register_edt_center_name); // tên
        register_edt_hotline = findViewById(R.id.register_edt_hotline); // điện thoại
        register_edt_center_email = findViewById(R.id.register_edt_center_email);
        register_edt_center_password = findViewById(R.id.register_edt_center_password);
        register_edt_center_re_password = findViewById(R.id.register_edt_center_re_password);
        // button
        register_btn_register = findViewById(R.id.register_btn_register); // nút đăng ký
        // Spinner
        Register_spn_provinces = findViewById(R.id.Register_spn_provinces); // tỉnh
        Register_spn_districts = findViewById(R.id.Register_spn_districts); // quận
        Register_spn_wards = findViewById(R.id.Register_spn_wards); // phường
        // image button
        register_img_btn_activity_certificate = findViewById(R.id.register_img_btn_activity_certificate); // ảnh chứng nhận
        register_img_btn_center_image = findViewById(R.id.register_img_btn_center_image); // ảnh
        // Radio group
        register_work_time = findViewById(R.id.register_work_time);
        // Radio button
        register_radio_all_day = findViewById(R.id.register_radio_all_day);
        register_radio_time = findViewById(R.id.register_radio_time);
        // Linner layout
        register_selected_hour_minute = findViewById(R.id.register_selected_hour_minute);

        // todo chọn ảnh
        register_img_btn_activity_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(PICK_IMAGE_CERTIFICATE);
            }
        });
        register_img_btn_center_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(PICK_IMAGE_CENTER);
            }
        });
        // todo chọn h làm việc
        register_radio_all_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_selected_hour_minute.setVisibility(View.GONE);
            }
        });
        register_radio_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_selected_hour_minute.setVisibility(View.VISIBLE);
            }
        });
        // todo chọn h làm việc
        register_tv_time_begin.setFocusable(false);
        register_tv_time_begin.setClickable(true);
        register_tv_time_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Register_for_vaccine_center.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        register_tv_time_begin.setText(view.getHour()+": "+view.getMinute());
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });
        register_tv_time_end.setFocusable(false);
        register_tv_time_end.setClickable(true);
        register_tv_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time_begin = register_tv_time_begin.getText().toString();
                if(time_begin.length()!=0){
                    String[] time = time_begin.split(": ");
                    int hour = Integer.parseInt(time[0]);
                    int minute = Integer.parseInt(time[1]);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Register_for_vaccine_center.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                            int hourSelected = view.getHour();
                            int minuteSelected = view.getMinute();
                            if (hourSelected==0&&minuteSelected==0){
                                register_tv_time_end.setText(hourSelected+": "+minuteSelected);
                                return;
                            }
                            if(hourSelected < hour ){
                                Toast.makeText(Register_for_vaccine_center.this,"Hãy nhập giờ chính xác", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else if(hourSelected == hour){
                                if(minuteSelected < minute ){
                                    Toast.makeText(Register_for_vaccine_center.this,"Hãy nhập phút chính xác", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            register_tv_time_end.setText(hourSelected+": "+minuteSelected);
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });

        // todo chọn địa chỉ
        try {
            // Đọc dữ liệu JSON từ tệp
            Context context = getApplicationContext();
            InputStream inputStream = context.getAssets().open("AddressData.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonAddressDataString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileReadError", "Error reading the JSON file from assets: " + e);
        }
        try {
            JSONObject jsonData = new JSONObject(jsonAddressDataString);

            // Lấy danh sách tỉnh/thành phố và thêm vào danh sách tỉnh/thành phố
            provincesArray = jsonData.getJSONArray("data");
            for (int i = 0; i < provincesArray.length(); i++) {
                JSONObject provinceObject = provincesArray.getJSONObject(i);
                String provinceName = provinceObject.getString("name");
                provinceList.add(provinceName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> provincesAdapter = new ArrayAdapter<>(Register_for_vaccine_center.this, android.R.layout.simple_spinner_item,provinceList);
        Register_spn_provinces.setAdapter(provincesAdapter);
        Register_spn_provinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Lấy tỉnh được chọn
                String selectedProvince = (String) adapterView.getItemAtPosition(position);
                // Tạo danh sách quận/huyện dựa trên tỉnh được chọn
                List<String> selectedDistricts = new ArrayList<>();
                for (int i = 0; i < provincesArray.length(); i++) {
                    JSONObject provinceObject = null;
                    try {
                        provinceObject = provincesArray.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String provinceName = null;
                    try {
                        provinceName = provinceObject.getString("name");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (provinceName.equals(selectedProvince)) {
                        JSONArray districtsArray = null;
                        try {
                            districtsArray = provinceObject.getJSONArray("districts");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int j = 0; j < districtsArray.length(); j++) {
                            JSONObject districtObject = null;
                            try {
                                districtObject = districtsArray.getJSONObject(j);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String districtName = null;
                            try {
                                districtName = districtObject.getString("name");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            selectedDistricts.add(districtName);
                        }
                        break;
                    }
                }

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(Register_for_vaccine_center.this, android.R.layout.simple_spinner_item, selectedDistricts);
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Register_spn_districts.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Register_spn_districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedDistrict = (String) adapterView.getItemAtPosition(position);

                // Tạo danh sách phường/xã dựa trên quận/huyện được chọn
                List<String> selectedWards = new ArrayList<>();
                for (int i = 0; i < provincesArray.length(); i++) {
                    JSONObject provinceObject = null;
                    try {
                        provinceObject = provincesArray.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONArray districtsArray = null;
                    try {
                        districtsArray = provinceObject.getJSONArray("districts");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    for (int j = 0; j < districtsArray.length(); j++) {
                        JSONObject districtObject = null;
                        try {
                            districtObject = districtsArray.getJSONObject(j);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String districtName = null;
                        try {
                            districtName = districtObject.getString("name");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if (districtName.equals(selectedDistrict)) {
                            JSONArray wardsArray = null;
                            try {
                                wardsArray = districtObject.getJSONArray("wards");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            for (int k = 0; k < wardsArray.length(); k++) {
                                JSONObject wardObject = null;
                                try {
                                    wardObject = wardsArray.getJSONObject(k);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String wardName = null;
                                try {
                                    wardName = wardObject.getString("name");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                selectedWards.add(wardName);
                            }
                            break;
                        }
                    }
                }

                // Cập nhật dữ liệu trong Spinner xã
                ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(Register_for_vaccine_center.this, android.R.layout.simple_spinner_item, selectedWards);
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Register_spn_wards.setAdapter(wardAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // todo đăng ký vaccine
        register_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tên trung tâm
                String center_name = register_edt_center_name.getText().toString();
                if (center_name.length()==0){
                    register_edt_center_name.requestFocus();
                    Toast.makeText(Register_for_vaccine_center.this,"Hãy nhập tên trung tâm vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }
                // Địa chỉ trung tâm
                String province = Register_spn_provinces.getSelectedItem().toString();
                String district = Register_spn_districts.getSelectedItem().toString();
                String ward = Register_spn_wards.getSelectedItem().toString();
                if (province.length() == 0 || district.length() == 0 || ward.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this,"Hãy chọn địa chỉ trung tâm vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }
                String center_address=ward+", "+district+", "+province;
                // Đường dây điện thoại Trung tâm
                String hotline = register_edt_hotline.getText().toString();
                if(hotline.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this,"Hãy nhập hotline trung tâm vắc-xin của bạn", Toast.LENGTH_LONG).show();
                    register_edt_hotline.requestFocus();
                    return;
                }
                if(hotline.length() !=  10){
                    Toast.makeText(Register_for_vaccine_center.this,"Hãy nhập đúng điện thoại", Toast.LENGTH_LONG).show();
                    register_edt_hotline.requestFocus();
                    return;
                }
                // Ngày làm việc trong tuần
                int index = register_work_time.getCheckedRadioButtonId();
                if(index==-1){
                    Toast.makeText(Register_for_vaccine_center.this, "Phải chọn ngày làm việc", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton rad = findViewById(index);
                String work_on_weekend = rad.getText().toString();
                // Thời gian làm việc trong ngày
                String work_hour = "";
                if(register_radio_all_day.isChecked()){
                    work_hour = register_radio_all_day.getText().toString();
                }
                else if (register_radio_time.isChecked()){
                    String time_begin = register_tv_time_begin.getText().toString();
                    String time_end = register_tv_time_end.getText().toString();
                    if(time_begin.length()==0||time_end.length()==0){
                        Toast.makeText(Register_for_vaccine_center.this, "Phải chọn giờ làm việc", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    work_hour = "Thời gian làm việc trong ngày bắt đầu từ "+time_begin+" đến "+time_end;
                }
                String work_time = work_on_weekend+", "+work_hour;
                // email center
                String center_email = register_edt_center_email.getText().toString();
                if(center_email.length() == 0){
                    register_edt_center_email.requestFocus();
                    Toast.makeText(Register_for_vaccine_center.this, "Phải nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                // password
                String center_password = register_edt_center_password.getText().toString();
                String center_repassword = register_edt_center_re_password.getText().toString();

                if(center_password.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this, "Phải nhập password", Toast.LENGTH_SHORT).show();
                    register_edt_center_password.requestFocus();
                    return;
                }
                if(center_repassword.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this, "Phải nhập lại password nhập lại", Toast.LENGTH_SHORT).show();
                    register_edt_center_re_password.requestFocus();
                    return;
                }
                if (!center_password.equals(center_repassword)){
                    Toast.makeText(Register_for_vaccine_center.this, "Hai mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
                Regex passwordRegex = new Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
                if (!passwordRegex.matches(center_password)){
                    Toast.makeText(Register_for_vaccine_center.this, "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.", Toast.LENGTH_LONG).show();
                    return;
                }
                // encode
                String encenter_password = Base64.getEncoder().encodeToString(center_password.getBytes(StandardCharsets.UTF_8));
                // decode
                byte[] decenter_passord = Base64.getDecoder().decode(encenter_password);
                String password = new String(decenter_passord, StandardCharsets.UTF_8);
                Log.i("Password Decoding",""+password);
                // hình ảnh
                // todo upload ảnh
                if(filePath_center_image.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this, "Phải chọn ảnh trung tâm", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(filePath_center_certificate.length()==0){
                    Toast.makeText(Register_for_vaccine_center.this, "Phải có bằng chứng chứng chỉ hoạt động", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> url_image = new ArrayList<>();
                ArrayList<String> path_image = new ArrayList<>();
                path_image.add(filePath_center_image);
                path_image.add(filePath_center_certificate);

                for(int i = 0 ;i < path_image.size() ; i++){
                    String path = path_image.get(i);
                    MediaManager.get().upload(path).callback(new UploadCallback() {
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
                            Log.i("upload image", "image URL: "+url);
                            url_image.add(url);
                            if(url_image.size() == path_image.size()){

                                register(center_name,
                                        center_address,
                                        hotline,work_time,
                                        center_email,
                                        encenter_password,
                                        url_image.get(0),
                                        url_image.get(1));
                            }
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




            }
        });

    }
    private void register(String center_name,
                          String center_address,
                          String hotline,
                          String work_time,
                          String center_email,
                          String encenter_password,
                          String centerImage,
                          String activityCertificate){
        // todo đăng ký
        Vaccine_center vaccineCenter = new Vaccine_center();
        vaccineCenter.setCenter_name(center_name);
        vaccineCenter.setCenter_address(center_address);
        vaccineCenter.setHotline(hotline);
        vaccineCenter.setWork_time(work_time);
        vaccineCenter.setCenter_email(center_email);
        vaccineCenter.setCenter_password(encenter_password);
        vaccineCenter.setActivity_certificate(activityCertificate);
        vaccineCenter.setCenter_image(centerImage);

        Vaccine_center_registration registration = new Vaccine_center_registration();
        registration.setCenter(vaccineCenter);
        registration.setStatus(0);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today = day+"/"+month+"/"+year;
        registration.setRegistration_date(today);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference datavaccineCenter = firebaseDatabase.getReference("Vaccine_center_registration");
        datavaccineCenter.push().setValue(registration);

    }

    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_CENTER = 1;
    private static final int PICK_IMAGE_CERTIFICATE = 2;

    private void requestPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission
                (this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery(requestCode);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery(requestCode);
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void accessTheGallery(int requestCode) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get the image’s file location
        if(resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_CENTER) {
                tv_center_image.setVisibility(View.GONE);
                filePath_center_image = getRealPathFromUri(data.getData(), Register_for_vaccine_center.this);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(),
                            data.getData());
                    register_img_btn_center_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == PICK_IMAGE_CERTIFICATE){
                tv_center_chungchi.setVisibility(View.GONE);
                filePath_center_certificate = getRealPathFromUri(data.getData(), Register_for_vaccine_center.this);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(),
                            data.getData());
                    register_img_btn_activity_certificate.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                String url = resultData.get("url").toString();
                Log.i("upload image", "image URL: "+url);
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
}