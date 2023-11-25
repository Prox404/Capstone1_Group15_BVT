package com.admin.babyvaccinationtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.admin.babyvaccinationtracker.model.BlackList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BlockDeletePost extends AppCompatDialogFragment {
    TextView tv_name;
    TextView tv_id;
    EditText editText_reason;
    DatabaseReference database;
    String post_id = "";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_blockuser, null);
        tv_name = view.findViewById(R.id.tv_block_name);
        tv_id = view.findViewById(R.id.tv_block_id);
        editText_reason = view.findViewById(R.id.edt_block_reason);

        Bundle args = getArguments();
        if (args != null) {
            String cus_name = args.getString("user_name", "");
            String cus_id = args.getString("user_id", "");
            post_id = args.getString("post_id","");
            tv_name.setText(cus_name);
            tv_id.setText(cus_id);
        }

        builder.setView(view).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss(); // Close the dialog
            }
        }).setPositiveButton("Chặn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reason = editText_reason.getText().toString();
                if (reason.isEmpty()) {
                    Toast.makeText(getActivity(),
                            "Phải nhập lý do chặn người dùng",
                            Toast.LENGTH_LONG).show();
                } else {
                    int check = args.getInt("isCus");
                    String user_id = args.getString("user_id", "");
                    String user_name = args.getString("user_name", "");
                    database = FirebaseDatabase.getInstance()
                            .getReference("BlackList");
                    BlackList blackList = new BlackList();
                    blackList.setCus_name(user_name);
                    blackList.setReason(reason);
                    if(check == 1){
                        action_function("customers",blackList,user_id);
                    }else {
                        action_function("Vaccine_center", blackList, user_id);
                    }
                    dismiss();
                    Toast.makeText(getActivity(), "Chặn thành công", Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();

    }
    void action_function(String type_person, BlackList blackList, String user_id ){
        database.child(type_person).child(user_id).setValue(blackList)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(type_person)
                            .child(user_id);
                    database.child("blocked").setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database = FirebaseDatabase.getInstance()
                                            .getReference("posts");
                                    database.child(post_id).setValue(null)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // đối chiếu đến Report trên firebase
                                                    database = FirebaseDatabase.getInstance()
                                                            .getReference("Report");
                                                    Query query = database
                                                            .orderByChild("post/post_id")
                                                            .equalTo(post_id);
                                                    query.addListenerForSingleValueEvent
                                                            (new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange
                                                                (@NonNull DataSnapshot snapshot)
                                                        {
                                                            if(snapshot.exists()){
                                                                for(DataSnapshot s :
                                                                        snapshot.getChildren()){
                                                                    s.getRef().removeValue();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled
                                                                (@NonNull DatabaseError error)
                                                        {

                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                }
            });
    }
}
