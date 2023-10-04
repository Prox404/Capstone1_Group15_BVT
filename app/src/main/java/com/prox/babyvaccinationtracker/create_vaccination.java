package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.prox.babyvaccinationtracker.model.Vaccines;

public class create_vaccination extends AppCompatActivity {

    EditText edt_price,edt_date_of_entry,edt_unit,edt_dosage,edt_vaccine_name, edt_vac_effectiveness, edt_post_vaccination_reactions,edt_origin,edt_vaccination_target_group,edt_contraindications,edt_quantity;
    Button btn_tt;
    ImageButton img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vaccination);

        edt_vaccine_name = findViewById(R.id.vaccine_name);
        edt_vac_effectiveness = findViewById(R.id.vac_effectiveness);
        edt_post_vaccination_reactions = findViewById(R.id.post_vaccination_reactions);
        edt_origin = findViewById(R.id.origin);
        edt_vaccination_target_group = findViewById(R.id.vaccination_target_group);
        edt_contraindications = findViewById(R.id.contraindications);
        edt_quantity = findViewById(R.id.quantity);
        edt_dosage = findViewById(R.id.dosage);
        edt_unit = findViewById(R.id.unit);
        edt_date_of_entry = findViewById(R.id.date_of_entry);
        edt_price = findViewById(R.id.price);

        btn_tt = findViewById(R.id.btn_tt);

        img = findViewById(R.id.img);
        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccine_name = edt_vaccine_name.getText().toString();
                String vac_effectiveness = edt_vac_effectiveness.getText().toString();
                String post_vaccination_reactions = edt_post_vaccination_reactions.getText().toString();
                String origin = edt_origin.getText().toString();
                int vaccination_target_group = 0;
                try{
                    vaccination_target_group = Integer.parseInt(edt_vaccination_target_group.getText().toString());
                }catch (Exception e){
                    edt_vaccination_target_group.setText("ERROR");
                    edt_vaccination_target_group.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                String contraindications = edt_contraindications.getText().toString();

                int quantity = 0;
                try {
                    quantity= Integer.parseInt(edt_quantity.getText().toString());
                }catch (Exception e){
                    edt_quantity.setText("ERROR");
                    edt_quantity.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                int dosage = 0;
                try{
                    dosage = Integer.parseInt(edt_dosage.getText().toString());
                }catch (Exception e){
                    edt_dosage.setText("ERROR");
                    edt_dosage.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unit = edt_unit.getText().toString();
                String date_of_entry = edt_date_of_entry.getText().toString();

                double price = 0;
                try{
                    price = Double.parseDouble(edt_price.getText().toString());
                }catch (Exception e){
                    edt_price.setText("ERROR");
                    edt_price.requestFocus();
                    Toast.makeText(create_vaccination.this, "Phải nhập số", Toast.LENGTH_SHORT).show();
                }


                Vaccines vaccines = new Vaccines(vaccine_name,
                        vac_effectiveness,
                        post_vaccination_reactions,
                        origin,
                        vaccination_target_group,
                        contraindications,
                        quantity,
                        dosage,
                        unit,
                        date_of_entry,
                        price
                );

                vaccines.pushDataFisebase();

            }
        });

    }
}