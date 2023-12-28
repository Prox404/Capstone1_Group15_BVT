package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.Toast;

import com.admin.babyvaccinationtracker.Adapter.ManageUserListViewAdapter;
import com.admin.babyvaccinationtracker.model.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editTexte_Search_user;
    RecyclerView RecylerViewListUser;

    Context context;
    DatabaseReference databaseReference;
    ManageUserListViewAdapter adapter;
    List<Customer> customers = new ArrayList<>();
    List<Customer> customers_origin = new ArrayList<>();

    View empty_layout;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1){
            Toast.makeText(context,"Chặn thành công", Toast.LENGTH_LONG).show();
        }
    }

    public ManageUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageUserFragment newInstance(String param1, String param2) {
        ManageUserFragment fragment = new ManageUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        editTexte_Search_user.setText("");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container != null ? container.getContext() : null;
        View view = inflater.inflate(R.layout.fragment_manage_user, container, false);

        empty_layout = view.findViewById(R.id.empty_layout);
        editTexte_Search_user = view.findViewById(R.id.editTexte_Search_user);
        RecylerViewListUser = view.findViewById(R.id.RecylerViewListUser);

        adapter = new ManageUserListViewAdapter(context,customers);
        RecylerViewListUser.setLayoutManager(new LinearLayoutManager(context));
        RecylerViewListUser.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers");
        Query query = databaseReference.orderByChild("blocked").equalTo(null);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    customers.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String cus_name = snapshot1.child("cus_name").getValue(String.class);
                        String cus_email = snapshot1.child("cus_email").getValue(String.class);
                        String cus_avatar = snapshot1.child("cus_avatar").getValue(String.class);
                        Customer customer = new Customer();
                        customer.setCustomer_id(snapshot1.getKey());
                        customer.setCus_name(cus_name);
                        customer.setCus_email(cus_email);
                        customer.setCus_avatar(cus_avatar);
                        customers.add(customer);
                    }
                    customers_origin = new ArrayList<>(customers);
                    if(customers_origin.size() <= 0){
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_layout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();

                    adapter.setOnItemClickListener(new ManageUserListViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Customer customer) {
                            String cus_name = customer.getCus_name();
                            String cus_email = customer.getCus_email();
                            String cus_id = customer.getCustomer_id();
                            BlockUser blockUser = new BlockUser();

                            Bundle args = new Bundle();
                            args.putInt("isCus",1);
                            args.putString("user_name", cus_name);
                            args.putString("user_email", cus_email);
                            args.putString("user_id", cus_id );
                            blockUser.setArguments(args);

                            blockUser.show(getActivity().getSupportFragmentManager(), "Chặn người dùng");

                        }
                    });
                }else {
                    customers_origin.clear();
                    empty_layout.setVisibility(View.VISIBLE);
                    customers.clear();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editTexte_Search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                search(name);
            }
        });

        return view;
    }


    public void search(String name) {
        List<Customer> customer_filter = new ArrayList<>();
        if (!name.isEmpty()) {
            customers.clear();
            for (Customer a : customers_origin) {
                if (removeDiacritics(a.getCus_name().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))) {
                    customer_filter.add(a);
                }
            }
            customers = new ArrayList<>(customer_filter); // Cập nhật danh sách gốc
        } else {
            customers = new ArrayList<>(customers_origin); // Khôi phục danh sách gốc
        }
        adapter.setData(customers); // Cập nhật dữ liệu trên adapter
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}