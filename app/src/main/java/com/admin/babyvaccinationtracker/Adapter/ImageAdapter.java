package com.admin.babyvaccinationtracker.Adapter;

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

import com.admin.babyvaccinationtracker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.viewholder> {
    private ArrayList<Uri> uriArrayList;
    private Context context;
    private LinearLayout lastClickedImageView = null;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Uri imageUri);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public ImageAdapter (ArrayList<Uri> uriArrayList){
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public ImageAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view  = layoutInflater.inflate(R.layout.post_image_item, parent, false);
        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.viewholder holder, int position) {
        Uri uri =  uriArrayList.get(position);
        String url = String.valueOf(uri);
        if(url.startsWith("http://res.cloudinary.com/") || url.startsWith("https://res.cloudinary.com/")){
            Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(holder.imageViewPost, new Callback() {
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
        else {
            holder.imageViewPost.setImageURI(uri);
        }
        if(lastClickedImageView!=null){
            lastClickedImageView.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(uri);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageViewPost;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
        }
    }
}
