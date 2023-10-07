package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class GetStartedActivity extends AppCompatActivity {

    private int page = 1;
    private Button buttonPrev, buttonNext;
    private LinearLayout nextContainer, prevContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        nextContainer = findViewById(R.id.nextContainer);
        prevContainer = findViewById(R.id.prevContainer);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrev);

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

    public void setPage1(){
        prevContainer.setVisibility(View.INVISIBLE);
        nextContainer.setVisibility(View.VISIBLE);
        page = 1;
        changeFragment("step1");
    }

    public void setCompleted(){
        prevContainer.setVisibility(View.INVISIBLE);
        nextContainer.setVisibility(View.INVISIBLE);
        changeFragment("completed");
    }

    public void changeFragment(String fragmentName) {
        Log.i("GetStarted activity", "changeFragment: " + fragmentName);
        switch (fragmentName){
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
}