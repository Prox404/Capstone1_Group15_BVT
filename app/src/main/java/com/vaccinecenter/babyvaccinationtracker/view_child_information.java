package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.Adapter.CertificateAdapter;
import com.vaccinecenter.babyvaccinationtracker.Adapter.CompletedRequestAdapter;
import com.vaccinecenter.babyvaccinationtracker.Adapter.HealthBabyAdapter;
import com.vaccinecenter.babyvaccinationtracker.Adapter.RecyclerAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Baby;
import com.vaccinecenter.babyvaccinationtracker.model.Customer;
import com.vaccinecenter.babyvaccinationtracker.model.Health;
import com.vaccinecenter.babyvaccinationtracker.model.VaccinationCertificate;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccination_Registration;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class view_child_information extends AppCompatActivity {

    LineChart lineChart;
    ImageView imageView_Baby_Avatar;
    TextView textView_Name, textView_Date_Of_Birth, textView_Congenital_Disease, textView_Gender;
    RecyclerView recyclerViewCertificate;
    Baby baby;
    View emptyLayout;
    HashMap<Integer, Health> health = new HashMap<>();
    RecyclerView health_baby_list_view;
    HealthBabyAdapter adapter;
    LinearLayout health_baby,health_arlet;
    TextView health_arlet_month_now,health_arlet_header,health_arlet_content;
    ImageView health_arlet_image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child_information);
        imageView_Baby_Avatar = findViewById(R.id.imageView_Baby_Avatar);
        textView_Name = findViewById(R.id.textView_Name);
        textView_Date_Of_Birth = findViewById(R.id.textView_Date_Of_Birth);
        textView_Congenital_Disease = findViewById(R.id.textView_Congenital_Disease);
        textView_Gender = findViewById(R.id.textView_Gender);
        recyclerViewCertificate = findViewById(R.id.recyclerViewCertificate);
        emptyLayout = findViewById(R.id.emptyLayout);
        health_baby_list_view = findViewById(R.id.health_baby_list_view);
        health_arlet = findViewById(R.id.health_arlet);
        health_arlet_month_now = findViewById(R.id.health_arlet_month_now);
        health_arlet_header = findViewById(R.id.health_arlet_header);
        health_arlet_content = findViewById(R.id.health_arlet_content);
        health_arlet_image = findViewById(R.id.health_arlet_image);
        lineChart = findViewById(R.id.lineChart);

        lineChart.setNoDataText("Chưa có dữ liệu");
        lineChart.setNoDataTextColor(R.color.textColor);
        lineChart.getLegend().setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng baby từ Bundle
            baby = (Baby) bundle.getSerializable("baby");
            if (baby != null) {

                Picasso.get().load(baby.getBaby_avatar()).into(imageView_Baby_Avatar);
                textView_Name.setText(baby.getBaby_name());
                textView_Date_Of_Birth.setText(baby.getBaby_birthday());
                textView_Gender.setText(baby.getBaby_gender());
                textView_Congenital_Disease.setText(baby.getBaby_congenital_disease());

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                Query query = databaseReference.orderByChild("baby/baby_id").equalTo(baby.getBaby_id());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Vaccination_Registration> vaccination_registrations = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                            vaccination_registration.setRegister_id(dataSnapshot.getKey());
                            if (vaccination_registration.getStatus() == 3)
                                vaccination_registrations.add(vaccination_registration);
                        }
                        CertificateAdapter pendingRequestAdapter = new CertificateAdapter( view_child_information.this , vaccination_registrations);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view_child_information.this);
                        recyclerViewCertificate.setAdapter(pendingRequestAdapter);
                        recyclerViewCertificate.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                getBabyHealth(baby.getBaby_id());
            }
        }

    }
    private void getBabyHealth(String baby_id){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("health");

        databaseReference.child(baby_id).child(""+year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    health.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Health h = dataSnapshot.getValue(Health.class);
                        if(h != null){
                            int k = Integer.parseInt(dataSnapshot.getKey());
                            health.put(k,h);
                        }
                    }
                    chartxyBMI(dataValues2(health));
                    ArrayList<Health> a = new ArrayList<>(health.values());
                    adapter = new HealthBabyAdapter(reverse_arraylist(a));
                    health_baby_list_view.setLayoutManager(new GridLayoutManager(view_child_information.this,1));
                    health_baby_list_view.setAdapter(adapter);
                    //todo arlet
                    Log.i("MONTHHHH",""+month);
                    if(health.containsKey(month)){
                        health_arlet.setVisibility(View.VISIBLE);
                        display_health_arlet_baby(health.get(month), month);

                    }

                }else {
                    Toast.makeText(view_child_information.this,"Chưa có dữ liệu vui lòng nhập các thông tin cơ bản cho bé",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chartxyBMI(ArrayList<Entry> a){

        LineDataSet lineDataSet = new LineDataSet(a, "Chỉ số BMI");
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(0xFF007F20);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(0xFF007F20);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        // Chỉ số BMI 1 đến 35: bên trái
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(40f);

        // phần trăm: Bên phải
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
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
        xAxis_month.setTextSize(13f);

        // Refresh the chart
        lineChart.invalidate();
    }
    public ArrayList<Entry> dataValues2(HashMap<Integer,Health> he) {
        ArrayList<Entry> values = new ArrayList<>();
        for(Map.Entry<Integer, Health> entry : he.entrySet()){
            float h = (float) entry.getValue().getHeight();
            float w = (float) entry.getValue().getWeight();
            h = h / 100f;
            float BMI = w / (h*h);
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

    private void display_health_arlet_baby(Health health, int month){
        double heigh = health.getHeight();
        double weight = health.getWeight();
        heigh = heigh / 100f;
        double BMI = weight / (heigh * heigh);
        health_arlet_month_now.setText("Tháng "+(month + 1)+" này");
        if(BMI <16){
            health_arlet_header.setText("Gầy độ III");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
        else if(16 <= BMI & BMI <17){
            health_arlet_header.setText("Gầy độ II");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
        else if(17 <= BMI & BMI <18.5){
            health_arlet_header.setText("Gầy độ I");
            health_arlet_content.setText("Tình trạng dưới trọng lượng có thể gây nguy cơ thiếu dinh dưỡng và yếu đuối.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
        else if (18.5 <= BMI & BMI <25){
            health_arlet_header.setText("Bình thường");
            health_arlet_content.setText("Tình trạng cơ thể trong khoảng trọng lượng bình thường, bé rất khỏe mạnh sức khỏe.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_nomal);
            health_arlet.setBackground(drawable);
            health_arlet_image.setImageResource(R.drawable.normal);
        }
        else if(25 <= BMI & BMI <30){
            health_arlet_header.setText("Thừa cân");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
        else if(30 <= BMI & BMI < 35){
            health_arlet_header.setText("Béo phì độ I");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (35 <= BMI & BMI < 40) {
            health_arlet_header.setText("Béo phì độ II");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        } else if (BMI >= 40) {
            health_arlet_header.setText("Béo phì độ III");
            health_arlet_content.setText("Thừa cân có thể gây ra những ảnh hưởng tiêu cực của bé trong tương lại như tăng nguy cơ bị nhiều bệnh lý như tiểu đường, tăng huyết áp, và bệnh tim mạch.");
            Drawable drawable = ContextCompat.getDrawable(view_child_information.this, R.drawable.arlet_warning);
            health_arlet.setBackground(drawable);
        }
    }
    private ArrayList<Health> reverse_arraylist(ArrayList<Health> h){
        int n = h.size();
        ArrayList<Health> new_arraylist = new ArrayList<>();
        for(int i = n - 1; i >= 0 ; i --){
            new_arraylist.add(h.get(i));
        }
        return new_arraylist;
    }
}