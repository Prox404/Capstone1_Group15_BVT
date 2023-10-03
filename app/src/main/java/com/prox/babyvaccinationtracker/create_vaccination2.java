package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class create_vaccination2 extends AppCompatActivity {
    EditText edt_quantity, edt_dosage,edt_unit,edt_date_of_entry,edt_price;
    Button btnthem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vaccination2);

        String vaccine_name = getIntent().getStringExtra("vaccine_name");
        String vac_effectiveness = getIntent().getStringExtra("vac_effectiveness");
        String post_vaccination_reactions = getIntent().getStringExtra("post_vaccination_reactions");
        String origin = getIntent().getStringExtra("origin");
        int vaccination_target_group = Integer.parseInt(getIntent().getStringExtra("vaccination_target_group"));
        String contraindications = getIntent().getStringExtra("contraindications");

        edt_quantity = findViewById(R.id.quantity);
        edt_dosage = findViewById(R.id.dosage);
        edt_unit = findViewById(R.id.unit);
        edt_date_of_entry = findViewById(R.id.date_of_entry);
        edt_price = findViewById(R.id.price);

        btnthem = findViewById(R.id.button);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(edt_quantity.getText().toString());
                int dosage = Integer.parseInt(edt_dosage.getText().toString());
                String unit = edt_unit.getText().toString();
                String date_of_entry = edt_date_of_entry.getText().toString();
                double price = Double.parseDouble(edt_price.getText().toString());
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
                Intent i = new Intent(create_vaccination2.this, MainActivity.class);
            }
        });


    }
}