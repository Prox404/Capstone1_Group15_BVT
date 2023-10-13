package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.Adapter.CustomerAdapter;
import com.prox.babyvaccinationtracker.model.Customer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SendNotificationUserFragment extends Fragment {

    Context context;
    private RecyclerView recyclerViewCustomer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> filterList = new ArrayList<>(customerList);
    CustomerAdapter adapter;

    public SendNotificationUserFragment() {
        // Required empty public constructor
    }


    public static SendNotificationUserFragment newInstance(String param1, String param2) {
        SendNotificationUserFragment fragment = new SendNotificationUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FRAGMENT_2", "onResume");
        Log.i("Resume", "onResume: " + customerList.toString());
        String customerNameSearch = SendNotificationActivity.searchQuery;
        if (!customerNameSearch.isEmpty()){
            filterList.clear();
            for (Customer customer : customerList){
                if (removeDiacritics(customer.getCus_name().toLowerCase()).contains(removeDiacritics(customerNameSearch.toLowerCase()))){
                    Log.i("Filter", "onResume: " + customer.getCus_name() + " " + customerNameSearch);
                    filterList.add(customer);
                }
            }
            updateUI();
        }else {
            filterList = new ArrayList<>(customerList);
            updateUI();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FRAGMENT_2", "onCreate");
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext() != null ? container.getContext() : null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification_user, container, false);

        recyclerViewCustomer = view.findViewById(R.id.recyclerViewCustomer);
        recyclerViewCustomer.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference().child("users").child("customers");
        customersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerList.clear();
                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
//                    Log.i("User", "onDataChange" + customerSnapshot.toString());
                    if (customerSnapshot.child("cus_name").exists()) {
                        Customer customer = new Customer();
                        customer.setCus_avatar(customerSnapshot.child("cus_avatar").getValue().toString());
                        customer.setCus_name(customerSnapshot.child("cus_name").getValue().toString());
                        customer.setCus_birthday(customerSnapshot.child("cus_birthday").getValue().toString());
                        customer.setCus_address(customerSnapshot.child("cus_address").getValue().toString());
                        customer.setCus_phone(customerSnapshot.child("cus_phone").getValue().toString());
                        customer.setCus_email(customerSnapshot.child("cus_email").getValue().toString());
                        customer.setCus_gender(customerSnapshot.child("cus_gender").getValue().toString());
                        customer.setCus_ethnicity(customerSnapshot.child("cus_ethnicity").getValue().toString());
                        customer.setCustomer_id(customerSnapshot.getKey());
                        customerList.add(customer);
                    }
                }
                filterList = new ArrayList<>(customerList);
                Log.i("User", "onDataChange: " + customerList.toString());
                updateUI(); // Thông báo rằng dữ liệu đã thay đổi
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu có)
            }
        });

        Log.i("onCreateView", "onCreateView: " + customerList.toString());

        return view;
    }

    private void updateUI() {
        if (getActivity() != null) {
            adapter = new CustomerAdapter(filterList);
            recyclerViewCustomer.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateFilter(String searchQuery) {
        if (!searchQuery.isEmpty()) {
            filterList.clear();
            Log.i("Filter", "updateFilter: " + customerList.toString());
            for (Customer customer : customerList) {
                Log.i("Filter", "updateFilter: " + customer.getCus_name().toLowerCase() + " " + searchQuery.toLowerCase());
                if (removeDiacritics(customer.getCus_name().toLowerCase()).contains(removeDiacritics(searchQuery.toLowerCase()))) {
                    filterList.add(customer);
                }
            }
            updateUI();
        } else {
            filterList.clear(); // Đảm bảo rằng filterList trống khi không có tìm kiếm
            filterList = new ArrayList<>(customerList);
            updateUI();
        }
    }
}