package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Customer;
import com.admin.babyvaccinationtracker.model.Vaccine_center;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class ManageCenterListAdapter extends RecyclerView.Adapter<ManageCenterListAdapter.viewholder> {
    List<Vaccine_center> list;
    Context context;
    private OnItemClickListener onItemClickListener;
    public ManageCenterListAdapter(List<Vaccine_center> list,Context context){
        this.list = list;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(Vaccine_center center);
    }
    public void setOnItemClickListener(ManageCenterListAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ManageCenterListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemuserformanage, parent, false);
        return new viewholder(view);
    }

    public void setData(List<Vaccine_center> newlist){
        this.list = newlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCenterListAdapter.viewholder holder, int position) {
        Vaccine_center center = list.get(position);
        holder.tv_center_name.setText(center.getCenter_name());
        holder.tv_center_email.setText(center.getCenter_email());
        holder.tv_manage_userid.setText(center.getCenter_id());
        if (center.getCenter_image() != null && !center.getCenter_image().isEmpty()) {
            String url = center.getCenter_image().contains("https") ? center.getCenter_image() : center.getCenter_image().replace("http", "https");
            Picasso.get().load(url).into(holder.imageViewAvatar, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Log.i("PICASSO", "success");
                }

                @Override
                public void onError(Exception e) {
                    holder.imageViewAvatar.setImageResource(R.drawable.user_default_avatar);
                }
            });
        }
        holder.tv_center_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(center!= null){
                   onItemClickListener.onItemClick(center);
               }
               else {
                   Log.i("CHECKKKK", center+"");
               }

            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_center_name;
        TextView tv_center_email, tv_manage_userid;

        ImageView tv_center_block, imageViewAvatar;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_center_name = itemView.findViewById(R.id.tv_manage_username);
            tv_center_email = itemView.findViewById(R.id.tv_manage_email);
            tv_center_block = itemView.findViewById(R.id.imageView_manage_Block);
            tv_manage_userid = itemView.findViewById(R.id.tv_manage_userid);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }
}
