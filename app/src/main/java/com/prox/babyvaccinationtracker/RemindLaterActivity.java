package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.model.NotificationMessage;
import com.prox.babyvaccinationtracker.service.NotificationService;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RemindLaterActivity extends AppCompatActivity {

    private EditText editTextDate, editTextTime;

    private Calendar selectedDate = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    NotificationMessage message = new NotificationMessage();
    private Button buttonSendNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_later);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        buttonSendNotification = findViewById(R.id.buttonSendNotification);

        Intent intent = getIntent();
        message = (NotificationMessage) intent.getSerializableExtra("notification");
        Log.i("RemindLaterActivity", "onCreate: " + message.toString());

        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
        editTextTime.setFocusable(false);
        editTextTime.setClickable(true);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        buttonSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDate.getText().toString().isEmpty() || editTextTime.getText().toString().isEmpty()) {
                    Toast.makeText(RemindLaterActivity.this, "Vui lòng chọn ngày và giờ", Toast.LENGTH_LONG).show();
                } else {

                    message.setMessage("Nhắc lại: " + message.getMessage());
                    String date = editTextDate.getText().toString();
                    String time = editTextTime.getText().toString();
                    String dateTime = date + " " + time;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    try {
                        message.setDate(sdf.parse(dateTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("RemindLaterPrefs", MODE_PRIVATE);
                    String notificationsJson = sharedPreferences.getString("notifications", null);
                    List<NotificationMessage> notificationList;
                    if (notificationsJson != null) {
                        // Có dữ liệu trong SharedPreferences, giải mã danh sách từ JSON
                        Type type = new TypeToken<List<NotificationMessage>>() {}.getType();
                        notificationList = new Gson().fromJson(notificationsJson, type);
                    } else {
                        // Không có dữ liệu, tạo danh sách mới
                        notificationList = new ArrayList<>();
                    }
                    notificationList.add(message);
                    String updatedNotificationsJson = new Gson().toJson(notificationList);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("notifications", updatedNotificationsJson);
                    editor.apply();
                    message.setNotification_id(null);
                    message.setType(null);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(message);
                    Toast.makeText(RemindLaterActivity.this, "Đã đặt lịch nhắc nhở", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Log.i("Selected Date", "onDateSet: " + selectedDate.getTime().toString());
                String selectedDateStr = dateFormat.format(selectedDate.getTime());
                editTextDate.setText(dateFormat.format(selectedDate.getTime()));
            }
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);
                Calendar currentTime = Calendar.getInstance();
                if(selectedDate.getTime().after(currentTime.getTime())){
                    editTextTime.setText(timeFormat.format(selectedTime.getTime()));
                } else {
                    if (selectedTime.getTime().before(currentTime.getTime())) {
                        Toast.makeText(RemindLaterActivity.this, "Giờ không được bé hơn giờ hiện tại", Toast.LENGTH_LONG).show();
                    } else {
                        editTextTime.setText(timeFormat.format(selectedTime.getTime()));
                    }
                }
            }
        }, selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
}