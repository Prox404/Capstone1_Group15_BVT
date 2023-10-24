package com.prox.babyvaccinationtracker.Adapter;

import android.annotation.SuppressLint;
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

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.See_detailed_vaccine_information;
import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.listvacine>{
    List<Vaccines> mlistvaccine;
    private Context mcontext;

    public VaccineAdapter(Context context, List<Vaccines> mlistvaccine) {
        this.mlistvaccine = mlistvaccine;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public listvacine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_vaccine, parent, false);
        return new listvacine(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listvacine holder, int position) {
        final Vaccines vaccines = mlistvaccine.get(position);
        if (vaccines == null){
            return;
        }else{
            holder.txtten.setText(vaccines.getVaccine_name());
            holder.txtgia.setText(vaccines.getPrice());
            Picasso.get().load(vaccines.getVaccine_image().get(0)).error(R.drawable.ic_launcher_foreground).into(holder.image_view_search_vaccine);
        }
        holder.vaccine_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickgotoInfor(vaccines);
            }
        });
    }
    private void onClickgotoInfor(Vaccines vaccines){
        Intent intent = new Intent(mcontext, See_detailed_vaccine_information.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vaccine_name", vaccines);
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mlistvaccine != null){
            return mlistvaccine.size();
        }
        return 0;
    }

    public class listvacine extends RecyclerView.ViewHolder{
        private TextView txtten;
        private TextView txtgia;
        private ImageView image_view_search_vaccine;
        private LinearLayout vaccine_id;

        public listvacine(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.txtten);
            txtgia = itemView.findViewById(R.id.txtgia);
            image_view_search_vaccine = itemView.findViewById(R.id.image_view_search_vaccine);
            vaccine_id = itemView.findViewById(R.id.vaccine_id);
        }
    }
}


//class informationadapter extends RecyclerView.Adapter<informationadapter.informationviewholder> {
//    List<Vaccines> mlistinformation;
//
//    public informationadapter(List<Vaccines> mlistinformation) {
//        this.mlistinformation = mlistinformation;
//    }
//
//    @NonNull
//    @Override
//    public informationviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.information_vaccine, parent, false);
//        return new informationviewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull informationviewholder holder, @SuppressLint("RecyclerView") int position) {
//        Vaccines vaccines = mlistinformation.get(position);
//        String imageUrl = String.valueOf(vaccines.getVaccine_image());
//        if (vaccines == null){
//            return;
//        }else{
//            Picasso.get()
//                    .load(imageUrl)
//                    .into(holder.large_image);
//            Picasso.get()
//                    .load(imageUrl)
//                    .into((Target) holder.small_image);
//            holder.txt_ten.setText(vaccines.getVaccine_name());
//            holder.txt_nhasx.setText(vaccines.getOrigin());
//            holder.txt_chucnang.setText(vaccines.getVac_effectiveness());
//            holder.txt_chongchidinh.setText(vaccines.getContraindications());
//            holder.txt_tacdungphu.setText(vaccines.getPost_vaccination_reactions());
//            holder.txt_doituongsudung.setText(vaccines.getVaccination_target_group());
//            holder.txt_gia.setText(vaccines.getPrice());
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mlistinformation != null){
//            return mlistinformation.size();
//        }
//        return 0;
//    }
//
//    public class informationviewholder extends RecyclerView.ViewHolder{
//        private ImageView large_image;
//        private LinearLayout small_image;
//        private TextView txt_ten;
//        private TextView txt_nhasx;
//        private TextView txt_thanhphan;
//        private TextView txt_chucnang;
//        private TextView txt_chongchidinh;
//        private TextView txt_tacdungphu;
//        private TextView txt_cachsudung;
//        private TextView txt_baoquan;
//        private TextView txt_doituongsudung;
//        private TextView txt_gia;
//        private TextView txt_hansudung;
//        private TextView txt_ghichu;
//
//        public informationviewholder(@NonNull View itemView) {
//            super(itemView);
//            large_image = itemView.findViewById(R.id.large_image);
//            small_image = itemView.findViewById(R.id.small_image);
//            txt_ten = itemView.findViewById(R.id.txt_ten);
//            txt_nhasx = itemView.findViewById(R.id.txt_nhasx);
//            txt_thanhphan = itemView.findViewById(R.id.txt_thanhphan);
//            txt_chucnang = itemView.findViewById(R.id.txt_chucnang);
//            txt_chongchidinh = itemView.findViewById(R.id.txt_chongchidinh);
//            txt_tacdungphu = itemView.findViewById(R.id.txt_tacdungphu);
//            txt_cachsudung = itemView.findViewById(R.id.txt_cachsudung);
//            txt_baoquan = itemView.findViewById(R.id.txt_baoquan);
//            txt_doituongsudung = itemView.findViewById(R.id.txt_doituongsudung);
//            txt_gia = itemView.findViewById(R.id.txt_gia);
//            txt_hansudung = itemView.findViewById(R.id.txt_hansudung);
//            txt_ghichu = itemView.findViewById(R.id.txt_ghichu);
//        }
//    }
//}


