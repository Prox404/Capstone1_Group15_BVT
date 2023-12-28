package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.Adapter.RecyclerAdapter;
import java.util.ArrayList;

public class See_detailed_vaccine_information extends AppCompatActivity {
    RecyclerView vaccine_image;
    RecyclerAdapter adapter;
    TextView tv_vaccine_name,tv_vac_effectiveness,tv_post_vaccination_reactions,tv_origin,tv_vaccination_target_group,tv_contraindications,tv_quantity,tv_dosage,tv_unit,tv_date_of_entry,tv_price,tv_detail_deleted;
    Vaccines vaccine;

    ImageView image_detail_vaccine_back,image_detail_edit_vaccine;
    public static ImageView single_vaccine_image;
    ArrayList<Uri> vacimage = new ArrayList<>();

    String id = "";

    String []arrayVaccineTypeEN = new String[18];
    String []arrayVaccineTypeVN = new String[18];

    final int UPDATE_VACCINE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_detailed_vaccine_information);
        // ánh xạ
        image_detail_vaccine_back = findViewById(R.id.image_detail_vaccine_back);
        image_detail_edit_vaccine = findViewById(R.id.image_detail_edit_vaccine);
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
        tv_detail_deleted = findViewById(R.id.tv_detail_deleted);
        arrayVaccineTypeEN = getResources().getStringArray(R.array.array_vaccine_type_EN);
        arrayVaccineTypeVN = getResources().getStringArray(R.array.array_vaccine_type_VN);
        // lấy dữ liệu tìm kiếm
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng Vaccines từ Bundle
            vaccine = (Vaccines) bundle.getSerializable("vaccine_name");
            if (vaccine != null) {
                // Bạn có thể sử dụng đối tượng vaccines ở đây.
                tv_vaccine_name.setText(vaccine.getVaccine_name());
                int position = 0 ;
                int size = arrayVaccineTypeEN.length;
                for(int i = 0 ; i < size;i++){
                    if(arrayVaccineTypeEN[i].equals(vaccine.getVac_effectiveness()))
                        position = i;
                }
                tv_vac_effectiveness.setText(arrayVaccineTypeVN[position]);
                vaccine.setVac_effectiveness(arrayVaccineTypeVN[position]);
                tv_post_vaccination_reactions.setText(vaccine.getPost_vaccination_reactions());
                tv_origin.setText(vaccine.getOrigin());
                tv_vaccination_target_group.setText(vaccine.getVaccination_target_group());
                tv_contraindications.setText(vaccine.getContraindications());
                tv_quantity.setText(vaccine.getQuantity());
                tv_dosage.setText(vaccine.getDosage());
                tv_unit.setText(vaccine.getUnit());
                tv_date_of_entry.setText(vaccine.getDate_of_entry());
                tv_price.setText(vaccine.getPrice());
                if(vaccine.isDeleted()){
                    tv_detail_deleted.setText("Đã ngừng bán");
                }
                else{
                    tv_detail_deleted.setText("Đang bán");
                }
                ArrayList<String> url_image = vaccine.getVaccine_image();
                for(String a : url_image){
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
            else{
                Toast.makeText(See_detailed_vaccine_information.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(See_detailed_vaccine_information.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
        }

        // edit
        image_detail_edit_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(See_detailed_vaccine_information.this, update_inforamtion_vaccine.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("vaccine_name", vaccine);
                intent.putExtras(bundle);
                startActivityForResult(intent,UPDATE_VACCINE);
            }
        });
        // back
        image_detail_vaccine_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_VACCINE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            vaccine = (Vaccines) bundle.getSerializable("vaccine_name");
            tv_vaccine_name.setText(vaccine.getVaccine_name());
            int position = 0 ;
            int size = arrayVaccineTypeEN.length;
            for(int i = 0 ; i < size;i++){
                if(arrayVaccineTypeEN[i].equals(vaccine.getVac_effectiveness()))
                    position = i;
            }
            tv_vac_effectiveness.setText(arrayVaccineTypeVN[position]);
            vaccine.setVac_effectiveness(arrayVaccineTypeVN[position]);
            tv_post_vaccination_reactions.setText(vaccine.getPost_vaccination_reactions());
            tv_origin.setText(vaccine.getOrigin());
            tv_vaccination_target_group.setText(vaccine.getVaccination_target_group());
            tv_contraindications.setText(vaccine.getContraindications());
            tv_quantity.setText(vaccine.getQuantity());
            tv_dosage.setText(vaccine.getDosage());
            tv_unit.setText(vaccine.getUnit());
            tv_date_of_entry.setText(vaccine.getDate_of_entry());
            tv_price.setText(vaccine.getPrice());
            if(vaccine.isDeleted()){
                tv_detail_deleted.setText("Đã ngừng bán");
            }
            else{
                tv_detail_deleted.setText("Đang bán");
            }
            vacimage.clear();
            for(String a : vaccine.getVaccine_image()){
                vacimage.add(Uri.parse(a));
            }
            adapter.notifyDataSetChanged();
            vaccine_image.setLayoutManager(new GridLayoutManager(See_detailed_vaccine_information.this,vacimage.size()));
            Picasso.get().load(vaccine.getVaccine_image().get(0)).into(single_vaccine_image);
            Toast.makeText(See_detailed_vaccine_information.this,"Đã cập nhập thành công", Toast.LENGTH_LONG).show();
        } else if (requestCode == UPDATE_VACCINE && resultCode == 2) {
            Bundle bundle = data.getExtras();
            vaccine = (Vaccines) bundle.getSerializable("vaccine_name");
            tv_detail_deleted.setText("Đã ngừng bán");
            Toast.makeText(See_detailed_vaccine_information.this,
                    "Đã xóa thành công", Toast.LENGTH_SHORT).show();
        }

    }
}