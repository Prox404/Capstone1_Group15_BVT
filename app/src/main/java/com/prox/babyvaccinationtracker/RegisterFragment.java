package com.prox.babyvaccinationtracker;

import static android.app.Activity.RESULT_OK;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.widget.Toolbar;

import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import kotlin.text.Regex;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    Toolbar onBackToolbar;
    Context context;

    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewAvatar;
    private EditText editTextName, editTextEmail, editTextPassword, editTextRePassword, editTextPhone, editTextBirthday;
    private Button buttonRegister;
    private DatePickerDialog datePickerDialog;

    private Bitmap selectedImage;
    private CustomerRegistration customerRegistration;

    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;

    ArrayList<String> provinceList = new ArrayList<>();
    ArrayList<String> districtList = new ArrayList<>();
    ArrayList<String> wardList = new ArrayList<>();

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    ArrayList<String> ethnicityList = new ArrayList<>();

    JSONArray provincesArray;
    JSONArray ethnicityArray;

    String gender = "Nam";

    String jsonAddressDataString = "";
    String jsonEthnicityDataString = "";

    Spinner spinnerProvince;
    Spinner spinnerDistrict;
    Spinner spinnerWard;
    Spinner spinnerEthnicity;
    
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.context = container != null ? container.getContext() : null;
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextRePassword = view.findViewById(R.id.editTextRePassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioButtonMale = view.findViewById(R.id.radioButtonMale);
        radioButtonFemale = view.findViewById(R.id.radioButtonFemale);
        onBackToolbar = (Toolbar) view.findViewById(R.id.onBackToolbar);

        customerRegistration = new CustomerRegistration();
        editTextBirthday = view.findViewById(R.id.editTextBirthday);

        onBackToolbar.setTitle("");

        ((AppCompatActivity) requireActivity()).setSupportActionBar(onBackToolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        onBackToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }

        Calendar currentDate = Calendar.getInstance();
        // Khởi tạo DatePickerDialog
        datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextBirthday.setText(selectedDate);
                    }
                },
                currentDate.get(Calendar.YEAR), // Năm mặc định
                currentDate.get(Calendar.MONTH), // Tháng mặc định
                currentDate.get(Calendar.DAY_OF_MONTH) // Ngày mặc định
        );

        datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());

        // Ngăn việc nhập trực tiếp vào EditText
        editTextBirthday.setFocusable(false);
        editTextBirthday.setClickable(true);

        // Sét sự kiện khi nhấn vào EditText
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Xử lý sự kiện khi người dùng chọn giới tính
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale){
                    gender = "Nam";
                    Log.i("AuthActivity", "onCheckedChanged: Male checker");
                }
                else if (checkedId == R.id.radioButtonFemale){
                    gender = "Nữ";
                    Log.i("AuthActivity", "onCheckedChanged: Female checker");
                }
            }
        });

        try {
            // Đọc dữ liệu JSON từ tệp (ví dụ: "data.json" trong thư mục assets)
            InputStream inputStream = context.getAssets().open("AddressData.json"); // Thay "data.json" bằng tên tệp của bạn
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            jsonAddressDataString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Đọc dữ liệu JSON từ tệp (ví dụ: "data.json" trong thư mục assets)
            InputStream inputStream = context.getAssets().open("ethnicityVN.json"); // Thay "data.json" bằng tên tệp của bạn
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            jsonEthnicityDataString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Đọc dữ liệu từ file JSON của bạn và lưu vào một JSONObject
//            Log.i("Auth", jsonAddressDataString);
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

        try {
            // Đọc dữ liệu từ file JSON của bạn và lưu vào một JSONObject
            JSONObject jsonData = new JSONObject(jsonEthnicityDataString);

            // Lấy danh sách tỉnh/thành phố và thêm vào danh sách tỉnh/thành phố
            ethnicityArray = jsonData.getJSONArray("Ethnicity");
            for (int i = 0; i < ethnicityArray.length(); i++) {
                JSONObject ethnicityObject = ethnicityArray.getJSONObject(i);
                String ethnicityName = ethnicityObject.getString("name");
                ethnicityList.add(ethnicityName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, provinceList);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, districtList);
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, wardList);
        ArrayAdapter<String> ethnicityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, ethnicityList);

        spinnerProvince = view.findViewById(R.id.spinnerProvince);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerWard = view.findViewById(R.id.spinnerWard);
        spinnerEthnicity = view.findViewById(R.id.spinnerEthnicity);

        spinnerProvince.setAdapter(provinceAdapter);
        spinnerEthnicity.setAdapter(ethnicityAdapter);

        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWard.setAdapter(wardAdapter);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tỉnh được chọn
                String selectedProvince = (String) parent.getItemAtPosition(position);

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

                // Cập nhật dữ liệu trong Spinner huyện
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, selectedDistricts);
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý ở đây
            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy quận/huyện được chọn
                String selectedDistrict = (String) parent.getItemAtPosition(position);

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
                ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, selectedWards);
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerWard.setAdapter(wardAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý ở đây
            }
        });

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
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
                imageViewAvatar.setImageBitmap(selectedImage);
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
        String birthday = editTextPhone.getText().toString().trim();
        String province = spinnerProvince.getSelectedItem().toString();
        String district = spinnerDistrict.getSelectedItem().toString();
        String ward = spinnerWard.getSelectedItem().toString();
        String ethnicity = spinnerEthnicity.getSelectedItem().toString();
        String address = ward + ", " + district + ", " + province;

        if (name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                rePassword.isEmpty() ||
                phone.isEmpty() ||
                birthday.isEmpty() ||
                province.isEmpty() ||
                district.isEmpty() ||
                ward.isEmpty() ||
                ethnicity.isEmpty() ||
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

        Customer customer = new Customer();
        customer.setCus_name(name);
        customer.setCus_email(email);
        customer.setCus_phone(phone);
        customer.setCus_password(password);
        customer.setCus_birthday(birthday);
        customer.setCus_address(address);
        customer.setCus_ethnicity(ethnicity);
        customer.setCus_gender(gender);
        customer.setCus_avatar("https://res.cloudinary.com/daahr9bmg/image/upload/v1696458517/sh3mokiznenwv6eggiqb.png");
        customer.setBabies(new ArrayList<Baby>());

        // Đăng ký người dùng và lưu thông tin
        customerRegistration.registerCustomer(context, customer, filePath);
    }
}