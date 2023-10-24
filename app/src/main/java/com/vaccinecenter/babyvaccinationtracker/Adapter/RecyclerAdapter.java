package com.vaccinecenter.babyvaccinationtracker.Adapter;


import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaccinecenter.babyvaccinationtracker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;
    private Context context;
    private LinearLayout lastClickedImageView = null;

    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(ArrayList<Uri> uriArrayList, Context context) {
        this.uriArrayList = uriArrayList;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(Uri imageUri);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view  = layoutInflater.inflate(R.layout.custom_single_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Uri uri =  uriArrayList.get(position);
        String url = String.valueOf(uri);
        if(url.startsWith("/storage/emulated/0/")){
            holder.imageView.setImageURI(uri);
        }
        else {
            Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i("Success", "Load image success");
                }

                @Override
                public void onError(Exception e) {
                    Log.i("Error", e+"");
                }
            });
        }
        if(lastClickedImageView!=null){
            lastClickedImageView.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if(lastClickedImageView!=null){
                        lastClickedImageView.setBackgroundResource(0);
                    }
                    holder.custom_single_image_border.setBackgroundResource(R.drawable.image_boder);
                    lastClickedImageView = holder.custom_single_image_border;
                    onItemClickListener.onItemClick(uri);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout custom_single_image_border;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            custom_single_image_border = itemView.findViewById(R.id.custom_single_image_border);
        }
    }
}


