package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Regimen;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.RegimenViewHolder> {
    private Context context;
    private List<Regimen> regimenList;
    public int highlightedPosition = -1;

    public TimeLineAdapter(Context context, List<Regimen> regimenList) {
        this.context = context;
        this.regimenList = regimenList;
        highlightClosestRegimen(new Date());
    }

    @Override
    public RegimenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        return new RegimenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RegimenViewHolder holder, int position) {
        Regimen regimen = regimenList.get(position);

        holder.contentTextView.setText(regimen.getContent());
        holder.dateTextView.setText(formatDate(regimen.getDate()));
        holder.radioButtonVaccinated.setClickable(false);

        holder.radioButtonVaccinated.setChecked(regimen.isVaccinated());

        if (position == highlightedPosition) {
            Log.i("Bind", "onBindViewHolder: " + highlightedPosition);
            holder.itemContainer.setBackgroundColor(context.getResources().getColor(R.color.colorSelected));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.timeline_item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return regimenList.size();
    }

    public class RegimenViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView dateTextView;
        RadioButton radioButtonVaccinated;
        LinearLayout itemContainer;
        // Define other TextViews for additional properties

        public RegimenViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            itemContainer = itemView.findViewById(R.id.itemContainer);
            radioButtonVaccinated = itemView.findViewById(R.id.radioButtonVaccinated);
            // Initialize other TextViews
        }
    }

    // You can define your own date formatting logic here
    private String formatDate(Date date) {
        // Implement your date formatting logic
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sdf.format(date);
        return strDate;
    }

    public void highlightClosestRegimen(Date currentDate) {
        int closestPosition = -1;
        Date closestDate = null;

        for (int i = 0; i < regimenList.size(); i++) {
            Date regimenDate = regimenList.get(i).getDate();

            if (regimenDate.before(currentDate) || regimenDate.equals(currentDate)) {
                if (closestDate == null || closestDate.before(regimenDate)) {
                    closestDate = regimenDate;
                    closestPosition = i;
                }
            }
        }

        Log.i("Huuuuuu", "highlightClosestRegimen: " + closestPosition + " " + closestDate + " " + currentDate);

        if (closestPosition != -1) {
            highlightedPosition = closestPosition;
            notifyDataSetChanged(); // Cập nhật giao diện để áp dụng highlight
        }
    }
}