package com.admin.babyvaccinationtracker;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.admin.babyvaccinationtracker.Adapter.TabPagerAdapter;
import com.admin.babyvaccinationtracker.model.NotificationMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SendNotificationActivity extends AppCompatActivity implements SearchQueryListener {
    @Override
    protected void onStop() {
        super.onStop();
        selectedCustomers.clear();
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static String searchQuery = "";

//    private ImageView imageView_back;

    private EditText editTextNotificationSearch, editTextDate, editTextTime, editTextTitle, editTextMessage;
    private Button buttonSearch, buttonSendNotification;

    private Calendar selectedDate = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static ArrayList<String> selectedCustomers = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
//        imageView_back = findViewById(R.id.imageView_back);
        editTextNotificationSearch = findViewById(R.id.editTextNotificationSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendNotification = findViewById(R.id.buttonSendNotification);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
        editTextTime.setFocusable(false);
        editTextTime.setClickable(true);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SendNotificationUserFragment(), "Người dùng");
        adapter.addFragment(new SendNotificationVaccineCenterFragment(), "Trung tâm tiêm chủng");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

//        imageView_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchQuery = editTextNotificationSearch.getText().toString();
                onSearchQueryChanged(searchQuery);
            }
        });

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
                String title = editTextTitle.getText().toString().trim();
                String message = editTextMessage.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();

                if (title.isEmpty()) {
                    editTextTitle.setError("Vui lòng nhập tiêu đề thông báo");
                    return;
                }

                if (message.isEmpty()) {
                    editTextMessage.setError("Vui lòng nhập nội dung thông báo");
                    return;
                }

                if (date.isEmpty()) {
                    editTextDate.setError("Vui lòng chọn ngày thông báo");
                    return;
                }

                if (time.isEmpty()) {
                    editTextTime.setError("Vui lòng chọn giờ thông báo");
                    return;
                }

                if (selectedCustomers.isEmpty() ) {
                    // Chưa chọn người dùng
                    // Hiển thị thông báo lỗi
                    Toast.makeText(SendNotificationActivity.this, "Vui lòng chọn người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference notifications = FirebaseDatabase.getInstance().getReference("notifications");
                // format dateTime to dd/MM/yyyy HH:mm
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date dateTime = null;
                try {
                    dateTime = sdf.parse(date + " " + time);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (dateTime.before(Calendar.getInstance().getTime())) {
                    // Ngày giờ đã chọn đã trôi qua
                    // Hiển thị thông báo lỗi
                    Toast.makeText(SendNotificationActivity.this, "Ngày giờ đã chọn đã trôi qua", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thực hiện gửi thông báo tại đây
                for (String customer_id : selectedCustomers) {
                    Log.i("SendNotification", "onClick: " + customer_id + " " + title + " " + message + " " + dateTime.toString());
                    NotificationMessage notificationMessage = new NotificationMessage(title,customer_id,message,dateTime );
                    notifications.push().setValue(notificationMessage);
                }

                Toast.makeText(SendNotificationActivity.this, "Đã gửi thông báo thành công", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onSearchQueryChanged(String searchQuery) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());

        if (currentFragment != null) {
            if (currentFragment instanceof SendNotificationUserFragment) {
                SendNotificationUserFragment userFragment = (SendNotificationUserFragment) currentFragment;
                userFragment.updateFilter(searchQuery);
            } else if (currentFragment instanceof SendNotificationVaccineCenterFragment) {
                SendNotificationVaccineCenterFragment vaccineCenterFragment = (SendNotificationVaccineCenterFragment) currentFragment;
                vaccineCenterFragment.updateFilter(searchQuery);
            }
        }
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Để đảm bảo ngày không nhỏ hơn ngày hiện tại
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                Calendar currentTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                if (minute == currentTime.get(Calendar.MINUTE)){
                    selectedTime.set(Calendar.MINUTE, minute + 1);
                } else{
                    selectedTime.set(Calendar.MINUTE, minute);
                }
                if(selectedDate.getTime().after(currentTime.getTime())){
                    editTextTime.setText(timeFormat.format(selectedTime.getTime()));
                } else {
                    if (selectedTime.getTime().before(currentTime.getTime())) {
                    // Giờ đã chọn nhỏ hơn giờ hiện tại
                        Toast.makeText(SendNotificationActivity.this, "Thời gian này đã qua !", Toast.LENGTH_LONG).show();
                    } else {
                        editTextTime.setText(timeFormat.format(selectedTime.getTime()));
                    }
                }
            }
        }, selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }


}
