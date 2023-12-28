package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Health;

import java.util.ArrayList;

public class HealthBabyAdapter  extends RecyclerView.Adapter<HealthBabyAdapter.viewholer> {
    private ArrayList<Health> healthItems;
    private String baby_id;

    public HealthBabyAdapter( ArrayList<Health> healthItems, String baby_id) {
        this.healthItems = healthItems;
        this.baby_id = baby_id;
    }

    @NonNull
    @Override
    public viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_health_baby, parent, false);
        return new viewholer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholer holder, int position) {
        Health health = healthItems.get(position);
        // todo kiểm tra tháng
        String[] m = health.getHealthCreated_at().split(" ");

        Calendar calendar = Calendar.getInstance();
        int current_month = calendar.get(Calendar.MONTH);
        int month_health = getMonthIndex(m[1]);
        String year_health = m[5];

        if(current_month == month_health){
            holder.heallth_month.setText("Tháng này");
            if (health.getCustomer_response() != null && !health.getCustomer_response().isEmpty()) {
                holder.responseContainer.setVisibility(View.VISIBLE);
                holder.textViewCustomerResponse.setText(health.getCustomer_response());
            } else {
                holder.textViewCustomerResponse.setVisibility(View.GONE);
                holder.responseContainer.setVisibility(View.VISIBLE);
                holder.sendResponseContainer.setVisibility(View.VISIBLE);
                holder.buttonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String response = holder.editTextCustomerResponse.getText().toString().trim();
                        if (response != null && !response.isEmpty()) {
                            DatabaseReference healthRef = FirebaseDatabase.getInstance().getReference("health").child(baby_id).child(year_health).child(""+month_health);
                            healthRef.child("customer_response").setValue(response).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Gửi phản hồi thành công", Toast.LENGTH_SHORT).show();
                                    holder.responseContainer.setVisibility(View.VISIBLE);
                                    holder.sendResponseContainer.setVisibility(View.GONE);
                                    holder.textViewCustomerResponse.setVisibility(View.VISIBLE);
                                    holder.textViewCustomerResponse.setText(response);
                                    health.setCustomer_response(response);
                                }
                            });
                        }else{
                            holder.editTextCustomerResponse.setError("Vui lòng nhập phản hồi");
                        }
                    }
                });
            }
        }
        else{
            holder.heallth_month.setText((current_month-month_health)+" tháng trước");
            if (health.getCustomer_response() != null && !health.getCustomer_response().isEmpty()) {
                holder.responseContainer.setVisibility(View.VISIBLE);
                holder.textViewCustomerResponse.setText(health.getCustomer_response());
            }
        }
        holder.height.setText(""+health.getHeight());
        holder.sleep.setText(""+health.getSleep());
        holder.weight.setText(""+health.getWeight());
    }

    private int getMonthIndex(String monthAbbreviation) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthAbbreviation)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return healthItems.size();
    }


    public class viewholer extends RecyclerView.ViewHolder {
        TextView height;
        TextView weight;
        TextView sleep;
        TextView heallth_month,textViewCustomerResponse;
        Button buttonSend;
        View sendResponseContainer, responseContainer;
        EditText editTextCustomerResponse;
        public viewholer(@NonNull View itemView) {
            super(itemView);

            height = itemView.findViewById(R.id.health_height_item);
            weight = itemView.findViewById(R.id.health_weight_item);
            sleep = itemView.findViewById(R.id.health_sleep_item);
            heallth_month = itemView.findViewById(R.id.heallth_month);
            responseContainer = itemView.findViewById(R.id.responseContainer);
            sendResponseContainer = itemView.findViewById(R.id.sendResponseContainer);
            textViewCustomerResponse = itemView.findViewById(R.id.textViewCustomerResponse);
            buttonSend = itemView.findViewById(R.id.buttonSend);
            editTextCustomerResponse = itemView.findViewById(R.id.editTextCustomerResponse);
        }
    }
}
