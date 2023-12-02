package com.prox.babyvaccinationtracker;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.internal.TextWatcherAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

public class GetStartedStep1Fragment extends Fragment {

    private TextView editTextBirthday, editTextHeight, editTextWeight, editTextName, editTextSleep;
    private DatePickerDialog datePickerDialog;
    private Bitmap selectedImage;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    String gender = "Nam";
    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewAvatar;

    private TextView textViewMessage;

    Context context;

    public GetStartedStep1Fragment() {
        // Required empty public constructor
    }

    public static GetStartedStep1Fragment newInstance(String param1, String param2) {
        GetStartedStep1Fragment fragment = new GetStartedStep1Fragment();
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
        this.context = container != null ? container.getContext() : null;

        View view = inflater.inflate(R.layout.fragment_get_started_step1, container, false);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        editTextName = view.findViewById(R.id.editTextName);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioButtonMale = view.findViewById(R.id.radioButtonMale);
        radioButtonFemale = view.findViewById(R.id.radioButtonFemale);
        editTextBirthday = view.findViewById(R.id.editTextBirthday);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        editTextSleep = view.findViewById(R.id.editTextSleep);

        textViewMessage.setVisibility(View.INVISIBLE);
        if (GetStartedActivity.baby.getBaby_name() != null) {
            editTextName.setText(GetStartedActivity.baby.getBaby_name());
        }
        if (GetStartedActivity.baby.getBaby_birthday() != null) {
            editTextBirthday.setText(GetStartedActivity.baby.getBaby_birthday());
        }
        if (GetStartedActivity.health.getHeight() != 0) {
            editTextHeight.setText(String.valueOf(GetStartedActivity.health.getHeight()));
        }
        if (GetStartedActivity.health.getWeight() != 0) {
            editTextWeight.setText(String.valueOf(GetStartedActivity.health.getWeight()));
        }
        if (GetStartedActivity.filePath != null && !GetStartedActivity.filePath.isEmpty()) {
            Log.i("GetStarted", "onCreateView: " + GetStartedActivity.filePath);
            File imgFile = new File(GetStartedActivity.filePath);
            if (imgFile.exists()) {
                Log.i("GetStarted", "onCreateView: " + "File exists");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewAvatar.setImageBitmap(myBitmap);
            }
        }
        if (GetStartedActivity.baby.getBaby_gender() != null){
            Log.i("GetStarted", "onCreateView: Gender Call");
            if (GetStartedActivity.baby.getBaby_gender().equals("Nam"))
                radioButtonMale.setChecked(true);
            else radioButtonFemale.setChecked(true);
        }
        if (GetStartedActivity.health.getSleep() != 0){
            editTextSleep.setText(String.valueOf(GetStartedActivity.health.getSleep()));
        }

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    gender = "Nam";
                    GetStartedActivity.baby.setBaby_gender("Nam");
                    Log.i("AuthActivity", "onCheckedChanged: Male checker");
                } else if (checkedId == R.id.radioButtonFemale) {
                    gender = "Nữ";
                    GetStartedActivity.baby.setBaby_gender("Nữ");
                    Log.i("AuthActivity", "onCheckedChanged: Female checker");
                }
            }
        });

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
                        textViewMessage.setVisibility(View.INVISIBLE);
                        GetStartedActivity.baby.setBaby_birthday(selectedDate);
                        Log.i("GetStarted", "Birthday: " + selectedDate);
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

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
                if (imageViewAvatar.getDrawable() == null) {
                    textViewMessage.setVisibility(View.VISIBLE);
                    textViewMessage.setText("Vui lòng chọn ảnh đại diện cho bé 🥲");
                } else {
                    textViewMessage.setVisibility(View.INVISIBLE);
                }
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = "Chưa có tên";
                if (s.toString() != null && !s.toString().isEmpty()) {
                    name = s.toString();
                } else {
                    editTextName.setError("Vui lòng nhập tên của bé");
                }
                Log.i("GetStarted", "afterTextChanged: " + name);
                GetStartedActivity.baby.setBaby_name(name);
            }
        });

        editTextHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double height = Double.valueOf(0);
                if (
                        s.toString() != null &&
                                !s.toString().isEmpty() &&
                                !s.toString().equals(".") &&
                                !s.toString().equals("-") &&
                                !s.toString().equals("0")
                ) {
                    if (Double.parseDouble(s.toString()) > 200) {
                        editTextHeight.setError("Chiều cao của bé không hợp lý 🥲");
                        return;
                    }else{
                        editTextHeight.setError(null);
                    }
                    height = Double.parseDouble(s.toString());
                    Log.i("GetStarted", "afterTextChanged: " + String.valueOf(height));
                } else {
                    editTextHeight.setError("Vui lòng nhập chiều cao của bé 🥲");
                }
                GetStartedActivity.health.setHeight(height);
            }
        });

        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double weight = Double.valueOf(0);
                if (
                        s.toString() != null &&
                                !s.toString().isEmpty() &&
                                !s.toString().equals(".")&&
                                !s.toString().equals("-") &&
                                !s.toString().equals("0")
                ) {
                    if (Double.parseDouble(s.toString()) > 150) {
                        editTextWeight.setError("Cân nặng của bé không hợp lý 🥲");
                        return;
                    }else{
                        editTextWeight.setError(null);
                    }
                    weight = Double.parseDouble(s.toString());
                    Log.i("GetStarted", "afterTextChanged: " + String.valueOf(weight));
                } else {
                    editTextWeight.setError("Vui lòng nhập cân nặng của bé 🥲");
                }
                GetStartedActivity.health.setWeight(weight);
            }
        });

        editTextSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double sleep = Double.valueOf(0);
                if (
                        s.toString() != null &&
                                !s.toString().isEmpty() &&
                                !s.toString().equals(".") &&
                                !s.toString().equals("-") &&
                                !s.toString().equals("0")
                ) {
                    if (Double.parseDouble(s.toString()) > 24) {
                        editTextSleep.setError("Số giờ bé ngủ mỗi ngày không quá 24h 🥲");
                        return;
                    }else{
                        editTextSleep.setError(null);
                    }
                    sleep = Double.parseDouble(s.toString());
                    Log.i("GetStarted", "afterTextChanged: " + String.valueOf(sleep));
                } else {
                    editTextSleep.setError("Vui lòng nhập số giờ bé ngủ mỗi ngày 🥲");
                }
                GetStartedActivity.health.setSleep(sleep);
            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                String filePath = getRealPathFromUri(data.getData(), getActivity());
                Log.i("Siuuu", "onActivityResult: " + filePath);
                GetStartedActivity.filePath = filePath;
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
}