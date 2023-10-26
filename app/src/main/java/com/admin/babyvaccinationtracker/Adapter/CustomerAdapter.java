package com.admin.babyvaccinationtracker.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.SendNotificationActivity;
import com.admin.babyvaccinationtracker.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList;
    private List<String> selectedCustomers = new ArrayList<>();

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
        this.selectedCustomers = SendNotificationActivity.selectedCustomers;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView emailTextView;
        private TextView phoneTextView;
        private TextView addressTextView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
        }

        public void bind(Customer customer) {
            Log.i("Adaper", "bind: " + customer.toString());
            nameTextView.setText(customer.getCus_name());
            emailTextView.setText(customer.getCus_email());
            phoneTextView.setText(customer.getCus_phone());
            addressTextView.setText(customer.getCus_address());

            if (selectedCustomers.contains(customer.getCustomer_id())) {
                // Thay đổi giao diện người dùng (ví dụ: thay đổi màu nền)
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.primarySelectedColor));
            } else {
                // Reset giao diện người dùng (ví dụ: trả về màu nền mặc định)
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedCustomers.contains(customer.getCustomer_id())) {
                        selectedCustomers.remove(customer.getCustomer_id());
                    } else {
                        selectedCustomers.add(customer.getCustomer_id());
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}

