package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.BabyHealthActivity;
import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Baby;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BabyHealthAdapter extends RecyclerView.Adapter<BabyHealthAdapter.BabyViewHolder> {
    private List<Baby> babyList;
    private Context context;

    public BabyHealthAdapter(Context context, List<Baby> babyList) {
        this.context = context;
        this.babyList = babyList;
    }

    @NonNull
    @Override
    public BabyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.baby_item, parent, false);
        return new BabyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabyViewHolder holder, int position) {
        Baby baby = babyList.get(position);
        holder.tvBabyName.setText(baby.getBaby_name());
        holder.tvBabyBirthday.setText(baby.getBaby_birthday());
        holder.tvBabyGender.setText(baby.getBaby_gender());

        String imgaeUrl = baby.getBaby_avatar().contains("https") ? baby.getBaby_avatar() : baby.getBaby_avatar().replace("http", "https");
        // Load baby avatar using Picasso or any other image loading library
        Picasso.get().load(imgaeUrl)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivBabyAvatar);

        // onClickListener for each baby item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BabyHealthActivity.class);
                intent.putExtra("baby", baby);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return babyList.size();
    }

    public static class BabyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBabyName, tvBabyBirthday, tvBabyGender;
        ImageView ivBabyAvatar;

        public BabyViewHolder(View itemView) {
            super(itemView);
            ivBabyAvatar = itemView.findViewById(R.id.imageViewAvatar);
            tvBabyName = itemView.findViewById(R.id.textViewBabyName);
            tvBabyBirthday = itemView.findViewById(R.id.textViewBabyBirthday);
            tvBabyGender = itemView.findViewById(R.id.textViewBabyGender);
        }
    }
}
