package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.Manifest;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {


    RelativeLayout registionContainer,createvaccineContainer,vaccinesContainer, QRScannerContainer, ChatContainer, childContainer, CommunityContainer;
    ImageView imageViewAvatar;
    TextView textViewNumberOfRegistration,textViewNumberOfVaccines, textViewNumberOfChild, textViewGreetings;

    Toolbar toolbar;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    Context context;
    Boolean check = false;
    void check(){

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BlackList").child("Vaccine_centers").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check = false;
                    startActivity(new Intent(HomeActivity.this, Display_block_user.class));
                    finish();
                }
                else {
                    check = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        toolbar = (Toolbar) findViewById(R.id.homeToolBar);

        setSupportActionBar(toolbar);

        registionContainer = findViewById(R.id.registionContainer);
        createvaccineContainer = findViewById(R.id.createvaccineContainer);
        textViewNumberOfRegistration = findViewById(R.id.textViewNumberOfRegistration);
        textViewNumberOfVaccines = findViewById(R.id.textViewNumberOfVaccines);
        textViewNumberOfChild = findViewById(R.id.textViewNumberOfChild);
        vaccinesContainer = findViewById(R.id.vaccinesContainer);
        QRScannerContainer = findViewById(R.id.QRScannerContainer);
        CommunityContainer = findViewById(R.id.CommunityContainer);
        ChatContainer = findViewById(R.id.ChatContainer);
        textViewGreetings = findViewById(R.id.textViewGreetings);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String center_name = sharedPreferences.getString("center_name", "Trần Công Trí");
        String center_image = sharedPreferences.getString("center_image", "");
        textViewGreetings.setText(center_name);

        if (!center_image.equals("")) {
            String imgaeUrl = center_image.contains("https") ? center_image : center_image.replace("http", "https");
            Picasso.get().load(imgaeUrl).into(imageViewAvatar);
        }

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        check();


        vaccinesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == true){
                    Intent intent = new Intent(HomeActivity.this, search_vaccination.class);
                    startActivity(intent);
                }

            }
        });

        registionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == true){
                    Intent intent = new Intent(HomeActivity.this, RegistrationRequestActivity.class);
                    startActivity(intent);
                }

            }
        });
        createvaccineContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == true){
                    Intent intent = new Intent(HomeActivity.this, create_vaccination.class);
                    startActivity(intent);
                }

            }
        });

        QRScannerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == true){
                    Intent intent = new Intent(HomeActivity.this, QrScannerActivity.class);
                    startActivity(intent);
                }

            }
        });

        ChatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });


        CommunityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
          }
        });
        childContainer = findViewById(R.id.childContainer);
        childContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, children_data_management.class);
                startActivity(intent);
            }
        });

        getNumberOfRegistration();
        getNunberOfVaccines();
        getNumberOfChild();

        // checkPermission();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
        }else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
        }else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void getNumberOfRegistration() {
        context = HomeActivity.this != null ? HomeActivity.this : null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("center/center_id").equalTo(id_vaccine_center);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numberOfRegistration = snapshot.getChildrenCount() + "";
                Log.i("Home", "onDataChange: " + numberOfRegistration);
                textViewNumberOfRegistration.setText(numberOfRegistration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getNunberOfVaccines(){
        context = HomeActivity.this != null ? HomeActivity.this : null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center").child(id_vaccine_center).child("vaccines");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numberOfRegistration = snapshot.getChildrenCount() + "";
                Log.i("Home", "onDataChange: " + numberOfRegistration);
                textViewNumberOfVaccines.setText(numberOfRegistration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNumberOfChild(){
        context = HomeActivity.this != null ? HomeActivity.this : null;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("baby/baby_id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numberOfChild = snapshot.getChildrenCount() + "";
                Log.i("Home", "onDataChange: " + numberOfChild);
                textViewNumberOfChild.setText(numberOfChild);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notification) {
            // Xử lý sự kiện khi người dùng nhấn vào biểu tượng thông báo ở đây
            // Ví dụ: mở màn hình thông báo, hiển thị danh sách thông báo, vv.
            Log.i("Home", "onOptionsItemSelected: notification" );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}