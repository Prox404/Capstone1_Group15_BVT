package com.prox.babyvaccinationtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.prox.babyvaccinationtracker.model.Customer;
import com.prox.babyvaccinationtracker.model.NotificationMessage;
import com.prox.babyvaccinationtracker.model.Regimen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStartedCompleteFragment extends Fragment implements GetStartedActivity.OnBackPressedListener {

    Button buttonCompleted;

    DatabaseReference databaseReference;

    private boolean isCompletedButtonClicked = false;

    public GetStartedCompleteFragment() {
        // Required empty public constructor
    }

    public static GetStartedCompleteFragment newInstance() {
        GetStartedCompleteFragment fragment = new GetStartedCompleteFragment();
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
        View view =  inflater.inflate(R.layout.fragment_get_started_complete, container, false);
        buttonCompleted = view.findViewById(R.id.buttonCompleted);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        String userID = sharedPreferences.getString("customer_id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(userID);
        buttonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference babiesReference = databaseReference.child("babies");
                DatabaseReference newBabyReference = babiesReference.push();
                newBabyReference.setValue(GetStartedActivity.baby).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String babyID = newBabyReference.getKey();
                        uploadAvatar(GetStartedActivity.filePath, babyID);
//                        GetStartedActivity.health.setBaby_id(babyID);

                        DatabaseReference checkList = FirebaseDatabase.getInstance().getReference("check_list");
                        checkList.child(babyID).setValue(GetStartedActivity.babyCheckList);

                        DatabaseReference healthReference = FirebaseDatabase.getInstance().getReference("health");
                        healthReference.child(babyID).setValue(GetStartedActivity.health);

                        DatabaseReference vaccinationRegimenReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen").child(babyID);
                        List<Regimen> regimens = null;
                        try {
                            regimens = VaccineRegimen.getVaccinationRegimen(GetStartedActivity.baby.getBaby_birthday());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        vaccinationRegimenReference.setValue(regimens);

                        DatabaseReference notifications = FirebaseDatabase.getInstance().getReference("notifications");
                        List<NotificationMessage>  messages = null;
                        try {
                            messages = VaccineNotificationMessage.getVaccinationNotificationMessage(GetStartedActivity.baby.getBaby_birthday(), userID, babyID);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        for (NotificationMessage message : messages) {
                            notifications.push().setValue(message);
                        }

                        Customer customer = new Customer();
                        customer.uploadUserData(getActivity(), userID);

                        isCompletedButtonClicked = true;

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);

                        Log.i("Completed", "onClick: " + babyID);
                    }
                });
            }
        });

        return view;
    }

    private void uploadAvatar(String filePath, String uid) {
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
                // save image url to firebase
                String avatarUrl = resultData.get("url").toString();
                databaseReference.child("babies").child(uid).child("baby_avatar").setValue(avatarUrl);
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
    public boolean onBackPressed() {

        if (isCompletedButtonClicked) {
            // đã nhấn nút completed thì không cho back
            return false;
        }else {
            // chưa nhấn nút completed thì cho back
            return true;
        }

    }
}