package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.BabyCheckList;
import com.prox.babyvaccinationtracker.model.Customer;
import com.prox.babyvaccinationtracker.model.Health;
import com.prox.babyvaccinationtracker.model.NotificationMessage;
import com.prox.babyvaccinationtracker.model.Regimen;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GetStartedCompleteFragment extends Fragment implements GetStartedActivity.OnBackPressedListener {

    Button buttonCompleted;
    Context context;

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
    // thêm hàm
    private int getMonthIndex(String monthAbbreviation) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthAbbreviation)) {
                return i;
            }
        }
        return -1; // Return -1 for an unknown month
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
        context = view.getContext();
        buttonCompleted = view.findViewById(R.id.buttonCompleted);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        String userID = sharedPreferences.getString("customer_id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(userID);

        Log.i("checkList", "onCreateView: " + GetStartedActivity.babyCheckList.toString());
        buttonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStartedActivity.loadingLayout.setVisibility(View.VISIBLE);
                DatabaseReference babiesReference = databaseReference.child("babies");
                DatabaseReference newBabyReference = babiesReference.push();

                newBabyReference.setValue(GetStartedActivity.baby).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String babyID = newBabyReference.getKey();

                        uploadAvatar(GetStartedActivity.filePath, babyID);
                        HashMap<String, String> qrContent = new HashMap<>();
                        qrContent.put("baby_id", babyID);
                        qrContent.put("cus_id", userID);
                        qrContent.put("type", "baby_information");
                        Gson gson = new Gson();
                        String qrContentString = gson.toJson(qrContent);
                        Bitmap bitmap = generateQRCode(qrContentString);
                        uploadQR(bitmap, userID, babyID);

                        GetStartedActivity.health.setBaby_id(babyID);

                        DatabaseReference checkList = FirebaseDatabase.getInstance().getReference("check_list");
                        checkList.child(babyID).setValue(GetStartedActivity.babyCheckList);

                        // Chỉnh sửa một tại chỗ này, mong được review
                        DatabaseReference healthReference = FirebaseDatabase.getInstance().getReference("health");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        Date now = new Date();
                        String formattedDate = dateFormat.format(now);
                        String[] sl = formattedDate.split(" ");
                        int monthIndex = getMonthIndex(sl[1]);
                        String referenceKey = sl[sl.length-1];
                        healthReference.child(babyID)
                                .child(referenceKey)
                                .child(""+monthIndex)
                                .setValue(GetStartedActivity.health);

                        DatabaseReference vaccinationRegimenReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen").child(babyID);
                        List<Regimen> regimens = null;
                        try {
                            regimens = VaccineRegimen.getVaccinationRegimen(GetStartedActivity.baby.getBaby_birthday(), GetStartedActivity.babyCheckList);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        vaccinationRegimenReference.setValue(regimens);

                        DatabaseReference notifications = FirebaseDatabase.getInstance().getReference("notifications");
                        List<NotificationMessage>  messages = null;
                        try {
                            messages = VaccineNotificationMessage.getVaccinationNotificationMessage(GetStartedActivity.baby.getBaby_birthday(), userID, babyID, GetStartedActivity.babyCheckList);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        for (NotificationMessage message : messages) {
                            notifications.push().setValue(message);
                        }



                        Customer customer = new Customer();
                        customer.uploadUserData(getActivity(), userID);

                        isCompletedButtonClicked = true;

                        Intent intent = new Intent();
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        GetStartedActivity.baby = new Baby();
                        GetStartedActivity.health = new Health();
                        GetStartedActivity.babyCheckList = new BabyCheckList();
                        GetStartedActivity.checkList = new ArrayList<>();
                        GetStartedActivity.filePath = "";
                        Toast.makeText(context, "Thêm bé thành công !", Toast.LENGTH_SHORT).show();
                        GetStartedActivity.loadingLayout.setVisibility(View.GONE);
                        String babies = sharedPreferences.getString("babiesList", null);
                        if (babies == null) {
                            Intent intent1 = new Intent(getActivity(), HomeActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                        }else{
                            getActivity().finish();
                        }

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

    private void uploadQR(Bitmap bitmap, String uid , String baby_id) {
        Uri uri = getImageUri(context, bitmap);
        MediaManager.get().upload(uri).callback(new UploadCallback() {
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
                Log.i("upload image", "image URL: " + resultData.get("url").toString());
                // save image url to firebase
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(uid).child("babies");
                databaseReference.child(baby_id).child("qr").setValue(resultData.get("url").toString());

            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.i("upload image", "error " + error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("upload image", "Reshedule " + error.getDescription());
            }
        }).dispatch();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.i("SomeImage", "getImageUri: " + path);
        return Uri.parse(path);
    }

    private Bitmap generateQRCode(String textToEncode) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? ContextCompat.getColor(context, R.color.black) : ContextCompat.getColor(context, R.color.white));
                }
            }

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}