package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.BabyProfileActivity;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Baby;

import java.lang.reflect.Type;
import java.util.List;

public class BabyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Baby> mBabies;

    private boolean checkLongClick = false;

    private ImageView imageViewBaby_delete_root;
    private LinearLayout backgroud_baby_root;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public BabyGridViewAdapter(Context context, List<Baby> babies) {
        mContext = context;
        mBabies = babies;
    }

    @Override
    public int getCount() {
        return mBabies.size();
    }

    @Override
    public Object getItem(int position) {
        return mBabies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if(gridViewItem == null) {
            gridViewItem = LayoutInflater.from(mContext).inflate(R.layout.baby_card_item, parent, false);
        }

        Baby baby = mBabies.get(position);

        TextView txtName = gridViewItem.findViewById(R.id.textViewBabyName);
        TextView txtBirthday = gridViewItem.findViewById(R.id.textViewBirthday);
        ImageView imageViewBaby_delete = gridViewItem.findViewById(R.id.imageViewBaby_delete);
        LinearLayout backgroud_baby = gridViewItem.findViewById(R.id.backgroud_baby);


        // Set baby data
        txtName.setText(baby.getBaby_name());
        txtBirthday.setText(baby.getBaby_birthday());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String UserID = sharedPreferences.getString("customer_id", "");

        imageViewBaby_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBabies.size() > 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Cảnh báo !")
                            .setMessage("Bạn có muốn xóa bé này không")
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    imageViewBaby_delete_root.setVisibility(View.GONE);
                                    backgroud_baby_root.setBackgroundResource(R.color.white);
                                    dialogInterface.cancel();
                                }
                            }).setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference reference_user = firebaseDatabase
                                            .getReference("users")
                                            .child("customers")
                                            .child(UserID)
                                            .child("babies")
                                            .child(baby.getBaby_id());
                                    reference_user.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            DatabaseReference vaccination_regimen =
                                                    firebaseDatabase
                                                            .getReference("vaccination_regimen");
                                            vaccination_regimen
                                                    .child(baby.getBaby_id())
                                                    .setValue(null)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    DatabaseReference heath = firebaseDatabase
                                                            .getReference("health")
                                                            .child(baby.getBaby_id());
                                                    heath.setValue(null)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            DatabaseReference check_list =
                                                                    firebaseDatabase
                                                                            .getReference("check_list")
                                                                            .child(baby.getBaby_id());
                                                            check_list.setValue(null)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    DatabaseReference notifications = firebaseDatabase.getReference("notifications");
                                                                    Query query = notifications.orderByChild("baby_id").equalTo(baby.getBaby_id());
                                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if(snapshot != null){
                                                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                                    dataSnapshot.getRef().removeValue();
                                                                                }
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    String babiesJson = sharedPreferences.getString("babiesList", "");
                                                                    Gson gson = new Gson();
                                                                    Type type = new TypeToken<List<Baby>>() {
                                                                    }.getType();
                                                                    List<Baby> babiesList = gson.fromJson(babiesJson, type);
                                                                    for(int i = 0 ; i < babiesList.size() ; i ++){
                                                                        if(babiesList.get(i).getBaby_id().equals(baby.getBaby_id())){
                                                                            babiesList.remove(i);
                                                                            break;
                                                                        }
                                                                    }
                                                                    babiesJson = gson.toJson(babiesList);
                                                                    editor.putString("babiesList", babiesJson);
                                                                    editor.apply();
                                                                    mBabies.remove(position);
                                                                    notifyDataSetChanged();

                                                                    checkLongClick = false;
                                                                    imageViewBaby_delete_root.setVisibility(View.GONE);
                                                                    backgroud_baby_root.setBackgroundResource(R.color.white);
                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                    builder.create().show();
                }else if(mBabies.size() == 1) {
                    Toast.makeText(view.getContext(), "Ít nhất phải có một bé trong tài khoản của bạn", Toast.LENGTH_LONG).show();
                }
            }
        });

        gridViewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(imageViewBaby_delete_root == null && backgroud_baby_root == null){
                    imageViewBaby_delete_root = imageViewBaby_delete;
                    backgroud_baby_root = backgroud_baby;

                }else {
                    imageViewBaby_delete_root.setVisibility(View.GONE);
                    backgroud_baby_root.setBackgroundResource(R.color.white);
                    imageViewBaby_delete_root = imageViewBaby_delete;
                    backgroud_baby_root = backgroud_baby;
                }
                imageViewBaby_delete.setVisibility(View.VISIBLE);
                backgroud_baby.setBackgroundResource(R.color.gray_400);
                checkLongClick = true;

                return false;
            }
        });

        gridViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkLongClick){
                    Intent intent = new Intent(mContext, BabyProfileActivity.class);
                    intent.putExtra("baby_id", baby.getBaby_id());
                    intent.putExtra("cus_id", UserID);
                    mContext.startActivity(intent);
                }else {
                    imageViewBaby_delete_root.setVisibility(View.GONE);
                    backgroud_baby_root.setBackgroundResource(R.color.white);
                    checkLongClick = false;
                }

            }
        });

        return gridViewItem;
    }

}