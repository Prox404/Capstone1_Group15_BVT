package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.babyvaccinationtracker.model.Baby;
import com.admin.babyvaccinationtracker.model.Health;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BabyHealthActivity extends AppCompatActivity {

    TextView tvBabyName, tvBabyBirthday, tvBabyGender, tvBabyCongenitalDisease;
    ImageView ivBabyAvatar;
    LineChart lineChart;
    String id;
    DatabaseReference databaseReference;
    HashMap<Integer, Health> health = new HashMap<>();

    LinearLayout health_arlet;
    TextView health_arlet_month_now, health_arlet_header, health_arlet_content;
    ImageView health_arlet_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_health);

        tvBabyName = findViewById(R.id.tvBabyName);
        tvBabyBirthday = findViewById(R.id.tvBabyBirthday);
        tvBabyGender = findViewById(R.id.tvBabyGender);
        tvBabyCongenitalDisease = findViewById(R.id.tvBabyCongenitalDisease);
        ivBabyAvatar = findViewById(R.id.ivBabyAvatar);
        lineChart = findViewById(R.id.lineChart);
        health_arlet = findViewById(R.id.health_arlet);
        health_arlet_month_now = findViewById(R.id.health_arlet_month_now);
        health_arlet_header = findViewById(R.id.health_arlet_header);
        health_arlet_content = findViewById(R.id.health_arlet_content);
        health_arlet_image = findViewById(R.id.health_arlet_image);


        Intent intent = getIntent();
        if (intent.hasExtra("baby")) {
            Baby baby = (Baby) intent.getSerializableExtra("baby");
            Log.i("BabyHealthActivity", "onCreate: " + baby.toString());

            tvBabyName.setText(baby.getBaby_name());
            tvBabyBirthday.setText(baby.getBaby_birthday());
            tvBabyGender.setText(baby.getBaby_gender());
            tvBabyCongenitalDisease.setText(baby.getBaby_congenital_disease());
            Picasso.get().load(baby.getBaby_avatar()).into(ivBabyAvatar);

            id = baby.getBaby_id();
            databaseReference = FirebaseDatabase.getInstance().getReference("health");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            databaseReference.child(id).child("" + year).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        health.clear();
                        int currentMonth = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Health h = dataSnapshot.getValue(Health.class);
                            if (h != null) {
                                int k = Integer.parseInt(dataSnapshot.getKey());
                                health.put(k, h);
                                if (k > currentMonth) {
                                    currentMonth = k;
                                }
                            }
                        }
                        chartxyBMI(dataValues2(health));
                        ArrayList<Health> a = new ArrayList<>(health.values());
                        //todo arlet
                        Log.i("MONTHHHH", "" + month);
                        if (currentMonth != 0){
                            health_arlet.setVisibility(LinearLayout.VISIBLE);
                            display_health_arlet_baby(health.get(currentMonth), currentMonth);
                        }

                        if (!health.containsKey(month)) {
                            Toast.makeText(BabyHealthActivity.this, "Người dùng chưa nhập thông tin chỉ số của bé trong tháng này!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BabyHealthActivity.this, "Chưa có dữ liệu thông tin cơ bản của bé", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private ArrayList<Entry> dataValues2(HashMap<Integer, Health> he) {
        ArrayList<Entry> values = new ArrayList<>();
        for (Map.Entry<Integer, Health> entry : he.entrySet()) {
            float h = (float) entry.getValue().getHeight();
            float w = (float) entry.getValue().getWeight();
            h = h / 100f;
            float BMI = w / (h * h);
            int month = entry.getKey();
            values.add(new Entry(month, BMI));
        }
        return values;
    }

    private ArrayList<Entry> dataValues(int month, float BMI) {
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(month, BMI));
        return values;
    }

    private void chartxyBMI(ArrayList<Entry> a) {
        // vẽ đường
        LineDataSet lineDataSet = new LineDataSet(a, "Chỉ số BMI");
        lineDataSet.setDrawCircles(true); // có vẽ vòng tròn mỗi điểm
        lineDataSet.setCircleColor(0xFF007F20);
        lineDataSet.setLineWidth(2f); // chiều rộng đường thẳng
        lineDataSet.setColor(0xFF007F20); // màu
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Customize the appearance of the chart
        Description description = new Description();
        description.setText("Sơ số BMI của bé");
        lineChart.setDescription(description);

        // Chỉ số BMI 1 đến 35: bên trái
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(40f);

        // phần trăm: Bên phải
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(100f);

        //xét tháng trong năm
        XAxis xAxis_month = lineChart.getXAxis();
        String[] monthAbbreviations = {
                "T1", "T2", "T3", "T4", "T5", "T6",
                "T7", "T8", "T9", "T10", "T11", "T12"
        };
        xAxis_month.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_month.setAxisMinimum(0);
        xAxis_month.setAxisMaximum(11);
        xAxis_month.setValueFormatter(new IndexAxisValueFormatter(monthAbbreviations));

        // Refresh the chart
        lineChart.invalidate();
    }

    private void display_health_arlet_baby(Health health, int month) {
        double heigh = health.getHeight();
        double weight = health.getWeight();
        heigh = heigh / 100f;
        double BMI = weight / (heigh * heigh);
        health_arlet_month_now.setText("Tháng " + (month + 1) + " này");
        if (BMI < 16) {
            health_arlet_header.setText("Gầy độ III");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (16 <= BMI & BMI < 17) {
            health_arlet_header.setText("Gầy độ II");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (17 <= BMI & BMI < 18.5) {
            health_arlet_header.setText("Gầy độ I");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (18.5 <= BMI & BMI < 25) {
            health_arlet_header.setText("Bình thường");
            health_arlet_content.setText("Tình trạng cơ thể trong khoảng trọng lượng bình thường, bé rất khỏe mạnh sức khỏe.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_nomal);
            health_arlet.setBackground(drawable);
            health_arlet_image.setImageResource(R.drawable.normal);
        } else if (25 <= BMI & BMI < 30) {
            health_arlet_header.setText("Thừa cân");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (30 <= BMI & BMI < 35) {
            health_arlet_header.setText("Béo phì độ I");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (35 <= BMI & BMI < 40) {
            health_arlet_header.setText("Béo phì độ II");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (BMI >= 40) {
            health_arlet_header.setText("Béo phì độ III");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(BabyHealthActivity.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
    }
}