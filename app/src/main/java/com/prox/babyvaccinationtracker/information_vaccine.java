package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class information_vaccine extends AppCompatActivity {
    ArrayList<Uri> vacimage = new ArrayList<>();
    Vaccines vaccine;
    String Vaccine_center_id = "";
    String customer_id = "";
    String vaccine_id = "";
    boolean care = false;
    DatabaseReference reference_Favorite;
    Query query_Favorite;
    String key_Favorite = "";
    Vaccine_center center_f;
    Vaccines vaccine_f;
    private ImageView large_image, imageView_care;
    private RecyclerView smallImageRecyclerView;
    private TextView txt_ten, tv_detail_deleted, txt_hieuqua, txt_phanungsautiem, txt_nguongoc, txt_nhomtuoisudung,
            txt_chongchidinh, txt_soluong, txt_lieuluong, txt_donvi, txt_hansudung, txt_gia, textViewAddToCart;
    private image_adapter imageAdapter;
    private LinearLayout addToCartContainer;

    String []arrayVaccineTypeEN = new String[18];
    String []arrayVaccineTypeVN = new String[18];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_vaccine);
        large_image = findViewById(R.id.large_image);
        smallImageRecyclerView = findViewById(R.id.smallImageRecyclerView);
        txt_ten = findViewById(R.id.txt_ten);
        txt_hieuqua = findViewById(R.id.txt_hieuqua);
        txt_phanungsautiem = findViewById(R.id.txt_phanungsautiem);
        txt_nguongoc = findViewById(R.id.txt_nguongoc);
        txt_nhomtuoisudung = findViewById(R.id.txt_nhomtuoisudung);
        txt_chongchidinh = findViewById(R.id.txt_chongchidinh);
        txt_soluong = findViewById(R.id.txt_soluong);
        txt_lieuluong = findViewById(R.id.txt_lieuluong);
        txt_donvi = findViewById(R.id.txt_donvi);
        txt_hansudung = findViewById(R.id.txt_hansudung);
        txt_gia = findViewById(R.id.txt_gia);
        tv_detail_deleted = findViewById(R.id.tv_detail_deleted);
        textViewAddToCart = findViewById(R.id.textViewAddToCart);
        addToCartContainer = findViewById(R.id.addToCartContainer);

        imageView_care = findViewById(R.id.imageView_care);

        arrayVaccineTypeEN = getResources().getStringArray(R.array.array_vaccine_type_EN);
        arrayVaccineTypeVN = getResources().getStringArray(R.array.array_vaccine_type_VN);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");


        addToCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (care) {
                    reference_Favorite.child(key_Favorite).setValue(null);
                    care = false;
                    imageView_care.setImageResource(R.drawable.store_default);
                    textViewAddToCart.setText("Thêm vào giỏ hàng");
                } else {
                    key_Favorite = vaccine_id;
                    Log.i("KEYYYY", key_Favorite);
                    reference_Favorite.child(key_Favorite).child("vaccines").setValue(vaccine_f);
                    care = true;
                    imageView_care.setImageResource(R.drawable.store_choose);
                    textViewAddToCart.setText("Đã thêm vào giỏ hàng");
                }

            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            vaccine = (Vaccines) bundle.getSerializable("vaccine");
            txt_ten.setText(vaccine.getVaccine_name());

            for(int i = 0 ; i  < arrayVaccineTypeEN.length ; i ++){
                if(arrayVaccineTypeEN[i].equals(vaccine.getVac_effectiveness())){
                    txt_hieuqua.setText(arrayVaccineTypeVN[i]);
                    break;
                }
            }

            txt_phanungsautiem.setText(vaccine.getPost_vaccination_reactions());
            txt_nguongoc.setText(vaccine.getOrigin());
            txt_nhomtuoisudung.setText(vaccine.getVaccination_target_group());
            txt_chongchidinh.setText(vaccine.getContraindications());
            txt_soluong.setText(vaccine.getQuantity());
            txt_lieuluong.setText(vaccine.getDosage());
            txt_donvi.setText(vaccine.getUnit());
            txt_hansudung.setText(vaccine.getDate_of_entry());
            txt_gia.setText(vaccine.getPrice());

            if (vaccine.isDeleted()) {
                tv_detail_deleted.setText("Đã ngừng bán");

            } else {
                tv_detail_deleted.setText("Đang bán");
            }


            vaccine_id = vaccine.getVaccine_id();
            center_f = vaccine.getVaccine_center_owner();
            vaccine_f = vaccine;

            center_f.setVaccines(null);
            vaccine_f.setVaccine_center_owner(center_f);

            reference_Favorite = FirebaseDatabase.getInstance().getReference("Favorite").child(customer_id);
            query_Favorite = reference_Favorite.orderByChild("vaccines/vaccine_id").equalTo(vaccine_id);

            query_Favorite.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HashMap<String, Objects> a = (HashMap<String, Objects>) snapshot.getValue();
                        key_Favorite = a.keySet().toString();
                        key_Favorite = key_Favorite.replace("[", "").replace("]", "");
                        Log.i("KEYYYY", key_Favorite + "");
                        care = true;
                        imageView_care.setImageResource(R.drawable.store_choose);
                        textViewAddToCart.setText("Đã thêm vào giỏ hàng");
                    } else {
                        care = false;
                        imageView_care.setImageResource(R.drawable.store_default);
                        textViewAddToCart.setText("Thêm vào giỏ hàng");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            if(vaccine.getAdditionInformation().get("is_user_care").equals("true")){
//                care = true;
//                imageView_care.setImageResource(R.drawable.ic_heart_solid);
//            }
//            else {
//                care = false;
//                imageView_care.setImageResource(R.drawable.ic_heart);
//            }

//            Log.i("Information_ADDITION", vaccine.getAdditionInformation()+"");
//            Vaccine_center_id = vaccine.getAdditionInformation().get("center_id");
//            reference = FirebaseDatabase.getInstance()
//                    .getReference("users")
//                    .child("Vaccine_center")
//                    .child(Vaccine_center_id)
//                    .child("vaccines")
//                    .child(vaccine.getVaccine_id())
//                    .child("user_cares")
//                    .child(customer_id);

            for (String a : vaccine.getVaccine_image()) {
                Uri b = Uri.parse(a);
                vacimage.add(b);
            }

            imageAdapter = new image_adapter(vacimage, information_vaccine.this);
            smallImageRecyclerView.setLayoutManager(new GridLayoutManager(information_vaccine.this, vacimage.size()));
            smallImageRecyclerView.setAdapter(imageAdapter);

            imageAdapter.setOnItemClickListener(new image_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(Uri imageUri) {
                    Log.i("Siuu", "" + imageUri.toString());
                    Picasso.get().load(imageUri.toString()).into(large_image);
                }
            });

            Picasso.get().load(vaccine.getVaccine_image().get(0)).into(large_image);
        }

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("Vaccines");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                vaccine = new Vaccines();
//                try{
//                    vaccine = snapshot.child(id).getValue(Vaccines.class);
//
//                }catch (Exception e){
//                    Toast.makeText(information_vaccine.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                txt_ten.setText(vaccine.getVaccine_name());
//                txt_hieuqua.setText(vaccine.getVac_effectiveness());
//                txt_phanungsautiem.setText(vaccine.getPost_vaccination_reactions());
//                txt_nguongoc.setText(vaccine.getOrigin());
//                txt_nhomtuoisudung.setText(vaccine.getVaccination_target_group());
//                txt_chongchidinh.setText(vaccine.getContraindications());
//                txt_soluong.setText(vaccine.getQuantity());
//                txt_lieuluong.setText(vaccine.getDosage());
//                txt_donvi.setText(vaccine.getUnit());
//                txt_hansudung.setText(vaccine.getDate_of_entry());
//                txt_gia.setText(vaccine.getPrice());
//
//                for(String a : vaccine.getVaccine_image()){
//                    Uri b = Uri.parse(a);
//                    vacimage.add(b);
//                }
//
//                imageAdapter = new image_adapter(vacimage, information_vaccine.this);
//                smallImageRecyclerView.setLayoutManager(new GridLayoutManager(information_vaccine.this,vacimage.size()));
//                smallImageRecyclerView.setAdapter(imageAdapter);
//
//                imageAdapter.setOnItemClickListener(new image_adapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Uri imageUri) {
//                        Log.i("Siuu", ""+imageUri.toString());
//                        Picasso.get().load(imageUri.toString()).into(large_image);
//                    }
//                });
//
//                Picasso.get().load(vaccine.getVaccine_image().get(0)).into(large_image);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(information_vaccine.this, "LỖI!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
//                Log.e("Firebase error",""+error);
//            }
//
//        });
    }
}