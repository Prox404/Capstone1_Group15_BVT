package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ManageVaccineRegistrationFragment extends Fragment {

    RecyclerView confirm_vaccine_center_registration;

    ArrayList<Vaccine_center_registration> registrations = new ArrayList<>();
    ArrayList<Vaccine_center_registration> registrations_origin = new ArrayList<>();
    CenterRegistrationAdapter adapter;
    Context context;
    TextView editTexte_Search_Vaccine_Center, textViewDialogAddress, textViewDialogWorkTime,
            textViewDialogPhone,
    textViewDialogCenterName;

    View empty_layout;
    CardView PopupImage;
    ImageView ImageCertificate, imageViewDialogCenterImage;
    LinearLayout ImageClose;

    public ManageVaccineRegistrationFragment() {

    }


    public static ManageVaccineRegistrationFragment newInstance(String param1, String param2) {
        ManageVaccineRegistrationFragment fragment = new ManageVaccineRegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        editTexte_Search_Vaccine_Center.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container != null ? container.getContext() : null;
        View view = inflater.inflate(R.layout.fragment_manage_vaccine_registration, container, false);
        confirm_vaccine_center_registration = view.findViewById(R.id.confirm_vaccine_center_registration);
        editTexte_Search_Vaccine_Center = view.findViewById(R.id.editTexte_Search_Vaccine_Center);
        empty_layout = view.findViewById(R.id.empty_layout);
        PopupImage = view.findViewById(R.id.PopupImage);
        ImageCertificate = view.findViewById(R.id.ImageCertificate);
        ImageClose = view.findViewById(R.id.ImageClose);
        imageViewDialogCenterImage = view.findViewById(R.id.imageViewDialogCenterImage);
        textViewDialogAddress = view.findViewById(R.id.textViewDialogAddress);
        textViewDialogWorkTime = view.findViewById(R.id.textViewDialogWorkTime);
        textViewDialogPhone = view.findViewById(R.id.textViewDialogPhone);
        textViewDialogCenterName = view.findViewById(R.id.textViewDialogCenterName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vaccineRef = database.getReference("Vaccine_center_registration");


        adapter = new CenterRegistrationAdapter(context,registrations);
        confirm_vaccine_center_registration.setLayoutManager(new LinearLayoutManager(context));
        confirm_vaccine_center_registration.setAdapter(adapter);

        Query query = vaccineRef.orderByChild("status").equalTo(0);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    registrations.clear();
                    registrations_origin.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Vaccine_center_registration regi = snap.getValue(Vaccine_center_registration.class);
                        regi.getCenter().setCenter_address2(snap.child("center").child("center_address2").getValue(String.class));
                        regi.setCenter_registration_id(snap.getKey());
                        registrations.add(regi);
                    }
                    adapter.setData(registrations);
                    registrations_origin = new ArrayList<>(registrations);
                    if (registrations_origin.size() <= 0){
                        empty_layout.setVisibility(View.VISIBLE);
                    } else {
                        empty_layout.setVisibility(View.GONE);
                    }
                }else {
                    registrations_origin.clear();
                    registrations.clear();
                    empty_layout.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }

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

        adapter.setOnItemClickListener(new CenterRegistrationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Vaccine_center_registration registration) {
                if(PopupImage.getVisibility() == View.VISIBLE){
                    hidePopupDialogWithAnimation();
                }
                else {

                    showPopupDialogWithAnimation(registration);
                }
            }
        });
        ImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePopupDialogWithAnimation();
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
    private void showPopupDialogWithAnimation(Vaccine_center_registration registration) {
        // Sử dụng Animation để thực hiện hiệu ứng xuất hiện
        Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);
        PopupImage.startAnimation(animation);
        String url_Cer = registration.getCenter().getActivity_certificate();
        url_Cer = url_Cer.contains("https") ? url_Cer : url_Cer.replace("http", "https");
        Picasso.get().load(url_Cer).into(ImageCertificate, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("IMAGE_MANAGEMENT", "onSuccess");
            }

            @Override
            public void onError(Exception e) {
                ImageCertificate.setImageResource(R.drawable.user_default_avatar);
            }
        });
        String address = registration.getCenter().getCenter_address2() + ", " + registrations.get(0).getCenter().getCenter_address();
        textViewDialogAddress.setText(address);
        textViewDialogWorkTime.setText(registration.getCenter().getWork_time());
        textViewDialogPhone.setText(registration.getCenter().getHotline());
        textViewDialogCenterName.setText(registration.getCenter().getCenter_name());

        String center_image = registration.getCenter().getCenter_image();
        center_image = center_image.contains("https") ? center_image : center_image.replace("http", "https");
        Picasso.get().load(center_image).into(imageViewDialogCenterImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("IMAGE_MANAGEMENT", "onSuccess");
            }

            @Override
            public void onError(Exception e) {
                imageViewDialogCenterImage.setImageResource(R.drawable.user_default_avatar);
            }
        });

        // Đặt sự kiện visibility cho popupDialog
        PopupImage.setVisibility(View.VISIBLE);
        ImageCertificate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        hidePopupDialogWithAnimation();
    }

    private void hidePopupDialogWithAnimation() {
        // Sử dụng Animation để thực hiện hiệu ứng ẩn đi
        Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_out);
        PopupImage.startAnimation(animation);

        // Đặt sự kiện visibility cho popupDialog
        PopupImage.setVisibility(View.GONE);
        ImageCertificate.setVisibility(View.GONE);
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}