package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.Adapter.AddChatAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Customer;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccine_center;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddChatConversation extends AppCompatActivity {

    TextView edt_chat_search;
    TextView tv_chat_center_address;
    TextView tv_chat_display_all;
    RecyclerView rv_chat_list_vaccine_center;
    AddChatAdapter adapter;

    ArrayList<Customer> customers = new ArrayList<>();// tất cả trung tâm tiêm chủng
    String address = "";

    ArrayList<Customer> filterAddressCenter = new ArrayList<>(); // trung tâm lọc theo địa chỉ khách hàng
    ArrayList<Customer> matchingCenters = new ArrayList<>(customers); // tìm trung tâm vắc-xin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat_conversition);

        edt_chat_search = findViewById(R.id.edt_chat_search);
        tv_chat_center_address = findViewById(R.id.tv_chat_center_address);
        tv_chat_display_all = findViewById(R.id.tv_chat_display_all);
        rv_chat_list_vaccine_center = findViewById(R.id.rv_chat_list_vaccine_center);

        Intent intent = getIntent();
        if(intent!= null){
            address = intent.getStringExtra("center_address");
            tv_chat_center_address.setText(address);
        }
        customers = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddChatConversation.this);
        rv_chat_list_vaccine_center.setLayoutManager(linearLayoutManager);
        adapter = new AddChatAdapter(AddChatConversation.this,customers);
        rv_chat_list_vaccine_center.setAdapter(adapter);

        GetDataOnFireBase_vaccine();

        tv_chat_display_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new AddChatAdapter(AddChatConversation.this, customers);
                rv_chat_list_vaccine_center.setAdapter(adapter);
            }
        });

        edt_chat_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                search_vaccines(name);
            }
        });
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    private void search_vaccines(String searchText){
        if(!searchText.isEmpty()){
            matchingCenters.clear();
            for(Customer center : customers){
                if(removeDiacritics(center.getCus_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))){
                    matchingCenters.add(center);
                }
            }
            adapter = new AddChatAdapter(AddChatConversation.this, matchingCenters);
            rv_chat_list_vaccine_center.setAdapter(adapter);
        }
        else{
            adapter = new AddChatAdapter(AddChatConversation.this, filterAddressCenter);
            rv_chat_list_vaccine_center.setAdapter(adapter);
        }
    }
    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("customers");;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String customerAddress = address;
                customers = new ArrayList<>();
                for (DataSnapshot vaccineSnapshot : snapshot.getChildren()) {
                    Customer customer = new Customer();
                    customer.setCus_name(vaccineSnapshot.child("cus_name").getValue().toString());
                    customer.setCus_address(vaccineSnapshot.child("cus_address").getValue().toString());
                    customer.setCus_phone(vaccineSnapshot.child("cus_phone").getValue().toString());
                    customer.setCustomer_id(vaccineSnapshot.getKey());
                    customer.setCus_avatar(vaccineSnapshot.child("cus_avatar").getValue().toString());
                    String cusAddress = customer.getCus_address();
                    customers.add(customer);
                    if (isAddressNearCustomer(customerAddress, cusAddress)) {
                        filterAddressCenter.add(customer);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private boolean isAddressNearCustomer(String customerAddress, String centerAddress) {
        String[] customerAddressParts = customerAddress.split(", "); // 0 1
        String[] centerAddressParts = centerAddress.split(", "); // 0 1 2

        int customerSizeAddress = customerAddressParts.length;

        if(customerSizeAddress == 1){
            if(!customerAddressParts[0].equals(centerAddressParts[2])){
                return false;
            }
        }
        else if(customerSizeAddress == 2){
            if(customerAddressParts[1].equals(centerAddressParts[2]))
                if(!customerAddressParts[0].equals(centerAddressParts[1])){
                    return false;
                }
                else
                    return true;
            else
                return false;
        } else if (customerSizeAddress == 3){
            for (int i = 0; i < customerSizeAddress; i++) {
                if (!customerAddressParts[i].equals(centerAddressParts[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}