package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.Manifest;

import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.BabyCheckList;
import com.prox.babyvaccinationtracker.model.Health;

import java.util.ArrayList;

public class GetStartedActivity extends AppCompatActivity {

    private int page = 1;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Button buttonPrev, buttonNext;
    private LinearLayout nextContainer, prevContainer;

    public static Baby baby = new Baby();

    public static ArrayList<String> checkList = new ArrayList<>();
    public static BabyCheckList babyCheckList = new BabyCheckList();

    public static Health health = new Health();

    public static String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        nextContainer = findViewById(R.id.nextContainer);
        prevContainer = findViewById(R.id.prevContainer);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrev);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClick();
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrevClick();
            }
        });

        setPage1();
    }

    private void onNextClick() {
        if (page < 2) {
            page++;
            updateButtonsVisibility();
            changeFragment("step" + page);
        } else if (page == 2) {
            // Đã đến bước cuối, thực hiện các xử lý cần thiết ở đây
            // Ví dụ: Hoàn thành quy trình
            String message = "";
            if (
                    baby.getBaby_name() == null || baby.getBaby_name().isEmpty() || baby.getBaby_name().equals("Chưa có tên")
            ) {
                message = "Hãy nhập tên cho bé !";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (
                    baby.getBaby_birthday() == null || baby.getBaby_birthday().isEmpty() || baby.getBaby_birthday().equals("Chưa có ngày sinh")
            ) {
                message = "Hãy nhập ngày sinh cho bé !";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (
                    health.getHeight() == 0
            ) {
                message = "Hãy nhập chiều cao cho bé !";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (
                    health.getWeight() == 0
            ) {
                message = "Hãy nhập cân nặng cho bé !";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (
                    health.getSleep() == 0
            ) {
                message = "Hãy nhập thời gian ngủ cho bé !";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }


            setCompleted();
        }
    }

    private void onPrevClick() {
        if (page > 1) {
            page--;
            updateButtonsVisibility();
            changeFragment("step" + page);
        }
    }

    private void updateButtonsVisibility() {
        if (page == 1) {
            prevContainer.setVisibility(View.INVISIBLE);
        } else if (page == 2) {
            nextContainer.setVisibility(View.VISIBLE);
            prevContainer.setVisibility(View.VISIBLE);
        } else {
            prevContainer.setVisibility(View.VISIBLE);
            nextContainer.setVisibility(View.VISIBLE);
        }
    }

    public void loadFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }

    public void setPage1() {
        prevContainer.setVisibility(View.INVISIBLE);
        nextContainer.setVisibility(View.VISIBLE);
        page = 1;
        changeFragment("step1");
    }

    public void setCompleted() {
        prevContainer.setVisibility(View.INVISIBLE);
        nextContainer.setVisibility(View.INVISIBLE);
        changeFragment("completed");
    }

    public void changeFragment(String fragmentName) {
        Log.i("GetStarted activity", "changeFragment: " + fragmentName);
        switch (fragmentName) {
            case "step1":
                loadFragment(new GetStartedStep1Fragment());
                break;
            case "step2":
                loadFragment(new GetStartedStep2Fragment());
                break;
            case "completed":
                loadFragment(new GetStartedCompleteFragment());
                break;
            default:
                loadFragment(new GetStartedStep1Fragment());
                break;
        }
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment instanceof OnBackPressedListener) {
            if (!((OnBackPressedListener) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}