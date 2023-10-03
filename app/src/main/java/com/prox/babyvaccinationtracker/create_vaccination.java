package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class create_vaccination extends AppCompatActivity {

    EditText edt_vaccine_name, edt_vac_effectiveness, edt_post_vaccination_reactions,edt_origin,edt_vaccination_target_group,edt_contraindications;
    Button btn_tt;
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

        btn_tt = findViewById(R.id.btn_tt);

        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccine_name = edt_vaccine_name.getText().toString();
                String vac_effectiveness = edt_vac_effectiveness.getText().toString();
                String post_vaccination_reactions = edt_post_vaccination_reactions.getText().toString();
                String origin = edt_origin.getText().toString();
                String vaccination_target_group = edt_vaccination_target_group.getText().toString();
                String contraindications = edt_contraindications.getText().toString();

                Intent i = new Intent(create_vaccination.this, create_vaccination2.class);
                i.putExtra("vaccine_name",vaccine_name);
                i.putExtra("vac_effectiveness",vac_effectiveness);
                i.putExtra("post_vaccination_reactions",post_vaccination_reactions);
                i.putExtra("origin",origin);
                i.putExtra("vaccination_target_group",vaccination_target_group);
                i.putExtra("contraindications",contraindications);
                startActivity(i);

            }
        });

    }
}