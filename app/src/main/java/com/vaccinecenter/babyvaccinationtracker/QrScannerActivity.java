package com.vaccinecenter.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ZXingScannerView scannerView;
    private boolean isPopupShown = false;
    private boolean isProcessing = false;
    private boolean flash = false;
    private String scannedContent;
    ImageButton btnFlash;

    Button buttonResumeCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        scannerView = findViewById(R.id.zxingScannerView);;
        btnFlash = findViewById(R.id.btn_flash);
        buttonResumeCamera = findViewById(R.id.buttonResumeCamera);

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash = !flash;
                Log.i("flash", String.valueOf(flash));
                scannerView.setFlash(flash);
            }
        });

        buttonResumeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProcessing = false;
                scannerView.resumeCameraPreview(QrScannerActivity.this);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if (!isProcessing) {
            isProcessing = true;
            String scanResult = result.getText();
            scannedContent = scanResult;
            buttonResumeCamera.setVisibility(View.GONE);

            if (scanResult != null) {
                Gson gson = new Gson();
                HashMap<String, String> map = new HashMap<>();
                try {
                    map = gson.fromJson(scanResult, HashMap.class);
                } catch (Exception e) {
                    isProcessing = false;
                    buttonResumeCamera.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Mã QR không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = map.get("type");


                if (type != null){
                    switch (type){
                        case "certificate":
                            isProcessing = false;
                            String id = map.get("id");
                            Intent intent = new Intent(QrScannerActivity.this, VaccinationCertificateActivity.class);
                            intent.putExtra("certificate_id", id);
                            startActivity(intent);
                            break;
                        case "baby_information":
                            isProcessing = false;
                            String baby_id = map.get("baby_id");
                            String cus_id = map.get("cus_id");
                            Intent intent2 = new Intent(QrScannerActivity.this, BabyProfileActivity.class);
                            intent2.putExtra("baby_id", baby_id);
                            intent2.putExtra("cus_id", cus_id);
                            startActivity(intent2);
                            break;
                        default:
                            isProcessing = false;
                            buttonResumeCamera.setVisibility(View.VISIBLE);
                            Toast.makeText(this, "Mã QR không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                    }
                }else{
                    isProcessing = false;
                    buttonResumeCamera.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Mã QR không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }else {
                scannerView.resumeCameraPreview(this);
            }

            Log.i("Scanner", "handleResult: " + scanResult );
        }
    }
}