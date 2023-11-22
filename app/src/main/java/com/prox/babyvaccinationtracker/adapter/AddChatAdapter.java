package com.prox.babyvaccinationtracker.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.ChatActivity;
import com.prox.babyvaccinationtracker.ChatActivity_user;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Conversation;
import com.prox.babyvaccinationtracker.model.Message;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddChatAdapter extends RecyclerView.Adapter<AddChatAdapter.viewholder> {
    List<Vaccine_center> mlistvaccinecenter;
    Context context;
    String conversation_id = null;
    List<Conversation> conversations = new ArrayList<>();


    public AddChatAdapter(Context context, List<Vaccine_center> mlistvaccinecenter) {
        this.mlistvaccinecenter = mlistvaccinecenter;
        this.context = context;
    }
    @NonNull
    @Override
    public AddChatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vaccine_center_item, parent, false);
        return new viewholder(view);
    }
    public void setCenters(ArrayList<Vaccine_center> vaccine_centers){
        this.mlistvaccinecenter = vaccine_centers;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull AddChatAdapter.viewholder holder, int position) {
        Vaccine_center vaccineCenter = mlistvaccinecenter.get(position);
        String center_name = vaccineCenter.getCenter_name();
        holder.chat_txtten.setText(center_name);
        holder.chat_txtdichi.setText(vaccineCenter.getCenter_address());
        holder.chat_txtworktime.setText(vaccineCenter.getWork_time());
        holder.chat_hotline.setText(vaccineCenter.getHotline());

        String image_center =  vaccineCenter.getCenter_image().toString();
        Picasso.get().load(image_center).into(holder.chat_image_center);

        String center_id = vaccineCenter.getCenter_id();
        Log.i("CENTER_IDDDDDD", center_id);
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("customer_id", "");


        DatabaseReference chatUserRef = FirebaseDatabase.getInstance().getReference("chat_users").child(user_id);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("chat");
        chatUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String conversation_id = dataSnapshot.getKey();
                        databaseRef.child(conversation_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot_1) {
                                Conversation conversation = snapshot_1.getValue(Conversation.class);
                                conversation.setConversation_id(conversation_id);
                                conversations.add(conversation);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isExist = false;
                if(!conversations.isEmpty()){
                    for (Conversation conversation : conversations) {
                        Log.i("chat with bot", "onClick: " + conversation.getConversation_id());
                        Map<String, Boolean> users = conversation.getUsers();

                        if (users.containsKey(center_id)) {
                            isExist = true;
                            conversation_id = conversation.getConversation_id();
                            break;
                        }
                    }
                }

                if(isExist){
                    Intent intent = new Intent(context, ChatActivity_user.class);
                    intent.putExtra("Center_ID",center_id );
                    intent.putExtra("conversation_id", conversation_id);
                    view.getContext().startActivity(intent);
                }else{
                    Map<String, Boolean> users = new HashMap<>();
                    users.put(center_id, true);
                    users.put(user_id, true);
                    conversation_id = databaseRef.push().getKey();
                    if (conversation_id != null) {
                        DatabaseReference chatCenterRef = FirebaseDatabase.getInstance().getReference("chat_users").child(center_id);
                        chatUserRef.child(conversation_id).setValue(true);
                        chatCenterRef.child(conversation_id).setValue(true);

                        Conversation c = new Conversation();

                        Message message = new Message(center_name, "Chào bạn, tôi có thể giúp gì cho bạn?");
                        String messageKey = databaseRef.child(conversation_id).child("messages").push().getKey();
                        HashMap<String, Message> messages = new HashMap<>();

                        if(messageKey != null){
                            messages.put(messageKey,message);
                            c.setMessages(messages);
                        }
                        c.setUsers(users);
                        databaseRef.child(conversation_id).setValue(c);

                        Intent intent = new Intent(context, ChatActivity_user.class);
                        intent.putExtra("conversation_id",conversation_id);
                        view.getContext().startActivity(intent);
                    } else {
                        // Handle the case where conversation_id is null
                        Log.e("chat", "Conversation ID is null.");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistvaccinecenter.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView chat_txtten;
        TextView chat_txtdichi;
        TextView chat_txtworktime;
        TextView chat_hotline;
        ImageView chat_image_center;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            chat_txtten = itemView.findViewById(R.id.textViewVaccineCenterName);
            chat_txtdichi = itemView.findViewById(R.id.textViewAddress);
            chat_txtworktime = itemView.findViewById(R.id.textViewWorkingTime);
            chat_hotline = itemView.findViewById(R.id.textViewHotLine);
            chat_image_center = itemView.findViewById(R.id.imgchinh);

        }
    }
}
