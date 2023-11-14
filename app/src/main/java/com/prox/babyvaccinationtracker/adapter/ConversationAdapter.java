package com.prox.babyvaccinationtracker.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.ChatActivity;
import com.prox.babyvaccinationtracker.ChatActivity_user;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Conversation;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private List<Conversation> conversations; // hiển thị tất cả các cuộc trò chuyện

    public ConversationAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
//        Log.i("conversation", "onBindViewHolder:  " + conversation.toString());
        // Hiển thị thông tin cuộc trò chuyện trên ViewHolder ở đây.
        // Ví dụ:
        Log.i("adapter", "onBindViewHolder: " + conversation.getUsers());
        if(!conversation.getUsers().containsKey("bot")){
            for(Map.Entry<String, Boolean> a : conversation.getUsers().entrySet()){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center").child(a.getKey());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String center_name = snapshot.child("center_name").getValue(String.class);
                            holder.userNameTextView.setText(center_name);
                            conversation.setConversation_name(center_name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }else {
            holder.userNameTextView.setText("bot");
            conversation.setConversation_name("Bot");
        }



        holder.lastMessageTextView.setText(conversation.getLastMessage().getMess_content());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent;
                 if(conversation.getUsers().containsKey("bot")){
                     intent = new Intent(v.getContext(), ChatActivity.class);
                     intent.putExtra("conversation_id", conversation.getConversation_id());
                     Log.i("CONVERSATIONNN",""+conversation.getUsers());
                     v.getContext().startActivity(intent);
                 }else {
                     intent = new Intent(v.getContext(), ChatActivity_user.class);
                     intent.putExtra("conversation_id", conversation.getConversation_id());
                     Log.i("CONVERSATIONNN",""+conversation.getUsers());
                     v.getContext().startActivity(intent);
                 }


             }
        });
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public void search (String name, List<Conversation> conversations_orgin){
        int size = conversations_orgin.size();
        List<Conversation> conversations_filter = new ArrayList<>();
        Log.i("conversation_origin", conversations_orgin+"");
        if(!name.isEmpty()){
            conversations.clear();
            for(int i = 0 ; i < size ; i ++){
                if(removeDiacritics(conversations_orgin.get(i).getConversation_name().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))){
                    conversations_filter.add(conversations_orgin.get(i));
                }
            }
            conversations = new ArrayList<>(conversations_filter);
            Log.i("conversation_filter", conversations+"");
        }else {
            conversations = new ArrayList<>(conversations_orgin);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }


    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView lastMessageTextView;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
        }
    }
}

