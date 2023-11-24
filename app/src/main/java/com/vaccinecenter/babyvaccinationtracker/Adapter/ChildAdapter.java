package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.Baby;
import com.vaccinecenter.babyvaccinationtracker.model.VaccinationCertificate;
import com.vaccinecenter.babyvaccinationtracker.view_child_information;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.listchild>{
    List<Baby> mlistchild;
    private Context context;

    public ChildAdapter (Context context, List<Baby> mlistchild){
        this.context = context;
        this.mlistchild = mlistchild;
    }

    @NonNull
    @Override
    public listchild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_child, parent, false);
        return new ChildAdapter.listchild(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listchild holder, int position) {
        final Baby baby = mlistchild.get(position);
        if (baby == null){
            return;
        }else{
            holder.txttenbe.setText(baby.getBaby_name());
            holder.txtgioitinh.setText(baby.getBaby_gender());
            holder.txtNgaySinh.setText(baby.getBaby_birthday());
            Picasso.get().load(baby.getBaby_avatar()).error(R.drawable.ic_launcher_foreground).into(holder.imageView_Baby_Avatar);
        }
        holder.child_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickgotoInfor(baby);
            }
        });
    }

    private void onClickgotoInfor(Baby baby){
        Intent intent = new Intent(context, view_child_information.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("baby", baby);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mlistchild != null){
            return mlistchild.size();
        }
        return 0;
    }

    public class listchild extends RecyclerView.ViewHolder{
        private TextView txttenbe;
        private TextView txtgioitinh, txtNgaySinh;
        private ImageView imageView_Baby_Avatar;
        private LinearLayout child_id;
        public listchild(@NonNull View itemView) {
            super(itemView);
            txttenbe = itemView.findViewById(R.id.txttenbe);
            txtgioitinh = itemView.findViewById(R.id.txtgioitinh);
            imageView_Baby_Avatar = itemView.findViewById(R.id.imageView_Baby_Avatar);
            child_id = itemView.findViewById(R.id.child_id);
            txtNgaySinh = itemView.findViewById(R.id.txtNgaySinh);
        }
    }
}
