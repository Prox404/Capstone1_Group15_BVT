package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vaccinecenter.babyvaccinationtracker.Adapter.RecyclerAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;
import java.util.Map;


public class create_vaccination extends AppCompatActivity {

    EditText edt_price,edt_date_of_entry,edt_unit,edt_dosage,edt_vaccine_name, edt_post_vaccination_reactions,edt_origin,edt_vaccination_target_group,edt_contraindications,edt_quantity;
    Button btn_tt;
    DatePickerDialog datePickerDialog;
    Button img_button;
    ImageView image_back;

    Spinner spinner, spinner_vaccine_type;

    String select_price_unti = ""; //
    String image_url = "https://res.cloudinary.com/du42cexqi/image/upload/v1696504103/nt4cybkx1k25elc2jrng.jpg"; // ảnh mặc định
    RecyclerView recyclerView; // nơi hiện ảnh đã chọn
    RecyclerAdapter recyclerAdapter; // Hiển thị những ảnh đã chọn
    ArrayList<Uri> uri = new ArrayList<>(); // những đường dẫn ảnh đã lưu
    ArrayList<String> Image_url;
    // Kiểm tra người dùng đã nhập vắc-xin vào chưa
    boolean is_input(String a){
        if(a.length() == 0){
            Toast.makeText(this, "Phải nhập thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    String[] arrayVaccineTypeEN = new String[18]; // loại vắc-xin - EN
    String vaccine_type = ""; // Chọn loại vắc-xin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vaccination);
        Log.i("CREATE","funtion onCreate");
        // ánh xạ
        edt_vaccine_name = findViewById(R.id.vaccine_name);
        edt_post_vaccination_reactions = findViewById(R.id.post_vaccination_reactions);
        edt_origin = findViewById(R.id.origin);
        edt_vaccination_target_group = findViewById(R.id.vaccination_target_group);
        edt_contraindications = findViewById(R.id.contraindications);
        edt_quantity = findViewById(R.id.quantity);
        edt_dosage = findViewById(R.id.dosage);
        edt_unit = findViewById(R.id.unit);
        edt_date_of_entry = findViewById(R.id.date_of_entry);
        edt_price = findViewById(R.id.price);
        image_back = findViewById(R.id.image_back);
        btn_tt = findViewById(R.id.btn_tt);

        // nút quay lại
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // chọn loại vắc-xin
        arrayVaccineTypeEN = getResources().getStringArray(R.array.array_vaccine_type_EN);
        spinner_vaccine_type = findViewById(R.id.vaccine_type);
        ArrayAdapter<CharSequence> adapter_vaccine_type = ArrayAdapter.createFromResource(create_vaccination.this, R.array.array_vaccine_type_VN, android.R.layout.simple_spinner_item);
        adapter_vaccine_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vaccine_type.setAdapter(adapter_vaccine_type);
        vaccine_type = arrayVaccineTypeEN[0];
        spinner_vaccine_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vaccine_type = arrayVaccineTypeEN[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Chọn tiền tệ
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter_currency = ArrayAdapter.createFromResource(create_vaccination.this, R.array.array_currency, android.R.layout.simple_spinner_item);
        adapter_currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_currency);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_price_unti = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                select_price_unti = "VND";
            }
        });



        recyclerView = findViewById(R.id.Recycler_view);
        recyclerAdapter = new RecyclerAdapter(uri,create_vaccination.this);
        recyclerView.setLayoutManager(new GridLayoutManager(create_vaccination.this, 3));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Uri imageUri) {
                int position = uri.indexOf(imageUri);
                if (position != -1) {
                    uri.remove(position);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        // chọn ngày nhập cảnh
        Calendar currentDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                create_vaccination.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edt_date_of_entry.setText(selectedDate);
                    }
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());
        edt_date_of_entry.setFocusable(false);
        edt_date_of_entry.setClickable(true);
        edt_date_of_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        // chọn ảnh
        img_button = findViewById(R.id.img_button);
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        // thêm vắc-xin
        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri.size() == 0){
                    Toast.makeText(create_vaccination.this, "Phải chọn ảnh", Toast.LENGTH_SHORT).show();

                }
                else{
                    String vaccine_name = edt_vaccine_name.getText().toString();
                    if(is_input(vaccine_name) == false){
                        edt_vaccine_name.requestFocus();
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
                    String vaccination_target_group = edt_vaccination_target_group.getText().toString();
                    if(is_input(vaccination_target_group) == false){
                        edt_vaccination_target_group.requestFocus();
                        return;
                    }
                    String contraindications = edt_contraindications.getText().toString();
                    if(is_input(contraindications)==false){
                        edt_contraindications.requestFocus();
                        return;
                    }
                    int quantity_int = 0;
                    try {
                        quantity_int= Integer.parseInt(edt_quantity.getText().toString());
                        if(quantity_int < 0){
                            edt_quantity.requestFocus();
                            Toast.makeText(create_vaccination.this, "Phải nhập số lớn hơn hoặc bằng 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        edt_quantity.requestFocus();
                        Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String quantity = edt_quantity.getText().toString();
                    int dosage_int = 0;
                    try{
                        dosage_int = Integer.parseInt(edt_dosage.getText().toString());
                        if(dosage_int < 0){
                            edt_dosage.requestFocus();
                            Toast.makeText(create_vaccination.this, "Phải nhập số lớn hơn hoặc bằng 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        edt_dosage.requestFocus();
                        Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String dosage = edt_dosage.getText().toString();
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

                    int price = 0;
                    try{
                        price = Integer.parseInt(edt_price.getText().toString());
                        if(price < 0){
                            Toast.makeText(create_vaccination.this, "Phải nhập số lớn hơn hoặc bằng 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        edt_price.requestFocus();
                        Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String price_vaccine = "" + edt_price.getText().toString() +" "+ select_price_unti;

                    Image_url = new ArrayList<>();
                    int total_image = uri.size();
                    for (int i = 0; i < total_image ; i ++){
                        Log.d("A", "sign up uploadToCloudinary- ");
                        Uri image = uri.get(i);
                        Log.i("IMAGEAAAA",total_image+" "+image);
                        MediaManager.get().upload(""+(image)).callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                                Log.i("upload image", "onStart: ");
                            }
                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                                Log.i("uploading image", "Uploading... ");
                            }
                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                String url = resultData.get("url").toString();
                                Log.i("upload image onSuccess", "image URL: "+ url);
                                Image_url.add(url);
                                if (Image_url.size() == total_image) {
                                    // All images are uploaded, proceed with saving data to Firebase.
                                    saveDataToFirebase(vaccine_name,
                                            post_vaccination_reactions,
                                            origin,
                                            vaccination_target_group,
                                            contraindications,
                                            quantity,
                                            dosage,
                                            unit,
                                            date_of_entry,
                                            price_vaccine
                                            );
                                }
                            }
                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                Log.i("upload image onError", "error "+ error.getDescription());
                            }
                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                                Log.i("upload image onReschedule", "Reshedule "+error.getDescription());
                            }
                        }).dispatch();
                    }
                }
                clear();
            }

        });

    }
    // làm sạch tất cả các thông tin đã nhập
    public void clear(){
        edt_vaccine_name.setText("");
        edt_post_vaccination_reactions.setText("");
        edt_origin.setText("");
        edt_vaccination_target_group.setText("");
        edt_contraindications.setText("");
        edt_dosage.setText("");
        edt_quantity.setText("");
        edt_unit.setText("");
        edt_date_of_entry.setText("");
        edt_price.setText("");
        uri.clear();
        recyclerAdapter.notifyDataSetChanged();
    }
    // push thông tin lên firebase
    public void saveDataToFirebase(String vaccine_name,
                                   String post_vaccination_reactions,
                                   String origin,
                                   String vaccination_target_group,
                                   String contraindications,
                                   String quantity,
                                   String dosage,
                                   String unit,
                                   String date_of_entry,
                                   String select_price_unti
                                   ){
        Vaccines vaccines;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vaccineRef = database.getReference("users");
        vaccines = new Vaccines();
        vaccines.setVaccine_name(vaccine_name);
        vaccines.setVac_effectiveness(vaccine_type);
        vaccines.setPost_vaccination_reactions(post_vaccination_reactions);
        vaccines.setOrigin(origin);
        vaccines.setVaccination_target_group(vaccination_target_group);
        vaccines.setContraindications(contraindications);
        vaccines.setQuantity(quantity);
        vaccines.setDosage(dosage);
        vaccines.setUnit(unit);
        vaccines.setDate_of_entry(date_of_entry);
        vaccines.setPrice(select_price_unti);
        if(Image_url.size() == 0){
            Image_url.add(image_url);
            vaccines.setVaccine_image(Image_url);
        }
        else{
            vaccines.setVaccine_image(Image_url);
        }
        vaccines.setDeleted(false);

        Context mcontext = create_vaccination.this;
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");
        vaccineRef.child("Vaccine_center").child(id_vaccine_center).child("vaccines").push().setValue(vaccines).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(create_vaccination.this, "successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(create_vaccination.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Kiểm tra quyền truy cập kho ảnh
    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;
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
   // Kho ảnh người dùng
    public void accessTheGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }
    // xử lý sự kiện chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get the image’s file location
//        filepath = getRealPathFromUri(data.getData(), this);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
             if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String filePath = getRealPathFromUri(selectedImageUri, this);
                if(!uri.contains(Uri.parse(filePath))){
                    uri.add(Uri.parse(filePath));
                }else{
                    Toast.makeText(this, "Ảnh đã được chọn trước đó", Toast.LENGTH_SHORT).show();
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }
    // chuyển đường dẫn thành Uri
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
}