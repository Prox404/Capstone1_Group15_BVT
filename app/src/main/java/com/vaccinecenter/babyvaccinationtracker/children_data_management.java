package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.Adapter.ChildAdapter;
import com.vaccinecenter.babyvaccinationtracker.Adapter.VaccineAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Baby;
import com.vaccinecenter.babyvaccinationtracker.model.VaccinationCertificate;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class children_data_management extends AppCompatActivity {
    private AutoCompleteTextView editText_Search;
    ImageView imageView_Back, imageView_Search_Child;
    RecyclerView rcvchild;

    ChildAdapter childAdapter;

    private List<Baby> mlistchild;

    ArrayList<String> check_id = new ArrayList<>();

    public void setrcv(){
        editText_Search = findViewById(R.id.editText_Search);
        imageView_Back = findViewById(R.id.imageView_Back);
        imageView_Search_Child = findViewById(R.id.imageView_Search_Child);
        rcvchild = findViewById(R.id.rcvchild);
        mlistchild = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvchild.setLayoutManager(linearLayoutManager);
        childAdapter = new ChildAdapter(this, mlistchild);
        rcvchild.setAdapter(childAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_data_management);
        setrcv();
        getdatafromrealtimedatabase();
        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text_search = editable.toString().trim();
                getdatafromrealtimedatabase();
            }
        });
        // nút quay lại

        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView_Search_Child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdatafromrealtimedatabase();
            }
        });
    }

    private void getdatafromrealtimedatabase() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String center_id = sharedPreferences.getString("center_id", "Trần Công Trí");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Certificate");
        Query query = databaseReference.orderByChild("center/center_id").equalTo(center_id);

        String searchTerm = editText_Search.getText().toString().trim();

        mlistchild.clear();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlistchild.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.exists()){
                        VaccinationCertificate vaccinationCertificate = dataSnapshot.getValue(VaccinationCertificate.class);
                        String id = vaccinationCertificate.getBaby().getBaby_id();
                        if (removeDiacritics(vaccinationCertificate.getBaby().getBaby_name().toLowerCase()).contains(removeDiacritics(searchTerm.toLowerCase()))){
                            if(!check_id.contains(id)){
                                check_id.add(id);
                                mlistchild.add(vaccinationCertificate.getBaby());
                                Log.i("siuuuu", "onDataChange: " + vaccinationCertificate.getBaby());
                            }
                        }
                    }
                }
                childAdapter.notifyDataSetChanged();
                Log.i("siiii", "onDataChange: " + mlistchild.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(children_data_management.this, "null", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
