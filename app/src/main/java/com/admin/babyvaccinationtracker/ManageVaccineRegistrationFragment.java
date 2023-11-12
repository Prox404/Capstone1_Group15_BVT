package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.babyvaccinationtracker.Adapter.CenterRegistrationAdapter;
import com.admin.babyvaccinationtracker.model.Vaccine_center;
import com.admin.babyvaccinationtracker.model.Vaccine_center_registration;
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
 * Use the {@link ManageVaccineRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageVaccineRegistrationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView confirm_vaccine_center_registration;

    ArrayList<Vaccine_center_registration> registrations = new ArrayList<>();
    ArrayList<Vaccine_center_registration> registrations_origin = new ArrayList<>();
    CenterRegistrationAdapter adapter;
    Context context;
    TextView editTexte_Search_Vaccine_Center;

    public ManageVaccineRegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageVaccineRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageVaccineRegistrationFragment newInstance(String param1, String param2) {
        ManageVaccineRegistrationFragment fragment = new ManageVaccineRegistrationFragment();
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
        editTexte_Search_Vaccine_Center.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container != null ? container.getContext() : null;
        View view = inflater.inflate(R.layout.fragment_manage_vaccine_registration, container, false);
        confirm_vaccine_center_registration = view.findViewById(R.id.confirm_vaccine_center_registration);
        editTexte_Search_Vaccine_Center = view.findViewById(R.id.editTexte_Search_Vaccine_Center);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vaccineRef = database.getReference("Vaccine_center_registration");


        adapter = new CenterRegistrationAdapter(context,registrations);
        confirm_vaccine_center_registration.setLayoutManager(new LinearLayoutManager(context));
        confirm_vaccine_center_registration.setAdapter(adapter);

        Query query = vaccineRef.orderByChild("status").equalTo(0);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registrations.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Vaccine_center_registration regi = snap.getValue(Vaccine_center_registration.class);
                    regi.setCenter_registration_id(snap.getKey());
                    registrations.add(regi);
                }
                registrations_origin = new ArrayList<>(registrations);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebasseeeee", ""+error);
            }
        });

        editTexte_Search_Vaccine_Center.addTextChangedListener(new TextWatcher() {
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
        List<Vaccine_center_registration> listfilter = new ArrayList<>();
        if(!name.isEmpty()){
            for(Vaccine_center_registration a : registrations_origin){
                if(removeDiacritics(a.getCenter().getCenter_name().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))){
                    listfilter.add(a);
                }
            }
            adapter.setData(listfilter);
        }
        else {
            adapter.setData(registrations_origin);
        }
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}