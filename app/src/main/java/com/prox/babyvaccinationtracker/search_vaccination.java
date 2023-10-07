package com.prox.babyvaccinationtracker;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;
import java.util.List;

public class search_vaccination extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private TextView vaccineInfoTextView;
    Button btntimkiem;
    RecyclerView recvaccine;
    vaccineadapter mVaccineadapter;
    private List<Vaccines> mlistvaccine;

    private void setrcv() {
        autoCompleteTextView = findViewById(R.id.edttimkiem);
        vaccineInfoTextView = findViewById(R.id.vaccineInfoTextView);
        btntimkiem = findViewById(R.id.btntimkiem);
        // Khởi tạo danh sách vaccine
        mlistvaccine = new ArrayList<>();
        recvaccine = findViewById(R.id.rcvvaccine);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recvaccine.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recvaccine.addItemDecoration(dividerItemDecoration);
        mVaccineadapter = new vaccineadapter(mlistvaccine);
        recvaccine.setAdapter(mVaccineadapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vaccination);
        setrcv();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = vaccineInfoTextView.getText().toString().trim().toLowerCase();
                    if (searchString.toLowerCase().contains(searchString)) {
                        getdatafromrealtimedatabase();
                    }
            }
        });
    }
    private void getdatafromrealtimedatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Vaccines");
        Log.d("vaccine", "getdatafromrealtimedatabase: call");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Vaccines vaccine = datasnapshot.getValue(Vaccines.class);
                    Log.i("vaccine", "onDataChange: " + vaccine.toString());
                    mlistvaccine.add(vaccine);
                }
                mVaccineadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_vaccination.this, "null", Toast.LENGTH_LONG).show();
            }
        });
    }
}














//        // Khởi tạo Firebase
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference vaccinesReference = FirebaseDatabase.getInstance().getReference("Vaccines");
//
//        // Lắng nghe sự thay đổi trên Firebase và lấy danh sách vaccine
//        vaccinesReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                vaccineList.clear();
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Vaccines vaccine = snapshot.getValue(Vaccines.class);
////                    vaccineList.add(vaccine);
////                }
//                if (dataSnapshot.exists()) {
//                    String vaccineInfo = dataSnapshot.child("info").getValue(String.class);
//                    vaccineInfoTextView = findViewById(R.id.edttimkiem);
//                    vaccineInfoTextView.setText(vaccineInfo);
//                    vaccineInfoTextView.setVisibility(View.VISIBLE);
//                } else {
//                    // Xử lý trường hợp không tìm thấy thông tin vaccine
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi nếu có
//            }
//        });
//        // Tạo ArrayAdapter cho AutoCompleteTextView
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getVaccineNames());
//        autoCompleteTextView.setAdapter(adapter);
//
//        // Xử lý sự kiện khi chọn đề xuất
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedVaccineName = parent.getItemAtPosition(position).toString();
//                showVaccineInfo(selectedVaccineName);
//            }
//        });
//    }
//
//    // Lấy danh sách tên vaccine từ danh sách vaccine
//    private List<String> getVaccineNames() {
//        List<String> vaccineNames = new ArrayList<>();
//        for (Vaccines vaccine : vaccineList) {
//            vaccineNames.add(vaccine.getVaccine_name());
//        }
//        return vaccineNames;
//    }
//
//
//    // Hiển thị thông tin vaccine khi chọn đề xuất
//    private void showVaccineInfo(String vaccineName) {
//        for (Vaccines vaccine : vaccineList) {
//            if (vaccine.getVaccine_name().equals(vaccineName)) {
//                String info = "Hình ảnh vaccine: \n" + vaccine.getVaccine_image() + "\n"
//                        + "Tên vaccine: \n" + vaccine.getVaccine_name() + "\n"
//                        + "Đơn vị: \n" + vaccine.getUnit() + "\n"
//                        + "Chống chỉ định: \n" + vaccine.getContraindications() + "\n"
//                        + "Hạn sử dụng: \n" + vaccine.getDate_of_entry() + "\n"
//                        + "Liều lượng: \n" + vaccine.getDosage() + "\n"
//                        + "Xuất xứ: \n" + vaccine.getOrigin() + "\n"
//                        + "Phản ứng sau tiêm: \n" + vaccine.getPost_vaccination_reactions() + "\n"
//                        + "Giá vaccine: \n" + vaccine.getPrice() + "\n"
//                        + "Số lượng: \n" + vaccine.getQuantity() + "\n"
//                        + "Hiệu quả: \n" + vaccine.getVac_effectiveness() + "\n"
//                        + "Đối tượng tiêm chủng: \n" + vaccine.getVaccination_target_group() + "\n";
//
//
//                vaccineInfoTextView.setText(info);
//                vaccineInfoTextView.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//    }
//}

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class search_vaccination extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_vaccination);
//        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.edttimkiem);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Vaccines);
//        autoCompleteTextView.setAdapter(adapter);
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedVaccineName = (String) parent.getItemAtPosition(position);
//                showVaccineInfo(selectedVaccineName);
//            }
//        });
//    }
//    private void showVaccineInfo(String vaccine_name) {
//        DatabaseReference vaccineReference = FirebaseDatabase.getInstance().getReference("vaccines");
//        vaccineReference.child(vaccine_name).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String vaccineInfo = dataSnapshot.child("info").getValue(String.class);
//                    TextView vaccineInfoTextView = findViewById(R.id.edtten);
//                    vaccineInfoTextView.setText(vaccineInfo);
//                    vaccineInfoTextView.setVisibility(View.VISIBLE);
//                } else {
//                    // Xử lý trường hợp không tìm thấy thông tin vaccine
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi nếu có
//            }
//        });
//    }
//}