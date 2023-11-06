package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Customer;

import java.util.List;

public class ManageUserListViewAdapter extends RecyclerView.Adapter<ManageUserListViewAdapter.viewholder> {
    private List<Customer> listCustomer;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public ManageUserListViewAdapter(Context context, List<Customer> listCustomer){
        this.listCustomer = listCustomer;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ManageUserListViewAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemuserformanage, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUserListViewAdapter.viewholder holder, int position) {
        Customer customer = listCustomer.get(position);
        String cus_name = customer.getCus_name();
        String cus_email = customer.getCus_email();

        holder.tv_manage_username.setText(cus_name);
        holder.tv_manage_email.setText(cus_email);
        holder.imageView_manage_Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onItemClickListener.onItemClick(customer);
            }
        });
    }
    public void setData(List<Customer> data) {
        this.listCustomer = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listCustomer.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_manage_username;
        TextView tv_manage_email;
        ImageView imageView_manage_Block;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_manage_username = itemView.findViewById(R.id.tv_manage_username);
            tv_manage_email = itemView.findViewById(R.id.tv_manage_email);
            imageView_manage_Block = itemView.findViewById(R.id.imageView_manage_Block);
        }
    }
}
