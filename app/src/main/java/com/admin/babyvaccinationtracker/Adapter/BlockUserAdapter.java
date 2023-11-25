package com.admin.babyvaccinationtracker.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;

import com.admin.babyvaccinationtracker.model.BlackList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.viewholder> {
    List<BlackList> blackLists;
    Boolean isCustomer;

    public BlockUserAdapter(List<BlackList> blackLists, Boolean check){
        this.blackLists = blackLists;
        this.isCustomer = check;
    }
    @NonNull
    @Override
    public BlockUserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_block_user, parent, false);
        return new viewholder(view);

    }

    public void setCheckisCustomer(Boolean check){
        this.isCustomer = check;
    }

    @Override
    public void onBindViewHolder(@NonNull BlockUserAdapter.viewholder holder, int position) {
        BlackList blackList = blackLists.get(position);
        String id_user = blackList.getBlacklist_id();
        holder.tv_name.setText(blackList.getCus_name());
        holder.tv_id.setText(id_user);
        holder.Reason.setText(blackList.getReason());

        holder.btn_unlock_for_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Câu hỏi")
                        .setMessage("Bạn có chắc muốn nhả người vào động của bạn không")
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference reference_user = firebaseDatabase.getReference("BlackList");
                                if(isCustomer){
                                    reference_user.child("customers").child(id_user).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            DatabaseReference reference_user = firebaseDatabase.getReference("users");
                                            reference_user.child("customers").child(id_user).child("blocked").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(view.getContext(),"Mở khóa thành công", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }else {
                                    reference_user.child("Vaccine_center").child(id_user).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            DatabaseReference reference_user = firebaseDatabase.getReference("users");
                                            reference_user.child("Vaccine_center").child(id_user).child("blocked").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(view.getContext(),"Mở khóa thành công", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        });
                builder.show();

            }
        });
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
        TextView tv_id;
        TextView Reason;
        Button btn_unlock_for_user;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_manage_block_username);
            tv_id = itemView.findViewById(R.id.tv_manage_block_id);
            Reason = itemView.findViewById(R.id.tv_manage_block_reason);
            btn_unlock_for_user = itemView.findViewById(R.id.btn_unlock_for_user);
        }
    }
}
