package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admin.babyvaccinationtracker.Adapter.ManageCenterListAdapter;
import com.admin.babyvaccinationtracker.model.Vaccine_center;
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
 * Use the {@link ManageVaccineCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageVaccineCenterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView  editTexte_Search_vaccine_center;
    RecyclerView recyclerViewcenter;
    Context context;
    DatabaseReference databaseReference;

    ManageCenterListAdapter adapter;
    View empty_layout;

    List<Vaccine_center> vaccineCenterList = new ArrayList<>();
    List<Vaccine_center> vaccineCenterList_origin = new ArrayList<>();
    public ManageVaccineCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageVaccineCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageVaccineCenterFragment newInstance(String param1, String param2) {
        ManageVaccineCenterFragment fragment = new ManageVaccineCenterFragment();
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
        editTexte_Search_vaccine_center.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_vaccine_center, container, false);
        context = container != null ? container.getContext() : null;
        editTexte_Search_vaccine_center = view.findViewById(R.id.editTexte_Search_manager_vaccine_center);
        recyclerViewcenter = view.findViewById(R.id.RecylerViewManageListVaccineCenter);
        empty_layout = view.findViewById(R.id.empty_layout);

        adapter = new ManageCenterListAdapter(vaccineCenterList,context);
        recyclerViewcenter.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewcenter.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center");
        Query query = databaseReference.orderByChild("blocked").equalTo(null);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    vaccineCenterList.clear();
                    for(DataSnapshot a : snapshot.getChildren()){
                        String center_id = a.getKey();
                        String center_name = a.child("center_name").getValue(String.class);
                        String center_email = a.child("center_email").getValue(String.class);

                        Vaccine_center v = new Vaccine_center();
                        v.setCenter_id(center_id);
                        v.setCenter_name(center_name);
                        v.setCenter_email(center_email);

                        vaccineCenterList.add(v);
                    }
                    vaccineCenterList_origin = new ArrayList<>(vaccineCenterList);
                    if(vaccineCenterList_origin.size() <= 0){
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_layout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();

                    adapter.setOnItemClickListener(new ManageCenterListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Vaccine_center center) {
                            String center_name = center.getCenter_name();
                            String center_email = center.getCenter_email();
                            String center_id = center.getCenter_id();
                            BlockUser blockUser = new BlockUser();

                            Bundle args = new Bundle();
                            args.putInt("isCus",0);
                            args.putString("user_name", center_name);
                            args.putString("user_email", center_email);
                            args.putString("user_id", center_id );
                            blockUser.setArguments(args);

                            blockUser.show(getActivity().getSupportFragmentManager(), "Chặn người dùng");
                        }
                    });
                }else {
                    vaccineCenterList_origin.clear();
                    empty_layout.setVisibility(View.VISIBLE);
                    vaccineCenterList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editTexte_Search_vaccine_center.addTextChangedListener(new TextWatcher() {
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

    private void search(String name) {
        List<Vaccine_center> centers_filter = new ArrayList<>();
        if(!name.isEmpty()){
            vaccineCenterList.clear();
            for(Vaccine_center a : vaccineCenterList_origin){
                if(removeDiacritics(a.getCenter_name().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))){
                    centers_filter.add(a);
                }
            }
            vaccineCenterList = new ArrayList<>(centers_filter);
        }
        else {
            vaccineCenterList = new ArrayList<>(vaccineCenterList_origin);
        }
        adapter.setData(vaccineCenterList);
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}