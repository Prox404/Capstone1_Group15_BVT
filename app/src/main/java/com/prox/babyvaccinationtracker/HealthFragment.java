package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.adapter.HealthBabyAdapter;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Health;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Context context;
    LinearLayout health_baby;
    ListView health_baby_list_view;
    TextView health_arlet_input;
    LineChart lineChart;
    Baby babychoose;
    HealthBabyAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    HashMap<Integer,Health> health = new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add any code you want to execute when the fragment is resumed
        health_arlet_input.setVisibility(View.GONE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        health_baby = view.findViewById(R.id.Button_health_baby);
        health_arlet_input = view.findViewById(R.id.health_arlet_input);
        // todo listview
        health_baby_list_view = view.findViewById(R.id.health_baby_list_view);

        context = container != null ? container.getContext() : null;

        health_arlet_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, heatlth_input_for_babies.class));
            }
        });
        // Lấy dữ liệu baby
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String babiesJson = sharedPreferences.getString("babiesList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Baby>>() {}.getType();
        List<Baby> babiesList = gson.fromJson(babiesJson, type);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("health_history");
        for (Baby baby : babiesList) {
            addButtonForBaby(baby);
        }
        Button firstButton = (Button) health_baby.getChildAt(0);
        firstButton.performClick();




        lineChart = view.findViewById(R.id.lineChart);

        // Inflate the layout for this fragment
        return view;

    }
    private void chartxyBMI(ArrayList<Entry> a){
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
//        String[] monthAbbreviations = {
//                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
//                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
//        };
        xAxis_month.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_month.setAxisMinimum(1);
        xAxis_month.setAxisMaximum(12);

        // Refresh the chart
        lineChart.invalidate();
    }
    private ArrayList<Entry> dataValues2(HashMap<Integer,Health> he) {
        ArrayList<Entry> values = new ArrayList<>();
        for(Map.Entry<Integer, Health> entry : he.entrySet()){
            float h = (float) entry.getValue().getHeight();
            float w = (float) entry.getValue().getWeight();
            h = h / 10f;
            float BMI = w / (h*h);
            int month = entry.getKey();
            values.add(new Entry(month+1, BMI));
        }
        return values;
    }
    private ArrayList<Entry> dataValues(int month, float BMI) {
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(month, BMI));
        return values;
    }
    private void addButtonForBaby(final Baby baby){
        Button button = new Button(context);
        button.setText(baby.getBaby_name());
        health_baby.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babychoose = baby;
                String id = babychoose.getBaby_id();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                databaseReference.child(id).child(""+year).addValueEventListener(new ValueEventListener() {
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
                            adapter = new HealthBabyAdapter(context,a);
                            Log.i("ADAAAPPTEERRR",adapter+"");
                            Log.i("HEALTH",""+a.get(0).getWeight());
                            health_baby_list_view.setAdapter(adapter);
                        }else {
                            Toast.makeText(context,"Chưa có dữ liệu vui lòng nhập các thông tin cơ bản cho bé",Toast.LENGTH_LONG).show();
                            health_arlet_input.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}