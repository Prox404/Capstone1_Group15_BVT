package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.geo.province;

import java.util.List;

public class ProvinceAdapter extends ArrayAdapter<province> {

    public ProvinceAdapter(Context context, List<province> provinces) {
        super(context, 0, provinces);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        province province = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textViewProvinceName = convertView.findViewById(android.R.id.text1);

        // Đặt thông tin vào TextView
        textViewProvinceName.setText(province.getName());

        // Các dòng khác để đặt thông tin về tỉnh/thành phố

        return convertView;
    }
}
