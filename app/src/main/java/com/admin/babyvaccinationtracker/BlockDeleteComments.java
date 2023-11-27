package com.admin.babyvaccinationtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.admin.babyvaccinationtracker.model.BlackList;
import com.admin.babyvaccinationtracker.model.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BlockDeleteComments extends AppCompatDialogFragment {
    TextView tv_name;
    TextView tv_id;
    EditText editText_reason;
    DatabaseReference database;
    String post_id = "";
    String comment_id_big = "";
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
            comment_id_big = args.getString("comment_id","");
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
    void action_function(String type_person, BlackList blackList, String user_id){
        database.child(type_person)
                .child(user_id).setValue(blackList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database = FirebaseDatabase.getInstance().getReference("users")
                                .child(type_person)
                                .child(user_id);
                        database.child("blocked").setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Remove_report(comment_id_big);
                                database = FirebaseDatabase.getInstance().getReference("posts")
                                        .child(post_id);
                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        HashMap<String, Comment> comments = (HashMap<String, Comment>)
                                                snapshot.child("comments").getValue();
                                        if(comments != null){
                                            DataSnapshot snapshot_comment = snapshot.child("comments");
                                            if(comments.containsKey(comment_id_big)){
                                                snapshot_comment.child(comment_id_big).getRef().removeValue();

                                            }
                                            else {
                                                for(Map.Entry<String, Comment> entry : comments.entrySet()){
                                                    Object commentObject = entry.getValue();
                                                    Log.i("REPLIESSSSS", commentObject +"");
                                                    Log.i("KEYYYYYY", entry.getKey());
                                                    if(commentObject != null){
                                                        remove_comment(snapshot_comment, // đường dẫn để xóa comment
                                                                entry.getKey(), // child cha
                                                                comment_id_big, // comment cần xóa
                                                                (HashMap<String, Object>) commentObject);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                });


    }
    void Remove_report(String comment_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
        Query query = reference.orderByChild("comment/comment_id").equalTo(comment_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    boolean remove_comment(DataSnapshot snapshot_comment_child,
                           String comment_id,
                           String comment_delete,
                           HashMap<String, Object> Object){
        DataSnapshot snapshot_comment = snapshot_comment_child.child(comment_id);
        Object replies =  Object.get("replies");
        Log.i("replies_1", replies+"");
        if(replies instanceof HashMap && replies != null){
            if(((HashMap<String, Object>) replies).containsKey(comment_delete)){
                Log.i("replies_2", comment_delete + " "+ comment_id);
                Log.i("snapshot_comment", snapshot_comment+"");
                snapshot_comment.child("replies").child(comment_delete).getRef().removeValue();
                return true;
            }
            else {
                for(Map.Entry<String, Object> entry: ((HashMap<String, Object>) replies).entrySet()){
                    Object object = entry.getValue();
                    Log.i("snapshot_replies", object+"");
                    if(object != null && object instanceof HashMap){
                        DataSnapshot snapshot_comment2 = snapshot_comment_child
                                .child(comment_id).child("replies");
                        if(remove_comment(snapshot_comment2,
                                entry.getKey(),
                                comment_delete,
                                (HashMap<String, java.lang.Object>) object)){
                            return true;
                        };
                    }
                }
            }
        }
        return false;
    }

}
