package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ImageCarouselViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public ImageCarouselAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageCarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_image_item, parent, false);
        return new ImageCarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageCarouselViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageCarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageCarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPost);
        }
    }
}
