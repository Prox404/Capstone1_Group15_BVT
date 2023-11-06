package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;

import com.admin.babyvaccinationtracker.model.BlackList;

import java.util.List;
import java.util.zip.Inflater;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.viewholder> {
    List<BlackList> blackLists;
    Context context;

    public BlockUserAdapter(List<BlackList> blackLists, Context context){
        this.blackLists = blackLists;
        this.context = context;
    }
    @NonNull
    @Override
    public BlockUserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.item_block_user, parent, false);
        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BlockUserAdapter.viewholder holder, int position) {
        BlackList blackList = blackLists.get(position);
        holder.tv_name.setText(blackList.getCus_name());
        holder.tv_email.setText(blackList.getCus_email());
        holder.Reason.setText(blackList.getReason());
    }

    public void setData(List<BlackList> blackLists){
        this.blackLists = blackLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return blackLists.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_email;
        TextView Reason;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_manage_block_username);
            tv_email = itemView.findViewById(R.id.tv_manage_block_email);
            Reason = itemView.findViewById(R.id.tv_manage_block_reason);
        }
    }
}
