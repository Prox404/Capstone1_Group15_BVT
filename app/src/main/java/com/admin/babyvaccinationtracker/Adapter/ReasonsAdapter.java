package com.admin.babyvaccinationtracker.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;

import java.util.ArrayList;

public class ReasonsAdapter extends RecyclerView.Adapter<ReasonsAdapter.viewholder> {
    ArrayList<String> reasons;
    public ReasonsAdapter(ArrayList<String> reasons){
        this.reasons = reasons;
    }

    @NonNull
    @Override
    public ReasonsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reason, parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReasonsAdapter.viewholder holder, int position) {
        holder.textView.setText(reasons.get(position));
    }

    @Override
    public int getItemCount() {
        return reasons.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_reason);
        }
    }
}
