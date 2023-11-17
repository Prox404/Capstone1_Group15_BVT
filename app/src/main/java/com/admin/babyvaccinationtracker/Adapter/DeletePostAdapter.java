package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.ArrayList;

public class DeletePostAdapter extends RecyclerView.Adapter<DeletePostAdapter.view > {
    ArrayList<Post> PostDete ;
    Context Deletepost;

    public DeletePostAdapter ( ArrayList<Post> PostDete, Context delete){
        this.PostDete=PostDete;
        this.Deletepost =delete;
    }
    @NonNull
    @Override
    public DeletePostAdapter.view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdeletepost, parent, false);
        return new view(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletePostAdapter.view holder, int position) {
        Post delteteP = PostDete.get(position);
    holder.tv_deletenumber.setText ((position +1)+"");
    holder.tv_user.setText(delteteP.getUser().getUser_name());
    holder.tv_content.setText(delteteP.getContent().toString());
    holder.bt_delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatabaseReference EditContent = FirebaseDatabase.getInstance().getReference("posts");
            EditContent.child(delteteP.getPost_id()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Deletepost, "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();

                }
            });

        }
    });
    }


    @Override
    public int getItemCount() {
        return PostDete.size();

    }

    public class view extends RecyclerView.ViewHolder {
        TextView tv_deletenumber, tv_user, tv_content ;
        ImageButton bt_delete;
        public view(@NonNull View itemView) {
            super(itemView);
            tv_deletenumber = itemView.findViewById(R.id.tv_deletenumber);
            tv_user = itemView.findViewById(R.id.tv_user);
            tv_content = itemView.findViewById(R.id.tv_content);
            bt_delete = itemView.findViewById(R.id.bt_delete);

        }
    }
}
