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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BlockUser extends AppCompatDialogFragment {
    TextView tv_name;
    TextView tv_email;
    EditText editText_reason;
    DatabaseReference database;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_blockuser, null);
        tv_name = view.findViewById(R.id.tv_block_name);
        tv_email = view.findViewById(R.id.tv_block_email);
        editText_reason = view.findViewById(R.id.edt_block_reason);

        Bundle args = getArguments();
        if (args != null) {
            String cus_name = args.getString("cus_name", "");
            String cus_email = args.getString("cus_email", "");
            tv_name.setText(cus_name);
            tv_email.setText(cus_email);
        }

        builder.setView(view).setTitle("Chặn người dùng").setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss(); // Close the dialog
            }
        }).setPositiveButton("Chặn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reason = editText_reason.getText().toString();
                if (reason.isEmpty()) {
                    Toast.makeText(getActivity(), "Phải nhập lý do chặn người dùng", Toast.LENGTH_LONG).show();
                } else {

                    String cus_id = args.getString("cus_id", "");
                    String cus_name = args.getString("cus_name", "");
                    String cus_email = args.getString("cus_email", "");

                    DatabaseReference blacklistData = FirebaseDatabase.getInstance().getReference("BlackList").child(cus_id);
                    BlackList blackList = new BlackList();
                    blackList.setCus_email(cus_email);
                    blackList.setCus_name(cus_name);
                    blackList.setReason(reason);
                    blacklistData.setValue(blackList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database = FirebaseDatabase.getInstance().getReference("users").child("customers").child(cus_id);
                            database.child("blocked").setValue(true);
                        }
                    });

                    dismiss();
                    Toast.makeText(getActivity(), "Chặn thành công", Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();

    }
}
