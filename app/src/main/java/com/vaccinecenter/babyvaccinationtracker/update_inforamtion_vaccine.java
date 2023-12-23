package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.security.ConfirmationCallback;
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


public class update_inforamtion_vaccine extends AppCompatActivity {
    EditText edt_vaccine_name, edt_post_vaccination_reactions, edt_origin, edt_vaccination_target_group, edt_contraindications, edt_quantity, edt_dosage, edt_unit, edt_date_of_entry, edt_price;
    Button btn_delete, btn_add_new_image;

    Spinner spinner_update_money_unit, spinner_vac_effectiveness;

    ImageView btn_update_new_information, image_back; // nút quay lại

    Vaccines vaccine; // thông tin vắc-xin

    RecyclerView recyclerview_image; // hiển thị ảnh
    RecyclerAdapter recyclerAdapter; // đổ ảnh
    ArrayList<Uri> uri_image = new ArrayList<>(); // lưu trữ tất cả các ảnh từ mới đến cũ
    DatePickerDialog datePickerDialog; // cập nhập ngày
    ArrayList<String> old_image = new ArrayList<>(); // lưu trữ những ảnh cũ của vaccine
    ArrayList<Uri> new_image = new ArrayList<>(); // chứa ảnh mới

    int bounary_image_old_new = 0; // xác định ảnh cũ ảnh mới

    FirebaseDatabase database;

    DatabaseReference reference;

    String id_vaccine_center = "";
    String vaccine_id = "";

    String effectiveness = "";

    String[] arrayVaccineTypeEN = new String[18];

    String selectedValue = "";
    DataValidate validate = new DataValidate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inforamtion_vaccine);

        edt_vaccine_name = findViewById(R.id.update_vaccine_name);
        spinner_vac_effectiveness = findViewById(R.id.spinner_vac_effectiveness);
        edt_post_vaccination_reactions = findViewById(R.id.update_post_vaccination_reactions);
        edt_origin = findViewById(R.id.update_orgin);
        edt_vaccination_target_group = findViewById(R.id.update_vaccination_target_group);
        edt_contraindications = findViewById(R.id.update_contraindications);
        edt_quantity = findViewById(R.id.update_quantity);
        edt_dosage = findViewById(R.id.update_dosage);
        edt_unit = findViewById(R.id.update_unit);
        edt_date_of_entry = findViewById(R.id.update_date_of_entry);
        edt_price = findViewById(R.id.update_price);
        spinner_update_money_unit = findViewById(R.id.spinner_update_money_unit);

        btn_delete = findViewById(R.id.btn_delete);
        btn_update_new_information = findViewById(R.id.btn_update_new_information);
        btn_add_new_image = findViewById(R.id.btn_add_new_image);

        // nút quay lại
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // todo chọn loại vắc-xin
        arrayVaccineTypeEN = getResources().getStringArray(R.array.array_vaccine_type_EN);
        ArrayAdapter<CharSequence> adapter_vaccine_type = ArrayAdapter.createFromResource(
                update_inforamtion_vaccine.this,
                R.array.array_vaccine_type_VN,
                android.R.layout.simple_spinner_item);

        adapter_vaccine_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vac_effectiveness.setAdapter(adapter_vaccine_type);
        spinner_vac_effectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                effectiveness = arrayVaccineTypeEN[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Tiền tề
        ArrayAdapter<CharSequence> adapter_currency = ArrayAdapter.createFromResource(
                update_inforamtion_vaccine.this,
                R.array.array_currency,
                android.R.layout.simple_spinner_item);

        adapter_currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_update_money_unit.setAdapter(adapter_currency);
        spinner_update_money_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedValue = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // chọn ngày
        edt_date_of_entry.setFocusable(false);
        edt_date_of_entry.setClickable(true);
        Calendar currentDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                update_inforamtion_vaccine.this,
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

        // Đổ thông tin của vắc-xin vào
        recyclerview_image = findViewById(R.id.update_recyclerview_image);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vaccine = (Vaccines) bundle.getSerializable("vaccine_name");
            if (vaccine != null) {
                vaccine_id = vaccine.getVaccine_id();
                vaccine.setVaccine_id(null);
                edt_vaccine_name.setText(vaccine.getVaccine_name());

                spinner_vac_effectiveness.setSelection(
                        ((ArrayAdapter<String>) spinner_vac_effectiveness.getAdapter())
                                .getPosition(vaccine.getVac_effectiveness()));

                edt_post_vaccination_reactions.setText(vaccine.getPost_vaccination_reactions());
                edt_origin.setText(vaccine.getOrigin());
                edt_vaccination_target_group.setText(vaccine.getVaccination_target_group());
                edt_contraindications.setText(vaccine.getContraindications());
                edt_quantity.setText(vaccine.getQuantity());
                edt_dosage.setText(vaccine.getDosage());
                edt_unit.setText(vaccine.getUnit());
                edt_date_of_entry.setText(vaccine.getDate_of_entry());

                String[] price_unit = vaccine.getPrice().toString().split(" ");
                edt_price.setText(price_unit[0]);
                spinner_update_money_unit.setSelection(
                        ((ArrayAdapter<String>) spinner_update_money_unit.getAdapter()).getPosition(price_unit[1])
                );


                old_image = vaccine.getVaccine_image();
                bounary_image_old_new = old_image.size();
                for (String a : old_image) {
                    Uri uri = Uri.parse(a);
                    uri_image.add(uri);
                }

                recyclerAdapter = new RecyclerAdapter(uri_image, update_inforamtion_vaccine.this);
                recyclerview_image.setLayoutManager(new GridLayoutManager(update_inforamtion_vaccine.this, 3));
                recyclerview_image.setAdapter(recyclerAdapter);
            } else {
                Toast.makeText(update_inforamtion_vaccine.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(update_inforamtion_vaccine.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
        }
        // xóa những ảnh đã chọn
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Uri imageUri) {
                int position = uri_image.indexOf(imageUri);
                if (position != -1) {
                    if (position < bounary_image_old_new) {
                        uri_image.remove(position);
                        old_image.remove(position);
                        bounary_image_old_new--;
                        Log.i("Bounary", "" + bounary_image_old_new);
                        recyclerAdapter.notifyDataSetChanged();
                    } else {
                        uri_image.remove(position);
                        new_image.remove(position);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        btn_add_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

        // Những bước cơ bản đễ update thông tin vắc-xin trung tâm
        Context mcontext = update_inforamtion_vaccine.this;
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("user", Context.MODE_PRIVATE);

        id_vaccine_center = sharedPreferences.getString("center_id", "");
        database = FirebaseDatabase.getInstance();

        // button delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog("Xác nhận xóa", "Bạn có chắc muốn xóa vắc-xin này không?",
                        new ConfirmationCallback() {
                    @Override
                    public void onConfirm(boolean result) {
                        if (result) {
                            boolean check = vaccine.isDeleted();
                            reference = database.getReference("users")
                                    .child("Vaccine_center")
                                    .child(id_vaccine_center).child("vaccines");
                            if (check == true) {
                                vaccine.setDeleted(true);
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Vắc-xin này đã xóa",
                                        Toast.LENGTH_SHORT).show();
                            } else if (check == false) {
                                check = true;
                                reference.child(vaccine_id).child("deleted")
                                        .setValue(check).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data was successfully pushed
                                            // You can perform any actions here, such as displaying a message
                                            vaccine.setDeleted(true);
                                            Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            vaccine.setVaccine_id(vaccine_id); // đặt lại trường vaccine ID
                                            bundle.putSerializable("vaccine_name", vaccine);
                                            intent.putExtras(bundle);
                                            setResult(2, intent);
                                            finish();
                                        } else {
                                            // Data push failed
                                            // Handle the error here
                                            Toast.makeText(update_inforamtion_vaccine.this,
                                                    "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }
        });

        // cập thông tin vắc-xin khi sẵn sàng
        btn_update_new_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog("Xác nhận cập nhật",
                        "Bạn có chắc muốn cập nhật thông tin vắc-xin không?",
                        new ConfirmationCallback() {
                    @Override
                    public void onConfirm(boolean result) {
                        if (result) {
                            String name = edt_vaccine_name.getText().toString();
                            if(name.isEmpty()){
                                edt_vaccine_name.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Vui lòng nhập tên vắc-xin",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!validate.IsValidNameVN(name.trim())){
                                edt_vaccine_name.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Tên vắc-xin không hợp lệ",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String reactions = edt_post_vaccination_reactions.getText().toString().trim();
                            if(reactions.isEmpty()){
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập phản ứng sau tiêm, nếu không có thì ghi không có",
                                        Toast.LENGTH_SHORT).show();
                                edt_post_vaccination_reactions.requestFocus();
                                return;
                            }


                            String origin = edt_origin.getText().toString().trim();
                            if(origin.isEmpty()){
                                edt_origin.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập nguồn gốc vắn-xin", Toast.LENGTH_LONG).show();
                                return;
                            }else if(!validate.isValidNameOriginVN(origin)){
                                edt_origin.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập nguồn gốc vắn-xin không hợp lệ", Toast.LENGTH_LONG).show();
                                return;
                            }


                            String target_group = edt_vaccination_target_group.getText().toString();
                            if(target_group.isEmpty()){
                                edt_vaccination_target_group.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập nhóm tuổi sử dụng", Toast.LENGTH_LONG).show();
                                return;
                            }else if(target_group.trim().isEmpty()){
                                edt_vaccination_target_group.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập nhóm tuổi sử dụng không hợp lệ", Toast.LENGTH_LONG).show();
                                return;
                            }


                            String contraindication = edt_contraindications.getText().toString().trim();
                            if(contraindication.isEmpty()){
                                edt_contraindications.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Nhập chống chỉ định", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String quantity = edt_quantity.getText().toString();
                            if (quantity.isEmpty()) {
                                edt_quantity.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải nhập số lượng của vắc-xin", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String dosage = edt_dosage.getText().toString();
                            if (dosage.isEmpty()) {
                                edt_dosage.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải nhập liều của vắc-xin", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String unit = edt_unit.getText().toString().trim();
                            if (unit.isEmpty()) {
                                edt_unit.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải nhập đơn vị sử dụng của vắc-xin",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            String date_of_entry = edt_date_of_entry.getText().toString();
                            if (date_of_entry.isEmpty()) {
                                edt_date_of_entry.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải nhập ngày nhập cảnh của vắc-xin", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String price = edt_price.getText().toString();
                            if (price.isEmpty()) {
                                edt_price.requestFocus();
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải nhập giá của vắc-xin", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (uri_image.size() == 0) {
                                Toast.makeText(update_inforamtion_vaccine.this,
                                        "Phải chọn ảnh vắc-xin", Toast.LENGTH_LONG).show();
                                return;
                            }
                            ArrayList<String> Url_image = new ArrayList<>();
                            int size = new_image.size();
                            if (size != 0) {
                                for (int i = 0; i < size; i++) {
                                    Uri image = new_image.get(i);
                                    MediaManager.get().upload("" + image).callback(new UploadCallback() {
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
                                            Log.i("upload image onSuccess", "image URL: " + url);
                                            Url_image.add(url);
                                            if (Url_image.size() == size) {
                                                ArrayList<String> vaccine_image;
                                                if (old_image.size() != 0) {
                                                    vaccine_image = merge_two_array(old_image, Url_image);
                                                } else {
                                                    vaccine_image = Url_image;
                                                }
                                                UpdateInformationVaccine(name,
                                                        effectiveness,
                                                        reactions,
                                                        origin,
                                                        target_group,
                                                        contraindication,
                                                        quantity,
                                                        dosage,
                                                        unit,
                                                        date_of_entry,
                                                        price,
                                                        vaccine_image);
                                            }

                                        }

                                        @Override
                                        public void onError(String requestId, ErrorInfo error) {
                                            Log.i("upload image onError", "error " + error.getDescription());
                                        }

                                        @Override
                                        public void onReschedule(String requestId, ErrorInfo error) {
                                            Log.i("upload image onReschedule", "Reshedule " + error.getDescription());
                                        }
                                    }).dispatch();
                                }

                            } else {
                                UpdateInformationVaccine(name,
                                        effectiveness,
                                        reactions,
                                        origin,
                                        target_group,
                                        contraindication,
                                        quantity,
                                        dosage,
                                        unit,
                                        date_of_entry,
                                        price,
                                        old_image);
                            }
                        }
                    }
                });
            }
        });
    }

    private ArrayList<String> merge_two_array(ArrayList<String> image_old, ArrayList<String> image_new) {
        int size_old = image_old.size();
        int size_new = image_new.size();
        ArrayList<String> merge_image = new ArrayList<>();
        for (int i = 0; i < size_old; i++) {
            merge_image.add(image_old.get(i));
        }
        for (int i = 0; i < size_new; i++) {
            merge_image.add(image_new.get(i));
        }
        return merge_image;
    }

    private void showConfirmDialog(String title, String message, final ConfirmationCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng đồng ý
                        callback.onConfirm(true);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng từ chối
                        callback.onConfirm(false);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface ConfirmationCallback {
        void onConfirm(boolean result);
    }


    private void UpdateInformationVaccine(String vaccine_name,
                                          String vac_effectiveness,
                                          String post_vaccination_reactions,
                                          String origin,
                                          String vaccination_target_group,
                                          String contraindications,
                                          String quantity,
                                          String dosage,
                                          String unit,
                                          String date_of_entry,
                                          String price,
                                          ArrayList<String> vaccine_image) {
        vaccine.setVaccine_name(vaccine_name);
        vaccine.setVac_effectiveness(vac_effectiveness);
        vaccine.setPost_vaccination_reactions(post_vaccination_reactions);
        vaccine.setOrigin(origin);
        vaccine.setVaccination_target_group(vaccination_target_group);
        vaccine.setContraindications(contraindications);
        vaccine.setQuantity(quantity);
        vaccine.setDosage(dosage);
        vaccine.setUnit(unit);
        vaccine.setDate_of_entry(date_of_entry);
        String vaccine_price = price + " " + selectedValue;
        vaccine.setPrice(vaccine_price);
        vaccine.setVaccine_image(vaccine_image);
        reference = database.getReference("users").child("Vaccine_center")
                .child(id_vaccine_center).child("vaccines").child(vaccine_id);
        reference.setValue(vaccine);
        // todo gửi thông tin đã chỉnh sửa về lại trang vaccine
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        vaccine.setVaccine_id(vaccine_id); // đặt lại trường vaccine ID
        bundle.putSerializable("vaccine_name", vaccine);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    // Kiểm tra quyền truy cập kho ảnh
    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
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
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String filePath = getRealPathFromUri(selectedImageUri, this);
                if (!uri_image.contains(Uri.parse(filePath))) {
                    uri_image.add(Uri.parse(filePath));
                    new_image.add(Uri.parse(filePath));
                } else {
                    Toast.makeText(this, "Ảnh đã được chọn trước đó", Toast.LENGTH_SHORT).show();
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    // chuyển đường dẫn thành Uri
    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri,
                null, null, null, null);
        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}