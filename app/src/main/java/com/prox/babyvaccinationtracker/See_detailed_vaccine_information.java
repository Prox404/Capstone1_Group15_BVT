package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class See_detailed_vaccine_information extends AppCompatActivity {

    RecyclerView vaccine_image;
    RecyclerAdapter adapter;
    TextView tv_vaccine_name,tv_vac_effectiveness,tv_post_vaccination_reactions,tv_origin,tv_vaccination_target_group,tv_contraindications,tv_quantity,tv_dosage,tv_unit,tv_date_of_entry,tv_price;
    Vaccines vaccine;
    public static ImageView single_vaccine_image;
    ArrayList<Uri> vacimage = new ArrayList<>();

    String id = "-NgF6FMuUzxw2hjRiwu6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_detailed_vaccine_information);

        vaccine_image = findViewById(R.id.see_detail_vaccine_image);

        tv_vaccine_name = findViewById(R.id.see_detail_vaccine_name);
        tv_vac_effectiveness = findViewById(R.id.see_detail_vac_effectiveness);
        tv_post_vaccination_reactions = findViewById(R.id.see_detail_post_vaccination_reactions);
        tv_origin = findViewById(R.id.see_detail_vaccine_origin);
        tv_vaccination_target_group = findViewById(R.id.see_detail_vaccination_target_group);
        tv_contraindications = findViewById(R.id.see_detail_vaccine_contraindications);
        tv_quantity = findViewById(R.id.see_detail_vaccine_quantity);
        tv_dosage = findViewById(R.id.see_detail_vaccine_dosage);
        tv_unit = findViewById(R.id.see_detail_vaccine_unit);
        tv_date_of_entry = findViewById(R.id.see_detail_vaccine_date_of_entry);
        tv_price = findViewById(R.id.see_detail_vaccine_price);

        single_vaccine_image = findViewById(R.id.see_detail_single_vaccine_image);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Vaccines");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccine = new Vaccines();
                try{
                    vaccine = snapshot.child(id).getValue(Vaccines.class);

                }catch (Exception e){
                    Toast.makeText(See_detailed_vaccine_information.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }

                tv_vaccine_name.setText(vaccine.getVaccine_name());
                tv_vac_effectiveness.setText(vaccine.getVac_effectiveness());
                tv_post_vaccination_reactions.setText(vaccine.getPost_vaccination_reactions());
                tv_origin.setText(vaccine.getOrigin());
                tv_vaccination_target_group.setText(vaccine.getVaccination_target_group());
                tv_contraindications.setText(vaccine.getContraindications());
                tv_quantity.setText(vaccine.getQuantity());
                tv_dosage.setText(vaccine.getDosage());
                tv_unit.setText(vaccine.getUnit());
                tv_date_of_entry.setText(vaccine.getDate_of_entry());
                tv_price.setText(vaccine.getPrice());

                for(String a : vaccine.getVaccine_image()){
                    Uri b = Uri.parse(a);
                    vacimage.add(b);
                }

                adapter = new RecyclerAdapter(vacimage, See_detailed_vaccine_information.this);
                vaccine_image.setLayoutManager(new GridLayoutManager(See_detailed_vaccine_information.this,vacimage.size()));
                vaccine_image.setAdapter(adapter);

                adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Uri imageUri) {
                        Log.i("Siuu", ""+imageUri.toString());
                        Picasso.get().load(imageUri.toString()).into(single_vaccine_image);
                    }
                });

                Picasso.get().load(vaccine.getVaccine_image().get(0)).into(single_vaccine_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(See_detailed_vaccine_information.this, "LỖI!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                Log.e("Firebase error",""+error);
            }

        });
    }
}